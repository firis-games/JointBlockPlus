package firis.jbplus.common.block;

import java.util.List;

import javax.annotation.Nullable;

import firis.jbplus.common.tileentity.JBPTileSupplyDevice;
import jp.mc.ancientred.jbrobot.JBRobotMODContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * 補給装置ブロック
 * @author firis-games
 *
 */
public class JBPBlockSupplyDevice extends AbstractBlockContainer {

	/**
	 * コンストラクタ
	 * @param materialIn
	 */
	public JBPBlockSupplyDevice() {
		super(Material.PISTON);
		this.setHardness(5.0F);
		this.setResistance(20.0F);
		this.setCreativeTab(JBRobotMODContainer.jbRobotTab);
	}

	/**
	 * TileEntity
	 */
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new JBPTileSupplyDevice();
	}
	
	/**
	 * info設定
	 */
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
		tooltip.add(TextFormatting.LIGHT_PURPLE + I18n.format("tile.supply_device.info"));
    }
}
