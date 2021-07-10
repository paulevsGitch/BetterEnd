package ru.betterend.blocks;

import com.google.common.collect.Lists;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SlimeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.storage.loot.LootContext;
import org.jetbrains.annotations.Nullable;
import ru.bclib.client.models.BlockModelProvider;
import ru.bclib.client.models.ModelsHelper;
import ru.bclib.client.render.BCLRenderLayer;
import ru.bclib.interfaces.IColorProvider;
import ru.bclib.interfaces.IRenderTyped;
import ru.bclib.util.ColorUtil;
import ru.bclib.util.MHelper;
import ru.betterend.client.models.Patterns;
import ru.betterend.noise.OpenSimplexNoise;

import java.util.List;
import java.util.Optional;

public class JellyshroomCapBlock extends SlimeBlock implements IRenderTyped, BlockModelProvider, IColorProvider {
	public static final IntegerProperty COLOR = EndBlockProperties.COLOR;
	private static final OpenSimplexNoise NOISE = new OpenSimplexNoise(0);
	private final Vec3i colorStart;
	private final Vec3i colorEnd;
	private final int coloritem;
	
	public JellyshroomCapBlock(int r1, int g1, int b1, int r2, int g2, int b2) {
		super(FabricBlockSettings.copyOf(Blocks.SLIME_BLOCK));
		colorStart = new Vec3i(r1, g1, b1);
		colorEnd = new Vec3i(r2, g2, b2);
		coloritem = ColorUtil.color((r1 + r2) >> 1, (g1 + g2) >> 1, (b1 + b2) >> 1);
	}
	
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		double px = ctx.getClickedPos().getX() * 0.1;
		double py = ctx.getClickedPos().getY() * 0.1;
		double pz = ctx.getClickedPos().getZ() * 0.1;
		return this.defaultBlockState().setValue(COLOR, MHelper.floor(NOISE.eval(px, py, pz) * 3.5 + 4));
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateManager) {
		stateManager.add(COLOR);
	}
	
	@Override
	public BCLRenderLayer getRenderLayer() {
		return BCLRenderLayer.TRANSLUCENT;
	}
	
	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		return Lists.newArrayList(new ItemStack(this));
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public BlockModel getItemModel(ResourceLocation resourceLocation) {
		return getBlockModel(resourceLocation, defaultBlockState());
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public @Nullable BlockModel getBlockModel(ResourceLocation resourceLocation, BlockState blockState) {
		Optional<String> pattern = Patterns.createJson(Patterns.BLOCK_COLORED, "jellyshroom_cap");
		return ModelsHelper.fromPattern(pattern);
	}
	
	@Override
	public BlockColor getProvider() {
		return (state, world, pos, tintIndex) -> {
			float delta = (float) state.getValue(COLOR) / 7F;
			int r = Mth.floor(Mth.lerp(delta, colorStart.getX() / 255F, colorEnd.getX() / 255F) * 255F);
			int g = Mth.floor(Mth.lerp(delta, colorStart.getY() / 255F, colorEnd.getY() / 255F) * 255F);
			int b = Mth.floor(Mth.lerp(delta, colorStart.getZ() / 255F, colorEnd.getZ() / 255F) * 255F);
			return ColorUtil.color(r, g, b);
		};
	}
	
	@Override
	public ItemColor getItemProvider() {
		return (stack, tintIndex) -> {
			return coloritem;
		};
	}
}
