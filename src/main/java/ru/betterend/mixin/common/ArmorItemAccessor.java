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
	@Accessor("ARMOR_MODIFIER_UUID_PER_SLOT")
	UUID[] be_getModifiers();

	@Accessor("defaultModifiers")
	Multimap<Attribute, AttributeModifier> be_getDefaultModifiers();

	@Accessor("defaultModifiers")
	void be_setDefaultModifiers(Multimap<Attribute, AttributeModifier> attributeModifiers);
}
