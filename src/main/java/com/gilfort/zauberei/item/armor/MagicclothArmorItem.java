package com.gilfort.zauberei.item.armor;

import com.gilfort.zauberei.entity.armor.magiccloth.MagicclothArmorModel;
import com.gilfort.zauberei.entity.armor.magiccloth.MagicclothArmorRenderer;
import com.gilfort.zauberei.registries.ArmorMaterialRegistry;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import software.bernie.geckolib.renderer.GeoArmorRenderer;


public class MagicclothArmorItem extends ExtendedArmorItem {

    public MagicclothArmorItem(Type slot, Properties settings){
        super(ArmorMaterialRegistry.MAGICCLOTH, slot, settings);
    }


    @OnlyIn(Dist.CLIENT)
    public GeoArmorRenderer<?> supplyRenderer() {
        return new MagicclothArmorRenderer(new MagicclothArmorModel());
    }

}
