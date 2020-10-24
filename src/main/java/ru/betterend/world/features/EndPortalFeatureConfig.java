package ru.betterend.world.features;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.FeatureConfig;
import ru.betterend.blocks.RunedFlavolite;

public class EndPortalFeatureConfig implements FeatureConfig {
	public static final Codec<EndPortalFeatureConfig> CODEC = RecordCodecBuilder.create((instance) -> {
		return instance.group(Codec.STRING.fieldOf("frame_block").forGetter(endPortalFeatureConfig -> {
			return Registry.BLOCK.getId(endPortalFeatureConfig.frameBlock).toString();
		}), Codec.STRING.fieldOf("axis").forGetter(endPortalFeatureConfig -> {
			return endPortalFeatureConfig.axis.getName();
		}), Codec.INT.fieldOf("width").forGetter(endPortalFeatureConfig -> {
			return endPortalFeatureConfig.width;
		}), Codec.INT.fieldOf("heigth").forGetter(endPortalFeatureConfig -> {
			return endPortalFeatureConfig.height;
		}), Codec.BOOL.fieldOf("activated").forGetter(endPortalFeatureConfig -> {
			return endPortalFeatureConfig.activated;
		})).apply(instance, EndPortalFeatureConfig::new);
	});
	
	public final RunedFlavolite frameBlock;
	public final Direction.Axis axis;
	public final boolean activated;
	public final int width;
	public final int height;
	
	private EndPortalFeatureConfig(String frameBlock, String axis, int width, int height, boolean active) {
		this.frameBlock = (RunedFlavolite) Registry.BLOCK.get(new Identifier(frameBlock));
		this.axis = Direction.Axis.fromName(axis);
		this.activated = active;
		this.width = width;
		this.height = height;
	}
	
	private EndPortalFeatureConfig(RunedFlavolite frameBlock, Direction.Axis axis, int width, int height, boolean active) {
		this.frameBlock = frameBlock;
		this.axis = axis;
		this.activated = active;
		this.width = width;
		this.height = height;
	}
	
	public static EndPortalFeatureConfig create(RunedFlavolite block, Direction.Axis axis, int width, int height, boolean active) {
		return new EndPortalFeatureConfig(block, axis, width, height, active);
	}
}
