package ru.betterend.blocks;

import com.google.common.collect.Lists;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.math.Matrix4f;
import com.mojang.math.Quaternion;
import com.mojang.math.Transformation;
import com.mojang.math.Vector3f;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.MultiVariant;
import net.minecraft.client.renderer.block.model.Variant;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.Mth;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import ru.bclib.blocks.BaseBlockNotFull;
import ru.bclib.client.models.ModelsHelper;
import ru.bclib.client.models.ModelsHelper.MultiPartBuilder;
import ru.bclib.client.render.BCLRenderLayer;
import ru.bclib.interfaces.IRenderTyped;
import ru.bclib.util.JsonFactory;
import ru.bclib.util.MHelper;
import ru.betterend.BetterEnd;
import ru.betterend.interfaces.PottablePlant;
import ru.betterend.registry.EndBlocks;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

public class FlowerPotBlock extends BaseBlockNotFull implements IRenderTyped {
	private static final VoxelShape SHAPE = Block.box(4, 4, 4, 12, 12, 12);
	private static final IntegerProperty PLANT_ID = EndBlockProperties.PLANT_ID;
	private final ResourceLocation[] blocks;
	
	@Environment(EnvType.CLIENT)
	private UnbakedModel source;
	
	public FlowerPotBlock(Block source) {
		super(FabricBlockSettings.copyOf(source));
		List<ResourceLocation> blocks = Lists.newArrayList();
		EndBlocks.getModBlocks().forEach(block -> {
			if (block instanceof PottablePlant && block.getStateDefinition().getProperties().isEmpty()) {
				blocks.add(Registry.BLOCK.getKey(block));
			}
		});
		this.blocks = blocks.toArray(new ResourceLocation[] {});
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(PLANT_ID);
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public UnbakedModel getModelVariant(ResourceLocation stateId, BlockState blockState, Map<ResourceLocation, UnbakedModel> modelCache) {
		ModelResourceLocation key = new ModelResourceLocation(stateId.getNamespace(), stateId.getPath(), "plant_age=0");
		
		if (modelCache.containsKey(key)) {
			return modelCache.get(key);
		}
		
		MultiPartBuilder model = MultiPartBuilder.create(stateDefinition);
		model.part(new ResourceLocation("block/flower_pot")).add();
		Transformation offset = new Transformation(new Vector3f(0, 0.5F, 0), null, null, null);
		for (int i = 0; i < blocks.length; i++) {
			final int compareID = i + 1;
			ResourceLocation modelPath = blocks[i];
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
		return SHAPE;
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
