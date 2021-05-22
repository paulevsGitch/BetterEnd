package ru.betterend.blocks.basis;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WoodButtonBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import ru.betterend.client.models.BlockModelProvider;
import ru.betterend.client.models.Patterns;

public class EndWoodenButtonBlock extends EndButtonBlock {

	public EndWoodenButtonBlock(Block source) {
		super(source, FabricBlockSettings.copyOf(source).strength(0.5F, 0.5F).noOcclusion(), true);
	}

	@Override
	protected SoundEvent getSound(boolean clicked) {
		return clicked ? SoundEvents.WOODEN_BUTTON_CLICK_ON : SoundEvents.WOODEN_BUTTON_CLICK_OFF;
	}
}
