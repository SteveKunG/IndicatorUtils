/*******************************************************************************
 *
 * Copyright 2016 Wasinthorn Suksri/SteveKunG - Indicator Utils
 *
 ******************************************************************************/

package stevekung.mods.indicatorutils.utils;

import java.awt.Color;

public enum EnumTextColor
{
    WHITE(16777215),
    GREEN(5635925),
    YELLOW(16777045),
    RED(16733525),
    DARK_RED(11141120),
    BLACK(0),
    RAINBOW(0),
    CUSTOM(0),
    ABSORPTION(EnumTextColor.rgbToDecimal(247, 219, 21)),
    REGENERATION(EnumTextColor.rgbToDecimal(244, 120, 226)),
    STRENGTH(EnumTextColor.rgbToDecimal(179, 55, 55)),
    SPEED(EnumTextColor.rgbToDecimal(120, 201, 224)),
    FIRE_RESISTANCE(EnumTextColor.rgbToDecimal(233, 157, 73)),
    RESISTANCE(EnumTextColor.rgbToDecimal(137, 140, 154)),
    JUMP_BOOST(EnumTextColor.rgbToDecimal(33, 251, 75)),
    NIGHT_VISION(EnumTextColor.rgbToDecimal(97, 97, 224)),
    WATER_BREATHING(EnumTextColor.rgbToDecimal(79, 122, 202)),
    SLOWNESS(EnumTextColor.rgbToDecimal(103, 123, 146)),
    HASTE(EnumTextColor.rgbToDecimal(182, 169, 80)),
    MINING_FATIGUE(EnumTextColor.rgbToDecimal(90, 81, 29)),
    NAUSEA(EnumTextColor.rgbToDecimal(125, 43, 108)),
    INVISIBILITY(EnumTextColor.rgbToDecimal(139, 142, 156)),
    BLINDNESS(EnumTextColor.rgbToDecimal(90, 90, 90)),
    HUNGER(EnumTextColor.rgbToDecimal(99, 133, 92)),
    WEAKNESS(EnumTextColor.rgbToDecimal(102, 108, 102)),
    POISON(EnumTextColor.rgbToDecimal(81, 152, 50)),
    WITHER(EnumTextColor.rgbToDecimal(105, 84, 80)),
    HEALTH_BOOST(EnumTextColor.rgbToDecimal(245, 124, 35)),
    GLOWING(EnumTextColor.rgbToDecimal(146, 158, 96)),
    LEVITATION(EnumTextColor.rgbToDecimal(204, 252, 252)),
    LUCK(EnumTextColor.rgbToDecimal(50, 151, 0)),
    UNLUCK(EnumTextColor.rgbToDecimal(190, 162, 76));

    private int color;

    private EnumTextColor(int color)
    {
        this.color = color;
    }

    public int getColor()
    {
        return this.color;
    }

    public EnumTextColor setRainbow()
    {
        this.color = Math.abs(Color.HSBtoRGB(System.currentTimeMillis() % 2500L / 2500.0F, 0.8F, 0.8F));
        return this;
    }

    public EnumTextColor setColor(int color)
    {
        this.color = color;
        return this;
    }

    public static int rgbToDecimal(int r, int g, int b)
    {
        return b + 256 * g + 65536 * r;
    }
}