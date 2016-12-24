/*******************************************************************************
 *
 * Copyright 2016 Wasinthorn Suksri/SteveKunG - Indicator Utils
 *
 ******************************************************************************/

package stevekung.mods.indicatorutils.utils.helper;

import java.util.Iterator;
import java.util.List;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.AxisAlignedBB;
import stevekung.mods.indicatorutils.ConfigManager;
import stevekung.mods.indicatorutils.IndicatorUtils;
import stevekung.mods.indicatorutils.IndicatorUtilsEventHandler;
import stevekung.mods.indicatorutils.utils.JsonMessageUtils;

public class GameInfoHelper
{
    public static GameInfoHelper INSTANCE = new GameInfoHelper();

    public static Predicate<Entity> IS_NOT_DEATH_OR_SPECTATOR = new Predicate<Entity>()
    {
        @Override
        public boolean apply(@Nullable Entity entity)
        {
            if (entity instanceof EntityPlayer)
            {
                EntityPlayer player = (EntityPlayer) entity;
                return !GameInfoHelper.INSTANCE.isDeathOrSpectator(player);
            }
            return false;
        }
    };

    public boolean isSinglePlayer()
    {
        return Minecraft.getMinecraft().isSingleplayer();
    }

    public int getPing()
    {
        return Minecraft.getMinecraft().getNetHandler().getPlayerInfo(Minecraft.getMinecraft().thePlayer.getUniqueID()).getResponseTime();
    }

    public boolean isHypixel()
    {
        return Minecraft.getMinecraft().getCurrentServerData() != null && Minecraft.getMinecraft().getCurrentServerData().serverIP.contains("hypixel");
    }

    public int parseInt(String input, String type)
    {
        try
        {
            return Integer.parseInt(input);
        }
        catch (NumberFormatException e)
        {
            Minecraft.getMinecraft().thePlayer.addChatMessage(JsonMessageUtils.textToJson(I18n.format("commands.generic.num.invalid", input) + " in " + type + " setting", "red"));
            return 0;
        }
    }

    public <T extends Entity> List<T> detectEntities(Class<? extends T> entity, AxisAlignedBB range)
    {
        return Minecraft.getMinecraft().thePlayer.worldObj.getEntitiesWithinAABB(entity, range);
    }

    // Credit to lib12time (Bukkit)
    public String getInGameTime(long worldTick)
    {
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
        String AMPM = JsonMessageUtils.textToJson(hours >= 12 ? "PM" : "AM", ConfigManager.customColorTimeAMPM).getFormattedText();
        String game = JsonMessageUtils.textToJson("Game: ", ConfigManager.customColorGameTime).getFormattedText();
        String value = JsonMessageUtils.textToJson(shours + ":" + sminutes, ConfigManager.customColorGameTimeValue).getFormattedText();

        if (ConfigManager.useCustomTextGameTime)
        {
            game = JsonMessageUtils.rawTextToJson(ConfigManager.customTextGameTime).getFormattedText();
        }
        return game + value + " " + AMPM;
    }

    public boolean isDeathOrSpectator(EntityPlayer player)
    {
        if (Minecraft.getMinecraft().playerController.getCurrentGameType().getName().equals("survival") && player.isPotionActive(Potion.invisibility) && player.getActivePotionEffect(Potion.invisibility).getDuration() > 9600)
        {
            return true;
        }
        else if (player.getHealth() <= 0.0F)
        {
            return true;
        }
        else
        {
            return player.isSpectator();
        }
    }

    public int getCPS()
    {
        Iterator<Long> iterator = IndicatorUtilsEventHandler.clicks.iterator();

        while (iterator.hasNext())
        {
            if (iterator.next().longValue() < System.currentTimeMillis() - 1000L)
            {
                iterator.remove();
            }
        }
        return IndicatorUtilsEventHandler.clicks.size();
    }

    public boolean isBelowMinecraft19()
    {
        return IndicatorUtils.MC_VERSION.equals("1.7.2") || IndicatorUtils.MC_VERSION.equals("1.7.10") || IndicatorUtils.MC_VERSION.equals("1.8.9");
    }

    public String ticksToElapsedTime(int ticks)
    {
        int i = ticks / 20;
        int j = i / 60;
        i = i % 60;
        return i < 10 ? j + ":0" + i : j + ":" + i;
    }
}