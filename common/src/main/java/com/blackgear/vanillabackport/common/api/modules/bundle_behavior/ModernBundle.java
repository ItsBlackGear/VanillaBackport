package com.blackgear.vanillabackport.common.api.modules.bundle_behavior;

public interface ModernBundle {
    void setSelectedItem(int index);

    default int getSelectedItem() {
        return -1;
    }

    int getNumberOfItemsToShow();

    interface Mutable {
        void toggleSelectedItem(int index);

        boolean indexIsOutsideAllowedBounds(int index);
    }
}