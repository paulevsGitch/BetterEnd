package ru.betterend.blocks.basis;

import java.util.Optional;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import ru.betterend.blocks.AuroraCrystalBlock;
import ru.betterend.client.models.ModelsHelper;
import ru.betterend.interfaces.IColorProvider;
import ru.betterend.client.models.Patterns;
import ru.betterend.util.MHelper;

public class StoneLanternBlock extends EndLanternBlock implements IColorProvider {
	private static final VoxelShape SHAPE_CEIL = Block.box(3, 1, 3, 13, 16, 13);
	private static final VoxelShape SHAPE_FLOOR = Block.box(3, 0, 3, 13, 15, 13);
	private static final Vec3i[] COLORS = AuroraCrystalBlock.COLORS;
	
	public StoneLanternBlock(Block source) {
		super(FabricBlockSettings.copyOf(source).luminance(15));
	}
	
	@Override
	public BlockColor getProvider() {
		return (state, world, pos, tintIndex) -> {
			long i = (long) pos.getX() + (long) pos.getY() + (long) pos.getZ();
			double delta = i * 0.1;
			int index = MHelper.floor(delta);
			int index2 = (index + 1) & 3;
			delta -= index;
			index &= 3;
			
			Vec3i color1 = COLORS[index];
			Vec3i color2 = COLORS[index2];
			
			int r = MHelper.floor(Mth.lerp(delta, color1.getX(), color2.getX()));
			int g = MHelper.floor(Mth.lerp(delta, color1.getY(), color2.getY()));
			int b = MHelper.floor(Mth.lerp(delta, color1.getZ(), color2.getZ()));
			
			return MHelper.color(r, g, b);
		};
	}

	@Override
	public ItemColor getItemProvider() {
		return (stack, tintIndex) -> {
			return MHelper.color(COLORS[3].getX(), COLORS[3].getY(), COLORS[3].getZ());
		};
	}
	
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter view, BlockPos pos, CollisionContext ePos) {
		return state.getValue(IS_FLOOR) ? SHAPE_FLOOR : SHAPE_CEIL;
	}

	@Override
	public @Nullable BlockModel getBlockModel(ResourceLocation resourceLocation, BlockState blockState) {
		String blockName = resourceLocation.getPath();
		Optional<String> pattern = blockState.getValue(IS_FLOOR) ?
				Patterns.createJson(Patterns.BLOCK_STONE_LANTERN_FLOOR, blockName, blockName) :
				Patterns.createJson(Patterns.BLOCK_STONE_LANTERN_CEIL, blockName, blockName);
		return ModelsHelper.fromPattern(pattern);
	}
}
