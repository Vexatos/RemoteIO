package com.dmillerw.remoteIO.ender.storage;

import buildcraft.api.power.IPowerEmitter;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerHandler;
import buildcraft.api.power.PowerHandler.PowerReceiver;
import buildcraft.api.power.PowerHandler.Type;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

public class SharedEnergyMJ extends SharedStorage implements IPowerReceptor {

	public PowerHandler handler;
	
	@Override
	public SharedStorage generateEmpty() {
		this.handler = new PowerHandler(this, Type.STORAGE);
		return this;
	}

	@Override
	public StorageType getType() {
		return StorageType.ENERGY_MJ;
	}

	@Override
	public boolean push(TileEntity tile) {
		return false;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		this.handler.writeToNBT(nbt, "power");
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		this.handler = new PowerHandler(this, Type.STORAGE);
		this.handler.readFromNBT(nbt, "power");
	}

	@Override
	public PowerReceiver getPowerReceiver(ForgeDirection side) {
		return this.handler.getPowerReceiver();
	}

	@Override
	public void doWork(PowerHandler workProvider) {}

	@Override
	public World getWorld() { return null; }

}
