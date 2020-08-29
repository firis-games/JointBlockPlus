package firis.jbplus.common.item;

import jp.mc.ancientred.jbrobot.model.wavefrontsample.ModelHarvester;

/**
 * F型ハーベスターモデル
 * @author firis-games
 *
 */
public class JBPItemHarvester extends JBPItemBase {
	
	/**
	 * ハーベスターモデルを設定
	 */
	@Override
	public IJBModel getIJBModel(int paramInt) {
		return ModelHarvester.instance;
	}
	
	/**
	 * アクション名を設定
	 */
	@Override
	public String getAction() {
		return "harvest_f";
	}
}