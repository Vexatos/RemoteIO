package com.dmillerw.remoteIO.ender.storage;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import appeng.api.DimentionalCoord;
import appeng.api.me.tiles.IGridTeleport;
import appeng.api.me.tiles.IGridTileEntity;
import appeng.api.me.util.IGridInterface;

public class SharedAEGrid extends SharedStorage {

	public IGridInterface grid;
	
	public List<IGridTileEntity> attachedLinks;
	
	@Override
	public SharedStorage generateEmpty() {
		this.grid = null;
		this.attachedLinks = new LinkedList<IGridTileEntity>();
		return this;
	}

	@Override
	public StorageType getType() {
		return StorageType.AE_GRID;
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {}

	public void register(IGridTileEntity gt) {
		this.attachedLinks.add(gt);
	}
	
	public void unregister(IGridTileEntity gt) {
		this.attachedLinks.remove(gt);
	}
	
	public IGridTileEntity[] getAllExcept(IGridTileEntity gt) {
		List<IGridTileEntity> otherLinks = new LinkedList<IGridTileEntity>(this.attachedLinks);
		otherLinks.remove(gt);
		return otherLinks.toArray(new IGridTileEntity[otherLinks.size()]);
	}
	
}
