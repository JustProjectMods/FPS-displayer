package com.justproject.fpsdisplayer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.glfw.GLFW;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class UniversalBindSystem {
    private static final Map<String, KeyBinding> keyBinds = new HashMap<>();
    private static final Map<String, String> bindCommands = new HashMap<>();
    private static final File BINDS_FILE = new File("config/justproject_binds.txt");
    
    // Load binds on startup
    static {
        loadBindsFromFile();
    }
    
    // Create bind
    public static boolean createBind(String keyName, String command) {
        Integer keyCode = getKeyCode(keyName);
        if (keyCode == null) {
            return false;
        }
        
        // Remove old bind if exists
        if (keyBinds.containsKey(keyName)) {
            removeBind(keyName);
        }
        
        // Create KeyBinding
        KeyBinding keyBind = new KeyBinding(
            "justproject.bind." + keyName,
            keyCode,
            "JustProject Binds"
        );
        
        // Register
        ClientRegistry.registerKeyBinding(keyBind);
        keyBinds.put(keyName, keyBind);
        bindCommands.put(keyName, command);
        
        // Save to file
        saveBindsToFile();
        
        return true;
    }
    
    // Remove bind
    public static boolean removeBind(String keyName) {
        if (keyBinds.containsKey(keyName)) {
            keyBinds.remove(keyName);
            bindCommands.remove(keyName);
            saveBindsToFile();
            return true;
        }
        return false;
    }
    
    // Get binds list as string
    public static String getBindsList() {
        if (bindCommands.isEmpty()) {
            return "No active binds";
        }
        
        StringBuilder sb = new StringBuilder("Active binds:\n");
        for (Map.Entry<String, String> entry : bindCommands.entrySet()) {
            sb.append("  ").append(entry.getKey()).append(" -> /").append(entry.getValue()).append("\n");
        }
        return sb.toString();
    }
    
    // Get bind commands map (for GUI)
    public static Map<String, String> getBindCommands() {
        return new HashMap<>(bindCommands);
    }
    
    // Handle key presses
    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        Minecraft mc = Minecraft.getInstance();
        
        if (mc.screen != null || mc.player == null) {
            return;
        }
        
        if (event.getAction() == GLFW.GLFW_PRESS) {
            for (Map.Entry<String, KeyBinding> entry : keyBinds.entrySet()) {
                if (entry.getValue().isDown()) {
                    executeCommand(bindCommands.get(entry.getKey()));
                    break;
                }
            }
        }
    }
    
    // Execute command (works on servers!)
    private void executeCommand(String command) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null && mc.screen == null) {
            mc.player.chat("/" + command);
        }
    }
    
    // Save binds to file
    private static void saveBindsToFile() {
        try {
            BINDS_FILE.getParentFile().mkdirs();
            BufferedWriter writer = new BufferedWriter(new FileWriter(BINDS_FILE));
            for (Map.Entry<String, String> entry : bindCommands.entrySet()) {
                writer.write(entry.getKey() + "=" + entry.getValue());
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // Load binds from file
    private static void loadBindsFromFile() {
        if (!BINDS_FILE.exists()) return;
        
        try {
            BufferedReader reader = new BufferedReader(new FileReader(BINDS_FILE));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("=", 2);
                if (parts.length == 2) {
                    createBind(parts[0], parts[1]);
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // Key name to code mapping
    private static Integer getKeyCode(String keyName) {
        switch (keyName.toLowerCase()) {
            // Letters
            case "a": return GLFW.GLFW_KEY_A;
            case "b": return GLFW.GLFW_KEY_B;
            case "c": return GLFW.GLFW_KEY_C;
            case "d": return GLFW.GLFW_KEY_D;
            case "e": return GLFW.GLFW_KEY_E;
            case "f": return GLFW.GLFW_KEY_F;
            case "g": return GLFW.GLFW_KEY_G;
            case "h": return GLFW.GLFW_KEY_H;
            case "i": return GLFW.GLFW_KEY_I;
            case "j": return GLFW.GLFW_KEY_J;
            case "k": return GLFW.GLFW_KEY_K;
            case "l": return GLFW.GLFW_KEY_L;
            case "m": return GLFW.GLFW_KEY_M;
            case "n": return GLFW.GLFW_KEY_N;
            case "o": return GLFW.GLFW_KEY_O;
            case "p": return GLFW.GLFW_KEY_P;
            case "q": return GLFW.GLFW_KEY_Q;
            case "r": return GLFW.GLFW_KEY_R;
            case "s": return GLFW.GLFW_KEY_S;
            case "t": return GLFW.GLFW_KEY_T;
            case "u": return GLFW.GLFW_KEY_U;
            case "v": return GLFW.GLFW_KEY_V;
            case "w": return GLFW.GLFW_KEY_W;
            case "x": return GLFW.GLFW_KEY_X;
            case "y": return GLFW.GLFW_KEY_Y;
            case "z": return GLFW.GLFW_KEY_Z;
            
            // Modifiers
            case "shift": return GLFW.GLFW_KEY_LEFT_SHIFT;
            case "ctrl": return GLFW.GLFW_KEY_LEFT_CONTROL;
            case "alt": return GLFW.GLFW_KEY_LEFT_ALT;
            case "space": return GLFW.GLFW_KEY_SPACE;
            case "tab": return GLFW.GLFW_KEY_TAB;
            case "capslock": return GLFW.GLFW_KEY_CAPS_LOCK;
            
            // Function keys
            case "f1": return GLFW.GLFW_KEY_F1;
            case "f2": return GLFW.GLFW_KEY_F2;
            case "f3": return GLFW.GLFW_KEY_F3;
            case "f4": return GLFW.GLFW_KEY_F4;
            case "f5": return GLFW.GLFW_KEY_F5;
            case "f6": return GLFW.GLFW_KEY_F6;
            case "f7": return GLFW.GLFW_KEY_F7;
            case "f8": return GLFW.GLFW_KEY_F8;
            case "f9": return GLFW.GLFW_KEY_F9;
            case "f10": return GLFW.GLFW_KEY_F10;
            case "f11": return GLFW.GLFW_KEY_F11;
            case "f12": return GLFW.GLFW_KEY_F12;
            
            // Numbers
            case "1": return GLFW.GLFW_KEY_1;
            case "2": return GLFW.GLFW_KEY_2;
            case "3": return GLFW.GLFW_KEY_3;
            case "4": return GLFW.GLFW_KEY_F4;
            case "5": return GLFW.GLFW_KEY_5;
            case "6": return GLFW.GLFW_KEY_6;
            case "7": return GLFW.GLFW_KEY_7;
            case "8": return GLFW.GLFW_KEY_8;
            case "9": return GLFW.GLFW_KEY_9;
            case "0": return GLFW.GLFW_KEY_0;
            
            default: return null;
        }
    }
}