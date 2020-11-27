package ru.betterend.blocks.basis;

import java.io.Reader;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.client.color.block.BlockColorProvider;
import net.minecraft.client.color.item.ItemColorProvider;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import ru.betterend.blocks.AuroraCrystalBlock;
import ru.betterend.interfaces.IColorProvider;
import ru.betterend.patterns.Patterns;
import ru.betterend.util.MHelper;

public class BlockStoneLantern extends BlockLantern implements IColorProvider {
	private static final VoxelShape SHAPE_CEIL = Block.createCuboidShape(3, 1, 3, 13, 16, 13);
	private static final VoxelShape SHAPE_FLOOR = Block.createCuboidShape(3, 0, 3, 13, 15, 13);
	private static final Vec3i[] COLORS = AuroraCrystalBlock.COLORS;
	
	public BlockStoneLantern(Block source) {
		super(FabricBlockSettings.copyOf(source).luminance(15));
	}
	
	@Override
	public BlockColorProvider getProvider() {
		return (state, world, pos, tintIndex) -> {
			long i = (long) pos.getX() + (long) pos.getY() + (long) pos.getZ();
			double delta = i * 0.1;
			int index = MHelper.floor(delta);
			int index2 = (index + 1) & 3;
			delta -= index;
			index &= 3;
			
			Vec3i color1 = COLORS[index];
			Vec3i color2 = COLORS[index2];
			
			int r = MHelper.floor(MathHelper.lerp(delta, color1.getX(), color2.getX()));
			int g = MHelper.floor(MathHelper.lerp(delta, color1.getY(), color2.getY()));
			int b = MHelper.floor(MathHelper.lerp(delta, color1.getZ(), color2.getZ()));
			
			return MHelper.color(r, g, b);
		};
	}

	@Override
	public ItemColorProvider getItemProvider() {
		return (stack, tintIndex) -> {
			return MHelper.color(COLORS[3].getX(), COLORS[3].getY(), COLORS[3].getZ());
		};
	}
	
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext ePos) {
		return state.get(IS_FLOOR) ? SHAPE_FLOOR : SHAPE_CEIL;
	}
	
	@Override
	public Identifier statePatternId() {
		return Patterns.STATE_STONE_LANTERN;
	}
	
	@Override
	public String getModelPattern(String block) {
		String texture = Registry.BLOCK.getId(this).getPath();
		if (block.contains("ceil")) {
			return Patterns.createJson(Patterns.BLOCK_STONE_LANTERN_CEIL, texture, texture);
		}
		return Patterns.createJson(Patterns.BLOCK_STONE_LANTERN_FLOOR, texture, texture);
	}
	
	@Override
	public String getStatesPattern(Reader data) {
		String block = Registry.BLOCK.getId(this).getPath();
		return Patterns.createJson(data, block, block);
	}
}
