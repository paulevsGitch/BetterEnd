package ru.betterend.entity.model;

import java.util.function.Function;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.world.entity.Entity;
import net.minecraft.resources.ResourceLocation;

public abstract class BlockBenchModel<T extends Entity> extends EntityModel<T> {
	public BlockBenchModel() {
		super();
	}

	public BlockBenchModel(Function<ResourceLocation, RenderLayer> function) {
		super(function);
	}

	protected void setRotationAngle(ModelPart modelRenderer, float x, float y, float z) {
		modelRenderer.pitch = x;
		modelRenderer.yaw = y;
		modelRenderer.roll = z;
	}
}
