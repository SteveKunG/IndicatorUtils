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
                for (MojangStatusChecker statusChecker : MojangStatusChecker.valuesCached())
                {
                    ServerStatus status = statusChecker.getStatus();
                    IULog.info(statusChecker.getName() + ": " + status.getStatus());
                }
            }
            else
            {
                for (MojangStatusChecker statusChecker : MojangStatusChecker.valuesCached())
                {
                    String service = statusChecker.getName();
                    ServerStatus status = statusChecker.getStatus();
                    Minecraft.getMinecraft().thePlayer.addChatMessage(new JsonUtils().text(service + ": ").appendSibling(new JsonUtils().text(status.getStatus()).setChatStyle(new JsonUtils().colorFromConfig(status.getColor()))));
                }
            }
        }
    }
}