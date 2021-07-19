package ru.betterend.registry;

import net.minecraft.core.Registry;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import ru.betterend.BetterEnd;
import ru.betterend.item.EndAttribute;

public class EndAttributes {
	public final static Attribute BLINDNESS_RESISTANCE = registerAttribute("generic.blindness_resistance", 0.0, true);
	
	public static Attribute registerAttribute(String name, double value, boolean syncable) {
		return Registry.register(
			Registry.ATTRIBUTE,
			BetterEnd.makeID(name),
			new EndAttribute("attribute.name." + name, value).setSyncable(syncable)
		);
	}
	
	public static AttributeSupplier.Builder addLivingEntityAttributes(AttributeSupplier.Builder builder) {
		return builder.add(EndAttributes.BLINDNESS_RESISTANCE);
	}
}


