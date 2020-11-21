package ru.betterend.blocks;

import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.StringIdentifiable;

public class BlockProperties {
	public static final EnumProperty<TripleShape> TRIPLE_SHAPE = EnumProperty.of("shape", TripleShape.class);
	public final static EnumProperty<PedestalState> PEDESTAL_STATE = EnumProperty.of("state", PedestalState.class);
	public static final BooleanProperty HAS_ITEM = BooleanProperty.of("has_item");
	public static final BooleanProperty HAS_LIGHT = BooleanProperty.of("has_light");
	public static final BooleanProperty ACTIVATED = BooleanProperty.of("active");
	
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
}
