package ru.betterend.blocks;

import java.util.Collections;
import java.util.List;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import ru.betterend.blocks.basis.BlockBaseNotFull;
import ru.betterend.client.render.ERenderLayer;
import ru.betterend.interfaces.IRenderTypeable;
import ru.betterend.registry.EndBlocks;

public class CavePumpkinBlock extends BlockBaseNotFull implements IRenderTypeable {
	public static final BooleanProperty SMALL = BlockProperties.SMALL;
	private static final VoxelShape SHAPE_SMALL;
	private static final VoxelShape SHAPE_BIG;
	
	public CavePumpkinBlock() {
		super(FabricBlockSettings.copyOf(Blocks.PUMPKIN).luminance((state) -> state.getValue(SMALL) ? 10 : 15));
		registerDefaultState(defaultBlockState().setValue(SMALL, false));
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateManager) {
		stateManager.add(SMALL);
	}
	
	@Override
	public ERenderLayer getRenderLayer() {
		return ERenderLayer.CUTOUT;
	}
	
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return state.getValue(SMALL) ? SHAPE_SMALL : SHAPE_BIG;
	}
	
	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		return state.getValue(SMALL) ? Collections.singletonList(new ItemStack(EndBlocks.CAVE_PUMPKIN_SEED)) : Collections.singletonList(new ItemStack(this));
	}
	
	static {
		VoxelShape lantern = Block.box(1, 0, 1, 15, 13, 15);
		VoxelShape cap = Block.box(0, 12, 0, 16, 15, 16);
		VoxelShape top = Block.box(5, 15, 5, 11, 16, 11);
		SHAPE_BIG = Shapes.or(lantern, cap, top);
		
		lantern = Block.box(1, 7, 1, 15, 13, 15);
		cap = Block.box(4, 12, 4, 12, 15, 12);
		top = Block.box(6, 15, 6, 10, 16, 10);
		SHAPE_SMALL = Shapes.or(lantern, cap, top);
	}
}
