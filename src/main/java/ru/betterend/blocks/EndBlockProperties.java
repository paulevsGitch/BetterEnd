package ru.betterend.blocks;

import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import ru.bclib.blocks.BlockProperties;
import ru.betterend.registry.EndPortals;

public class EndBlockProperties extends BlockProperties {
	public static final EnumProperty<HydraluxShape> HYDRALUX_SHAPE = EnumProperty.create("shape", HydraluxShape.class);
	public static final EnumProperty<PedestalState> PEDESTAL_STATE = EnumProperty.create("state", PedestalState.class);
	public static final EnumProperty<CactusBottom> CACTUS_BOTTOM = EnumProperty.create("bottom", CactusBottom.class);
	
	public static final BooleanProperty HAS_ITEM = BooleanProperty.create("has_item");
	public static final IntegerProperty PORTAL = IntegerProperty.create("portal", 0, EndPortals.getCount());
	
	public enum PedestalState implements StringRepresentable {
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
		public String getSerializedName() {
			return this.name;
		}
		
		@Override
		public String toString() {
			return this.name;
		}
	}
	
	public enum HydraluxShape implements StringRepresentable {
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
		public String getSerializedName() {
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
	
	public enum LumecornShape implements StringRepresentable {
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
		public String getSerializedName() {
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
	
	public enum CactusBottom implements StringRepresentable {
		EMPTY("empty"),
		SAND("sand"),
		MOSS("moss");
		
		private final String name;
		
		CactusBottom(String name) {
			this.name = name;
		}

		@Override
		public String getSerializedName() {
			return name;
		}
		
		@Override
		public String toString() {
			return name;
		}
	}
}
