package ru.betterend.blocks;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.math.Transformation;
import com.mojang.math.Vector3f;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import ru.bclib.blocks.BaseBlockNotFull;
import ru.bclib.client.models.BasePatterns;
import ru.bclib.client.models.BlockModelProvider;
import ru.bclib.client.models.ModelsHelper;
import ru.bclib.client.models.ModelsHelper.MultiPartBuilder;
import ru.bclib.client.models.PatternsHelper;
import ru.bclib.client.render.BCLRenderLayer;
import ru.bclib.interfaces.IPostInit;
import ru.bclib.interfaces.IRenderTyped;
import ru.bclib.interfaces.ISpetialItem;
import ru.bclib.util.BlocksHelper;
import ru.bclib.util.JsonFactory;
import ru.betterend.BetterEnd;
import ru.betterend.blocks.basis.EndTerrainBlock;
import ru.betterend.blocks.basis.PottableLeavesBlock;
import ru.betterend.client.models.Patterns;
import ru.betterend.config.Configs;
import ru.betterend.interfaces.PottablePlant;
import ru.betterend.registry.EndBlocks;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.SplittableRandom;
import java.util.stream.Stream;

public class FlowerPotBlock extends BaseBlockNotFull implements IRenderTyped, IPostInit {
	private static final IntegerProperty PLANT_ID = EndBlockProperties.PLANT_ID;
	private static final IntegerProperty SOIL_ID = EndBlockProperties.SOIL_ID;
	private static final VoxelShape SHAPE_EMPTY;
	private static final VoxelShape SHAPE_FULL;
	private static Block[] plants;
	private static Block[] soils;
	
	public FlowerPotBlock(Block source) {
		super(FabricBlockSettings.copyOf(source));
		this.registerDefaultState(this.defaultBlockState().setValue(PLANT_ID, 0));
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(PLANT_ID, SOIL_ID);
	}
	
	@Override
	public void postInit() {
		if (FlowerPotBlock.plants != null) {
			return;
		}
		
		Block[] plants = new Block[128];
		Block[] soils = new Block[16];
		
		Map<String, Integer> reservedPlantsIDs = Maps.newHashMap();
		Map<String, Integer> reservedSoilIDs = Maps.newHashMap();
		
		JsonObject obj = JsonFactory.getJsonObject(new File(FabricLoader.getInstance().getConfigDir().toFile(), BetterEnd.MOD_ID + "/blocks.json"));
		if (obj.get("flower_pots") != null) {
			JsonElement plantsObj = obj.get("flower_pots").getAsJsonObject().get("plants");
			JsonElement soilsObj = obj.get("flower_pots").getAsJsonObject().get("soils");
			if (plantsObj != null) {
				plantsObj.getAsJsonObject().entrySet().forEach(entry -> {
					String name = entry.getKey().substring(0, entry.getKey().indexOf(' '));
					reservedPlantsIDs.put(name, entry.getValue().getAsInt());
				});
			}
			if (soilsObj != null) {
				soilsObj.getAsJsonObject().entrySet().forEach(entry -> {
					String name = entry.getKey().substring(0, entry.getKey().indexOf(' '));
					reservedSoilIDs.put(name, entry.getValue().getAsInt());
				});
			}
		}
		
		EndBlocks.getModBlocks().forEach(block -> {
			if (block instanceof PottablePlant && canBeAdded(block)) {
				processBlock(plants, block, "flower_pots.plants", reservedPlantsIDs);
			}
			else if (block instanceof EndTerrainBlock) {
				processBlock(soils, block, "flower_pots.soils", reservedSoilIDs);
			}
		});
		Configs.BLOCK_CONFIG.saveChanges();
		
		FlowerPotBlock.plants = new Block[maxNotNull(plants) + 1];
		System.arraycopy(plants, 0, FlowerPotBlock.plants, 0, FlowerPotBlock.plants.length);
		
		FlowerPotBlock.soils = new Block[maxNotNull(soils) + 1];
		System.arraycopy(soils, 0, FlowerPotBlock.soils, 0, FlowerPotBlock.soils.length);
		
		if (PLANT_ID.getValue(Integer.toString(FlowerPotBlock.plants.length)).isEmpty()) {
			throw new RuntimeException("There are too much plant ID values!");
		}
		if (SOIL_ID.getValue(Integer.toString(FlowerPotBlock.soils.length)).isEmpty()) {
			throw new RuntimeException("There are too much soil ID values!");
		}
	}
	
