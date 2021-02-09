package ru.betterend.integration;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import net.fabricmc.fabric.api.tag.TagRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.ItemTags;
import net.minecraft.tag.Tag;
import net.minecraft.tag.Tag.Identified;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import ru.betterend.BetterEnd;
import ru.betterend.world.features.EndFeature;

public abstract class ModIntegration {
	private final String modID;
	
	public abstract void register();
	
	public abstract void addBiomes();
	
	public ModIntegration(String modID) {
		this.modID = modID;
	}
	
	public Identifier getID(String name) {
		return new Identifier(modID, name);
	}
	
	public Block getBlock(String name) {
		return Registry.BLOCK.get(getID(name));
	}

	public BlockState getDefaultState(String name) {
		return getBlock(name).getDefaultState();
	}
	
	public RegistryKey<Biome> getKey(String name) {
		return RegistryKey.of(Registry.BIOME_KEY, getID(name));
	}
	
	public boolean modIsInstalled() {
		return FabricLoader.getInstance().isModLoaded(modID);
	}
	
	public EndFeature getFeature(String featureID, String configuredFeatureID, GenerationStep.Feature featureStep) {
		Feature<?> feature = Registry.FEATURE.get(getID(featureID));
		ConfiguredFeature<?, ?> featureConfigured = BuiltinRegistries.CONFIGURED_FEATURE.get(getID(configuredFeatureID));
		return new EndFeature(feature, featureConfigured, featureStep);
	}
	
	public EndFeature getFeature(String name, GenerationStep.Feature featureStep) {
		return getFeature(name, name, featureStep);
	}
	
	public ConfiguredFeature<?, ?> getConfiguredFeature(String name) {
		return BuiltinRegistries.CONFIGURED_FEATURE.get(getID(name));
	}
	
	public Biome getBiome(String name) {
		return BuiltinRegistries.BIOME.get(getID(name));
	}
	
	public Class<?> getClass(String path) {
		Class<?> cl = null;
		try {
			cl = Class.forName(path);
		}
		catch (ClassNotFoundException e) {
			BetterEnd.LOGGER.error(e.getMessage());
			if (BetterEnd.isDevEnvironment()) {
				e.printStackTrace();
			}
		}
		return cl;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Object> T getStaticFieldValue(Class<?> cl, String name) {
		if (cl != null) {
			try {
				Field field = cl.getDeclaredField(name);
				if (field != null) {
					return (T) field.get(null);
				}
			}
			catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public Object getFieldValue(Class<?> cl, String name, Object classInstance) {
		if (cl != null) {
			try {
				Field field = cl.getDeclaredField(name);
				if (field != null) {
					return field.get(classInstance);
				}
			}
			catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public Method getMethod(Class<?> cl, String functionName, Class<?>... args) {
		if (cl != null) {
			try {
				return cl.getMethod(functionName, args);
			}
			catch (NoSuchMethodException | SecurityException e) {
				BetterEnd.LOGGER.error(e.getMessage());
				if (BetterEnd.isDevEnvironment()) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
	public Object executeMethod(Object instance, Method method, Object... args) {
		if (method != null) {
			try {
				return method.invoke(instance, args);
			}
			catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				BetterEnd.LOGGER.error(e.getMessage());
				if (BetterEnd.isDevEnvironment()) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
	public Object getAndExecuteStatic(Class<?> cl, String functionName, Object... args) {
		if (cl != null) {
			Class<?>[] classes = new Class<?>[args.length];
			for (int i = 0; i < args.length; i++) {
				classes[i] = args[i].getClass();
			}
			Method method = getMethod(cl, functionName, classes);
			return executeMethod(null, method, args);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Object> T getAndExecuteRuntime(Class<?> cl, Object instance, String functionName, Object... args) {
		if (instance != null) {
			Class<?>[] classes = new Class<?>[args.length];
			for (int i = 0; i < args.length; i++) {
				classes[i] = args[i].getClass();
			}
			Method method = getMethod(cl, functionName, classes);
			return (T) executeMethod(instance, method, args);
		}
		return null;
	}
	
	public Object newInstance(Class<?> cl, Object... args) {
		if (cl != null) {
			for (Constructor<?> constructor: cl.getConstructors()) {
				if (constructor.getParameterCount() == args.length) {
					try {
						return constructor.newInstance(args);
					}
					catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
						BetterEnd.LOGGER.error(e.getMessage());
						if (BetterEnd.isDevEnvironment()) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		return null;
	}
	
	public Tag.Identified<Item> getItemTag(String name) {
		Identifier id = getID(name);
		Tag<Item> tag = ItemTags.getTagGroup().getTag(id);
		return tag == null ? (Identified<Item>) TagRegistry.item(id) : (Identified<Item>) tag;
	}
	
	public Tag.Identified<Block> getBlockTag(String name) {
		Identifier id = getID(name);
		Tag<Block> tag = BlockTags.getTagGroup().getTag(id);
		return tag == null ? (Identified<Block>) TagRegistry.block(id) : (Identified<Block>) tag;
	}
}
