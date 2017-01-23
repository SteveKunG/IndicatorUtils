/*******************************************************************************
 *
 * Copyright 2016 Wasinthorn Suksri/SteveKunG - Indicator Utils
 *
 ******************************************************************************/

package stevekung.mods.indicatorutils;

import javax.xml.bind.DatatypeConverter;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent;
import stevekung.mods.indicatorutils.utils.AutoLogin.AutoLoginData;
import stevekung.mods.indicatorutils.utils.JsonMessageUtils;
import stevekung.mods.indicatorutils.utils.helper.GameInfoHelper;

public class NewChatEventHandler
{
    @SubscribeEvent
    public void onClientConnectedToServer(ClientConnectedToServerEvent event)
    {
        Minecraft.getMinecraft().addScheduledTask(() -> { MinecraftForge.EVENT_BUS.register(new EntityJoinWorldEventHandler()); });
    }

    private class EntityJoinWorldEventHandler
    {
        @SubscribeEvent
        public void onEntityJoinWorld(EntityJoinWorldEvent event)
        {
            if (event.getEntity() instanceof EntityPlayerSP)
            {
                EntityPlayerSP player = (EntityPlayerSP) event.getEntity();
                this.runHypixelCommand(player);
                this.runAutoLoginCommand(player);
                MinecraftForge.EVENT_BUS.unregister(this);
            }
        }

        private void runHypixelCommand(EntityPlayerSP player)
        {
            if (GameInfoHelper.INSTANCE.isHypixel())
            {
                if (ExtendedModSettings.CHAT_MODE.equalsIgnoreCase("hypixel_party_chat"))
                {
                    player.sendChatMessage("/chat p");
                    player.sendMessage(new JsonMessageUtils().text("Set chat mode to Hypixel Party Chat"));
                }
                if (ExtendedModSettings.CHAT_MODE.equalsIgnoreCase("hypixel_guild_chat"))
                {
                    player.sendChatMessage("/chat g");
                    player.sendMessage(new JsonMessageUtils().text("Set chat mode to Hypixel Guild Chat"));
                }
                if (ExtendedModSettings.CHAT_MODE.equalsIgnoreCase(""))
                {
                    player.sendChatMessage("/chat a");
                    player.sendMessage(new JsonMessageUtils().text("Reset Chat Mode"));
                }
            }
        }

        private void runAutoLoginCommand(EntityPlayerSP player)
        {
            if (Minecraft.getMinecraft().getCurrentServerData() != null)
            {
                for (AutoLoginData login : ExtendedModSettings.loginData.getAutoLoginList())
                {
                    if (Minecraft.getMinecraft().getCurrentServerData().serverIP.equalsIgnoreCase(login.getServerIP()))
                    {
                        player.sendChatMessage(login.getCommand() + new String(DatatypeConverter.parseBase64Binary(login.getValue())));
                    }
                }
            }
        }
    }
}