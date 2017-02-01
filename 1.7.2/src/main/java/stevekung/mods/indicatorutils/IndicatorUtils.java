/*******************************************************************************
 *
 * Copyright 2016 Wasinthorn Suksri/SteveKunG - Indicator Utils
 *
 ******************************************************************************/

package stevekung.mods.indicatorutils;

import java.io.File;
import java.util.Arrays;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.RenderTickEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import cpw.mods.fml.relauncher.FMLInjectionData;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import stevekung.mods.indicatorutils.command.CommandAFK;
import stevekung.mods.indicatorutils.command.CommandAutoFish;
import stevekung.mods.indicatorutils.command.CommandAutoLogin;
import stevekung.mods.indicatorutils.command.CommandGetPlayerPosition;
import stevekung.mods.indicatorutils.command.CommandIndicatorUtils;
import stevekung.mods.indicatorutils.command.CommandMojangStatusCheck;
import stevekung.mods.indicatorutils.command.CommandRecTemp;
import stevekung.mods.indicatorutils.command.CommandShowCape;
import stevekung.mods.indicatorutils.renderer.CapeRenderer;
import stevekung.mods.indicatorutils.renderer.HealthClientRenderer;
import stevekung.mods.indicatorutils.renderer.RenderPlayerMOD;
import stevekung.mods.indicatorutils.utils.CapeUtils;
import stevekung.mods.indicatorutils.utils.ClientCommandHandlerIU;
import stevekung.mods.indicatorutils.utils.GuiNewChatIU;
import stevekung.mods.indicatorutils.utils.IULog;
import stevekung.mods.indicatorutils.utils.JsonMessageUtils;
import stevekung.mods.indicatorutils.utils.ModSecurityManager;
import stevekung.mods.indicatorutils.utils.ReflectionUtils;
import stevekung.mods.indicatorutils.utils.ThreadMojangStatusCheck;
import stevekung.mods.indicatorutils.utils.VersionChecker;

