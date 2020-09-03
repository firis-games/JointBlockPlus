package firis.jbplus.common.tileentity;

import java.util.List;

import firis.jbplus.common.helper.JBPlusHelper;
import jp.mc.ancientred.jointblock.entity.EntityJointed;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

/**
 * 集荷装置
 * 
 * @author firis-games
 *
 */
public class JBPTileCargoCollectionDevice extends AbstractTileEntity implements ITickable {

	/**
	 * updateTick
	 */
	protected int updateTick = 0;

	/**
	 * コンストラクタ
	 */
	public JBPTileCargoCollectionDevice() {
	}

	/**
	 * 20tickに1度処理を行う
	 */
	@Override
	public void update() {

		if (world.isRemote) return;

		// カウンター処理
		this.updateTick++;
		
		if (this.updateTick % 20 != 0) return;
		TileEntity tile = this.world.getTileEntity(this.getPos().up());
		if (tile == null) return;

		// 対象のJBEntityを取得
		List<EntityJointed> jbEntityList = this.world.getEntitiesWithinAABB(EntityJointed.class,
				(new AxisAlignedBB(this.pos)).grow(2.5D));
		if (jbEntityList.size() == 0) return;

		// アイテム回収
		this.updateCargoCollectionFromJBEntity(tile, jbEntityList);

	}
	
	/**
	 * アイテム回収処理
	 */
	protected void updateCargoCollectionFromJBEntity(TileEntity tile, List<EntityJointed> jbEntityList) {

		// チェストを取得する
		IItemHandler itemHandler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN);
		if (itemHandler != null) {
			for (EntityJointed jbEntity : jbEntityList) {
				IInventory jbInventory = jbEntity.getInnerInventory();
				if (jbInventory == null) {
					continue;
				}
				
				//空きスロットにアイテムを挿入する
				for (int slot = 0; slot < 45; slot++) {
					ItemStack invStack = jbInventory.getStackInSlot(slot);
					if (!invStack.isEmpty()) {
						invStack = JBPlusHelper.insertItemStackToIItemHandler(itemHandler, invStack);
						jbInventory.setInventorySlotContents(slot, invStack);
						if(invStack.isEmpty()) {
							break;
						}
					}
				}
			}
		}
	}

}
