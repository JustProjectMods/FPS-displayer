package com.justproject.fpsdisplayer;

import net.minecraft.util.text.TranslationTextComponent;

public class LanguageManager {
    
    public static String getString(String key) {
        return new TranslationTextComponent(key).getString();
    }
    
    public static String getString(String key, Object... args) {
        return new TranslationTextComponent(key, args).getString();
    }
    
    public static TranslationTextComponent getText(String key) {
        return new TranslationTextComponent(key);
    }
    
    public static TranslationTextComponent getText(String key, Object... args) {
        return new TranslationTextComponent(key, args);
    }
}