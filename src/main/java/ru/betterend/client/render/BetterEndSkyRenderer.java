package ru.betterend.client.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexBuffer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Matrix4f;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import ru.bclib.util.BackgroundInfo;
import ru.bclib.util.MHelper;
import ru.betterend.BetterEnd;

import java.util.Random;

import net.fabricmc.fabric.api.client.rendering.v1.DimensionRenderingRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;

public class BetterEndSkyRenderer implements DimensionRenderingRegistry.SkyRenderer {
	private static final ResourceLocation NEBULA_1 = BetterEnd.makeID("textures/sky/nebula_2.png");
	private static final ResourceLocation NEBULA_2 = BetterEnd.makeID("textures/sky/nebula_3.png");
	private static final ResourceLocation HORIZON = BetterEnd.makeID("textures/sky/nebula_1.png");
	private static final ResourceLocation STARS = BetterEnd.makeID("textures/sky/stars.png");
	private static final ResourceLocation FOG = BetterEnd.makeID("textures/sky/fog.png");
	
	private VertexBuffer nebula1;
	private VertexBuffer nebula2;
	private VertexBuffer horizon;
	private VertexBuffer stars1;
	private VertexBuffer stars2;
	private VertexBuffer stars3;
	private VertexBuffer stars4;
	private VertexBuffer fog;
	private Vector3f axis1;
	private Vector3f axis2;
	private Vector3f axis3;
	private Vector3f axis4;

	private boolean initalized = false;

	private void initialise() {
		if(!initalized) {
			initStars();
			Random random = new Random(131);
			axis1 = new Vector3f(random.nextFloat(), random.nextFloat(), random.nextFloat());
			axis2 = new Vector3f(random.nextFloat(), random.nextFloat(), random.nextFloat());
			axis3 = new Vector3f(random.nextFloat(), random.nextFloat(), random.nextFloat());
			axis4 = new Vector3f(random.nextFloat(), random.nextFloat(), random.nextFloat());
			axis1.normalize();
			axis2.normalize();
			axis3.normalize();
			axis4.normalize();
			this.initalized = true;
		}
	}

	@Override
	public void render(WorldRenderContext context) {
		if (context.world() == null || context.matrixStack() == null) {
			return;
		}
		
		initialise();
		
		Matrix4f projectionMatrix = context.projectionMatrix();
		PoseStack matrices = context.matrixStack();
		
		float time = ((context.world().getDayTime() + context.tickDelta()) % 360000) * 0.000017453292F;
		float time2 = time * 2;
		float time3 = time * 3;
		
		FogRenderer.levelFogColor();
		RenderSystem.depthMask(false);
		RenderSystem.enableTexture();
		RenderSystem.enableBlend();
		RenderSystem.setShaderColor(1.0F ,1.0F ,1.0F ,1.0F);
		RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		
		float blindA = 1F - BackgroundInfo.blindness;
		float blind02 = blindA * 0.2F;
		float blind06 = blindA * 0.6F;
		
		if (blindA > 0) {
			matrices.pushPose();
			matrices.mulPose(new Quaternion(0, time, 0, false));
			RenderSystem.setShaderTexture(0, HORIZON);
			renderBuffer(matrices, projectionMatrix, horizon, DefaultVertexFormat.POSITION_TEX, 0.77F, 0.31F, 0.73F, 0.7F * blindA);
			matrices.popPose();
			
			matrices.pushPose();
			matrices.mulPose(new Quaternion(0, -time, 0, false));
			RenderSystem.setShaderTexture(0, NEBULA_1);
			renderBuffer(matrices, projectionMatrix, nebula1, DefaultVertexFormat.POSITION_TEX, 0.77F, 0.31F, 0.73F, blind02);
			matrices.popPose();
			
			matrices.pushPose();
			matrices.mulPose(new Quaternion(0, time2, 0, false));
			RenderSystem.setShaderTexture(0, NEBULA_2);
			renderBuffer(matrices, projectionMatrix, nebula2, DefaultVertexFormat.POSITION_TEX, 0.77F, 0.31F, 0.73F, blind02);
			matrices.popPose();
			
			RenderSystem.setShaderTexture(0, STARS);
			
			matrices.pushPose();
			matrices.mulPose(axis3.rotation(time));
			renderBuffer(matrices, projectionMatrix, stars3, DefaultVertexFormat.POSITION_TEX, 0.77F, 0.31F, 0.73F, blind06);
			matrices.popPose();
			
			matrices.pushPose();
			matrices.mulPose(axis4.rotation(time2));
			renderBuffer(matrices, projectionMatrix, stars4, DefaultVertexFormat.POSITION_TEX, 1F, 1F, 1F, blind06);
			matrices.popPose();
		}
		
		float a = (BackgroundInfo.fogDensity - 1F);
		if (a > 0) {
			if (a > 1) a = 1;
			RenderSystem.setShaderTexture(0, FOG);
			renderBuffer(
				matrices,
				projectionMatrix,
				fog,
				DefaultVertexFormat.POSITION_TEX,
				BackgroundInfo.fogColorRed,
				BackgroundInfo.fogColorGreen,
				BackgroundInfo.fogColorBlue,
				a);
		}
		
		RenderSystem.disableTexture();
		
		if (blindA > 0) {
			matrices.pushPose();
			matrices.mulPose(axis1.rotation(time3));
			renderBuffer(matrices, projectionMatrix, stars1, DefaultVertexFormat.POSITION, 1, 1, 1, blind06);
			matrices.popPose();
			
			matrices.pushPose();
			matrices.mulPose(axis2.rotation(time2));
			renderBuffer(matrices, projectionMatrix, stars2, DefaultVertexFormat.POSITION, 0.95F, 0.64F, 0.93F, blind06);
			matrices.popPose();
		}
		
		RenderSystem.enableTexture();
		RenderSystem.depthMask(true);
		RenderSystem.defaultBlendFunc();
		RenderSystem.disableBlend();
	}
	
