/*******************************************************************************
 *
 * Copyright 2016 Wasinthorn Suksri/SteveKunG - Indicator Utils
 *
 ******************************************************************************/

package stevekung.mods.indicatorutils.utils;

import org.apache.logging.log4j.Level;

import cpw.mods.fml.relauncher.FMLRelaunchLog;

public class IULog
{
    public static void info(String message)
    {
        FMLRelaunchLog.log("Indicator Utils", Level.INFO, message);
    }

    public static void error(String message)
    {
        FMLRelaunchLog.log("Indicator Utils", Level.ERROR, message);
    }

    public static void warning(String message)
    {
        FMLRelaunchLog.log("Indicator Utils", Level.WARN, message);
    }

    public static void info(String message, Object... obj)
    {
        FMLRelaunchLog.log("Indicator Utils", Level.INFO, message, obj);
    }

    public static void error(String message, Object... obj)
    {
        FMLRelaunchLog.log("Indicator Utils", Level.ERROR, message, obj);
    }

    public static void warning(String message, Object... obj)
    {
        FMLRelaunchLog.log("Indicator Utils", Level.WARN, message, obj);
    }
}