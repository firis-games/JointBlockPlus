package firis.jbplus.common.action.factory;

import java.util.HashMap;
import java.util.Map;

import firis.jbplus.api.JointBlockPlusAPI;
import firis.jbplus.api.action.IJBPlusActionHandler;
import jp.mc.ancientred.jointblock.JBModContainer;
import jp.mc.ancientred.jointblock.action.JBActionFactory;
import jp.mc.ancientred.jointblock.api.IJBModelItem;

/**
 * JBActionFactoryへの割込み用クラス
 * @author firis-games
 *
 */
public class JBPlusActionFactory extends JBActionFactory {
	
	/**
	 * アクション
	 */
	public static Map<String, Class<? extends IJBPlusActionHandler>> jbPlusActionHandlers = new HashMap<>();
	
	/**
	 * JointBlockのActionFactoryの差し替え
	 */
	public static void preInit() {
    	JBModContainer.actionFactory = new JBPlusActionFactory();
	}

	/**
	 * 拡張版createAction
	 */
	public IJBModelItem.IJBActionHandler createAction(String actionTypeName, Map<String, Object> properties) {
		
		//拡張Actionチェック
		for (String actionId : jbPlusActionHandlers.keySet()) {
			if (actionId.equals(actionTypeName)) {
				IJBPlusActionHandler actionHandler = JointBlockPlusAPI.newInstanceAction(jbPlusActionHandlers.get(actionId));
				if (properties != null) {
					actionHandler.setProperties(properties);
				}
				return actionHandler;
			}
		}
		
		//標準アクション
		return super.createAction(actionTypeName, properties);
	}
}
