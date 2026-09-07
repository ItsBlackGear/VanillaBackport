package com.blackgear.vanillabackport.common.commands.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public class ColorArgument implements ArgumentType<ChatFormatting> {
    private static final Collection<String> EXAMPLES = Arrays.asList("red", "green");
    public static final DynamicCommandExceptionType ERROR_INVALID_VALUE = new DynamicCommandExceptionType(value -> Component.translatable("argument.color.invalid", value));

    public static ColorArgument color() {
        return new ColorArgument();
    }
    
    public static ChatFormatting getTeamColor(CommandContext<CommandSourceStack> context, String name) {
        return context.getArgument(name, ChatFormatting.class);
    }
    
    @Override
    public ChatFormatting parse(StringReader reader) throws CommandSyntaxException {
        String id = reader.readUnquotedString();
        ChatFormatting result = ChatFormatting.getByName(id);
        if (result == null) {
            throw ERROR_INVALID_VALUE.createWithContext(reader, id);
        } else {
            return result;
        }
    }
    
    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return SharedSuggestionProvider.suggest(ChatFormatting.getNames(true, false), builder);
    }
    
    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }
}