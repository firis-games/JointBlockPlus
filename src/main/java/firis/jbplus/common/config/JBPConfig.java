package firis.jbplus.common.config;

import java.io.File;

import firis.jbplus.JointBlockPlus;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * JointBlockPlus設定
 * @author firis-games
 *
 */
public class JBPConfig {
	
	protected static final String GROUP_GENERAL = "GENERAL";
	
	/**
	 * JointBlockのbypathEJRenderByStickyTileEntity設定を差し替え
	 */
	public static boolean cfg_gen_override_bypathEJRenderByStickyTileEntity = false;
	
	/**
	 * 装置系ブロックの範囲設定
	 */
	public static double cfg_gen_device_block_range = 2.5D;
	
	/**
	 * EN：RFレート設定
	 * 標準は1EN:1000RF
	 */
	public static int cfg_gen_en_rate = 1000;
	
	/**
	 * 設定ファイル読込
	 * @param configFile
	 */
	public static void init(FMLPreInitializationEvent event) {
		
		File configFile = new File(event.getModConfigurationDirectory(), "JointBlockPlus.cfg");
		Configuration config = new Configuration(configFile, JointBlockPlus.VERSION, true);
		config.load();
		
		//グループコメント
		config.addCustomCategoryComment(GROUP_GENERAL, "Modに含まれる各機能の設定ができます。");
		
		//JointBlockのbypathEJRenderByStickyTileEntityの代わり
		cfg_gen_override_bypathEJRenderByStickyTileEntity = config.getBoolean("bypathEJRenderByStickyTileEntity", GROUP_GENERAL, false,
				"JointBlock設定のbypathEJRenderByStickyTileEntityの代わりに適応されます。");
		
		//設置系ブロックの範囲
		cfg_gen_device_block_range = config.getFloat("DeviceBlockRange", GROUP_GENERAL, 2.5F, 0.1F, 5F, 
				"装置系ブロックの有効範囲の半径を設定します。");
		
		//ENのRate設定
		cfg_gen_en_rate = config.getInt("EN_Rate", GROUP_GENERAL, 1000, 1, 100000, 
				"ENとRFの変換レートを設定します。1EN:1000RFが標準となります。");
		
		config.save();
	}
}
