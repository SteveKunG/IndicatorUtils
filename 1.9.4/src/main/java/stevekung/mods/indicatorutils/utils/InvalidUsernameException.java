/*******************************************************************************
 *
 * Copyright 2016 Wasinthorn Suksri/SteveKunG - Indicator Utils
 *
 ******************************************************************************/

package stevekung.mods.indicatorutils.utils;

public class InvalidUsernameException extends RuntimeException
{
    public InvalidUsernameException(String username)
    {
        super("Invalid Username " + "\"" + username + "\"" + ", only " + username + " can use this mod!");
    }
}