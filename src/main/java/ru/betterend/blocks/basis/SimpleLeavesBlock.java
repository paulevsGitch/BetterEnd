package ru.betterend.blocks.basis;

import java.io.Reader;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import ru.betterend.client.render.ERenderLayer;
import ru.betterend.interfaces.IRenderTypeable;
import ru.betterend.patterns.Patterns;

public class SimpleLeavesBlock extends BlockBaseNotFull implements IRenderTypeable {
	public SimpleLeavesBlock(MaterialColor color) {
		super(FabricBlockSettings.of(Material.LEAVES).strength(0.2F).sounds(SoundType.GRASS).nonOpaque()
				.allowsSpawning((state, world, pos, type) -> {
					return false;
				}).suffocates((state, world, pos) -> {
					return false;
				}).blockVision((state, world, pos) -> {
					return false;
				}).materialColor(color));
	}

	public SimpleLeavesBlock(MaterialColor color, int light) {
		super(FabricBlockSettings.of(Material.LEAVES).strength(0.2F).sounds(SoundType.GRASS).nonOpaque()
				.luminance(light).allowsSpawning((state, world, pos, type) -> {
					return false;
				}).suffocates((state, world, pos) -> {
					return false;
				}).blockVision((state, world, pos) -> {
					return false;
				}).materialColor(color));
	}

	@Override
	public String getStatesPattern(Reader data) {
		String texture = Registry.BLOCK.getKey(this).getPath();
		return Patterns.createJson(data, texture, texture);
	}

	@Override
	public String getModelPattern(String block) {
		String texture = Registry.BLOCK.getKey(this).getPath();
		return Patterns.createJson(Patterns.BLOCK_BASE, texture, texture);
	}

	@Override
	public ResourceLocation statePatternId() {
		return Patterns.STATE_SIMPLE;
	}

	@Override
	public ERenderLayer getRenderLayer() {
		return ERenderLayer.CUTOUT;
	}
}