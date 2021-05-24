package ru.betterend.blocks.basis;

import java.util.Optional;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.core.Registry;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import ru.betterend.client.render.ERenderLayer;
import ru.betterend.interfaces.IRenderTypeable;
import ru.betterend.client.models.Patterns;

public class SimpleLeavesBlock extends BlockBaseNotFull implements IRenderTypeable {
	public SimpleLeavesBlock(MaterialColor color) {
		super(FabricBlockSettings.of(Material.LEAVES)
				.strength(0.2F)
				.materialColor(color)
				.sound(SoundType.GRASS)
				.noOcclusion()
				.isValidSpawn((state, world, pos, type) -> false)
				.isSuffocating((state, world, pos) -> false)
				.isViewBlocking((state, world, pos) -> false));
	}
	
	public SimpleLeavesBlock(MaterialColor color, int light) {
		super(FabricBlockSettings.of(Material.LEAVES)
				.luminance(light)
				.materialColor(color)
				.strength(0.2F)
				.sound(SoundType.GRASS)
				.noOcclusion()
				.isValidSpawn((state, world, pos, type) -> false)
				.isSuffocating((state, world, pos) -> false)
				.isViewBlocking((state, world, pos) -> false));
	}

	@Override
	public ERenderLayer getRenderLayer() {
		return ERenderLayer.CUTOUT;
	}
}