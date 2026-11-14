package com.blackgear.vanillabackport.common.level.recipes;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.Optional;
import net.minecraft.core.NonNullList;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class RecipeCache {
	private final RecipeCache.Entry[] entries;
	private WeakReference<RecipeManager> cachedRecipeManager = new WeakReference<>(null);

	public RecipeCache(int size) {
		this.entries = new RecipeCache.Entry[size];
	}

	public Optional<CraftingRecipe> get(Level level, CraftingContainer input) {
		if (input.isEmpty()) {
			return Optional.empty();
		} else {
			this.validateRecipeManager(level);

			for (int i = 0; i < this.entries.length; i++) {
				RecipeCache.Entry entry = this.entries[i];
				if (entry != null && entry.matches(input)) {
					this.moveEntryToFront(i);
					return Optional.ofNullable(entry.value());
				}
			}

			return this.compute(input, level);
		}
	}

	private void validateRecipeManager(Level level) {
		RecipeManager recipeManager = level.getRecipeManager();
		if (recipeManager != this.cachedRecipeManager.get()) {
			this.cachedRecipeManager = new WeakReference<>(recipeManager);
			Arrays.fill(this.entries, null);
		}
	}

	private Optional<CraftingRecipe> compute(CraftingContainer input, Level level) {
		Optional<CraftingRecipe> optional = level.getRecipeManager().getRecipeFor(RecipeType.CRAFTING, input, level);
		this.insert(input, optional.orElse(null));
		return optional;
	}

	private void moveEntryToFront(int index) {
		if (index > 0) {
			RecipeCache.Entry entry = this.entries[index];
			System.arraycopy(this.entries, 0, this.entries, 1, index);
			this.entries[0] = entry;
		}
	}

	private void insert(CraftingContainer input, @Nullable CraftingRecipe recipe) {
		NonNullList<ItemStack> nonNullList = NonNullList.withSize(input.getItems().size(), ItemStack.EMPTY);

		for (int i = 0; i < input.getItems().size(); i++) {
			nonNullList.set(i, input.getItem(i).copyWithCount(1));
		}

		System.arraycopy(this.entries, 0, this.entries, 1, this.entries.length - 1);
		this.entries[0] = new RecipeCache.Entry(nonNullList, input.getWidth(), input.getHeight(), recipe);
	}

	record Entry(NonNullList<ItemStack> key, int width, int height, @Nullable CraftingRecipe value) {
		public boolean matches(CraftingContainer input) {
			if (this.width == input.getWidth() && this.height == input.getHeight()) {
				for (int i = 0; i < this.key.size(); i++) {
					if (!ItemStack.isSameItemSameTags(this.key.get(i), input.getItem(i))) {
						return false;
					}
				}

				return true;
			} else {
				return false;
			}
		}
	}
}
