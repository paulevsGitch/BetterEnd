package ru.betterend.blocks.basis;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.AnvilBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.level.storage.loot.LootContext;
import org.jetbrains.annotations.NotNull;
import ru.bclib.blocks.BaseAnvilBlock;
import ru.betterend.blocks.complex.MetalMaterial;
import ru.betterend.item.EndAnvilItem;

import java.util.List;
import java.util.Objects;

public class EndAnvilBlock extends BaseAnvilBlock {
	
	protected final int level;
	protected final Item anvilItem;
	protected IntegerProperty durability;
	protected MetalMaterial metalMaterial;
	protected int maxDurability;
	
	public EndAnvilBlock(MaterialColor color, int level) {
		super(color);
		this.anvilItem = new EndAnvilItem(this);
		this.level = level;
	}
	
	public EndAnvilBlock(MetalMaterial metalMaterial, MaterialColor color, int level) {
		this(color, level);
		this.metalMaterial = metalMaterial;
	}
	
	public int getDurability(BlockState blockState) {
		Block anvilBlock = blockState.getBlock();
		if (anvilBlock instanceof EndAnvilBlock) {
			blockState.getValue(durability);
		}
		return 0;
	}
	
	public IntegerProperty getDurability() {
		if (durability == null) {
			this.maxDurability = 5;
			this.durability = IntegerProperty.create("durability", 0, maxDurability);
		}
		return durability;
	}
	
	public int getMaxDurability() {
		return maxDurability;
	}
	
	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		List<ItemStack> drops = super.getDrops(state, builder);
		ItemStack itemStack = drops.get(0);
		itemStack.getOrCreateTag().putInt(EndAnvilItem.DURABILITY, state.getValue(durability));
		return drops;
	}
	
	@Override
	public Item asItem() {
		return anvilItem;
	}
	
	@Override
	public BlockState getStateForPlacement(@NotNull BlockPlaceContext blockPlaceContext) {
		return Objects.requireNonNull(super.getStateForPlacement(blockPlaceContext))
					  .setValue(durability, maxDurability);
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(getDurability());
	}
	
	public int getCraftingLevel() {
		return level;
	}
	
	public static BlockState applyDamage(BlockState blockState) {
		Block anvilBlock = blockState.getBlock();
		if (anvilBlock instanceof EndAnvilBlock endAnvilBlock) {
			IntegerProperty durability = endAnvilBlock.getDurability();
			int damage = blockState.getValue(durability) - 1;
			if (damage > 0) {
				return blockState.setValue(durability, damage);
			}
			int maxDurability = endAnvilBlock.getMaxDurability();
			blockState = blockState.setValue(durability, maxDurability);
		}
		return getDamagedState(blockState);
	}
	
	private static BlockState getDamagedState(BlockState fallingState) {
		Block anvilBlock = fallingState.getBlock();
		if (anvilBlock instanceof EndAnvilBlock) {
			IntegerProperty destructionProperty = EndAnvilBlock.DESTRUCTION;
			int destruction = fallingState.getValue(destructionProperty) + 1;
			if (destructionProperty.getPossibleValues().contains(destruction)) {
				try {
					return fallingState.setValue(destructionProperty, destruction);
				}
				catch (Exception ex) {
					return null;
				}
			}
			return null;
		}
		return AnvilBlock.damage(fallingState);
	}
}
