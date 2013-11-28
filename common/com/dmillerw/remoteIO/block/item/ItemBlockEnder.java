package com.dmillerw.remoteIO.block.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

import com.dmillerw.remoteIO.block.BlockEnder;
import com.dmillerw.remoteIO.block.tile.TileCore;

public class ItemBlockEnder extends ItemBlock {

	public ItemBlockEnder(int id) {
		super(id);
	}

	public int getMetadata(int damage) {
		return damage;
	}

	public String getUnlocalizedName(ItemStack stack) {
		return super.getUnlocalizedName() + BlockEnder.INTERNAL_NAMES[stack.getItemDamage()];
	}
	
	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float i, float d, float k) {
		super.onItemUse(stack, player, world, x, y, z, side, i, d, k);

		ForgeDirection sideForge = ForgeDirection.getOrientation(side);
		x += sideForge.offsetX;
		y += sideForge.offsetY;
		z += sideForge.offsetZ;

		TileEntity tile = world.getBlockTileEntity(x, y, z);

		if (tile != null && tile instanceof TileCore) {
			((TileCore) tile).onBlockAdded(side);
		}

		return true;
	}

}
