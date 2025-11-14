package com.blackgear.vanillabackport.common.level.entities.armadillo;

import net.minecraft.util.StringRepresentable;

public enum ArmadilloState implements StringRepresentable {
    IDLE("idle", false, 0, 0) {
        @Override
        public boolean shouldHideInShell(long duration) {
            return false;
        }
    },
    ROLLING("rolling", true, 10, 1) {
        @Override
        public boolean shouldHideInShell(long duration) {
            return duration > 5L;
        }
    },
    SCARED("scared", true, 50, 2) {
        @Override
        public boolean shouldHideInShell(long duration) {
            return true;
        }
    },
    UNROLLING("unrolling", true, 30, 3) {
        @Override
        public boolean shouldHideInShell(long duration) {
            return duration < 26L;
        }
    };

    public static final StringRepresentable.EnumCodec<ArmadilloState> CODEC = StringRepresentable.fromEnum(ArmadilloState::values);

    private final String name;
    private final boolean isThreatened;
    private final int animationDuration;
    private final int id;

    ArmadilloState(String name, boolean isThreatened, int animationDuration, int id) {
        this.name = name;
        this.isThreatened = isThreatened;
        this.animationDuration = animationDuration;
        this.id = id;
    }

    public static ArmadilloState fromName(String name) {
        return CODEC.byName(name, IDLE);
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }

    public int id() {
        return this.id;
    }

    public abstract boolean shouldHideInShell(long duration);

    public boolean isThreatened() {
        return this.isThreatened;
    }

    public int animationDuration() {
        return this.animationDuration;
    }
}