package ru.betterend.blocks.basis;

import net.minecraft.world.level.block.AnvilBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.MaterialColor;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.bclib.blocks.BaseAnvilBlock;

public class EndAnvilBlock extends BaseAnvilBlock {

	protected final int level;
	protected IntegerProperty durability;
	protected int maxDamage;
	
	public EndAnvilBlock(MaterialColor color, int level) {
		super(color);
		this.level = level;
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
			this.maxDamage = 5;
			this.durability = IntegerProperty.create("durability", 0, maxDamage);
		}
		return durability;
	}

	public int getMaxDamage() {
		return maxDamage;
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
		if (anvilBlock instanceof EndAnvilBlock) {
			EndAnvilBlock endAnvilBlock = (EndAnvilBlock) anvilBlock;
			IntegerProperty durability = endAnvilBlock.getDurability();
			int damage = blockState.getValue(durability) + 1;
			if (damage <= endAnvilBlock.getMaxDamage()) {
				return blockState.setValue(durability, damage);
			}
			blockState = blockState.setValue(durability, 0);
		}
		return getDamagedState(blockState);
	}

	private static BlockState getDamagedState(BlockState fallingState) {
		Block anvilBlock = fallingState.getBlock();
		if (anvilBlock instanceof EndAnvilBlock) {
			IntegerProperty destructionProperty = ((EndAnvilBlock) anvilBlock).getDestructionProperty();
			int destruction = fallingState.getValue(destructionProperty) + 1;
			if (destructionProperty.getPossibleValues().contains(destruction)) {
				try {
					return fallingState.setValue(destructionProperty, destruction);
				} catch (Exception ex) {
					return null;
				}
			}
			return null;
		}
		return AnvilBlock.damage(fallingState);
	}
}
