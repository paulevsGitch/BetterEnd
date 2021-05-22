package ru.betterend.blocks.basis;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.StoneButtonBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import org.jetbrains.annotations.Nullable;
import ru.betterend.client.models.BlockModelProvider;
import ru.betterend.client.models.Patterns;

public class EndStoneButtonBlock extends EndButtonBlock {

	public EndStoneButtonBlock(Block source) {
		super(source, FabricBlockSettings.copyOf(source).noOcclusion(), false);
	}

	@Override
	protected SoundEvent getSound(boolean clicked) {
		return clicked ? SoundEvents.STONE_BUTTON_CLICK_ON : SoundEvents.STONE_BUTTON_CLICK_OFF;
	}
}
