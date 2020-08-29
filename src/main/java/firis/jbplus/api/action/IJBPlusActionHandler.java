package firis.jbplus.api.action;

import java.util.Map;

import jp.mc.ancientred.jointblock.api.IJBModelItem;

/**
 * JBPlusアクションインターフェース
 * @author firis-games
 *
 */
public interface IJBPlusActionHandler extends IJBModelItem.IJBActionHandler {
	
	/**
	 * カスタムパラメータ設定
	 */
	public void setProperties(Map<String, Object> properties);

}
