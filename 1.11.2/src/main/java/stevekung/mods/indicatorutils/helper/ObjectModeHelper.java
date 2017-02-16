/*******************************************************************************
 *
 * Copyright 2016 Wasinthorn Suksri/SteveKunG - Indicator Utils
 *
 ******************************************************************************/

package stevekung.mods.indicatorutils.helper;

import stevekung.mods.indicatorutils.config.ConfigManager;
import stevekung.mods.indicatorutils.config.ExtendedModSettings;

public class ObjectModeHelper
{
    public static boolean getArmorStatusMode(ArmorStatusPosition pos)
    {
        switch (pos)
        {
        case LEFT:
        default:
            return ConfigManager.armorStatusPosition.equalsIgnoreCase("LEFT");
        case RIGHT:
            return ConfigManager.armorStatusPosition.equalsIgnoreCase("RIGHT");
        case HOTBAR:
            return ConfigManager.armorStatusPosition.equalsIgnoreCase("HOTBAR");
        case HOTBAR_LEFT:
            return ConfigManager.armorStatusPosition.equalsIgnoreCase("HOTBAR_LEFT");
        case HOTBAR_RIGHT:
            return ConfigManager.armorStatusPosition.equalsIgnoreCase("HOTBAR_RIGHT");
        }
    }

    public static boolean getPotionStatusMode(PotionStatusPosition pos)
    {
        switch (pos)
        {
        case LEFT:
        default:
            return ConfigManager.potionStatusPosition.equalsIgnoreCase("LEFT");
        case RIGHT:
            return ConfigManager.potionStatusPosition.equalsIgnoreCase("RIGHT");
        case HOTBAR_LEFT:
            return ConfigManager.potionStatusPosition.equalsIgnoreCase("HOTBAR_LEFT");
        case HOTBAR_RIGHT:
            return ConfigManager.potionStatusPosition.equalsIgnoreCase("HOTBAR_RIGHT");
        }
    }

    public static boolean getDisplayMode(EnumDisplayMode mode)
    {
        switch (mode)
        {
        case DEFAULT:
        default:
            return ExtendedModSettings.DISPLAY_MODE.equalsIgnoreCase("default");
        case UHC:
            return ExtendedModSettings.DISPLAY_MODE.equalsIgnoreCase("uhc");
        case PVP:
            return ExtendedModSettings.DISPLAY_MODE.equalsIgnoreCase("pvp");
        case COMMAND_BLOCK:
            return ExtendedModSettings.DISPLAY_MODE.equalsIgnoreCase("command");
        }
    }

    public static enum ArmorStatusPosition
    {
        LEFT,
        RIGHT,
        HOTBAR,
        HOTBAR_LEFT,
        HOTBAR_RIGHT;
    }

    public static enum PotionStatusPosition
    {
        LEFT,
        RIGHT,
        HOTBAR_LEFT,
        HOTBAR_RIGHT;
    }

    public static enum EnumDisplayMode
    {
        DEFAULT,
        UHC,
        PVP,
        COMMAND_BLOCK;
    }
}