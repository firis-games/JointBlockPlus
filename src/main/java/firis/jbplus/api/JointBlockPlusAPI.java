package firis.jbplus.api;

import java.lang.reflect.Field;
import java.util.ArrayList;

import firis.jbplus.api.action.IJBPlusActionHandler;
import firis.jbplus.common.action.factory.JBPlusActionFactory;
import jp.mc.ancientred.jbrobot.item.merchant.DefaultModelMerchantPackage;
import jp.mc.ancientred.jbrobot.item.merchant.DefaultModelMerchantPackage.DefaultModelMerchantEntry;
import jp.mc.ancientred.jbrobot.item.merchant.IJBRMerchantEntry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * JointBlock公開API
 * @author firis-games
 *
 */
public class JointBlockPlusAPI {
	
	/**
	 * JBアクションを追加する
	 */
	public static boolean registerActionHandler(String actionId, Class<? extends IJBPlusActionHandler> actionHandler) {
		//アクションクラスのチェック
		IJBPlusActionHandler instance = newInstanceAction(actionHandler);
		if (instance == null) return false;
		
		//リストへ登録
		JBPlusActionFactory.jbPlusActionHandlers.put(actionId, actionHandler);
		return true;
	}
	
	/**
	 * デフォルトモデルカタログ登録
	 * 
	 * postInitのタイミングで使用すること
	 * @param item
	 * @return
	 */
	public static boolean registerDefaultModelMerchantPackage(Item item) {
		//登録
		return registerDefaultModelMerchantPackage(new ItemStack(item));
	}

	/**
	 * デフォルトモデルカタログ登録
	 * 
	 * postInitのタイミングで使用すること
	 * @param stack
	 * @return
	 */
	public static boolean registerDefaultModelMerchantPackage(ItemStack stack) {
		ArrayList<IJBRMerchantEntry> register = getMerchantModelsEntryList();
		if (register == null) return false;
		//登録
		register.add(new DefaultModelMerchantEntry(stack.copy()));
		return true;
	}
	
	/**
	 * デフォルトモデルカタログリストのインスタンス
	 */
	private static ArrayList<IJBRMerchantEntry> merchantModelsEntryList = null;
	
	/**
	 * デフォルトモデルカタログリストのインスタンスをリフレクションで取得する
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static ArrayList<IJBRMerchantEntry> getMerchantModelsEntryList() {
		if (merchantModelsEntryList == null) {
			DefaultModelMerchantPackage defaultModelMerchantPackage = DefaultModelMerchantPackage.getInstanceUnSafe();
			try {
				Field field = DefaultModelMerchantPackage.class.getDeclaredField("merchantModelsEntryList");
				field.setAccessible(true);
				merchantModelsEntryList = (ArrayList<IJBRMerchantEntry>) field.get(defaultModelMerchantPackage);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return merchantModelsEntryList;
	}
	
	/**
	 * アクションクラスをインスタンス化する
	 * 失敗した場合はnullを返却する
	 * @param clazz
	 * @return
	 */
	public static <T extends IJBPlusActionHandler> T newInstanceAction(Class<T> clazz) {
		T instance = null;
		try {
			instance = clazz.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return instance;
	}

}
