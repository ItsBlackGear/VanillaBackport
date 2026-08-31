package com.blackgear.vanillabackport.common.level.entities.decoration;

import com.blackgear.vanillabackport.client.registries.ModSoundEvents;
import com.blackgear.vanillabackport.common.registries.entities.ModEntityDataSerializers;
import com.blackgear.vanillabackport.common.registries.items.ModItems;
import com.blackgear.vanillabackport.core.util.Utilities.CollisionUtils;
import com.blackgear.vanillabackport.core.util.Utilities.PositionUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.Map;

public class Cushion extends BlockAttachedEntity {
    public static final Map<DyeColor, CushionData> CUSHIONS_BY_DYE = new EnumMap<>(DyeColor.class);
    private static final DyeColor DEFAULT_COLOR = DyeColor.WHITE;
    private static final EntityDataAccessor<DyeColor> DATA_COLOR = SynchedEntityData.defineId(Cushion.class, ModEntityDataSerializers.DYE_COLOR.get());
    
    public Cushion(EntityType<? extends BlockAttachedEntity> entityType, Level level) {
        super(entityType, level);
    }
    
    public DyeColor getColor() {
        return this.entityData.get(DATA_COLOR);
    }
    
    public void setColor(DyeColor color) {
        this.entityData.set(DATA_COLOR, color);
    }
    
