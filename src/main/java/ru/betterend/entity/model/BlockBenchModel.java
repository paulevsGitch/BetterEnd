package ru.betterend.entity.model;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

import java.util.function.Function;

public abstract class BlockBenchModel<T extends Entity> extends EntityModel<T> {
	public BlockBenchModel() {
		super();
	}
	
	public BlockBenchModel(Function<ResourceLocation, RenderType> function) {
		super(function);
	}
	
	protected void setRotationAngle(ModelPart modelRenderer, float x, float y, float z) {
		modelRenderer.xRot = x;
		modelRenderer.yRot = y;
		modelRenderer.zRot = z;
	}
}
