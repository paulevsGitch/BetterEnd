package ru.betterend.blocks.basis;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import ru.betterend.blocks.BlockProperties;
import ru.betterend.patterns.Patterns;

public class StalactiteBlock extends BlockBaseNotFull {
	public static final IntProperty SIZE = BlockProperties.SIZE;
	private static final Mutable POS = new Mutable();
	private static final VoxelShape[] SHAPES;
	private final Block source;

	public StalactiteBlock(Block source) {
		super(FabricBlockSettings.copy(source).nonOpaque());
		this.setDefaultState(getStateManager().getDefaultState().with(SIZE, 0));
		this.source = source;
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
		stateManager.add(SIZE);
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext ePos) {
		return SHAPES[state.get(SIZE)];
	}

	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
		if (world.getBlockState(pos.down()).getBlock() instanceof StalactiteBlock) {
			POS.setX(pos.getX());
			POS.setZ(pos.getZ());
			for (int i = 1; i < 8; i++) {
				POS.setY(pos.getY() - i);
				if (world.getBlockState(POS).getBlock() instanceof StalactiteBlock) {
					BlockState state2 = world.getBlockState(POS);
					int size = state2.get(SIZE);
					if (size < i) {
						world.setBlockState(POS, state2.with(SIZE, i));
					}
					else {
						break;
					}
				}
				else {
					break;
				}
			}
		}
		if (world.getBlockState(pos.up()).getBlock() instanceof StalactiteBlock) {
			POS.setX(pos.getX());
			POS.setZ(pos.getZ());
			for (int i = 1; i < 8; i++) {
				POS.setY(pos.getY() + i);
				if (world.getBlockState(POS).getBlock() instanceof StalactiteBlock) {
					BlockState state2 = world.getBlockState(POS);
					int size = state2.get(SIZE);
					if (size < i) {
						world.setBlockState(POS, state2.with(SIZE, i));
					}
					else {
						break;
					}
				}
				else {
					break;
				}
			}
		}
	}
	
	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction facing, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		if (!canPlaceAt(state, world, pos)) {
			return Blocks.AIR.getDefaultState();
		}
		else {
			return state;
		}
	}

	@Override
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		BlockState upState = world.getBlockState(pos.up());
		BlockState downState = world.getBlockState(pos.down());
		int size = state.get(SIZE);
		boolean validUp = (upState.isOf(this) && upState.get(SIZE) >= size) || upState.isFullCube(world, pos.up());
		boolean validDown = (downState.isOf(this) && downState.get(SIZE) >= size) || downState.isFullCube(world, pos.down());
		return validUp || validDown;
	}
	
	@Override
	public String getModelPattern(String block) {
		Identifier blockId = Registry.BLOCK.getId(this);
		if (block.contains("item")) {
			return Patterns.createJson(Patterns.ITEM_GENERATED, "item/" + blockId.getPath());
		}
		blockId = Registry.BLOCK.getId(source);
		int shape = Character.getNumericValue(block.charAt(block.lastIndexOf('_') + 1));
		return Patterns.createJson(Patterns.BLOCKS_STALACTITE[shape], blockId.getNamespace() + ":block/" + blockId.getPath());
	}
	
	@Override
	public Identifier statePatternId() {
		return Patterns.STATE_STALACTITE;
	}

	static {
		SHAPES = new VoxelShape[8];
		for (int i = 0; i < 8; i++) {
			SHAPES[i] = Block.createCuboidShape(7 - i, 0, 7 - i, 9 + i, 16, 9 + i);
		}
	}
}