package com.dmillerw.remoteIO.client.model;

public class AdvancedModel {

	public final ModelTechne model;
	
	public final Texture texture;
	
	public AdvancedModel(ModelTechne model, Texture texture) {
		this.model = model;
		this.texture = texture;
	}
	
	public void render() {
		this.texture.bind();
		this.model.render(0.0625F);
		this.model.renderLast(0.0625F);
	}
	
}
