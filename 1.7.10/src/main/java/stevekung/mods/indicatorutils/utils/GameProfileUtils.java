package stevekung.mods.indicatorutils.utils;

import java.util.UUID;

import net.minecraft.client.Minecraft;

public class GameProfileUtils
{
    public static String getUsername()
    {
        return Minecraft.getMinecraft().getSession().func_148256_e().getName();
    }

    public static UUID getUUID()
    {
        return Minecraft.getMinecraft().getSession().func_148256_e().getId();
    }
}