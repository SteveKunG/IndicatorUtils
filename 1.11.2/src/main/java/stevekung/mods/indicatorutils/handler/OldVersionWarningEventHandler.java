/*******************************************************************************
 *
 * Copyright 2016 Wasinthorn Suksri/SteveKunG - Indicator Utils
 *
 ******************************************************************************/

package stevekung.mods.indicatorutils.handler;

import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import stevekung.mods.indicatorutils.IndicatorUtils;
import stevekung.mods.indicatorutils.gui.GuiOldVersionWarning;
import stevekung.mods.indicatorutils.utils.Base64Utils;
import stevekung.mods.indicatorutils.utils.VersionChecker;

public class OldVersionWarningEventHandler
{
    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onGuiOpen(GuiOpenEvent event)
    {
        for (String uuid : IndicatorUtils.IGNORE_LIST)
        {
            if (Minecraft.getMinecraft().getSession().getProfile().getId() == UUID.fromString(Base64Utils.decode(uuid)) || IndicatorUtils.isObfuscatedEnvironment())
            {
                MinecraftForge.EVENT_BUS.unregister(this);
                return;
            }
        }

        if (VersionChecker.INSTANCE.isLatestVersion())
        {
            if (event.getGui() instanceof GuiMainMenu)
            {
                event.setGui(new GuiOldVersionWarning());
            }
        }
        MinecraftForge.EVENT_BUS.unregister(this);
    }
}