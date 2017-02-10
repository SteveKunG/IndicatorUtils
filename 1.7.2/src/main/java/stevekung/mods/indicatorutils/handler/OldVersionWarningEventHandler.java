/*******************************************************************************
 *
 * Copyright 2016 Wasinthorn Suksri/SteveKunG - Indicator Utils
 *
 ******************************************************************************/

package stevekung.mods.indicatorutils.handler;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import stevekung.mods.indicatorutils.gui.GuiOldVersionWarning;
import stevekung.mods.indicatorutils.utils.VersionChecker;

public class OldVersionWarningEventHandler
{
    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onGuiOpen(GuiOpenEvent event)
    {
        if (VersionChecker.INSTANCE.isLatestVersion())
        {
            if (event.gui instanceof GuiMainMenu)
            {
                event.gui = new GuiOldVersionWarning();
            }
        }
        MinecraftForge.EVENT_BUS.unregister(this);
    }
}