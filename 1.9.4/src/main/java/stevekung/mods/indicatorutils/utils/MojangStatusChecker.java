/*******************************************************************************
 *
 * Copyright 2016 Wasinthorn Suksri/SteveKunG - Indicator Utils
 *
 ******************************************************************************/

package stevekung.mods.indicatorutils.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import net.minecraft.util.text.TextFormatting;

public enum MojangStatusChecker
{
    MAIN_WEBSITE("Main Website", "minecraft.net"),
    MC_SESSION_SERVER("Minecraft Session Server", "session.minecraft.net"),
    MC_SKINS_SERVER("Minecraft Skins Server", "skins.minecraft.net"),
    TEXTURES_SERVICE("Minecraft Textures Service", "textures.minecraft.net"),
    MOJANG_ACCOUNT_SERVICE("Mojang Account Service", "account.mojang.com"),
    MOJANG_SESSION_SERVER("Mojang Session Server", "sessionserver.mojang.com"),
    MOJANG_AUTHENTICATION_SERVICE("Mojang Authentication Service", "auth.mojang.com"),
    MOJANG_AUTHENTICATION_SERVER("Mojang Authentication Server", "authserver.mojang.com"),
    MOJANG_PUBLIC_API("Mojang Public API", "api.mojang.com"),
    MOJANG_MAIN_WEBSITE("Mojang Main Website", "mojang.com");

    private String name;
    private String serviceURL;
    private static MojangStatusChecker[] values = MojangStatusChecker.values();

    private MojangStatusChecker(String name, String serviceURL)
    {
        this.name = name;
        this.serviceURL = serviceURL;
    }

    public static MojangStatusChecker[] valuesCached()
    {
        return MojangStatusChecker.values;
    }

    public String getName()
    {
        return this.name;
    }

    public ServerStatus getStatus()
    {
        try
        {
            URL url = new URL("http://status.mojang.com/check?service=" + this.serviceURL);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
            JsonElement jsonElement = new JsonParser().parse(bufferedReader).getAsJsonObject().get(this.serviceURL);
            return ServerStatus.get(jsonElement.getAsString());
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return ServerStatus.UNKNOWN;
        }
    }

    public enum ServerStatus
    {
        ONLINE("Online", TextFormatting.GREEN.getFriendlyName()),
        UNSTABLE("Unstable", TextFormatting.YELLOW.getFriendlyName()),
        OFFLINE("Offline", TextFormatting.DARK_RED.getFriendlyName()),
        UNKNOWN("Unknown", TextFormatting.RED.getFriendlyName());

        private String status;
        private String color;

        ServerStatus(String status, String color)
        {
            this.status = status;
            this.color = color;
        }

        public String getStatus()
        {
            return this.status;
        }

        public String getColor()
        {
            return this.color;
        }

        public static ServerStatus get(String status)
        {
            if (status.equalsIgnoreCase("green"))
            {
                return ServerStatus.ONLINE;
            }
            else if (status.equalsIgnoreCase("yellow"))
            {
                return ServerStatus.UNSTABLE;
            }
            else if (status.equalsIgnoreCase("red"))
            {
                return ServerStatus.OFFLINE;
            }
            else
            {
                return ServerStatus.UNKNOWN;
            }
        }
    }
}