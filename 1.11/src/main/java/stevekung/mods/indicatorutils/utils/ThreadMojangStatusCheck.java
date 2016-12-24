/*******************************************************************************
 *
 * Copyright 2016 Wasinthorn Suksri/SteveKunG - Indicator Utils
 *
 ******************************************************************************/

package stevekung.mods.indicatorutils.utils;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import stevekung.mods.indicatorutils.utils.MojangStatusChecker.ServerStatus;

public class ThreadMojangStatusCheck extends Thread
{
    private final boolean startup;

    public ThreadMojangStatusCheck(boolean startup)
    {
        super("Mojang Status Check Thread");
        this.startup = startup;
    }

    @Override
    public void run()
    {
        if (FMLCommonHandler.instance().getSide() == Side.CLIENT)
        {
            if (this.startup)
            {
                for (MojangStatusChecker statusChecker : MojangStatusChecker.values())
                {
                    ServerStatus status = statusChecker.getStatus();
                    IULog.info(statusChecker.getName() + ": " + status.getStatus());
                }
            }
            else
            {
                for (MojangStatusChecker statusChecker : MojangStatusChecker.values())
                {
                    String service = statusChecker.getName();
                    ServerStatus status = statusChecker.getStatus();
                    Minecraft.getMinecraft().player.sendMessage(new JsonMessageUtils().text(service + ": ").appendSibling(new JsonMessageUtils().text(status.getStatus()).setStyle(new JsonMessageUtils().colorFromConfig(status.getColor()))));
                }
            }
        }
    }
}