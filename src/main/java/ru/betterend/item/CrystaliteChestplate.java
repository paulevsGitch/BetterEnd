package ru.betterend.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import ru.betterend.effects.EndStatusEffects;
import ru.betterend.interfaces.MobEffectApplier;
import ru.betterend.registry.EndItems;

import java.util.List;

public class CrystaliteChestplate extends CrystaliteArmor implements MobEffectApplier {

	public CrystaliteChestplate() {
		super(EquipmentSlot.CHEST, EndItems.makeItemSettings().rarity(Rarity.RARE));
	}

	@Override
	public void applyEffect(LivingEntity owner) {
		owner.addEffect(new MobEffectInstance(EndStatusEffects.CRYSTALITE_DIG_SPEED));
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> lines, TooltipFlag tooltip) {
		super.appendHoverText(stack, level, lines, tooltip);
		lines.add(1, TextComponent.EMPTY);
		lines.add(2, CHEST_DESC);
	}
}
