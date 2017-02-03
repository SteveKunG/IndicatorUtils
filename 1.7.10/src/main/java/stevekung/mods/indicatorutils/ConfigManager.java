/*******************************************************************************
 *
 * Copyright 2016 Wasinthorn Suksri/SteveKunG - Indicator Utils
 *
 ******************************************************************************/

package stevekung.mods.indicatorutils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.client.config.ConfigGuiType;
import cpw.mods.fml.client.config.DummyConfigElement;
import cpw.mods.fml.client.config.GuiConfigEntries.NumberSliderEntry;
import cpw.mods.fml.client.config.IConfigElement;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import stevekung.mods.indicatorutils.utils.IULog;
import stevekung.mods.indicatorutils.utils.helper.GameInfoHelper;

public class ConfigManager
{
    private static Configuration config;
    public static String MAIN_SETTINGS = "indicatorutilmaingui";
    public static String RENDER_INFO_SETTINGS = "indicatorutilrenderinfogui";
    public static String INGAME_RENDER_SETTINGS = "indicatorutilingamerendergui";
    public static String KEY_BINDING_SETTINGS = "indicatorutilkeybindinggui";
    public static String OFFSET_SETTINGS = "indicatorutiloffsetgui";
    public static String TIME_INFO_SETTINGS = "indicatorutiltimeinfogui";
    public static String CUSTOM_COLOR_SETTINGS = "indicatorutilcustomcolorgui";
    public static String CUSTOM_TEXT_SETTINGS = "indicatorutilcustomtextgui";

    // Main Settings
    public static boolean enableAllRenderInfo;
    public static int afkMessageTime;
    public static String endGameChatMessage;
    public static boolean enableArmorStatusRenderBug;
    public static boolean enableHeldItemRenderBug;
    public static boolean enableEndGameChatMessage;
    public static boolean enableAFKMessage;
    public static boolean displayArmorHeldWhileChatOpen;
    public static boolean highlightPotionColor;
    public static boolean enableModifiedMovementHandler;
    public static boolean enableCustomCapeFeature;
    public static boolean showChangeLogInGame;
    public static boolean enableVersionChecker;
    public static boolean mojangStatusCheckOnStartup;

    // Render Info Settings
    public static boolean swapMainRenderInfoToRight;
    public static String armorStatusMode;
    public static String healthStatusMode;
    public static String keystrokeMode;
    public static String potionStatusStyle;
    public static String keystrokeSize;
    public static String heldItemStatusMode;
    public static boolean enablePing;
    public static boolean enableServerIP;
    public static boolean enableServerIPWithMCVersion;
    public static boolean enableFPS;
    public static boolean enableXYZ;
    public static boolean enableOverworldCoordinate;
    public static boolean enableLookingAtBlock;
    public static boolean enableDirection;
    public static boolean enableBiome;
    public static boolean enableArmorStatus;
    public static boolean enablePotionStatus;
    public static boolean enableKeystroke;
    public static boolean enableKeystrokeLMBRMB;
    public static boolean enableKeystrokeSprintSneak;
    public static boolean enableKeystrokeBlocking;
    public static boolean enableCPS;
    public static boolean enableRPS;
    public static boolean enableHeldItemInHand;
    public static boolean showPotionIcon;

    // In-game Render Settings
    public static String playerPingMode;
    public static boolean renderScoreboard;
    public static boolean renderBossHealthBar;
    public static boolean hideBossHealthBar;

    // Key Binding Settings
    public static String keyDisplayModeNext;
    public static String keyDisplayModePrevious;
    public static String keyToggleSprint;
    public static String keyToggleSneak;
    public static String keyAutoSwim;

    // Offset Settings
    public static String armorStatusPosition;
    public static String potionStatusPosition;
    public static String keystrokePosition;
    public static int potionSize;
    public static int potionLengthVal;
    public static int potionLength;

    // Time Info Settings
    public static String timeZoneName;
    public static String dateFormat;
    public static boolean useShortDate;
    public static boolean enableCurrentTime;
    public static boolean enableGameTime;
    public static boolean enableWeatherStatus;
    public static boolean enableTimeZone;
    public static boolean enableStandardWorldTime;

