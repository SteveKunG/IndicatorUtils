package stevekung.mods.indicatorutils;

import net.minecraft.client.gui.GuiMainMenu;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import stevekung.mods.indicatorutils.utils.VersionChecker;
import stevekung.mods.indicatorutils.utils.gui.GuiOldVersionWarning;

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