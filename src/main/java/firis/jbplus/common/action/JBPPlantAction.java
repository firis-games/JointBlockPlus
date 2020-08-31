package firis.jbplus.common.action;

import firis.jbplus.common.entity.JBPFakePlayer;
import jp.mc.ancientred.jointblock.api.IJBEntityState;
import jp.mc.ancientred.jointblock.api.IJBJointParameterHolder;
import jp.mc.ancientred.jointblock.api.IJBVectorTransformer;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * F型プランターモデル
 * @author firis-games
 *
 */
public class JBPPlantAction extends JBPBlockActionBase {
	
	@Override
	public String getActionName() {
		return "jbc.action.plant.name";
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
		if (entityState.getEntityEnergy() < this.useEnergy)
			return;

		// 種チェック
		ItemStack seedStack = this.getInventorySeed(entityState);
		if (seedStack.isEmpty())
			return;

		BlockPos farmlandPos = actionPos.down();
		
		IBlockState workState = world.getBlockState(actionPos);
		IBlockState farmlandState = world.getBlockState(farmlandPos);

		// 種植えチェック
		if (workState.getMaterial() != Material.AIR) {
			return;
		}
		if (!Block.isEqualTo(farmlandState.getBlock(), Blocks.FARMLAND)) {
			return;
		}

		// 問題ない場合は種を設置する
		EnumActionResult action = seedStack.onItemUse(new JBPFakePlayer(world), world, farmlandPos, EnumHand.MAIN_HAND,
				EnumFacing.UP, farmlandPos.getX(), farmlandPos.getY(), farmlandPos.getZ());
		if (EnumActionResult.SUCCESS == action) {
			entityState.useEntityEnergy(useEnergy);
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
