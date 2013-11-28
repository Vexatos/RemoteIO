package com.dmillerw.remoteIO.block.render;

import org.lwjgl.opengl.GL11;

import com.dmillerw.remoteIO.block.tile.TileEnderLink;
import com.dmillerw.remoteIO.client.model.AdvancedModel;
import com.dmillerw.remoteIO.client.model.ModelEnderLink;
import com.dmillerw.remoteIO.client.model.Texture;

import net.minecraft.client.renderer.tileentity.RenderEndPortal;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityEndPortal;

public class RenderBlockEnderLink extends TileEntitySpecialRenderer {

	public final AdvancedModel modelEnderLink;
	
	public RenderBlockEnderLink() {
		this.modelEnderLink = new AdvancedModel(new ModelEnderLink(), Texture.ENDER_LINK);
	}
	
	public void renderEnderLinkAt(TileEnderLink tile, double x, double y, double z, float partial) {
		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_LIGHTING);
		
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
		GL11.glTranslated(x + 0.5F, y + 1.5, z + 0.5);
		GL11.glRotated(180, 1, 0, 0);
		
		switch(tile.orientation) {
		case NORTH: {
			GL11.glRotated(0, 0, 0, 0);
			break;
		}
		case DOWN: {
			GL11.glRotated(-90, 1, 0, 0);
			GL11.glTranslated(0, -1, 1);
			break;
		}
		case EAST: {
			GL11.glRotated(90, 0, 1, 0);
			break;
		}
		case SOUTH: {
			GL11.glRotated(180, 0, 1, 0);
			break;
		}
		case UP: {
			GL11.glRotated(90, 1, 0, 0);
			GL11.glTranslated(0, -1, -1);
			break;
		}
		case WEST: {
			GL11.glRotated(-90, 0, 1, 0);
			break;
		}
		default: break;
		}
		
		this.modelEnderLink.render();
		
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glPopMatrix();
	}
	
	@Override
	public void renderTileEntityAt(TileEntity tileentity, double d0, double d1, double d2, float f) {
		renderEnderLinkAt((TileEnderLink) tileentity, d0, d1, d2, f);
	}

}
