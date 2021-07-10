package ru.betterend.client.models;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.SimpleBakedModel;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import ru.bclib.util.BlocksHelper;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;

public class MergedModel implements UnbakedModel {
	private final List<UnbakedModel> models = Lists.newArrayList();
	private final UnbakedModel source;
	private final BlockState state;
	
	public MergedModel(BlockState state, UnbakedModel source) {
		this.source = source;
		this.state = state;
	}
	
	public void addModel(UnbakedModel model) {
		models.add(model);
	}
	
	@Override
	public Collection<ResourceLocation> getDependencies() {
		Set<ResourceLocation> dependencies = Sets.newHashSet();
		dependencies.addAll(source.getDependencies());
		models.forEach(model -> dependencies.addAll(model.getDependencies()));
		return dependencies;
	}
	
	@Override
	public Collection<Material> getMaterials(Function<ResourceLocation, UnbakedModel> function, Set<Pair<String, String>> set) {
		Set<Material> material = Sets.newHashSet();
		material.addAll(source.getMaterials(function, set));
		models.forEach(model -> material.addAll(model.getMaterials(function, set)));
		return material;
	}
	
	@Nullable
	@Override
	public BakedModel bake(ModelBakery modelBakery, Function<Material, TextureAtlasSprite> function, ModelState modelState, ResourceLocation resourceLocation) {
		Random random = new Random(resourceLocation.toString().hashCode());
		BakedModel baked = source.bake(modelBakery, function, modelState, resourceLocation);
		Map<Direction, List<BakedQuad>> map = makeMap();
		List<BakedQuad> quads = Lists.newArrayList();
		processModel(baked, map, quads, random);
		models.forEach(model -> {
			BakedModel baked2 = source.bake(modelBakery, function, modelState, resourceLocation);
			processModel(baked2, map, quads, random);
		});
		return new SimpleBakedModel(quads, map, baked.useAmbientOcclusion(), baked.usesBlockLight(), baked.isGui3d(), baked.getParticleIcon(), baked.getTransforms(), baked.getOverrides());
	}
	
	private Map<Direction, List<BakedQuad>> makeMap() {
		Map<Direction, List<BakedQuad>> map = Maps.newEnumMap(Direction.class);
		for (Direction dir : BlocksHelper.DIRECTIONS) {
			map.put(dir, Lists.newArrayList());
		}
		return map;
	}
	
	private void processModel(BakedModel model, Map<Direction, List<BakedQuad>> map, List<BakedQuad> quads, Random random) {
		for (Direction dir : BlocksHelper.DIRECTIONS) {
			map.get(dir).addAll(model.getQuads(state, dir, random));
		}
		quads.addAll(model.getQuads(state, null, random));
	}
}
