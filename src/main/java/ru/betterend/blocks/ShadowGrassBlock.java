package ru.betterend.blocks;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MaterialColor;
import ru.bclib.api.tag.NamedCommonBlockTags;
import ru.bclib.api.tag.TagAPI.TagLocation;
import ru.bclib.interfaces.TagProvider;
import ru.betterend.blocks.basis.EndTerrainBlock;
import ru.betterend.registry.EndParticles;

import java.util.List;
import java.util.Random;

public class ShadowGrassBlock extends EndTerrainBlock implements TagProvider {
	public ShadowGrassBlock() {
		super(MaterialColor.COLOR_BLACK);
	}
	
	@Environment(EnvType.CLIENT)
	public void animateTick(BlockState state, Level world, BlockPos pos, Random random) {
		super.animateTick(state, world, pos, random);
		if (random.nextInt(32) == 0) {
			world.addParticle(
				EndParticles.BLACK_SPORE,
				(double) pos.getX() + random.nextDouble(),
				(double) pos.getY() + 1.1D,
				(double) pos.getZ() + random.nextDouble(),
				0.0D,
				0.0D,
				0.0D
			);
		}
	}
	
	@Override
	public void addTags(List<TagLocation<Block>> blockTags, List<TagLocation<Item>> itemTags) {
		blockTags.add(NamedCommonBlockTags.END_STONES);
	}
}
