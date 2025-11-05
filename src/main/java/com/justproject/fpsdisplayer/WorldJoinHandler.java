package com.justproject.fpsdisplayer;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class WorldJoinHandler {
    private boolean wasInWorld = false;
    private int joinTimer = 0;
    
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            Minecraft mc = Minecraft.getInstance();
            boolean isInWorld = mc.player != null && mc.level != null;
            
            if (isInWorld && !wasInWorld) {
                joinTimer = 40; // 2 seconds
            }
            
            if (joinTimer > 0) {
                joinTimer--;
                if (joinTimer == 0) {
                    showWelcomeMessage();
                }
            }
            
            wasInWorld = isInWorld;
        }
    }
    
    private void showWelcomeMessage() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null) {
            mc.player.sendMessage(new StringTextComponent(TextFormatting.GOLD + "=== JustProject Mod ==="), mc.player.getUUID());
            mc.player.sendMessage(new StringTextComponent(TextFormatting.GREEN + "FPS Display: " + TextFormatting.WHITE + "FPS, Coordinates, CPS, Armor"), mc.player.getUUID());
            mc.player.sendMessage(new StringTextComponent(TextFormatting.GREEN + "Entity Info: " + TextFormatting.WHITE + "Real HP when looking at mobs"), mc.player.getUUID());
            mc.player.sendMessage(new StringTextComponent(TextFormatting.GREEN + "Bind System: " + TextFormatting.WHITE + "Use .bind to create key binds"), mc.player.getUUID());
            mc.player.sendMessage(new StringTextComponent(TextFormatting.GREEN + "Armor Swap: " + TextFormatting.WHITE + "Right-click with armor to swap"), mc.player.getUUID());
            mc.player.sendMessage(new StringTextComponent(TextFormatting.GREEN + "Multi-Language: " + TextFormatting.WHITE + "Russian & English support"), mc.player.getUUID());
            mc.player.sendMessage(new StringTextComponent(TextFormatting.GREEN + "Customizable: " + TextFormatting.WHITE + "Edit config/fpsdisplayer.toml for positions"), mc.player.getUUID());
            mc.player.sendMessage(new StringTextComponent(TextFormatting.YELLOW + "Try: " + TextFormatting.WHITE + ".bind g spawn"), mc.player.getUUID());
        }
    }
}