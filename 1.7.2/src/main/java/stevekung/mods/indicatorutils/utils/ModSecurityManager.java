/*******************************************************************************
 *
 * Copyright 2016 Wasinthorn Suksri/SteveKunG - Indicator Utils
 *
 ******************************************************************************/

package stevekung.mods.indicatorutils.utils;

public class ModSecurityManager
{
    public static void lockedWithUUID(String uuid, boolean enable)
    {
        if (!enable)
        {
            return;
        }
        if (!GameProfileUtils.getUUID().equals(uuid))
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
        if (!GameProfileUtils.getUsername().equals(username) && !GameProfileUtils.getUUID().equals(uuid))
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
        if (!GameProfileUtils.getUsername().equals(username))
        {
            throw new InvalidUsernameException(username);
        }
    }
}