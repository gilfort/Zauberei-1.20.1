package com.gilfort.zauberei.item.armor;

import com.gilfort.zauberei.entity.armor.magiccloth.MagicclothArmorModel;
import com.gilfort.zauberei.entity.armor.magiccloth.MagicclothArmorRenderer;
import com.gilfort.zauberei.helpers.PlayerDataHelper;
import com.gilfort.zauberei.registries.ArmorMaterialRegistry;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
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

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        if (entity instanceof ServerPlayer player && !level.isClientSide()) {
            if (PlayerDataHelper.getMajor(player) != null) {
                ArmorEffects.checkAndApplyEffects(player, level);
            }
        }
    }

}
