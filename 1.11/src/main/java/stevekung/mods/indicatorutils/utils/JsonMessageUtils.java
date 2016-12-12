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
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;

public class JsonMessageUtils
{
    public static ITextComponent rawTextToJson(String json)
    {
        ITextComponent text = new JsonMessageUtils().text("null ").setStyle(new JsonMessageUtils().style().setColor(TextFormatting.RED));

        try
        {
            text = Serializer.jsonToComponent("[{" + json + "}]");
        }
        catch (JsonParseException jsonparseexception)
        {
            if (Minecraft.getMinecraft().thePlayer.ticksExisted % 300 == 0)
            {
                Minecraft.getMinecraft().thePlayer.addChatMessage(new JsonMessageUtils().text(jsonparseexception.getMessage()).setStyle(new JsonMessageUtils().style().setColor(TextFormatting.RED)));
            }
        }
        return text;
    }

    public TextComponentString text(String text)
    {
        return new TextComponentString(text);
    }

    public Style style()
    {
        return new Style();
    }

    public Style colorFromConfig(String color)
    {
        return this.style().setColor(this.color(color));
    }

    private TextFormatting color(String color)
    {
        if (color.equalsIgnoreCase("black"))
        {
            return TextFormatting.BLACK;
        }
        else if (color.equalsIgnoreCase("dark_blue"))
        {
            return TextFormatting.DARK_BLUE;
        }
        else if (color.equalsIgnoreCase("dark_green"))
        {
            return TextFormatting.DARK_GREEN;
        }
        else if (color.equalsIgnoreCase("dark_aqua"))
        {
            return TextFormatting.DARK_AQUA;
        }
        else if (color.equalsIgnoreCase("dark_red"))
        {
            return TextFormatting.DARK_RED;
        }
        else if (color.equalsIgnoreCase("dark_purple"))
        {
            return TextFormatting.DARK_PURPLE;
        }
        else if (color.equalsIgnoreCase("gold"))
        {
            return TextFormatting.GOLD;
        }
        else if (color.equalsIgnoreCase("gray"))
        {
            return TextFormatting.GRAY;
        }
        else if (color.equalsIgnoreCase("dark_gray"))
        {
            return TextFormatting.DARK_GRAY;
        }
        else if (color.equalsIgnoreCase("blue"))
        {
            return TextFormatting.BLUE;
        }
        else if (color.equalsIgnoreCase("green"))
        {
            return TextFormatting.GREEN;
        }
        else if (color.equalsIgnoreCase("aqua"))
        {
            return TextFormatting.AQUA;
        }
        else if (color.equalsIgnoreCase("red"))
        {
            return TextFormatting.RED;
        }
        else if (color.equalsIgnoreCase("light_purple"))
        {
            return TextFormatting.LIGHT_PURPLE;
        }
        else if (color.equalsIgnoreCase("yellow"))
        {
            return TextFormatting.YELLOW;
        }
        else
        {
            return TextFormatting.WHITE;
        }
    }

    public Style white()
    {
        return this.style().setColor(TextFormatting.WHITE);
    }

    public Style red()
    {
        return this.style().setColor(TextFormatting.RED);
    }

    public ClickEvent click(ClickEvent.Action action, String url)
    {
        return new ClickEvent(action, url);
    }

    public HoverEvent hover(HoverEvent.Action action, ITextComponent text)
    {
        return new HoverEvent(action, text);
    }
}