package com.gilfort.zauberei.item.armor;

import com.gilfort.zauberei.Zauberei;
import com.gilfort.zauberei.helpers.PlayerDataHelper;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import com.google.gson.JsonObject;

public class ArmorEffects {

    public static void checkAndApplyEffects(Entity entity, Level level) {
        if (entity instanceof ServerPlayer player && !level.isClientSide()) {
            String major = PlayerDataHelper.getMajor(player);
            int year = PlayerDataHelper.getYear(player);

            if (major == null || year <= 0) return;

            // Lade die JSON-Daten für den Major
            ResourceLocation resourceLocation = ResourceLocation.fromNamespaceAndPath(Zauberei.MODID, "majors/" + major.toLowerCase() + ".json");
            JsonObject data = Zauberei.getData(resourceLocation.toString());

            if (data == null) return;

            applyEffectsFromJson(player, data, year, major);
            applyAttributesFromJson(player, data, year, major);
        }
    }

    private static void applyEffectsFromJson(Player player, JsonObject data, int year, String major) {
        if (!data.has("majors")) return;

        JsonObject majors = data.getAsJsonObject("majors");
        if (!majors.has(major)) return;

        JsonObject majorData = majors.getAsJsonObject(major);
        if (!majorData.has("effects")) return;

        JsonObject effects = majorData.getAsJsonObject("effects");
        for (ItemStack armorStack : player.getArmorSlots()) {
            if (armorStack.getItem() instanceof ArmorItem armorItem) {
                ArmorMaterial material = armorItem.getMaterial().value();
                String materialName = material.toString().toLowerCase();

                if (effects.has(materialName)) {
                    JsonObject materialEffects = effects.getAsJsonObject(materialName);
                    int armorPieces = countArmorPieces(player, material);

                    if (materialEffects.has(String.valueOf(armorPieces))) {
                        for (var effectElement : materialEffects.getAsJsonArray(String.valueOf(armorPieces))) {
                            JsonObject effectData = effectElement.getAsJsonObject();
                            String effectFullName = effectData.get("effect").getAsString();
                            String[] effectParts = effectFullName.split(":");
                            if (effectParts.length != 2) {
                                System.err.println("Invalid effect format: " + effectFullName);
                                continue;
                            }
                            String effectNamespace = effectParts[0];
                            String effectName = effectParts[1];
                            int amplifier = effectData.get("amplifier").getAsInt();

                            Holder<MobEffect> effectHolder = BuiltInRegistries.MOB_EFFECT.getHolder(ResourceLocation.fromNamespaceAndPath(effectNamespace, effectName)).orElse(null);
                            if (effectHolder != null) {
                                player.addEffect(new MobEffectInstance(effectHolder, 200, amplifier));
                            } else {
                                System.err.println("Invalid MobEffect: " + effectNamespace + ":" + effectName);
                            }
                        }
                    }
                }
            }
        }
    }

    private static void applyAttributesFromJson(Player player, JsonObject data, int year, String major) {
        if (!data.has("majors")) return;

        JsonObject majors = data.getAsJsonObject("majors");
        if (!majors.has(major)) return;

        JsonObject majorData = majors.getAsJsonObject(major);
        if (!majorData.has("attributes")) return;

        JsonObject attributes = majorData.getAsJsonObject("attributes");
        for (ItemStack armorStack : player.getArmorSlots()) {
            if (armorStack.getItem() instanceof ArmorItem armorItem) {
                ArmorMaterial material = armorItem.getMaterial().value();
                String materialName = material.toString().toLowerCase();

                if (attributes.has(materialName)) {
                    JsonObject materialAttributes = attributes.getAsJsonObject(materialName);
                    int armorPieces = countArmorPieces(player, material);

                    if (materialAttributes.has(String.valueOf(armorPieces))) {
                        JsonObject attributeData = materialAttributes.getAsJsonObject(String.valueOf(armorPieces));
                        String attributeNamespace = attributeData.has("namespace") ? attributeData.get("namespace").getAsString() : "minecraft";
                        String attributeName = attributeData.get("attribute").getAsString();
                        double amount = attributeData.get("amount").getAsDouble();
                        String operation = attributeData.get("operation").getAsString();

                        Holder<Attribute> attribute = BuiltInRegistries.ATTRIBUTE.getHolder(ResourceLocation.fromNamespaceAndPath(attributeNamespace, attributeName)).orElse(null);
                        if (attribute != null && player.getAttribute(attribute) != null) {
                            AttributeModifier modifier = new AttributeModifier(
                                    ResourceLocation.fromNamespaceAndPath(attributeNamespace, attributeName),
                                    amount,
                                    AttributeModifier.Operation.valueOf(operation.toUpperCase())
                            );
                            player.getAttribute(attribute).addTransientModifier(modifier);
                        } else {
                            System.err.println("Invalid attribute: " + attributeNamespace + ":" + attributeName);
                        }
                    }
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

    // Debug-Methode
    public static void testArmorEffects(Player player, String major) {
        ResourceLocation resourceLocation = ResourceLocation.fromNamespaceAndPath(Zauberei.MODID, "majors/" + major.toLowerCase() + ".json");
        JsonObject data = Zauberei.getData(resourceLocation.toString());

        if (data != null) {
            System.out.println("ArmorEffects data for " + major + ":");
            System.out.println("Effects: " + data.get("effects"));
            System.out.println("Attributes: " + data.get("attributes"));
        } else {
            System.err.println("No ArmorEffects data loaded for major: " + major);
        }
    }
}
