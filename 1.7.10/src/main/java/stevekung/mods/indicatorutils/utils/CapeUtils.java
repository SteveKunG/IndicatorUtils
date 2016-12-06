/*******************************************************************************
 *
 * Copyright 2016 Wasinthorn Suksri/SteveKunG - Indicator Utils
 *
 ******************************************************************************/

package stevekung.mods.indicatorutils.utils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;

import org.lwjgl.opengl.GL11;

import com.google.common.collect.Maps;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import stevekung.mods.indicatorutils.ExtendedModSettings;
import stevekung.mods.indicatorutils.IndicatorUtils;

public class CapeUtils
{
    public static Map<String, DynamicTexture> CAPE_TEXTURE = Maps.newHashMap();
    public static boolean textureUploaded = true;

    public static void bindCapeTexture()
    {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, CapeUtils.CAPE_TEXTURE.get(IndicatorUtils.USERNAME).getGlTextureId());
    }

    public static void setCapeURL(String url, boolean startup)
    {
        if (CapeUtils.textureUploaded)
        {
            try
            {
                CapeUtils.CAPE_TEXTURE.put(IndicatorUtils.USERNAME, new DynamicTexture(ImageIO.read(new URL(url))));
                ExtendedModSettings.CAPE_URL = CapeUtils.encodeCapeURL(url);
                ExtendedModSettings.saveExtendedSettings();
                CapeUtils.textureUploaded = false;
            }
            catch (MalformedURLException e)
            {
                Minecraft.getMinecraft().thePlayer.addChatMessage(JsonMessageUtils.textToJson("Missing protocol or wrong URL format", "red"));
                e.printStackTrace();
                return;
            }
            catch (IOException e)
            {
                Minecraft.getMinecraft().thePlayer.addChatMessage(JsonMessageUtils.textToJson("Cannot read image from URL", "red"));
                e.printStackTrace();
                return;
            }
        }

        if (!startup)
        {
            Minecraft.getMinecraft().thePlayer.addChatMessage(JsonMessageUtils.textToJson("Downloaded new cape texture from URL"));
        }
    }

    public static String decodeCapeURL(String url)
    {
        return new String(DatatypeConverter.parseBase64Binary(url));
    }

    public static String encodeCapeURL(String url)
    {
        return DatatypeConverter.printBase64Binary(url.getBytes());
    }
}