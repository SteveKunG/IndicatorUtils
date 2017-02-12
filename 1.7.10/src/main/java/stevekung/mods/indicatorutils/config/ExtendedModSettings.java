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
    public static float CPS_OPACITY = 0.5F;

    public static String CPS_POSITION = "left";
    public static String DISPLAY_MODE = "default";
    public static String AUTO_CLEAR_CHAT_MODE = "all";
    public static String CAPE_URL = "";
    public static String CHAT_MODE = "";

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

            ExtendedModSettings.KEYSTROKE_X_OFFSET = nbttagcompound.getInteger("KeystrokeX");
            ExtendedModSettings.KEYSTROKE_Y_OFFSET = nbttagcompound.getInteger("KeystrokeY");
            ExtendedModSettings.ARMOR_STATUS_OFFSET = nbttagcompound.getInteger("ArmorStatusOffset");
            ExtendedModSettings.POTION_STATUS_OFFSET = nbttagcompound.getInteger("PotionStatusOffset");
            ExtendedModSettings.AUTO_CLEAR_CHAT_TIME = nbttagcompound.getInteger("AutoClearChatTime");
            ExtendedModSettings.CPS_X_OFFSET = nbttagcompound.getInteger("CpsX");
            ExtendedModSettings.CPS_Y_OFFSET = nbttagcompound.getInteger("CpsY");
            ExtendedModSettings.CPS_OPACITY = nbttagcompound.getFloat("CpsOpacity");

            ExtendedModSettings.CPS_POSITION = nbttagcompound.getString("CpsPosition");
            ExtendedModSettings.DISPLAY_MODE = nbttagcompound.getString("DisplayMode");
            ExtendedModSettings.AUTO_CLEAR_CHAT_MODE = nbttagcompound.getString("AutoClearChatMode");
            ExtendedModSettings.CAPE_URL = nbttagcompound.getString("CapeURL");
            ExtendedModSettings.CHAT_MODE = nbttagcompound.getString("ChatMode");

            ExtendedModSettings.TOGGLE_SPRINT_USE_MODE = nbttagcompound.getString("ToggleSprintUseMode");
            ExtendedModSettings.TOGGLE_SNEAK_USE_MODE = nbttagcompound.getString("ToggleSneakUseMode");
            ExtendedModSettings.DISPLAY_MODE_USE_MODE = nbttagcompound.getString("DisplayModeUseMode");
            ExtendedModSettings.AUTO_SWIM_USE_MODE = nbttagcompound.getString("AutoSwimUseMode");

            ExtendedModSettings.readAutoLoginData(nbttagcompound.getTagList("AutoLoginData", 10));

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
            nbttagcompound.setFloat("CpsOpacity", ExtendedModSettings.CPS_OPACITY);

            nbttagcompound.setString("CpsPosition", ExtendedModSettings.CPS_POSITION);
            nbttagcompound.setString("DisplayMode", ExtendedModSettings.DISPLAY_MODE);
            nbttagcompound.setString("AutoClearChatMode", ExtendedModSettings.AUTO_CLEAR_CHAT_MODE);
            nbttagcompound.setString("CapeURL", ExtendedModSettings.CAPE_URL);
            nbttagcompound.setString("ChatMode", ExtendedModSettings.CHAT_MODE);

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
}