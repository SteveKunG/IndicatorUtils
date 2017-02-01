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

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;
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

@Mod(modid = IndicatorUtils.MOD_ID, name = IndicatorUtils.NAME, version = IndicatorUtils.VERSION, dependencies = "after:Forge@[12.17.0.1976,);", clientSideOnly = true, guiFactory = IndicatorUtils.GUI_FACTORY)
public class IndicatorUtils
{
    public static final String NAME = "Indicator Utils";
    public static final String MOD_ID = "indicatorutils";
    public static final int MAJOR_VERSION = 2;
    public static final int MINOR_VERSION = 0;
    public static final int BUILD_VERSION = 0;
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
        ModSecurityManager.lockedWithUUID("66c8dd08-2aed-485f-8987-a91631b418a6", false);
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        ExtendedModSettings.loadExtendedSettings();
        ConfigManager.init(new File(event.getModConfigurationDirectory(), "IndicatorUtils.cfg"));
        KeyBindingHandler.initKeyBinding();
        IndicatorUtils.initModInfo(event.getModMetadata());
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

                    if (entry.getKey().equals("default"))
                    {
                        it.remove();
                        IULog.info("Successfully removed RenderPlayer.class.skinMap:default");
                    }
                    if (entry.getKey().equals("slim"))
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

    @SubscribeEvent
    public void onRenderTick(RenderTickEvent event)
    {
        Minecraft mc = Minecraft.getMinecraft();

        if (event.phase == Phase.END)
        {
            if (mc.currentScreen instanceof GuiIngameMenu)
            {
                int i = Mouse.getEventX() * mc.currentScreen.width / mc.displayWidth;
                int j = mc.currentScreen.height - Mouse.getEventY() * mc.currentScreen.height / mc.displayHeight - 1;
                int k = Mouse.getEventButton();
                int deltaColor = 0;
                boolean galacticraft = Loader.isModLoaded("GalacticraftCore");
                float defaultval = galacticraft ? -35.0F : 0.0F;
                boolean height = galacticraft ? j > mc.currentScreen.height - 70 && j < mc.currentScreen.height - 35 : j > mc.currentScreen.height - 35;

                if (Minecraft.IS_RUNNING_ON_MAC && k == 0 && (Keyboard.isKeyDown(29) || Keyboard.isKeyDown(157)))
                {
                    k = 1;
                }
                if (i > mc.currentScreen.width - 101 && height)
                {
                    deltaColor = 50;

                    if (k == 0)
                    {
                        if (Mouse.getEventButtonState())
                        {
                            mc.displayGuiScreen(new ConfigGuiFactory.ConfigGUI(mc.currentScreen));
                            mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                        }
                    }
                }
                GlStateManager.translate(0.0F, defaultval, 0.0F);
                this.drawGradientRect(mc.currentScreen.width - 100, mc.currentScreen.height - 35, mc.currentScreen.width, mc.currentScreen.height, this.to32BitColor(150, 10 + deltaColor, 10 + deltaColor, 10 + deltaColor), this.to32BitColor(250, 10 + deltaColor, 10 + deltaColor, 10 + deltaColor));
                mc.fontRendererObj.drawString(I18n.format("gui.indicatorutils.config.name0"), mc.currentScreen.width - 50 - mc.fontRendererObj.getStringWidth(I18n.format("gui.indicatorutils.config.name0")) / 2, mc.currentScreen.height - 26, this.to32BitColor(255, 240, 240, 240));
                mc.fontRendererObj.drawString(I18n.format("gui.indicatorutils.config.name1"), mc.currentScreen.width - 50 - mc.fontRendererObj.getStringWidth(I18n.format("gui.indicatorutils.config.name1")) / 2, mc.currentScreen.height - 16, this.to32BitColor(255, 240, 240, 240));
                Gui.drawRect(mc.currentScreen.width - 100, mc.currentScreen.height - 35, mc.currentScreen.width - 99, mc.currentScreen.height, this.to32BitColor(255, 0, 0, 0));
                Gui.drawRect(mc.currentScreen.width - 100, mc.currentScreen.height - 35, mc.currentScreen.width, mc.currentScreen.height - 34, this.to32BitColor(255, 0, 0, 0));
            }
        }
    }

