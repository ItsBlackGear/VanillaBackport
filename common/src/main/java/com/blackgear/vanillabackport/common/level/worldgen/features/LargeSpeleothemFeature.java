package com.blackgear.vanillabackport.common.level.worldgen.features;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Column;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class LargeSpeleothemFeature extends Feature<LargeSpeleothemConfiguration> {
	public LargeSpeleothemFeature(Codec<LargeSpeleothemConfiguration> codec) {
		super(codec);
	}

	@Override
	public boolean place(FeaturePlaceContext<LargeSpeleothemConfiguration> context) {
		WorldGenLevel level = context.level();
		BlockPos origin = context.origin();
		LargeSpeleothemConfiguration config = context.config();
		RandomSource random = context.random();
		if (!SpeleothemUtils.isEmptyOrWater(level, origin)) {
			return false;
		} else {
			Optional<Column> column = Column.scan(
				level,
				origin,
				config.floorToCeilingSearchRange(),
				SpeleothemUtils::isEmptyOrWater,
				state -> SpeleothemUtils.isBaseOrLava(state, config.baseBlock().getBlock(), config.replaceableBlocks())
			);
			if (column.isPresent() && column.get() instanceof Column.Range columnRange) {
				if (columnRange.height() < 4) {
					return false;
				} else {
					int maxColumnRadiusBasedOnColumnHeight = (int)(columnRange.height() * config.maxColumnRadiusToCaveHeightRatio());
					int maxColumnRadius = Mth.clamp(maxColumnRadiusBasedOnColumnHeight, config.columnRadius().getMinValue(), config.columnRadius().getMaxValue());
					int radius = Mth.randomBetweenInclusive(random, config.columnRadius().getMinValue(), maxColumnRadius);
					LargeSpeleothem stalactite = makeSpeleothem(
						config.baseBlock(),
						origin.atY(columnRange.ceiling() - 1),
						false,
						random,
						radius,
						config.stalactiteBluntness(),
						config.heightScale()
					);
					LargeSpeleothem stalagmite = makeSpeleothem(
						config.baseBlock(),
						origin.atY(columnRange.floor() + 1),
						true,
						random,
						radius,
						config.stalagmiteBluntness(),
						config.heightScale()
					);
					WindOffsetter wind;
					if (stalactite.isSuitableForWind(config) && stalagmite.isSuitableForWind(config)) {
						wind = new WindOffsetter(origin.getY(), random, config.windSpeed(), 16 - radius);
					} else {
						wind = WindOffsetter.noWind();
					}

					boolean stalactiteBaseEmbeddedInStone = stalactite.moveBackUntilBaseIsInsideStoneAndShrinkRadiusIfNecessary(level, wind);
					boolean stalagmiteBaseEmbeddedInStone = stalagmite.moveBackUntilBaseIsInsideStoneAndShrinkRadiusIfNecessary(level, wind);
					if (stalactiteBaseEmbeddedInStone) {
						stalactite.placeBlocks(level, random, wind);
					}

					if (stalagmiteBaseEmbeddedInStone) {
						stalagmite.placeBlocks(level, random, wind);
					}
					
					return true;
				}
			} else {
				return false;
			}
		}
	}

	private static LargeSpeleothem makeSpeleothem(
		BlockState baseBlock,
		BlockPos root,
		boolean pointingUp,
		RandomSource random,
		int radius,
		FloatProvider bluntness,
		FloatProvider heightScale
	) {
		return new LargeSpeleothem(baseBlock, root, pointingUp, radius, bluntness.sample(random), heightScale.sample(random));
	}

	static final class LargeSpeleothem {
		private final BlockState baseBlock;
		private BlockPos root;
		private final boolean pointingUp;
		private int radius;
		private final double bluntness;
		private final double scale;

		LargeSpeleothem(BlockState baseBlock, BlockPos root, boolean pointingUp, int radius, double bluntness, double scale) {
            this.baseBlock = baseBlock;
            this.root = root;
			this.pointingUp = pointingUp;
			this.radius = radius;
			this.bluntness = bluntness;
			this.scale = scale;
		}

		private int getHeight() {
			return this.getHeightAtRadius(0.0F);
		}
		
		boolean moveBackUntilBaseIsInsideStoneAndShrinkRadiusIfNecessary(WorldGenLevel level, WindOffsetter wind) {
			while (this.radius > 1) {
				BlockPos.MutableBlockPos newRoot = this.root.mutable();
				int maxTries = Math.min(10, this.getHeight());
				
				for (int i = 0; i < maxTries; i++) {
					if (level.getBlockState(newRoot).is(Blocks.LAVA)) {
						return false;
					}
					
					if (SpeleothemUtils.isCircleMostlyEmbeddedInStone(level, wind.offset(newRoot), this.radius)) {
						this.root = newRoot;
						return true;
					}
					
					newRoot.move(this.pointingUp ? Direction.DOWN : Direction.UP);
				}
				
				this.radius /= 2;
			}
			
			return false;
		}
		
		private int getHeightAtRadius(float radius) {
			return (int)SpeleothemUtils.getSpeleothemHeight(radius, this.radius, this.scale, this.bluntness);
		}
		
		void placeBlocks(WorldGenLevel level, RandomSource random, WindOffsetter wind) {
			for (int dx = -this.radius; dx <= this.radius; dx++) {
				for (int dz = -this.radius; dz <= this.radius; dz++) {
					float currentRadius = Mth.sqrt(dx * dx + dz * dz);
					if (!(currentRadius > this.radius)) {
						int height = this.getHeightAtRadius(currentRadius);
						if (height > 0) {
							if (random.nextFloat() < 0.2) {
								height = (int)(height * Mth.randomBetween(random, 0.8F, 1.0F));
							}
							
							BlockPos.MutableBlockPos pos = this.root.offset(dx, 0, dz).mutable();
							boolean hasBeenOutOfStone = false;
							int maxY = this.pointingUp ? level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, pos.getX(), pos.getZ()) : Integer.MAX_VALUE;
							
							for (int i = 0; i < height && pos.getY() < maxY; i++) {
								BlockPos windAdjustedPos = wind.offset(pos);
								if (SpeleothemUtils.isEmptyOrWaterOrLava(level, windAdjustedPos)) {
									hasBeenOutOfStone = true;
									Block block = this.baseBlock.getBlock();
									level.setBlock(windAdjustedPos, block.defaultBlockState(), 2);
								} else if (hasBeenOutOfStone && level.getBlockState(windAdjustedPos).is(BlockTags.BASE_STONE_OVERWORLD)) {
									break;
								}
								
								pos.move(this.pointingUp ? Direction.UP : Direction.DOWN);
							}
						}
					}
				}
			}
		}

		boolean isSuitableForWind(LargeSpeleothemConfiguration config) {
			return this.radius >= config.minRadiusForWind() && this.bluntness >= config.minBluntnessForWind();
		}
	}

	static final class WindOffsetter {
		private final int originY;
		@Nullable private final Vec3 windSpeed;
		private final int maxOffset;

		WindOffsetter(int originY, RandomSource random, FloatProvider windSpeedRange, int maxOffset) {
			this.originY = originY;
            this.maxOffset = maxOffset;
            float speed = windSpeedRange.sample(random);
			float direction = Mth.randomBetween(random, 0.0F, (float) Math.PI);
			this.windSpeed = new Vec3(Mth.cos(direction) * speed, 0.0, Mth.sin(direction) * speed);
		}

		private WindOffsetter() {
            this.originY = 0;
			this.windSpeed = null;
            this.maxOffset = 0;
		}

		static WindOffsetter noWind() {
			return new WindOffsetter();
		}

		BlockPos offset(BlockPos pos) {
			if (this.windSpeed == null) {
				return pos;
			} else {
				int dy = this.originY - pos.getY();
				Vec3 totalWindAdjust = this.windSpeed.scale(dy);
				int dx = Mth.clamp(Mth.floor(totalWindAdjust.x), -this.maxOffset, this.maxOffset);
				int dz = Mth.clamp(Mth.floor(totalWindAdjust.z), -this.maxOffset, this.maxOffset);
				return pos.offset(dx, 0, dz);
			}
		}
	}
}
