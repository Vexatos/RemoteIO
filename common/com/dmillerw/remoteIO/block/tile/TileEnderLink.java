package com.dmillerw.remoteIO.block.tile;

import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.tile.IEnergySource;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import net.minecraftforge.fluids.IFluidTank;

import com.dmillerw.remoteIO.block.render.EnderLinkRenderHelper;
import com.dmillerw.remoteIO.block.render.EnderLinkRenderHelper.BlockDetail;
import com.dmillerw.remoteIO.core.IOLogger;
import com.dmillerw.remoteIO.ender.IEnderLink;
import com.dmillerw.remoteIO.ender.storage.SharedRegistry;
import com.dmillerw.remoteIO.ender.storage.SharedStorage.StorageType;
import com.dmillerw.remoteIO.item.ItemUpgrade.Upgrade;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TileEnderLink extends TileCore implements IEnderLink, IFluidHandler {

	public static final float ANGLE_MODIFIER = 90F;
	public static final float ANGLE_MIN = 60 - ANGLE_MODIFIER;
	public static final float ANGLE_MAX = 125F - ANGLE_MODIFIER;
	
	@SideOnly(Side.CLIENT)
	public float targetAngle = 0F;
	
	@SideOnly(Side.CLIENT)
	public float clampAngle = 0;
	
	public int frequency = -1;
	
	public boolean isIC2 = false;
	
	public ForgeDirection orientation = ForgeDirection.UNKNOWN;
	
	@Override
	public void updateEntity() {
		if (worldObj.isRemote) {
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
		}
	}
	
	@Override
	public void onBlockAdded(int side) {
		this.orientation = ForgeDirection.getOrientation(side).getOpposite();
		this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
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
	public IFluidTank getSharedTank() {
		return (IFluidTank) SharedRegistry.instance().getStorage(StorageType.FLUID, this);
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
		return true;
	}

	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid) {
		return true;
	}

	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from) {
		return new FluidTankInfo[] {getSharedTank().getInfo()};
	}

}
