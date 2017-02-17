/*******************************************************************************
 *
 * Copyright 2016 Wasinthorn Suksri/SteveKunG - Indicator Utils
 *
 ******************************************************************************/

package stevekung.mods.indicatorutils.helper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.IChatComponent;
import stevekung.mods.indicatorutils.IndicatorUtils;
import stevekung.mods.indicatorutils.config.ConfigManager;
import stevekung.mods.indicatorutils.handler.IndicatorUtilsEventHandler;
import stevekung.mods.indicatorutils.utils.JsonUtils;

public class GameInfoHelper
{
    public static GameInfoHelper INSTANCE = new GameInfoHelper();

    public int getPing()
    {
        return Minecraft.getMinecraft().getNetHandler().getPlayerInfo(Minecraft.getMinecraft().thePlayer.getUniqueID()).getResponseTime();
    }

    public boolean isHypixel()
    {
        return Minecraft.getMinecraft().getCurrentServerData() != null && Minecraft.getMinecraft().getCurrentServerData().serverIP.contains("hypixel");
    }

    public boolean isMineplex()
    {
        return Minecraft.getMinecraft().getCurrentServerData() != null && Minecraft.getMinecraft().getCurrentServerData().serverIP.contains("mineplex");
    }

    public List<String> getPlayerInfoListClient()
    {
        NetHandlerPlayClient connection = Minecraft.getMinecraft().thePlayer.sendQueue;
        List<NetworkPlayerInfo> playerInfo = new ArrayList(connection.getPlayerInfoMap());
        List<String> playerList = Lists.<String>newArrayList();

        for (int i = 0; i < playerInfo.size(); ++i)
        {
            if (i < playerInfo.size())
            {
                playerList.add(playerInfo.get(i).getGameProfile().getName());
            }
        }
        return playerList;
    }

    public void setActionBarMessage(String message, boolean isPlaying)
    {
        Minecraft.getMinecraft().ingameGUI.setRecordPlaying(message, isPlaying);
    }

    public void setActionBarMessage(IChatComponent component, boolean isPlaying)
    {
        Minecraft.getMinecraft().ingameGUI.setRecordPlaying(component, isPlaying);
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

    public int getRPS()
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

    public String[] getColorCode()
    {
        return new String[] {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};
    }

    public String[] getJsonColor()
    {
        return new String[] {"black", "dark_blue", "dark_green", "dark_aqua", "dark_red", "dark_purple", "gold", "gray", "dark_gray", "blue", "green", "aqua", "red", "light_purple", "yellow", "white"};
    }
}