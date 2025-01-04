package com.gilfort.zauberei;

import com.gilfort.zauberei.registries.*;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.llamalad7.mixinextras.utils.MixinExtrasLogger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(Zauberei.MODID)
public class Zauberei {

    public static final String MODID = "zauberei";

    public static final Logger LOGGER = LogUtils.getLogger();

    private static final Map<String, JsonObject> DATA = new HashMap<>();

    public Zauberei(IEventBus modEventBus, ModContainer modContainer) {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        NeoForge.EVENT_BUS.register(this);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);

        //Registries
        ArmorMaterialRegistry.register(modEventBus);
        ComponentRegistry.register(modEventBus);
        ItemRegistry.register(modEventBus);
        CreativeTabRegistry.register(modEventBus);
        AttributeRegistry.register(modEventBus);

    }

    private void commonSetup(final FMLCommonSetupEvent event) {

    }


    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
        loadArmorEffects(event.getServer().getResourceManager());
        testJsonFileFound("summoning");
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            // Some client setup code
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }
    }

    public static ResourceLocation id(@NotNull String path) {
        return ResourceLocation.fromNamespaceAndPath(Zauberei.MODID, path);
    }


    public static void loadArmorEffects(ResourceManager resourceManager) {
        try {
            // Durchsuche alle Ressourcen im "majors"-Ordner
            Map<ResourceLocation, Resource> resources = resourceManager.listResources("majors", location -> location.getPath().endsWith(".json"));
            for (Map.Entry<ResourceLocation, Resource> entry : resources.entrySet()) {
                ResourceLocation resourceLocation = entry.getKey();
                Resource resource = entry.getValue();

                // Lade die JSON-Daten
                try (var reader = new InputStreamReader(resource.open())) {
                    JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
                    DATA.put(resourceLocation.toString(), json);
                } catch (Exception e) {
                    Zauberei.LOGGER.error("Failed to load armor effects", e);
                }
            }
        } catch (Exception e) {
            Zauberei.LOGGER.error("Failed to load armor effects", e);
        }
    }

    public static JsonObject getData(String key) {
        return DATA.get(key);
    }

    //Tests
    public static void testJsonFileFound(String major) {
        ResourceLocation resourceLocation = ResourceLocation.fromNamespaceAndPath("zauberei", "majors/" + major.toLowerCase() + ".json");
        if (getData(resourceLocation.toString()) != null) {
            System.out.println("JSON file found for major: " + major);
        } else {
            System.err.println("JSON file NOT found for major: " + major);
        }
    }
}