package ru.betterend.blocks.basis;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.entity.player.PlayerEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public class StrippableBarkBlock extends BarkBlock {
	private final Block striped;

	public StrippableBarkBlock(MaterialColor color, Block striped) {
		super(FabricBlockSettings.copyOf(striped).materialColor(color));
		this.striped = striped;
	}

	@Override
	public ActionResult onUse(BlockState state, Level world, BlockPos pos, PlayerEntity player, Hand hand,
			BlockHitResult hit) {
		if (player.getMainHandStack().getItem().isIn(FabricToolTags.AXES)) {
			world.playLocalSound(player, pos, SoundEvents.ITEM_AXE_STRIP, SoundSource.BLOCKS, 1.0F, 1.0F);
			if (!world.isClientSide) {
				world.setBlockAndUpdate(pos, striped.defaultBlockState().with(RotatedPillarBlock.AXIS,
						state.getValue(RotatedPillarBlock.AXIS)), 11);
				if (player != null && !player.isCreative()) {
					player.getMainHandStack().damage(1, world.random, (ServerPlayer) player);
				}
			}
			return ActionResult.SUCCESS;
		}
		return ActionResult.FAIL;
	}
}
