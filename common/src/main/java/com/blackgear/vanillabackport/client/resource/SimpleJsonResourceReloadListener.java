package com.blackgear.vanillabackport.client.resource;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.mojang.logging.LogUtils;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.slf4j.Logger;

public abstract class SimpleJsonResourceReloadListener<T> extends SimplePreparableReloadListener<Map<ResourceLocation, T>> {
	private static final Logger LOGGER = LogUtils.getLogger();
	private final DynamicOps<JsonElement> ops;
	private final Codec<T> codec;
	private final FileToIdConverter lister;
	
	protected SimpleJsonResourceReloadListener(Codec<T> codec, FileToIdConverter lister) {
		this(JsonOps.INSTANCE, codec, lister);
	}
	
	protected SimpleJsonResourceReloadListener(DynamicOps<JsonElement> ops, Codec<T> codec, FileToIdConverter lister) {
		this.ops = ops;
		this.codec = codec;
		this.lister = lister;
	}
	
	@Override
	protected Map<ResourceLocation, T> prepare(ResourceManager manager, ProfilerFiller profiler) {
		Map<ResourceLocation, T> result = new HashMap<>();

		for (Entry<ResourceLocation, Resource> entry : this.lister.listMatchingResources(manager).entrySet()) {
			ResourceLocation location = entry.getKey();
			ResourceLocation id = this.lister.fileToId(location);

			try {
				Reader reader = entry.getValue().openAsReader();

				try {
					this.codec.parse(this.ops, StrictJsonParser.parse(reader)).ifSuccess(parsed -> {
						if (result.putIfAbsent(id, parsed) != null) {
							throw new IllegalStateException("Duplicate data file ignored with ID " + id);
						}
					}).ifError(error -> LOGGER.error("Couldn't parse data file '{}' from '{}': {}", id, location, error));
				} catch (Throwable throwable) {
					if (reader != null) {
						try {
							reader.close();
						} catch (Throwable throwablex) {
							throwable.addSuppressed(throwablex);
						}
					}

					throw throwable;
				}

				if (reader != null) {
					reader.close();
				}
			} catch (IllegalArgumentException | IOException | JsonParseException exception) {
				LOGGER.error("Couldn't parse data file {} from {}", id, location, exception);
			}
		}
		
		return result;
	}
}
