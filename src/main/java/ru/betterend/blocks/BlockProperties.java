package ru.betterend.blocks;

import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.StringIdentifiable;

public class BlockProperties {
	public static final EnumProperty<TripleShape> TRIPLE_SHAPE = EnumProperty.of("shape", TripleShape.class);
	
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
}
