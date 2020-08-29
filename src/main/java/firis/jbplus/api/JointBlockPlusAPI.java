package firis.jbplus.api;

import firis.jbplus.api.action.IJBPlusActionHandler;
import firis.jbplus.common.action.factory.JBPlusActionFactory;

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
	 * アクションクラスをインスタンス化する
	 * 失敗した場合はfalseを返却する
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
