package ru.betterend.blocks;

import java.io.Reader;
import java.util.List;

import com.google.common.collect.Lists;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SlimeBlock;
import net.minecraft.client.color.block.BlockColorProvider;
import net.minecraft.client.color.item.ItemColorProvider;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.registry.Registry;
import ru.betterend.client.render.ERenderLayer;
import ru.betterend.interfaces.IColorProvider;
import ru.betterend.interfaces.IRenderTypeable;
import ru.betterend.noise.OpenSimplexNoise;
import ru.betterend.patterns.BlockPatterned;
import ru.betterend.patterns.Patterns;
import ru.betterend.util.MHelper;

public class BlockJellyshroomCap extends SlimeBlock implements IRenderTypeable, BlockPatterned, IColorProvider {
	public static final IntProperty COLOR = IntProperty.of("color", 0, 7);
	private static final OpenSimplexNoise NOISE = new OpenSimplexNoise(0);
	private final Vec3i colorStart;
	private final Vec3i colorEnd;
	private final int coloritem;
	
	public BlockJellyshroomCap(int r1, int g1, int b1, int r2, int g2, int b2) {
		super(FabricBlockSettings.copyOf(Blocks.SLIME_BLOCK));
		colorStart = new Vec3i(r1, g1, b1);
		colorEnd = new Vec3i(r2, g2, b2);
		coloritem = MHelper.color((r1 + r2) >> 1, (g1 + g2) >> 1, (b1 + b2) >> 1);
	}
	
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		double px = ctx.getBlockPos().getX() * 0.1;
		double py = ctx.getBlockPos().getY() * 0.1;
		double pz = ctx.getBlockPos().getZ() * 0.1;
		return this.getDefaultState().with(COLOR, MHelper.floor(NOISE.eval(px, py, pz) * 3.5 + 4));
	}
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
		stateManager.add(COLOR);
	}
	
	@Override
	public ERenderLayer getRenderLayer() {
		return ERenderLayer.TRANSLUCENT;
	}
	
	@Override
	public List<ItemStack> getDroppedStacks(BlockState state, LootContext.Builder builder) {
		return Lists.newArrayList(new ItemStack(this));
	}
	
	@Override
	public String getStatesPattern(Reader data) {
		String block = Registry.BLOCK.getId(this).getPath();
		return Patterns.createJson(data, block, block);
	}
	
	@Override
	public String getModelPattern(String block) {
		return Patterns.createJson(Patterns.BLOCK_COLORED, "jellyshroom_cap");
	}
	
	@Override
	public Identifier statePatternId() {
		return Patterns.STATE_SIMPLE;
	}
	
	@Override
	public BlockColorProvider getProvider() {
		return (state, world, pos, tintIndex) -> {
			float delta = (float) state.get(COLOR) / 7F;
			int r = MathHelper.floor(MathHelper.lerp(delta, colorStart.getX() / 255F, colorEnd.getX() / 255F) * 255F);
			int g = MathHelper.floor(MathHelper.lerp(delta, colorStart.getY() / 255F, colorEnd.getY() / 255F) * 255F);
			int b = MathHelper.floor(MathHelper.lerp(delta, colorStart.getZ() / 255F, colorEnd.getZ() / 255F) * 255F);
			return MHelper.color(r, g, b);
		};
	}

	@Override
	public ItemColorProvider getItemProvider() {
		return (stack, tintIndex) -> {
			return coloritem;
		};
	}
}
