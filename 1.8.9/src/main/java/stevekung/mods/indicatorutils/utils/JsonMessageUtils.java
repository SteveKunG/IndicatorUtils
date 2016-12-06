/*******************************************************************************
 *
 * Copyright 2016 Wasinthorn Suksri/SteveKunG - Indicator Utils
 *
 ******************************************************************************/

package stevekung.mods.indicatorutils.utils;

import com.google.gson.JsonParseException;

import net.minecraft.client.Minecraft;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.IChatComponent.Serializer;

public class JsonMessageUtils
{
    public static IChatComponent json(String json)
    {
        return Serializer.jsonToComponent("[{" + json + "}]");
    }

    public static IChatComponent textToJson(String text)
    {
        return Serializer.jsonToComponent("[{\"text\":\"" + text + "\"}]");
    }

    public static IChatComponent textToJson(String text, String color)
    {
        return Serializer.jsonToComponent("[{\"text\":\"" + text + "\",\"color\":\"" + color + "\"}]");
    }

    public static IChatComponent textToJson(String text, String color, String extra)
    {
        return Serializer.jsonToComponent("[{\"text\":\"" + text + "\",\"color\":\"" + color + "\",\"extra\":" + extra + "}]");
    }

    public static IChatComponent rawTextToJson(String json)
    {
        IChatComponent text = Serializer.jsonToComponent("[{\"text\":\"null \",\"color\":\"red\",\"bold\":\"true\"}]");

        try
        {
            text = JsonMessageUtils.json(json);
        }
        catch (JsonParseException jsonparseexception)
        {
            if (Minecraft.getMinecraft().thePlayer.ticksExisted % 300 == 0)
            {
                Minecraft.getMinecraft().thePlayer.addChatMessage(JsonMessageUtils.textToJson(jsonparseexception.getMessage(), "red"));
            }
        }
        return text;
    }
}