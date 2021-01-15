package ru.betterend.entity.model;

import com.google.common.collect.ImmutableList;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.CompositeEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import ru.betterend.entity.EndSlimeEntity;
import ru.betterend.util.MHelper;

public class EndSlimeEntityModel<T extends EndSlimeEntity> extends CompositeEntityModel<T> {
	private final ModelPart flower;
	private final ModelPart crop;
	private final ModelPart innerCube;
	private final ModelPart rightEye;
	private final ModelPart leftEye;
	private final ModelPart mouth;
	
	public EndSlimeEntityModel(boolean onlyShell) {
		super(RenderLayer::getEntityCutout);
		
		this.innerCube = new ModelPart(this, 0, 16);
		this.rightEye = new ModelPart(this, 32, 0);
		this.leftEye = new ModelPart(this, 32, 4);
		this.mouth = new ModelPart(this, 32, 8);
		this.flower = new ModelPart(this);
		this.crop = new ModelPart(this);

		if (onlyShell) {
			this.innerCube.setTextureOffset(0, 0);
			this.innerCube.addCuboid(-4.0F, 16.0F, -4.0F, 8.0F, 8.0F, 8.0F);
		}
		else {
			this.innerCube.addCuboid(-3.0F, 17.0F, -3.0F, 6.0F, 6.0F, 6.0F);
			this.rightEye.addCuboid(-3.25F, 18.0F, -3.5F, 2.0F, 2.0F, 2.0F);
			this.leftEye.addCuboid(1.25F, 18.0F, -3.5F, 2.0F, 2.0F, 2.0F);
			this.mouth.addCuboid(0.0F, 21.0F, -3.5F, 1.0F, 1.0F, 1.0F);
			
			for (int i = 0; i < 4; i++) {
				ModelPart petalRot = new ModelPart(this);
				petalRot.yaw = MHelper.degreesToRadians(i * 45F);
				
				ModelPart petal = new ModelPart(this, 40, 0);
				petal.setPivot(-4, 8, 0);
				petal.addCuboid(0.0F, 0.0F, 0.0F, 8.0F, 8.0F, 0.0F, 0.0F);
				
				this.flower.addChild(petalRot);
				petalRot.addChild(petal);
			}
			
			for (int i = 0; i < 2; i++) {
				ModelPart petalRot = new ModelPart(this);
				petalRot.yaw = MHelper.degreesToRadians(i * 90F + 45F);
				
				ModelPart petal = new ModelPart(this, 40, 0);
				petal.setPivot(-4, 8, 0);
				petal.addCuboid(0.0F, 0.0F, 0.0F, 8.0F, 8.0F, 0.0F, 0.0F);
				
				this.crop.addChild(petalRot);
				petalRot.addChild(petal);
			}
		}
	}
	
	@Override
	public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {}
	
	public void renderFlower(MatrixStack matrices, VertexConsumer vertices, int light, int overlay) {
		flower.render(matrices, vertices, light, overlay);
	}
	
	public void renderCrop(MatrixStack matrices, VertexConsumer vertices, int light, int overlay) {
		crop.render(matrices, vertices, light, overlay);
	}

	@Override
	public Iterable<ModelPart> getParts() {
		return ImmutableList.of(this.innerCube, this.rightEye, this.leftEye, this.mouth);
	}
}
