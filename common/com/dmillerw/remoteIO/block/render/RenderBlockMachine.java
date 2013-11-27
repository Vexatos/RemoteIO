package com.dmillerw.remoteIO.block.render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;

import org.lwjgl.opengl.GL11;

import com.dmillerw.remoteIO.block.BlockMachine;
import com.dmillerw.remoteIO.block.BlockMachine;
import com.dmillerw.remoteIO.block.BlockMachine;
import com.dmillerw.remoteIO.block.tile.TileHeater;
import com.dmillerw.remoteIO.block.tile.TileReservoir;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class RenderBlockMachine extends BlockRenderer implements ISimpleBlockRenderingHandler {

	public static int renderID;

	static {
		renderID = RenderingRegistry.getNextAvailableRenderId();
	}

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
		if (metadata == 0) {
			GL11.glPushMatrix();
			GL11.glDisable(GL11.GL_ALPHA_TEST);
			block.setBlockBounds(0.1F, 0.1F, 0.1F, 0.9F, 0.9F, 0.9F);
			drawFaces(renderer, block, Block.lavaStill.getBlockTextureFromSide(0), true);
			block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
			renderer.setRenderBoundsFromBlock(block);
			drawFaces(renderer, block, ((BlockMachine)block).iconBars, true);
			GL11.glEnable(GL11.GL_ALPHA_TEST);
			GL11.glPopMatrix();
		} else if (metadata == 1) {
			GL11.glPushMatrix();
			GL11.glDisable(GL11.GL_ALPHA_TEST);
			block.setBlockBounds(0.1F, 0.1F, 0.1F, 0.9F, 0.9F, 0.9F);
			drawFaces(renderer, block, Block.waterStill.getBlockTextureFromSide(0), true);
			block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
			renderer.setRenderBoundsFromBlock(block);
			drawFaces(renderer, block, ((BlockMachine)block).iconFrame, true);
			drawFaces(renderer, block, ((BlockMachine)block).iconGlass, true);
			GL11.glEnable(GL11.GL_ALPHA_TEST);
			GL11.glPopMatrix();
		}
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
		int meta = world.getBlockMetadata(x, y, z);
		
		if (meta == 0) {
			TileHeater tile = (TileHeater) world.getBlockTileEntity(x, y, z);

			block.setBlockBounds(0F, 0F, 0F, 1F, 1F, 1F);
			renderer.setRenderBoundsFromBlock(block);
			Tessellator.instance.setBrightness(setBrightness(world, x, y, z, block));
			renderAllSides(world, x, y, z, block, renderer, ((BlockMachine)block).iconBars);
			
			for (int i = 1; i <= 20; i++) {
				final float adjustConstant = 0.001F;
				block.setBlockBounds(0F + (i * adjustConstant), 0F + (i * adjustConstant), 0F + (i * adjustConstant), 1F - (i * adjustConstant), 1F - (i * adjustConstant), 1F - (i * adjustConstant));
				renderer.setRenderBoundsFromBlock(block);
				Tessellator.instance.setBrightness(setBrightness(world, x, y, z, block));
				renderAllSides(world, x, y, z, block, renderer, ((BlockMachine)block).iconBarsDark);
			}
			
			if (tile != null && tile.hasLava) {
				block.setBlockBounds(0.02F, 0.02F, 0.02F, 0.98F, 0.98F, 0.98F);
				renderer.setRenderBoundsFromBlock(block);
				Tessellator.instance.setBrightness(setBrightness(world, x, y, z, block));
				renderer.renderStandardBlock(block, x, y, z);
				renderer.clearOverrideBlockTexture();
			}
			
			return true;
		} else if (meta == 1) {
			TileReservoir tile = (TileReservoir) world.getBlockTileEntity(x, y, z);

			renderAllSides(world, x, y, z, block, renderer, ((BlockMachine)block).iconFrame);
			
			for (int i = 1; i <= 40; i++) {
				final float adjustConstant = 0.001F;
				block.setBlockBounds(0F + (i * adjustConstant), 0F + (i * adjustConstant), 0F + (i * adjustConstant), 1F - (i * adjustConstant), 1F - (i * adjustConstant), 1F - (i * adjustConstant));
				renderer.setRenderBoundsFromBlock(block);
				Tessellator.instance.setBrightness(setBrightness(world, x, y, z, block));
				renderAllSides(world, x, y, z, block, renderer, ((BlockMachine)block).iconFrameDark);
			}
			
			block.setBlockBounds(0.04F, 0.04F, 0.04F, 0.96F, 0.96F, 0.96F);
			renderer.setRenderBoundsFromBlock(block);
			Tessellator.instance.setBrightness(setBrightness(world, x, y, z, block));
			renderAllSides(world, x, y, z, block, renderer, ((BlockMachine)block).iconGlass);
			
			if (tile != null && tile.hasWater) {
				setBrightness(world, x, y, z, block);
				block.setBlockBounds(0.05F, 0.05F, 0.05F, 0.95F, 0.95F, 0.95F);
				renderer.setRenderBoundsFromBlock(block);
				Tessellator.instance.setBrightness(setBrightness(world, x, y, z, block));
				renderer.renderStandardBlock(block, x, y, z);
				renderer.clearOverrideBlockTexture();
			}
			
			return true;
		}
		
		return false;
	}

	@Override
	public boolean shouldRender3DInInventory() {
		return true;
	}

	@Override
	public int getRenderId() {
		return renderID;
	}

}
