/*******************************************************************************
 *
 * Copyright 2016 Wasinthorn Suksri/SteveKunG - Indicator Utils
 *
 ******************************************************************************/

package stevekung.mods.indicatorutils.config;

import java.io.File;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import stevekung.mods.indicatorutils.utils.AutoLogin;
import stevekung.mods.indicatorutils.utils.AutoLogin.AutoLoginData;
import stevekung.mods.indicatorutils.utils.IULog;

public class ExtendedModSettings
{
    public static AutoLogin loginData = new AutoLogin();

    public static boolean TOGGLE_SPRINT = false;
    public static boolean TOGGLE_SNEAK = false;
    public static boolean AUTO_CLEAR_CHAT = false;
    public static boolean AUTO_SWIM = false;
    public static boolean SHOW_CAPE = true;

    public static int KEYSTROKE_Y_OFFSET = 0;
    public static int KEYSTROKE_X_OFFSET = 0;
    public static int ARMOR_STATUS_OFFSET = 0;
    public static int POTION_STATUS_OFFSET = 0;
    public static int AUTO_CLEAR_CHAT_TIME = 60;
    public static int CPS_X_OFFSET = 3;
    public static int CPS_Y_OFFSET = 2;
    public static int MAX_POTION_DISPLAY = 2;
    public static int POTION_LENGTH_Y_OFFSET = 23;
    public static int POTION_LENGTH_Y_OFFSET_OVERLAP = 45;
    public static float CPS_OPACITY = 0.5F;
    public static float RENDER_INFO_OPACITY = 0.0F;
    public static long SLIME_CHUNK_SEED = 0L;

    public static String CPS_POSITION = "left";
    public static String DISPLAY_MODE = "default";
    public static String AUTO_CLEAR_CHAT_MODE = "all";
    public static String ENTITY_DETECT_TYPE = "reset";
    public static String CAPE_URL = "";
    public static String CHAT_MODE = "";
    public static String HYPIXEL_NICK_NAME = "";
    public static String REALMS_MESSAGE = "";

    public static String TOGGLE_SPRINT_USE_MODE = "command";
    public static String TOGGLE_SNEAK_USE_MODE = "command";
    public static String DISPLAY_MODE_USE_MODE = "command";
    public static String AUTO_SWIM_USE_MODE = "command";

