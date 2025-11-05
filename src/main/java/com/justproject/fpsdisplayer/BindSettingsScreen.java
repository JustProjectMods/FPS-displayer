package com.justproject.fpsdisplayer;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.glfw.GLFW;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BindSettingsScreen extends Screen {
    private static final int BUTTON_WIDTH = 80;
    private static final int BUTTON_HEIGHT = 20;
    private static final int FIELD_WIDTH = 120;
    private static final int MARGIN = 5;
    
    private Screen parentScreen;
    private List<BindEntry> bindEntries = new ArrayList<>();
    private Button addBindButton;
    private int scrollOffset = 0;
    private static final int MAX_VISIBLE_ENTRIES = 6;
    private BindEntry listeningEntry = null;

    public BindSettingsScreen(Screen parentScreen) {
        super(new StringTextComponent("Key Bind Settings"));
        this.parentScreen = parentScreen;
        loadBinds();
    }

    @Override
    protected void init() {
        super.init();
        this.bindEntries.clear();
        loadBinds();
        createBindEntries();
    }
    
    private void loadBinds() {
        Map<String, String> binds = UniversalBindSystem.getBindCommands();
        for (Map.Entry<String, String> entry : binds.entrySet()) {
            bindEntries.add(new BindEntry(entry.getKey(), entry.getValue()));
        }
    }
    
    private void createBindEntries() {
        this.buttons.clear();
        this.children.clear();
        
        int centerX = this.width / 2;
        int startY = 60;
        int entryHeight = 25;
        
        // Add Bind Button
        this.addBindButton = this.addButton(new Button(
            centerX - BUTTON_WIDTH / 2,
            this.height - 50,
            BUTTON_WIDTH,
            BUTTON_HEIGHT,
            new StringTextComponent("+ Add Bind"),
            button -> addNewBind()
        ));
        
        // Back Button
        this.addButton(new Button(
            centerX - BUTTON_WIDTH / 2,
            this.height - 25,
            BUTTON_WIDTH,
            BUTTON_HEIGHT,
            new StringTextComponent("Back"),
            button -> this.onClose()
        ));
        
        // Add visible bind entries
        for (int i = scrollOffset; i < Math.min(bindEntries.size(), scrollOffset + MAX_VISIBLE_ENTRIES); i++) {
            BindEntry entry = bindEntries.get(i);
            int yPos = startY + (i - scrollOffset) * entryHeight;
            
            // Key binding button [>G<]
            Button keyButton = new Button(
                centerX - 150,
                yPos,
                BUTTON_WIDTH,
                BUTTON_HEIGHT,
                new StringTextComponent("> " + entry.key + " <"),
                button -> startListening(entry)
            );
            this.addButton(keyButton);
            entry.keyButton = keyButton;
            
            // Command input field
            TextFieldWidget commandField = new TextFieldWidget(
                this.font,
                centerX - 60,
                yPos,
                FIELD_WIDTH,
                BUTTON_HEIGHT,
                new StringTextComponent("Command")
            );
            commandField.setValue(entry.command);
            commandField.setResponder(text -> {
                entry.command = text;
                UniversalBindSystem.createBind(entry.key, entry.command);
            });
            this.addWidget(commandField);
            entry.commandField = commandField;
            
            // Delete button
            this.addButton(new Button(
                centerX + 70,
                yPos,
                60,
                BUTTON_HEIGHT,
                new StringTextComponent("Delete"),
                button -> deleteBind(entry)
            ));
        }
        
        // Scroll buttons
        if (bindEntries.size() > MAX_VISIBLE_ENTRIES) {
            this.addButton(new Button(
                this.width - 30,
                50,
                20,
                BUTTON_HEIGHT,
                new StringTextComponent("↑"),
                button -> scrollUp()
            ));
            
            this.addButton(new Button(
                this.width - 30,
                this.height - 80,
                20,
                BUTTON_HEIGHT,
                new StringTextComponent("↓"),
                button -> scrollDown()
            ));
        }
    }
    
    private void startListening(BindEntry entry) {
        listeningEntry = entry;
        entry.keyButton.setMessage(new StringTextComponent("> Press any key <"));
    }
    
    private void stopListening() {
        if (listeningEntry != null) {
            listeningEntry.keyButton.setMessage(new StringTextComponent("> " + listeningEntry.key + " <"));
            listeningEntry = null;
        }
    }
    
    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (listeningEntry != null) {
            if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
                stopListening();
                return true;
            }
            
            String keyName = getKeyName(keyCode);
            if (keyName != null) {
                if (!listeningEntry.key.equals(keyName)) {
                    UniversalBindSystem.removeBind(listeningEntry.key);
                }
                
                listeningEntry.key = keyName;
                UniversalBindSystem.createBind(keyName, listeningEntry.command);
                stopListening();
                createBindEntries();
                return true;
            }
        }
        
        if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
            this.onClose();
            return true;
        }
        
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
    
    private String getKeyName(int keyCode) {
        if (keyCode >= GLFW.GLFW_KEY_A && keyCode <= GLFW.GLFW_KEY_Z) {
            return String.valueOf((char) ('a' + (keyCode - GLFW.GLFW_KEY_A)));
        }
        
        if (keyCode >= GLFW.GLFW_KEY_0 && keyCode <= GLFW.GLFW_KEY_9) {
            return String.valueOf((char) ('0' + (keyCode - GLFW.GLFW_KEY_0)));
        }
        
        switch (keyCode) {
            case GLFW.GLFW_KEY_SPACE: return "space";
            case GLFW.GLFW_KEY_LEFT_SHIFT: return "shift";
            case GLFW.GLFW_KEY_LEFT_CONTROL: return "ctrl";
            case GLFW.GLFW_KEY_LEFT_ALT: return "alt";
            case GLFW.GLFW_KEY_TAB: return "tab";
            case GLFW.GLFW_KEY_CAPS_LOCK: return "capslock";
            case GLFW.GLFW_KEY_F1: return "f1";
            case GLFW.GLFW_KEY_F2: return "f2";
            case GLFW.GLFW_KEY_F3: return "f3";
            case GLFW.GLFW_KEY_F4: return "f4";
            case GLFW.GLFW_KEY_F5: return "f5";
            case GLFW.GLFW_KEY_F6: return "f6";
            case GLFW.GLFW_KEY_F7: return "f7";
            case GLFW.GLFW_KEY_F8: return "f8";
            case GLFW.GLFW_KEY_F9: return "f9";
            case GLFW.GLFW_KEY_F10: return "f10";
            case GLFW.GLFW_KEY_F11: return "f11";
            case GLFW.GLFW_KEY_F12: return "f12";
            default: return null;
        }
    }
    
    private void addNewBind() {
        BindEntry newEntry = new BindEntry("f", "say Hello");
        bindEntries.add(newEntry);
        UniversalBindSystem.createBind(newEntry.key, newEntry.command);
        createBindEntries();
    }
    
    private void deleteBind(BindEntry entry) {
        UniversalBindSystem.removeBind(entry.key);
        bindEntries.remove(entry);
        createBindEntries();
    }
    
    private void scrollUp() {
        if (scrollOffset > 0) {
            scrollOffset--;
            createBindEntries();
        }
    }
    
    private void scrollDown() {
        if (scrollOffset < bindEntries.size() - MAX_VISIBLE_ENTRIES) {
            scrollOffset++;
            createBindEntries();
        }
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        
        // Title
        drawCenteredString(matrixStack, this.font, 
            new StringTextComponent("Key Bind Settings"), 
            this.width / 2, 20, Color.WHITE.getRGB());
        
        // Instructions
        drawString(matrixStack, this.font, 
            new StringTextComponent("How to use binds:"), 
            10, this.height - 120, Color.YELLOW.getRGB());
        drawString(matrixStack, this.font, 
            new StringTextComponent("1. Click Key button and press any key"), 
            10, this.height - 105, Color.GRAY.getRGB());
        drawString(matrixStack, this.font, 
            new StringTextComponent("2. Type command in the field (without /)"), 
            10, this.height - 90, Color.GRAY.getRGB());
        drawString(matrixStack, this.font, 
            new StringTextComponent("3. Press ESC to close menu"), 
            10, this.height - 75, Color.GRAY.getRGB());
        
        // Listening indicator
        if (listeningEntry != null) {
            drawCenteredString(matrixStack, this.font, 
                new StringTextComponent("Press any key... (ESC to cancel)"), 
                this.width / 2, 40, Color.GREEN.getRGB());
        }
        
        // Column headers
        int centerX = this.width / 2;
        drawString(matrixStack, this.font, 
            new StringTextComponent("Key"), 
            centerX - 150, 45, Color.WHITE.getRGB());
        drawString(matrixStack, this.font, 
            new StringTextComponent("Command"), 
            centerX - 60, 45, Color.WHITE.getRGB());
        drawString(matrixStack, this.font, 
            new StringTextComponent("Action"), 
            centerX + 70, 45, Color.WHITE.getRGB());
        
        // Bind count
        drawString(matrixStack, this.font, 
            new StringTextComponent(TextFormatting.GRAY + "Binds: " + bindEntries.size()), 
            this.width - 80, 25, Color.GRAY.getRGB());
        
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        
        // Render text fields
        this.children.stream()
            .filter(widget -> widget instanceof TextFieldWidget)
            .forEach(widget -> ((TextFieldWidget) widget).render(matrixStack, mouseX, mouseY, partialTicks));
    }

    @Override
    public void onClose() {
        stopListening();
        Minecraft.getInstance().setScreen(parentScreen);
    }
    
    private static class BindEntry {
        String key;
        String command;
        Button keyButton;
        TextFieldWidget commandField;
        
        BindEntry(String key, String command) {
            this.key = key;
            this.command = command;
        }
    }
}