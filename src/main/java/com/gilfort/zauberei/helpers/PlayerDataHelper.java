package com.gilfort.zauberei.helpers;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;

public class PlayerDataHelper {

    private static final String MOD_DATA_KEY = "tutorialmod";
    private static final String MAJOR_TAG = "Major";

    // Speichert den Major-Tag
    public static void setMajor(ServerPlayer player, String major) {
        CompoundTag persistentData = player.getPersistentData().getCompound(MOD_DATA_KEY);
        persistentData.putString(MAJOR_TAG, major);
        player.getPersistentData().put(MOD_DATA_KEY, persistentData);
    }

    // Liest den Major-Tag
    public static String getMajor(ServerPlayer player) {
        CompoundTag persistentData = player.getPersistentData().getCompound(MOD_DATA_KEY);
        return persistentData.getString(MAJOR_TAG);
    }
}
