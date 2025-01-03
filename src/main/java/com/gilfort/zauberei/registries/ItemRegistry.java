package com.gilfort.zauberei.registries;


import com.gilfort.zauberei.Zauberei;
import com.gilfort.zauberei.item.armor.MagicclothArmorItem;
import com.gilfort.zauberei.util.ItemPropertiesHelper;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.bus.api.IEventBus;

public class ItemRegistry {

    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, Zauberei.MODID);

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

    /**
     * Basic Items
     */

    public static final DeferredHolder<Item, Item> MAGICCLOTH = ITEMS.register("magiccloth",
            ()-> new Item(ItemPropertiesHelper.material()
                    .rarity(Rarity.UNCOMMON)));
    public static final DeferredHolder<Item, Item> MAGICCOMPONENT = ITEMS.register("magiccomponent",
            ()-> new Item(ItemPropertiesHelper.material()
                    .rarity(Rarity.UNCOMMON)));
    public static final DeferredHolder<Item, Item> MAGICCOMPONENT_TWO = ITEMS.register("magiccomponent_two",
            ()-> new Item(ItemPropertiesHelper.material()
                    .rarity(Rarity.UNCOMMON)));

    /**
     * Armor
     */

    //MAGICCLOTH
    public static final DeferredHolder<Item, Item> MAGICCLOTH_HELMET = ITEMS.register("magiccloth_helmet",
            ()-> new MagicclothArmorItem(ArmorItem.Type.HELMET, ItemPropertiesHelper.equipment(1)
                    .durability(ArmorItem.Type.HELMET.getDurability(26))));
    public static final DeferredHolder<Item, Item> MAGICCLOTH_CHESTPLATE = ITEMS.register("magiccloth_chestplate",
            ()-> new MagicclothArmorItem(ArmorItem.Type.CHESTPLATE, ItemPropertiesHelper.equipment(1)
                    .durability(ArmorItem.Type.CHESTPLATE.getDurability(26))));
    public static final DeferredHolder<Item, Item> MAGICCLOTH_LEGGINGS = ITEMS.register("magiccloth_leggings",
            ()-> new MagicclothArmorItem(ArmorItem.Type.LEGGINGS, ItemPropertiesHelper.equipment(1)
                    .durability(ArmorItem.Type.LEGGINGS.getDurability(26))));
    public static final DeferredHolder<Item, Item> MAGICCLOTH_BOOTS = ITEMS.register("magiccloth_boots",
            ()-> new MagicclothArmorItem(ArmorItem.Type.BOOTS, ItemPropertiesHelper.equipment(1)
                    .durability(ArmorItem.Type.BOOTS.getDurability(26))));


}
