/*******************************************************************************
 *
 * Copyright 2016 Wasinthorn Suksri/SteveKunG - Indicator Utils
 *
 ******************************************************************************/

package stevekung.mods.indicatorutils.utils;

import com.google.gson.JsonParseException;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.ITextComponent.Serializer;

public class JsonMessageUtils
{
    public static ITextComponent json(String json)
    {
        return Serializer.jsonToComponent("[{" + json + "}]");
    }

    public static ITextComponent textToJson(String text)
    {
        return Serializer.jsonToComponent("[{\"text\":\"" + text + "\"}]");
    }

    public static ITextComponent textToJson(String text, String color)
    {
        return Serializer.jsonToComponent("[{\"text\":\"" + text + "\",\"color\":\"" + color + "\"}]");
    }

    public static ITextComponent rawTextToJson(String json)
    {
        ITextComponent text = Serializer.jsonToComponent("[{\"text\":\"null \",\"color\":\"red\",\"bold\":\"true\"}]");

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