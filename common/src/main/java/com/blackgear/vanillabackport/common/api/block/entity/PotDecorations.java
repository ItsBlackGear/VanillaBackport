package com.blackgear.vanillabackport.common.api.block.entity;

import com.mojang.serialization.Codec;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Stream;

public record PotDecorations(Optional<Item> back, Optional<Item> left, Optional<Item> right, Optional<Item> front) /*implements TooltipProvider*/ {
    /*public static final PotDecorations EMPTY = new PotDecorations(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());
    public static final Codec<PotDecorations> CODEC = BuiltInRegistries.ITEM
            .byNameCodec()
            .sizeLimitedListOf(4)
            .xmap(PotDecorations::new, PotDecorations::ordered);
    public static final StreamCodec<RegistryFriendlyByteBuf, PotDecorations> STREAM_CODEC = ByteBufCodecs.registry(Registries.ITEM)
            .apply(ByteBufCodecs.list(4))
            .map(PotDecorations::new, PotDecorations::ordered);

    private PotDecorations(List<Item> pDecorations) {
        this(getItem(pDecorations, 0), getItem(pDecorations, 1), getItem(pDecorations, 2), getItem(pDecorations, 3));
    }

    public PotDecorations(Item pBack, Item pLeft, Item pRight, Item pFront) {
        this(List.of(pBack, pLeft, pRight, pFront));
    }

    private static Optional<Item> getItem(List<Item> pDecorations, int pIndex) {
        if (pIndex >= pDecorations.size()) {
            return Optional.empty();
        } else {
            Item item = pDecorations.get(pIndex);
            return item == Items.BRICK ? Optional.empty() : Optional.of(item);
        }
    }

    public List<Item> ordered() {
        return Stream.of(this.back, this.left, this.right, this.front).map(p_330456_ -> p_330456_.orElse(Items.BRICK)).toList();
    }

    @Override
    public void addToTooltip(Item.TooltipContext p_396913_, Consumer<Component> p_396999_, TooltipFlag p_393266_, DataComponentGetter p_394963_) {
        if (!this.equals(EMPTY)) {
            p_396999_.accept(CommonComponents.EMPTY);
            addSideDetailsToTooltip(p_396999_, this.front);
            addSideDetailsToTooltip(p_396999_, this.left);
            addSideDetailsToTooltip(p_396999_, this.right);
            addSideDetailsToTooltip(p_396999_, this.back);
        }
    }

    private static void addSideDetailsToTooltip(Consumer<Component> pTooltipAdder, Optional<Item> pItem) {
        pTooltipAdder.accept(new ItemStack(pItem.orElse(Items.BRICK), 1).getHoverName().plainCopy().withStyle(ChatFormatting.GRAY));
    }*/
}
