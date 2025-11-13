package com.blackgear.vanillabackport.common.api.bundle;

public interface IBundle {
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