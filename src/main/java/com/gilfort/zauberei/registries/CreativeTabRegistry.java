package com.gilfort.zauberei.registries;


import com.gilfort.zauberei.Zauberei;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.bus.api.IEventBus;
import net.minecraft.core.registries.BuiltInRegistries;

@EventBusSubscriber(modid = Zauberei.MODID, bus = EventBusSubscriber.Bus.MOD)
public class CreativeTabRegistry {

    private static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Zauberei.MODID);

    public static void register(IEventBus eventBus) {
        TABS.register(eventBus);
    }


    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> ZAUBEREI_TAB = TABS.register("zauberei_tab",
            ()-> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup." + Zauberei.MODID + ".zauberei_tab"))
                    .icon(()-> new ItemStack(ItemRegistry.MAGICCLOTH_HELMET.get()))
                    .withTabsBefore(CreativeModeTabs.INGREDIENTS)
                    .displayItems((enabledFeatures, entries) ->{
                        entries.accept(ItemRegistry.MAGICCLOTH_HELMET.get());
                        entries.accept(ItemRegistry.MAGICCLOTH_CHESTPLATE.get());
                        entries.accept(ItemRegistry.MAGICCLOTH_LEGGINGS.get());
                        entries.accept(ItemRegistry.MAGICCLOTH_BOOTS.get());
                        entries.accept(ItemRegistry.MAGICCOMPONENT.get());
                        entries.accept(ItemRegistry.MAGICCOMPONENT_TWO.get());
                        entries.accept(ItemRegistry.MAGICCLOTH.get());
                    })
                    .build());

    @SubscribeEvent
    public static void fillCreativeTabs(final BuildCreativeModeTabContentsEvent event){
        if (event.getTab() == BuiltInRegistries.CREATIVE_MODE_TAB.get(CreativeModeTabs.FUNCTIONAL_BLOCKS)){

        };
    }

}

