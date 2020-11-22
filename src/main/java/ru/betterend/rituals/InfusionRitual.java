package ru.betterend.rituals;

import java.awt.Point;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import ru.betterend.blocks.entities.InfusionPedestalEntity;
import ru.betterend.blocks.entities.PedestalBlockEntity;
import ru.betterend.particle.InfusionParticleType;
import ru.betterend.recipe.builders.InfusionRecipe;

public class InfusionRitual implements Inventory {
	private static Point[] pedestalsMap = new Point[] {
		new Point(0, 3), new Point(2, 2), new Point(3, 0), new Point(2, -2),
		new Point(0, -3), new Point(-2, -2), new Point(-3, 0), new Point(-2, 2)
	};

	private World world;
	private BlockPos worldPos;
	private InfusionRecipe activeRecipe;
	private boolean isDirty = false;
	private boolean hasRecipe = false;
	private int progress = 0;
	private int time = 0;
	
	private InfusionPedestalEntity input;
	private PedestalBlockEntity[] catalysts = new PedestalBlockEntity[8];
	
	public InfusionRitual(World world, BlockPos pos) {
		this.world = world;
		this.worldPos = pos;
		this.configure();
	}
	
	public void configure() {
		if (world == null || world.isClient || worldPos == null) return;
		BlockEntity inputEntity = world.getBlockEntity(worldPos);
		if (inputEntity instanceof InfusionPedestalEntity) {
			this.input = (InfusionPedestalEntity) inputEntity;
		}
		int i = 0;
		for(Point point : pedestalsMap) {
			BlockPos.Mutable checkPos = worldPos.mutableCopy().move(Direction.EAST, point.x).move(Direction.NORTH, point.y);
			BlockEntity catalystEntity = world.getBlockEntity(checkPos);
			if (catalystEntity instanceof PedestalBlockEntity) {
				catalysts[i] = (PedestalBlockEntity) catalystEntity;
				i++;
			} else {
				break;
			}
		}
	}
	
	public boolean checkRecipe() {
		if (!isValid()) return false;
		InfusionRecipe recipe = this.world.getRecipeManager().getFirstMatch(InfusionRecipe.TYPE, this, world).orElse(null);
		if (hasRecipe()) {
			if (recipe == null) {
				this.stop();
				return false;
			} else if (recipe.getInfusionTime() != time) {
				this.activeRecipe = recipe;
				this.time = this.activeRecipe.getInfusionTime();
				this.progress = 0;
				this.markDirty();
			} else if (activeRecipe == null) {
				this.activeRecipe = recipe;
			}
			return true;
		}
		if (recipe != null) {
			this.activeRecipe = recipe;
			this.time = this.activeRecipe.getInfusionTime();
			this.hasRecipe = true;
			this.progress = 0;
			this.markDirty();
			return true;
		}
		return false;
	}
	
	public void stop() {
		this.activeRecipe = null;
		this.hasRecipe = false;
		this.progress = 0;
		this.time = 0;
		this.markDirty();
	}
	
	public void tick() {
		if (isDirty) {
			this.configure();
			this.isDirty = false;
		}
		if (!isValid() || !hasRecipe()) return;
		if (!checkRecipe()) return;
		this.progress++;
		if (progress == time) {
			BlockState inputState = world.getBlockState(input.getPos());
			this.input.removeStack(world, inputState);
			this.input.setStack(world, inputState, activeRecipe.craft(this));
			for (PedestalBlockEntity catalyst : catalysts) {
				catalyst.removeStack(world, world.getBlockState(catalyst.getPos()));
			}
			this.stop();
		} else {
			ServerWorld world = (ServerWorld) this.world;
			BlockPos target = this.worldPos.up();
			double tx = target.getX() + 0.5;
			double ty = target.getY() + 0.5;
			double tz = target.getZ() + 0.5;
			for (PedestalBlockEntity catalyst : catalysts) {
				ItemStack stack = catalyst.getStack(0);
				if (!stack.isEmpty()) {
					BlockPos start = catalyst.getPos();
					double sx = start.getX() + 0.5;
					double sy = start.getY() + 1.25;
					double sz = start.getZ() + 0.5;
					world.spawnParticles(new InfusionParticleType(stack), sx, sy, sz, 0, tx - sx, ty - sy, tz - sz, 0.5);
				}
			}
		}
		
	}
	
	@Override
	public boolean isValid(int slot, ItemStack stack) {
		return this.isValid();
	}
	
	public boolean isValid() {
		if (world == null || world.isClient || worldPos == null || input == null) return false;
		for (PedestalBlockEntity catalyst : catalysts) {
			if (catalyst == null) return false;
		}
		return true;
	}
	
	public boolean hasRecipe() {
		return this.hasRecipe;
	}

	public void setLocation(World world, BlockPos pos) {
		this.world = world;
		this.worldPos = pos;
		this.isDirty = true;
	}

	@Override
	public void clear() {
		if (!isValid()) return;
		this.input.clear();
		for (PedestalBlockEntity catalyst : catalysts) {
			catalyst.clear();
		}
	}

	@Override
	public int size() {
		return 9;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public ItemStack getStack(int slot) {
		if (slot > 8) return ItemStack.EMPTY;
		if (slot == 0) {
			return this.input.getStack(0);
		} else {
			return this.catalysts[slot - 1].getStack(0);
		}
	}

	@Override
	public ItemStack removeStack(int slot, int amount) {
		return this.removeStack(slot);
	}

	@Override
	public ItemStack removeStack(int slot) {
		if (slot > 8) return ItemStack.EMPTY;
		if (slot == 0) {
			return this.input.removeStack(0);
		} else {
			return this.catalysts[slot - 1].getStack(0);
		}
	}

	@Override
	public void setStack(int slot, ItemStack stack) {
		if (slot > 8) return;
		if (slot == 0) {
			this.input.setStack(0, stack);
		} else {
			this.catalysts[slot - 1].setStack(0, stack);
		}
	}

	@Override
	public void markDirty() {
		if (isValid()) {
			this.input.markDirty();
			for (PedestalBlockEntity catalyst : catalysts) {
				catalyst.markDirty();
			}
		}
	}

	@Override
	public boolean canPlayerUse(PlayerEntity player) {
		return true;
	}
	
	public void fromTag(CompoundTag tag) {
		if (tag.contains("recipe")) {
			this.hasRecipe = tag.getBoolean("recipe");
			this.progress = tag.getInt("progress");
			this.time = tag.getInt("time");
		}
	}

	public CompoundTag toTag(CompoundTag tag) {
		if (hasRecipe()) {
			tag.putBoolean("recipe", this.hasRecipe);
			tag.putInt("progress", progress);
			tag.putInt("time", time);
		}
		return tag;
	}
}
