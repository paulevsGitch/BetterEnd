package ru.betterend.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import ru.betterend.effects.EndStatusEffects;
import ru.betterend.item.material.EndArmorMaterial;
import ru.betterend.registry.EndItems;

public class CrystaliteArmor extends EndArmorItem {

	protected final static TranslatableComponent CHEST_DESC;
	protected final static TranslatableComponent BOOTS_DESC;

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

	/*@Environment(EnvType.CLIENT)
	public static void registerTooltips() {
		ItemTooltipCallback.EVENT.register((stack, context, lines) -> {
			if (stack.getItem() instanceof CrystaliteArmor) {
				boolean hasSet = false;
				Player owner = Minecraft.getInstance().player;
				if (owner != null) {
					hasSet = hasFullSet(owner);
				}

				TranslatableComponent setDesc = new TranslatableComponent("tooltip.armor.crystalite_set");
				setDesc.setStyle(Style.EMPTY.applyFormats(hasSet ? ChatFormatting.BLUE : ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC));
				lines.add(TextComponent.EMPTY);
				lines.add(setDesc);

				if (stack.getItem() == EndItems.CRYSTALITE_CHESTPLATE) {
					lines.add(1, TextComponent.EMPTY);
					lines.add(2, CHEST_DESC);
				} else if (stack.getItem() == EndItems.CRYSTALITE_BOOTS) {
					lines.add(1, TextComponent.EMPTY);
					lines.add(2, BOOTS_DESC);
				}
			}
		});
	}*/

	static {
		Style descStyle = Style.EMPTY.applyFormats(ChatFormatting.DARK_AQUA, ChatFormatting.ITALIC);
		CHEST_DESC = new TranslatableComponent("tooltip.armor.crystalite_chest");
		CHEST_DESC.setStyle(descStyle);
		BOOTS_DESC = new TranslatableComponent("tooltip.armor.crystalite_boots");
		BOOTS_DESC.setStyle(descStyle);
	}
}
