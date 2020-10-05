package ru.betterend.registry;

import net.minecraft.sound.SoundEvent;
import net.minecraft.util.registry.Registry;
import ru.betterend.BetterEnd;

public class SoundRegistry
{
	// Music
	public static final SoundEvent MUSIC_FOGGY_MUSHROOMLAND = register("music", "foggy_mushroomland");
	
	// Ambient
	public static final SoundEvent AMBIENT_FOGGY_MUSHROOMLAND = register("ambient", "foggy_mushroomland");
	
	// Entity
	public static final SoundEvent ENTITY_DRAGONFLY = register("entity", "dragonfly");
	
	public static void register() {}
	
	private static SoundEvent register(String type, String id)
	{
		id = "betterend." + type + "." + id;
		return Registry.register(Registry.SOUND_EVENT, id, new SoundEvent(BetterEnd.makeID(id)));
	}
}
