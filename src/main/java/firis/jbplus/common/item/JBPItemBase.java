package firis.jbplus.common.item;

import java.util.List;

import javax.annotation.Nullable;

import jp.mc.ancientred.jbrobot.JBRobotMODContainer;
import jp.mc.ancientred.jointblock.api.IJBModelItem;
import jp.mc.ancientred.jointblock.common.CompatHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * F型アクションモデルのベースクラス
 * @author firis-games
 *
 */
public abstract class JBPItemBase extends Item implements IJBModelItem {

	protected IJBActionHandler itemAction = null;
	
	/**
	 * コンストラクタ
	 */
	public JBPItemBase() {
		this.setCreativeTab(JBRobotMODContainer.jbRobotTab);
	}
	
	/**
	 * アクション名
	 * @return
	 */
	public abstract String getAction();
	
	/**
	 * アクションを生成する
	 */
	@Override
	public IJBActionHandler getIJBActionHandler(int paramInt) {
		if ((itemAction == null) && (JBRobotMODContainer.actionFactory != null)) {
			itemAction = JBRobotMODContainer.actionFactory.createAction(getAction(), null);
		}
		return itemAction;
	}

	@Override
	public double getRenderExpand(int paramInt) {
		return 1.0D;
	}
	
	@Override
	public boolean doesSneakBypassUse(ItemStack stack, IBlockAccess world, BlockPos pos, EntityPlayer player) {
		return true;
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(CompatHelper.formatLang("jbrobot.info.hasaction", new Object[0]));
    }
}