    public static void loadExtendedSettings()
    {
        File file = new File(Minecraft.getMinecraft().mcDataDir, "indicatorutils.dat");

        try
        {
            NBTTagCompound nbt = CompressedStreamTools.read(file);

            if (nbt == null)
            {
                return;
            }

            ExtendedModSettings.TOGGLE_SPRINT = ExtendedModSettings.getBoolean(nbt, "ToggleSprint", ExtendedModSettings.TOGGLE_SPRINT);
            ExtendedModSettings.TOGGLE_SNEAK = ExtendedModSettings.getBoolean(nbt, "ToggleSneak", ExtendedModSettings.TOGGLE_SNEAK);
            ExtendedModSettings.AUTO_CLEAR_CHAT = ExtendedModSettings.getBoolean(nbt, "AutoClearChat", ExtendedModSettings.AUTO_CLEAR_CHAT);
            ExtendedModSettings.AUTO_SWIM = ExtendedModSettings.getBoolean(nbt, "AutoSwim", ExtendedModSettings.AUTO_SWIM);
            ExtendedModSettings.SHOW_CAPE = ExtendedModSettings.getBoolean(nbt, "ShowCape", ExtendedModSettings.SHOW_CAPE);

            ExtendedModSettings.KEYSTROKE_X_OFFSET = ExtendedModSettings.getInteger(nbt, "KeystrokeX", ExtendedModSettings.KEYSTROKE_X_OFFSET);
            ExtendedModSettings.KEYSTROKE_Y_OFFSET = ExtendedModSettings.getInteger(nbt, "KeystrokeY", ExtendedModSettings.KEYSTROKE_Y_OFFSET);
            ExtendedModSettings.ARMOR_STATUS_OFFSET = ExtendedModSettings.getInteger(nbt, "ArmorStatusOffset", ExtendedModSettings.ARMOR_STATUS_OFFSET);
            ExtendedModSettings.POTION_STATUS_OFFSET = ExtendedModSettings.getInteger(nbt, "PotionStatusOffset", ExtendedModSettings.POTION_STATUS_OFFSET);
            ExtendedModSettings.AUTO_CLEAR_CHAT_TIME = ExtendedModSettings.getInteger(nbt, "AutoClearChatTime", ExtendedModSettings.AUTO_CLEAR_CHAT_TIME);
            ExtendedModSettings.CPS_X_OFFSET = ExtendedModSettings.getInteger(nbt, "CpsX", ExtendedModSettings.CPS_X_OFFSET);
            ExtendedModSettings.CPS_Y_OFFSET = ExtendedModSettings.getInteger(nbt, "CpsY", ExtendedModSettings.CPS_Y_OFFSET);
            ExtendedModSettings.MAX_POTION_DISPLAY = ExtendedModSettings.getInteger(nbt, "MaxPotionDisplay", ExtendedModSettings.MAX_POTION_DISPLAY);
            ExtendedModSettings.POTION_LENGTH_Y_OFFSET = ExtendedModSettings.getInteger(nbt, "PotionLengthYOffset", ExtendedModSettings.POTION_LENGTH_Y_OFFSET);
            ExtendedModSettings.POTION_LENGTH_Y_OFFSET_OVERLAP = ExtendedModSettings.getInteger(nbt, "PotionLengthYOffsetOverlap", ExtendedModSettings.POTION_LENGTH_Y_OFFSET_OVERLAP);
            ExtendedModSettings.CPS_OPACITY = ExtendedModSettings.getFloat(nbt, "CpsOpacity", ExtendedModSettings.CPS_OPACITY);
            ExtendedModSettings.RENDER_INFO_OPACITY = ExtendedModSettings.getFloat(nbt, "RenderInfoOpacity", ExtendedModSettings.RENDER_INFO_OPACITY);
            ExtendedModSettings.SLIME_CHUNK_SEED = ExtendedModSettings.getLong(nbt, "SlimeChunkSeed", ExtendedModSettings.SLIME_CHUNK_SEED);

            ExtendedModSettings.CPS_POSITION = ExtendedModSettings.getString(nbt, "CpsPosition", ExtendedModSettings.CPS_POSITION);
            ExtendedModSettings.DISPLAY_MODE = ExtendedModSettings.getString(nbt, "DisplayMode", ExtendedModSettings.DISPLAY_MODE);
            ExtendedModSettings.AUTO_CLEAR_CHAT_MODE = ExtendedModSettings.getString(nbt, "AutoClearChatMode", ExtendedModSettings.AUTO_CLEAR_CHAT_MODE);
            ExtendedModSettings.ENTITY_DETECT_TYPE = ExtendedModSettings.getString(nbt, "EntityDetectType", ExtendedModSettings.ENTITY_DETECT_TYPE);
            ExtendedModSettings.CAPE_URL = ExtendedModSettings.getString(nbt, "CapeURL", ExtendedModSettings.CAPE_URL);
            ExtendedModSettings.CHAT_MODE = ExtendedModSettings.getString(nbt, "ChatMode", ExtendedModSettings.CHAT_MODE);
            ExtendedModSettings.HYPIXEL_NICK_NAME = ExtendedModSettings.getString(nbt, "HypixelNickName", ExtendedModSettings.HYPIXEL_NICK_NAME);
            ExtendedModSettings.REALMS_MESSAGE = ExtendedModSettings.getString(nbt, "RealmsMessage", ExtendedModSettings.REALMS_MESSAGE);

            ExtendedModSettings.TOGGLE_SPRINT_USE_MODE = ExtendedModSettings.getString(nbt, "ToggleSprintUseMode", ExtendedModSettings.TOGGLE_SPRINT_USE_MODE);
            ExtendedModSettings.TOGGLE_SNEAK_USE_MODE = ExtendedModSettings.getString(nbt, "ToggleSneakUseMode", ExtendedModSettings.TOGGLE_SNEAK_USE_MODE);
            ExtendedModSettings.DISPLAY_MODE_USE_MODE = ExtendedModSettings.getString(nbt, "DisplayModeUseMode", ExtendedModSettings.DISPLAY_MODE_USE_MODE);
            ExtendedModSettings.AUTO_SWIM_USE_MODE = ExtendedModSettings.getString(nbt, "AutoSwimUseMode", ExtendedModSettings.AUTO_SWIM_USE_MODE);

            ExtendedModSettings.readAutoLoginData(nbt.getTagList("AutoLoginData", 10));

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

            nbttagcompound.setInteger("KeystrokeX", ExtendedModSettings.KEYSTROKE_X_OFFSET);
            nbttagcompound.setInteger("KeystrokeY", ExtendedModSettings.KEYSTROKE_Y_OFFSET);
            nbttagcompound.setInteger("ArmorStatusOffset", ExtendedModSettings.ARMOR_STATUS_OFFSET);
            nbttagcompound.setInteger("PotionStatusOffset", ExtendedModSettings.POTION_STATUS_OFFSET);
            nbttagcompound.setInteger("AutoClearChatTime", ExtendedModSettings.AUTO_CLEAR_CHAT_TIME);
            nbttagcompound.setInteger("CpsX", ExtendedModSettings.CPS_X_OFFSET);
            nbttagcompound.setInteger("CpsY", ExtendedModSettings.CPS_Y_OFFSET);
            nbttagcompound.setInteger("MaxPotionDisplay", ExtendedModSettings.MAX_POTION_DISPLAY);
            nbttagcompound.setInteger("PotionLengthYOffset", ExtendedModSettings.POTION_LENGTH_Y_OFFSET);
            nbttagcompound.setInteger("PotionLengthYOffsetOverlap", ExtendedModSettings.POTION_LENGTH_Y_OFFSET_OVERLAP);
            nbttagcompound.setFloat("CpsOpacity", ExtendedModSettings.CPS_OPACITY);
            nbttagcompound.setFloat("RenderInfoOpacity", ExtendedModSettings.RENDER_INFO_OPACITY);
            nbttagcompound.setLong("SlimeChunkSeed", ExtendedModSettings.SLIME_CHUNK_SEED);

            nbttagcompound.setString("CpsPosition", ExtendedModSettings.CPS_POSITION);
            nbttagcompound.setString("DisplayMode", ExtendedModSettings.DISPLAY_MODE);
            nbttagcompound.setString("AutoClearChatMode", ExtendedModSettings.AUTO_CLEAR_CHAT_MODE);
            nbttagcompound.setString("EntityDetectType", ExtendedModSettings.ENTITY_DETECT_TYPE);
            nbttagcompound.setString("CapeURL", ExtendedModSettings.CAPE_URL);
            nbttagcompound.setString("ChatMode", ExtendedModSettings.CHAT_MODE);
            nbttagcompound.setString("HypixelNickName", ExtendedModSettings.HYPIXEL_NICK_NAME);
            nbttagcompound.setString("RealmsMessage", ExtendedModSettings.REALMS_MESSAGE);

            nbttagcompound.setString("ToggleSprintUseMode", ExtendedModSettings.TOGGLE_SPRINT_USE_MODE);
            nbttagcompound.setString("ToggleSneakUseMode", ExtendedModSettings.TOGGLE_SNEAK_USE_MODE);
            nbttagcompound.setString("DisplayModeUseMode", ExtendedModSettings.DISPLAY_MODE_USE_MODE);
            nbttagcompound.setString("AutoSwimUseMode", ExtendedModSettings.AUTO_SWIM_USE_MODE);

            nbttagcompound.setTag("AutoLoginData", ExtendedModSettings.writeAutoLoginData());

            CompressedStreamTools.safeWrite(nbttagcompound, file);
        }
        catch (Exception e) {}
    }

