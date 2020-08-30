package firis.jbplus.common.helper;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

/**
 * ヘルパークラス
 * @author firis-games
 *
 */
public class JBPlusHelper {
	
	/**
	 * float型へ変換
	 * @param value
	 * @param defVal
	 * @return
	 */
	public static float parseFloat(Object value, float defVal) {
		float ret = defVal;
		if (value != null) {
			try {
				ret = Float.parseFloat(value.toString());
			} catch (Exception e) {}
		}
		return ret;
	}
	
	/**
	 * double型へ変換
	 * @param value
	 * @param defVal
	 * @return
	 */
	public static double parseDouble(Object value, double defVal) {
		double ret = defVal;
		if (value != null) {
			try {
				ret = Double.parseDouble(value.toString());
			} catch (Exception e) {}
		}
		return ret;
	}
	
	/**
	 * int型へ変換
	 * @param value
	 * @param defVal
	 * @return
	 */
	public static int parseInt(Object value, int defVal) {
		int ret = defVal;
		if (value != null) {
			try {
				ret = (int) Math.floor(parseDouble(value, (double) defVal));
			} catch (Exception e) {}
		}
		return ret;
	}
	
	/**
	 * IInventoryへアイテムを挿入する
	 * @return
	 */
	public static ItemStack insertItemStackToInventory(IInventory inventory, ItemStack stack, int start, int end) {
		//指定スロットの間を走査する
		for (int slot = start; slot < end && !stack.isEmpty(); slot++) {
			ItemStack slotStack = inventory.getStackInSlot(slot);
			if (slotStack.isEmpty()) {
				// 空の場合はそのまま設定
				inventory.setInventorySlotContents(slot, stack.copy());
				stack.shrink(stack.getCount());
			} else if (ItemStack.areItemsEqual(slotStack, stack)) {
				// アイテムが一致する場合は重ねる
				if (slotStack.getCount() + stack.getCount() <= slotStack.getMaxStackSize()) {
					slotStack.grow(stack.getCount());
					stack.shrink(stack.getCount());
					inventory.setInventorySlotContents(slot, slotStack);
				} else {
					int shrink = slotStack.getMaxStackSize() - slotStack.getCount();
					slotStack.grow(shrink);
					stack.shrink(stack.getCount());
					inventory.setInventorySlotContents(slot, slotStack);
				}
			}
		}
		return stack;
	}
	
	/**
	 * インタラクトインベントリ
	 * @return
	 */
	public static ItemStack insertItemStackToInteractInventory(IInventory inventory, ItemStack stack) {
		return insertItemStackToInventory(inventory, stack, 0, 18);
	}

}
