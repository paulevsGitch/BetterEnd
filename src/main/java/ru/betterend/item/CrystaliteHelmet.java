package ru.betterend.item;

import java.util.UUID;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Rarity;
import ru.betterend.registry.EndAttributes;
import ru.betterend.registry.EndItems;

public class CrystaliteHelmet extends CrystaliteArmor {

	public CrystaliteHelmet() {
		super(EquipmentSlot.HEAD, EndItems.makeEndItemSettings().rarity(Rarity.RARE));
		UUID uuid = ARMOR_MODIFIER_UUID_PER_SLOT[EquipmentSlot.HEAD.getIndex()];
		addAttributeModifier(EndAttributes.BLINDNESS_RESISTANCE, new AttributeModifier(uuid, "Helmet blindness resistance", 1.0, AttributeModifier.Operation.ADDITION));
	}
}
