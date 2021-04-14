package ru.betterend.item;

import java.util.UUID;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import ru.betterend.mixin.common.ArmorItemAccessor;
import ru.betterend.patterns.Patterned;
import ru.betterend.patterns.Patterns;

public class EndArmorItem extends ArmorItem implements Patterned {
	public EndArmorItem(ArmorMaterial material, EquipmentSlot slot, Item.Properties settings) {
		super(material, slot, settings);

		addKnockbackResistance((ArmorItemAccessor) this, slot, this.knockbackResistance);
	}

	/** Ensures knockback resistance is actually applied */
	private static void addKnockbackResistance(ArmorItemAccessor accessor, EquipmentSlot slot, double knockbackResistance) {
		if (knockbackResistance == 0) {
			return;
		}

		Multimap<Attribute, AttributeModifier> attributeModifiers = accessor.be_getDefaultModifiers();

		// In case Mojang or anyone else decided to fix this
		if (attributeModifiers.keys().contains(Attributes.KNOCKBACK_RESISTANCE)) {
			return;
		}

		UUID uuid = accessor.be_getModifiers()[slot.getIndex()];

		// Rebuild attributeModifiers to include knockback resistance
		ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
		builder.putAll(attributeModifiers);
		builder.put(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(uuid, "Armor knockback resistance", knockbackResistance, AttributeModifier.Operation.ADDITION));
		accessor.be_setDefaultModifiers(builder.build());
	}

	@Override
	public String getModelPattern(String name) {
		return Patterns.createJson(Patterns.ITEM_GENERATED, name);
	}
}
