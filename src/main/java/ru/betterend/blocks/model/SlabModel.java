package ru.betterend.blocks.model;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

import com.mojang.datafixers.util.Pair;

import net.fabricmc.fabric.api.renderer.v1.Renderer;
import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.mesh.Mesh;
import net.fabricmc.fabric.api.renderer.v1.mesh.MeshBuilder;
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.client.render.model.json.ModelOverrideList;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;
import ru.betterend.BetterEnd;

public class SlabModel implements UnbakedModel, BakedModel, FabricBakedModel {

	private static final Identifier DEFAULT_SLAB_MODEL = new Identifier("minecraft:block/slab");
	
	private final SpriteIdentifier[] spritesIDs;
    private final Sprite[] sprites;
    private ModelTransformation transformation;
    private Mesh mesh;

	public SlabModel(String... textures) {
		this.spritesIDs = new SpriteIdentifier[textures.length];
		this.sprites = new Sprite[textures.length];
		for (int i = 0; i < textures.length; i++) {
			this.spritesIDs[i] = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, BetterEnd.makeID(textures[i]));
		}
	}
    
    @Override
	public BakedModel bake(ModelLoader loader, Function<SpriteIdentifier, Sprite> textureGetter,
			ModelBakeSettings rotationContainer, Identifier modelId) {
		for(int i = 0; i < sprites.length; i++) {
			this.sprites[i] = textureGetter.apply(spritesIDs[i]);
        }
		
		JsonUnbakedModel jsonBlockModel = (JsonUnbakedModel) loader.getOrLoadModel(DEFAULT_SLAB_MODEL);
		this.transformation = jsonBlockModel.getTransformations();
		
		Renderer renderer = RendererAccess.INSTANCE.getRenderer();
		MeshBuilder builder = renderer.meshBuilder();
		QuadEmitter emitter = builder.getEmitter();
		
		for (Direction direction : Direction.values()) {
			switch (sprites.length) {
				case 1: {
					this.buildFace(emitter, direction, sprites[0]);
					break;
				}
				case 2: {
					switch (direction) {
						case DOWN:
						case UP: {
							this.buildFace(emitter, direction, sprites[0]);
							break;
						}
						default: {
							this.buildFace(emitter, direction, sprites[1]);
						}
					}
				}
				case 3: {
					switch (direction) {
						case DOWN:
						case UP: {
							this.buildFace(emitter, direction, sprites[0]);
							break;
						}
						case NORTH:
						case SOUTH: {
							this.buildFace(emitter, direction, sprites[1]);
							break;
						}
						default: {
							this.buildFace(emitter, direction, sprites[2]);
						}
					}
				}
				case 4: {
					switch (direction) {
						case DOWN:
						case UP: {
							this.buildFace(emitter, direction, sprites[0]);
							break;
						}
						case NORTH: {
							this.buildFace(emitter, direction, sprites[1]);
							break;
						}
						case SOUTH: {
							this.buildFace(emitter, direction, sprites[2]);
							break;
						}
						default: {
							this.buildFace(emitter, direction, sprites[3]);
						}
					}
				}
				case 5: {
					switch (direction) {
						case DOWN:
						case UP:
						case NORTH:
						case SOUTH: {
							this.buildFace(emitter, direction, sprites[direction.ordinal()]);
							break;
						}
						default: {
							this.buildFace(emitter, direction, sprites[4]);
						}
					}
				}
				default: {
					this.buildFace(emitter, direction, sprites[direction.ordinal()]);
				}
			}

			emitter.spriteColor(0, -1, -1, -1, -1);
			emitter.emit();
		}
		this.mesh = builder.build();
		
		return this;
	}
    
    private void buildFace(QuadEmitter emitter, Direction direction, Sprite sprite) {
    	switch(direction) {
    		case DOWN: {
    			emitter.square(direction, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f);
    			emitter.cullFace(direction);
    			break;
    		}
    		case UP: {
    			emitter.square(direction, 0.0f, 0.0f, 1.0f, 1.0f, 0.5f);
    			break;
    		}
    		default: {
    			emitter.square(direction, 0.0f, 0.0f, 1.0f, 0.5f, 0.0f);
    			emitter.cullFace(direction);
    		}
    	}
    	emitter.spriteBake(0, sprite, MutableQuadView.BAKE_LOCK_UV);
    }
    
    @Override
	public boolean isVanillaAdapter() {
		return false;
	}

	@Override
	public void emitBlockQuads(BlockRenderView blockView, BlockState state, BlockPos pos,
			Supplier<Random> randomSupplier, RenderContext context) {
		context.meshConsumer().accept(mesh);
	}

	@Override
	public void emitItemQuads(ItemStack stack, Supplier<Random> randomSupplier, RenderContext context) {
		context.meshConsumer().accept(mesh);
	}

	@Override
	public List<BakedQuad> getQuads(BlockState state, Direction face, Random random) {
		return null;
	}

	@Override
	public boolean useAmbientOcclusion() {
		return false;
	}

	@Override
	public boolean hasDepth() {
		return false;
	}

	@Override
	public boolean isSideLit() {
		return true;
	}

	@Override
	public boolean isBuiltin() {
		return false;
	}

	@Override
	public Sprite getSprite() {
		return this.sprites[0];
	}

	@Override
	public ModelTransformation getTransformation() {
		return this.transformation;
	}

	@Override
	public ModelOverrideList getOverrides() {
		return ModelOverrideList.EMPTY;
	}

	@Override
	public Collection<Identifier> getModelDependencies() {
		return Arrays.asList(DEFAULT_SLAB_MODEL);
	}

	@Override
	public Collection<SpriteIdentifier> getTextureDependencies(Function<Identifier, UnbakedModel> unbakedModelGetter,
			Set<Pair<String, String>> unresolvedTextureReferences) {
		return Arrays.asList(spritesIDs);
	}
}
