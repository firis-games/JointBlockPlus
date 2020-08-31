package firis.jbplus.plugin.hwyla;

import firis.jbplus.JointBlockPlus;
import firis.jbplus.common.block.JBPBlockSupplyDevice;
import firis.jbplus.plugin.hwyla.provider.DataProviderJBPlus;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.IWailaRegistrar;
import mcp.mobius.waila.api.WailaPlugin;

@WailaPlugin(value = JointBlockPlus.MODID)
public class WailaPluginJBPlus implements IWailaPlugin {
	
	@Override
	public void register(IWailaRegistrar registrar) {
		// Provider登録
		DataProviderJBPlus dataProvider = new DataProviderJBPlus();
		registrar.registerBodyProvider(dataProvider, JBPBlockSupplyDevice.class);
		registrar.registerNBTProvider(dataProvider, JBPBlockSupplyDevice.class);
	}
	
}
