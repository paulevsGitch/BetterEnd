package ru.betterend.mixin.common;

import java.util.UUID;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import com.google.common.collect.Multimap;

import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ArmorItem;

@Mixin(ArmorItem.class)
public interface ArmorItemAccessor {
	@Accessor("MODIFIERS")
	UUID[] be_getModifiers();

	@Accessor("attributeModifiers")
	Multimap<Attribute, AttributeModifier> be_getAttributeModifiers();

	@Accessor("attributeModifiers")
	void be_setAttributeModifiers(Multimap<Attribute, AttributeModifier> attributeModifiers);
}
