package ru.betterend.mixin.common;

import com.google.common.collect.Multimap;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.ArmorItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.UUID;

@Mixin(ArmorItem.class)
public interface ArmorItemAccessor {
	@Accessor("MODIFIERS")
	UUID[] be_getModifiers();

	@Accessor("attributeModifiers")
	Multimap<EntityAttribute, EntityAttributeModifier> be_getAttributeModifiers();

	@Accessor("attributeModifiers")
	void be_setAttributeModifiers(Multimap<EntityAttribute, EntityAttributeModifier> attributeModifiers);
}
