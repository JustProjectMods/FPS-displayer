package com.justproject.fpsdisplayer;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Arrays;
import java.util.List;

public class ChatBindCommand {
    
    private static final List<String> AVAILABLE_KEYS = Arrays.asList(
        "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", 
        "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z",
        "shift", "ctrl", "alt", "space", "tab", "capslock",
        "f1", "f2", "f3", "f4", "f5", "f6", "f7", "f8", "f9", "f10", "f11", "f12",
        "1", "2", "3", "4", "5", "6", "7", "8", "9", "0"
    );
    
    private static final SuggestionProvider<CommandSource> KEY_SUGGESTIONS = 
        (context, builder) -> ISuggestionProvider.suggest(AVAILABLE_KEYS, builder);
    
    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSource> dispatcher = event.getDispatcher();
        
        // .bind command
        dispatcher.register(Commands.literal("bind")
            .executes(context -> {
                context.getSource().sendSuccess(new StringTextComponent(TextFormatting.RED + "Usage: .bind <key> <command>"), false);
                context.getSource().sendSuccess(new StringTextComponent(TextFormatting.YELLOW + "Example: " + TextFormatting.WHITE + ".bind g spawn"), false);
                context.getSource().sendSuccess(new StringTextComponent(TextFormatting.GRAY + "Available keys: a-z, shift, ctrl, alt, space, f1-f12, 0-9"), false);
                return 1;
            })
            .then(Commands.argument("key", StringArgumentType.string())
                .suggests(KEY_SUGGESTIONS)
                .then(Commands.argument("command", StringArgumentType.greedyString())
                    .executes(context -> {
                        String key = StringArgumentType.getString(context, "key").toLowerCase();
                        String command = StringArgumentType.getString(context, "command");
                        
                        if (UniversalBindSystem.createBind(key, command)) {
                            context.getSource().sendSuccess(new StringTextComponent(TextFormatting.GREEN + "Key " + TextFormatting.YELLOW + key + 
                                       TextFormatting.GREEN + " bound to: " + TextFormatting.AQUA + "/" + command), false);
                            context.getSource().sendSuccess(new StringTextComponent(TextFormatting.GRAY + "Now pressing " + TextFormatting.YELLOW + key + 
                                       TextFormatting.GRAY + " will execute this command"), false);
                        } else {
                            context.getSource().sendSuccess(new StringTextComponent(TextFormatting.RED + "Unknown key: " + key), false);
                            context.getSource().sendSuccess(new StringTextComponent(TextFormatting.YELLOW + "Available keys: a-z, shift, ctrl, alt, space, f1-f12, 0-9"), false);
                        }
                        return 1;
                    })
                )
            )
        );
        
        // .unbind command
        dispatcher.register(Commands.literal("unbind")
            .executes(context -> {
                context.getSource().sendSuccess(new StringTextComponent(TextFormatting.RED + "Usage: .unbind <key>"), false);
                context.getSource().sendSuccess(new StringTextComponent(TextFormatting.YELLOW + "Example: " + TextFormatting.WHITE + ".unbind g"), false);
                return 1;
            })
            .then(Commands.argument("key", StringArgumentType.string())
                .suggests((context, builder) -> {
                    // Suggest only existing binds for removal
                    String bindsList = UniversalBindSystem.getBindsList();
                    if (bindsList.equals("No active binds")) {
                        return ISuggestionProvider.suggest(new String[0], builder);
                    }
                    
                    String[] binds = bindsList.replace("Active binds:\n", "").split("\n");
                    List<String> existingKeys = Arrays.stream(binds)
                        .filter(s -> s.contains("->"))
                        .map(s -> s.split("->")[0].trim())
                        .collect(java.util.stream.Collectors.toList());
                    
                    return ISuggestionProvider.suggest(existingKeys, builder);
                })
                .executes(context -> {
                    String key = StringArgumentType.getString(context, "key").toLowerCase();
                    
                    if (UniversalBindSystem.removeBind(key)) {
                        context.getSource().sendSuccess(new StringTextComponent(TextFormatting.GREEN + "Bind for key " + TextFormatting.YELLOW + key + 
                                   TextFormatting.GREEN + " removed"), false);
                    } else {
                        context.getSource().sendSuccess(new StringTextComponent(TextFormatting.RED + "No bind found for key: " + key), false);
                    }
                    return 1;
                })
            )
        );
        
