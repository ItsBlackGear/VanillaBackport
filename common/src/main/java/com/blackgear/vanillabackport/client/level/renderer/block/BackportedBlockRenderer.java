package com.blackgear.vanillabackport.client.level.renderer.block;

import com.blackgear.platform.client.v2.render.BlockRendererRegistry;
import com.blackgear.platform.core.util.config.ConfigBuilder;
import com.blackgear.platform.core.util.event.ResultHolder;
import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class BackportedBlockRenderer implements BlockRendererRegistry.Renderer {
  private final ConfigBuilder.ConfigValue<Boolean> configValue;
  private final Set<Block> blocks;

  public BackportedBlockRenderer(ConfigBuilder.ConfigValue<Boolean> configValue, Set<Block> blocks) {
    this.configValue = configValue;
    this.blocks = blocks;
  }

  @Override
  public ResultHolder<BakedModel> render(BlockState state, BlockModelShaper blockModelShaper) {
    ResourceLocation block = BuiltInRegistries.BLOCK.getKey(state.getBlock());
    ResourceLocation backportBlock = VanillaBackport.resource(block.getPath());
    ModelResourceLocation modelLocation = BlockModelShaper.stateToModelLocation(backportBlock, state);
    return ResultHolder.submit(blockModelShaper.getModelManager().getModel(modelLocation));
  }

  @Override
  public Map<Block, ResourceLocation> registerBlockStates() {
    HashMap<Block, ResourceLocation> map = new HashMap<>();
    for (Block block : blocks) {
      String path = BuiltInRegistries.BLOCK.getKey(block).getPath();
      map.put(block, VanillaBackport.resource(path));
    }
    return map;
  }

  @Override
  public boolean shouldUse() {
    return configValue.get();
  }
}
