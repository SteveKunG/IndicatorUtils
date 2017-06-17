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

    public static int KEYSTROKE_BLOCK_RED = 255;
    public static int KEYSTROKE_BLOCK_GREEN = 255;
    public static int KEYSTROKE_BLOCK_BLUE = 255;
    public static int KEYSTROKE_CPS_RED = 255;
    public static int KEYSTROKE_CPS_GREEN = 255;
    public static int KEYSTROKE_CPS_BLUE = 255;
    public static int KEYSTROKE_WASD_RED = 255;
    public static int KEYSTROKE_WASD_GREEN = 255;
    public static int KEYSTROKE_WASD_BLUE = 255;
    public static int KEYSTROKE_LMBRMB_RED = 255;
    public static int KEYSTROKE_LMBRMB_GREEN = 255;
    public static int KEYSTROKE_LMBRMB_BLUE = 255;
    public static int KEYSTROKE_SPRINT_RED = 255;
    public static int KEYSTROKE_SPRINT_GREEN = 255;
    public static int KEYSTROKE_SPRINT_BLUE = 255;
    public static int KEYSTROKE_SNEAK_RED = 255;
    public static int KEYSTROKE_SNEAK_GREEN = 255;
    public static int KEYSTROKE_SNEAK_BLUE = 255;

    public static boolean KEYSTROKE_BLOCK_RAINBOW = false;
    public static boolean KEYSTROKE_CPS_RAINBOW = false;
    public static boolean KEYSTROKE_WASD_RAINBOW = false;
    public static boolean KEYSTROKE_LMBRMB_RAINBOW = false;
    public static boolean KEYSTROKE_SPRINT_RAINBOW = false;
    public static boolean KEYSTROKE_SNEAK_RAINBOW = false;

    public static String CPS_POSITION = "left";
    public static String AUTO_CLEAR_CHAT_MODE = "all";
    public static String CAPE_URL = "";
    public static String CHAT_MODE = "";

    public static String TOGGLE_SPRINT_USE_MODE = "command";
    public static String TOGGLE_SNEAK_USE_MODE = "command";
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

            ExtendedModSettings.KEYSTROKE_BLOCK_RED = ExtendedModSettings.getInteger(nbt, "KSBlockR", ExtendedModSettings.KEYSTROKE_BLOCK_RED);
            ExtendedModSettings.KEYSTROKE_BLOCK_GREEN = ExtendedModSettings.getInteger(nbt, "KSBlockG", ExtendedModSettings.KEYSTROKE_BLOCK_GREEN);
            ExtendedModSettings.KEYSTROKE_BLOCK_BLUE = ExtendedModSettings.getInteger(nbt, "KSBlockB", ExtendedModSettings.KEYSTROKE_BLOCK_BLUE);
            ExtendedModSettings.KEYSTROKE_CPS_RED = ExtendedModSettings.getInteger(nbt, "KSCPSR", ExtendedModSettings.KEYSTROKE_CPS_RED);
            ExtendedModSettings.KEYSTROKE_CPS_GREEN = ExtendedModSettings.getInteger(nbt, "KSCPSG", ExtendedModSettings.KEYSTROKE_CPS_GREEN);
            ExtendedModSettings.KEYSTROKE_CPS_BLUE = ExtendedModSettings.getInteger(nbt, "KSCPSB", ExtendedModSettings.KEYSTROKE_CPS_BLUE);
            ExtendedModSettings.KEYSTROKE_WASD_RED = ExtendedModSettings.getInteger(nbt, "KSWASDR", ExtendedModSettings.KEYSTROKE_WASD_RED);
            ExtendedModSettings.KEYSTROKE_WASD_GREEN = ExtendedModSettings.getInteger(nbt, "KSWASDG", ExtendedModSettings.KEYSTROKE_WASD_GREEN);
            ExtendedModSettings.KEYSTROKE_WASD_BLUE = ExtendedModSettings.getInteger(nbt, "KSWASDB", ExtendedModSettings.KEYSTROKE_WASD_BLUE);
            ExtendedModSettings.KEYSTROKE_LMBRMB_RED = ExtendedModSettings.getInteger(nbt, "KSLMBRMBR", ExtendedModSettings.KEYSTROKE_LMBRMB_RED);
            ExtendedModSettings.KEYSTROKE_LMBRMB_GREEN = ExtendedModSettings.getInteger(nbt, "KSLMBRMBG", ExtendedModSettings.KEYSTROKE_LMBRMB_GREEN);
            ExtendedModSettings.KEYSTROKE_LMBRMB_BLUE = ExtendedModSettings.getInteger(nbt, "KSLMBRMBB", ExtendedModSettings.KEYSTROKE_LMBRMB_BLUE);
            ExtendedModSettings.KEYSTROKE_SPRINT_RED = ExtendedModSettings.getInteger(nbt, "KSSprintR", ExtendedModSettings.KEYSTROKE_SPRINT_RED);
            ExtendedModSettings.KEYSTROKE_SPRINT_GREEN = ExtendedModSettings.getInteger(nbt, "KSSprintG", ExtendedModSettings.KEYSTROKE_SPRINT_GREEN);
            ExtendedModSettings.KEYSTROKE_SPRINT_BLUE = ExtendedModSettings.getInteger(nbt, "KSSprintB", ExtendedModSettings.KEYSTROKE_SPRINT_BLUE);
            ExtendedModSettings.KEYSTROKE_SNEAK_RED = ExtendedModSettings.getInteger(nbt, "KSSneakR", ExtendedModSettings.KEYSTROKE_SNEAK_RED);
            ExtendedModSettings.KEYSTROKE_SNEAK_GREEN = ExtendedModSettings.getInteger(nbt, "KSSneakG", ExtendedModSettings.KEYSTROKE_SNEAK_GREEN);
            ExtendedModSettings.KEYSTROKE_SNEAK_BLUE = ExtendedModSettings.getInteger(nbt, "KSSneakB", ExtendedModSettings.KEYSTROKE_SNEAK_BLUE);

            ExtendedModSettings.KEYSTROKE_BLOCK_RAINBOW = ExtendedModSettings.getBoolean(nbt, "KSBlockRB", ExtendedModSettings.KEYSTROKE_BLOCK_RAINBOW);
            ExtendedModSettings.KEYSTROKE_CPS_RAINBOW = ExtendedModSettings.getBoolean(nbt, "KSCPSRB", ExtendedModSettings.KEYSTROKE_CPS_RAINBOW);
            ExtendedModSettings.KEYSTROKE_WASD_RAINBOW = ExtendedModSettings.getBoolean(nbt, "KSWASDRB", ExtendedModSettings.KEYSTROKE_WASD_RAINBOW);
            ExtendedModSettings.KEYSTROKE_LMBRMB_RAINBOW = ExtendedModSettings.getBoolean(nbt, "KSLMBRMBRB", ExtendedModSettings.KEYSTROKE_LMBRMB_RAINBOW);
            ExtendedModSettings.KEYSTROKE_SPRINT_RAINBOW = ExtendedModSettings.getBoolean(nbt, "KSSprintRB", ExtendedModSettings.KEYSTROKE_SPRINT_RAINBOW);
            ExtendedModSettings.KEYSTROKE_SNEAK_RAINBOW = ExtendedModSettings.getBoolean(nbt, "KSSneakRB", ExtendedModSettings.KEYSTROKE_SNEAK_RAINBOW);

            ExtendedModSettings.CPS_POSITION = ExtendedModSettings.getString(nbt, "CpsPosition", ExtendedModSettings.CPS_POSITION);
            ExtendedModSettings.AUTO_CLEAR_CHAT_MODE = ExtendedModSettings.getString(nbt, "AutoClearChatMode", ExtendedModSettings.AUTO_CLEAR_CHAT_MODE);
            ExtendedModSettings.CAPE_URL = ExtendedModSettings.getString(nbt, "CapeURL", ExtendedModSettings.CAPE_URL);
            ExtendedModSettings.CHAT_MODE = ExtendedModSettings.getString(nbt, "ChatMode", ExtendedModSettings.CHAT_MODE);

            ExtendedModSettings.TOGGLE_SPRINT_USE_MODE = ExtendedModSettings.getString(nbt, "ToggleSprintUseMode", ExtendedModSettings.TOGGLE_SPRINT_USE_MODE);
            ExtendedModSettings.TOGGLE_SNEAK_USE_MODE = ExtendedModSettings.getString(nbt, "ToggleSneakUseMode", ExtendedModSettings.TOGGLE_SNEAK_USE_MODE);
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
            NBTTagCompound nbt = new NBTTagCompound();
            nbt.setBoolean("ToggleSprint", ExtendedModSettings.TOGGLE_SPRINT);
            nbt.setBoolean("ToggleSneak", ExtendedModSettings.TOGGLE_SNEAK);
            nbt.setBoolean("AutoClearChat", ExtendedModSettings.AUTO_CLEAR_CHAT);
            nbt.setBoolean("AutoSwim", ExtendedModSettings.AUTO_SWIM);
            nbt.setBoolean("ShowCape", ExtendedModSettings.SHOW_CAPE);

            nbt.setInteger("KeystrokeY", ExtendedModSettings.KEYSTROKE_Y_OFFSET);
            nbt.setInteger("ArmorStatusOffset", ExtendedModSettings.ARMOR_STATUS_OFFSET);
            nbt.setInteger("PotionStatusOffset", ExtendedModSettings.POTION_STATUS_OFFSET);
            nbt.setInteger("AutoClearChatTime", ExtendedModSettings.AUTO_CLEAR_CHAT_TIME);
            nbt.setInteger("CpsX", ExtendedModSettings.CPS_X_OFFSET);
            nbt.setInteger("CpsY", ExtendedModSettings.CPS_Y_OFFSET);
            nbt.setFloat("CpsOpacity", ExtendedModSettings.CPS_OPACITY);
            nbt.setFloat("RenderInfoOpacity", ExtendedModSettings.RENDER_INFO_OPACITY);
            nbt.setLong("SlimeChunkSeed", ExtendedModSettings.SLIME_CHUNK_SEED);

            nbt.setInteger("KSBlockR", ExtendedModSettings.KEYSTROKE_BLOCK_RED);
            nbt.setInteger("KSBlockG", ExtendedModSettings.KEYSTROKE_BLOCK_GREEN);
            nbt.setInteger("KSBlockB", ExtendedModSettings.KEYSTROKE_BLOCK_BLUE);
            nbt.setInteger("KSCPSR", ExtendedModSettings.KEYSTROKE_CPS_RED);
            nbt.setInteger("KSCPSG", ExtendedModSettings.KEYSTROKE_CPS_GREEN);
            nbt.setInteger("KSCPSB", ExtendedModSettings.KEYSTROKE_CPS_BLUE);
            nbt.setInteger("KSWASDR", ExtendedModSettings.KEYSTROKE_WASD_RED);
            nbt.setInteger("KSWASDG", ExtendedModSettings.KEYSTROKE_WASD_GREEN);
            nbt.setInteger("KSWASDB", ExtendedModSettings.KEYSTROKE_WASD_BLUE);
            nbt.setInteger("KSLMBRMBR", ExtendedModSettings.KEYSTROKE_LMBRMB_RED);
            nbt.setInteger("KSLMBRMBG", ExtendedModSettings.KEYSTROKE_LMBRMB_GREEN);
            nbt.setInteger("KSLMBRMBB", ExtendedModSettings.KEYSTROKE_LMBRMB_BLUE);
            nbt.setInteger("KSSprintR", ExtendedModSettings.KEYSTROKE_SPRINT_RED);
            nbt.setInteger("KSSprintG", ExtendedModSettings.KEYSTROKE_SPRINT_GREEN);
            nbt.setInteger("KSSprintB", ExtendedModSettings.KEYSTROKE_SPRINT_BLUE);
            nbt.setInteger("KSSneakR", ExtendedModSettings.KEYSTROKE_SNEAK_RED);
            nbt.setInteger("KSSneakG", ExtendedModSettings.KEYSTROKE_SNEAK_GREEN);
            nbt.setInteger("KSSneakB", ExtendedModSettings.KEYSTROKE_SNEAK_BLUE);

            nbt.setBoolean("KSBlockRB", ExtendedModSettings.KEYSTROKE_BLOCK_RAINBOW);
            nbt.setBoolean("KSCPSRB", ExtendedModSettings.KEYSTROKE_CPS_RAINBOW);
            nbt.setBoolean("KSWASDRB", ExtendedModSettings.KEYSTROKE_WASD_RAINBOW);
            nbt.setBoolean("KSLMBRMBRB", ExtendedModSettings.KEYSTROKE_LMBRMB_RAINBOW);
            nbt.setBoolean("KSSprintRB", ExtendedModSettings.KEYSTROKE_SPRINT_RAINBOW);
            nbt.setBoolean("KSSneakRB", ExtendedModSettings.KEYSTROKE_SNEAK_RAINBOW);

            nbt.setString("CpsPosition", ExtendedModSettings.CPS_POSITION);
            nbt.setString("AutoClearChatMode", ExtendedModSettings.AUTO_CLEAR_CHAT_MODE);
            nbt.setString("CapeURL", ExtendedModSettings.CAPE_URL);
            nbt.setString("ChatMode", ExtendedModSettings.CHAT_MODE);

            nbt.setString("ToggleSprintUseMode", ExtendedModSettings.TOGGLE_SPRINT_USE_MODE);
            nbt.setString("ToggleSneakUseMode", ExtendedModSettings.TOGGLE_SNEAK_USE_MODE);
            nbt.setString("AutoSwimUseMode", ExtendedModSettings.AUTO_SWIM_USE_MODE);

            nbt.setTag("AutoLoginData", ExtendedModSettings.writeAutoLoginData());

            CompressedStreamTools.safeWrite(nbt, file);
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