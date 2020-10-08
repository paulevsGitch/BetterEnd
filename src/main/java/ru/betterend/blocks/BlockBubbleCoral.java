package ru.betterend.blocks;

import java.util.Random;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.ShapeContext;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import ru.betterend.blocks.basis.BlockUnderwaterPlant;

public class BlockBubbleCoral extends BlockUnderwaterPlant {
	private static final VoxelShape SHAPE = Block.createCuboidShape(0, 0, 0, 16, 14, 16);
	
	public BlockBubbleCoral() {
		super(FabricBlockSettings.of(Material.UNDERWATER_PLANT)
				.breakByTool(FabricToolTags.SHEARS)
				.sounds(BlockSoundGroup.CORAL)
				.breakByHand(true)
				.noCollision());
	}
	
	@Override
	public AbstractBlock.OffsetType getOffsetType() {
		return AbstractBlock.OffsetType.NONE;
	}

	@Environment(EnvType.CLIENT)
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
		double x = pos.getX() + random.nextDouble();
		double y = pos.getY() + random.nextDouble() * 0.5F + 0.5F;
		double z = pos.getZ() + random.nextDouble();
		world.addParticle(ParticleTypes.BUBBLE, x, y, z, 0.0D, 0.0D, 0.0D);
	}
	
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext ePos) {
		return SHAPE;
	}
}
