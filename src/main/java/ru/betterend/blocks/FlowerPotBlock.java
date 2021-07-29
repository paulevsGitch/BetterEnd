package ru.betterend.blocks;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
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
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.storage.loot.LootContext.Builder;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import ru.bclib.blocks.BaseBlockNotFull;
import ru.bclib.client.models.BasePatterns;
import ru.bclib.client.models.ModelsHelper;
import ru.bclib.client.models.ModelsHelper.MultiPartBuilder;
import ru.bclib.client.models.PatternsHelper;
import ru.bclib.client.render.BCLRenderLayer;
import ru.bclib.interfaces.PostInitable;
import ru.bclib.interfaces.RenderLayerProvider;
import ru.bclib.util.BlocksHelper;
import ru.bclib.util.JsonFactory;
import ru.betterend.BetterEnd;
import ru.betterend.blocks.basis.PottableLeavesBlock;
import ru.betterend.client.models.Patterns;
import ru.betterend.config.Configs;
import ru.betterend.interfaces.PottablePlant;
import ru.betterend.interfaces.PottableTerrain;
import ru.betterend.registry.EndBlocks;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class FlowerPotBlock extends BaseBlockNotFull implements RenderLayerProvider, PostInitable {
	private static final IntegerProperty PLANT_ID = EndBlockProperties.PLANT_ID;
	private static final IntegerProperty SOIL_ID = EndBlockProperties.SOIL_ID;
	private static final IntegerProperty POT_LIGHT = EndBlockProperties.POT_LIGHT;
	private static final VoxelShape SHAPE_EMPTY;
	private static final VoxelShape SHAPE_FULL;
	private static Block[] plants;
	private static Block[] soils;
	
	public FlowerPotBlock(Block source) {
		super(FabricBlockSettings.copyOf(source).luminance(state -> state.getValue(POT_LIGHT) * 5));
		this.registerDefaultState(this.defaultBlockState()
									  .setValue(PLANT_ID, 0)
									  .setValue(SOIL_ID, 0)
									  .setValue(POT_LIGHT, 0));
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(PLANT_ID, SOIL_ID, POT_LIGHT);
	}
	
	@Override
	public List<ItemStack> getDrops(BlockState state, Builder builder) {
		List<ItemStack> drop = Lists.newArrayList(new ItemStack(this));
		int id = state.getValue(SOIL_ID) - 1;
		if (id >= 0 && id < soils.length && soils[id] != null) {
			drop.add(new ItemStack(soils[id]));
		}
		id = state.getValue(PLANT_ID) - 1;
		if (id >= 0 && id < plants.length && plants[id] != null) {
			drop.add(new ItemStack(plants[id]));
		}
		return drop;
	}
	
	@Override
	public BlockState updateShape(BlockState state, Direction facing, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
		int plantID = state.getValue(PLANT_ID);
		if (plantID < 1 || plantID > plants.length || plants[plantID - 1] == null) {
			return state.getValue(POT_LIGHT) > 0 ? state.setValue(POT_LIGHT, 0) : state;
		}
		int light = plants[plantID - 1].defaultBlockState().getLightEmission() / 5;
		if (state.getValue(POT_LIGHT) != light) {
			state = state.setValue(POT_LIGHT, light);
		}
		return state;
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
		
		JsonObject obj = JsonFactory.getJsonObject(new File(
			FabricLoader.getInstance().getConfigDir().toFile(),
			BetterEnd.MOD_ID + "/blocks.json"
		));
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
			if (block instanceof PottablePlant && ((PottablePlant) block).addToPot()) {//&& canBeAdded(block)) {
				processBlock(plants, block, "flower_pots.plants", reservedPlantsIDs);
			}
			else if (block instanceof PottableTerrain) {
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
		if (soilID == 0 || soilID > soils.length || soils[soilID - 1] == null) {
			if (!(itemStack.getItem() instanceof BlockItem)) {
				return InteractionResult.PASS;
			}
			Block block = ((BlockItem) itemStack.getItem()).getBlock();
			for (int i = 0; i < soils.length; i++) {
				if (block == soils[i]) {
					BlocksHelper.setWithUpdate(level, pos, state.setValue(SOIL_ID, i + 1));
					if (!player.isCreative()) {
						itemStack.shrink(1);
					}
					level.playSound(
						player,
						pos.getX() + 0.5,
						pos.getY() + 0.5,
						pos.getZ() + 0.5,
						SoundEvents.SOUL_SOIL_PLACE,
						SoundSource.BLOCKS,
						1,
						1
					);
					return InteractionResult.SUCCESS;
				}
			}
			return InteractionResult.PASS;
		}
		
		int plantID = state.getValue(PLANT_ID);
		if (itemStack.isEmpty()) {
			if (plantID > 0 && plantID <= plants.length && plants[plantID - 1] != null) {
				BlocksHelper.setWithUpdate(level, pos, state.setValue(PLANT_ID, 0).setValue(POT_LIGHT, 0));
				player.addItem(new ItemStack(plants[plantID - 1]));
				return InteractionResult.SUCCESS;
			}
			if (soilID > 0 && soilID <= soils.length && soils[soilID - 1] != null) {
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
				int light = plants[i].defaultBlockState().getLightEmission() / 5;
				BlocksHelper.setWithUpdate(level, pos, state.setValue(PLANT_ID, i + 1).setValue(POT_LIGHT, light));
				level.playSound(
					player,
					pos.getX() + 0.5,
					pos.getY() + 0.5,
					pos.getZ() + 0.5,
					SoundEvents.HOE_TILL,
					SoundSource.BLOCKS,
					1,
					1
				);
				if (!player.isCreative()) {
					itemStack.shrink(1);
				}
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
			ResourceLocation objSource = new ResourceLocation(
				modelPath.getNamespace(),
				"models/block/" + modelPath.getPath() + "_potted.json"
			);
			
			if (Minecraft.getInstance().getResourceManager().hasResource(objSource)) {
				objSource = new ResourceLocation(modelPath.getNamespace(), "block/" + modelPath.getPath() + "_potted");
				model.part(objSource)
					 .setTransformation(offset)
					 .setCondition(state -> state.getValue(PLANT_ID) == compareID)
					 .add();
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
				model.part(modelPath)
					 .setTransformation(offset)
					 .setCondition(state -> state.getValue(PLANT_ID) == compareID)
					 .add();
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
				model.part(modelPath)
					 .setTransformation(offset)
					 .setCondition(state -> state.getValue(PLANT_ID) == compareID)
					 .add();
				continue;
			}
			
			objSource = new ResourceLocation(modelPath.getNamespace(), "blockstates/" + modelPath.getPath() + ".json");
			JsonObject obj = JsonFactory.getJsonObject(objSource);
			if (obj != null) {
				JsonElement variants = obj.get("variants");
				JsonElement list = null;
				String path = null;
				
				if (variants == null) {
					continue;
				}
				
				if (variants.isJsonArray()) {
					list = variants.getAsJsonArray().get(0);
				}
				else if (variants.isJsonObject()) {
					list = variants.getAsJsonObject().get(((PottablePlant) plants[i]).getPottedState());
				}
				
				if (list == null) {
					BetterEnd.LOGGER.warning("Incorrect json for pot plant " + objSource + ", no matching variants");
					continue;
				}
				
				if (list.isJsonArray()) {
					path = list.getAsJsonArray().get(0).getAsJsonObject().get("model").getAsString();
				}
				else {
					path = list.getAsJsonObject().get("model").getAsString();
				}
				
				if (path == null) {
					BetterEnd.LOGGER.warning("Incorrect json for pot plant " + objSource + ", no matching variants");
					continue;
				}
				
				model.part(new ResourceLocation(path))
					 .setTransformation(offset)
					 .setCondition(state -> state.getValue(PLANT_ID) == compareID)
					 .add();
			}
			else {
				for (ResourceLocation location : modelCache.keySet()) {
					if (location.getPath().equals(modelPath.getPath())) {
						model.part(location)
							 .setTransformation(offset)
							 .setCondition(state -> state.getValue(PLANT_ID) == compareID)
							 .add();
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