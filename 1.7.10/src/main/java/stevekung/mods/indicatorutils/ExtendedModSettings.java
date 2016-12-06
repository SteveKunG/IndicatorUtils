/*******************************************************************************
 *
 * Copyright 2016 Wasinthorn Suksri/SteveKunG - Indicator Utils
 *
 ******************************************************************************/

package stevekung.mods.indicatorutils;

import java.io.File;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import stevekung.mods.indicatorutils.utils.CapeUtils;
import stevekung.mods.indicatorutils.utils.IULog;

public class ExtendedModSettings
{
    public static boolean TOGGLE_SPRINT = false;
    public static boolean TOGGLE_SNEAK = false;
    public static boolean AUTO_CLEAR_CHAT = false;
    public static boolean AUTO_SWIM = false;
    public static boolean SHOW_CAPE = true;

    public static int KETSTROKE_Y_OFFSET = 0;
    public static int KETSTROKE_X_OFFSET = 0;
    public static int ARMOR_STATUS_OFFSET = 0;
    public static int POTION_STATUS_OFFSET = 0;
    public static int AUTO_CLEAR_CHAT_TIME = 60;

    public static String CPS_POSITION = "left";
    public static String DISPLAY_MODE = "default";
    public static String AUTO_CLEAR_CHAT_MODE = "all";
    public static String ENTITY_DETECT_TYPE = "null";
    public static String CAPE_URL = "";

    public static String TOGGLE_SPRINT_USE_MODE = "command";
    public static String TOGGLE_SNEAK_USE_MODE = "command";
    public static String DISPLAY_MODE_USE_MODE = "command";
    public static String AUTO_SWIM_USE_MODE = "command";

    public static void loadExtendedSettings()
    {
        File file = new File(Minecraft.getMinecraft().mcDataDir, "indicatorutils.dat");

        try
        {
            NBTTagCompound nbttagcompound = CompressedStreamTools.read(file);

            if (nbttagcompound == null)
            {
                return;
            }

            ExtendedModSettings.TOGGLE_SPRINT = nbttagcompound.getBoolean("ToggleSprint");
            ExtendedModSettings.TOGGLE_SNEAK = nbttagcompound.getBoolean("ToggleSneak");
            ExtendedModSettings.AUTO_CLEAR_CHAT = nbttagcompound.getBoolean("AutoClearChat");
            ExtendedModSettings.AUTO_SWIM = nbttagcompound.getBoolean("AutoSwim");
            ExtendedModSettings.SHOW_CAPE = nbttagcompound.getBoolean("ShowCape");

            ExtendedModSettings.KETSTROKE_X_OFFSET = nbttagcompound.getInteger("KeystrokeX");
            ExtendedModSettings.KETSTROKE_Y_OFFSET = nbttagcompound.getInteger("KeystrokeY");
            ExtendedModSettings.ARMOR_STATUS_OFFSET = nbttagcompound.getInteger("ArmorStatusOffset");
            ExtendedModSettings.POTION_STATUS_OFFSET = nbttagcompound.getInteger("PotionStatusOffset");
            ExtendedModSettings.AUTO_CLEAR_CHAT_TIME = nbttagcompound.getInteger("AutoClearChatTime");

            ExtendedModSettings.CPS_POSITION = nbttagcompound.getString("CpsPosition");
            ExtendedModSettings.DISPLAY_MODE = nbttagcompound.getString("DisplayMode");
            ExtendedModSettings.AUTO_CLEAR_CHAT_MODE = nbttagcompound.getString("AutoClearChatMode");
            ExtendedModSettings.ENTITY_DETECT_TYPE = nbttagcompound.getString("EntityDetectType");
            ExtendedModSettings.CAPE_URL = CapeUtils.decodeCapeURL(nbttagcompound.getString("CapeURL"));

            ExtendedModSettings.TOGGLE_SPRINT_USE_MODE = nbttagcompound.getString("ToggleSprintUseMode");
            ExtendedModSettings.TOGGLE_SNEAK_USE_MODE = nbttagcompound.getString("ToggleSneakUseMode");
            ExtendedModSettings.DISPLAY_MODE_USE_MODE = nbttagcompound.getString("DisplayModeUseMode");
            ExtendedModSettings.AUTO_SWIM_USE_MODE = nbttagcompound.getString("AutoSwimUseMode");

            IULog.info("Loading extended settings : %s", file.getPath());
        }
        catch (Exception e) {}
    }

    public static void saveExtendedSettings()
    {
        File file = new File(Minecraft.getMinecraft().mcDataDir, "indicatorutils.dat");

        try
        {
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            nbttagcompound.setBoolean("ToggleSprint", ExtendedModSettings.TOGGLE_SPRINT);
            nbttagcompound.setBoolean("ToggleSneak", ExtendedModSettings.TOGGLE_SNEAK);
            nbttagcompound.setBoolean("AutoClearChat", ExtendedModSettings.AUTO_CLEAR_CHAT);
            nbttagcompound.setBoolean("AutoSwim", ExtendedModSettings.AUTO_SWIM);
            nbttagcompound.setBoolean("ShowCape", ExtendedModSettings.SHOW_CAPE);

            nbttagcompound.setInteger("KeystrokeX", ExtendedModSettings.KETSTROKE_X_OFFSET);
            nbttagcompound.setInteger("KeystrokeY", ExtendedModSettings.KETSTROKE_Y_OFFSET);
            nbttagcompound.setInteger("ArmorStatusOffset", ExtendedModSettings.ARMOR_STATUS_OFFSET);
            nbttagcompound.setInteger("PotionStatusOffset", ExtendedModSettings.POTION_STATUS_OFFSET);
            nbttagcompound.setInteger("AutoClearChatTime", ExtendedModSettings.AUTO_CLEAR_CHAT_TIME);

            nbttagcompound.setString("CpsPosition", ExtendedModSettings.CPS_POSITION);
            nbttagcompound.setString("DisplayMode", ExtendedModSettings.DISPLAY_MODE);
            nbttagcompound.setString("AutoClearChatMode", ExtendedModSettings.AUTO_CLEAR_CHAT_MODE);
            nbttagcompound.setString("EntityDetectType", ExtendedModSettings.ENTITY_DETECT_TYPE);
            nbttagcompound.setString("CapeURL", ExtendedModSettings.CAPE_URL);

            nbttagcompound.setString("ToggleSprintUseMode", ExtendedModSettings.TOGGLE_SPRINT_USE_MODE);
            nbttagcompound.setString("ToggleSneakUseMode", ExtendedModSettings.TOGGLE_SNEAK_USE_MODE);
            nbttagcompound.setString("DisplayModeUseMode", ExtendedModSettings.DISPLAY_MODE_USE_MODE);
            nbttagcompound.setString("AutoSwimUseMode", ExtendedModSettings.AUTO_SWIM_USE_MODE);

            CompressedStreamTools.safeWrite(nbttagcompound, file);
            IULog.info("Saving extended settings : %s", file.getPath());
        }
        catch (Exception e) {}
    }
}