package ru.betterend.item;

import java.util.List;

import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import org.jetbrains.annotations.Nullable;

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
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import ru.betterend.effects.EndStatusEffects;
import ru.betterend.item.material.EndArmorMaterial;
import ru.betterend.registry.EndItems;

public class CrystaliteArmor extends EndArmorItem {

	public final static TranslatableComponent CHEST_DESC;
	public final static TranslatableComponent BOOTS_DESC;

	public CrystaliteArmor(EquipmentSlot equipmentSlot, Properties settings) {
		super(EndArmorMaterial.CRYSTALITE, equipmentSlot, settings);
	}

	public static boolean hasFullSet(LivingEntity owner) {
		for (ItemStack armorStack : owner.getArmorSlots()) {
			if (!(armorStack.getItem() instanceof CrystaliteArmor)) {
				return false;
			}
		}
		return true;
	}

	public static void applySetEffect(LivingEntity owner) {
		owner.addEffect(new MobEffectInstance(EndStatusEffects.CRYSTALITE_HEALTH_REGEN));
	}

	static {
		Style descStyle = Style.EMPTY.applyFormats(ChatFormatting.DARK_AQUA, ChatFormatting.ITALIC);
		CHEST_DESC = new TranslatableComponent("tooltip.armor.crystalite_chest");
		CHEST_DESC.setStyle(descStyle);
		BOOTS_DESC = new TranslatableComponent("tooltip.armor.crystalite_boots");
		BOOTS_DESC.setStyle(descStyle);
	}
}