    // Credit to Jarbelar
    // 0 = OutOfDate, 1 = ShowDesc, 2 = NoConnection, 3 = MissingUUID
    @SubscribeEvent
    public void onCheckVersion(PlayerTickEvent event)
    {
        String URL = "http://adf.ly/1cDWrG";
        String changeLog = "http://pastebin.com/rJ7He59c";

        if (event.player.worldObj.isRemote)
        {
            if (ConfigManager.enableVersionChecker)
            {
                if (!IndicatorUtils.STATUS_CHECK[2] && VersionChecker.INSTANCE.noConnection())
                {
                    event.player.addChatMessage(JsonMessageUtils.textToJson("Unable to check latest version, Please check your internet connection", "red"));
                    event.player.addChatMessage(JsonMessageUtils.textToJson(VersionChecker.INSTANCE.getExceptionMessage(), "red"));
                    IndicatorUtils.STATUS_CHECK[2] = true;
                    return;
                }
                if (!IndicatorUtils.STATUS_CHECK[0] && !IndicatorUtils.STATUS_CHECK[2] && VersionChecker.INSTANCE.isLatestVersion())
                {
                    event.player.addChatMessage(JsonMessageUtils.json("\"text\":\"New version of \",\"extra\":[{\"text\":\"Indicator Utils\",\"color\":\"aqua\",\"extra\":[{\"text\":\" is available \",\"color\":\"white\",\"extra\":[{\"text\":\"v" + VersionChecker.INSTANCE.getLatestVersion().replace("[" + IndicatorUtils.MC_VERSION + "]=", "") + " \",\"color\":\"green\",\"extra\":[{\"text\":\"for \",\"color\":\"white\",\"extra\":[{\"text\":\"MC-" + IndicatorUtils.MC_VERSION + "\",\"color\":\"gold\"}]}]}]}]}]"));
                    event.player.addChatMessage(JsonMessageUtils.json("\"text\":\"Download Link \",\"color\":\"yellow\",\"extra\":[{\"text\":\"[CLICK HERE]\",\"color\":\"blue\",\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"" + TextFormatting.DARK_GREEN + "Click Here!\"},\"clickEvent\":{\"action\":\"open_url\",\"value\":\"" + URL + "\"}}]"));
                    IndicatorUtils.STATUS_CHECK[0] = true;
                }
                if (!IndicatorUtils.STATUS_CHECK[1] && !IndicatorUtils.STATUS_CHECK[2])
                {
                    for (String log : VersionChecker.INSTANCE.getChangeLog())
                    {
                        if (ConfigManager.showChangeLogInGame)
                        {
                            event.player.addChatMessage(JsonMessageUtils.json("\"text\":\"" + log + "\",\"color\":\"gray\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"" + changeLog + "\"}"));
                        }
                    }
                    IndicatorUtils.STATUS_CHECK[1] = true;
                }
            }
            if (IndicatorUtils.STATUS_CHECK[3])
            {
                event.player.addChatMessage(JsonMessageUtils.json("\"text\":\"Ping will display as n/a causes by /nick command in Hypixel\",\"color\":\"red\",\"bold\":\"true\""));
                IndicatorUtils.STATUS_CHECK[3] = false;
            }
        }
    }

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent event)
    {
        if (event.getModID().equals(IndicatorUtils.MOD_ID))
        {
            ConfigManager.syncConfig(false);
        }
    }

    private static void initModInfo(ModMetadata info)
    {
        info.autogenerated = false;
        info.modId = IndicatorUtils.MOD_ID;
        info.name = IndicatorUtils.NAME;
        info.version = IndicatorUtils.VERSION;
        info.description = "Displaying all player status, Entity info, Health, etc!";
        info.url = "https://www.mediafire.com/folder/11vdjbssscho2/Indicator_Utils";
        info.authorList = Arrays.asList("SteveKunG");
    }

    public static boolean isObfuscatedEnvironment()
    {
        try
        {
            Blocks.class.getField("AIR");
            return true;
        }
        catch (Throwable e) {}
        return false;
    }

    private int to32BitColor(int a, int r, int g, int b)
    {
        a = a << 24;
        r = r << 16;
        g = g << 8;
        return a | r | g | b;
    }

    private void drawGradientRect(int left, int top, int right, int bottom, int startColor, int endColor)
    {
        float f = (startColor >> 24 & 255) / 255.0F;
        float f1 = (startColor >> 16 & 255) / 255.0F;
        float f2 = (startColor >> 8 & 255) / 255.0F;
        float f3 = (startColor & 255) / 255.0F;
        float f4 = (endColor >> 24 & 255) / 255.0F;
        float f5 = (endColor >> 16 & 255) / 255.0F;
        float f6 = (endColor >> 8 & 255) / 255.0F;
        float f7 = (endColor & 255) / 255.0F;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.shadeModel(7425);
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer worldRenderer = tessellator.getBuffer();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        worldRenderer.pos(right, top, 0.0D).color(f1, f2, f3, f).endVertex();
        worldRenderer.pos(left, top, 0.0D).color(f1, f2, f3, f).endVertex();
        worldRenderer.pos(left, bottom, 0.0D).color(f5, f6, f7, f4).endVertex();
        worldRenderer.pos(right, bottom, 0.0D).color(f5, f6, f7, f4).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }
}