/*******************************************************************************
 *
 * Copyright 2016 Wasinthorn Suksri/SteveKunG - Indicator Utils
 *
 ******************************************************************************/

package stevekung.mods.indicatorutils;

import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

import javax.xml.bind.DatatypeConverter;

import org.apache.commons.lang3.Validate;

import com.google.common.collect.Queues;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import stevekung.mods.indicatorutils.utils.AutoLogin.AutoLoginData;

public class NewChatEventHandler
{
    private Thread currentThread = Thread.currentThread();
    private Queue queue = Queues.newArrayDeque();

    @SubscribeEvent
    public void onClientConnectedToServer(ClientConnectedToServerEvent event)
    {
        this.run(new Runnable()
        {
            @Override
            public void run()
            {
                MinecraftForge.EVENT_BUS.register(new EntityJoinWorldEventHandler());
            }
        });
    }

    private ListenableFuture run(Runnable runnable)
    {
        Validate.notNull(runnable);
        return this.callListenable(Executors.callable(runnable));
    }

    private ListenableFuture callListenable(Callable callable)
    {
        Validate.notNull(callable);

        if (!this.isCurrentThread())
        {
            ListenableFutureTask listenablefuturetask = ListenableFutureTask.create(callable);
            synchronized (this.queue)
            {
                this.queue.add(listenablefuturetask);
                return listenablefuturetask;
            }
        }
        else
        {
            try
            {
                return Futures.immediateFuture(callable.call());
            }
            catch (Exception e)
            {
                return Futures.immediateFailedCheckedFuture(e);
            }
        }
    }

    private boolean isCurrentThread()
    {
        return Thread.currentThread() == this.currentThread;
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