package ru.betterend.item;

import java.util.UUID;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import ru.betterend.mixin.common.ArmorItemAccessor;
import ru.betterend.patterns.Patterned;
import ru.betterend.patterns.Patterns;

public class EndArmorItem extends ArmorItem implements Patterned {
	public EndArmorItem(ArmorMaterial material, EquipmentSlot slot, Item.Settings settings) {
		super(material, slot, settings);

		addKnockbackResistance((ArmorItemAccessor) this, slot, this.knockbackResistance);
	}

	/** Ensures knockback resistance is actually applied */
	private static void addKnockbackResistance(ArmorItemAccessor accessor, EquipmentSlot slot, double knockbackResistance) {
		if (knockbackResistance == 0) {
			return;
		}

		Multimap<EntityAttribute, EntityAttributeModifier> attributeModifiers = accessor.be_getAttributeModifiers();

		// In case Mojang or anyone else decided to fix this
		if (attributeModifiers.keys().contains(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE)) {
			return;
		}

		UUID uuid = accessor.be_getModifiers()[slot.getEntitySlotId()];

		// Rebuild attributeModifiers to include knockback resistance
		ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
		builder.putAll(attributeModifiers);
		builder.put(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, new EntityAttributeModifier(uuid, "Armor knockback resistance", knockbackResistance, EntityAttributeModifier.Operation.ADDITION));
		accessor.be_setAttributeModifiers(builder.build());
	}

	@Override
	public String getModelPattern(String name) {
		return Patterns.createJson(Patterns.ITEM_GENERATED, name);
	}
}