	private void renderBuffer(PoseStack matrices, Matrix4f matrix4f, VertexBuffer buffer, VertexFormat format, float r, float g, float b, float a) {
		RenderSystem.setShaderColor(r, g, b, a);
		if (format == DefaultVertexFormat.POSITION) {
			buffer.drawWithShader(matrices.last().pose(), matrix4f, GameRenderer.getPositionShader());
		}
		else {
			buffer.drawWithShader(matrices.last().pose(), matrix4f, GameRenderer.getPositionTexShader());
		}
	}
	
	private void initStars() {
		BufferBuilder buffer = Tesselator.getInstance().getBuilder();
		stars1 = buildBufferStars(buffer, stars1, 0.1, 0.30, 3500, 41315);
		stars2 = buildBufferStars(buffer, stars2, 0.1, 0.35, 2000, 35151);
		stars3 = buildBufferUVStars(buffer, stars3, 0.4, 1.2, 1000, 61354);
		stars4 = buildBufferUVStars(buffer, stars4, 0.4, 1.2, 1000, 61355);
		nebula1 = buildBufferFarFog(buffer, nebula1, 40, 60, 30, 11515);
		nebula2 = buildBufferFarFog(buffer, nebula2, 40, 60, 10, 14151);
		horizon = buildBufferHorizon(buffer, horizon);
		fog = buildBufferFog(buffer, fog);
	}
	
	private VertexBuffer buildBufferStars(BufferBuilder bufferBuilder, VertexBuffer buffer, double minSize, double maxSize, int count, long seed) {
		if (buffer != null) {
			buffer.close();
		}
		
		buffer = new VertexBuffer();
		makeStars(bufferBuilder, minSize, maxSize, count, seed);
		bufferBuilder.end();
		buffer.upload(bufferBuilder);
		
		return buffer;
	}
	
	private VertexBuffer buildBufferUVStars(BufferBuilder bufferBuilder, VertexBuffer buffer, double minSize, double maxSize, int count, long seed) {
		if (buffer != null) {
			buffer.close();
		}
		
		buffer = new VertexBuffer();
		makeUVStars(bufferBuilder, minSize, maxSize, count, seed);
		bufferBuilder.end();
		buffer.upload(bufferBuilder);
		
		return buffer;
	}
	
