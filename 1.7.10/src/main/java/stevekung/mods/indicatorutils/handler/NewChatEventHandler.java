/*******************************************************************************
 *
 * Copyright 2016 Wasinthorn Suksri/SteveKunG - Indicator Utils
 *
 ******************************************************************************/

package stevekung.mods.indicatorutils.handler;

import javax.xml.bind.DatatypeConverter;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import stevekung.mods.indicatorutils.IndicatorUtils;
import stevekung.mods.indicatorutils.config.ExtendedModSettings;
import stevekung.mods.indicatorutils.utils.AutoLogin.AutoLoginData;

public class NewChatEventHandler
{
    @SubscribeEvent
    public void onClientConnectedToServer(ClientConnectedToServerEvent event)
    {
        Minecraft.getMinecraft().func_152344_a(new Runnable()
        {
            @Override
            public void run()
            {
                MinecraftForge.EVENT_BUS.register(new EntityJoinWorldEventHandler());
            }
        });
    }

    public static class EntityJoinWorldEventHandler
    {
        @SubscribeEvent
        public void onEntityJoinWorld(EntityJoinWorldEvent event)
        {
            if (event.entity instanceof EntityClientPlayerMP)
            {
                EntityClientPlayerMP player = (EntityClientPlayerMP) event.entity;
                this.runAutoLoginCommand(player);
                MinecraftForge.EVENT_BUS.unregister(this);
            }
        }

        private void runAutoLoginCommand(EntityClientPlayerMP player)
        {
            if (Minecraft.getMinecraft().func_147104_D() != null)
            {
                for (AutoLoginData login : ExtendedModSettings.loginData.getAutoLoginList())
                {
                    if (Minecraft.getMinecraft().func_147104_D().serverIP.equalsIgnoreCase(login.getServerIP()) && IndicatorUtils.USERNAME.equals(login.getUsername()))
                    {
                        player.sendChatMessage(login.getCommand() + new String(DatatypeConverter.parseBase64Binary(login.getValue())));
                    }
                }
            }
        }
    }
}