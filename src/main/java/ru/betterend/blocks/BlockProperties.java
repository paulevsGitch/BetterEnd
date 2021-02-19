package ru.betterend.blocks;

import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.StringIdentifiable;
import ru.betterend.registry.EndPortals;

public class BlockProperties {
	public static final EnumProperty<HydraluxShape> HYDRALUX_SHAPE = EnumProperty.of("shape", HydraluxShape.class);
	public final static EnumProperty<PedestalState> PEDESTAL_STATE = EnumProperty.of("state", PedestalState.class);
	public static final EnumProperty<TripleShape> TRIPLE_SHAPE = EnumProperty.of("shape", TripleShape.class);
	public static final EnumProperty<PentaShape> PENTA_SHAPE = EnumProperty.of("shape", PentaShape.class);
	
	public static final BooleanProperty HAS_LIGHT = BooleanProperty.of("has_light");
	public static final BooleanProperty HAS_ITEM = BooleanProperty.of("has_item");
	public static final BooleanProperty NATURAL = BooleanProperty.of("natural");
	public static final BooleanProperty ACTIVE = BooleanProperty.of("active");
	
	public static final IntProperty DESTRUCTION_LONG = IntProperty.of("destruction", 0, 8);
	public static final IntProperty DESTRUCTION = IntProperty.of("destruction", 0, 2);
	public static final IntProperty ROTATION = IntProperty.of("rotation", 0, 3);
	public static final IntProperty FULLNESS = IntProperty.of("fullness", 0, 3);
	public static final IntProperty COLOR = IntProperty.of("color", 0, 7);
	public static final IntProperty PORTAL = IntProperty.of("portal", 0, EndPortals.getCount());
	
	public static enum TripleShape implements StringIdentifiable {
		TOP("top"),
		MIDDLE("middle"),
		BOTTOM("bottom");
		
		private final String name;
		
		TripleShape(String name) {
			this.name = name;
		}

		@Override
		public String asString() {
			return name;
		}
		
		@Override
		public String toString() {
			return name;
		}
	}

	public static enum PedestalState implements StringIdentifiable {
		PEDESTAL_TOP("pedestal_top"),
		COLUMN_TOP("column_top"),
		BOTTOM("bottom"),
		PILLAR("pillar"),
		COLUMN("column"),
		DEFAULT("default");
		
		private final String name;
		
		PedestalState(String name) {
			this.name = name;
		}
	
		@Override
		public String asString() {
			return this.name;
		}
		
		@Override
		public String toString() {
			return this.name;
		}
	}
	
	public static enum HydraluxShape implements StringIdentifiable {
		FLOWER_BIG_BOTTOM("flower_big_bottom", true),
		FLOWER_BIG_TOP("flower_big_top", true),
		FLOWER_SMALL_BOTTOM("flower_small_bottom", true),
		FLOWER_SMALL_TOP("flower_small_top", true),
		VINE("vine", false),
		ROOTS("roots", false);
		
		private final String name;
		private final boolean glow;
		
		HydraluxShape(String name, boolean glow) {
			this.name = name;
			this.glow = glow;
		}

		@Override
		public String asString() {
			return name;
		}
		
		@Override
		public String toString() {
			return name;
		}
		
		public boolean hasGlow() {
			return glow;
		}
	}
	
	public static enum PentaShape implements StringIdentifiable {
		BOTTOM("bottom"),
		PRE_BOTTOM("pre_bottom"),
		MIDDLE("middle"),
		PRE_TOP("pre_top"),
		TOP("top");
		
		private final String name;
		
		PentaShape(String name) {
			this.name = name;
		}

		@Override
		public String asString() {
			return name;
		}
		
		@Override
		public String toString() {
			return name;
		}
	}
	
	public static enum LumecornShape implements StringIdentifiable {
		LIGHT_TOP("light_top", 15),
		LIGHT_TOP_MIDDLE("light_top_middle", 15),
		LIGHT_MIDDLE("light_middle", 15),
		LIGHT_BOTTOM("light_bottom", 15),
		MIDDLE("middle", 0),
		BOTTOM_BIG("bottom_big", 0),
		BOTTOM_SMALL("bottom_small", 0);
		
		private final String name;
		private final int light;
		
		LumecornShape(String name, int light) {
			this.name = name;
			this.light = light;
		}

		@Override
		public String asString() {
			return name;
		}
		
		@Override
		public String toString() {
			return name;
		}
		
		public int getLight() {
			return light;
		}
	}
}
