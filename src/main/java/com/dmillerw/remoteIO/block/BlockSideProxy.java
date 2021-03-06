package com.dmillerw.remoteIO.block;

import com.dmillerw.remoteIO.block.tile.TileSideProxy;
import com.dmillerw.remoteIO.core.CreativeTabRIO;
import com.dmillerw.remoteIO.lib.ModInfo;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockSideProxy extends BlockContainer {

	public static float MIN = 0.1875F;
	public static float MAX = 0.8125F;
	
	public Icon[] icons;
	
	public BlockSideProxy(int id) {
		super(id, Material.iron);
		
		this.setHardness(5F);
		this.setResistance(1F);
		this.setBlockBounds(MIN, MIN, MIN, MAX, MAX, MAX);
		this.setCreativeTab(CreativeTabRIO.tab);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public Icon getBlockTexture(IBlockAccess world, int x, int y, int z, int side) {
		TileSideProxy tile = (TileSideProxy) world.getBlockTileEntity(x, y, z);

		if (tile != null && tile.fullyValid()) {
			return this.icons[1];
		} else {
			return this.icons[0];
		}
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public Icon getIcon(int side, int meta) {
		return this.icons[0];
	}
	
	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}
	
	@Override
	public boolean isOpaqueCube() {
		return false;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void registerIcons(IconRegister register) {
		this.icons = new Icon[2];
		
		this.icons[1] = register.registerIcon(ModInfo.RESOURCE_PREFIX + "other/active");
		this.icons[0] = register.registerIcon(ModInfo.RESOURCE_PREFIX + "other/inactive");
	}
	
	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileSideProxy();
	}

}
