package stevekung.mods.indicatorutils.helper;

import org.apache.logging.log4j.Level;

import cpw.mods.fml.common.FMLLog;

public class OldMethodHelper
{
    public static void exitJava(int exitCode, boolean hardExit)
    {
        FMLLog.log(Level.INFO, "Java has been asked to exit (code %d) by %s.", exitCode, Thread.currentThread().getStackTrace()[1]);

        if (hardExit)
        {
            FMLLog.log(Level.INFO, "This is an abortive exit and could cause world corruption or other things");
        }
        if (Boolean.parseBoolean(System.getProperty("fml.debugExit", "false")))
        {
            FMLLog.log(Level.INFO, new Throwable(), "Exit trace");
        }
        else
        {
            FMLLog.log(Level.INFO, "If this was an unexpected exit, use -Dfml.debugExit=true as a JVM argument to find out where it was called");
        }
        if (hardExit)
        {
            Runtime.getRuntime().halt(exitCode);
        }
        else
        {
            Runtime.getRuntime().exit(exitCode);
        }
    }
}