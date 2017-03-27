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

import org.lwjgl.opengl.GL11;

import com.google.common.collect.Maps;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import stevekung.mods.indicatorutils.IndicatorUtils;
import stevekung.mods.indicatorutils.config.ExtendedModSettings;

public class CapeUtils
{
    public static Map<String, DynamicTexture> CAPE_TEXTURE = Maps.newHashMap();
    public static boolean textureUploaded = true;

    public static void bindCapeTexture()
    {
        if (CapeUtils.CAPE_TEXTURE.get(IndicatorUtils.USERNAME) != null)
        {
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, CapeUtils.CAPE_TEXTURE.get(IndicatorUtils.USERNAME).getGlTextureId());
        }
    }

    public static void setCapeURL(String url, boolean startup)
    {
        URL jurl = null;
        boolean noConnection = false;
        JsonUtils json = new JsonUtils();

        try
        {
            jurl = new URL(url);
        }
        catch (MalformedURLException e)
        {
            noConnection = true;
            e.printStackTrace();
            return;
        }

        if (CapeUtils.textureUploaded && !noConnection)
        {
            try
            {
                CapeUtils.CAPE_TEXTURE.put(IndicatorUtils.USERNAME, new DynamicTexture(ImageIO.read(jurl)));
                ExtendedModSettings.CAPE_URL = Base64Utils.encode(url);
                ExtendedModSettings.saveExtendedSettings();
                CapeUtils.textureUploaded = false;
            }
            catch (MalformedURLException e)
            {
                if (Minecraft.getMinecraft().thePlayer != null)
                {
                    Minecraft.getMinecraft().thePlayer.addChatMessage(json.text("Missing protocol or wrong URL format").setChatStyle(json.red()));
                }
                e.printStackTrace();
                return;
            }
            catch (IOException e)
            {
                if (Minecraft.getMinecraft().thePlayer != null)
                {
                    Minecraft.getMinecraft().thePlayer.addChatMessage(json.text("Cannot read image from URL").setChatStyle(json.red()));
                }
                e.printStackTrace();
                return;
            }
        }

        if (!startup)
        {
            Minecraft.getMinecraft().thePlayer.addChatMessage(json.text("Downloaded new cape texture from URL"));
        }
    }
}