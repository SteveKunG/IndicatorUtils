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
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.minecraft.util.text.TextFormatting;

public enum MojangStatusChecker
{
    MAIN_WEBSITE("Main Website", "minecraft.net"),
    SESSION_SERVER("Minecraft Session Server", "session.minecraft.net"),
    ACCOUNTS("Accounts Service", "account.mojang.com"),
    AUTHENTICATION("Authentication Service", "auth.mojang.com"),
    SKINS_SERVER("Skins Server", "skins.minecraft.net"),
    AUTHENTICATION_SERVER("Authentication Server", "authserver.mojang.com"),
    MOJANG_SESSION_SERVER("Mojang Session Server", "sessionserver.mojang.com"),
    MOJANG_API("Mojang API Service", "api.mojang.com"),
    TEXTURES("Textures Service", "textures.minecraft.net"),
    MOJANG_MAIN_WEBSITE("Mojang Main Website", "mojang.com");

    private String name;
    private String serviceURL;

    private MojangStatusChecker(String name, String serviceURL)
    {
        this.name = name;
        this.serviceURL = serviceURL;
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
            JsonObject jsonObject = (JsonObject) new JsonParser().parse(bufferedReader);
            JsonElement status = jsonObject.get(this.serviceURL);
            return ServerStatus.get(status.getAsString());
        }
        catch (IOException e)
        {
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