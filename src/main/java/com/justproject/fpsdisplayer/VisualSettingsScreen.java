package com.justproject.fpsdisplayer;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.glfw.GLFW;

import java.awt.*;

public class VisualSettingsScreen extends Screen {
    private static final int BUTTON_WIDTH = 200;
    private static final int BUTTON_HEIGHT = 20;
    private static final int MARGIN = 5;
    
    private Screen parentScreen;

    public VisualSettingsScreen(Screen parentScreen) {
        super(new StringTextComponent("Visual Settings"));
        this.parentScreen = parentScreen;
    }

    @Override
    protected void init() {
        super.init();
        
        int centerX = this.width / 2;
        int startY = 60;
        
        // Toggle buttons for each display element
        this.addButton(new Button(
            centerX - BUTTON_WIDTH / 2,
            startY,
            BUTTON_WIDTH,
            BUTTON_HEIGHT,
            getToggleText("FPS Display", Config.SHOW_FPS.get()),
            button -> {
                Config.SHOW_FPS.set(!Config.SHOW_FPS.get());
                button.setMessage(getToggleText("FPS Display", Config.SHOW_FPS.get()));
            }
        ));
        
        this.addButton(new Button(
            centerX - BUTTON_WIDTH / 2,
            startY + BUTTON_HEIGHT + MARGIN,
            BUTTON_WIDTH,
            BUTTON_HEIGHT,
            getToggleText("Coordinates", Config.SHOW_COORDINATES.get()),
            button -> {
                Config.SHOW_COORDINATES.set(!Config.SHOW_COORDINATES.get());
                button.setMessage(getToggleText("Coordinates", Config.SHOW_COORDINATES.get()));
            }
        ));
        
        this.addButton(new Button(
            centerX - BUTTON_WIDTH / 2,
            startY + (BUTTON_HEIGHT + MARGIN) * 2,
            BUTTON_WIDTH,
            BUTTON_HEIGHT,
            getToggleText("CPS Display", Config.SHOW_CPS.get()),
            button -> {
                Config.SHOW_CPS.set(!Config.SHOW_CPS.get());
                button.setMessage(getToggleText("CPS Display", Config.SHOW_CPS.get()));
            }
        ));
        
        this.addButton(new Button(
            centerX - BUTTON_WIDTH / 2,
            startY + (BUTTON_HEIGHT + MARGIN) * 3,
            BUTTON_WIDTH,
            BUTTON_HEIGHT,
            getToggleText("Armor Display", Config.SHOW_ARMOR.get()),
            button -> {
                Config.SHOW_ARMOR.set(!Config.SHOW_ARMOR.get());
                button.setMessage(getToggleText("Armor Display", Config.SHOW_ARMOR.get()));
            }
        ));
        
        this.addButton(new Button(
            centerX - BUTTON_WIDTH / 2,
            startY + (BUTTON_HEIGHT + MARGIN) * 4,
            BUTTON_WIDTH,
            BUTTON_HEIGHT,
            getToggleText("Entity Info", Config.SHOW_ENTITY_INFO.get()),
            button -> {
                Config.SHOW_ENTITY_INFO.set(!Config.SHOW_ENTITY_INFO.get());
                button.setMessage(getToggleText("Entity Info", Config.SHOW_ENTITY_INFO.get()));
            }
        ));
        
        // Reset Positions Button
        this.addButton(new Button(
            centerX - BUTTON_WIDTH / 2,
            startY + (BUTTON_HEIGHT + MARGIN) * 6,
            BUTTON_WIDTH,
            BUTTON_HEIGHT,
            new StringTextComponent("Reset Positions"),
            button -> resetPositions()
        ));
        
        // Back Button
        this.addButton(new Button(
            centerX - BUTTON_WIDTH / 2,
            this.height - 30,
            BUTTON_WIDTH,
            BUTTON_HEIGHT,
            new StringTextComponent("Back"),
            button -> this.onClose()
        ));
    }
    
    private IFormattableTextComponent getToggleText(String name, boolean status) {
        String statusText = status ? "ON" : "OFF";
        // Use TextFormatting instead of special characters
        if (status) {
            return new StringTextComponent(name + ": ").append(new StringTextComponent(statusText).withStyle(TextFormatting.GREEN));
        } else {
            return new StringTextComponent(name + ": ").append(new StringTextComponent(statusText).withStyle(TextFormatting.RED));
        }
    }
    
    private void resetPositions() {
        Config.FPS_X.set(10);
        Config.FPS_Y.set(10);
        Config.COORDINATES_X.set(10);
        Config.COORDINATES_Y.set(25);
        Config.CPS_X.set(10);
        Config.CPS_Y.set(40);
        Config.ARMOR_X.set(-40);
        Config.ARMOR_Y.set(-100);
        Config.ENTITY_INFO_X.set(0);
        Config.ENTITY_INFO_Y.set(0);
        Config.SPEC.save();
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        
        // Title
        drawCenteredString(matrixStack, this.font, 
            new StringTextComponent("Visual Settings"), 
            this.width / 2, 20, Color.WHITE.getRGB());
        
        // Description
        drawCenteredString(matrixStack, this.font, 
            new StringTextComponent(TextFormatting.GRAY + "Toggle display elements"), 
            this.width / 2, 40, Color.GRAY.getRGB());
        
        // Config file info
        drawString(matrixStack, this.font, 
            new StringTextComponent("Advanced settings in: config/fpsdisplayer.toml"), 
            10, this.height - 40, Color.YELLOW.getRGB());
        
        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
            this.onClose();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void onClose() {
        Config.SPEC.save();
        Minecraft.getInstance().setScreen(parentScreen);
    }
}