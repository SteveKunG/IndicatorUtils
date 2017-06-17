package stevekung.mods.indicatorutils.helper;

public class OffsetHelper
{
    public static int getHotbarArmorOffset(boolean text, EnumSide side)
    {
        if (side == EnumSide.LEFT_AND_RIGHT)
        {
            if (text)
            {
                if (GameInfoHelper.INSTANCE.isBelowMinecraft19())
                {
                    return 72;
                }
                else
                {
                    return 84;
                }
            }
            else
            {
                if (GameInfoHelper.INSTANCE.isBelowMinecraft19())
                {
                    return 76;
                }
                else
                {
                    return 88;
                }
            }
        }
        else
        {
            if (text)
            {
                if (GameInfoHelper.INSTANCE.isBelowMinecraft19())
                {
                    return 36;
                }
                else
                {
                    return 52;
                }
            }
            else
            {
                if (GameInfoHelper.INSTANCE.isBelowMinecraft19())
                {
                    return 40;
                }
                else
                {
                    return 56;
                }
            }
        }
    }

    public static int getHotbarPotionOffset()
    {
        if (GameInfoHelper.INSTANCE.isBelowMinecraft19())
        {
            return 36;
        }
        else
        {
            return 46;
        }
    }

    public static enum EnumSide
    {
        LEFT_AND_RIGHT,
        HOTBAR;
    }
}