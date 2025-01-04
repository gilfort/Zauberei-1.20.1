package com.gilfort.zauberei.attributes;

import com.gilfort.zauberei.Zauberei;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;

public class ModAttributes {
    public static final Attribute MAGIC_DAMAGE = new RangedAttribute("attribute."+ Zauberei.MODID+".magic_damage", 0.0D, 0.0D, 100)
            .setSyncable(true);
}