	private VertexBuffer buildBufferFarFog(BufferBuilder bufferBuilder, VertexBuffer buffer, double minSize, double maxSize, int count, long seed) {
		if (buffer != null) {
			buffer.close();
		}
		
		buffer = new VertexBuffer();
		makeFarFog(bufferBuilder, minSize, maxSize, count, seed);
		bufferBuilder.end();
		buffer.upload(bufferBuilder);
		
		return buffer;
	}
	
	private VertexBuffer buildBufferHorizon(BufferBuilder bufferBuilder, VertexBuffer buffer) {
		if (buffer != null) {
			buffer.close();
		}
		
		buffer = new VertexBuffer();
		makeCylinder(bufferBuilder, 16, 50, 100);
		bufferBuilder.end();
		buffer.upload(bufferBuilder);
		
		return buffer;
	}
	
	private VertexBuffer buildBufferFog(BufferBuilder bufferBuilder, VertexBuffer buffer) {
		if (buffer != null) {
			buffer.close();
		}
		
		buffer = new VertexBuffer();
		makeCylinder(bufferBuilder, 16, 50, 70);
		bufferBuilder.end();
		buffer.upload(bufferBuilder);
		
		return buffer;
	}
	
	private void makeStars(BufferBuilder buffer, double minSize, double maxSize, int count, long seed) {
		Random random = new Random(seed);
		buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION);
		
