package firis.jbplus.common.capability;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;

/**
 * 簡易エネルギー管理クラス
 * @author firis-games
 *
 */
public class TileEntityEnergyStorage extends EnergyStorage {

	public TileEntityEnergyStorage(int capacity) {
		super(capacity);
	}
	
	/**
	 * EnergyStorageをNBT化
	 * @return
	 */
    public NBTTagCompound serializeNBT() {
    	NBTTagCompound nbt = new NBTTagCompound();
    	nbt.setInteger("energy", this.energy);
    	nbt.setInteger("capacity", this.capacity);
    	nbt.setInteger("maxReceive", this.maxReceive);
    	nbt.setInteger("maxExtract", this.maxExtract);
        return nbt;
    }

    /**
     * NBTからEnergyStorageへ反映
     * @param nbt
     */
    public void deserializeNBT(NBTTagCompound nbt) {
    	this.energy = nbt.getInteger("energy");
    	this.capacity = nbt.getInteger("capacity");
    	this.maxReceive = nbt.getInteger("maxReceive");
    	this.maxExtract = nbt.getInteger("maxExtract");
    }
    
    /**
     * IEnergyStorageからエネルギーを取得する
     * @param energyStorage
     * @return
     */
    public boolean chargeEnergy(IEnergyStorage energyStorage, int amount) {
    	
    	//エネルギーなし
    	int simExtract = energyStorage.extractEnergy(amount, true);
    	if (simExtract == 0) return false;
    	
    	//残容量なし
    	int simReceive = this.receiveEnergy(simExtract, true);
    	if (simReceive == 0) return false;
    	
    	//実行
    	int energy = Math.min(simExtract, simReceive);
    	energyStorage.extractEnergy(energy, false);
    	this.receiveEnergy(energy, false);
    	
    	return true;
    }
    
    /**
     * 満充電状態かのチェック
     * @return
     */
    public boolean isMaxEnergyStored() {
    	return this.getMaxEnergyStored() <= this.getEnergyStored();
    }
    
}
