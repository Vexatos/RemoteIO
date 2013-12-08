package com.dmillerw.remoteIO.ender.storage;

import net.minecraft.nbt.NBTTagCompound;

public abstract class SharedStorage {

	public abstract SharedStorage generateEmpty();
	
	public abstract StorageType getType();
	
	public abstract void writeToNBT(NBTTagCompound nbt);
	
	public abstract void readFromNBT(NBTTagCompound nbt);
	
	public static enum StorageType {
		ITEM, FLUID, ENERGY_RF, AE_GRID;
	}
	
}
