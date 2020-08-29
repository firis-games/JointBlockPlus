package firis.jbplus.common.item;

import jp.mc.ancientred.jbrobot.model.wavefrontsample.ModelPlanter;

/**
 * F型プランターモデル
 * @author firis-games
 *
 */
public class JBPItemPlanter extends JBPItemBase {
	/**
	 * プランターモデルを設定
	 */
	@Override
	public IJBModel getIJBModel(int paramInt) {
		return ModelPlanter.instance;
	}
	
	/**
	 * アクション名を設定
	 */
	@Override
	public String getAction() {
		return "plant_f";
	}
}