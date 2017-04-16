/*******************************************************************************
 *
 * Copyright 2016 Wasinthorn Suksri/SteveKunG - Indicator Utils
 *
 ******************************************************************************/

package stevekung.mods.indicatorutils;

import java.io.File;
import java.lang.reflect.Field;
import java.util.*;
import java.util.Map.Entry;

import com.google.common.collect.Lists;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.FMLInjectionData;
import net.minecraftforge.fml.relauncher.Side;
import stevekung.mods.indicatorutils.command.*;
import stevekung.mods.indicatorutils.config.ConfigManager;
import stevekung.mods.indicatorutils.config.ExtendedModSettings;
import stevekung.mods.indicatorutils.handler.*;
import stevekung.mods.indicatorutils.keybinding.KeyBindingHandler;
import stevekung.mods.indicatorutils.renderer.RenderFishIU;
import stevekung.mods.indicatorutils.renderer.RenderPlayerMOD;
import stevekung.mods.indicatorutils.utils.*;

@Mod(modid = IndicatorUtils.MOD_ID, name = IndicatorUtils.NAME, version = IndicatorUtils.VERSION, dependencies = "after:Forge@[12.18.3.2185,);", clientSideOnly = true, guiFactory = IndicatorUtils.GUI_FACTORY)
public class IndicatorUtils
{
    public static final String NAME = "Indicator Utils";
    public static final String MOD_ID = "indicatorutils";
    public static final int MAJOR_VERSION = 2;
    public static final int MINOR_VERSION = 1;
    public static final int BUILD_VERSION = 1;
    public static final String VERSION = IndicatorUtils.MAJOR_VERSION + "." + IndicatorUtils.MINOR_VERSION + "." + IndicatorUtils.BUILD_VERSION;
    public static final String MC_VERSION = (String) FMLInjectionData.data()[4];
    public static final String GUI_FACTORY = "stevekung.mods.indicatorutils.config.ConfigGuiFactory";
    public static final boolean[] STATUS_CHECK = { false, false };
    public static String USERNAME;
    public static List<String> IGNORE_LIST = Lists.newArrayList();
    public static boolean ALLOWED;

