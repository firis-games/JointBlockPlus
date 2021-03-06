package firis.jbplus;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import firis.jbplus.api.JointBlockPlusAPI;
import firis.jbplus.common.action.JBPHarvesterAction;
import firis.jbplus.common.action.JBPPlantAction;
import firis.jbplus.common.action.JBPVacuumAction;
import firis.jbplus.common.action.factory.JBPlusActionFactory;
import firis.jbplus.common.block.JBPBlockCargoCollectionDevice;
import firis.jbplus.common.block.JBPBlockSupplyDevice;
import firis.jbplus.common.config.JBPConfig;
import firis.jbplus.common.item.JBPItemHarvester;
import firis.jbplus.common.item.JBPItemPlanter;
import firis.jbplus.common.item.JBPItemVacuum;
import firis.jbplus.common.tileentity.JBPTileCargoCollectionDevice;
import firis.jbplus.common.tileentity.JBPTileSupplyDevice;
import jp.mc.ancientred.jointblock.config.JBConfig;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
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
import net.minecraftforge.fml.common.registry.GameRegistry;
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
//    @SidedProxy(serverSide = "firis.jbplus.common.proxy.CommonProxy", 
//   		clientSide = "firis.jbplus.client.proxy.ClientProxy")
//	public static IProxy proxy;
    
    @ObjectHolder(MODID)
    public static class JBPBlocks {
    	public final static Block SUPPLY_DEVICE = null;
    	public final static Block CARGO_COLLECTION_DEVICE = null;
    }
    
    @ObjectHolder(MODID)
    public static class JBPItems {
    	public final static Item MODEL_PLANTER_F = null;
    	public final static Item MODEL_HARVESTER_F = null;
    	public final static Item MODEL_VACUUM_F = null;
    }
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
    	
    	//Config初期化
    	JBPConfig.init(event);
    	
    	//ActionFactory初期化
    	JBPlusActionFactory.preInit();
    	
    	//Actionクラスを登録
    	JointBlockPlusAPI.registerActionHandler("plant_f", JBPPlantAction.class);
    	JointBlockPlusAPI.registerActionHandler("harvest_f", JBPHarvesterAction.class);
    	JointBlockPlusAPI.registerActionHandler("vacuum_f", JBPVacuumAction.class);
    	
    	//描画設定を強制的に変更する
    	JBConfig.bypathEJRenderByStickyTileEntity = JBPConfig.cfg_gen_override_bypathEJRenderByStickyTileEntity;
    	
    	//TileEntity登録
    	GameRegistry.registerTileEntity(JBPTileSupplyDevice.class, 
        		new ResourceLocation(MODID, "te_supply_device"));
    	GameRegistry.registerTileEntity(JBPTileCargoCollectionDevice.class, 
        		new ResourceLocation(MODID, "te_cargo_collection_device"));
    	
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event) {}
    
    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
    	
    	//デフォルトモデルカタログ登録
    	JointBlockPlusAPI.registerDefaultModelMerchantPackage(JBPItems.MODEL_PLANTER_F);
    	JointBlockPlusAPI.registerDefaultModelMerchantPackage(JBPItems.MODEL_HARVESTER_F);
    	JointBlockPlusAPI.registerDefaultModelMerchantPackage(JBPItems.MODEL_VACUUM_F);
    	
    }
    
    
    /**
     * ブロックを登録*/
    @SubscribeEvent
    protected static void registerBlocks(RegistryEvent.Register<Block> event) {
    	
    	// 補給装置
        event.getRegistry().register(
                new JBPBlockSupplyDevice()
                .setRegistryName(MODID, "supply_device")
                .setUnlocalizedName("supply_device")
        );
        // 集荷装置
        event.getRegistry().register(
                new JBPBlockCargoCollectionDevice()
                .setRegistryName(MODID, "cargo_collection_device")
                .setUnlocalizedName("cargo_collection_device")
        );
    }
    
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
    	
    	//F型バキューム
    	event.getRegistry().register(new JBPItemVacuum()
    			.setRegistryName(MODID, "model_vacuum_f")
    			.setUnlocalizedName("model_vacuum_f"));
    	
    	//補給装置
    	event.getRegistry().register(new ItemBlock(JBPBlocks.SUPPLY_DEVICE)
    			.setRegistryName(MODID, "supply_device"));
    	
    	//集荷装置
    	event.getRegistry().register(new ItemBlock(JBPBlocks.CARGO_COLLECTION_DEVICE)
    			.setRegistryName(MODID, "cargo_collection_device"));
    	
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
    	
    	//Blocksから自動でModelを登録する
    	for (Field filed : JBPBlocks.class.getFields()) {
    		try {
    			int mod = filed.getModifiers();
    			//final かつ static
    			if (Modifier.isFinal(mod) && Modifier.isStatic(mod)) {
    				if(filed.get(null) instanceof Block) {
    					Block regBlock = (Block) filed.get(null);
    					ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(regBlock), 0,
    			    			new ModelResourceLocation(regBlock.getRegistryName(), "inventory"));
    				}
    			}
			} catch (IllegalArgumentException | IllegalAccessException e) {
				logger.info("registerModels Block error " + filed.getName());
			}
    	}

    }
	
}