	private int maxNotNull(Block[] array) {
		int max = 0;
		for (int i = 0; i < array.length; i++) {
			if (array[i] != null) {
				max = i;
			}
		}
		return max;
	}
	
	private boolean canBeAdded(Block block) {
		if (block instanceof SaplingBlock) {
			return true;
		}
		else if (block instanceof PottableLeavesBlock) {
			return true;
		}
		else if (block instanceof ISpetialItem) {
			return !((ISpetialItem) block).canPlaceOnWater();
		}
		else if (block.getStateDefinition().getProperties().isEmpty()) {
			return true;
		}
		return false;
	}
	
	private void processBlock(Block[] target, Block block, String path, Map<String, Integer> idMap) {
		ResourceLocation location = Registry.BLOCK.getKey(block);
		if (idMap.containsKey(location.getPath())) {
			target[idMap.get(location.getPath())] = block;
		}
		else {
			for (int i = 0; i < target.length; i++) {
				if (!idMap.values().contains(i)) {
					target[i] = block;
					idMap.put(location.getPath(), i);
					Configs.BLOCK_CONFIG.getInt(path, location.getPath(), i);
					break;
				}
			}
		}
	}
	
	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (level.isClientSide) {
			return InteractionResult.CONSUME;
		}
		ItemStack itemStack = player.getItemInHand(hand);
		int soilID = state.getValue(SOIL_ID);
		if (soilID == 0 || soils[soilID - 1] == null) {
			if (!(itemStack.getItem() instanceof BlockItem)) {
				return InteractionResult.PASS;
			}
			Block block = ((BlockItem) itemStack.getItem()).getBlock();
			for (int i = 0; i < soils.length; i++) {
				if (block == soils[i]) {
					BlocksHelper.setWithUpdate(level, pos, state.setValue(SOIL_ID, i + 1));
					return InteractionResult.SUCCESS;
				}
			}
			return InteractionResult.PASS;
		}
		
