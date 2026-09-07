package com.blackgear.vanillabackport.common.commands.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FastColor.ARGB32;

public class HexColorArgument implements ArgumentType<Integer> {
	private static final Collection<String> EXAMPLES = Arrays.asList("F00", "FF0000");
	public static final DynamicCommandExceptionType ERROR_INVALID_HEX = new DynamicCommandExceptionType(value -> Component.translatableEscape("argument.hexcolor.invalid", value));

	public static HexColorArgument hexColor() {
		return new HexColorArgument();
	}

	public static Integer getHexColor(CommandContext<CommandSourceStack> context, String name) {
		return context.getArgument(name, Integer.class);
	}

	public Integer parse(StringReader reader) throws CommandSyntaxException {
		String colorString = reader.readUnquotedString();

		return switch (colorString.length()) {
			case 3 -> ARGB32.color(
				duplicateDigit(Integer.parseInt(colorString, 0, 1, 16)),
				duplicateDigit(Integer.parseInt(colorString, 1, 2, 16)),
				duplicateDigit(Integer.parseInt(colorString, 2, 3, 16))
			);
			case 6 -> ARGB32.color(
				Integer.parseInt(colorString, 0, 2, 16),
				Integer.parseInt(colorString, 2, 4, 16),
				Integer.parseInt(colorString, 4, 6, 16)
			);
			default -> throw ERROR_INVALID_HEX.createWithContext(reader, colorString);
		};
	}

	private static int duplicateDigit(int digit) {
		return digit * 17;
	}

	@Override
	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> contextBuilder, SuggestionsBuilder builder) {
		return SharedSuggestionProvider.suggest(EXAMPLES, builder);
	}

	@Override
	public Collection<String> getExamples() {
		return EXAMPLES;
	}
}
