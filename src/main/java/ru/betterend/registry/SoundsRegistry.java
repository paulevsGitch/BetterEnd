package ru.betterend.registry;

import net.minecraft.sound.SoundEvent;
import net.minecraft.util.registry.Registry;
import ru.betterend.BetterEnd;

public class SoundsRegistry
{
	public static final SoundEvent AMBIENT_FOGGY_MUSHROOMLAND = registerAmbient("foggy_mushroomland");
	
	public static void register() {}
	
	private static SoundEvent registerAmbient(String id)
	{
		id = "betterend.ambient." + id;
		return Registry.register(Registry.SOUND_EVENT, id, new SoundEvent(BetterEnd.makeID(id)));
	}
}
