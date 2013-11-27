package com.dmillerw.remoteIO.client.model;

import com.dmillerw.remoteIO.lib.ModInfo;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public enum Texture {

	ENDER_LINK("enderLink");
	
	public final ResourceLocation texture;
	
	private Texture(String path) {
		this.texture = new ResourceLocation(ModInfo.RESOURCE_PREFIX + "/textures/model/" + path + ".png");
	}
	
	public void bind() {
		Minecraft.getMinecraft().renderEngine.bindTexture(this.texture);
	}
	
}
