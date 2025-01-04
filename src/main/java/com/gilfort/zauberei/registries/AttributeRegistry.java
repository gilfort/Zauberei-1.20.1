package com.gilfort.zauberei.registries;

import com.gilfort.zauberei.Zauberei;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.world.entity.ai.attributes.Attribute;

public class AttributeRegistry {

    private static final DeferredRegister<Attribute> ATTRIBUTE = DeferredRegister.create(Registries.ATTRIBUTE, Zauberei.MODID);

    public static void register(IEventBus eventBus) {
        ATTRIBUTE.register(eventBus);
    }

    public static final DeferredHolder<Attribute, Attribute> MAGIC = ATTRIBUTE.register("magic",
            () -> new RangedAttribute("attribute.zauberei.magic", 0.0, 0.0, 1024.0).setSyncable(true));
}
