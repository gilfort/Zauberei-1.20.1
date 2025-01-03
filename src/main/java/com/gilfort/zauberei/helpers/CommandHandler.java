package com.gilfort.zauberei.helpers;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
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

            return Command.SINGLE_SUCCESS;
        }
    }