package stevekung.mods.indicatorutils.utils;

import net.minecraft.client.Minecraft;

public class GameProfileUtils
{
    public static String getUsername()
    {
        return Minecraft.getMinecraft().getSession().func_148256_e().getName();
    }

    public static String getUUID()
    {
        return Minecraft.getMinecraft().getSession().func_148256_e().getId();
    }
}