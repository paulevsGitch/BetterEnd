package ru.betterend.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Rarity;
import ru.betterend.effects.EndStatusEffects;
import ru.betterend.interfaces.MobEffectApplier;
import ru.betterend.registry.EndItems;

public class CrystaliteBoots extends CrystaliteArmor implements MobEffectApplier {

	public CrystaliteBoots() {
		super(EquipmentSlot.FEET, EndItems.makeItemSettings().rarity(Rarity.RARE));
	}

	@Override
	public void applyEffect(LivingEntity owner) {
		owner.addEffect(new MobEffectInstance(EndStatusEffects.CRYSTALITE_MOVE_SPEED));
	}
}
