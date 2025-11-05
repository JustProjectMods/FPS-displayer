package com.justproject.fpsdisplayer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.awt.*;

public class RenderOverlay {
    private final Minecraft mc = Minecraft.getInstance();
    private long lastUpdate = 0;
    private int fps = 0;
    private int frameCount = 0;
    
    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Post event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.ALL) return;
        
        MatrixStack matrixStack = event.getMatrixStack();
        FontRenderer font = mc.font;
        
        updateFPS();
        
        if (Config.SHOW_FPS.get()) {
            renderFPSDisplay(matrixStack, font);
        }
        if (Config.SHOW_COORDINATES.get()) {
            renderCoordinatesDisplay(matrixStack, font);
        }
        if (Config.SHOW_CPS.get()) {
            renderCPSDisplay(matrixStack, font);
        }
        if (Config.SHOW_ARMOR.get()) {
            renderArmorDisplay(matrixStack, font);
        }
    }
    
    private void updateFPS() {
        frameCount++;
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastUpdate >= 1000) {
            fps = frameCount;
            frameCount = 0;
            lastUpdate = currentTime;
        }
    }
    
    private void renderFPSDisplay(MatrixStack matrixStack, FontRenderer font) {
        int x = Config.FPS_X.get();
        int y = Config.FPS_Y.get();
        String fpsText = "FPS: " + fps;
        
        renderTextWithBackground(matrixStack, font, fpsText, x, y, getSmoothFPSColor(fps));
    }
    
    private void renderCoordinatesDisplay(MatrixStack matrixStack, FontRenderer font) {
        PlayerEntity player = mc.player;
        if (player == null) return;
        
        double posX = player.getX();
        double posY = player.getY();
        double posZ = player.getZ();
        
        String coordText = String.format("XYZ: %.1f, %.1f, %.1f", posX, posY, posZ);
        int x = Config.COORDINATES_X.get();
        int y = Config.COORDINATES_Y.get();
        
        renderTextWithBackground(matrixStack, font, coordText, x, y, Color.WHITE);
    }
    
    private void renderCPSDisplay(MatrixStack matrixStack, FontRenderer font) {
        int leftCPS = ClientEventHandler.getLeftCPS();
        int rightCPS = ClientEventHandler.getRightCPS();
        
        String cpsText = String.format("CPS: %d | %d", leftCPS, rightCPS);
        int x = Config.CPS_X.get();
        int y = Config.CPS_Y.get();
        
        renderTextWithBackground(matrixStack, font, cpsText, x, y, Color.WHITE);
    }
    
    private void renderArmorDisplay(MatrixStack matrixStack, FontRenderer font) {
        PlayerEntity player = mc.player;
        if (player == null) return;
        
        int screenWidth = mc.getWindow().getGuiScaledWidth();
        int screenHeight = mc.getWindow().getGuiScaledHeight();
        
        // Calculate position with offsets
        int baseX = Config.ARMOR_X.get() >= 0 ? Config.ARMOR_X.get() : screenWidth + Config.ARMOR_X.get();
        int baseY = Config.ARMOR_Y.get() >= 0 ? Config.ARMOR_Y.get() : screenHeight + Config.ARMOR_Y.get();
        
        ItemStack helmet = player.getItemBySlot(EquipmentSlotType.HEAD);
        ItemStack chestplate = player.getItemBySlot(EquipmentSlotType.CHEST);
        ItemStack leggings = player.getItemBySlot(EquipmentSlotType.LEGS);
        ItemStack boots = player.getItemBySlot(EquipmentSlotType.FEET);
        
        // Vertical layout with proper spacing
        renderArmorPiece(matrixStack, font, helmet, baseX, baseY);
        renderArmorPiece(matrixStack, font, chestplate, baseX, baseY + 22);
        renderArmorPiece(matrixStack, font, leggings, baseX, baseY + 44);
        renderArmorPiece(matrixStack, font, boots, baseX, baseY + 66);
    }
    
    private void renderArmorPiece(MatrixStack matrixStack, FontRenderer font, ItemStack armor, int x, int y) {
        if (!armor.isEmpty() && armor.getItem() instanceof ArmorItem) {
            // Render armor icon
            mc.getItemRenderer().renderAndDecorateItem(armor, x, y);
            
            // Calculate durability percentage
            int durability = (int) ((1.0f - (float) armor.getDamageValue() / armor.getMaxDamage()) * 100);
            String durabilityText = durability + "%";
            
            // Smooth durability color
            Color durabilityColor = getSmoothDurabilityColor(durability);
            
            // Render durability text below icon - SIMPLE BACKGROUND
            int textWidth = font.width(durabilityText);
            int padding = 2;
            fill(matrixStack, x - padding, y + 15, x + textWidth + padding, y + 27, 0x80000000);
            font.draw(matrixStack, new StringTextComponent(durabilityText), x, y + 16, durabilityColor.getRGB());
        }
    }
    
    public static void renderTextWithBackground(MatrixStack matrixStack, FontRenderer font, String text, int x, int y, Color color) {
        int textWidth = font.width(text);
        int padding = 3;
        
        // Simple rectangle background
        fill(matrixStack, x - padding, y - 1, x + textWidth + padding, y + 11, 0x80000000);
        
        // Render text
        font.draw(matrixStack, new StringTextComponent(text), x, y, color.getRGB());
    }
    
    private Color getSmoothFPSColor(int fps) {
        if (fps >= 60) return Color.GREEN;
        if (fps <= 20) return Color.RED;
        
        float ratio = (fps - 20) / 40.0f;
        if (ratio > 0.5f) {
            float greenRatio = (ratio - 0.5f) * 2;
            return new Color(1.0f - greenRatio, 1.0f, 0.0f);
        } else {
            float redRatio = ratio * 2;
            return new Color(1.0f, redRatio, 0.0f);
        }
    }
    
    private Color getSmoothDurabilityColor(int durability) {
        if (durability >= 70) return Color.GREEN;
        if (durability <= 20) return Color.RED;
        
        float ratio = (durability - 20) / 50.0f;
        if (ratio > 0.5f) {
            float greenRatio = (ratio - 0.5f) * 2;
            return new Color(1.0f - greenRatio, 1.0f, 0.0f);
        } else {
            float redRatio = ratio * 2;
            return new Color(1.0f, redRatio, 0.0f);
        }
    }
    
    public static void fill(MatrixStack matrixStack, int x1, int y1, int x2, int y2, int color) {
        RenderSystem.enableBlend();
        RenderSystem.disableTexture();
        RenderSystem.defaultBlendFunc();
        
        float alpha = (float)(color >> 24 & 255) / 255.0F;
        float red = (float)(color >> 16 & 255) / 255.0F;
        float green = (float)(color >> 8 & 255) / 255.0F;
        float blue = (float)(color & 255) / 255.0F;
        
        RenderSystem.color4f(red, green, blue, alpha);
        
        net.minecraft.client.gui.AbstractGui.fill(matrixStack, x1, y1, x2, y2, color);
        
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
    }
}