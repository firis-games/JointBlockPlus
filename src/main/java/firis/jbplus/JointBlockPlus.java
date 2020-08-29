package firis.jbplus;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import firis.jbplus.api.JointBlockPlusAPI;
import firis.jbplus.common.action.JBPHarvesterAction;
import firis.jbplus.common.action.JBPPlantAction;
import firis.jbplus.common.action.factory.JBPlusActionFactory;
import firis.jbplus.common.item.JBPItemHarvester;
import firis.jbplus.common.item.JBPItemPlanter;
import jp.mc.ancientred.jointblock.config.JBConfig;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod(
		modid = JointBlockPlus.MODID, 
		name = JointBlockPlus.NAME,
		version = JointBlockPlus.VERSION,
		dependencies = JointBlockPlus.MOD_DEPENDENCIES,
		acceptedMinecraftVersions = JointBlockPlus.MOD_ACCEPTED_MINECRAFT_VERSIONS
)
@EventBusSubscriber(modid=JointBlockPlus.MODID)
public class JointBlockPlus {

    public static final String MODID = "jbplus";
    public static final String NAME = "JointBlockPlus";
    public static final String VERSION = "0.1";
    public static final String MOD_DEPENDENCIES = "required-after:forge@[1.12.2-14.23.5.2768,);"
    		+ "required-after:modj_jointblock@[0.8.4,);";
    public static final String MOD_ACCEPTED_MINECRAFT_VERSIONS = "[1.12.2]";
    
    @Instance(MODID)
	public static JointBlockPlus instance;
    
    /** logger */
    public static Logger logger = LogManager.getLogger(MODID);
    
    /** proxy */
//    @SidedProxy(serverSide = "firis.lmavatar.common.proxy.CommonProxy", 
//   		clientSide = "firis.lmavatar.client.proxy.ClientProxy")
//	public static IProxy proxy;
    
    @ObjectHolder(MODID)
    public static class JBPItems {
    	public final static Item MODEL_PLANTER_F = null;
    	public final static Item MODEL_HARVESTER_F = null;
    }
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
    	
    	//ActionFactory初期化
    	JBPlusActionFactory.preInit();
    	
    	//Actionクラスを登録
    	JointBlockPlusAPI.registerActionHandler("plant_f", JBPPlantAction.class);
    	JointBlockPlusAPI.registerActionHandler("harvest_f", JBPHarvesterAction.class);
    	
    	//描画設定を強制的に変更する
    	JBConfig.bypathEJRenderByStickyTileEntity = false;
    	
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event) {}
    
    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {}
    
    /**
     * アイテム登録
     * @param event
     */
    @SubscribeEvent
    protected static void registerItems(RegistryEvent.Register<Item> event) {
    	
    	//F型プランター
    	event.getRegistry().register(new JBPItemPlanter()
    			.setRegistryName(MODID, "model_planter_f")
    			.setUnlocalizedName("model_planter_f"));
    	
    	//F型ハーベスター
    	event.getRegistry().register(new JBPItemHarvester()
    			.setRegistryName(MODID, "model_harvester_f")
    			.setUnlocalizedName("model_harvester_f"));
    	
    }
    
    /**
     * モデル登録
     * @param event
     */
    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    protected static void registerModels(ModelRegistryEvent event) {
    	//Itemsから自動でModelを登録する
    	for (Field filed : JBPItems.class.getFields()) {
    		try {
    			int mod = filed.getModifiers();
    			//final かつ static
    			if (Modifier.isFinal(mod) && Modifier.isStatic(mod)) {
    				Object objItem = filed.get(null);
    				if(objItem instanceof Item) {
    					Item regItem = (Item) objItem;
    					ModelLoader.setCustomModelResourceLocation(regItem, 0,
    			    			new ModelResourceLocation(regItem.getRegistryName(), "inventory"));
    				}
    			}
			} catch (IllegalArgumentException | IllegalAccessException e) {
				logger.info("registerModels Item error " + filed.getName());
			}
    	}

    }
	
}
