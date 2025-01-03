package com.gilfort.zauberei.item.armor;

import com.gilfort.zauberei.Zauberei;
import com.gilfort.zauberei.registries.ArmorMaterialRegistry;
import com.gilfort.zauberei.helpers.PlayerDataHelper;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import net.neoforged.neoforge.event.tick.*;
import net.neoforged.neoforge.common.*;
import org.apache.commons.lang3.ObjectUtils;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArmorEffects{
    private static final Map<String, Map<ArmorMaterial, Map<Integer, List<MobEffectInstance>>>> MAJOR_EFFECT_MAP = new HashMap<>();

    static {
        // Effekte für Magiccloth basierend auf Major-Strings
        MAJOR_EFFECT_MAP.put("Summoning", Map.of(
                ArmorMaterialRegistry.MAGICCLOTH.get(), Map.of(
                        2, List.of(new MobEffectInstance(MobEffects.JUMP, 200, 1, false, false)),
                        4, List.of(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 200, 2, false, false))
                )
        ));

        MAJOR_EFFECT_MAP.put("Hemomagic", Map.of(
                ArmorMaterialRegistry.MAGICCLOTH.get(), Map.of(
                        2, List.of(new MobEffectInstance(MobEffects.DARKNESS, 200, 0, false, false)),
                        4, List.of(new MobEffectInstance(MobEffects.REGENERATION, 200, 1, false, false))
                )
        ));

//        // Effekte für Magicmetal basierend auf Major-Strings
//        MAJOR_EFFECT_MAP.put("Elemental", Map.of(
//                ArmorMaterialRegistry.MAGICMETAL.get(), Map.of(
//                        2, List.of(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 200, 0, false, false)),
//                        4, List.of(new MobEffectInstance(MobEffects.DIG_SPEED, 200, 2, false, false))
//                )
//        ));
//
//        MAJOR_EFFECT_MAP.put("MagicalCombat", Map.of(
//                ArmorMaterialRegistry.MAGICMETAL.get(), Map.of(
//                        2, List.of(new MobEffectInstance(MobEffects.RESISTANCE, 200, 1, false, false)),
//                        4, List.of(new MobEffectInstance(MobEffects.STRENGTH, 200, 3, false, false))
//                )
//        ));
    }



    public static void checkAndApplyEffects(Entity entity, Level level) {
        if (entity instanceof ServerPlayer player && !level.isClientSide()) {
            // Lade den Major-String des Spielers
            String major = PlayerDataHelper.getMajor(player);

            if (MAJOR_EFFECT_MAP.containsKey(major)) {
                Map<ArmorMaterial, Map<Integer, List<MobEffectInstance>>> materialEffects = MAJOR_EFFECT_MAP.get(major);

                for (Map.Entry<ArmorMaterial, Map<Integer, List<MobEffectInstance>>> entry : materialEffects.entrySet()) {
                    ArmorMaterial material = entry.getKey();
                    Map<Integer, List<MobEffectInstance>> effectsByPieces = entry.getValue();

                    int armorPieces = countArmorPieces(player, material);
                    applyEffects(player, effectsByPieces, armorPieces);
                }
            }
        }
    }

    private static int countArmorPieces(Player player, ArmorMaterial material) {
        int count = 0;
        for (ItemStack armorStack : player.getArmorSlots()) {
            if (armorStack.getItem() instanceof ArmorItem armorItem &&
                    armorItem.getMaterial().value() == material) {
                count++;
            }
        }
        return count;
    }

    private static void applyEffects(Player player, Map<Integer, List<MobEffectInstance>> effectsByPieces, int armorPieces) {
        if (effectsByPieces.containsKey(armorPieces)) {
            List<MobEffectInstance> effects = effectsByPieces.get(armorPieces);
            for (MobEffectInstance effect : effects) {
                player.addEffect(new MobEffectInstance(effect.getEffect(),
                        effect.getDuration(), effect.getAmplifier(), effect.isAmbient(), effect.isVisible()));
            }
        }
    }
}