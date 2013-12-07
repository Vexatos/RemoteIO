package com.dmillerw.remoteIO.block;

import net.minecraft.world.World;

import com.dmillerw.remoteIO.block.tile.TileCore;
import com.dmillerw.remoteIO.block.tile.TileEnderLink;

import cpw.mods.fml.common.FMLCommonHandler;

public class BlockEnder extends BlockCore {

	public static final String[] INTERNAL_NAMES = new String[] {"enderLink"};
	public static final String[] NAMES = new String[] {"Ender Link"};
	
	public BlockEnder(int id) {
		super(id);
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}
	
	@Override
	public boolean isOpaqueCube() {
		return false;
	}
	
	@Override
	public int getRenderType() {
		return -1;
	}
	
	@Override
	public TileCore getTile(int meta) {
		return new TileEnderLink();
	}

}