    // Custom Text Color Settings
    public static String customColorPing;
    public static String customColorPingValue1;
    public static String customColorPingValue2;
    public static String customColorPingValue3;
    public static String customColorPingValue4;
    public static String customColorIP;
    public static String customColorIPValue;
    public static String customColorIPMCValue;
    public static String customColorFPS;
    public static String customColorFPSValue1;
    public static String customColorFPSValue2;
    public static String customColorFPSValue3;
    public static String customColorXYZ;
    public static String customColorXYZNether;
    public static String customColorXYZOverworld;
    public static String customColorXValue;
    public static String customColorYValue;
    public static String customColorZValue;
    public static String customColorBiome;
    public static String customColorBiomeValue;
    public static String customColorCPS;
    public static String customColorCPSValue;
    public static String customColorRPS;
    public static String customColorRPSValue;
    public static String customColorArmorPercent;
    public static String customColorArmorMaxDurability;
    public static String customColorArmorDamageDurability;
    public static String customColorCurrentTime;
    public static String customColorCurrentTimeDay;
    public static String customColorCurrentTimeMonth;
    public static String customColorCurrentTimeYear;
    public static String customColorCurrentTimeValue;
    public static String customColorTimeAMPM;
    public static String customColorGameTime;
    public static String customColorGameTimeValue;
    public static String customColorTimeZone;
    public static String customColorTimeZoneValue;
    public static String customColorWorldTime;
    public static String customColorLookingAt;
    public static String customColorDirection;
    public static String customColorDirectionValue;
    public static String customColorWeatherPrefix;
    public static String customColorRaining;
    public static String customColorThunder;
    public static String customColorHeldItem;
    public static String customColorHeldItemArrowCount;

    // Custom Text Settings
    public static boolean useCustomTextPing;
    public static String customTextPing;
    public static boolean useCustomTextIP;
    public static String customTextIP;
    public static String customTextRealms;
    public static boolean useCustomTextFPS;
    public static String customTextFPS;
    public static boolean useCustomTextXYZ;
    public static String customTextXYZ;
    public static String customTextXYZNether;
    public static String customTextXYZOverworld;
    public static boolean useCustomTextBiome;
    public static String customTextBiome;
    public static boolean useCustomTextCPS;
    public static String customTextCPS;
    public static boolean useCustomTextRPS;
    public static String customTextRPS;
    public static boolean useCustomTextTime;
    public static String customTextTime;
    public static boolean useCustomTextGameTime;
    public static String customTextGameTime;
    public static boolean useCustomTextTimeZone;
    public static String customTextTimeZone;
    public static boolean useCustomTextLookingAt;
    public static String customTextLookingAt;
    public static boolean useCustomTextDirection;
    public static String customTextDirection;
    public static boolean useCustomWeather;
    public static String customTextWeather;
    public static String customTextRaining;
    public static String customTextThunder;

    public static void init(File file)
    {
        ConfigManager.config = new Configuration(file);
        ConfigManager.syncConfig(true);
    }

    public static void syncConfig(boolean load)
    {
        try
        {
            Property prop = null;

            if (!ConfigManager.config.isChild)
            {
                if (load)
                {
                    ConfigManager.config.load();
                }
            }

            ConfigManager.config.setCategoryPropertyOrder(ConfigManager.MAIN_SETTINGS, ConfigManager.addMainSetting(prop));
            ConfigManager.config.setCategoryPropertyOrder(ConfigManager.RENDER_INFO_SETTINGS, ConfigManager.addRenderInfoSetting(prop));
            ConfigManager.config.setCategoryPropertyOrder(ConfigManager.INGAME_RENDER_SETTINGS, ConfigManager.addIngameRenderSetting(prop));
            ConfigManager.config.setCategoryPropertyOrder(ConfigManager.KEY_BINDING_SETTINGS, ConfigManager.addKeyBindingSetting(prop));
            ConfigManager.config.setCategoryPropertyOrder(ConfigManager.OFFSET_SETTINGS, ConfigManager.addOffsetSetting(prop));
            ConfigManager.config.setCategoryPropertyOrder(ConfigManager.TIME_INFO_SETTINGS, ConfigManager.addTimeInfoSetting(prop));
            ConfigManager.config.setCategoryPropertyOrder(ConfigManager.CUSTOM_COLOR_SETTINGS, ConfigManager.addCustomColorSetting(prop));
            ConfigManager.config.setCategoryPropertyOrder(ConfigManager.CUSTOM_TEXT_SETTINGS, ConfigManager.addCustomTextSetting(prop));

            if (ConfigManager.config.hasChanged())
            {
                ConfigManager.config.save();
            }
        }
        catch (Exception e)
        {
            IULog.error("Indicator Utils has a problem loading it's configuration");
        }
    }

