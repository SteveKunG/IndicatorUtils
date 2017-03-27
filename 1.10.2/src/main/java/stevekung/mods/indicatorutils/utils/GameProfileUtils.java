/*******************************************************************************
 *
 * Copyright 2016 Wasinthorn Suksri/SteveKunG - Indicator Utils
 *
 ******************************************************************************/

package stevekung.mods.indicatorutils.utils;

import java.util.UUID;

import net.minecraft.client.Minecraft;

public class GameProfileUtils
{
    public static String getUsername()
    {
        return Minecraft.getMinecraft().getSession().getProfile().getName();
    }

    public static UUID getUUID()
    {
        return Minecraft.getMinecraft().getSession().getProfile().getId();
    }
}