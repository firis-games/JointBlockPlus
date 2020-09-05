package firis.jbplus.common.tileentity;

import java.util.List;

import javax.annotation.Nullable;

import firis.jbplus.common.capability.TileEntityEnergyStorage;
import firis.jbplus.common.config.JBPConfig;
import firis.jbplus.common.helper.JBPlusHelper;
import jp.mc.ancientred.jointblock.entity.EntityJointed;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

/**
 * 補給装置
 * 
 * @author firis-games
 *
 */
public class JBPTileSupplyDevice extends AbstractTileEntity implements ITickable {

	/**
	 * updateTick
	 */
	protected int updateTick = 0;

	/**
	 * Energy管理用
	 */
	public TileEntityEnergyStorage energyStorage;

	/**
	 * コンストラクタ
	 */
	public JBPTileSupplyDevice() {
		this.energyStorage = new TileEntityEnergyStorage(100000);
	}

	/**
	 * NBTを読み込みクラスへ反映する処理
	 */
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		// Energy
		this.energyStorage.deserializeNBT(compound);
	}

	/**
	 * クラスの情報をNBTへ反映する処理
	 */
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound = super.writeToNBT(compound);
		// Energy
		compound.merge(this.energyStorage.serializeNBT());
		return compound;
	}

	@Override
	public boolean hasCapability(net.minecraftforge.common.capabilities.Capability<?> capability,
			@Nullable net.minecraft.util.EnumFacing facing) {
		if (capability == CapabilityEnergy.ENERGY) {
			return true;
		}
		return super.hasCapability(capability, facing);
	}

	@Override
	@Nullable
	public <T> T getCapability(net.minecraftforge.common.capabilities.Capability<T> capability,
			@Nullable net.minecraft.util.EnumFacing facing) {
		if (capability == CapabilityEnergy.ENERGY) {
			return CapabilityEnergy.ENERGY.cast(this.energyStorage);
		}
		return super.getCapability(capability, facing);

	}

	/**
	 * 20tickに1度処理を行う
	 */
	@Override
	public void update() {

		if (world.isRemote) return;

		// カウンター処理
		this.updateTick++;
		
		// 充電処理
		if (this.updateTick % 2 != 0) return;
		this.updateChargeEnergy();
		
		if (this.updateTick % 20 != 0) return;
		
		//赤石チェック
		if (isRedStonePower()) return;

		// 対象のJBEntityを取得
		double range = JBPConfig.cfg_gen_device_block_range;
		List<EntityJointed> jbEntityList = this.world.getEntitiesWithinAABB(EntityJointed.class,
				(new AxisAlignedBB(this.pos)).grow(range));
		if (jbEntityList.size() == 0) return;

		// RFを取得する
		this.updateEnergyToJBEntity(jbEntityList);

		// 燃料スロットへ追加
		this.updateFuelItemToJBEntity(jbEntityList);

	}

	/**
	 * 外部Energyのチャージ
	 */
	protected void updateChargeEnergy() {
		
		if (this.energyStorage.isMaxEnergyStored()) return;

		for (EnumFacing facing : EnumFacing.VALUES) {
			TileEntity tile = this.world.getTileEntity(this.getPos().offset(facing));
			if (tile == null) continue;
			IEnergyStorage storage = tile.getCapability(CapabilityEnergy.ENERGY, facing.getOpposite());
			if (storage == null) continue;
			// チャージする
			if (this.energyStorage.chargeEnergy(storage, 1000)) break;
		}

	}

	/**
	 * エネルギーを直接補給する
	 * 
	 * @param tile
	 * @param jbEntityList
	 */
	protected void updateEnergyToJBEntity(List<EntityJointed> jbEntityList) {
		
		// 1000RF = 1EN
		int energyRF = JBPConfig.cfg_gen_en_rate;

		// 1回あたり10ENまで
		float energyUnit = 10;

		// エネルギーを補給していく
		for (EntityJointed jbEntity : jbEntityList) {

			// エネルギーが足りない場合は何もしない
			if (this.energyStorage.getEnergyStored() < energyRF)
				break;

			// エネルギーがいっぱいでない場合
			if (jbEntity.getEnergy() < jbEntity.getMaxEnergy()) {

				// 1回あたりの補給量
				float supplyUnit = Math.min(jbEntity.getMaxEnergy() - jbEntity.getEnergy(), energyUnit);
				int supplyRF = ((int) Math.ceil(supplyUnit)) * energyRF;

				int simExtractRF = this.energyStorage.extractEnergy(supplyRF, true);

				// エネルギー取り出し
				int extractRF = ((int) Math.floor((float) simExtractRF / (float) energyRF)) * energyRF;
				extractRF = this.energyStorage.extractEnergy(extractRF, false);

				jbEntity.setEnergy(jbEntity.getEnergy() + extractRF / energyRF);
			}
		}
	}

	/**
	 * 燃料スロットへの追加処理
	 */
	protected void updateFuelItemToJBEntity(List<EntityJointed> jbEntityList) {

		TileEntity tile = this.world.getTileEntity(this.getPos().up());
		if (tile == null) return;
		
		// チェストを取得する
		IItemHandler itemHandler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN);
		if (itemHandler != null) {

			for (int slot = 0; slot < itemHandler.getSlots(); slot++) {

				ItemStack invStack = itemHandler.getStackInSlot(slot);
				invStack = itemHandler.extractItem(slot, invStack.getCount(), true);

				// 燃料チェック
				if (TileEntityFurnace.isItemFuel(invStack) && !invStack.isEmpty()) {

					// 燃料の数
					int invStackCount = invStack.getCount();

					// Entityに対して処理を行う
					for (EntityJointed jbEntity : jbEntityList) {
						IInventory jbInventory = jbEntity.getInnerInventory();
						if (jbInventory == null) {
							continue;
						}
						// 燃料を挿入する
						invStack = JBPlusHelper.insertItemStackToFuelInventory(jbInventory, invStack);
						if (invStack.isEmpty()) {
							break;
						}
					}
					// 一つでも挿入している場合はチェストから引き出し
					if (invStackCount != invStack.getCount()) {
						itemHandler.extractItem(slot, invStackCount - invStack.getCount(), false);
						break;
					}
				}
			}
		}
	}

}
