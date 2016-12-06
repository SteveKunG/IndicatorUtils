/*******************************************************************************
 *
 * Copyright 2016 Wasinthorn Suksri/SteveKunG - Indicator Utils
 *
 ******************************************************************************/

package stevekung.mods.indicatorutils.utils;

import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;

public class ModSecurityManager
{
    public static void lockedWithUUID(String uuid, boolean enable)
    {
        if (!enable)
        {
            return;
        }

        Minecraft mc = Minecraft.getMinecraft();
        Session session = mc.getSession();

        if (!session.func_148256_e().getId().equals(UUID.fromString(uuid)))
        {
            throw new InvalidUUIDException(uuid);
        }
    }

    public static void lockedWithUsernameAndUUID(String username, String uuid, boolean enable)
    {
        if (!enable)
        {
            return;
        }

        Minecraft mc = Minecraft.getMinecraft();
        Session session = mc.getSession();

        if (!session.getUsername().equals(username) && !session.func_148256_e().getId().equals(UUID.fromString(uuid)))
        {
            throw new InvalidUUIDException(username, uuid);
        }
    }

    public static void lockedWithPirateUser(String username, boolean enable)
    {
        if (!enable)
        {
            return;
        }

        Minecraft mc = Minecraft.getMinecraft();
        Session session = mc.getSession();

        if (!session.getUsername().equals(username))
        {
            throw new InvalidUsernameException(username);
        }
    }
}