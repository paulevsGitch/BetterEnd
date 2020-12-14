package ru.betterend.registry;

import net.minecraft.sound.SoundEvent;
import net.minecraft.util.registry.Registry;
import ru.betterend.BetterEnd;

public class EndSounds {
	// Music
	public static final SoundEvent MUSIC_FOGGY_MUSHROOMLAND = register("music", "foggy_mushroomland");
	public static final SoundEvent MUSIC_CHORUS_FOREST = register("music", "chorus_forest");
	public static final SoundEvent MUSIC_CRYSTAL_MOUNTAINS = register("music", "crystal_mountains");
	public static final SoundEvent MUSIC_MEGALAKE = register("music", "megalake");
	public static final SoundEvent MUSIC_DUST_WASTELANDS = register("music", "dust_wastelands");
	public static final SoundEvent MUSIC_SHADOW_FOREST = register("music", "shadow_forest");
	public static final SoundEvent MUSIC_BLOSSOMING_SPIRES = register("music", "blossoming_spires");
	public static final SoundEvent MUSIC_AMBER_LAND = register("music", "amber_land");
	public static final SoundEvent MUSIC_SULPHUR_SPRINGS = register("music", "sulphur_springs");
	
	// Ambient
	public static final SoundEvent AMBIENT_FOGGY_MUSHROOMLAND = register("ambient", "foggy_mushroomland");
	public static final SoundEvent AMBIENT_CHORUS_FOREST = register("ambient", "chorus_forest");
	public static final SoundEvent AMBIENT_MEGALAKE = register("ambient", "megalake");
	public static final SoundEvent AMBIENT_DUST_WASTELANDS = register("ambient", "dust_wastelands");
	public static final SoundEvent AMBIENT_MEGALAKE_GROVE = register("ambient", "megalake_grove");
	public static final SoundEvent AMBIENT_BLOSSOMING_SPIRES = register("ambient", "blossoming_spires");
	public static final SoundEvent AMBIENT_SULPHUR_SPRINGS = register("ambient", "sulphur_springs");
	
	// Entity
	public static final SoundEvent ENTITY_DRAGONFLY = register("entity", "dragonfly");
	public static final SoundEvent ENTITY_SHADOW_WALKER = register("entity", "shadow_walker");
	public static final SoundEvent ENTITY_SHADOW_WALKER_DAMAGE = register("entity", "shadow_walker_damage");
	public static final SoundEvent ENTITY_SHADOW_WALKER_DEATH = register("entity", "shadow_walker_death");
	
	public static void register() {}
	
	private static SoundEvent register(String type, String id) {
		id = "betterend." + type + "." + id;
		return Registry.register(Registry.SOUND_EVENT, id, new SoundEvent(BetterEnd.makeID(id)));
	}
}
