package stevekung.mods.indicatorutils;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import com.google.common.collect.Lists;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.FMLInjectionData;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import stevekung.mods.indicatorutils.command.*;
import stevekung.mods.indicatorutils.config.ConfigManager;
import stevekung.mods.indicatorutils.config.ExtendedModSettings;
import stevekung.mods.indicatorutils.handler.ClientCommandHandlerIU;
import stevekung.mods.indicatorutils.handler.IndicatorUtilsEventHandler;
import stevekung.mods.indicatorutils.handler.NewChatEventHandler;
import stevekung.mods.indicatorutils.handler.OldVersionWarningEventHandler;
import stevekung.mods.indicatorutils.keybinding.KeyBindingHandler;
import stevekung.mods.indicatorutils.profile.ProfileSettings;
import stevekung.mods.indicatorutils.renderer.CapeRenderer;
import stevekung.mods.indicatorutils.renderer.RenderPlayerMOD;
import stevekung.mods.indicatorutils.utils.*;

@Mod(modid = IndicatorUtils.MOD_ID, name = IndicatorUtils.NAME, version = IndicatorUtils.VERSION, dependencies = IndicatorUtils.FORGE_VERSION, guiFactory = IndicatorUtils.GUI_FACTORY)
public class IndicatorUtils
{
    public static final String NAME = "Indicator Utils";
    public static final String MOD_ID = "indicatorutils";
    public static final int MAJOR_VERSION = 2;
    public static final int MINOR_VERSION = 1;
    public static final int BUILD_VERSION = 3;
    public static final String VERSION = IndicatorUtils.MAJOR_VERSION + "." + IndicatorUtils.MINOR_VERSION + "." + IndicatorUtils.BUILD_VERSION;
    public static final String MC_VERSION = (String) FMLInjectionData.data()[4];
    public static final String GUI_FACTORY = "stevekung.mods.indicatorutils.config.ConfigGuiFactory";
    public static final String FORGE_VERSION = "after:Forge@[10.13.4.1558,);";
    public static final boolean[] STATUS_CHECK = { false, false };
    public static String USERNAME;
    public static List<String> IGNORE_LIST = Lists.newArrayList();
    public static boolean ALLOWED;
    private static boolean DEOBFUSCATED;

    static
    {
        IndicatorUtils.IGNORE_LIST.add("ZmJlNmVjYTgtODQwNy00MWI5LWI4ZGItODljMmM4YjE5MmRj");
        IndicatorUtils.IGNORE_LIST.add("ZGVlZTUwMDAtZGUwNC00ZDI1LTgyNDUtNDZmYWZiYzE3NzIw");
        IndicatorUtils.IGNORE_LIST.add("ZGVhMjE5MmItNTM4Yy00MjdhLThmZjUtMzBiZWVlMmVhNGQz");
        IndicatorUtils.IGNORE_LIST.add("MmNkODhhZDAtODliMS00Y2E3LTkwN2UtNzgwNjZmZTM2YjA4");
        IndicatorUtils.IGNORE_LIST.add("ZjFkZmRkNDctNmUwMy00YzJkLWI3NjYtZTQxNGM3Yjc3ZjEw");
        IndicatorUtils.IGNORE_LIST.add("N2QwNmM5M2QtNzM2Yy00ZDYzLWE2ODMtYzc1ODNmNjc2M2U3");
        IndicatorUtils.IGNORE_LIST.add("OWU5NmQyODktNmRhNy00MzE4LWI4NjktMDczNzg5ZDZhNGFj");
        IndicatorUtils.IGNORE_LIST.add("MzY5MjRhNjYtZTQ0ZC00MzE2LWIxN2ItOWU0ZjFlYjA1Y2Rj");

        try
        {
            IndicatorUtils.DEOBFUSCATED = Launch.classLoader.getClassBytes("net.minecraft.world.World") != null;
        }
        catch (Exception e) {}
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        if (FMLCommonHandler.instance().getSide() == Side.CLIENT)
        {
            ExtendedModSettings.loadExtendedSettings();
            ProfileSettings.loadProfileSettings();
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
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        if (FMLCommonHandler.instance().getSide() == Side.CLIENT)
        {
            if (ConfigManager.mojangStatusCheckOnStartup)
            {
                IULog.info("Checking Mojang Status on startup");
                new ThreadMojangStatusCheck(true).start();
            }

            MinecraftForge.EVENT_BUS.register(new IndicatorUtilsEventHandler());
            FMLCommonHandler.instance().bus().register(new IndicatorUtilsEventHandler());
            MinecraftForge.EVENT_BUS.register(new NewChatEventHandler());
            FMLCommonHandler.instance().bus().register(new NewChatEventHandler());
            MinecraftForge.EVENT_BUS.register(new OldVersionWarningEventHandler());
            FMLCommonHandler.instance().bus().register(new OldVersionWarningEventHandler());
            MinecraftForge.EVENT_BUS.register(new CapeRenderer());
            FMLCommonHandler.instance().bus().register(new CapeRenderer());
            MinecraftForge.EVENT_BUS.register(this);
            FMLCommonHandler.instance().bus().register(this);

            ClientCommandHandler.instance.registerCommand(new CommandGetPlayerPosition());
            ClientCommandHandler.instance.registerCommand(new CommandIndicatorUtils());
            ClientCommandHandler.instance.registerCommand(new CommandMojangStatusCheck());
            ClientCommandHandler.instance.registerCommand(new CommandAFK());
            ClientCommandHandler.instance.registerCommand(new CommandAutoFish());
            ClientCommandHandler.instance.registerCommand(new CommandAutoLogin());
            ClientCommandHandler.instance.registerCommand(new CommandSlimeChunkSeed());
            ClientCommandHandler.instance.registerCommand(new CommandProfile());

            if (ConfigManager.enableCustomCapeFeature)
            {
                RenderingRegistry.registerEntityRenderingHandler(EntityPlayerSP.class, new RenderPlayerMOD());
                RenderingRegistry.registerEntityRenderingHandler(EntityOtherPlayerMP.class, new RenderPlayerMOD());
            }
        }
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        if (FMLCommonHandler.instance().getSide() == Side.CLIENT)
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
    }

    public static boolean isObfuscatedEnvironment()
    {
        return IndicatorUtils.DEOBFUSCATED;
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