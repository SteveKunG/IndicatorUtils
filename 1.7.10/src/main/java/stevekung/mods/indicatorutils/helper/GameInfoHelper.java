/*******************************************************************************
 *
 * Copyright 2016 Wasinthorn Suksri/SteveKunG - Indicator Utils
 *
 ******************************************************************************/

package stevekung.mods.indicatorutils.helper;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import stevekung.mods.indicatorutils.IndicatorUtils;
import stevekung.mods.indicatorutils.config.ConfigManager;
import stevekung.mods.indicatorutils.config.ExtendedModSettings;
import stevekung.mods.indicatorutils.handler.IndicatorUtilsEventHandler;
import stevekung.mods.indicatorutils.utils.GameProfileUtils;
import stevekung.mods.indicatorutils.utils.JsonUtils;

public class GameInfoHelper
{
    public static GameInfoHelper INSTANCE = new GameInfoHelper();

    public int getPing()
    {
        for (Entry<String, Integer> entry : IndicatorUtilsEventHandler.playerPingMap.entrySet())
        {
            if (entry.getKey().contains(GameProfileUtils.getUsername()))
            {
                return entry.getValue();
            }
        }
        return 0;
    }

    public void setActionBarMessage(String message, boolean isPlaying)
    {
        Minecraft.getMinecraft().ingameGUI.func_110326_a(message, isPlaying);
    }

    public void setActionBarMessage(IChatComponent component, boolean isPlaying)
    {
        Minecraft.getMinecraft().ingameGUI.func_110326_a(component.getFormattedText(), isPlaying);
    }

    // Credit to lib24time (Bukkit)
    public String getInGameTime(long worldTick)
    {
        JsonUtils json = new JsonUtils();
        int hours = (int)((worldTick / 1000 + 6) % 24);
        int minutes = (int)(60 * (worldTick % 1000) / 1000);
        String sminutes = "" + minutes;
        String shours = "" + hours;

        if (hours <= 9)
        {
            shours = 0 + "" + hours;
        }
        if (minutes <= 9)
        {
            sminutes = 0 + "" + minutes;
        }

        String AMPM = json.text(hours >= 12 ? "PM" : "AM").setChatStyle(json.colorFromConfig(ConfigManager.customColorTimeAMPM)).getFormattedText();
        String game = json.text("Game: ").setChatStyle(json.colorFromConfig(ConfigManager.customColorGameTime)).getFormattedText();
        String value = json.text(shours + ":" + sminutes).setChatStyle(json.colorFromConfig(ConfigManager.customColorGameTimeValue)).getFormattedText();

        if (ConfigManager.useCustomTextGameTime)
        {
            game = JsonUtils.rawTextToJson(ConfigManager.customTextGameTime).getFormattedText();
        }
        return game + value + " " + AMPM;
    }

    public int getCPS()
    {
        Iterator<Long> iterator = IndicatorUtilsEventHandler.L_CLICK.iterator();

        while (iterator.hasNext())
        {
            if (iterator.next().longValue() < System.currentTimeMillis() - 1000L)
            {
                iterator.remove();
            }
        }
        return IndicatorUtilsEventHandler.L_CLICK.size();
    }

    public int getRCPS()
    {
        Iterator<Long> iterator = IndicatorUtilsEventHandler.R_CLICK.iterator();

        while (iterator.hasNext())
        {
            if (iterator.next().longValue() < System.currentTimeMillis() - 1000L)
            {
                iterator.remove();
            }
        }
        return IndicatorUtilsEventHandler.R_CLICK.size();
    }

    public boolean isBelowMinecraft19()
    {
        return IndicatorUtils.MC_VERSION.equals("1.7.2") || IndicatorUtils.MC_VERSION.equals("1.7.10") || IndicatorUtils.MC_VERSION.equals("1.8.9");
    }

    public int parseInt(String input, String type)
    {
        try
        {
            return Integer.parseInt(input);
        }
        catch (NumberFormatException e)
        {
            Minecraft.getMinecraft().thePlayer.addChatMessage(new JsonUtils().text(I18n.format("commands.generic.num.invalid", input) + " in " + type + " setting").setChatStyle(new JsonUtils().red()));
            return 0;
        }
    }

    public String ticksToElapsedTime(int ticks)
    {
        int i = ticks / 20;
        int j = i / 60;
        i = i % 60;
        return i < 10 ? j + ":0" + i : j + ":" + i;
    }

    public String getMoonPhase(Minecraft mc)
    {
        int[] moonPhaseFactors = new int[] {4, 3, 2, 1, 0, -1, -2, -3};
        int phase = moonPhaseFactors[mc.theWorld.provider.getMoonPhase(mc.theWorld.getWorldTime())];
        JsonUtils json = new JsonUtils();
        String status;

        switch (phase)
        {
        case 4:
        default:
            status = "Full Moon";
            break;
        case 3:
            status = "Waning Gibbous";
            break;
        case 2:
            status = "Last Quarter";
            break;
        case 1:
            status = "Waning Crescent";
            break;
        case 0:
            status = "New Moon";
            break;
        case -1:
            status = "Waxing Crescent";
            break;
        case -2:
            status = "First Quarter";
            break;
        case -3:
            status = "Waxing Gibbous";
            break;
        }

        String moonPhaseText = json.text("Moon Phase: ").setChatStyle(json.colorFromConfig(ConfigManager.customColorMoonPhase)).getFormattedText();
        String moonPhaseStatusText = json.text(status).setChatStyle(json.colorFromConfig(ConfigManager.customColorMoonPhaseStatus)).getFormattedText();

        if (ConfigManager.useCustomMoonPhaseText)
        {
            moonPhaseText = JsonUtils.rawTextToJson(ConfigManager.customTextMoonPhase).getFormattedText();
        }
        return moonPhaseText + moonPhaseStatusText;
    }

    public String[] getColorCode()
    {
        return new String[] {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};
    }

    public String[] getJsonColor()
    {
        return new String[] {"black", "dark_blue", "dark_green", "dark_aqua", "dark_red", "dark_purple", "gold", "gray", "dark_gray", "blue", "green", "aqua", "red", "light_purple", "yellow", "white"};
    }

    public boolean isSlimeChunk(int chunkX, int chunkZ)
    {
        int x = MathHelper.bucketInt(chunkX, 16);
        int z = MathHelper.bucketInt(chunkZ, 16);
        Random rnd = new Random(ExtendedModSettings.SLIME_CHUNK_SEED + x * x * 4987142 + x * 5947611 + z * z * 4392871L + z * 389711 ^ 987234911L);
        return rnd.nextInt(10) == 0;
    }

    public boolean isHalfScreen(Minecraft mc)
    {
        return mc.displayWidth >= 1280 && mc.displayHeight >= 720;
    }
}