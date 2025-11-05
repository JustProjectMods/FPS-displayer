package com.justproject.fpsdisplayer;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.awt.*;

public class EntityInfoRenderer {
    private final Minecraft mc = Minecraft.getInstance();
    
    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Post event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.ALL) return;
        if (!Config.SHOW_ENTITY_INFO.get()) return;
        
        if (mc.hitResult != null && mc.hitResult.getType() == RayTraceResult.Type.ENTITY) {
            EntityRayTraceResult entityHit = (EntityRayTraceResult) mc.hitResult;
            Entity entity = entityHit.getEntity();
            
            // Show HP only for mobs, not players
            if (entity instanceof LivingEntity && !(entity instanceof PlayerEntity)) {
                renderEntityInfo(event.getMatrixStack(), (LivingEntity) entity);
            }
        }
    }
    
    private void renderEntityInfo(MatrixStack matrixStack, LivingEntity entity) {
        FontRenderer font = mc.font;
        
        // Get real health values
        float health = entity.getHealth();
        float maxHealth = entity.getMaxHealth();
        float absorption = entity.getAbsorptionAmount();
        
        // Format health text
        String healthText;
        if (absorption > 0) {
            healthText = String.format("%.1f / %.1f (+%.1f)", health, maxHealth, absorption);
        } else {
            healthText = String.format("%.1f / %.1f", health, maxHealth);
        }
        
        String nameText = entity.getName().getString();
        
        // Calculate text dimensions
        int healthWidth = font.width(healthText);
        int nameWidth = font.width(nameText);
        int maxWidth = Math.max(healthWidth, nameWidth);
        
        // Calculate position (centered by default, or use config)
        int screenWidth = mc.getWindow().getGuiScaledWidth();
        int screenHeight = mc.getWindow().getGuiScaledHeight();
        
        int x = Config.ENTITY_INFO_X.get() == 0 ? 
            screenWidth / 2 - maxWidth / 2 : 
            (Config.ENTITY_INFO_X.get() > 0 ? Config.ENTITY_INFO_X.get() : screenWidth + Config.ENTITY_INFO_X.get());
        
        int y = Config.ENTITY_INFO_Y.get() == 0 ? 
            screenHeight / 2 + 20 : 
            (Config.ENTITY_INFO_Y.get() > 0 ? Config.ENTITY_INFO_Y.get() : screenHeight + Config.ENTITY_INFO_Y.get());
        
        // Simple background without double borders
        int padding = 4;
        int backgroundWidth = maxWidth + padding * 2;
        int backgroundHeight = 20;
        
        // Simple rectangle background
        RenderOverlay.fill(matrixStack, x - padding, y - padding, x + maxWidth + padding, y + backgroundHeight, 0x80000000);
        
        // Render entity name
        font.draw(matrixStack, new StringTextComponent(nameText), x, y, Color.WHITE.getRGB());
        
        // Render health with color based on percentage
        Color healthColor = getHealthColor(health, maxHealth);
        font.draw(matrixStack, new StringTextComponent(healthText), x, y + 10, healthColor.getRGB());
    }
    
    private Color getHealthColor(float health, float maxHealth) {
        float percent = health / maxHealth;
        
        if (percent >= 0.7f) return Color.GREEN;
        if (percent >= 0.4f) return Color.YELLOW;
        if (percent >= 0.2f) return new Color(255, 165, 0); // Orange
        return Color.RED;
    }
}