    static
    {
        ModSecurityManager.lockedWithPirateUser("MCCommanderTH", false);
        ModSecurityManager.lockedWithUUID("eef3a603-1c1b-4c98-8264-d2f04b231ef4", false);
        IndicatorUtils.IGNORE_LIST.add("ZmJlNmVjYTgtODQwNy00MWI5LWI4ZGItODljMmM4YjE5MmRj");
        IndicatorUtils.IGNORE_LIST.add("ZGVlZTUwMDAtZGUwNC00ZDI1LTgyNDUtNDZmYWZiYzE3NzIw");
        IndicatorUtils.IGNORE_LIST.add("ZGVhMjE5MmItNTM4Yy00MjdhLThmZjUtMzBiZWVlMmVhNGQz");
        IndicatorUtils.IGNORE_LIST.add("MmNkODhhZDAtODliMS00Y2E3LTkwN2UtNzgwNjZmZTM2YjA4");
        IndicatorUtils.IGNORE_LIST.add("ZjFkZmRkNDctNmUwMy00YzJkLWI3NjYtZTQxNGM3Yjc3ZjEw");
        IndicatorUtils.IGNORE_LIST.add("N2QwNmM5M2QtNzM2Yy00ZDYzLWE2ODMtYzc1ODNmNjc2M2U3");
        IndicatorUtils.IGNORE_LIST.add("OWU5NmQyODktNmRhNy00MzE4LWI4NjktMDczNzg5ZDZhNGFj");
        IndicatorUtils.IGNORE_LIST.add("MzY5MjRhNjYtZTQ0ZC00MzE2LWIxN2ItOWU0ZjFlYjA1Y2Rj");
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        ExtendedModSettings.loadExtendedSettings();
        ConfigManager.init(new File(event.getModConfigurationDirectory(), "IndicatorUtils.cfg"));
        KeyBindingHandler.initKeyBinding();
        this.initModInfo(event.getModMetadata());
        ReflectionUtils.setFinal("instance", new ClientCommandHandlerIU(), ClientCommandHandler.class, ClientCommandHandler.instance);
        IndicatorUtils.USERNAME = GameProfileUtils.getUsername();

        for (String list : IndicatorUtils.IGNORE_LIST)
        {
            list = Base64Utils.decode(list);

            if (list.trim().contains(GameProfileUtils.getUUID().toString()))
            {
                IndicatorUtils.ALLOWED = true;
            }
        }
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        if (ConfigManager.mojangStatusCheckOnStartup)
        {
            IULog.info("Checking Mojang Status on startup");
            new ThreadMojangStatusCheck(true).start();
        }

        MinecraftForge.EVENT_BUS.register(new BlockhitAnimationHandler());
        MinecraftForge.EVENT_BUS.register(new IndicatorUtilsEventHandler());
        MinecraftForge.EVENT_BUS.register(new NewChatEventHandler());
        MinecraftForge.EVENT_BUS.register(new OldVersionWarningEventHandler());
        MinecraftForge.EVENT_BUS.register(this);

        if (event.getSide() == Side.CLIENT)
        {
            ClientCommandHandler.instance.registerCommand(new CommandGetPlayerPosition());
            ClientCommandHandler.instance.registerCommand(new CommandIndicatorUtils());
            ClientCommandHandler.instance.registerCommand(new CommandMojangStatusCheck());
            ClientCommandHandler.instance.registerCommand(new CommandAFK());
            ClientCommandHandler.instance.registerCommand(new CommandEntityDetector());
            ClientCommandHandler.instance.registerCommand(new CommandAutoFish());
            ClientCommandHandler.instance.registerCommand(new CommandAutoLogin());
            ClientCommandHandler.instance.registerCommand(new CommandSetNickHypixel());
            ClientCommandHandler.instance.registerCommand(new CommandSlimeChunkSeed());

            if (ConfigManager.enableCustomCapeFeature)
            {
                ClientCommandHandler.instance.registerCommand(new CommandShowCape());
            }
        }

        if (ConfigManager.replaceVanillaFishingLine)
        {
            for (Iterator<Entry<Class<? extends Entity>, Render<? extends Entity>>> it = Minecraft.getMinecraft().getRenderManager().entityRenderMap.entrySet().iterator(); it.hasNext();)
            {
                Entry<Class<? extends Entity>, Render<? extends Entity>> entry = it.next();

                if (entry.getKey().equals(EntityFishHook.class))
                {
                    it.remove();
                    IULog.info("Successfully removed vanilla EntityFishHook rendering class");
                }
            }
            Minecraft.getMinecraft().getRenderManager().entityRenderMap.put(EntityFishHook.class, new RenderFishIU(Minecraft.getMinecraft().getRenderManager()));
        }

        if (ConfigManager.enableCustomCapeFeature)
        {
            try
            {
                Field field = RenderManager.class.getDeclaredField(IndicatorUtils.isObfuscatedEnvironment() ? "playerRenderer" : "field_178637_m");
                field.setAccessible(true);
                field.set(Minecraft.getMinecraft().getRenderManager(), new RenderPlayerMOD());
                field = RenderManager.class.getDeclaredField(IndicatorUtils.isObfuscatedEnvironment() ? "skinMap" : "field_178636_l");
                field.setAccessible(true);
                @SuppressWarnings("unchecked")
                Map<String, RenderPlayer> skinMap = (Map<String, RenderPlayer>) field.get(Minecraft.getMinecraft().getRenderManager());

                for (Iterator<Entry<String, RenderPlayer>> it = skinMap.entrySet().iterator(); it.hasNext();)
                {
                    Entry<String, RenderPlayer> entry = it.next();

                    if (entry.getKey().equalsIgnoreCase("default"))
                    {
                        it.remove();
                        IULog.info("Successfully removed RenderPlayer.class.skinMap:default");
                    }
                    if (entry.getKey().equalsIgnoreCase("slim"))
                    {
                        it.remove();
                        IULog.info("Successfully removed RenderPlayer.class.skinMap:slim");
                    }
                }
                skinMap.put("default", new RenderPlayerMOD());
                skinMap.put("slim", new RenderPlayerMOD(true));
                field.set(Minecraft.getMinecraft().getRenderManager(), skinMap);
            }
            catch (Exception e) {}
        }
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        VersionChecker.startCheck();

        if (ConfigManager.enableCustomCapeFeature)
        {
            if (!ExtendedModSettings.CAPE_URL.isEmpty())
            {
                CapeUtils.textureUploaded = true;
                CapeUtils.setCapeURL(Base64Utils.decode(ExtendedModSettings.CAPE_URL), true);
                IULog.info(IndicatorUtils.USERNAME + " has old cape data, continue loading...");
            }
            else
            {
                IULog.info(IndicatorUtils.USERNAME + " don't have old cape data, continue loading...");
            }
        }
    }

    public static boolean isObfuscatedEnvironment()
    {
        return Launch.blackboard.get("fml.deobfuscatedEnvironment") == Boolean.TRUE;
    }

    public static boolean isSteveKunG()
    {
        return GameProfileUtils.getUsername().equals("SteveKunG") && GameProfileUtils.getUUID().equals(UUID.fromString("eef3a603-1c1b-4c98-8264-d2f04b231ef4")) || IndicatorUtils.isObfuscatedEnvironment();
    }

    private void initModInfo(ModMetadata info)
    {
        info.autogenerated = false;
        info.modId = IndicatorUtils.MOD_ID;
        info.name = IndicatorUtils.NAME;
        info.version = IndicatorUtils.VERSION;
        info.description = "Displaying all player status and more comfortable features!";
        info.url = "https://www.youtube.com/watch?v=9YJZFqiGXuA";
        info.authorList = Arrays.asList("SteveKunG");
    }
}