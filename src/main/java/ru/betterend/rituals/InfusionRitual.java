package ru.betterend.rituals;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import ru.betterend.blocks.entities.InfusionPedestalEntity;
import ru.betterend.blocks.entities.PedestalBlockEntity;
import ru.betterend.particle.InfusionParticleType;
import ru.betterend.recipe.builders.InfusionRecipe;

import java.awt.Point;
import java.util.Arrays;
import java.util.Objects;

public class InfusionRitual implements Container {
	private static final Point[] PEDESTALS_MAP = new Point[] {
		new Point(0, 3),
		new Point(2, 2),
		new Point(3, 0),
		new Point(2, -2),
		new Point(0, -3),
		new Point(-2, -2),
		new Point(-3, 0),
		new Point(-2, 2)
	};
	
	private Level world;
	private BlockPos worldPos;
	private InfusionRecipe activeRecipe;
	private boolean isDirty = false;
	private boolean hasRecipe = false;
	private int progress = 0;
	private int time = 0;
	
	private final PedestalBlockEntity[] catalysts = new PedestalBlockEntity[8];
	private final InfusionPedestalEntity input;
	
	public InfusionRitual(InfusionPedestalEntity pedestal, Level world, BlockPos pos) {
		this.input = pedestal;
		this.world = world;
		this.worldPos = pos;
		configure();
	}
	
	public void configure() {
		if (world == null || worldPos == null || world.isClientSide) return;
		for (int i = 0; i < catalysts.length; i++) {
			Point point = PEDESTALS_MAP[i];
			MutableBlockPos checkPos = worldPos.mutable().move(Direction.EAST, point.x).move(Direction.NORTH, point.y);
			BlockEntity catalystEntity = world.getBlockEntity(checkPos);
			if (catalystEntity instanceof PedestalBlockEntity) {
				catalysts[i] = (PedestalBlockEntity) catalystEntity;
			}
			else {
				catalysts[i] = null;
			}
		}
	}
	
	public boolean checkRecipe() {
		if (!isValid()) return false;
		InfusionRecipe recipe = world.getRecipeManager().getRecipeFor(InfusionRecipe.TYPE, this, world).orElse(null);
		if (hasRecipe()) {
			if (recipe == null) {
				reset();
				return false;
			}
			else if (activeRecipe == null || recipe.getInfusionTime() != time) {
				updateRecipe(recipe);
			}
			return true;
		}
		if (recipe != null) {
			updateRecipe(recipe);
			return true;
		}
		return false;
	}
	
	private void updateRecipe(InfusionRecipe recipe) {
		activeRecipe = recipe;
		hasRecipe = true;
		resetTimer();
		setChanged();
	}
	
	private void resetTimer() {
		time = activeRecipe != null ? activeRecipe.getInfusionTime() : 0;
		progress = 0;
	}
	
	public void reset() {
		activeRecipe = null;
		hasRecipe = false;
		resetTimer();
		setChanged();
	}
	
	public void tick() {
		if (isDirty) {
			configure();
			isDirty = false;
		}
		if (!checkRecipe()) return;
		progress++;
		if (progress == time) {
			clearContent();
			input.setItem(0, activeRecipe.assemble(this));
			reset();
		}
		else {
			ServerLevel serverLevel = (ServerLevel) world;
			BlockPos target = worldPos.above();
			double tx = target.getX() + 0.5;
			double ty = target.getY() + 0.5;
			double tz = target.getZ() + 0.5;
			for (PedestalBlockEntity catalyst : catalysts) {
				ItemStack stack = catalyst.getItem(0);
				if (!stack.isEmpty()) {
					BlockPos start = catalyst.getBlockPos();
					double sx = start.getX() + 0.5;
					double sy = start.getY() + 1.25;
					double sz = start.getZ() + 0.5;
					serverLevel.sendParticles(
						new InfusionParticleType(stack),
						sx,
						sy,
						sz,
						0,
						tx - sx,
						ty - sy,
						tz - sz,
						0.5
					);
				}
			}
		}
		
	}
	
	@Override
	public boolean canPlaceItem(int slot, ItemStack stack) {
		return isValid();
	}
	
	public boolean isValid() {
		if (world == null || world.isClientSide || worldPos == null || input == null) return false;
		return Arrays.stream(catalysts).noneMatch(Objects::isNull);
	}
	
	public boolean hasRecipe() {
		return hasRecipe;
	}
	
	public void setLocation(Level world, BlockPos pos) {
		this.world = world;
		this.worldPos = pos;
		this.isDirty = true;
	}
	
	@Override
	public void clearContent() {
		if (!isValid()) return;
		input.clearContent();
		Arrays.stream(catalysts).forEach(PedestalBlockEntity::clearContent);
	}
	
	@Override
	public int getContainerSize() {
		return 9;
	}
	
	@Override
	public boolean isEmpty() {
		return false;
	}
	
	@Override
	public ItemStack getItem(int slot) {
		if (slot > 8) return ItemStack.EMPTY;
		if (slot == 0) {
			return input.getItem(0);
		}
		else {
			return catalysts[slot - 1].getItem(0);
		}
	}
	
	@Override
	public ItemStack removeItem(int slot, int amount) {
		return removeItemNoUpdate(slot);
	}
	
	@Override
	public ItemStack removeItemNoUpdate(int slot) {
		if (slot > 8) return ItemStack.EMPTY;
		if (slot == 0) {
			return input.removeItemNoUpdate(0);
		}
		else {
			return catalysts[slot - 1].removeItemNoUpdate(0);
		}
	}
	
	@Override
	public void setItem(int slot, ItemStack stack) {
		if (slot > 8) return;
		if (slot == 0) {
			input.setItem(0, stack);
		}
		else {
			catalysts[slot - 1].setItem(0, stack);
		}
	}
	
	@Override
	public void setChanged() {
		if (isValid()) {
			input.setChanged();
			Arrays.stream(catalysts).forEach(PedestalBlockEntity::setChanged);
		}
	}
	
	public void markDirty() {
		this.isDirty = true;
	}
	
	@Override
	public boolean stillValid(Player player) {
		return true;
	}
	
	public void fromTag(CompoundTag tag) {
		if (tag.contains("recipe")) {
			hasRecipe = tag.getBoolean("recipe");
			progress = tag.getInt("progress");
			time = tag.getInt("time");
		}
	}
	
	public CompoundTag toTag(CompoundTag tag) {
		if (hasRecipe()) {
			tag.putBoolean("recipe", hasRecipe);
			tag.putInt("progress", progress);
			tag.putInt("time", time);
		}
		return tag;
	}
	
	public static Point[] getMap() {
		return PEDESTALS_MAP;
	}
}
