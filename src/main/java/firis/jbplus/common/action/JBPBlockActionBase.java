package firis.jbplus.common.action;

import java.util.Map;

import firis.jbplus.api.action.IJBPlusActionHandler;
import jp.mc.ancientred.jointblock.api.IJBEntityState;
import jp.mc.ancientred.jointblock.api.IJBJointParameterHolder;
import jp.mc.ancientred.jointblock.api.IJBVectorTransformer;
import jp.mc.ancientred.jointblock.common.JBCommonUtil;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * Blockに対する処理を行うActionのベースクラス
 * @author firis-games
 *
 */
public abstract class JBPBlockActionBase implements IJBPlusActionHandler {

	protected float useEnergy = 0.5F;
	
	/**
	 * float型へ変換
	 * @param value
	 * @param defVal
	 * @return
	 */
	protected float parseFloat(Object value, float defVal) {
		float fVal = defVal;
		if (value instanceof Float) {
			fVal = (float) value;
		}
		return fVal;
	}
	
	/**
	 * カスタムプロパティ
	 */
	@Override
	public void setProperties(Map<String, Object> properties) {
		if (properties.containsKey("useEnergy")) {
			this.useEnergy = parseFloat(properties.get("useEnergy"), this.useEnergy);
		}
	}
	
	/**
	 * アクション
	 */
	@Override
	public void action(World world, EntityLivingBase entityLivingBase, EntityLivingBase entityConducted,
			IJBJointParameterHolder.IJBJoint joint, IJBVectorTransformer matrixUtil) {

		// ActionPosの取得
		BlockPos actionPos = this.getActionBlockPos(world, entityLivingBase, entityConducted, joint, matrixUtil);
		if (actionPos == null)
			return;

		// Action実行
		this.actionBlock(actionPos, world, (IJBEntityState) entityLivingBase, entityConducted, joint, matrixUtil);
		
	}
	
	/**
	 * 作業対象のBlockPosを取得する
	 * 
	 * @return
	 */
	public BlockPos getActionBlockPos(World world, EntityLivingBase entityLivingBase, EntityLivingBase entityConducted,
			IJBJointParameterHolder.IJBJoint joint, IJBVectorTransformer matrixUtil) {
		// 動作チェック
		if (entityConducted == null) {
			return null;
		}
		if (!(entityLivingBase instanceof IJBEntityState)) {
			return null;
		}
		if ((entityConducted instanceof EntityPlayer)) {
			EntityPlayer entityPlayer = (EntityPlayer) entityConducted;
			if (!entityPlayer.isAllowEdit()) {
				return null;
			}
		}

		// BlockPos取得
		Vec3d posCenter = JBCommonUtil.createVectorHelper(0.0D, 1.0D, 0.0D);
		posCenter = matrixUtil.transformAbsolute(posCenter);

		return new BlockPos(posCenter.x, posCenter.y, posCenter.z);
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
	public abstract void actionBlock(BlockPos actionPos, World world, IJBEntityState entityState,
			EntityLivingBase entityConducted, IJBJointParameterHolder.IJBJoint joint, IJBVectorTransformer matrixUtil);
	

	
}
