package ru.betterend.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import ru.betterend.blocks.basis.PedestalBlock;
import ru.betterend.blocks.entities.InfusionPedestalEntity;
import ru.betterend.rituals.InfusionRitual;

public class InfusionPedestal extends PedestalBlock {
	private static final VoxelShape SHAPE_DEFAULT;
	private static final VoxelShape SHAPE_PEDESTAL_TOP;

	public InfusionPedestal() {
		super(Blocks.OBSIDIAN);
		this.height = 1.08F;
	}
	
	@Override
	public void checkRitual(World world, BlockPos pos) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof InfusionPedestalEntity) {
			InfusionPedestalEntity pedestal = (InfusionPedestalEntity) blockEntity;
			if (pedestal.hasRitual()) {
				pedestal.getRitual().checkRecipe();
			} else {
				InfusionRitual ritual = new InfusionRitual(world, pos);
				pedestal.linkRitual(ritual);
				ritual.checkRecipe();
			}
		}
	}
	
	@Override
	public BlockEntity createBlockEntity(BlockView world) {
		return new InfusionPedestalEntity();
	}
	
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		if (state.isOf(this)) {
			switch(state.get(STATE)) {
				case PEDESTAL_TOP: {
					return SHAPE_PEDESTAL_TOP;
				}
				case DEFAULT: {
					return SHAPE_DEFAULT;
				}
				default: {
					return super.getOutlineShape(state, world, pos, context);
				}
			}
		}
		return super.getOutlineShape(state, world, pos, context);
	}
	
	static {
		VoxelShape basinUp = Block.createCuboidShape(2, 3, 2, 14, 4, 14);
		VoxelShape basinDown = Block.createCuboidShape(0, 0, 0, 16, 3, 16);
		VoxelShape pedestalTop = Block.createCuboidShape(1, 9, 1, 15, 11, 15);
		VoxelShape pedestalDefault = Block.createCuboidShape(1, 13, 1, 15, 15, 15);
		VoxelShape pillar = Block.createCuboidShape(3, 0, 3, 13, 9, 13);
		VoxelShape pillarDefault = Block.createCuboidShape(3, 4, 3, 13, 13, 13);
		VoxelShape eyeDefault = Block.createCuboidShape(4, 15, 4, 12, 16, 12);
		VoxelShape eyeTop = Block.createCuboidShape(4, 11, 4, 12, 12, 12);
		VoxelShape basin = VoxelShapes.union(basinDown, basinUp);
		SHAPE_DEFAULT = VoxelShapes.union(basin, pillarDefault, pedestalDefault, eyeDefault);
		SHAPE_PEDESTAL_TOP = VoxelShapes.union(pillar, pedestalTop, eyeTop);
	}
}
