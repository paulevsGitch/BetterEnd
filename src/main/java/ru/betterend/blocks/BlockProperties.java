package ru.betterend.blocks;

import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.StringIdentifiable;

public class BlockProperties {
	public static final EnumProperty<TripleShape> TRIPLE_SHAPE = EnumProperty.of("shape", TripleShape.class);
	public final static EnumProperty<State> STATE = EnumProperty.of("state", State.class);
	public static final BooleanProperty HAS_ITEM = BooleanProperty.of("has_item");
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

	public static enum State implements StringIdentifiable {
		DEFAULT,
		BOTTOM,
		TOP,
		PILLAR;
	
		@Override
		public String asString() {
			return this.name().toLowerCase();
		}
	}
}
