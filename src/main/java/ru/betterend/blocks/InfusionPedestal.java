package ru.betterend.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import ru.betterend.blocks.basis.PedestalBlock;
import ru.betterend.blocks.entities.InfusionPedestalEntity;
import ru.betterend.rituals.InfusionRitual;

@SuppressWarnings("deprecation")
public class InfusionPedestal extends PedestalBlock {
	private static final VoxelShape SHAPE_DEFAULT;
	private static final VoxelShape SHAPE_PEDESTAL_TOP;

	public InfusionPedestal() {
		super(Blocks.OBSIDIAN);
		this.height = 1.08F;
	}
	
	@Override
	public void checkRitual(Level world, BlockPos pos) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof InfusionPedestalEntity) {
			InfusionPedestalEntity pedestal = (InfusionPedestalEntity) blockEntity;
			if (pedestal.hasRitual()) {
				InfusionRitual ritual = pedestal.getRitual();
				if (!ritual.isValid()) {
					ritual.configure();
				}
				pedestal.getRitual().checkRecipe();
			} else {
				InfusionRitual ritual = new InfusionRitual(pedestal, world, pos);
				pedestal.linkRitual(ritual);
				ritual.checkRecipe();
			}
		}
	}
	
	@Override
	public BlockEntity newBlockEntity(BlockGetter world) {
		return new InfusionPedestalEntity();
	}

	@Override
	public boolean hasUniqueEntity() {
		return true;
	}

	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		if (state.is(this)) {
			switch(state.getValue(STATE)) {
				case PEDESTAL_TOP: {
					return SHAPE_PEDESTAL_TOP;
				}
				case DEFAULT: {
					return SHAPE_DEFAULT;
				}
				default: {
					return super.getShape(state, world, pos, context);
				}
			}
		}
		return super.getShape(state, world, pos, context);
	}

	static {
		VoxelShape basinUp = Block.box(2, 3, 2, 14, 4, 14);
		VoxelShape basinDown = Block.box(0, 0, 0, 16, 3, 16);
		VoxelShape pedestalTop = Block.box(1, 9, 1, 15, 11, 15);
		VoxelShape pedestalDefault = Block.box(1, 13, 1, 15, 15, 15);
		VoxelShape pillar = Block.box(3, 0, 3, 13, 9, 13);
		VoxelShape pillarDefault = Block.box(3, 4, 3, 13, 13, 13);
		VoxelShape eyeDefault = Block.box(4, 15, 4, 12, 16, 12);
		VoxelShape eyeTop = Block.box(4, 11, 4, 12, 12, 12);
		VoxelShape basin = Shapes.or(basinDown, basinUp);
		SHAPE_DEFAULT = Shapes.or(basin, pillarDefault, pedestalDefault, eyeDefault);
		SHAPE_PEDESTAL_TOP = Shapes.or(pillar, pedestalTop, eyeTop);
	}
}
