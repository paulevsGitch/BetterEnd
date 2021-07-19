package ru.betterend.blocks;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import ru.bclib.blocks.BaseBlockNotFull;
import ru.bclib.client.render.BCLRenderLayer;
import ru.bclib.interfaces.RenderLayerProvider;
import ru.bclib.util.BlocksHelper;
import ru.betterend.registry.EndBlocks;

import java.util.Random;

@SuppressWarnings("deprecation")
public class MengerSpongeWetBlock extends BaseBlockNotFull implements RenderLayerProvider {
	public MengerSpongeWetBlock() {
		super(FabricBlockSettings.copyOf(Blocks.WET_SPONGE).noOcclusion());
	}
	
	@Override
	public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean notify) {
		if (world.dimensionType().ultraWarm()) {
			world.setBlock(pos, EndBlocks.MENGER_SPONGE.defaultBlockState(), 3);
			world.levelEvent(2009, pos, 0);
			world.playSound(
				null,
				pos,
				SoundEvents.FIRE_EXTINGUISH,
				SoundSource.BLOCKS,
				1.0F,
				(1.0F + world.getRandom().nextFloat() * 0.2F) * 0.7F
			);
		}
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public void animateTick(BlockState state, Level world, BlockPos pos, Random random) {
		Direction direction = Direction.getRandom(random);
		if (direction != Direction.UP) {
			BlockPos blockPos = pos.relative(direction);
			BlockState blockState = world.getBlockState(blockPos);
			if (!state.canOcclude() || !blockState.isFaceSturdy(world, blockPos, direction.getOpposite())) {
				double x = pos.getX();
				double y = pos.getY();
				double z = pos.getZ();
				if (direction == Direction.DOWN) {
					y -= 0.05;
					x += random.nextDouble();
					z += random.nextDouble();
				}
				else {
					y += random.nextDouble() * 0.8;
					if (direction.getAxis() == Direction.Axis.X) {
						z += random.nextDouble();
						if (direction == Direction.EAST) {
							++x;
						}
						else {
							x += 0.05;
						}
					}
					else {
						x += random.nextDouble();
						if (direction == Direction.SOUTH) {
							++z;
						}
						else {
							z += 0.05;
						}
					}
				}
				
				world.addParticle(ParticleTypes.DRIPPING_WATER, x, y, z, 0, 0, 0);
			}
		}
	}
	
	@Override
	public void playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
		BlocksHelper.setWithUpdate(world, pos, Blocks.AIR);
		if (!world.isClientSide()) {
			world.levelEvent(2001, pos, getId(state));
		}
		if (world.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS) && (player == null || !player.isCreative())) {
			ItemEntity drop = new ItemEntity(
				world,
				pos.getX() + 0.5,
				pos.getY() + 0.5,
				pos.getZ() + 0.5,
				new ItemStack(this)
			);
			world.addFreshEntity(drop);
		}
	}
	
	@Override
	public BCLRenderLayer getRenderLayer() {
		return BCLRenderLayer.CUTOUT;
	}
	
	@Override
	public FluidState getFluidState(BlockState state) {
		return Fluids.WATER.getSource(false);
	}
}
