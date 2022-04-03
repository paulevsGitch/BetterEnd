package ru.betterend.blocks;

import com.google.common.collect.Lists;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.WaterLilyBlockItem;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import ru.bclib.interfaces.CustomItemProvider;
import ru.bclib.interfaces.tools.AddMineableShears;
import ru.betterend.blocks.basis.EndPlantBlock;

import java.util.List;

public class FlamaeaBlock extends EndPlantBlock implements CustomItemProvider, AddMineableShears {
	private static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 1, 16);
	
	public FlamaeaBlock() {
		super(FabricBlockSettings.of(Material.PLANT)
								 .breakByHand(true)
								 .sound(SoundType.WET_GRASS));
	}
	
	@Override
	protected boolean isTerrain(BlockState state) {
		return state.is(Blocks.WATER);
	}
	
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter view, BlockPos pos, CollisionContext ePos) {
		return SHAPE;
	}
	
	@Override
	public BlockBehaviour.OffsetType getOffsetType() {
		return BlockBehaviour.OffsetType.NONE;
	}
	
	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		return Lists.newArrayList(new ItemStack(this));
	}
	
	@Override
	public boolean canBePotted() {
		return false;
	}
	
	@Override
	public BlockItem getCustomItem(ResourceLocation resourceLocation, FabricItemSettings fabricItemSettings) {
		return new WaterLilyBlockItem(this, fabricItemSettings);
	}
}
