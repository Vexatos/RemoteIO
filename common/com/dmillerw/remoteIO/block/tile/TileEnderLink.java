package com.dmillerw.remoteIO.block.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.ForgeDirection;

public class TileEnderLink extends TileCore {

	public ForgeDirection orientation = ForgeDirection.DOWN;
	
	@Override
	public void writeCustomNBT(NBTTagCompound nbt) {
		nbt.setByte("orientation", (byte) this.orientation.ordinal());
	}

	@Override
	public void readCustomNBT(NBTTagCompound nbt) {
		this.orientation = ForgeDirection.values()[nbt.getByte("orientation")];
	}

}
