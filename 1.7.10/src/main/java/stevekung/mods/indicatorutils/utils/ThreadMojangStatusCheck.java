/*******************************************************************************
 *
 * Copyright 2016 Wasinthorn Suksri/SteveKunG - Indicator Utils
 *
 ******************************************************************************/

package stevekung.mods.indicatorutils.utils;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.client.Minecraft;
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
                    Minecraft.getMinecraft().thePlayer.addChatMessage(JsonMessageUtils.json("\"text\":\"" + service + ": \",\"extra\":[{\"text\":\"" + status.getStatus() + "\",\"color\":\"" + status.getColor() + "\"}]"));
                }
            }
        }
    }
}