package com.gilfort.zauberei.helpers;

import com.gilfort.zauberei.item.armor.ArmorEffects;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.Set;


public class CommandHandler {
    private static final Set<String> VALID_MAJORS = Set.of(
            "Summoning", "Hemomagic", "Arcanes", "Elemental",
            "CircleOfLife", "MagicalCombat", "Otherworld"
    );

    public static void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("zauberei")
                        .then(Commands.literal("setmajor")
                                .then(Commands.argument("major", StringArgumentType.word())
                                        .executes(CommandHandler::setMajorCommand)))
                        .then(Commands.literal("checktag")
                                .then(Commands.argument("player", StringArgumentType.word())
                                        .executes(CommandHandler::checkTagCommand)))
                        .then(Commands.literal("setyear")
                                .then(Commands.argument("year", IntegerArgumentType.integer())
                                        .executes(CommandHandler::setYearCommand)))
                        .then(Commands.literal("checkyeartag")
                                .then(Commands.argument("player", StringArgumentType.word())
                                        .executes(CommandHandler::checkYearTagCommand)))
        );
    }

    private static int setMajorCommand(CommandContext<CommandSourceStack> context) {
        String major = StringArgumentType.getString(context, "major");
        CommandSourceStack source = context.getSource();

        if (!(source.getEntity() instanceof ServerPlayer player)) {
            source.sendFailure(Component.literal("Dieser Befehl kann nur von einem Spieler ausgeführt werden."));
            return Command.SINGLE_SUCCESS;
        }

        if (!VALID_MAJORS.contains(major)) {
            source.sendFailure(Component.literal("Ungültiger Major-Typ. Gültige Optionen: " + VALID_MAJORS));
            return Command.SINGLE_SUCCESS;
        }

        PlayerDataHelper.setMajor(player, major);
        source.sendSuccess(() -> Component.literal("Mayor-Typ gesetzt: " + major), true);

        return Command.SINGLE_SUCCESS;
    }

    private static int setYearCommand(CommandContext<CommandSourceStack> context) {
        int year = IntegerArgumentType.getInteger(context, "year");
        CommandSourceStack source = context.getSource();

        if (!(source.getEntity() instanceof ServerPlayer player)) {
            source.sendFailure(Component.literal("Dieser Befehl kann nur von einem Spieler ausgeführt werden."));
            return Command.SINGLE_SUCCESS;
        }

        PlayerDataHelper.setYear(player, year);
        source.sendSuccess(() -> Component.literal("Year gesetzt: " + year), true);

        return Command.SINGLE_SUCCESS;
    }

    private static int checkTagCommand(CommandContext<CommandSourceStack> context) {
        String playerName = StringArgumentType.getString(context, "player");
        CommandSourceStack source = context.getSource();

        ServerPlayer targetPlayer = source.getServer().getPlayerList().getPlayerByName(playerName);
        if (targetPlayer == null) {
            source.sendFailure(Component.literal("Spieler " + playerName + " wurde nicht gefunden."));
            return Command.SINGLE_SUCCESS;
        }

        String major = PlayerDataHelper.getMajor(targetPlayer);
        source.sendSuccess(() -> Component.literal("Player:" + playerName + " hat den Major-Typ: " + major), true);

        return Command.SINGLE_SUCCESS;
    }

    private static int checkYearTagCommand(CommandContext<CommandSourceStack> context) {
        String playerName = StringArgumentType.getString(context, "player");
        CommandSourceStack source = context.getSource();

        ServerPlayer targetPlayer = source.getServer().getPlayerList().getPlayerByName(playerName);
        if (targetPlayer == null) {
            source.sendFailure(Component.literal("Spieler " + playerName + " wurde nicht gefunden."));
            return Command.SINGLE_SUCCESS;
        }

        int year = PlayerDataHelper.getYear(targetPlayer);
        source.sendSuccess(() -> Component.literal("Player:" + playerName + " ist im Jahr: " + year), true);
        ArmorEffects.testArmorEffects(targetPlayer, "summoning");

        return Command.SINGLE_SUCCESS;
    }

}