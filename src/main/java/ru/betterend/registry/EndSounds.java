package ru.betterend.registry;

import net.minecraft.core.Registry;
import net.minecraft.sounds.SoundEvent;
import ru.betterend.BetterEnd;

public class EndSounds {
	// Music
	public static final SoundEvent MUSIC_FOREST = register("music", "forest");
	public static final SoundEvent MUSIC_WATER = register("music", "water");
	public static final SoundEvent MUSIC_DARK = register("music", "dark");
	public static final SoundEvent MUSIC_OPENSPACE = register("music", "openspace");
	public static final SoundEvent MUSIC_CAVES = register("music", "caves");
	
	// Ambient
	public static final SoundEvent AMBIENT_FOGGY_MUSHROOMLAND = register("ambient", "foggy_mushroomland");
	public static final SoundEvent AMBIENT_CHORUS_FOREST = register("ambient", "chorus_forest");
	public static final SoundEvent AMBIENT_MEGALAKE = register("ambient", "megalake");
	public static final SoundEvent AMBIENT_DUST_WASTELANDS = register("ambient", "dust_wastelands");
	public static final SoundEvent AMBIENT_MEGALAKE_GROVE = register("ambient", "megalake_grove");
	public static final SoundEvent AMBIENT_BLOSSOMING_SPIRES = register("ambient", "blossoming_spires");
	public static final SoundEvent AMBIENT_SULPHUR_SPRINGS = register("ambient", "sulphur_springs");
	public static final SoundEvent AMBIENT_UMBRELLA_JUNGLE = register("ambient", "umbrella_jungle");
	public static final SoundEvent AMBIENT_GLOWING_GRASSLANDS = register("ambient", "glowing_grasslands");
	public static final SoundEvent AMBIENT_CAVES = register("ambient", "caves");
	public static final SoundEvent AMBIENT_AMBER_LAND = register("ambient", "amber_land");
	
	// Entity
	public static final SoundEvent ENTITY_DRAGONFLY = register("entity", "dragonfly");
	public static final SoundEvent ENTITY_SHADOW_WALKER = register("entity", "shadow_walker");
	public static final SoundEvent ENTITY_SHADOW_WALKER_DAMAGE = register("entity", "shadow_walker_damage");
	public static final SoundEvent ENTITY_SHADOW_WALKER_DEATH = register("entity", "shadow_walker_death");
	
	// Records
	public static final SoundEvent RECORD_STRANGE_AND_ALIEN = register("record", "strange_and_alien");
	public static final SoundEvent RECORD_GRASPING_AT_STARS = register("record", "grasping_at_stars");
	public static final SoundEvent RECORD_ENDSEEKER = register("record", "endseeker");
	public static final SoundEvent RECORD_EO_DRACONA = register("record", "eo_dracona");
	
	public static void register() {
	}
	
	private static SoundEvent register(String type, String id) {
		id = "betterend." + type + "." + id;
		return Registry.register(Registry.SOUND_EVENT, id, new SoundEvent(BetterEnd.makeID(id)));
	}
}
