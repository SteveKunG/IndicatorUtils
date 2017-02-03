/*******************************************************************************
 *
 * Copyright 2016 Wasinthorn Suksri/SteveKunG - Indicator Utils
 *
 ******************************************************************************/

package stevekung.mods.indicatorutils;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.relauncher.FMLInjectionData;
import net.minecraftforge.fml.relauncher.Side;
import stevekung.mods.indicatorutils.command.ClientCommandHandlerIU;
import stevekung.mods.indicatorutils.command.CommandAFK;
import stevekung.mods.indicatorutils.command.CommandAutoFish;
import stevekung.mods.indicatorutils.command.CommandAutoLogin;
import stevekung.mods.indicatorutils.command.CommandEntityDetector;
import stevekung.mods.indicatorutils.command.CommandGetPlayerPosition;
import stevekung.mods.indicatorutils.command.CommandIndicatorUtils;
import stevekung.mods.indicatorutils.command.CommandMojangStatusCheck;
import stevekung.mods.indicatorutils.command.CommandRecTemp;
import stevekung.mods.indicatorutils.command.CommandShowCape;
import stevekung.mods.indicatorutils.keybinding.KeyBindingHandler;
import stevekung.mods.indicatorutils.renderer.RenderFishIU;
import stevekung.mods.indicatorutils.renderer.RenderPlayerMOD;
import stevekung.mods.indicatorutils.utils.BlockhitAnimation;
import stevekung.mods.indicatorutils.utils.CapeUtils;
import stevekung.mods.indicatorutils.utils.IULog;
import stevekung.mods.indicatorutils.utils.JsonMessageUtils;
import stevekung.mods.indicatorutils.utils.ModSecurityManager;
import stevekung.mods.indicatorutils.utils.ReflectionUtils;
import stevekung.mods.indicatorutils.utils.ThreadMojangStatusCheck;
import stevekung.mods.indicatorutils.utils.VersionChecker;

@Mod(modid = IndicatorUtils.MOD_ID, name = IndicatorUtils.NAME, version = IndicatorUtils.VERSION, dependencies = "after:forge@[13.20.0.2201,);", clientSideOnly = true, guiFactory = IndicatorUtils.GUI_FACTORY)
public class IndicatorUtils
{
    public static final String NAME = "Indicator Utils";
    public static final String MOD_ID = "indicatorutils";
    public static final int MAJOR_VERSION = 2;
    public static final int MINOR_VERSION = 0;
    public static final int BUILD_VERSION = 1;
    public static final String VERSION = IndicatorUtils.MAJOR_VERSION + "." + IndicatorUtils.MINOR_VERSION + "." + IndicatorUtils.BUILD_VERSION;
    public static final String MC_VERSION = (String) FMLInjectionData.data()[4];
    public static final String GUI_FACTORY = "stevekung.mods.indicatorutils.ConfigGuiFactory";
    public static final boolean[] STATUS_CHECK = { false, false, false, false };
    public static String USERNAME;
    public static boolean loadCapeOnStartup = true;

    static
    {
        // Put locked user here
        ModSecurityManager.lockedWithPirateUser("MCCommanderTH", false);
        ModSecurityManager.lockedWithUUID("eef3a603-1c1b-4c98-8264-d2f04b231ef4", false);
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        ExtendedModSettings.loadExtendedSettings();
        ConfigManager.init(new File(event.getModConfigurationDirectory(), "IndicatorUtils.cfg"));
        KeyBindingHandler.initKeyBinding();
        this.initModInfo(event.getModMetadata());
        ReflectionUtils.setFinal("instance", new ClientCommandHandlerIU(), ClientCommandHandler.class, ClientCommandHandler.instance);
        IndicatorUtils.USERNAME = Minecraft.getMinecraft().getSession().getProfile().getName();
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        if (ConfigManager.mojangStatusCheckOnStartup)
        {
            IULog.info("Checking Mojang Status on startup");
            new ThreadMojangStatusCheck(true).start();
        }

        MinecraftForge.EVENT_BUS.register(new BlockhitAnimation());
        MinecraftForge.EVENT_BUS.register(new IndicatorUtilsEventHandler());
        MinecraftForge.EVENT_BUS.register(new NewChatEventHandler());
        MinecraftForge.EVENT_BUS.register(this);

        if (event.getSide() == Side.CLIENT)
        {
            ClientCommandHandler.instance.registerCommand(new CommandGetPlayerPosition());
            ClientCommandHandler.instance.registerCommand(new CommandIndicatorUtils());
            ClientCommandHandler.instance.registerCommand(new CommandMojangStatusCheck());
            ClientCommandHandler.instance.registerCommand(new CommandAFK());
            ClientCommandHandler.instance.registerCommand(new CommandRecTemp());
            ClientCommandHandler.instance.registerCommand(new CommandEntityDetector());
            ClientCommandHandler.instance.registerCommand(new CommandAutoFish());
            ClientCommandHandler.instance.registerCommand(new CommandAutoLogin());

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
            if (!ExtendedModSettings.CAPE_URL.isEmpty() && IndicatorUtils.loadCapeOnStartup)
            {
                CapeUtils.textureUploaded = true;
                CapeUtils.setCapeURL(CapeUtils.decodeCapeURL(ExtendedModSettings.CAPE_URL), true);
                IULog.info(IndicatorUtils.USERNAME + " has old cape data, continue loading...");
                IndicatorUtils.loadCapeOnStartup = false;
            }
            else
            {
                IULog.info(IndicatorUtils.USERNAME + " don't have old cape data, continue loading...");
            }
        }
    }

