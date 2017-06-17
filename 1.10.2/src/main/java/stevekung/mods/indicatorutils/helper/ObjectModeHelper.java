package stevekung.mods.indicatorutils.helper;

import stevekung.mods.indicatorutils.config.ConfigManager;

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
}