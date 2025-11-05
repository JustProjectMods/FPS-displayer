package com.justproject.fpsdisplayer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.OptionsScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("fpsdisplayer")
public class FPSDisplayer {
    public static final String MODID = "fpsdisplayer";
    public static final Logger LOGGER = LogManager.getLogger();

    public FPSDisplayer() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
        MinecraftForge.EVENT_BUS.register(this);
        
        // Register config
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.SPEC, "fpsdisplayer.toml");
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(new ClientEventHandler());
        MinecraftForge.EVENT_BUS.register(new RenderOverlay());
        MinecraftForge.EVENT_BUS.register(new EntityInfoRenderer());
        MinecraftForge.EVENT_BUS.register(new UniversalBindSystem());
        MinecraftForge.EVENT_BUS.register(new ChatBindCommand());
        MinecraftForge.EVENT_BUS.register(new WorldJoinHandler());
        // ArmorSwapHelper удален - не регистрируем
        
        // Add mod button to options screen
        MinecraftForge.EVENT_BUS.addListener(this::onOptionsScreenInit);
        
        // Initialize network
        NetworkHandler.register();
    }
    
    private void onOptionsScreenInit(GuiScreenEvent.InitGuiEvent.Post event) {
        if (event.getGui() instanceof OptionsScreen) {
            OptionsScreen screen = (OptionsScreen) event.getGui();
            int buttonWidth = 150;
            int buttonHeight = 20;
            int x = screen.width / 2 - buttonWidth / 2;
            int y = screen.height / 6 + 144;
            
            event.addWidget(new Button(
                x, y, buttonWidth, buttonHeight,
                new StringTextComponent("JustProject Settings"),
                button -> Minecraft.getInstance().setScreen(new ConfigScreen(screen))
            ));
        }
    }
}