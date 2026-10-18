package com.blackgear.vanillabackport.client.level.renderer.item;

import com.blackgear.platform.core.util.event.ResultHolder;
import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.client.renderer.ItemModelShaper;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class BannerPatternRenderer extends BackportedItemRenderer {
  private static final Set<Item> BANNER_PATTERNS = Set.of(
      Items.CREEPER_BANNER_PATTERN, Items.FLOW_BANNER_PATTERN, Items.FLOWER_BANNER_PATTERN, Items.GLOBE_BANNER_PATTERN,
      Items.GUSTER_BANNER_PATTERN, Items.MOJANG_BANNER_PATTERN, Items.PIGLIN_BANNER_PATTERN, Items.SKULL_BANNER_PATTERN
  );
  private static final ModelResourceLocation BLANK_MODEL_LOCATION = ModelResourceLocation.inventory(VanillaBackport.resource("blank_banner_pattern"));
  public static final BannerPatternRenderer INSTANCE = new BannerPatternRenderer();

  public BannerPatternRenderer() {
    super(VanillaBackport.CLIENT_CONFIG.hasModernBannerPatternTextures, BANNER_PATTERNS);
  }

  @Override
  public ResultHolder<BakedModel> renderFirstPerson(ItemStack itemStack, ItemDisplayContext itemDisplayContext, ItemModelShaper itemModelShaper) {
    return BANNER_PATTERNS.contains(itemStack.getItem())
        ? super.renderFirstPerson(itemStack, itemDisplayContext, itemModelShaper)
        : getResult(itemModelShaper);
  }

  @Override
  public ResultHolder<BakedModel> renderThirdPerson(ItemStack itemStack, ItemModelShaper itemModelShaper) {
    return BANNER_PATTERNS.contains(itemStack.getItem())
        ? super.renderThirdPerson(itemStack, itemModelShaper)
        : getResult(itemModelShaper);
  }

  private static @NotNull ResultHolder<BakedModel> getResult(ItemModelShaper itemModelShaper) {
    return ResultHolder.submit(itemModelShaper.getModelManager().getModel(BLANK_MODEL_LOCATION));
  }

  @Override
  public Set<ModelResourceLocation> registerModels() {
    Set<ModelResourceLocation> locations = super.registerModels();
    locations.add(BLANK_MODEL_LOCATION);
    return locations;
  }
}
