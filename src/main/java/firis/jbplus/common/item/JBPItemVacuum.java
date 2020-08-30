package firis.jbplus.common.item;

import jp.mc.ancientred.jbrobot.model.wavefrontsample.ModelThruster;

/**
 * F型バキュームモデル
 * @author firis-games
 *
 */
public class JBPItemVacuum extends JBPItemBase {
	/**
	 * スラスターモデルを設定
	 */
	@Override
	public IJBModel getIJBModel(int paramInt) {
		return ModelThruster.instance;
	}
	
	/**
	 * アクション名を設定
	 */
	@Override
	public String getAction() {
		return "vacuum_f";
	}
}