		int plantID = state.getValue(PLANT_ID);
		if (itemStack.isEmpty()) {
			if (plantID > 0 && plantID <= plants.length) {
				BlocksHelper.setWithUpdate(level, pos, state.setValue(PLANT_ID, 0));
				player.addItem(new ItemStack(plants[plantID - 1]));
				return InteractionResult.SUCCESS;
			}
			if (soilID > 0 && soilID <= soils.length) {
				BlocksHelper.setWithUpdate(level, pos, state.setValue(SOIL_ID, 0));
				player.addItem(new ItemStack(soils[soilID - 1]));
			}
			return InteractionResult.PASS;
		}
		if (!(itemStack.getItem() instanceof BlockItem)) {
			return InteractionResult.PASS;
		}
		BlockItem item = (BlockItem) itemStack.getItem();
		for (int i = 0; i < plants.length; i++) {
			if (item.getBlock() == plants[i]) {
				if (!((PottablePlant) plants[i]).canPlantOn(soils[soilID - 1])) {
					return InteractionResult.PASS;
				}
				BlocksHelper.setWithUpdate(level, pos, state.setValue(PLANT_ID, i + 1));
				level.playLocalSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, SoundEvents.HOE_TILL, SoundSource.BLOCKS, 1, 1, false);
				return InteractionResult.SUCCESS;
			}
		}
		return InteractionResult.PASS;
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public BlockModel getItemModel(ResourceLocation blockId) {
		Optional<String> pattern = PatternsHelper.createJson(Patterns.BLOCK_FLOWER_POT, blockId);
		return ModelsHelper.fromPattern(pattern);
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public UnbakedModel getModelVariant(ResourceLocation stateId, BlockState blockState, Map<ResourceLocation, UnbakedModel> modelCache) {
		MultiPartBuilder model = MultiPartBuilder.create(stateDefinition);
		model.part(new ModelResourceLocation(stateId.getNamespace(), stateId.getPath(), "inventory")).add();
		Transformation offset = new Transformation(new Vector3f(0, 7.5F / 16F, 0), null, null, null);
		
		for (int i = 0; i < plants.length; i++) {
			if (plants[i] == null) {
				continue;
			}
			
			final int compareID = i + 1;
			ResourceLocation modelPath = Registry.BLOCK.getKey(plants[i]);
			ResourceLocation objSource = new ResourceLocation(modelPath.getNamespace(), "models/block/" + modelPath.getPath() + "_potted.json");
			
			if (Minecraft.getInstance().getResourceManager().hasResource(objSource)) {
				objSource = new ResourceLocation(modelPath.getNamespace(), "block/" + modelPath.getPath() + "_potted");
				model.part(objSource).setTransformation(offset).setCondition(state -> state.getValue(PLANT_ID) == compareID).add();
				continue;
			}
			
			else if (plants[i] instanceof SaplingBlock) {
				ResourceLocation loc = Registry.BLOCK.getKey(plants[i]);
				modelPath = new ResourceLocation(loc.getNamespace(), "block/" + loc.getPath() + "_potted");
				Map<String, String> textures = Maps.newHashMap();
				textures.put("%modid%", loc.getNamespace());
				textures.put("%texture%", loc.getPath());
				Optional<String> pattern = Patterns.createJson(BasePatterns.BLOCK_CROSS, textures);
				UnbakedModel unbakedModel = ModelsHelper.fromPattern(pattern);
				modelCache.put(modelPath, unbakedModel);
				model.part(modelPath).setTransformation(offset).setCondition(state -> state.getValue(PLANT_ID) == compareID).add();
				continue;
			}
			else if (plants[i] instanceof PottableLeavesBlock) {
				ResourceLocation loc = Registry.BLOCK.getKey(plants[i]);
				modelPath = new ResourceLocation(loc.getNamespace(), "block/" + loc.getPath() + "_potted");
				Map<String, String> textures = Maps.newHashMap();
				textures.put("%leaves%", loc.getPath().contains("lucernia") ? loc.getPath() + "_1" : loc.getPath());
				textures.put("%stem%", loc.getPath().replace("_leaves", "_log_side"));
				Optional<String> pattern = Patterns.createJson(Patterns.BLOCK_POTTED_LEAVES, textures);
				UnbakedModel unbakedModel = ModelsHelper.fromPattern(pattern);
				modelCache.put(modelPath, unbakedModel);
				model.part(modelPath).setTransformation(offset).setCondition(state -> state.getValue(PLANT_ID) == compareID).add();
				continue;
			}
			
			objSource = new ResourceLocation(modelPath.getNamespace(), "blockstates/" + modelPath.getPath() + ".json");
			JsonObject obj = JsonFactory.getJsonObject(objSource);
			if (obj != null) {
				JsonElement variants = obj.get("variants").getAsJsonObject().get("");
				String path = null;
				if (variants.isJsonArray()) {
					path = variants.getAsJsonArray().get(0).getAsJsonObject().get("model").getAsString();
				}
				else {
					path = variants.getAsJsonObject().get("model").getAsString();
				}
				model.part(new ResourceLocation(path)).setTransformation(offset).setCondition(state -> state.getValue(PLANT_ID) == compareID).add();
			}
			else {
				for (ResourceLocation location: modelCache.keySet()) {
					if (location.getPath().equals(modelPath.getPath())) {
						model.part(location).setTransformation(offset).setCondition(state -> state.getValue(PLANT_ID) == compareID).add();
						break;
					}
				}
			}
		}
		
		for (int i = 0; i < soils.length; i++) {
			if (soils[i] == null) {
				continue;
			}
			
			ResourceLocation soilLoc = BetterEnd.makeID("flower_pot_soil_" + i);
			if (!modelCache.containsKey(soilLoc)) {
				String texture = Registry.BLOCK.getKey(soils[i]).getPath() + "_top";
				if (texture.contains("rutiscus")) {
					texture += "_1";
				}
				Optional<String> pattern = Patterns.createJson(Patterns.BLOCK_FLOWER_POT_SOIL, texture);
				UnbakedModel soil = ModelsHelper.fromPattern(pattern);
				modelCache.put(soilLoc, soil);
			}
			final int compareID = i + 1;
			model.part(soilLoc).setCondition(state -> state.getValue(SOIL_ID) == compareID).add();
		}
		
		UnbakedModel result = model.build();
		modelCache.put(stateId, result);
		return result;
	}
	
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter view, BlockPos pos, CollisionContext ePos) {
		int id = state.getValue(PLANT_ID);
		return id > 0 && id <= plants.length ? SHAPE_FULL : SHAPE_EMPTY;
	}
	
	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter view, BlockPos pos, CollisionContext ePos) {
		return SHAPE_EMPTY;
	}
	
	@Override
	public BCLRenderLayer getRenderLayer() {
		return BCLRenderLayer.CUTOUT;
	}
	
	static {
		SHAPE_EMPTY = Shapes.or(Block.box(4, 1, 4, 12, 8, 12), Block.box(5, 0, 5, 11, 1, 11));
		SHAPE_FULL = Shapes.or(SHAPE_EMPTY, Block.box(3, 8, 3, 13, 16, 13));
	}
}