		for (int i = 0; i < count; ++i) {
			double posX = random.nextDouble() * 2.0 - 1.0;
			double posY = random.nextDouble() * 2.0 - 1.0;
			double posZ = random.nextDouble() * 2.0 - 1.0;
			double size = MHelper.randRange(minSize, maxSize, random);
			double length = posX * posX + posY * posY + posZ * posZ;
			
			if (length < 1.0 && length > 0.001) {
				length = 1.0 / Math.sqrt(length);
				posX *= length;
				posY *= length;
				posZ *= length;
				
				double px = posX * 100.0;
				double py = posY * 100.0;
				double pz = posZ * 100.0;
				
				double angle = Math.atan2(posX, posZ);
				double sin1 = Math.sin(angle);
				double cos1 = Math.cos(angle);
				angle = Math.atan2(Math.sqrt(posX * posX + posZ * posZ), posY);
				double sin2 = Math.sin(angle);
				double cos2 = Math.cos(angle);
				angle = random.nextDouble() * Math.PI * 2.0;
				double sin3 = Math.sin(angle);
				double cos3 = Math.cos(angle);
				
				for (int index = 0; index < 4; ++index) {
					double x = (double) ((index & 2) - 1) * size;
					double y = (double) ((index + 1 & 2) - 1) * size;
					double aa = x * cos3 - y * sin3;
					double ab = y * cos3 + x * sin3;
					double dy = aa * sin2 + 0.0 * cos2;
					double ae = 0.0 * sin2 - aa * cos2;
					double dx = ae * sin1 - ab * cos1;
					double dz = ab * sin1 + ae * cos1;
					buffer.vertex(px + dx, py + dy, pz + dz).endVertex();
				}
			}
		}
	}
	
	private void makeUVStars(BufferBuilder buffer, double minSize, double maxSize, int count, long seed) {
		Random random = new Random(seed);
		buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
		
		for (int i = 0; i < count; ++i) {
			double posX = random.nextDouble() * 2.0 - 1.0;
			double posY = random.nextDouble() * 2.0 - 1.0;
			double posZ = random.nextDouble() * 2.0 - 1.0;
			double size = MHelper.randRange(minSize, maxSize, random);
			double length = posX * posX + posY * posY + posZ * posZ;
			
			if (length < 1.0 && length > 0.001) {
				length = 1.0 / Math.sqrt(length);
				posX *= length;
				posY *= length;
				posZ *= length;
				
				double px = posX * 100.0;
				double py = posY * 100.0;
				double pz = posZ * 100.0;
				
				double angle = Math.atan2(posX, posZ);
				double sin1 = Math.sin(angle);
				double cos1 = Math.cos(angle);
				angle = Math.atan2(Math.sqrt(posX * posX + posZ * posZ), posY);
				double sin2 = Math.sin(angle);
				double cos2 = Math.cos(angle);
				angle = random.nextDouble() * Math.PI * 2.0;
				double sin3 = Math.sin(angle);
				double cos3 = Math.cos(angle);
				
				float minV = random.nextInt(4) / 4F;
				for (int index = 0; index < 4; ++index) {
					double x = (double) ((index & 2) - 1) * size;
					double y = (double) ((index + 1 & 2) - 1) * size;
					double aa = x * cos3 - y * sin3;
					double ab = y * cos3 + x * sin3;
					double dy = aa * sin2 + 0.0 * cos2;
					double ae = 0.0 * sin2 - aa * cos2;
					double dx = ae * sin1 - ab * cos1;
					double dz = ab * sin1 + ae * cos1;
					float texU = (index >> 1) & 1;
					float texV = (((index + 1) >> 1) & 1) / 4F + minV;
					buffer.vertex(px + dx, py + dy, pz + dz).uv(texU, texV).endVertex();
				}
			}
		}
	}
	
	private void makeFarFog(BufferBuilder buffer, double minSize, double maxSize, int count, long seed) {
		Random random = new Random(seed);
		buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
		
		for (int i = 0; i < count; ++i) {
			double posX = random.nextDouble() * 2.0 - 1.0;
			double posY = random.nextDouble() - 0.5;
			double posZ = random.nextDouble() * 2.0 - 1.0;
			double size = MHelper.randRange(minSize, maxSize, random);
			double length = posX * posX + posY * posY + posZ * posZ;
			double distance = 2.0;
			
			if (length < 1.0 && length > 0.001) {
				length = distance / Math.sqrt(length);
				size *= distance;
				posX *= length;
				posY *= length;
				posZ *= length;
				
				double px = posX * 100.0;
				double py = posY * 100.0;
				double pz = posZ * 100.0;
				
				double angle = Math.atan2(posX, posZ);
				double sin1 = Math.sin(angle);
				double cos1 = Math.cos(angle);
				angle = Math.atan2(Math.sqrt(posX * posX + posZ * posZ), posY);
				double sin2 = Math.sin(angle);
				double cos2 = Math.cos(angle);
				angle = random.nextDouble() * Math.PI * 2.0;
				double sin3 = Math.sin(angle);
				double cos3 = Math.cos(angle);
				
				for (int index = 0; index < 4; ++index) {
					double x = (double) ((index & 2) - 1) * size;
					double y = (double) ((index + 1 & 2) - 1) * size;
					double aa = x * cos3 - y * sin3;
					double ab = y * cos3 + x * sin3;
					double dy = aa * sin2 + 0.0 * cos2;
					double ae = 0.0 * sin2 - aa * cos2;
					double dx = ae * sin1 - ab * cos1;
					double dz = ab * sin1 + ae * cos1;
					float texU = (index >> 1) & 1;
					float texV = ((index + 1) >> 1) & 1;
					buffer.vertex(px + dx, py + dy, pz + dz).uv(texU, texV).endVertex();
				}
			}
		}
	}
	
	private void makeCylinder(BufferBuilder buffer, int segments, double height, double radius) {
		buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
		for (int i = 0; i < segments; i++) {
			double a1 = (double) i * Math.PI * 2.0 / (double) segments;
			double a2 = (double) (i + 1) * Math.PI * 2.0 / (double) segments;
			double px1 = Math.sin(a1) * radius;
			double pz1 = Math.cos(a1) * radius;
			double px2 = Math.sin(a2) * radius;
			double pz2 = Math.cos(a2) * radius;
			
			float u0 = (float) i / (float) segments;
			float u1 = (float) (i + 1) / (float) segments;
			
			buffer.vertex(px1, -height, pz1).uv(u0, 0).endVertex();
			buffer.vertex(px1, height, pz1).uv(u0, 1).endVertex();
			buffer.vertex(px2, height, pz2).uv(u1, 1).endVertex();
			buffer.vertex(px2, -height, pz2).uv(u1, 0).endVertex();
		}
	}
}
