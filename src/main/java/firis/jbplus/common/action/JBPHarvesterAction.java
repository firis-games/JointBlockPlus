package firis.jbplus.common.action;

import jp.mc.ancientred.jointblock.api.IJBEntityState;
import jp.mc.ancientred.jointblock.api.IJBJointParameterHolder;
import jp.mc.ancientred.jointblock.api.IJBVectorTransformer;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * F型プランターモデル
 * 
 * @author firis-games
 *
 */
public class JBPHarvesterAction extends JBPBlockActionBase {

	@Override
	public String getActionName() {
		return "jbaction.harvest.name";
	}

	/**
	 * Blockに対してのアクションを実行する
	 * 
	 * @param actionPos
	 * @param world
	 * @param entityState
	 * @param entityConducted
	 * @param joint
	 * @param matrixUtil
	 * @return
	 */
	@Override
	public void actionBlock(BlockPos actionPos, World world, IJBEntityState entityState,
			EntityLivingBase entityConducted, IJBJointParameterHolder.IJBJoint joint, IJBVectorTransformer matrixUtil) {
		
		// エネルギーチェック
		if (entityState.getEntityEnergy() < this.useEnergy) {
			return;
		}

		// 収穫物チェック
		boolean isHarvest = false;
		IBlockState workState = world.getBlockState(actionPos);
		if (workState.getBlock() instanceof BlockCrops) {
			// 成長段階チェック
			BlockCrops workCrops = (BlockCrops) workState.getBlock();
			if (workCrops.isMaxAge(workState)) {
				isHarvest = true;
			}
		} else if (workState.getBlock() instanceof BlockBush) {
			// 植物系
			isHarvest = true;
		}

		// 収穫対象の場合は収穫する
		if (isHarvest) {
			NonNullList<ItemStack> drops = NonNullList.create();
			workState.getBlock().getDrops(drops, world, actionPos, workState, 0);
			IInventory inventory = entityState.getInnerInventory();
			if (inventory == null)
				return;

			// ブロック破壊
			world.destroyBlock(actionPos, false);

			for (ItemStack dropStack : drops) {
				// とりあえずワークインベントリに突っ込む
				for (int slot = 0; slot < 18 && !dropStack.isEmpty(); slot++) {
					ItemStack stack = inventory.getStackInSlot(slot);
					if (stack.isEmpty()) {
						// 空の場合はそのまま設定
						inventory.setInventorySlotContents(slot, dropStack.copy());
						dropStack.shrink(dropStack.getCount());
					} else if (ItemStack.areItemsEqual(stack, dropStack)) {
						// アイテムが一致する場合は重ねる
						if (stack.getCount() + dropStack.getCount() <= stack.getMaxStackSize()) {
							stack.grow(dropStack.getCount());
							dropStack.shrink(dropStack.getCount());
							inventory.setInventorySlotContents(slot, stack);
						} else {
							int shrink = stack.getMaxStackSize() - stack.getCount();
							stack.grow(shrink);
							dropStack.shrink(dropStack.getCount());
							inventory.setInventorySlotContents(slot, stack);
						}
					}
				}
				if (!dropStack.isEmpty()) {
					// アイテムとしてEntityを生成する
					InventoryHelper.spawnItemStack(world, actionPos.getX(), actionPos.getY(), actionPos.getX(),
							dropStack);
				}
			}
		}

	}

	/**
	 * インベントリから種を取得する
	 * 
	 * @return
	 */
	protected ItemStack getInventorySeed(IJBEntityState entityState) {

		IInventory targetInventory = entityState.getInnerInventory();
		if (targetInventory == null) {
			return ItemStack.EMPTY;
		}
		for (int i = 0; (i < targetInventory.getSizeInventory()) && (i < 18); i++) {
			ItemStack stack = targetInventory.getStackInSlot(i);
			if ((stack.getItem() instanceof net.minecraftforge.common.IPlantable)
					|| (stack.getItem() instanceof ItemBlock && ((ItemBlock) stack.getItem())
							.getBlock() instanceof net.minecraftforge.common.IPlantable)) {
				return stack;
			}
		}
		return ItemStack.EMPTY;
	}

}