    private static NBTTagList writeAutoLoginData()
    {
        NBTTagList list = new NBTTagList();

        for (AutoLoginData login : ExtendedModSettings.loginData.getAutoLoginList())
        {
            NBTTagCompound nbt = new NBTTagCompound();
            nbt.setString("ServerIP", login.getServerIP());
            nbt.setString("CommandName", login.getCommand());
            nbt.setString("Value", login.getValue());
            nbt.setString("Username", login.getUsername());
            list.appendTag(nbt);
        }
        return list;
    }

    private static void readAutoLoginData(NBTTagList list)
    {
        for (int i = 0; i < list.tagCount(); ++i)
        {
            NBTTagCompound nbt = list.getCompoundTagAt(i);
            ExtendedModSettings.loginData.addAutoLogin(nbt.getString("ServerIP"), nbt.getString("CommandName"), nbt.getString("Value"), nbt.getString("Username"));
        }
    }

    private static boolean getBoolean(NBTTagCompound nbt, String key, boolean defaultValue)
    {
        if (nbt.hasKey(key, 99))
        {
            return nbt.getBoolean(key);
        }
        else
        {
            return defaultValue;
        }
    }

    private static int getInteger(NBTTagCompound nbt, String key, int defaultValue)
    {
        if (nbt.hasKey(key, 99))
        {
            return nbt.getInteger(key);
        }
        else
        {
            return defaultValue;
        }
    }

    private static float getFloat(NBTTagCompound nbt, String key, float defaultValue)
    {
        if (nbt.hasKey(key, 99))
        {
            return nbt.getFloat(key);
        }
        else
        {
            return defaultValue;
        }
    }

    private static String getString(NBTTagCompound nbt, String key, String defaultValue)
    {
        if (nbt.hasKey(key, 8))
        {
            return nbt.getString(key);
        }
        else
        {
            return defaultValue;
        }
    }

    private static long getLong(NBTTagCompound nbt, String key, long defaultValue)
    {
        if (nbt.hasKey(key, 99))
        {
            return nbt.getLong(key);
        }
        else
        {
            return defaultValue;
        }
    }
}