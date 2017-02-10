/*******************************************************************************
 *
 * Copyright 2016 Wasinthorn Suksri/SteveKunG - Indicator Utils
 *
 ******************************************************************************/

package stevekung.mods.indicatorutils.handler;

import javax.xml.bind.DatatypeConverter;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent;
import stevekung.mods.indicatorutils.IndicatorUtils;
import stevekung.mods.indicatorutils.config.ExtendedModSettings;
import stevekung.mods.indicatorutils.helper.GameInfoHelper;
import stevekung.mods.indicatorutils.utils.AutoLogin.AutoLoginData;
import stevekung.mods.indicatorutils.utils.JsonUtils;

public class NewChatEventHandler
{
    @SubscribeEvent
    public void onClientConnectedToServer(ClientConnectedToServerEvent event)
    {
        Minecraft.getMinecraft().addScheduledTask(() -> { MinecraftForge.EVENT_BUS.register(new EntityJoinWorldEventHandler()); });
    }

    private static class EntityJoinWorldEventHandler
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
                    player.addChatMessage(new JsonUtils().text("Set chat mode to Hypixel Party Chat"));
                }
                if (ExtendedModSettings.CHAT_MODE.equalsIgnoreCase("hypixel_guild_chat"))
                {
                    player.sendChatMessage("/chat g");
                    player.addChatMessage(new JsonUtils().text("Set chat mode to Hypixel Guild Chat"));
                }
                if (ExtendedModSettings.CHAT_MODE.equalsIgnoreCase(""))
                {
                    player.sendChatMessage("/chat a");
                    player.addChatMessage(new JsonUtils().text("Reset Chat Mode"));
                }
            }
        }

        private void runAutoLoginCommand(EntityPlayerSP player)
        {
            if (Minecraft.getMinecraft().getCurrentServerData() != null)
            {
                for (AutoLoginData login : ExtendedModSettings.loginData.getAutoLoginList())
                {
                    if (Minecraft.getMinecraft().getCurrentServerData().serverIP.equalsIgnoreCase(login.getServerIP()) && IndicatorUtils.USERNAME.equals(login.getUsername()))
                    {
                        player.sendChatMessage(login.getCommand() + new String(DatatypeConverter.parseBase64Binary(login.getValue())));
                    }
                }
            }
        }
    }
}