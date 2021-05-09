package ru.betterend.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import ru.betterend.effects.EndStatusEffects;
import ru.betterend.item.material.EndArmorMaterial;

public class CrystaliteArmor extends EndArmorItem {

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
}
