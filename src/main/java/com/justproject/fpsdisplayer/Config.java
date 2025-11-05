package com.justproject.fpsdisplayer;

import net.minecraftforge.common.ForgeConfigSpec;

public class Config {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;
    
    public static final ForgeConfigSpec.ConfigValue<Boolean> SHOW_FPS;
    public static final ForgeConfigSpec.ConfigValue<Boolean> SHOW_ARMOR;
    public static final ForgeConfigSpec.ConfigValue<Boolean> SHOW_CPS;
    public static final ForgeConfigSpec.ConfigValue<Boolean> SHOW_EFFECTS;
    public static final ForgeConfigSpec.ConfigValue<Boolean> SHOW_COORDINATES;
    public static final ForgeConfigSpec.ConfigValue<Boolean> SHOW_ENTITY_INFO;

    
    // Position settings
    public static final ForgeConfigSpec.ConfigValue<Integer> FPS_X;
    public static final ForgeConfigSpec.ConfigValue<Integer> FPS_Y;
    public static final ForgeConfigSpec.ConfigValue<Integer> COORDINATES_X;
    public static final ForgeConfigSpec.ConfigValue<Integer> COORDINATES_Y;
    public static final ForgeConfigSpec.ConfigValue<Integer> CPS_X;
    public static final ForgeConfigSpec.ConfigValue<Integer> CPS_Y;
    public static final ForgeConfigSpec.ConfigValue<Integer> ARMOR_X;
    public static final ForgeConfigSpec.ConfigValue<Integer> ARMOR_Y;
    public static final ForgeConfigSpec.ConfigValue<Integer> ENTITY_INFO_X;
    public static final ForgeConfigSpec.ConfigValue<Integer> ENTITY_INFO_Y;
    
    static {
        BUILDER.push("FPS Displayer Config");
        
        SHOW_FPS = BUILDER.define("show_fps", true);
        SHOW_ARMOR = BUILDER.define("show_armor", true);
        SHOW_CPS = BUILDER.define("show_cps", true);
        SHOW_EFFECTS = BUILDER.define("show_effects", true);
        SHOW_COORDINATES = BUILDER.define("show_coordinates", true);
        SHOW_ENTITY_INFO = BUILDER.define("show_entity_info", true);

        
        BUILDER.comment("Position settings (in pixels from top-left corner)");
        FPS_X = BUILDER.defineInRange("fps_x", 10, 0, 1000);
        FPS_Y = BUILDER.defineInRange("fps_y", 10, 0, 1000);
        COORDINATES_X = BUILDER.defineInRange("coordinates_x", 10, 0, 1000);
        COORDINATES_Y = BUILDER.defineInRange("coordinates_y", 25, 0, 1000);
        CPS_X = BUILDER.defineInRange("cps_x", 10, 0, 1000);
        CPS_Y = BUILDER.defineInRange("cps_y", 40, 0, 1000);
        ARMOR_X = BUILDER.defineInRange("armor_x", -40, -1000, 1000);
        ARMOR_Y = BUILDER.defineInRange("armor_y", -100, -1000, 1000);
        ENTITY_INFO_X = BUILDER.defineInRange("entity_info_x", 0, -1000, 1000);
        ENTITY_INFO_Y = BUILDER.defineInRange("entity_info_y", 0, -1000, 1000);
        
        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}