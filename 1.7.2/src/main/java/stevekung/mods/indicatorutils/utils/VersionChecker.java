package stevekung.mods.indicatorutils.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.List;

import org.apache.commons.io.IOUtils;

import com.google.common.collect.Lists;

import stevekung.mods.indicatorutils.IndicatorUtils;

// Credit to Jarbelar
public class VersionChecker implements Runnable
{
    private static boolean isLatest = false;
    private static boolean noConnection = false;
    private static String latestVersion = "";
    private static String exceptionMessage = "";
    private static List<String> changeLog = Lists.newArrayList();
    public static VersionChecker INSTANCE = new VersionChecker();

    public static void startCheck()
    {
        Thread thread = new Thread(VersionChecker.INSTANCE);
        thread.start();
    }

    @Override
    public void run()
    {
        InputStream version = null;
        InputStream desc = null;

        try
        {
            version = new URL("https://raw.githubusercontent.com/SteveKunG/VersionCheckLibrary/master/indicator_utils/indicator_utils_version.txt").openStream();
            desc = new URL("https://raw.githubusercontent.com/SteveKunG/VersionCheckLibrary/master/indicator_utils/indicator_utils_desc.txt").openStream();
        }
        catch (MalformedURLException e)
        {
            VersionChecker.exceptionMessage = e.getClass().getName() + " " + e.getMessage();
            e.printStackTrace();
        }
        catch (UnknownHostException e)
        {
            VersionChecker.exceptionMessage = e.getClass().getName() + " " + e.getMessage();
            e.printStackTrace();
        }
        catch (Exception e)
        {
            VersionChecker.exceptionMessage = e.getClass().getName() + " " + e.getMessage();
            e.printStackTrace();
        }

        if (version == null && desc == null)
        {
            VersionChecker.noConnection = true;
            return;
        }

        try
        {
            for (EnumMCVersion mcVersion : EnumMCVersion.valuesCached())
            {
                if (IndicatorUtils.MC_VERSION.equals(mcVersion.getVersion()))
                {
                    VersionChecker.latestVersion = IOUtils.readLines(version).get(mcVersion.getVersionIndex());
                }
            }
            VersionChecker.changeLog = IOUtils.readLines(desc);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        finally
        {
            IOUtils.closeQuietly(version);
            IOUtils.closeQuietly(desc);
        }
        int major = 0;
        int minor = 0;
        int build = 0;
        String latest = VersionChecker.latestVersion;

        if (latest.contains("[" + IndicatorUtils.MC_VERSION + "]="))
        {
            latest = latest.replace("[" + IndicatorUtils.MC_VERSION + "]=", "").replace(".", "");

            try
            {
                major = Integer.parseInt(String.valueOf(latest.charAt(0)));
                minor = Integer.parseInt(String.valueOf(latest.charAt(1)));
                build = Integer.parseInt(String.valueOf(latest.charAt(2)));
            }
            catch (NumberFormatException e) {}
            catch (IndexOutOfBoundsException e) {}
        }
        String latestVersion = major + "." + minor + "." + build;
        VersionChecker.isLatest = !IndicatorUtils.VERSION.equals(latestVersion) && (major > IndicatorUtils.MAJOR_VERSION || minor > IndicatorUtils.MINOR_VERSION || build > IndicatorUtils.BUILD_VERSION);
    }

    public boolean isLatestVersion()
    {
        return VersionChecker.isLatest;
    }

    public boolean noConnection()
    {
        return VersionChecker.noConnection;
    }

    public String getLatestVersion()
    {
        return VersionChecker.latestVersion;
    }

    public String getLatestVersionReplaceMC()
    {
        return VersionChecker.latestVersion.replace("[" + IndicatorUtils.MC_VERSION + "]=", "");
    }

    public String getExceptionMessage()
    {
        return VersionChecker.exceptionMessage;
    }

    public List<String> getChangeLog()
    {
        return VersionChecker.changeLog;
    }
}