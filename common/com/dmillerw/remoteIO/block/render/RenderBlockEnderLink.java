package com.dmillerw.remoteIO.block.render;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import com.dmillerw.remoteIO.block.tile.TileEnderLink;
import com.dmillerw.remoteIO.client.model.AdvancedModel;
import com.dmillerw.remoteIO.client.model.ModelEnderLink;
import com.dmillerw.remoteIO.client.model.Texture;

public class RenderBlockEnderLink extends TileEntitySpecialRenderer implements IItemRenderer {

	public final AdvancedModel modelEnderLink;
	
	public RenderBlockEnderLink() {
		this.modelEnderLink = new AdvancedModel(new ModelEnderLink(), Texture.ENDER_LINK);
	}
	
	public void renderEnderLinkAt(TileEnderLink tile, double x, double y, double z, float partial) {
		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_LIGHTING);
		
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
		
		/* RESET ANGLES */
		this.modelEnderLink.rotatePart("arm1", 0F, 0);
		this.modelEnderLink.rotatePart("arm1angle", 37F, 0);
		this.modelEnderLink.rotatePart("arm2", 0, 1);
		this.modelEnderLink.rotatePart("arm2angle", -37F, 1);
		this.modelEnderLink.rotatePart("arm3", 0, 0);
		this.modelEnderLink.rotatePart("arm3angle", -37F, 0);
		this.modelEnderLink.rotatePart("arm4", 0, 1);
		this.modelEnderLink.rotatePart("arm4angle", 37F, 1);
		
		/* THEN MODIFY */
		this.modelEnderLink.rotatePart("arm1", (float)(Math.toRadians(tile.clampAngle)), 0);
		this.modelEnderLink.rotatePart("arm1angle", (float)(Math.toRadians(tile.clampAngle) + 37), 0);
		this.modelEnderLink.rotatePart("arm2", -(float)(Math.toRadians(tile.clampAngle)), 1);
		this.modelEnderLink.rotatePart("arm2angle", -(float)(Math.toRadians(tile.clampAngle) + 37), 1);
		this.modelEnderLink.rotatePart("arm3", -(float)(Math.toRadians(tile.clampAngle)), 0);
		this.modelEnderLink.rotatePart("arm3angle", -(float)(Math.toRadians(tile.clampAngle) + 37), 0);
		this.modelEnderLink.rotatePart("arm4", (float)(Math.toRadians(tile.clampAngle)), 1);
		this.modelEnderLink.rotatePart("arm4angle", (float)(Math.toRadians(tile.clampAngle) + 37), 1);
		
		this.modelEnderLink.renderAll();
		
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glPopMatrix();
	}
	
	@Override
	public void renderTileEntityAt(TileEntity tileentity, double d0, double d1, double d2, float f) {
		renderEnderLinkAt((TileEnderLink) tileentity, d0, d1, d2, f);
	}

	/* IITEMRENDERER */
	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		return true;
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
		return true;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_LIGHTING);
		
		switch(type) {
		case ENTITY: {
			GL11.glRotated(180, 1, 0, 0);
			GL11.glTranslated(0, -2.25, 0);
			GL11.glScaled(2, 2, 2);
			break;
		}
		case EQUIPPED: {
			GL11.glRotated(200, 1, 0, 0);
			GL11.glRotated(-35, 0, 1, 0);
			GL11.glTranslated(.25, -1.80, -1);
			GL11.glScaled(1.5, 1.5, 1.5);
			break;
		}
		case EQUIPPED_FIRST_PERSON: {
			GL11.glRotated(180, 1, 0, 0);
			GL11.glRotated(25, 0, 1, 0);
			GL11.glTranslated(0, -2.25, -.85);
			break;
		}
		case INVENTORY: {
			GL11.glRotated(180, 1, 0, 0);
			GL11.glRotated(180, 0, 1, 0);
			GL11.glScaled(1.5, 1.5, 1.5);
			GL11.glTranslated(-.25, -1.2, 0);
			break;
		}
		default: break;
		}
		
		this.modelEnderLink.renderAll();
		
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glPopMatrix();
	}

}