@Mod(modid = IndicatorUtils.MOD_ID, name = IndicatorUtils.NAME, version = IndicatorUtils.VERSION, dependencies = "after:Forge@[10.12.2.1147,);", guiFactory = IndicatorUtils.GUI_FACTORY)
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
        ModSecurityManager.lockedWithUUID("111e947f-3dfb-4bcc-809f-c071a415d26d", false);
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        ExtendedModSettings.loadExtendedSettings();

        if (event.getSide() == Side.CLIENT)
        {
            ConfigManager.init(new File(event.getModConfigurationDirectory(), "IndicatorUtils.cfg"));
        }
        IndicatorUtils.USERNAME = Minecraft.getMinecraft().getSession().func_148256_e().getName();
        IndicatorUtils.initModInfo(event.getModMetadata());
        KeyBindingHandler.initKeyBinding();
        ReflectionUtils.setFinal("instance", new ClientCommandHandlerIU(), ClientCommandHandler.class, ClientCommandHandler.instance);
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
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
        MinecraftForge.EVENT_BUS.register(this);
        FMLCommonHandler.instance().bus().register(this);

        if (event.getSide() == Side.CLIENT)
        {
            MinecraftForge.EVENT_BUS.register(new HealthClientRenderer());
            FMLCommonHandler.instance().bus().register(new HealthClientRenderer());
            MinecraftForge.EVENT_BUS.register(new CapeRenderer());
            FMLCommonHandler.instance().bus().register(new CapeRenderer());
            ClientCommandHandler.instance.registerCommand(new CommandGetPlayerPosition());
            ClientCommandHandler.instance.registerCommand(new CommandIndicatorUtils());
            ClientCommandHandler.instance.registerCommand(new CommandMojangStatusCheck());
            ClientCommandHandler.instance.registerCommand(new CommandAFK());
            ClientCommandHandler.instance.registerCommand(new CommandRecTemp());
            ClientCommandHandler.instance.registerCommand(new CommandAutoFish());
            ClientCommandHandler.instance.registerCommand(new CommandAutoLogin());

            if (ConfigManager.enableCustomCapeFeature)
            {
                ClientCommandHandler.instance.registerCommand(new CommandShowCape());
            }
        }
        if (ConfigManager.enableCustomCapeFeature)
        {
            RenderingRegistry.registerEntityRenderingHandler(EntityPlayerSP.class, new RenderPlayerMOD());
            RenderingRegistry.registerEntityRenderingHandler(EntityOtherPlayerMP.class, new RenderPlayerMOD());
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

                if (Minecraft.isRunningOnMac && k == 0 && (Keyboard.isKeyDown(29) || Keyboard.isKeyDown(157)))
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
                            mc.getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0F));
                        }
                    }
                }
                GL11.glTranslatef(0.0F, defaultval, 0.0F);
                this.drawGradientRect(mc.currentScreen.width - 100, mc.currentScreen.height - 35, mc.currentScreen.width, mc.currentScreen.height, this.to32BitColor(150, 10 + deltaColor, 10 + deltaColor, 10 + deltaColor), this.to32BitColor(250, 10 + deltaColor, 10 + deltaColor, 10 + deltaColor));
                mc.fontRenderer.drawString(I18n.format("gui.indicatorutils.config.name0"), mc.currentScreen.width - 50 - mc.fontRenderer.getStringWidth(I18n.format("gui.indicatorutils.config.name0")) / 2, mc.currentScreen.height - 26, this.to32BitColor(255, 240, 240, 240));
                mc.fontRenderer.drawString(I18n.format("gui.indicatorutils.config.name1"), mc.currentScreen.width - 50 - mc.fontRenderer.getStringWidth(I18n.format("gui.indicatorutils.config.name1")) / 2, mc.currentScreen.height - 16, this.to32BitColor(255, 240, 240, 240));
                Gui.drawRect(mc.currentScreen.width - 100, mc.currentScreen.height - 35, mc.currentScreen.width - 99, mc.currentScreen.height, this.to32BitColor(255, 0, 0, 0));
                Gui.drawRect(mc.currentScreen.width - 100, mc.currentScreen.height - 35, mc.currentScreen.width, mc.currentScreen.height - 34, this.to32BitColor(255, 0, 0, 0));
            }
        }
    }

    // Credit to Jarbelar
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
                    event.player.addChatMessage(JsonMessageUtils.json("\"text\":\"Download Link \",\"color\":\"yellow\",\"extra\":[{\"text\":\"[CLICK HERE]\",\"color\":\"blue\",\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"" + EnumChatFormatting.DARK_GREEN + "Click Here!\"},\"clickEvent\":{\"action\":\"open_url\",\"value\":\"" + URL + "\"}}]"));
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
        if (event.modID.equals(IndicatorUtils.MOD_ID))
        {
            ConfigManager.syncConfig(false);
        }
    }

    @SubscribeEvent
    public void onClientConnectedToServer(FMLNetworkEvent.ClientConnectedToServerEvent event)
    {
        ReflectionUtils.set(!IndicatorUtils.isObfuscatedEnvironment() ? "field_73840_e" : "persistantChatGUI", new GuiNewChatIU(Minecraft.getMinecraft()), GuiIngame.class, Minecraft.getMinecraft().ingameGUI);
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
            Blocks.class.getField("air");
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
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.setColorRGBA_F(f1, f2, f3, f);
        tessellator.addVertex(right, top, 0.0D);
        tessellator.addVertex(left, top, 0.0D);
        tessellator.setColorRGBA_F(f5, f6, f7, f4);
        tessellator.addVertex(left, bottom, 0.0D);
        tessellator.addVertex(right, bottom, 0.0D);
        tessellator.draw();
        GL11.glShadeModel(GL11.GL_FLAT);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }
}