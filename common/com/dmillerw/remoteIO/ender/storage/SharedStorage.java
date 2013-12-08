package com.dmillerw.remoteIO.ender.storage;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public abstract class SharedStorage {

	public abstract SharedStorage generateEmpty();
	
	public abstract StorageType getType();
	
//	public abstract boolean extract(TileEntity tile);
	
	public abstract boolean push(TileEntity tile);
	
	public abstract void writeToNBT(NBTTagCompound nbt);
	
	public abstract void readFromNBT(NBTTagCompound nbt);
	
	public static enum StorageType {
		ITEM, FLUID, ENERGY_MJ, ENERGY_RF, AE_GRID;
	}
	
}
