package ru.betterend.blocks;

import com.google.common.collect.Lists;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.math.Transformation;
import com.mojang.math.Vector3f;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import ru.bclib.blocks.BaseBlockNotFull;
import ru.bclib.client.models.ModelsHelper;
import ru.bclib.client.models.ModelsHelper.MultiPartBuilder;
import ru.bclib.client.models.PatternsHelper;
import ru.bclib.client.render.BCLRenderLayer;
import ru.bclib.interfaces.IPostInit;
import ru.bclib.interfaces.IRenderTyped;
import ru.bclib.interfaces.ISpetialItem;
import ru.bclib.util.BlocksHelper;
import ru.bclib.util.JsonFactory;
import ru.betterend.client.models.Patterns;
import ru.betterend.interfaces.PottablePlant;
import ru.betterend.registry.EndBlocks;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class FlowerPotBlock extends BaseBlockNotFull implements IRenderTyped, IPostInit {
	private static final VoxelShape SHAPE = Block.box(4, 0, 4, 12, 8, 12);
	private static final IntegerProperty PLANT_ID = EndBlockProperties.PLANT_ID;
	private static VoxelShape[] plantBoxes;
	private static Block[] blocks;
	
	@Environment(EnvType.CLIENT)
	private UnbakedModel source;
	
	public FlowerPotBlock(Block source) {
		super(FabricBlockSettings.copyOf(source));
		this.registerDefaultState(this.defaultBlockState().setValue(PLANT_ID, 0));
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(PLANT_ID);
	}
	
	@Override
	public void postInit() {
		if (this.blocks != null) {
			return;
		}
		List<Block> blocks = Lists.newArrayList();
		EndBlocks.getModBlocks().forEach(block -> {
			if (block instanceof PottablePlant && block.getStateDefinition().getProperties().isEmpty()) {
				if (!(block instanceof ISpetialItem) || !((ISpetialItem) block).canPlaceOnWater()) {
					blocks.add(block);
				}
			}
		});
		FlowerPotBlock.blocks = blocks.toArray(new Block[] {});
		FlowerPotBlock.plantBoxes = new VoxelShape[FlowerPotBlock.blocks.length];
		for (int i = 0; i < FlowerPotBlock.blocks.length; i++) {
			Block block = FlowerPotBlock.blocks[i];
			VoxelShape shape = block.getShape(block.defaultBlockState(), null, BlockPos.ZERO, CollisionContext.empty());
			plantBoxes[i] = Shapes.or(SHAPE, shape.move(0.25, 0.5, 0.25));
		}
	}
	
	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (level.isClientSide) {
			return InteractionResult.CONSUME;
		}
		ItemStack itemStack = player.getItemInHand(hand);
		if (!(itemStack.getItem() instanceof BlockItem)) {
			return InteractionResult.PASS;
		}
		BlockItem item = (BlockItem) itemStack.getItem();
		for (int i = 0; i < blocks.length; i++) {
			if (item.getBlock() == blocks[i]) {
				BlocksHelper.setWithUpdate(level, pos, state.setValue(PLANT_ID, i + 1));
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
		ModelResourceLocation key = new ModelResourceLocation(stateId.getNamespace(), stateId.getPath(), "plant_age=0");
		
		if (modelCache.containsKey(key)) {
			return modelCache.get(key);
		}
		
		MultiPartBuilder model = MultiPartBuilder.create(stateDefinition);
		model.part(new ModelResourceLocation(stateId.getNamespace(), stateId.getPath(), "inventory")).add();
		Transformation offset = new Transformation(new Vector3f(0, 0.5F, 0), null, null, null);
		for (int i = 0; i < blocks.length; i++) {
			final int compareID = i + 1;
			ResourceLocation modelPath = Registry.BLOCK.getKey(blocks[i]);
			ResourceLocation objSource = new ResourceLocation(modelPath.getNamespace(), "block/potted_" + modelPath.getPath() + ".json");
			if (Minecraft.getInstance().getResourceManager().hasResource(objSource)) {
				objSource = new ResourceLocation(modelPath.getNamespace(), "block/potted_" + modelPath.getPath());
				model.part(objSource).setTransformation(offset).setCondition(state -> state.getValue(PLANT_ID) == compareID).add();
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
		}
		
		UnbakedModel result = model.build();
		modelCache.put(key, result);
		return result;
	}
	
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter view, BlockPos pos, CollisionContext ePos) {
		int id = state.getValue(PLANT_ID);
		return id > 0 && id <= blocks.length ? plantBoxes[id - 1] : SHAPE;
	}
	
	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter view, BlockPos pos, CollisionContext ePos) {
		return SHAPE;
	}
	
	@Override
	public BCLRenderLayer getRenderLayer() {
		return BCLRenderLayer.CUTOUT;
	}
}
