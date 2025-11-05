package com.justproject.fpsdisplayer;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ServerBindCommand {
    
    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSource> dispatcher = event.getDispatcher();
        
        // Server-side bind command
        dispatcher.register(Commands.literal("sbind")
            .executes(context -> {
                context.getSource().sendSuccess(new StringTextComponent(TextFormatting.RED + "Usage: /sbind <key> \"<command>\""), false);
                context.getSource().sendSuccess(new StringTextComponent(TextFormatting.YELLOW + "Example: " + TextFormatting.WHITE + "/sbind g \"spawn\""), false);
                context.getSource().sendSuccess(new StringTextComponent(TextFormatting.GRAY + "Note: Binds only work in singleplayer"), false);
                return 1;
            })
            .then(Commands.argument("key", StringArgumentType.string())
                .then(Commands.argument("command", StringArgumentType.greedyString())
                    .executes(context -> {
                        String key = StringArgumentType.getString(context, "key").toLowerCase();
                        String command = StringArgumentType.getString(context, "command");
                        
                        if (command.startsWith("\"") && command.endsWith("\"")) {
                            command = command.substring(1, command.length() - 1);
                        }
                        
                        context.getSource().sendSuccess(new StringTextComponent(TextFormatting.GREEN + "Server bind created for key: " + TextFormatting.YELLOW + key), true);
                        context.getSource().sendSuccess(new StringTextComponent(TextFormatting.GRAY + "Command: " + TextFormatting.AQUA + "/" + command), false);
                        context.getSource().sendSuccess(new StringTextComponent(TextFormatting.RED + "Note: Key binds only work in singleplayer!"), false);
                        return 1;
                    })
                )
            )
        );
        
        // Server-side binds list
        dispatcher.register(Commands.literal("sbinds")
            .executes(context -> {
                context.getSource().sendSuccess(new StringTextComponent(TextFormatting.GOLD + "Server Bind Info"), false);
                context.getSource().sendSuccess(new StringTextComponent(TextFormatting.GRAY + "Key binds are client-side only"), false);
                context.getSource().sendSuccess(new StringTextComponent(TextFormatting.GRAY + "They work only in singleplayer"), false);
                context.getSource().sendSuccess(new StringTextComponent(TextFormatting.YELLOW + "Use /sbind to create server-side bind records"), false);
                return 1;
            })
        );
    }
}