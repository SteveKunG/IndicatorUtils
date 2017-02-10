/*******************************************************************************
 *
 * Copyright 2016 Wasinthorn Suksri/SteveKunG - Indicator Utils
 *
 ******************************************************************************/

package stevekung.mods.indicatorutils.handler;

import net.minecraft.client.gui.GuiMainMenu;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
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
            if (event.getGui() instanceof GuiMainMenu)
            {
                event.setGui(new GuiOldVersionWarning());
            }
        }
        MinecraftForge.EVENT_BUS.unregister(this);
    }
}