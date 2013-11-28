package com.dmillerw.remoteIO.block.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.ForgeDirection;

public class TileEnderLink extends TileCore {

	public ForgeDirection orientation = ForgeDirection.UNKNOWN;
	
	@Override
	public void onBlockAdded(int side) {
		this.orientation = ForgeDirection.getOrientation(side).getOpposite();
		this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
	}
	
	@Override
	public void writeCustomNBT(NBTTagCompound nbt) {
		nbt.setByte("orientation", (byte) this.orientation.ordinal());
	}

	@Override
	public void readCustomNBT(NBTTagCompound nbt) {
		this.orientation = ForgeDirection.values()[nbt.getByte("orientation")];
	}

}
