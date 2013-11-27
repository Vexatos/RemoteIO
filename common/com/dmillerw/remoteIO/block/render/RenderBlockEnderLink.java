package com.dmillerw.remoteIO.block.render;

import org.lwjgl.opengl.GL11;

import com.dmillerw.remoteIO.block.tile.TileEnderLink;
import com.dmillerw.remoteIO.client.model.AdvancedModel;
import com.dmillerw.remoteIO.client.model.ModelEnderLink;
import com.dmillerw.remoteIO.client.model.Texture;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

public class RenderBlockEnderLink extends TileEntitySpecialRenderer {

	public final AdvancedModel modelEnderLink;
	
	public RenderBlockEnderLink() {
		this.modelEnderLink = new AdvancedModel(new ModelEnderLink(), Texture.ENDER_LINK);
	}
	
	public void renderEnderLinkAt(TileEnderLink tile, double x, double y, double z, float partial) {
		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_LIGHTING);
		
		GL11.glTranslated(x + 0.5, y + 1.5, z + 0.5);
		GL11.glRotated(180, 1, 0, 0);
		
		this.modelEnderLink.render();
		
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glPopMatrix();
	}
	
	@Override
	public void renderTileEntityAt(TileEntity tileentity, double d0, double d1, double d2, float f) {
		renderEnderLinkAt((TileEnderLink) tileentity, d0, d1, d2, f);
	}

}