    @Override
    public void dropItem(@Nullable Entity entity) {
        this.playSound(ModSoundEvents.CUSHION_BREAK.get());
        this.showBreakingParticles();
        if (this.level() instanceof ServerLevel level && level.getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
            if (!(entity instanceof Player player && player.getAbilities().instabuild)) {
                ItemStack cushion = new ItemStack(Cushion.getByColor(this.getColor()).cushion());
                cushion.setHoverName(getCustomName());
                this.spawnAtLocation(cushion);
            }
        }
    }
    
    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        if (!player.isSecondaryUseActive() && !this.isVehicle() && (this.level().isClientSide() || player.startRiding(this))) {
            if (!this.level().isClientSide()) {
                this.playSound(ModSoundEvents.CUSHION_SIT.get());
                return InteractionResult.CONSUME;
            } else {
                return InteractionResult.SUCCESS;
            }
        } else {
            return InteractionResult.PASS;
        }
    }
    
    @Override
    protected void removePassenger(Entity passenger) {
        super.removePassenger(passenger);
        if (!this.level().isClientSide() && this.getRemovalReason() == null) {
            this.playSound(ModSoundEvents.CUSHION_GET_UP.get());
        }
    }
    
    @Override
    public @Nullable ItemStack getPickResult() {
        return new ItemStack(Cushion.getByColor(this.getColor()).cushion());
    }
    
    @Override
    public void setPos(double x, double y, double z) {
        this.setPosRaw(x, y, z);
        super.setPos(x, y, z);
    }
    
    private void showBreakingParticles() {
        if (this.level() instanceof ServerLevel level) {
            level.sendParticles(
                new BlockParticleOption(ParticleTypes.BLOCK, Cushion.getByColor(this.getColor()).wool().defaultBlockState()),
                this.getX(),
                this.getY(0.67F),
                this.getZ(),
                10,
                this.getBbWidth() / 4.0F,
                this.getBbWidth() / 4.0F,
                this.getBbWidth() / 4.0F,
                0.05
            );
        }
    }

    public static boolean canBePlacedAt(final Level level, final AABB boundingBox) {
        return wouldSurviveAt(level, boundingBox) && !isAnchorBuried(level, boundingBox);
    }

    public static boolean wouldSurviveAt(final Level level, final AABB boundingBox) {
        return hasAnchorBelow(level, boundingBox) && !isCoveredBySuffocatingBlocks(level, boundingBox);
    }

    private static boolean isCoveredBySuffocatingBlocks(final Level level, final AABB boundingBox) {
        for (BlockPos blockPos : PositionUtils.betweenClosed(CollisionUtils.nextDeflated(boundingBox))) {
            if (!level.getBlockState(blockPos).isSuffocating(level, blockPos)) {
                return false;
            }
        }

        return true;
    }

    public static boolean hasAnchorBelow(Level level, AABB box) {
        AABB anchorBox = new AABB(box.minX, box.minY - 0.015625, box.minZ, Math.nextDown(box.maxX), box.minY, Math.nextDown(box.maxZ));

        for (BlockPos pos : PositionUtils.betweenClosed(anchorBox)) {
            BlockState state = level.getBlockState(pos);
            VoxelShape shape = state.getShape(level, pos);
            if (!shape.isEmpty() && shape.bounds().move(pos).intersects(anchorBox)) {
                return true;
            }
        }

        return false;
    }

    private static boolean isAnchorBuried(final Level level, final AABB boundingBox) {
        AABB restingSlice = CollisionUtils.nextDeflated(
            new AABB(boundingBox.minX, boundingBox.minY, boundingBox.minZ, boundingBox.maxX, boundingBox.minY + 0.015625, boundingBox.maxZ)
        );
        VoxelShape exposedSurface = Shapes.create(restingSlice);

        for (VoxelShape collider : level.getBlockCollisions(null, restingSlice)) {
            exposedSurface = Shapes.join(exposedSurface, collider, BooleanOp.ONLY_FIRST);
            if (exposedSurface.isEmpty()) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean survives() {
        return wouldSurviveAt(this.level(), this.getBoundingBox());
    }

    @Override
    public void tick() {
        super.tick();

        if (level() instanceof ServerLevel serverLevel) {
            destroyIfInFire(serverLevel);
        }
    }

    public void destroyIfInFire(ServerLevel level) {
        if (this.isRemoved()) return;

        boolean isInFire = level
            .getBlockStates(CollisionUtils.nextDeflated(getBoundingBox()))
            .anyMatch(blockState -> blockState.is(BlockTags.FIRE));

        if (isInFire) hurt(damageSources().inFire(), 1.0f);
    }

    @Override
    public void thunderHit(ServerLevel level, LightningBolt lightning) {
        if (!this.isRemoved()) {
            this.kill();
            this.dropItem(null);
        }
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        return !isBreakingDeniedFor(source) && super.hurt(source, amount);
    }

    private boolean isBreakingDeniedFor(final DamageSource source) {
        if (!(source.getEntity() instanceof Player player)) return false;

        boolean deniedBecauseEntity = !player.mayBuild();
        boolean deniedBecauseLevel = !this.level().mayInteract(player, this.pos);

        return deniedBecauseEntity || deniedBecauseLevel;
    }

    @Override
    protected void recalculateBoundingBox() {
        this.setBoundingBox(this.makeBoundingBox());
    }
    
    @Override
    protected void defineSynchedData() {
        this.entityData.define(DATA_COLOR, DEFAULT_COLOR);
    }
    
    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putByte("color", (byte) this.getColor().getId());
    }
    
    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setColor(DyeColor.byId(compound.getByte("color")));
    }
    
    public static void register(DyeColor color, Block wool, Item cushion) {
        CUSHIONS_BY_DYE.put(color, new CushionData(wool, cushion));
    }
    
    public static CushionData getByColor(DyeColor color) {
        return CUSHIONS_BY_DYE.getOrDefault(color, new CushionData(Blocks.WHITE_WOOL, ModItems.WHITE_CUSHION.get()));
    }
    
    static {
        register(DyeColor.WHITE, Blocks.WHITE_WOOL, ModItems.WHITE_CUSHION.get());
        register(DyeColor.ORANGE, Blocks.ORANGE_WOOL, ModItems.ORANGE_CUSHION.get());
        register(DyeColor.MAGENTA, Blocks.MAGENTA_WOOL, ModItems.MAGENTA_CUSHION.get());
        register(DyeColor.LIGHT_BLUE, Blocks.LIGHT_BLUE_WOOL, ModItems.LIGHT_BLUE_CUSHION.get());
        register(DyeColor.YELLOW, Blocks.YELLOW_WOOL, ModItems.YELLOW_CUSHION.get());
        register(DyeColor.LIME, Blocks.LIME_WOOL, ModItems.LIME_CUSHION.get());
        register(DyeColor.PINK, Blocks.PINK_WOOL, ModItems.PINK_CUSHION.get());
        register(DyeColor.GRAY, Blocks.GRAY_WOOL, ModItems.GRAY_CUSHION.get());
        register(DyeColor.LIGHT_GRAY, Blocks.LIGHT_GRAY_WOOL, ModItems.LIGHT_GRAY_CUSHION.get());
        register(DyeColor.CYAN, Blocks.CYAN_WOOL, ModItems.CYAN_CUSHION.get());
        register(DyeColor.PURPLE, Blocks.PURPLE_WOOL, ModItems.PURPLE_CUSHION.get());
        register(DyeColor.BLUE, Blocks.BLUE_WOOL, ModItems.BLUE_CUSHION.get());
        register(DyeColor.BROWN, Blocks.BROWN_WOOL, ModItems.BROWN_CUSHION.get());
        register(DyeColor.GREEN, Blocks.GREEN_WOOL, ModItems.GREEN_CUSHION.get());
        register(DyeColor.RED, Blocks.RED_WOOL, ModItems.RED_CUSHION.get());
        register(DyeColor.BLACK, Blocks.BLACK_WOOL, ModItems.BLACK_CUSHION.get());
    }
    
    public record CushionData(Block wool, Item cushion) { /* NO-OP */ }
}