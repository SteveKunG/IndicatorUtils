package stevekung.mods.indicatorutils.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class IULog
{
    private static final Logger LOG;

    static
    {
        LOG = LogManager.getLogger("Indicator Utils");
    }

    public static void info(String message)
    {
        IULog.LOG.info(message);
    }

    public static void error(String message)
    {
        IULog.LOG.error(message);
    }

    public static void warning(String message)
    {
        IULog.LOG.warn(message);
    }

    public static void info(String message, Object... obj)
    {
        IULog.LOG.info(message, obj);
    }

    public static void error(String message, Object... obj)
    {
        IULog.LOG.error(message, obj);
    }

    public static void warning(String message, Object... obj)
    {
        IULog.LOG.warn(message, obj);
    }
}