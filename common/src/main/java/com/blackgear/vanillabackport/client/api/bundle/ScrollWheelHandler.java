package com.blackgear.vanillabackport.client.api.bundle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.joml.Vector2i;

@Environment(EnvType.CLIENT)
public class ScrollWheelHandler {
    private double accumulatedScrollY;

    public Vector2i onMouseScroll(double delta) {
        if (this.accumulatedScrollY != 0.0 && Math.signum(delta) != Math.signum(this.accumulatedScrollY)) {
            this.accumulatedScrollY = 0.0;
        }

        this.accumulatedScrollY += delta;
        int scroll = (int) this.accumulatedScrollY;
        if (scroll == 0) {
            return new Vector2i(0, 0);
        } else {
            this.accumulatedScrollY -= scroll;
            return new Vector2i(0, scroll);
        }
    }

    public static int getNextScrollWheelSelection(double delta, int index, int max) {
        int direction = (int) Math.signum(delta);
        index -= direction;
        index = Math.max(-1, index);

        while (index < 0) {
            index += max;
        }

        while (index >= max) {
            index -= max;
        }

        return index;
    }
}
