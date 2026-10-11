package com.blackgear.vanillabackport.common.level.items.spear;

import net.minecraft.util.StringRepresentable;

public enum SwingAnimationType implements StringRepresentable {
    NONE(0, "none"),
    WHACK(1, "whack"),
    STAB(2, "stab");
    
    private final int id;
    private final String name;
    
    SwingAnimationType(int id, String name) {
        this.id = id;
        this.name = name;
    }
    
    public int getId() {
        return this.id;
    }
    
    @Override
    public String getSerializedName() {
        return this.name;
    }
}