    public static List<String> addMainSetting(Property prop)
    {
        List<String> propOrder = new ArrayList<String>();
        prop = ConfigManager.config.get(ConfigManager.MAIN_SETTINGS, "Enable All Render Info", true);
        ConfigManager.enableAllRenderInfo = prop.getBoolean();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.MAIN_SETTINGS, "AFK Message Time (minute)", 5);
        prop.setMinValue(1).setMaxValue(60).setConfigEntryClass(NumberSliderEntry.class);
        ConfigManager.afkMessageTime = prop.getInt();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.MAIN_SETTINGS, "End Game Chat Message", "gg");
        prop.setMaxListLength(255);
        ConfigManager.endGameChatMessage = prop.getString();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.MAIN_SETTINGS, "Enable Armor Status Render Bug", false);
        prop.comment = I18n.format("gui.config.indicatorutils.advanced");
        ConfigManager.enableArmorStatusRenderBug = prop.getBoolean();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.MAIN_SETTINGS, "Enable Held Item Render Bug", false);
        prop.comment = I18n.format("gui.config.indicatorutils.advanced");
        ConfigManager.enableHeldItemRenderBug = prop.getBoolean();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.MAIN_SETTINGS, "Enable End Game Message", false);
        ConfigManager.enableEndGameChatMessage = prop.getBoolean();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.MAIN_SETTINGS, "Enable AFK Message", true);
        ConfigManager.enableAFKMessage = prop.getBoolean();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.MAIN_SETTINGS, "Display Armor Status and Held Item while chat is open", false);
        ConfigManager.displayArmorHeldWhileChatOpen = prop.getBoolean();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.MAIN_SETTINGS, "Highlight Potion Color", false);
        ConfigManager.highlightPotionColor = prop.getBoolean();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.MAIN_SETTINGS, "Enable Modified Movement Handler", true);
        ConfigManager.enableModifiedMovementHandler = prop.getBoolean();
        prop.comment = I18n.format("gui.config.indicatorutils.movementhandler");
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.MAIN_SETTINGS, "Enable Custom Cape Feature", false);
        ConfigManager.enableCustomCapeFeature = prop.getBoolean();
        prop.comment = I18n.format("gui.config.indicatorutils.customcape");
        prop.setRequiresMcRestart(true);
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.MAIN_SETTINGS, "Show Change Log in Game", true);
        ConfigManager.showChangeLogInGame = prop.getBoolean();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.MAIN_SETTINGS, "Enable Version Checker", true);
        ConfigManager.enableVersionChecker = prop.getBoolean();
        prop.setRequiresMcRestart(true);
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.MAIN_SETTINGS, "Mojang Status Check on Startup", true);
        ConfigManager.mojangStatusCheckOnStartup = prop.getBoolean();
        prop.setRequiresMcRestart(true);
        propOrder.add(prop.getName());
        return propOrder;
    }

    public static List<String> addRenderInfoSetting(Property prop)
    {
        List<String> propOrder = new ArrayList<String>();
        prop = ConfigManager.config.get(ConfigManager.RENDER_INFO_SETTINGS, "Swap Main Render Info to Right", false);
        ConfigManager.swapMainRenderInfoToRight = prop.getBoolean();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.RENDER_INFO_SETTINGS, "Armor Status", "PERCENT");
        prop.setValidValues(new String[] { "PERCENT", "NORMAL_1", "NORMAL_2", "NONE" });
        ConfigManager.armorStatusMode = prop.getString();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.RENDER_INFO_SETTINGS, "Health Status", "DISABLE");
        prop.setValidValues(new String[] { "DISABLE", "ALWAYS", "POINTED" });
        ConfigManager.healthStatusMode = prop.getString();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.RENDER_INFO_SETTINGS, "Keystroke Mode", "NORMAL");
        prop.setValidValues(new String[] { "NORMAL", "ADVANCED" });
        ConfigManager.keystrokeMode = prop.getString();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.RENDER_INFO_SETTINGS, "Potion Status Style", "DEFAULT");
        prop.setValidValues(new String[] { "DEFAULT", "ICON_AND_TIME" });
        ConfigManager.potionStatusStyle = prop.getString();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.RENDER_INFO_SETTINGS, "Keystroke GUI Size", "NORMAL");
        prop.setValidValues(new String[] { "NORMAL", "SMALL" });
        ConfigManager.keystrokeSize = prop.getString();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.RENDER_INFO_SETTINGS, "Held Item Status", "NORMAL");
        prop.setValidValues(new String[] { "NORMAL", "PERCENT", "NORMAL_2", "NONE" });
        ConfigManager.heldItemStatusMode = prop.getString();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.RENDER_INFO_SETTINGS, "Enable Ping", true);
        ConfigManager.enablePing = prop.getBoolean();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.RENDER_INFO_SETTINGS, "Enable Server IP", true);
        ConfigManager.enableServerIP = prop.getBoolean();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.RENDER_INFO_SETTINGS, "Enable Server IP with Minecraft Version", true);
        ConfigManager.enableServerIPWithMCVersion = prop.getBoolean();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.RENDER_INFO_SETTINGS, "Enable FPS", true);
        ConfigManager.enableFPS = prop.getBoolean();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.RENDER_INFO_SETTINGS, "Enable XYZ", true);
        ConfigManager.enableXYZ = prop.getBoolean();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.RENDER_INFO_SETTINGS, "Enable Overworld Coordinate in the Nether", true);
        ConfigManager.enableOverworldCoordinate = prop.getBoolean();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.RENDER_INFO_SETTINGS, "Enable Looking at Block", false);
        ConfigManager.enableLookingAtBlock = prop.getBoolean();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.RENDER_INFO_SETTINGS, "Enable Direction", true);
        ConfigManager.enableDirection = prop.getBoolean();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.RENDER_INFO_SETTINGS, "Enable Biome", true);
        ConfigManager.enableBiome = prop.getBoolean();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.RENDER_INFO_SETTINGS, "Enable Armor Status", true);
        ConfigManager.enableArmorStatus = prop.getBoolean();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.RENDER_INFO_SETTINGS, "Enable Potion Status", false);
        ConfigManager.enablePotionStatus = prop.getBoolean();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.RENDER_INFO_SETTINGS, "Enable Keystroke", false);
        ConfigManager.enableKeystroke = prop.getBoolean();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.RENDER_INFO_SETTINGS, "Enable LMB/RMB on Keystroke", true);
        ConfigManager.enableKeystrokeLMBRMB = prop.getBoolean();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.RENDER_INFO_SETTINGS, "Enable Sprint/Sneak on Keystroke", false);
        ConfigManager.enableKeystrokeSprintSneak = prop.getBoolean();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.RENDER_INFO_SETTINGS, "Enable Blocking on Keystroke", false);
        ConfigManager.enableKeystrokeBlocking = prop.getBoolean();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.RENDER_INFO_SETTINGS, "Enable CPS", false);
        ConfigManager.enableCPS = prop.getBoolean();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.RENDER_INFO_SETTINGS, "Enable RPS", false);
        ConfigManager.enableRPS = prop.getBoolean();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.RENDER_INFO_SETTINGS, "Enable Held Item", false);
        ConfigManager.enableHeldItemInHand = prop.getBoolean();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.RENDER_INFO_SETTINGS, "Show Potion Icon", GameInfoHelper.INSTANCE.isBelowMinecraft19() ? true : false);
        ConfigManager.showPotionIcon = prop.getBoolean();
        propOrder.add(prop.getName());
        return propOrder;
    }

    private static List<String> addIngameRenderSetting(Property prop)
    {
        List<String> propOrder = new ArrayList<String>();
        prop = ConfigManager.config.get(ConfigManager.INGAME_RENDER_SETTINGS, "Player List Ping Mode", "DEFAULT");
        prop.setValidValues(new String[] { "DEFAULT", "NUMBER" });
        ConfigManager.playerPingMode = prop.getString();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.INGAME_RENDER_SETTINGS, "Render Scoreboard", true);
        ConfigManager.renderScoreboard = prop.getBoolean();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.INGAME_RENDER_SETTINGS, "Render Boss Health Bar", true);
        ConfigManager.renderBossHealthBar = prop.getBoolean();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.INGAME_RENDER_SETTINGS, "Hide Boss Health Bar", true);
        ConfigManager.hideBossHealthBar = prop.getBoolean();
        propOrder.add(prop.getName());
        return propOrder;
    }

    private static List<String> addKeyBindingSetting(Property prop)
    {
        List<String> propOrder = new ArrayList<String>();
        prop = ConfigManager.config.get(ConfigManager.KEY_BINDING_SETTINGS, "Key Display Mode Next (Ctrl) + (Key)", "29,27");
        ConfigManager.keyDisplayModeNext = prop.getString();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.KEY_BINDING_SETTINGS, "Key Display Mode Previous (Ctrl) + (Key)", "29,26");
        ConfigManager.keyDisplayModePrevious = prop.getString();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.KEY_BINDING_SETTINGS, "Key Toggle Sprint (Ctrl) + (Key)", "29,31");
        ConfigManager.keyToggleSprint = prop.getString();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.KEY_BINDING_SETTINGS, "Key Toggle Sneak (Ctrl) + (Key)", "29,42");
        ConfigManager.keyToggleSneak = prop.getString();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.KEY_BINDING_SETTINGS, "Key Auto Swim (Ctrl) + (Key)", "29,19");
        ConfigManager.keyAutoSwim = prop.getString();
        propOrder.add(prop.getName());
        return propOrder;
    }

    public static List<String> addOffsetSetting(Property prop)
    {
        List<String> propOrder = new ArrayList<String>();
        prop = ConfigManager.config.get(ConfigManager.OFFSET_SETTINGS, "Armor Status Position", "LEFT");
        prop.setValidValues(new String[] { "LEFT", "RIGHT", "HOTBAR", "HOTBAR_LEFT", "HOTBAR_RIGHT" });
        ConfigManager.armorStatusPosition = prop.getString();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.OFFSET_SETTINGS, "Potion Status Position", "LEFT");
        prop.setValidValues(new String[] { "LEFT", "RIGHT", "HOTBAR_LEFT", "HOTBAR_RIGHT" });
        ConfigManager.potionStatusPosition = prop.getString();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.OFFSET_SETTINGS, "Keystroke Position", "RIGHT");
        prop.setValidValues(new String[] { "RIGHT", "LEFT" });
        ConfigManager.keystrokePosition = prop.getString();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.OFFSET_SETTINGS, "Potion Size", 2);
        prop.setMinValue(2).setMaxValue(8).setConfigEntryClass(NumberSliderEntry.class);
        prop.comment = I18n.format("gui.config.indicatorutils.advanced");
        ConfigManager.potionSize = prop.getInt();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.OFFSET_SETTINGS, "Potion Length Y Offset (More than default)", 45);
        prop.setMinValue(1).setMaxValue(256).setConfigEntryClass(NumberSliderEntry.class);
        prop.comment = I18n.format("gui.config.indicatorutils.advanced");
        ConfigManager.potionLengthVal = prop.getInt();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.OFFSET_SETTINGS, "Potion Length Y Offset (Default)", 23);
        prop.setMinValue(1).setMaxValue(128).setConfigEntryClass(NumberSliderEntry.class);
        prop.comment = I18n.format("gui.config.indicatorutils.advanced");
        ConfigManager.potionLength = prop.getInt();
        propOrder.add(prop.getName());
        return propOrder;
    }

    public static List<String> addTimeInfoSetting(Property prop)
    {
        List<String> propOrder = new ArrayList<String>();
        prop = ConfigManager.config.get(ConfigManager.TIME_INFO_SETTINGS, "Time Zone Name", "GMT");
        prop.setValidValues(new String[] { "GMT", "UTC", "EST", "AST", "NST", "PST", "CST", "CET", "EET", "WET" });
        ConfigManager.timeZoneName = prop.getString();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.TIME_INFO_SETTINGS, "Date Format", "TH");
        prop.setValidValues(new String[] { "TH", "US", "JP", "CN" });
        ConfigManager.dateFormat = prop.getString();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.TIME_INFO_SETTINGS, "Use Short Date", false);
        ConfigManager.useShortDate = prop.getBoolean();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.TIME_INFO_SETTINGS, "Enable Current Time", true);
        ConfigManager.enableCurrentTime = prop.getBoolean();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.TIME_INFO_SETTINGS, "Enable Game Time", true);
        ConfigManager.enableGameTime = prop.getBoolean();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.TIME_INFO_SETTINGS, "Enable Weather Status", true);
        ConfigManager.enableWeatherStatus = prop.getBoolean();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.TIME_INFO_SETTINGS, "Enable Time Zone", false);
        ConfigManager.enableTimeZone = prop.getBoolean();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.TIME_INFO_SETTINGS, "Enable Standard World Time", false);
        ConfigManager.enableStandardWorldTime = prop.getBoolean();
        propOrder.add(prop.getName());
        return propOrder;
    }

    public static List<String> addCustomColorSetting(Property prop)
    {
        List<String> propOrder = new ArrayList<String>();
        prop = ConfigManager.config.get(ConfigManager.CUSTOM_COLOR_SETTINGS, "Ping", "white");
        ConfigManager.customColorPing = prop.getString();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.CUSTOM_COLOR_SETTINGS, "Ping < 199", "green");
        ConfigManager.customColorPingValue1 = prop.getString();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.CUSTOM_COLOR_SETTINGS, "Ping >= 200 && Ping <= 300", "yellow");
        ConfigManager.customColorPingValue2 = prop.getString();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.CUSTOM_COLOR_SETTINGS, "Ping >= 301 && Ping <= 499", "red");
        ConfigManager.customColorPingValue3 = prop.getString();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.CUSTOM_COLOR_SETTINGS, "Ping >= 500", "dark_red");
        ConfigManager.customColorPingValue4 = prop.getString();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.CUSTOM_COLOR_SETTINGS, "IP", "white");
        ConfigManager.customColorIP = prop.getString();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.CUSTOM_COLOR_SETTINGS, "IP Server", "white");
        ConfigManager.customColorIPValue = prop.getString();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.CUSTOM_COLOR_SETTINGS, "IP Server MC Version", "white");
        ConfigManager.customColorIPMCValue = prop.getString();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.CUSTOM_COLOR_SETTINGS, "FPS", "white");
        ConfigManager.customColorFPS = prop.getString();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.CUSTOM_COLOR_SETTINGS, "FPS > 40", "green");
        ConfigManager.customColorFPSValue1 = prop.getString();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.CUSTOM_COLOR_SETTINGS, "FPS >= 26 && FPS <= 40", "yellow");
        ConfigManager.customColorFPSValue2 = prop.getString();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.CUSTOM_COLOR_SETTINGS, "FPS <= 25", "red");
        ConfigManager.customColorFPSValue3 = prop.getString();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.CUSTOM_COLOR_SETTINGS, "XYZ", "white");
        ConfigManager.customColorXYZ = prop.getString();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.CUSTOM_COLOR_SETTINGS, "XYZ Nether", "white");
        ConfigManager.customColorXYZNether = prop.getString();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.CUSTOM_COLOR_SETTINGS, "XYZ Overworld", "white");
        ConfigManager.customColorXYZOverworld = prop.getString();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.CUSTOM_COLOR_SETTINGS, "XYZ X Value", "white");
        ConfigManager.customColorXValue = prop.getString();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.CUSTOM_COLOR_SETTINGS, "XYZ Y Value", "white");
        ConfigManager.customColorYValue = prop.getString();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.CUSTOM_COLOR_SETTINGS, "XYZ Z Value", "white");
        ConfigManager.customColorZValue = prop.getString();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.CUSTOM_COLOR_SETTINGS, "Biome", "white");
        ConfigManager.customColorBiome = prop.getString();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.CUSTOM_COLOR_SETTINGS, "Biome Name", "white");
        ConfigManager.customColorBiomeValue = prop.getString();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.CUSTOM_COLOR_SETTINGS, "CPS", "white");
        ConfigManager.customColorCPS = prop.getString();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.CUSTOM_COLOR_SETTINGS, "CPS Value", "white");
        ConfigManager.customColorCPSValue = prop.getString();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.CUSTOM_COLOR_SETTINGS, "RPS", "white");
        ConfigManager.customColorRPS = prop.getString();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.CUSTOM_COLOR_SETTINGS, "RPS Value", "white");
        ConfigManager.customColorRPSValue = prop.getString();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.CUSTOM_COLOR_SETTINGS, "Armor Percent", "white");
        ConfigManager.customColorArmorPercent = prop.getString();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.CUSTOM_COLOR_SETTINGS, "Armor Max Durability", "white");
        ConfigManager.customColorArmorMaxDurability = prop.getString();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.CUSTOM_COLOR_SETTINGS, "Armor Damage Durability", "white");
        ConfigManager.customColorArmorDamageDurability = prop.getString();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.CUSTOM_COLOR_SETTINGS, "Time AM PM", "white");
        ConfigManager.customColorTimeAMPM = prop.getString();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.CUSTOM_COLOR_SETTINGS, "Current Time", "white");
        ConfigManager.customColorCurrentTime = prop.getString();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.CUSTOM_COLOR_SETTINGS, "Current Time Day", "white");
        ConfigManager.customColorCurrentTimeDay = prop.getString();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.CUSTOM_COLOR_SETTINGS, "Current Time Month", "white");
        ConfigManager.customColorCurrentTimeMonth = prop.getString();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.CUSTOM_COLOR_SETTINGS, "Current Time Year", "white");
        ConfigManager.customColorCurrentTimeYear = prop.getString();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.CUSTOM_COLOR_SETTINGS, "Current Time Value", "white");
        ConfigManager.customColorCurrentTimeValue = prop.getString();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.CUSTOM_COLOR_SETTINGS, "Game Time", "white");
        ConfigManager.customColorGameTime = prop.getString();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.CUSTOM_COLOR_SETTINGS, "Game Time Value", "white");
        ConfigManager.customColorGameTimeValue = prop.getString();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.CUSTOM_COLOR_SETTINGS, "Time Zone", "white");
        ConfigManager.customColorTimeZone = prop.getString();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.CUSTOM_COLOR_SETTINGS, "Time Zone Value", "white");
        ConfigManager.customColorTimeZoneValue = prop.getString();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.CUSTOM_COLOR_SETTINGS, "World Time", "white");
        ConfigManager.customColorWorldTime = prop.getString();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.CUSTOM_COLOR_SETTINGS, "Looking at", "white");
        ConfigManager.customColorLookingAt = prop.getString();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.CUSTOM_COLOR_SETTINGS, "Direction", "white");
        ConfigManager.customColorDirection = prop.getString();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.CUSTOM_COLOR_SETTINGS, "Direction Value", "white");
        ConfigManager.customColorDirectionValue = prop.getString();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.CUSTOM_COLOR_SETTINGS, "Weather Prefix", "white");
        ConfigManager.customColorWeatherPrefix = prop.getString();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.CUSTOM_COLOR_SETTINGS, "Raining", "white");
        ConfigManager.customColorRaining = prop.getString();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.CUSTOM_COLOR_SETTINGS, "Thunder", "white");
        ConfigManager.customColorThunder = prop.getString();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.CUSTOM_COLOR_SETTINGS, "Held Item", "white");
        ConfigManager.customColorHeldItem = prop.getString();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.CUSTOM_COLOR_SETTINGS, "Held Item Arrow Count", "white");
        ConfigManager.customColorHeldItemArrowCount = prop.getString();
        propOrder.add(prop.getName());
        return propOrder;
    }

    public static List<String> addCustomTextSetting(Property prop)
    {
        List<String> propOrder = new ArrayList<String>();
        prop = ConfigManager.config.get(ConfigManager.CUSTOM_TEXT_SETTINGS, "Use Custom Ping Text", false);
        ConfigManager.useCustomTextPing = prop.getBoolean();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.CUSTOM_TEXT_SETTINGS, "Ping Text", "\"text\":\"Ping: \"");
        ConfigManager.customTextPing = prop.getString();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.CUSTOM_TEXT_SETTINGS, "Use Custom IP Text", false);
        ConfigManager.useCustomTextIP = prop.getBoolean();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.CUSTOM_TEXT_SETTINGS, "IP Text", "\"text\":\"IP: \"");
        ConfigManager.customTextIP = prop.getString();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.CUSTOM_TEXT_SETTINGS, "Realms Text", "\"text\":\"[Realms Server]\"");
        ConfigManager.customTextRealms = prop.getString();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.CUSTOM_TEXT_SETTINGS, "Use Custom FPS Text", false);
        ConfigManager.useCustomTextFPS = prop.getBoolean();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.CUSTOM_TEXT_SETTINGS, "FPS Text", "\"text\":\"FPS: \"");
        ConfigManager.customTextFPS = prop.getString();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.CUSTOM_TEXT_SETTINGS, "Use Custom XYZ Text", false);
        ConfigManager.useCustomTextXYZ = prop.getBoolean();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.CUSTOM_TEXT_SETTINGS, "XYZ Text", "\"text\":\"XYZ: \"");
        ConfigManager.customTextXYZ = prop.getString();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.CUSTOM_TEXT_SETTINGS, "XYZ Nether Text", "\"text\":\"Nether \"");
        ConfigManager.customTextXYZNether = prop.getString();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.CUSTOM_TEXT_SETTINGS, "XYZ Overworld Text", "\"text\":\"Overworld \"");
        ConfigManager.customTextXYZOverworld = prop.getString();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.CUSTOM_TEXT_SETTINGS, "Use Custom Biome Text", false);
        ConfigManager.useCustomTextBiome = prop.getBoolean();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.CUSTOM_TEXT_SETTINGS, "Biome Text", "\"text\":\"Biome: \"");
        ConfigManager.customTextBiome = prop.getString();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.CUSTOM_TEXT_SETTINGS, "Use Custom CPS Text", false);
        ConfigManager.useCustomTextCPS = prop.getBoolean();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.CUSTOM_TEXT_SETTINGS, "CPS Text", "\"text\":\"CPS: \"");
        ConfigManager.customTextCPS = prop.getString();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.CUSTOM_TEXT_SETTINGS, "Use Custom RPS Text", false);
        ConfigManager.useCustomTextRPS = prop.getBoolean();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.CUSTOM_TEXT_SETTINGS, "RPS Text", "\"text\":\" RPS: \"");
        ConfigManager.customTextRPS = prop.getString();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.CUSTOM_TEXT_SETTINGS, "Use Custom Time Text", false);
        ConfigManager.useCustomTextTime = prop.getBoolean();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.CUSTOM_TEXT_SETTINGS, "Time Text", "\"text\":\"Time: \"");
        ConfigManager.customTextTime = prop.getString();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.CUSTOM_TEXT_SETTINGS, "Use Custom Game Time Text", false);
        ConfigManager.useCustomTextGameTime = prop.getBoolean();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.CUSTOM_TEXT_SETTINGS, "Game Time Text", "\"text\":\"Game Time: \"");
        ConfigManager.customTextGameTime = prop.getString();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.CUSTOM_TEXT_SETTINGS, "Use Custom Time Zone Text", false);
        ConfigManager.useCustomTextTimeZone = prop.getBoolean();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.CUSTOM_TEXT_SETTINGS, "Time Zone Text", "\"text\":\"Time Zone: \"");
        ConfigManager.customTextTimeZone = prop.getString();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.CUSTOM_TEXT_SETTINGS, "Use Custom Looking at Text", false);
        ConfigManager.useCustomTextLookingAt = prop.getBoolean();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.CUSTOM_TEXT_SETTINGS, "Looking at Text", "\"text\":\"Looking at: \"");
        ConfigManager.customTextLookingAt = prop.getString();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.CUSTOM_TEXT_SETTINGS, "Use Custom Direction Text", false);
        ConfigManager.useCustomTextDirection = prop.getBoolean();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.CUSTOM_TEXT_SETTINGS, "Direction Text", "\"text\":\"Direction: \"");
        ConfigManager.customTextDirection = prop.getString();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.CUSTOM_TEXT_SETTINGS, "Use Weather Text", false);
        ConfigManager.useCustomWeather = prop.getBoolean();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.CUSTOM_TEXT_SETTINGS, "Weather Prefix Text", "\"text\":\" | \"");
        ConfigManager.customTextWeather = prop.getString();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.CUSTOM_TEXT_SETTINGS, "Raining Text", "\"text\":\"Raining\"");
        ConfigManager.customTextRaining = prop.getString();
        propOrder.add(prop.getName());

        prop = ConfigManager.config.get(ConfigManager.CUSTOM_TEXT_SETTINGS, "Thunder Text", "\"text\":\"Thunder\"");
        ConfigManager.customTextThunder = prop.getString();
        propOrder.add(prop.getName());
        return propOrder;
    }

    public static List<IConfigElement> getConfigElements()
    {
        List<IConfigElement> list = new ArrayList<IConfigElement>();
        list.add(new ConfigElement(ConfigManager.config.getCategory(ConfigManager.MAIN_SETTINGS)));
        list.add(new ConfigElement(ConfigManager.config.getCategory(ConfigManager.RENDER_INFO_SETTINGS)));
        list.add(new ConfigElement(ConfigManager.config.getCategory(ConfigManager.INGAME_RENDER_SETTINGS)));
        list.add(new DummyConfigElement("Key Code Example", "http://minecraft.gamepedia.com/Key_codes", ConfigGuiType.STRING, "keycodeexample"));
        list.add(new ConfigElement(ConfigManager.config.getCategory(ConfigManager.KEY_BINDING_SETTINGS)));
        list.add(new ConfigElement(ConfigManager.config.getCategory(ConfigManager.OFFSET_SETTINGS)));
        list.add(new ConfigElement(ConfigManager.config.getCategory(ConfigManager.TIME_INFO_SETTINGS)));

        ConfigCategory configCustomColor = ConfigManager.config.getCategory(ConfigManager.CUSTOM_COLOR_SETTINGS);
        configCustomColor.setComment(I18n.format("gui.config.indicatorutils.advanced"));
        list.add(new ConfigElement(configCustomColor));
        ConfigCategory configCustomText = ConfigManager.config.getCategory(ConfigManager.CUSTOM_TEXT_SETTINGS);
        configCustomText.setComment(I18n.format("gui.config.indicatorutils.advanced"));
        list.add(new ConfigElement(configCustomText));
        return list;
    }
}