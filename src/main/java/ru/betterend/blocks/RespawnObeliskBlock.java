package ru.betterend.blocks;

import com.google.common.collect.Lists;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import ru.bclib.blocks.BaseBlock;
import ru.bclib.blocks.BlockProperties;
import ru.bclib.blocks.BlockProperties.TripleShape;
import ru.bclib.client.render.BCLRenderLayer;
import ru.bclib.interfaces.CustomColorProvider;
import ru.bclib.interfaces.RenderLayerProvider;
import ru.bclib.util.BlocksHelper;
import ru.bclib.util.ColorUtil;
import ru.betterend.particle.InfusionParticleType;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndItems;

import java.util.List;

public class RespawnObeliskBlock extends BaseBlock implements CustomColorProvider, RenderLayerProvider {
	private static final VoxelShape VOXEL_SHAPE_BOTTOM = Block.box(1, 0, 1, 15, 16, 15);
	private static final VoxelShape VOXEL_SHAPE_MIDDLE_TOP = Block.box(2, 0, 2, 14, 16, 14);
	
	public static final EnumProperty<TripleShape> SHAPE = BlockProperties.TRIPLE_SHAPE;
	
	public RespawnObeliskBlock() {
		super(FabricBlockSettings.copyOf(Blocks.END_STONE).luminance((state) -> {
			return (state.getValue(SHAPE) == TripleShape.BOTTOM) ? 0 : 15;
		}));
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public VoxelShape getShape(BlockState state, BlockGetter view, BlockPos pos, CollisionContext ePos) {
		return (state.getValue(SHAPE) == TripleShape.BOTTOM) ? VOXEL_SHAPE_BOTTOM : VOXEL_SHAPE_MIDDLE_TOP;
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateManager) {
		stateManager.add(SHAPE);
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
		for (int i = 0; i < 3; i++) {
			if (!world.getBlockState(pos.above(i)).getMaterial().isReplaceable()) {
				return false;
			}
		}
		return true;
	}
	
	@Override
	public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
		state = this.defaultBlockState();
		BlocksHelper.setWithUpdate(world, pos, state.setValue(SHAPE, TripleShape.BOTTOM));
		BlocksHelper.setWithUpdate(world, pos.above(), state.setValue(SHAPE, TripleShape.MIDDLE));
		BlocksHelper.setWithUpdate(world, pos.above(2), state.setValue(SHAPE, TripleShape.TOP));
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public BlockState updateShape(BlockState state, Direction facing, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
		TripleShape shape = state.getValue(SHAPE);
		if (shape == TripleShape.BOTTOM) {
			if (world.getBlockState(pos.above()).is(this)) {
				return state;
			}
			else {
				return Blocks.AIR.defaultBlockState();
			}
		}
		else if (shape == TripleShape.MIDDLE) {
			if (world.getBlockState(pos.above()).is(this) && world.getBlockState(pos.below()).is(this)) {
				return state;
			}
			else {
				return Blocks.AIR.defaultBlockState();
			}
		}
		else {
			if (world.getBlockState(pos.below()).is(this)) {
				return state;
			}
			else {
				return Blocks.AIR.defaultBlockState();
			}
		}
	}
	
	@Override
	public void playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
		if (player.isCreative()) {
			TripleShape shape = state.getValue(SHAPE);
			if (shape == TripleShape.MIDDLE) {
				BlocksHelper.setWithUpdate(world, pos.below(), Blocks.AIR);
			}
			else if (shape == TripleShape.TOP) {
				BlocksHelper.setWithUpdate(world, pos.below(2), Blocks.AIR);
			}
		}
		super.playerWillDestroy(world, pos, state, player);
	}
	
	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		if (state.getValue(SHAPE) == TripleShape.BOTTOM) {
			return Lists.newArrayList(new ItemStack(this));
		}
		else {
			return Lists.newArrayList();
		}
	}
	
	@Override
	public BCLRenderLayer getRenderLayer() {
		return BCLRenderLayer.TRANSLUCENT;
	}
	
	@Override
	public BlockColor getProvider() {
		return ((CustomColorProvider) EndBlocks.AURORA_CRYSTAL).getProvider();
	}
	
	@Override
	public ItemColor getItemProvider() {
		return (stack, tintIndex) -> {
			return ColorUtil.color(255, 255, 255);
		};
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		ItemStack itemStack = player.getItemInHand(hand);
		boolean canActivate = itemStack.getItem() == EndItems.AMBER_GEM && itemStack.getCount() > 5;
		if (hand != InteractionHand.MAIN_HAND || !canActivate) {
			if (!world.isClientSide && !(itemStack.getItem() instanceof BlockItem) && !player.isCreative()) {
				ServerPlayer serverPlayerEntity = (ServerPlayer) player;
				serverPlayerEntity.displayClientMessage(
					new TranslatableComponent("message.betterend.fail_spawn"),
					true
				);
			}
			return InteractionResult.FAIL;
		}
		else if (!world.isClientSide) {
			ServerPlayer serverPlayerEntity = (ServerPlayer) player;
			serverPlayerEntity.setRespawnPosition(world.dimension(), pos, 0.0F, false, false);
			serverPlayerEntity.displayClientMessage(new TranslatableComponent("message.betterend.set_spawn"), true);
			double px = pos.getX() + 0.5;
			double py = pos.getY() + 0.5;
			double pz = pos.getZ() + 0.5;
			InfusionParticleType particle = new InfusionParticleType(new ItemStack(EndItems.AMBER_GEM));
			if (world instanceof ServerLevel) {
				double py1 = py;
				double py2 = py - 0.2;
				if (state.getValue(SHAPE) == TripleShape.BOTTOM) {
					py1 += 1;
					py2 += 2;
				}
				else if (state.getValue(SHAPE) == TripleShape.MIDDLE) {
					py1 += 0;
					py2 += 1;
				}
				else {
					py1 -= 2;
				}
				((ServerLevel) world).sendParticles(particle, px, py1, pz, 20, 0.14, 0.5, 0.14, 0.1);
				((ServerLevel) world).sendParticles(particle, px, py2, pz, 20, 0.14, 0.3, 0.14, 0.1);
			}
			world.playSound(null, px, py, py, SoundEvents.RESPAWN_ANCHOR_SET_SPAWN, SoundSource.BLOCKS, 1F, 1F);
			if (!player.isCreative()) {
				itemStack.shrink(6);
			}
		}
		return player.isCreative() ? InteractionResult.PASS : InteractionResult.sidedSuccess(world.isClientSide);
	}
}
