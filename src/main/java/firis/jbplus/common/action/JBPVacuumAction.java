package firis.jbplus.common.action;

import java.util.List;
import java.util.Map;

import firis.jbplus.common.helper.JBPlusHelper;
import jp.mc.ancientred.jointblock.api.IJBEntityState;
import jp.mc.ancientred.jointblock.api.IJBJointParameterHolder;
import jp.mc.ancientred.jointblock.api.IJBVectorTransformer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * F型バキュームモデル
 * 
 * @author firis-games
 *
 */
public class JBPVacuumAction extends JBPBlockActionBase {

	protected static int COOL_TIME_TICK_IDX = 0;

	// 動作CoolTimeTick
	protected int coolTimeTick = 5;

	// アイテム回収範囲
	protected double vacuumRange = 2.0D;

	/**
	 * カスタムプロパティ
	 */
	@Override
	public void setProperties(Map<String, Object> properties) {

		super.setProperties(properties);

		// クールタイム設定
		if (properties.containsKey("coolTimeTick")) {
			this.coolTimeTick = JBPlusHelper.parseInt(properties.get("coolTimeTick"), this.coolTimeTick);
		}

		// 回収範囲
		if (properties.containsKey("range")) {
			this.vacuumRange = JBPlusHelper.parseDouble(properties.get("range"), this.vacuumRange);
		}

	}

	@Override
	public String getActionName() {
		return "jbaction.vacuum.name";
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

		// CoolTime設定
		if (!joint.hasInitExtraActionVars()) {
			joint.initExtraActionVars(COOL_TIME_TICK_IDX, -1, 0, Integer.MAX_VALUE);
		}
		if (joint.getExtraActionVars(COOL_TIME_TICK_IDX) > 0) {
			return;
		}

		// インベントリチェック
		IInventory inventory = entityState.getInnerInventory();
		if (inventory == null)
			return;

		// アイテム検索
		List<EntityItem> itemList = world.getEntitiesWithinAABB(EntityItem.class,
				(new AxisAlignedBB(actionPos)).grow(vacuumRange));
		for (EntityItem entityItem : itemList) {

			if (entityItem.cannotPickup())
				continue;
			ItemStack item = entityItem.getItem();
			item = JBPlusHelper.insertItemStackToInteractInventory(inventory, item);
			if (item.isEmpty()) {
				entityItem.setDead();
			} else {
				entityItem.setItem(item);
				entityItem.setPickupDelay(10);
			}
		}

		// クールタイム設定
		joint.setExtraActionVars(COOL_TIME_TICK_IDX, coolTimeTick);
	}

}
