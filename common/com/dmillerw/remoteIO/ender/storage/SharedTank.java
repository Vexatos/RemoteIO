package com.dmillerw.remoteIO.ender.storage;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidTank;

public class SharedTank extends SharedStorage implements IFluidTank {

	public FluidStack fluid;

	public int capacity;

	@Override
	public SharedStorage generateEmpty() {
		fluid = null;
		capacity = FluidContainerRegistry.BUCKET_VOLUME; // Tank limited to one buckets worth
		return this;
	}

	@Override
	public StorageType getType() {
		return StorageType.FLUID;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		if (!nbt.hasKey("Empty")) {
			fluid = FluidStack.loadFluidStackFromNBT(nbt);
		}
	}

	public void writeToNBT(NBTTagCompound nbt) {
		if (fluid != null) {
			fluid.writeToNBT(nbt);
		} else {
			nbt.setString("Empty", "");
		}
	}

	@Override
	public FluidStack getFluid() {
		return fluid;
	}

	@Override
	public int getFluidAmount() {
		if (fluid == null) {
			return 0;
		}
		System.out.println(fluid.amount);
		return fluid.amount;
	}

	@Override
	public int getCapacity() {
		return capacity;
	}

	@Override
	public FluidTankInfo getInfo() {
		return new FluidTankInfo(this);
	}

	@Override
	public int fill(FluidStack resource, boolean doFill) {
		if (resource == null) {
			return 0;
		}

		if (!doFill) {
			if (fluid == null) {
				return Math.min(capacity, resource.amount);
			}

			if (!fluid.isFluidEqual(resource)) {
				return 0;
			}

			return Math.min(capacity - fluid.amount, resource.amount);
		}

		if (fluid == null) {
			fluid = new FluidStack(resource, Math.min(capacity, resource.amount));
			return fluid.amount;
		}

		if (!fluid.isFluidEqual(resource)) {
			return 0;
		}
		
		int filled = capacity - fluid.amount;

		if (resource.amount < filled) {
			fluid.amount += resource.amount;
			filled = resource.amount;
		} else {
			fluid.amount = capacity;
		}

		return filled;
	}

	@Override
	public FluidStack drain(int maxDrain, boolean doDrain) {
		if (fluid == null) {
			return null;
		}

		int drained = maxDrain;
		if (fluid.amount < drained) {
			drained = fluid.amount;
		}

		FluidStack stack = new FluidStack(fluid, drained);
		if (doDrain) {
			fluid.amount -= drained;
			if (fluid.amount <= 0) {
				fluid = null;
			}
		}
		return stack;
	}

}
