package com.blackgear.vanillabackport.client.level.renderer.item;

import com.blackgear.platform.client.v2.render.ItemRendererRegistry;
import com.blackgear.platform.core.util.config.ConfigBuilder;
import com.blackgear.platform.core.util.event.ResultHolder;
import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.client.renderer.ItemModelShaper;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.stream.Collectors;

public class BackportedItemRenderer implements ItemRendererRegistry.Renderer {
  private final ConfigBuilder.ConfigValue<Boolean> configValue;
  private final Set<Item> items;

  public BackportedItemRenderer(ConfigBuilder.ConfigValue<Boolean> configValue, Set<Item> items) {
    this.configValue = configValue;
    this.items = items;
  }

  private static ModelResourceLocation getModelResourceLocation(Item item) {
    ResourceLocation originalItem = BuiltInRegistries.ITEM.getKey(item);
    ResourceLocation backportItem = VanillaBackport.resource(originalItem.getPath());
    return new ModelResourceLocation(backportItem, "inventory");
  }

  private static @NotNull ResultHolder<BakedModel> getModel(ItemStack itemStack, ItemModelShaper itemModelShaper) {
    ModelResourceLocation modelResourceLocation = getModelResourceLocation(itemStack.getItem());
    return ResultHolder.submit(itemModelShaper.getModelManager().getModel(modelResourceLocation));
  }

  @Override
  public ResultHolder<BakedModel> renderFirstPerson(ItemStack itemStack, ItemDisplayContext itemDisplayContext, ItemModelShaper itemModelShaper) {
    return getModel(itemStack, itemModelShaper);
  }

  @Override
  public ResultHolder<BakedModel> renderThirdPerson(ItemStack itemStack, ItemModelShaper itemModelShaper) {
    return getModel(itemStack, itemModelShaper);
  }

  @Override
  public Set<ModelResourceLocation> registerModels() {
    return items
        .stream()
        .map(BackportedItemRenderer::getModelResourceLocation)
        .collect(Collectors.toSet());
  }

  @Override
  public boolean shouldUse() {
    return configValue.get();
  }
}
