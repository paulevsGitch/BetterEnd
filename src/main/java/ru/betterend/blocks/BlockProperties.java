package ru.betterend.blocks;

import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.StringIdentifiable;

public class BlockProperties {
	public static final EnumProperty<TripleShape> TRIPLE_SHAPE = EnumProperty.of("shape", TripleShape.class);
	public final static EnumProperty<PedestalState> PEDESTAL_STATE = EnumProperty.of("state", PedestalState.class);
	public static final EnumProperty<HydraluxShape> HYDRALUX_SHAPE = EnumProperty.of("shape", HydraluxShape.class);
	public static final EnumProperty<PentaShape> PENTA_SHAPE = EnumProperty.of("shape", PentaShape.class);
	public static final BooleanProperty HAS_ITEM = BooleanProperty.of("has_item");
	public static final BooleanProperty HAS_LIGHT = BooleanProperty.of("has_light");
	public static final BooleanProperty ACTIVE = BooleanProperty.of("active");
	public static final IntProperty ROTATION = IntProperty.of("rotation", 0, 3);
	public static final BooleanProperty NATURAL = BooleanProperty.of("natural");
	
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
}