    // Credit to Jarbelar
    // 0 = OutOfDate, 1 = ShowDesc, 2 = NoConnection, 3 = MissingUUID
    @SubscribeEvent
    public void onCheckVersion(PlayerTickEvent event)
    {
        String URL = "https://www.mediafire.com/folder/11vdjbssscho2/Indicator_Utils";
        String changeLog = "http://pastebin.com/rJ7He59c";
        JsonMessageUtils json = new JsonMessageUtils();

        if (event.player.world.isRemote)
        {
            if (ConfigManager.enableVersionChecker)
            {
                if (!IndicatorUtils.STATUS_CHECK[2] && VersionChecker.INSTANCE.noConnection())
                {
                    event.player.sendMessage(json.text("Unable to check latest version, Please check your internet connection").setStyle(json.red()));
                    event.player.sendMessage(json.text(VersionChecker.INSTANCE.getExceptionMessage()).setStyle(json.red()));
                    IndicatorUtils.STATUS_CHECK[2] = true;
                    return;
                }
                if (!IndicatorUtils.STATUS_CHECK[0] && !IndicatorUtils.STATUS_CHECK[2] && VersionChecker.INSTANCE.isLatestVersion())
                {
                    event.player.sendMessage(json.text("New version of ").appendSibling(json.text("Indicator Utils").setStyle(json.style().setColor(TextFormatting.AQUA)).appendSibling(json.text(" is available ").setStyle(json.white()).appendSibling(json.text("v" + VersionChecker.INSTANCE.getLatestVersion().replace("[" + IndicatorUtils.MC_VERSION + "]=", "")).setStyle(json.style().setColor(TextFormatting.GREEN)).appendSibling(json.text(" for ").setStyle(json.white()).appendSibling(json.text("MC-" + IndicatorUtils.MC_VERSION).setStyle(json.style().setColor(TextFormatting.GOLD))))))));
                    event.player.sendMessage(json.text("Download Link ").setStyle(json.style().setColor(TextFormatting.YELLOW)).appendSibling(json.text("[CLICK HERE]").setStyle(json.style().setColor(TextFormatting.BLUE).setHoverEvent(json.hover(HoverEvent.Action.SHOW_TEXT, json.text("Click Here!").setStyle(json.style().setColor(TextFormatting.DARK_GREEN)))).setClickEvent(json.click(ClickEvent.Action.OPEN_URL, URL)))));
                    IndicatorUtils.STATUS_CHECK[0] = true;
                }
                if (!IndicatorUtils.STATUS_CHECK[1] && !IndicatorUtils.STATUS_CHECK[2])
                {
                    for (String log : VersionChecker.INSTANCE.getChangeLog())
                    {
                        if (ConfigManager.showChangeLogInGame)
                        {
                            event.player.sendMessage(json.text(log).setStyle(json.style().setColor(TextFormatting.GRAY).setClickEvent(json.click(ClickEvent.Action.OPEN_URL, changeLog))));
                        }
                    }
                    IndicatorUtils.STATUS_CHECK[1] = true;
                }
            }
            if (IndicatorUtils.STATUS_CHECK[3])
            {
                event.player.sendMessage(json.text("Ping will display as n/a causes by /nick command in Hypixel").setStyle(json.red().setBold(true)));
                IndicatorUtils.STATUS_CHECK[3] = false;
            }
        }
    }

    private void initModInfo(ModMetadata info)
    {
        info.autogenerated = false;
        info.modId = IndicatorUtils.MOD_ID;
        info.name = IndicatorUtils.NAME;
        info.version = IndicatorUtils.VERSION;
        info.description = "Displaying all player status, Entity info, Health, and more features!";
        info.url = "https://www.mediafire.com/folder/11vdjbssscho2/Indicator_Utils";
        info.authorList = Arrays.asList("SteveKunG");
    }

    public static boolean isObfuscatedEnvironment()
    {
        try
        {
            Minecraft.class.getField("mcDataDir");
            return true;
        }
        catch (Throwable e) {}
        return false;
    }

    public static boolean isSteveKunG()
    {
        return Minecraft.getMinecraft().getSession().getProfile().getName().equals("SteveKunG") && Minecraft.getMinecraft().getSession().getProfile().getId().equals(UUID.fromString("eef3a603-1c1b-4c98-8264-d2f04b231ef4"));
    }
}