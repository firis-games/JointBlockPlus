package firis.jbplus.common.block;

import java.util.List;

import javax.annotation.Nullable;

import firis.jbplus.common.tileentity.JBPTileCargoCollectionDevice;
import jp.mc.ancientred.jointblock.JBModContainer;
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
 * 集荷装置ブロック
 * @author firis-games
 *
 */
public class JBPBlockCargoCollectionDevice extends AbstractBlockContainer {

	/**
	 * コンストラクタ
	 * @param materialIn
	 */
	public JBPBlockCargoCollectionDevice() {
		super(Material.PISTON);
		this.setHardness(5.0F);
		this.setResistance(20.0F);
		this.setCreativeTab(JBModContainer.jointBlockTab);
	}

	/**
	 * TileEntity
	 */
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new JBPTileCargoCollectionDevice();
	}
	
	/**
	 * info設定
	 */
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
		tooltip.add(TextFormatting.LIGHT_PURPLE + I18n.format("tile.cargo_collection_device.info"));
    }
}