        // .binds command
        dispatcher.register(Commands.literal("binds")
            .executes(context -> {
                String bindsList = UniversalBindSystem.getBindsList();
                context.getSource().sendSuccess(new StringTextComponent(TextFormatting.GOLD + "Binds: " + bindsList), false);
                return 1;
            })
        );
    }
    
    // Chat handler for compatibility
    @SubscribeEvent
    public void onClientChat(ClientChatEvent event) {
        String message = event.getMessage();
        
        // Old processing for compatibility
        if (message.startsWith(".bind ")) {
            event.setCanceled(true);
            handleBindCommand(message.substring(6));
        } else if (message.equals(".binds")) {
            event.setCanceled(true);
            handleBindsCommand();
        } else if (message.startsWith(".unbind ")) {
            event.setCanceled(true);
            handleUnbindCommand(message.substring(8));
        } else if (message.equals(".bind") || message.equals(".unbind")) {
            event.setCanceled(true);
            showHelp();
        }
    }
    
    private void handleBindCommand(String args) {
        String[] parts = args.split(" ", 2);
        if (parts.length < 2) {
            sendMessage(TextFormatting.RED + "Usage: .bind <key> <command>");
            sendMessage(TextFormatting.YELLOW + "Example: .bind g spawn");
            return;
        }
        
        String key = parts[0].toLowerCase();
        String command = parts[1];
        
        if (UniversalBindSystem.createBind(key, command)) {
            sendMessage(TextFormatting.GREEN + "Key " + TextFormatting.YELLOW + key + 
                       TextFormatting.GREEN + " bound to: " + TextFormatting.AQUA + "/" + command);
            sendMessage(TextFormatting.GRAY + "Now pressing " + TextFormatting.YELLOW + key + 
                       TextFormatting.GRAY + " will execute this command");
        } else {
            sendMessage(TextFormatting.RED + "Unknown key: " + key);
            sendMessage(TextFormatting.YELLOW + "Available keys: a-z, shift, ctrl, alt, space, f1-f12, 0-9");
        }
    }
    
    private void handleUnbindCommand(String key) {
        if (UniversalBindSystem.removeBind(key.toLowerCase())) {
            sendMessage(TextFormatting.GREEN + "Bind for key " + TextFormatting.YELLOW + key + 
                       TextFormatting.GREEN + " removed");
        } else {
            sendMessage(TextFormatting.RED + "No bind found for key: " + key);
        }
    }
    
    private void handleBindsCommand() {
        String bindsList = UniversalBindSystem.getBindsList();
        sendMessage(TextFormatting.GOLD + "Binds: " + bindsList);
    }
    
    private void showHelp() {
        sendMessage(TextFormatting.GOLD + "=== JustProject Bind Commands ===");
        sendMessage(TextFormatting.YELLOW + ".bind <key> <command>" + TextFormatting.WHITE + " - Create bind");
        sendMessage(TextFormatting.YELLOW + ".unbind <key>" + TextFormatting.WHITE + " - Remove bind");
        sendMessage(TextFormatting.YELLOW + ".binds" + TextFormatting.WHITE + " - List binds");
        sendMessage(TextFormatting.GRAY + "Example: " + TextFormatting.WHITE + ".bind g spawn");
    }
    
    private void sendMessage(String message) {
        net.minecraft.client.Minecraft.getInstance().player.sendMessage(
            new StringTextComponent(message),
            net.minecraft.client.Minecraft.getInstance().player.getUUID()
        );
    }
}