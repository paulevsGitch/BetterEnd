package ru.betterend.item;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import ru.bclib.interfaces.ItemModelProvider;

import java.util.UUID;

public class EndArmorItem extends ArmorItem implements ItemModelProvider {
	
	protected static final UUID[] ARMOR_MODIFIER_UUID_PER_SLOT = new UUID[] {
		UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6B"),
		UUID.fromString("D8499B04-0E66-4726-AB29-64469D734E0D"),
		UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E"),
		UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB150")
	};
	
	protected final Multimap<Attribute, AttributeModifier> defaultModifiers;
	
	public EndArmorItem(ArmorMaterial material, EquipmentSlot equipmentSlot, Properties settings) {
		super(material, equipmentSlot, settings);
		this.defaultModifiers = HashMultimap.create();
		UUID uuid = ARMOR_MODIFIER_UUID_PER_SLOT[equipmentSlot.getIndex()];
		addAttributeModifier(
			Attributes.ARMOR,
			new AttributeModifier(uuid, "Armor modifier", getDefense(), AttributeModifier.Operation.ADDITION)
		);
		addAttributeModifier(
			Attributes.ARMOR_TOUGHNESS,
			new AttributeModifier(uuid, "Armor toughness", getToughness(), AttributeModifier.Operation.ADDITION)
		);
		if (knockbackResistance > 0.0F) {
			addAttributeModifier(
				Attributes.KNOCKBACK_RESISTANCE,
				new AttributeModifier(uuid,
					"Armor knockback resistance",
					knockbackResistance,
					AttributeModifier.Operation.ADDITION
				)
			);
		}
	}
	
	@Override
	public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot equipmentSlot) {
		return equipmentSlot == slot ? defaultModifiers : super.getDefaultAttributeModifiers(equipmentSlot);
	}
	
	protected void addAttributeModifier(Attribute attribute, AttributeModifier modifier) {
		if (defaultModifiers.containsKey(attribute)) {
			defaultModifiers.removeAll(attribute);
		}
		defaultModifiers.put(attribute, modifier);
	}
}
