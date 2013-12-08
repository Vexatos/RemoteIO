package com.dmillerw.remoteIO.ender.storage;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class SharedInventory extends SharedStorage implements IInventory {

	public ItemStack[] inventoryContents;
	
	@Override
	public SharedStorage generateEmpty() {
		inventoryContents = new ItemStack[1]; // Shared inventory limited to one stack
		return this;
	}

	@Override
	public StorageType getType() {
		return StorageType.ITEM;
	}
	
	@Override
	public boolean push(TileEntity tile) {
		return false;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		NBTTagCompound item = new NBTTagCompound();
		if (inventoryContents[0] != null) {
			inventoryContents[0].writeToNBT(item);
		} else {
			item.setBoolean("null", true);
		}
		nbt.setTag("inventory", item);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		this.inventoryContents = new ItemStack[1];
		
		if (!nbt.hasKey("null")) {
			this.inventoryContents[0] = ItemStack.loadItemStackFromNBT(nbt.getCompoundTag("item"));
		}
	}

	@Override
	public int getSizeInventory() {
		return 1;
	}

	@Override
	public ItemStack getStackInSlot(int i) {
		return inventoryContents[i];
	}

	@Override
	public ItemStack decrStackSize(int i, int j) {
		if (this.inventoryContents[i] != null) {
			ItemStack itemstack;

			if (this.inventoryContents[i].stackSize <= j) {
				itemstack = this.inventoryContents[i];
				this.inventoryContents[i] = null;
				this.onInventoryChanged();
				return itemstack;
			} else {
				itemstack = this.inventoryContents[i].splitStack(j);

				if (this.inventoryContents[i].stackSize == 0) {
					this.inventoryContents[i] = null;
				}

				this.onInventoryChanged();
				return itemstack;
			}
		} else {
			return null;
		}
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int i) {
		ItemStack stack = inventoryContents[i];
		if (stack != null) {
			return stack;
		}
		return null;
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack) {
		inventoryContents[i] = itemstack;
		if (inventoryContents[i] != null && inventoryContents[i].stackSize > getInventoryStackLimit()) {
			inventoryContents[i].stackSize = getInventoryStackLimit();
		}
	}

	@Override
	public String getInvName() { return "Shared Inventory"; }

	@Override
	public boolean isInvNameLocalized() { return false; }

	@Override
	public int getInventoryStackLimit() { return 64; }

	@Override
	public void onInventoryChanged() {}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer) { return false; }

	@Override
	public void openChest() {}

	@Override
	public void closeChest() {}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack) { return true; }

}
