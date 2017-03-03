/*******************************************************************************
 *
 * Copyright 2016 Wasinthorn Suksri/SteveKunG - Indicator Utils
 *
 ******************************************************************************/

package stevekung.mods.indicatorutils.utils;

public class InvalidUUIDException extends RuntimeException
{
    private static final long serialVersionUID = 6188339323554674291L;

    public InvalidUUIDException(String uuid)
    {
        super("Invalid UUID " + "\"" + uuid + "\"");
    }

    public InvalidUUIDException(String username, String uuid)
    {
        super("Invalid UUID and Username " + "\"" + username + ":" + uuid + "\"" + ", only " + username + " can use this mod!");
    }
}