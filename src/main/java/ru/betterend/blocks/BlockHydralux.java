package ru.betterend.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;
import ru.betterend.blocks.BlockProperties.HydraluxShape;
import ru.betterend.blocks.basis.BlockUnderwaterPlant;
import ru.betterend.registry.EndBlocks;

public class BlockHydralux extends BlockUnderwaterPlant {
	public static final EnumProperty<HydraluxShape> SHAPE = BlockProperties.HYDRALUX_SHAPE;
	
	public BlockHydralux() {
		super(FabricBlockSettings.of(Material.UNDERWATER_PLANT)
				.breakByTool(FabricToolTags.SHEARS)
				.sounds(BlockSoundGroup.WET_GRASS)
				.breakByHand(true)
				.luminance((state) -> { return state.get(SHAPE).hasGlow() ? 15 : 0; })
				.noCollision());
	}
	
	@Override
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		BlockState down = world.getBlockState(pos.down());
		HydraluxShape shape = state.get(SHAPE);
		if (shape == HydraluxShape.FLOWER_BIG_TOP || shape == HydraluxShape.FLOWER_SMALL_TOP) {
			return down.isOf(this);
		}
		else if (shape == HydraluxShape.ROOTS) {
			return down.isOf(EndBlocks.SULPHURIC_ROCK.stone) && world.getBlockState(pos.up()).isOf(this);
		}
		else {
			return down.isOf(this) && world.getBlockState(pos.up()).isOf(this);
		}
	}
}
