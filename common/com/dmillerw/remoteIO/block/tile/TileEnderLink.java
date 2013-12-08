package com.dmillerw.remoteIO.block.tile;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import net.minecraftforge.fluids.IFluidTank;
import appeng.api.DimentionalCoord;
import appeng.api.WorldCoord;
import appeng.api.events.GridTileLoadEvent;
import appeng.api.events.GridTileUnloadEvent;
import appeng.api.me.tiles.IGridTeleport;
import appeng.api.me.tiles.IGridTileEntity;
import appeng.api.me.util.IGridInterface;

import com.dmillerw.remoteIO.block.render.EnderLinkRenderHelper;
import com.dmillerw.remoteIO.block.render.EnderLinkRenderHelper.BlockDetail;
import com.dmillerw.remoteIO.ender.IEnderLink;
import com.dmillerw.remoteIO.ender.storage.SharedAEGrid;
import com.dmillerw.remoteIO.ender.storage.SharedRegistry;
import com.dmillerw.remoteIO.ender.storage.SharedStorage.StorageType;
import com.dmillerw.remoteIO.item.ItemUpgrade.Upgrade;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TileEnderLink extends TileCore implements IEnderLink, IInventory, ISidedInventory, IFluidHandler, IGridTileEntity, IGridTeleport {

	public static final float ANGLE_MODIFIER = 90F;
	public static final float ANGLE_MIN = 60 - ANGLE_MODIFIER;
	public static final float ANGLE_MAX = 125F - ANGLE_MODIFIER;
	
	@SideOnly(Side.CLIENT)
	public float targetAngle = 0F;
	
	@SideOnly(Side.CLIENT)
	public float clampAngle = 0;
	
	public int frequency = -1;
	
	/* AE SPECIFIC */
	public IGridInterface grid;
	public boolean aeEventFired = false;
	
	public ForgeDirection orientation = ForgeDirection.UNKNOWN;
	
	@Override
	public void updateEntity() {
		if (worldObj.isRemote) { // CLIENT
			if (targetAngle > ANGLE_MAX) {
				targetAngle = ANGLE_MAX;
			}
			
			if (targetAngle < ANGLE_MIN) {
				targetAngle = ANGLE_MIN;
			}
			
			if (clampAngle > targetAngle) {
				clampAngle -= 1;
			} else if (clampAngle < targetAngle) {
				clampAngle += 1;
			}
		} else { // SERVER
			if (!aeEventFired) {
				MinecraftForge.EVENT_BUS.post(new GridTileLoadEvent(this, this.worldObj, this.getLocation())); //TODO Removal events
				getSharedAEGrid().register(this);
				aeEventFired = true;
			}
		}
	}
	
	@Override
	public void onBlockAdded(int side) {
		this.orientation = ForgeDirection.getOrientation(side).getOpposite();
		this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
	}
	
	@Override
	public void onBlockBreak() {
		if (aeEventFired) {
			MinecraftForge.EVENT_BUS.post(new GridTileUnloadEvent(this, this.worldObj, getLocation()));
			getSharedAEGrid().unregister(this);
			aeEventFired = false;
		}
	}
	
	@Override
	public void onChunkUnload() {
		super.onChunkUnload();
		
		if (aeEventFired) {
			MinecraftForge.EVENT_BUS.post(new GridTileUnloadEvent(this, this.worldObj, getLocation()));
			getSharedAEGrid().unregister(this);
			aeEventFired = false;
		}
	}
	
	@Override
	public void invalidate() {
		super.invalidate();
		
		if (aeEventFired) {
			MinecraftForge.EVENT_BUS.post(new GridTileUnloadEvent(this, this.worldObj, getLocation()));
			getSharedAEGrid().unregister(this);
			aeEventFired = false;
		}
	}
	
	@Override
	public void onNeighborBlockUpdate() {
		float angle = 0F;
		
		int x = this.xCoord + orientation.offsetX;
		int y = this.yCoord + orientation.offsetY;
		int z = this.zCoord + orientation.offsetZ;
		
		if (!this.worldObj.isAirBlock(x, y, z) && this.worldObj.blockHasTileEntity(x, y, z)) {
			TileEntity tileAt = this.worldObj.getBlockTileEntity(x, y, z);
			BlockDetail detail = new BlockDetail(tileAt);
			
			angle = EnderLinkRenderHelper.getAngle(detail);
		} else {
			angle = 0F;
		}
		
		NBTTagCompound tag = new NBTTagCompound();
		tag.setFloat("angleOverride", angle);
		sendClientUpdate(tag);
	}
	
	@Override
	public void writeCustomNBT(NBTTagCompound nbt) {
		nbt.setInteger("freq", this.frequency);
		nbt.setByte("orientation", (byte) this.orientation.ordinal());
	}

	@Override
	public void readCustomNBT(NBTTagCompound nbt) {
		this.frequency = nbt.getInteger("freq");
		this.orientation = ForgeDirection.values()[nbt.getByte("orientation")];
	}

	@Override
	public void onClientUpdate(NBTTagCompound tag) {
		if (tag.hasKey("angleOverride")) {
			this.targetAngle = tag.getFloat("angleOverride");
		}
	}
	
	/* STORAGE INTERACTION METHODS */
	public IInventory getSharedInventory() {
		return (IInventory) SharedRegistry.instance().getStorage(StorageType.ITEM, this);
	}
	
	public IFluidTank getSharedTank() {
		return (IFluidTank) SharedRegistry.instance().getStorage(StorageType.FLUID, this);
	}
	
	public SharedAEGrid getSharedAEGrid() {
		return (SharedAEGrid) SharedRegistry.instance().getStorage(StorageType.AE_GRID, this);
	}
	
	/* IENDERLINK */
	@Override
	public int getFrequency() {
		return this.frequency;
	}

	@Override
	public boolean hasUpgrade(Upgrade upgrade) {
		return false;
	}

	/* IINVENTORY */
	@Override
	public int getSizeInventory() {
		return 1;
	}

	@Override
	public ItemStack getStackInSlot(int i) {
		return getSharedInventory().getStackInSlot(i);
	}

	@Override
	public ItemStack decrStackSize(int i, int j) {
		return getSharedInventory().decrStackSize(i, j);
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int i) {
		return getSharedInventory().getStackInSlotOnClosing(i);
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack) {
		getSharedInventory().setInventorySlotContents(i, itemstack);
	}

	public String getInvName() { return null; }
	public boolean isInvNameLocalized() { return false; }
	public int getInventoryStackLimit() { return 64; }
	public boolean isUseableByPlayer(EntityPlayer entityplayer) { return false; }
	public void openChest() {}
	public void closeChest() {}
	public boolean isItemValidForSlot(int i, ItemStack itemstack) { return true; }
	
	/* ISIDEDINVENTORY */
	@Override
	public int[] getAccessibleSlotsFromSide(int var1) {
		return ForgeDirection.getOrientation(var1) == this.orientation ? new int[] {0} : new int[0];
	}

	@Override
	public boolean canInsertItem(int i, ItemStack itemstack, int j) {
		return ForgeDirection.getOrientation(i) == this.orientation;
	}

	@Override
	public boolean canExtractItem(int i, ItemStack itemstack, int j) {
		return ForgeDirection.getOrientation(i) == this.orientation;
	}
	
	/* IFLUIDHANDLER */
	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
		return from == this.orientation ? getSharedTank().fill(resource, doFill) : 0;
	}

	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
		return drain(from, resource.amount, doDrain);
	}

	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
		return from == this.orientation ? getSharedTank().drain(maxDrain, doDrain) : null;
	}

	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid) {
		return from == this.orientation;
	}

	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid) {
		return from == this.orientation;
	}

	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from) {
		return from == this.orientation ? new FluidTankInfo[] {getSharedTank().getInfo()} : new FluidTankInfo[0];
	}

	/* IGRIDTILEENTITY */
	@Override
	public WorldCoord getLocation() {
		return new WorldCoord(xCoord, yCoord, zCoord);
	}

	@Override
	public DimentionalCoord[] findRemoteSide() {
		List<IGridTileEntity> otherLinks = Arrays.asList(getSharedAEGrid().getAllExcept(this));
		List<DimentionalCoord> linkCoords = new LinkedList<DimentionalCoord>();
		for (IGridTileEntity tile : otherLinks) {
			linkCoords.add(new DimentionalCoord(tile.getWorld(), tile.getLocation().x, tile.getLocation().y, tile.getLocation().z));
		}
		return linkCoords.toArray(new DimentionalCoord[linkCoords.size()]);
	}
	
	@Override
	public boolean isValid() { return true; }

	@Override
	public void setPowerStatus(boolean hasPower) {}

	@Override
	public boolean isPowered() { return true; }

	@Override
	public IGridInterface getGrid() {
		return this.grid;
	}

	@Override
	public void setGrid(IGridInterface gi) {
		this.grid = gi;
	}

	@Override
	public World getWorld() {
		return this.worldObj;
	}

}
