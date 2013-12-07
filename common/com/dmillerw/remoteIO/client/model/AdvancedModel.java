package com.dmillerw.remoteIO.client.model;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import com.dmillerw.remoteIO.core.IOLogger;

import net.minecraft.client.model.ModelRenderer;

public class AdvancedModel {

	public static final float SCALE = 0.0625F;
	
	public final ModelTechne model;
	
	public final Texture texture;
	
	public AdvancedModel(ModelTechne model, Texture texture) {
		this.model = model;
		this.texture = texture;
	}
	
	public void renderAll() {
		this.texture.bind();
		this.model.renderAll();
	}
	
	public void renderAllExcept(String ... except) {
		List<String> parts = Arrays.asList(except);
		
		this.texture.bind();
		for (Field part : this.model.getClass().getDeclaredFields()) {
			try {
				if (!parts.contains(part.getName())) {
					ModelRenderer mPart = (ModelRenderer)part.get(this.model);
					mPart.render(SCALE);
				}
			} catch(IllegalAccessException ex) {
				IOLogger.warn("Failed to get model part! No access allowed! (WTF?)");
			}
		}
	}
	
	public void renderOnly(String ... only) {
		this.texture.bind();
		for (String part : only) {
			try {
				Field fPart = this.model.getClass().getDeclaredField(part);
				ModelRenderer mPart = (ModelRenderer)fPart.get(this.model);
				mPart.render(SCALE);
			} catch (NoSuchFieldException e) {
				IOLogger.warn("Failed to get model part " + part + ". It doesn't exist!");
			} catch (Exception e) {
				IOLogger.warn("Encountered general error when accessing part " + part + ".");
				IOLogger.warn("Reason: " + e.getLocalizedMessage());
			}
		}
	}
	
	/** Sets the definied part rotation on the definied axis to the defined RADIAN value */
	public void rotatePart(String part, float rad, int switchByte) {
		try {
			Field fPart = this.model.getClass().getDeclaredField(part);
			ModelRenderer mPart = (ModelRenderer)fPart.get(this.model);
			switch(switchByte) {
			case 0: mPart.rotateAngleX = rad; break;
			case 1: mPart.rotateAngleY = rad; break;
			case 2: mPart.rotateAngleZ = rad; break;
			default: break;
			}
		} catch (NoSuchFieldException e) {
			IOLogger.warn("Failed to get model part " + part + ". It doesn't exist!");
		} catch(IllegalAccessException ex) {
			IOLogger.warn("Failed to get model part! No access allowed! (WTF?)");
		}
	}
	
}
