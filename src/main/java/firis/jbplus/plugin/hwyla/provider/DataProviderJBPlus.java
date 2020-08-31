package firis.jbplus.plugin.hwyla.provider;

import java.util.List;

import firis.jbplus.common.tileentity.JBPTileSupplyDevice;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * 補給装置描画用
 * 
 * @author firis-games
 *
 */
public class DataProviderJBPlus implements IWailaDataProvider {

	/**
	 * ハートの下に描画する
	 */
	@Override
	public List<String> getWailaBody(ItemStack itemStack, List<String> tooltip, IWailaDataAccessor accessor,
			IWailaConfigHandler config) {

		if (!(accessor.getTileEntity() instanceof JBPTileSupplyDevice)) return tooltip;
		
		NBTTagCompound nbtData = accessor.getNBTData();
		tooltip.add(String.format("%d / %d RF", nbtData.getInteger("w_energy"), nbtData.getInteger("w_max_energy")));
		
		return tooltip;
	}

	@Override
	public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world, BlockPos pos) {

		// 初期化
		if (tag == null) tag = new NBTTagCompound();

		if (!(te instanceof JBPTileSupplyDevice)) return tag;

		JBPTileSupplyDevice teSupplyDevice = (JBPTileSupplyDevice) te;

		tag.setInteger("w_energy", teSupplyDevice.energyStorage.getEnergyStored());
		tag.setInteger("w_max_energy", teSupplyDevice.energyStorage.getMaxEnergyStored());
		
		return tag;

	}

}
