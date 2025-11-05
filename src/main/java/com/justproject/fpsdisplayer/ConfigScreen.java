package com.justproject.fpsdisplayer;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.glfw.GLFW;

import java.awt.*;

public class ConfigScreen extends Screen {
    private static final int BUTTON_WIDTH = 200;
    private static final int BUTTON_HEIGHT = 20;
    private static final int MARGIN = 10;
    
    private Screen parentScreen;

    public ConfigScreen(Screen parentScreen) {
        super(new StringTextComponent("JustProject Settings"));
        this.parentScreen = parentScreen;
    }

    @Override
    protected void init() {
        super.init();
        
        int centerX = this.width / 2;
        int startY = this.height / 4;
        
        // Bind Settings Button
        this.addButton(new Button(
            centerX - BUTTON_WIDTH / 2,
            startY,
            BUTTON_WIDTH,
            BUTTON_HEIGHT,
            new StringTextComponent("Key Bind Settings"),
            button -> Minecraft.getInstance().setScreen(new BindSettingsScreen(this))
        ));
        
        // Visual Settings Button
        this.addButton(new Button(
            centerX - BUTTON_WIDTH / 2,
            startY + BUTTON_HEIGHT + MARGIN,
            BUTTON_WIDTH,
            BUTTON_HEIGHT,
            new StringTextComponent("Visual Settings"),
            button -> Minecraft.getInstance().setScreen(new VisualSettingsScreen(this))
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

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        
        // Title
        drawCenteredString(matrixStack, this.font, 
            new StringTextComponent("JustProject Settings"), 
            this.width / 2, 20, Color.WHITE.getRGB());
        
        // Description
        drawCenteredString(matrixStack, this.font, 
            new StringTextComponent(TextFormatting.GRAY + "Configure your mod settings"), 
            this.width / 2, 40, Color.GRAY.getRGB());
        
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
        Minecraft.getInstance().setScreen(parentScreen);
    }
}