package com.dmillerw.remoteIO.ender.storage;

import net.minecraft.nbt.NBTTagCompound;
import cofh.api.energy.IEnergyStorage;

public class SharedEnergyRF extends SharedStorage implements IEnergyStorage {

	public int currentEnergyStored;
	public int maxEnergyStored;
	
	@Override
	public SharedStorage generateEmpty() {
		this.currentEnergyStored = 0;
		this.maxEnergyStored = 1000;
		return this;
	}

	@Override
	public StorageType getType() {
		return StorageType.ENERGY_RF;
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		nbt.setInteger("energy", this.currentEnergyStored);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		this.currentEnergyStored = nbt.getInteger("energy");
	}

	/* IENERGYSTORAGE */
	@Override
	public int receiveEnergy(int maxReceive, boolean simulate) {
		int energyReceived = Math.min(maxEnergyStored - currentEnergyStored, maxReceive);
		if (!simulate) {
			currentEnergyStored += energyReceived;
		}
		return energyReceived;
	}

	@Override
	public int extractEnergy(int maxExtract, boolean simulate) {
		int energyExtracted = Math.min(currentEnergyStored, maxExtract);
		if (!simulate) {
			currentEnergyStored -= energyExtracted;
		}
		return energyExtracted;
	}

	@Override
	public int getEnergyStored() {
		return this.currentEnergyStored;
	}

	@Override
	public int getMaxEnergyStored() {
		return this.maxEnergyStored;
	}
	
}
