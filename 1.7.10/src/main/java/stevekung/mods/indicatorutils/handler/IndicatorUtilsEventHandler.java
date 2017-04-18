/*******************************************************************************
 *
 * Copyright 2016 Wasinthorn Suksri/SteveKunG - Indicator Utils
 *
 ******************************************************************************/

package stevekung.mods.indicatorutils.handler;

import java.util.*;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent.KeyInputEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.RenderTickEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.ClientDisconnectionFromServerEvent;
import io.netty.channel.ChannelOption;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.*;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.event.ClickEvent;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.RenderLivingEvent;
import stevekung.mods.indicatorutils.IndicatorUtils;
import stevekung.mods.indicatorutils.config.ConfigGuiFactory;
import stevekung.mods.indicatorutils.config.ConfigManager;
import stevekung.mods.indicatorutils.config.ExtendedModSettings;
import stevekung.mods.indicatorutils.gui.*;
import stevekung.mods.indicatorutils.helper.ClientRendererHelper;
import stevekung.mods.indicatorutils.helper.GameInfoHelper;
import stevekung.mods.indicatorutils.helper.ObjectModeHelper;
import stevekung.mods.indicatorutils.helper.ObjectModeHelper.EnumDisplayMode;
import stevekung.mods.indicatorutils.helper.StatusRendererHelper;
import stevekung.mods.indicatorutils.keybinding.KeyBindingHandler;
import stevekung.mods.indicatorutils.renderer.KeystrokeRenderer;
import stevekung.mods.indicatorutils.renderer.mode.CommandBlock;
import stevekung.mods.indicatorutils.renderer.mode.Global;
import stevekung.mods.indicatorutils.renderer.mode.PvP;
import stevekung.mods.indicatorutils.renderer.mode.UHC;
import stevekung.mods.indicatorutils.utils.*;
import stevekung.mods.indicatorutils.window.WindowGameXYZ;

public class IndicatorUtilsEventHandler
{
    public static boolean AFK_ENABLED;
    public static String AFK_MODE = "idle";
    public static String AFK_REASON;
    public static int AFK_MOVE_TICK;
    public static int AFK_TICK;

    public static boolean REC_ENABLED;
    private int recTick;

    public static boolean AUTO_FISH_ENABLED;
    public static int AUTO_FISH_TICK;

    public static List<Long> L_CLICK = new ArrayList<Long>();
    public static List<Long> R_CLICK = new ArrayList<Long>();

    private int pressTime;
    private int pressOneTimeTick;
    private int pressTimeDelay;

    private int clearChatTick;
    private Minecraft mc;
    private JsonUtils json;

    public static List<String> playerList = Lists.<String>newArrayList();
    public static Map<String, Integer> playerPingMap = Maps.<String, Integer>newHashMap();
    public static boolean setTCPNoDelay = false;
    private static boolean windowStartup = true;

    public IndicatorUtilsEventHandler()
    {
        this.mc = Minecraft.getMinecraft();
        this.json = new JsonUtils();
    }

    // Credit to Jarbelar
    // 0 = ShowDesc, 1 = NoConnection
    @SubscribeEvent
    public void onCheckVersion(PlayerTickEvent event)
    {
        String changeLog = "http://pastebin.com/rJ7He59c";
        JsonUtils json = new JsonUtils();

        if (event.player.worldObj.isRemote)
        {
            if (ConfigManager.enableVersionChecker)
            {
                if (!IndicatorUtils.STATUS_CHECK[1] && VersionChecker.INSTANCE.noConnection())
                {
                    event.player.addChatMessage(json.text("Unable to check latest version, Please check your internet connection").setChatStyle(json.red()));
                    event.player.addChatMessage(json.text(VersionChecker.INSTANCE.getExceptionMessage()).setChatStyle(json.red()));
                    IndicatorUtils.STATUS_CHECK[1] = true;
                    return;
                }
                if (!IndicatorUtils.STATUS_CHECK[0] && !IndicatorUtils.STATUS_CHECK[1])
                {
                    for (String log : VersionChecker.INSTANCE.getChangeLog())
                    {
                        if (ConfigManager.showChangeLogInGame)
                        {
                            event.player.addChatMessage(json.text(log).setChatStyle(json.style().setColor(EnumChatFormatting.GRAY).setChatClickEvent(json.click(ClickEvent.Action.OPEN_URL, changeLog))));
                        }
                    }
                    IndicatorUtils.STATUS_CHECK[0] = true;
                }
            }
        }
    }

    @SubscribeEvent
    public void onClientConnectedToServer(ClientConnectedToServerEvent event)
    {
        Minecraft.getMinecraft().ingameGUI.persistantChatGUI = new GuiNewChatIU(Minecraft.getMinecraft());
        IndicatorUtilsEventHandler.setTCPNoDelay = true;
    }

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent event)
    {
        if (event.modID.equalsIgnoreCase(IndicatorUtils.MOD_ID))
        {
            ConfigManager.syncConfig(false);
        }
    }

    @SubscribeEvent
    public void onClientTick(ClientTickEvent event)
    {
        this.replaceChatGUI();
        this.initWindow();

        if (this.mc.getNetHandler() != null && IndicatorUtilsEventHandler.setTCPNoDelay)
        {
            this.mc.getNetHandler().getNetworkManager().channel().config().setOption(ChannelOption.TCP_NODELAY, true);
            IULog.info("Set TCP_NODELAY to true");
            IndicatorUtilsEventHandler.setTCPNoDelay = false;
        }
        if (this.mc.thePlayer != null)
        {
            NetHandlerPlayClient handler = this.mc.thePlayer.sendQueue;
            @SuppressWarnings("unchecked")
            List<GuiPlayerInfo> players = handler.playerInfoList;
            int maxPlayers = handler.currentServerMaxPlayers;

            for (int i = 0; i < maxPlayers; i++)
            {
                if (i < players.size())
                {
                    GuiPlayerInfo player = players.get(i);
                    playerList.add(player.name);
                    Set<String> hs = new HashSet<String>();
                    hs.addAll(playerList);
                    playerList.clear();
                    playerList.addAll(hs);
                    playerPingMap.put(player.name, player.responseTime);
                }
            }
        }

        if (event.phase == Phase.START)
        {
            this.runAFK();
            this.runAutoFish();
            this.runAutoClearChat();

            if (this.pressTimeDelay > 0)
            {
                --this.pressTimeDelay;
            }
            if (this.pressOneTimeTick > 0)
            {
                --this.pressOneTimeTick;
            }
            if (this.pressOneTimeTick == 0)
            {
                this.pressTime = 0;
            }

            if (IndicatorUtilsEventHandler.REC_ENABLED)
            {
                ++this.recTick;
            }
            else
            {
                this.recTick = 0;
            }
        }
        if (this.mc.thePlayer != null && !(this.mc.thePlayer.movementInput instanceof MovementInputFromOptionsIU))
        {
            this.mc.thePlayer.movementInput = new MovementInputFromOptionsIU(this.mc.gameSettings);
        }
        GuiIngameForge.renderBossHealth = ConfigManager.renderBossHealthBar;
        GuiIngameForge.renderObjective = ConfigManager.renderScoreboard;
    }

    @SubscribeEvent
    public void onPlayerLoggedOut(PlayerLoggedOutEvent event)
    {
        this.stopCommandTick();
    }

    @SubscribeEvent
    public void onPlayerRespawn(PlayerRespawnEvent event)
    {
        this.stopCommandTick();
    }

    @SubscribeEvent
    public void onDisconnectedFromServerEvent(ClientDisconnectionFromServerEvent event)
    {
        this.stopCommandTick();
        IndicatorUtilsEventHandler.playerList.clear();
        IndicatorUtilsEventHandler.playerPingMap.clear();
    }

    @SubscribeEvent
    public void onMouseClick(MouseEvent event)
    {
        if (event.button == 0 && event.buttonstate)
        {
            IndicatorUtilsEventHandler.L_CLICK.add(Long.valueOf(System.currentTimeMillis()));
        }
        if (event.button == 1 && event.buttonstate)
        {
            IndicatorUtilsEventHandler.R_CLICK.add(Long.valueOf(System.currentTimeMillis()));
        }
    }

    @SubscribeEvent
    public void onPreRender(RenderGameOverlayEvent.Pre event)
    {
        Minecraft mc = Minecraft.getMinecraft();

        if (event.type == ElementType.PLAYER_LIST)
        {
            event.setCanceled(true);
            ScoreObjective scoreobjective = mc.theWorld.getScoreboard().func_96539_a(0);
            NetHandlerPlayClient handler = mc.thePlayer.sendQueue;
            @SuppressWarnings("unchecked")
            List<GuiPlayerInfo> players = handler.playerInfoList;
            int maxPlayers = handler.currentServerMaxPlayers;
            int width = event.resolution.getScaledWidth();

            if (mc.gameSettings.keyBindPlayerList.getIsKeyPressed() && (!mc.isIntegratedServerRunning() || handler.playerInfoList.size() > 1 || scoreobjective != null))
            {
                int rows = maxPlayers;
                int columns = 1;

                for (columns = 1; rows > 20; rows = (maxPlayers + columns - 1) / columns)
                {
                    columns++;
                }

                int columnWidth = 300 / columns;

                if (columnWidth > 150)
                {
                    columnWidth = 150;
                }

                int left = (width - columns * columnWidth) / 2;
                byte border = 10;
                Gui.drawRect(left - 1, border - 1, left + columnWidth * columns, border + 9 * rows, Integer.MIN_VALUE);

                for (int i = 0; i < maxPlayers; i++)
                {
                    int xPos = left + i % columns * columnWidth;
                    int yPos = border + i / columns * 9;
                    Gui.drawRect(xPos, yPos, xPos + columnWidth - 1, yPos + 8, 553648127);
                    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                    GL11.glEnable(GL11.GL_ALPHA_TEST);

                    if (i < players.size())
                    {
                        GuiPlayerInfo player = players.get(i);
                        ScorePlayerTeam team = mc.theWorld.getScoreboard().getPlayersTeam(player.name);
                        String displayName = ScorePlayerTeam.formatPlayerName(team, player.name);
                        mc.fontRenderer.drawStringWithShadow(displayName, xPos, yPos, 16777215);

                        if (scoreobjective != null)
                        {
                            int endX = xPos + mc.fontRenderer.getStringWidth(displayName) + 5;
                            int maxX = xPos + columnWidth - 12 - 5;

                            if (maxX - endX > 5)
                            {
                                Score score = scoreobjective.getScoreboard().func_96529_a(player.name, scoreobjective);
                                String scoreDisplay = EnumChatFormatting.YELLOW + "" + score.getScorePoints();
                                mc.fontRenderer.drawStringWithShadow(scoreDisplay, maxX - mc.fontRenderer.getStringWidth(scoreDisplay), yPos, 16777215);
                            }
                        }

                        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                        int ping = player.responseTime;

                        if (ConfigManager.playerPingMode.equalsIgnoreCase("default"))
                        {
                            mc.getTextureManager().bindTexture(Gui.icons);
                            int pingIndex = 4;

                            if (ping < 0)
                            {
                                pingIndex = 5;
                            }
                            else if (ping < 150)
                            {
                                pingIndex = 0;
                            }
                            else if (ping < 300)
                            {
                                pingIndex = 1;
                            }
                            else if (ping < 600)
                            {
                                pingIndex = 2;
                            }
                            else if (ping < 1000)
                            {
                                pingIndex = 3;
                            }
                            mc.ingameGUI.zLevel = 100.0F;
                            mc.ingameGUI.drawTexturedModalRect(xPos + columnWidth - 12, yPos, 0, 176 + pingIndex * 8, 10, 8);
                            mc.ingameGUI.zLevel = -100.0F;
                        }
                        else
                        {
                            EnumTextColor color = EnumTextColor.GREEN;

                            if (ping >= 200 && ping <= 300)
                            {
                                color = EnumTextColor.YELLOW;
                            }
                            else if (ping >= 301 && ping <= 499)
                            {
                                color = EnumTextColor.RED;
                            }
                            else if (ping >= 500)
                            {
                                color = EnumTextColor.DARK_RED;
                            }
                            ClientRendererHelper.drawString(String.valueOf(ping), xPos + columnWidth - 1 - mc.fontRenderer.getStringWidth(String.valueOf(ping)), yPos + 0.5F, color, true);
                        }
                    }
                }
            }
        }
        if (event.type == ElementType.BOSSHEALTH)
        {
            event.setCanceled(true);
            GL11.glEnable(GL11.GL_BLEND);

            if (BossStatus.bossName != null && BossStatus.statusBarTime > 0)
            {
                --BossStatus.statusBarTime;
                FontRenderer fontrenderer = mc.fontRenderer;
                ScaledResolution scaledresolution = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
                int i = scaledresolution.getScaledWidth();
                short short1 = 182;
                int j = i / 2 - short1 / 2;
                int k = (int)(BossStatus.healthScale * (short1 + 1));
                byte b0 = 12;

                if (!ConfigManager.hideBossHealthBar)
                {
                    mc.ingameGUI.drawTexturedModalRect(j, b0, 0, 74, short1, 5);
                    mc.ingameGUI.drawTexturedModalRect(j, b0, 0, 74, short1, 5);

                    if (k > 0)
                    {
                        mc.ingameGUI.drawTexturedModalRect(j, b0, 0, 79, k, 5);
                    }
                }
                String s = BossStatus.bossName;
                fontrenderer.drawStringWithShadow(s, i / 2 - fontrenderer.getStringWidth(s) / 2, b0 - 10, 16777215);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                mc.getTextureManager().bindTexture(Gui.icons);
            }
            GL11.glDisable(GL11.GL_BLEND);
        }
    }

    @SubscribeEvent
    public void onRenderChat(RenderGameOverlayEvent.Chat event)
    {
        if (event.type == ElementType.CHAT)
        {
            event.setCanceled(true);
            GL11.glPushMatrix();
            GL11.glTranslatef(0, event.resolution.getScaledHeight() - 48, 0.0F);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            Minecraft.getMinecraft().ingameGUI.getChatGUI().drawChat(Minecraft.getMinecraft().ingameGUI.getUpdateCounter());
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glPopMatrix();
        }
    }

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent event)
    {
        if (event.type == ElementType.TEXT)
        {
            if (ConfigManager.enableKeystroke)
            {
                if (!this.mc.gameSettings.hideGUI)
                {
                    if (this.mc.currentScreen == null || this.mc.currentScreen instanceof GuiChat || this.mc.currentScreen instanceof GuiRenderStatusSettings)
                    {
                        KeystrokeRenderer.init(this.mc);
                    }
                }
            }
            if (ConfigManager.enableAllRenderInfo)
            {
                if (ObjectModeHelper.getDisplayMode(EnumDisplayMode.UHC))
                {
                    UHC.init(this.mc);
                }
                else if (ObjectModeHelper.getDisplayMode(EnumDisplayMode.PVP))
                {
                    PvP.init(this.mc);
                }
                else if (ObjectModeHelper.getDisplayMode(EnumDisplayMode.COMMAND_BLOCK))
                {
                    CommandBlock.init(this.mc);
                }
                else
                {
                    Global.init(this.mc);
                }

                if (ConfigManager.enableCPS)
                {
                    String cps = this.json.text("CPS: ").setChatStyle(this.json.colorFromConfig(ConfigManager.customColorCPS)).getFormattedText();
                    String rps = ConfigManager.enableRPS ? this.json.text(" RPS: ").setChatStyle(this.json.colorFromConfig(ConfigManager.customColorRPS)).getFormattedText() : "";
                    String cpsValue = this.json.text(String.valueOf(GameInfoHelper.INSTANCE.getCPS())).setChatStyle(this.json.colorFromConfig(ConfigManager.customColorCPSValue)).getFormattedText();
                    String rpsValue = ConfigManager.enableRPS ? this.json.text(String.valueOf(GameInfoHelper.INSTANCE.getRPS())).setChatStyle(this.json.colorFromConfig(ConfigManager.customColorRPSValue)).getFormattedText() : "";

                    if (ConfigManager.useCustomTextCPS)
                    {
                        cps = JsonUtils.rawTextToJson(ConfigManager.customTextCPS).getFormattedText();
                    }
                    if (ConfigManager.useCustomTextRPS)
                    {
                        rps = JsonUtils.rawTextToJson(ConfigManager.customTextRPS).getFormattedText();
                    }
                    if (ExtendedModSettings.CPS_POSITION.equalsIgnoreCase("record"))
                    {
                        ClientRendererHelper.drawStringAtRecord(cps + cpsValue + rps + rpsValue, event.partialTicks);
                    }
                    if (ExtendedModSettings.CPS_POSITION.equalsIgnoreCase("custom"))
                    {
                        ClientRendererHelper.drawRectNew(ExtendedModSettings.CPS_X_OFFSET, ExtendedModSettings.CPS_Y_OFFSET, ExtendedModSettings.CPS_X_OFFSET + this.mc.fontRenderer.getStringWidth(cps + cpsValue + rps + rpsValue) + 4, ExtendedModSettings.CPS_Y_OFFSET + 11, 16777216, ExtendedModSettings.CPS_OPACITY);
                        ClientRendererHelper.drawString(cps + cpsValue + rps + rpsValue, ExtendedModSettings.CPS_X_OFFSET + 2, ExtendedModSettings.CPS_Y_OFFSET + 2, 16777215, true);
                    }
                }

                if (IndicatorUtilsEventHandler.REC_ENABLED)
                {
                    ScaledResolution res = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
                    EnumTextColor color = EnumTextColor.WHITE;

                    if (this.recTick % 24 >= 0 && this.recTick % 24 <= 12)
                    {
                        color = EnumTextColor.RED;
                    }
                    ClientRendererHelper.drawString("REC: " + GameInfoHelper.INSTANCE.ticksToElapsedTime(this.recTick), res.getScaledWidth() - this.mc.fontRenderer.getStringWidth("REC: " + GameInfoHelper.INSTANCE.ticksToElapsedTime(this.recTick)) - 2, res.getScaledHeight() - 10, color, true);
                }
            }
        }
    }

    @SubscribeEvent
    public void onPressKey(KeyInputEvent event)
    {
        if (Minecraft.getMinecraft().currentScreen == null && Minecraft.getMinecraft().gameSettings.keyBindCommand.isPressed())
        {
            Minecraft.getMinecraft().displayGuiScreen(new GuiNewChatSettings("/"));
        }
        if (ConfigManager.enableCustomCapeFeature)
        {
            if (KeyBindingHandler.KEY_OPEN_CAPE_DOWNLOADER_GUI != null && KeyBindingHandler.KEY_OPEN_CAPE_DOWNLOADER_GUI.getIsKeyPressed())
            {
                Minecraft mc = Minecraft.getMinecraft();
                mc.displayGuiScreen(new GuiCapeDownloader());
            }
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_F3) && Keyboard.isKeyDown(Keyboard.KEY_D))
        {
            Minecraft.getMinecraft().ingameGUI.getChatGUI().clearChatMessages();
        }
        if (KeyBindingHandler.KEY_REC_COMMAND.getIsKeyPressed())
        {
            if (IndicatorUtilsEventHandler.REC_ENABLED)
            {
                IndicatorUtilsEventHandler.REC_ENABLED = false;
            }
            else
            {
                IndicatorUtilsEventHandler.REC_ENABLED = true;
            }
        }
        if (ExtendedModSettings.DISPLAY_MODE_USE_MODE.equalsIgnoreCase("keybinding"))
        {
            String[] keyNext = ConfigManager.keyDisplayModeNext.split(",");
            String[] keyPrevious = ConfigManager.keyDisplayModePrevious.split(",");
            int keyNextCtrl = GameInfoHelper.INSTANCE.parseInt(keyNext[0], "Display Mode");
            int keyNextOther = GameInfoHelper.INSTANCE.parseInt(keyNext[1], "Display Mode");
            int keyPreviousCtrl = GameInfoHelper.INSTANCE.parseInt(keyPrevious[0], "Display Mode");
            int keyPreviousOther = GameInfoHelper.INSTANCE.parseInt(keyPrevious[1], "Display Mode");

            if (Keyboard.isKeyDown(keyNextCtrl) && Keyboard.isKeyDown(keyNextOther))
            {
                StatusRendererHelper.INSTANCE.enumRenderMode = (StatusRendererHelper.INSTANCE.enumRenderMode + 1) % 4;
                StatusRendererHelper.INSTANCE.setDisplayMode(StatusRendererHelper.INSTANCE.enumRenderMode);
            }
            if (Keyboard.isKeyDown(keyPreviousCtrl) && Keyboard.isKeyDown(keyPreviousOther))
            {
                StatusRendererHelper.INSTANCE.enumRenderMode = (StatusRendererHelper.INSTANCE.enumRenderMode - 1) % 4;

                if (StatusRendererHelper.INSTANCE.enumRenderMode < 0)
                {
                    StatusRendererHelper.INSTANCE.enumRenderMode = 3;
                }
                StatusRendererHelper.INSTANCE.setDisplayMode(StatusRendererHelper.INSTANCE.enumRenderMode);
            }
        }
        if (ExtendedModSettings.TOGGLE_SPRINT_USE_MODE.equalsIgnoreCase("keybinding"))
        {
            String[] keyTS = ConfigManager.keyToggleSprint.split(",");
            int keyTGCtrl = GameInfoHelper.INSTANCE.parseInt(keyTS[0], "Toggle Sprint");
            int keyTGOther = GameInfoHelper.INSTANCE.parseInt(keyTS[1], "Toggle Sprint");

            if (Keyboard.isKeyDown(keyTGCtrl) && Keyboard.isKeyDown(keyTGOther))
            {
                if (ExtendedModSettings.TOGGLE_SPRINT)
                {
                    ExtendedModSettings.TOGGLE_SPRINT = false;
                    Minecraft.getMinecraft().ingameGUI.func_110326_a(new JsonUtils().text("Toggle Sprint Disabled").getFormattedText(), false);
                }
                else
                {
                    ExtendedModSettings.TOGGLE_SPRINT = true;
                    Minecraft.getMinecraft().ingameGUI.func_110326_a(new JsonUtils().text("Toggle Sprint Enabled").getFormattedText(), false);
                }
                ExtendedModSettings.saveExtendedSettings();
            }
        }
        if (ExtendedModSettings.TOGGLE_SNEAK_USE_MODE.equalsIgnoreCase("keybinding"))
        {
            String[] keyTS = ConfigManager.keyToggleSneak.split(",");
            int keyTGCtrl = GameInfoHelper.INSTANCE.parseInt(keyTS[0], "Toggle Sneak");
            int keyTGOther = GameInfoHelper.INSTANCE.parseInt(keyTS[1], "Toggle Sneak");

            if (Keyboard.isKeyDown(keyTGCtrl) && Keyboard.isKeyDown(keyTGOther))
            {
                if (ExtendedModSettings.TOGGLE_SNEAK)
                {
                    ExtendedModSettings.TOGGLE_SNEAK = false;
                    Minecraft.getMinecraft().ingameGUI.func_110326_a(new JsonUtils().text("Toggle Sneak Disabled").getFormattedText(), false);
                }
                else
                {
                    ExtendedModSettings.TOGGLE_SNEAK = true;
                    Minecraft.getMinecraft().ingameGUI.func_110326_a(new JsonUtils().text("Toggle Sneak Enabled").getFormattedText(), false);
                }
                ExtendedModSettings.saveExtendedSettings();
            }
        }
        if (ExtendedModSettings.AUTO_SWIM_USE_MODE.equalsIgnoreCase("keybinding"))
        {
            String[] keyAW = ConfigManager.keyAutoSwim.split(",");
            int keyAWCtrl = GameInfoHelper.INSTANCE.parseInt(keyAW[0], "Auto Swim");
            int keyAWOther = GameInfoHelper.INSTANCE.parseInt(keyAW[1], "Auto Swim");

            if (Keyboard.isKeyDown(keyAWCtrl) && Keyboard.isKeyDown(keyAWOther))
            {
                if (ExtendedModSettings.AUTO_SWIM)
                {
                    ExtendedModSettings.AUTO_SWIM = false;
                    Minecraft.getMinecraft().ingameGUI.func_110326_a(new JsonUtils().text("Auto Swim Disabled").getFormattedText(), false);
                }
                else
                {
                    ExtendedModSettings.AUTO_SWIM = true;
                    Minecraft.getMinecraft().ingameGUI.func_110326_a(new JsonUtils().text("Auto Swim Enabled").getFormattedText(), false);
                }
                ExtendedModSettings.saveExtendedSettings();
            }
        }
        if (ConfigManager.enableEndGameChatMessage)
        {
            if (KeyBindingHandler.KEY_END_GAME_MESSAGE.getIsKeyPressed())
            {
                if (this.pressTime <= 2 && this.pressTimeDelay <= 0 && this.pressOneTimeTick >= 0)
                {
                    ++this.pressTime;
                }
                if (this.pressTimeDelay <= 0)
                {
                    this.pressOneTimeTick = 10;
                }
            }
            if (this.pressTime >= 2)
            {
                Minecraft mc = Minecraft.getMinecraft();
                this.pressTime = 0;
                this.pressTimeDelay = 200;
                mc.thePlayer.sendChatMessage(ConfigManager.endGameChatMessage);
            }
        }
    }

    @SubscribeEvent
    public void onRenderHealthStatus(RenderLivingEvent.Specials.Post event)
    {
        Minecraft mc = Minecraft.getMinecraft();
        EntityLivingBase entity = event.entity;
        float health = entity.getHealth() + entity.getAbsorptionAmount();
        boolean halfHealth = health <= entity.getMaxHealth() / 2F;
        boolean halfHealth1 = health <= entity.getMaxHealth() / 4F;
        float range = entity.isSneaking() ? RendererLivingEntity.NAME_TAG_RANGE_SNEAK : RendererLivingEntity.NAME_TAG_RANGE;
        double distance = entity.getDistanceSqToEntity(mc.renderViewEntity);
        String status = ConfigManager.healthStatusMode;
        boolean flag = true;
        String color = "green";

        if (halfHealth)
        {
            color = "red";
        }
        if (halfHealth1)
        {
            color = "dark_red";
        }

        if (status.equals("DISABLE"))
        {
            flag = false;
        }
        else if (status.equals("POINTED"))
        {
            flag = entity == mc.pointedEntity;
        }

        if (distance < range * range)
        {
            if (!mc.gameSettings.hideGUI && !entity.isInvisible() && flag)
            {
                String heart = new JsonUtils().text("\u2764 ").setChatStyle(new JsonUtils().colorFromConfig(color)).getFormattedText();
                StatusRendererHelper.renderHealthStatus(entity, heart + String.format("%.1f", health), event.x, event.y, event.z, entity.getDistanceSqToEntity(mc.renderViewEntity));
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
                ClientRendererHelper.drawGradientRect(mc.currentScreen.width - 100, mc.currentScreen.height - 35, mc.currentScreen.width, mc.currentScreen.height, ClientRendererHelper.to32BitColor(150, 10 + deltaColor, 10 + deltaColor, 10 + deltaColor), ClientRendererHelper.to32BitColor(250, 10 + deltaColor, 10 + deltaColor, 10 + deltaColor));
                mc.fontRenderer.drawString(I18n.format("gui.indicatorutils.config.name0"), mc.currentScreen.width - 50 - mc.fontRenderer.getStringWidth(I18n.format("gui.indicatorutils.config.name0")) / 2, mc.currentScreen.height - 26, ClientRendererHelper.to32BitColor(255, 240, 240, 240));
                mc.fontRenderer.drawString(I18n.format("gui.indicatorutils.config.name1"), mc.currentScreen.width - 50 - mc.fontRenderer.getStringWidth(I18n.format("gui.indicatorutils.config.name1")) / 2, mc.currentScreen.height - 16, ClientRendererHelper.to32BitColor(255, 240, 240, 240));
                Gui.drawRect(mc.currentScreen.width - 100, mc.currentScreen.height - 35, mc.currentScreen.width - 99, mc.currentScreen.height, ClientRendererHelper.to32BitColor(255, 0, 0, 0));
                Gui.drawRect(mc.currentScreen.width - 100, mc.currentScreen.height - 35, mc.currentScreen.width, mc.currentScreen.height - 34, ClientRendererHelper.to32BitColor(255, 0, 0, 0));
            }
        }
    }

    private void runAutoFish()
    {
        if (IndicatorUtilsEventHandler.AUTO_FISH_ENABLED)
        {
            ++IndicatorUtilsEventHandler.AUTO_FISH_TICK;
            IndicatorUtilsEventHandler.AUTO_FISH_TICK %= 4;
            boolean flag = true;

            if (this.mc.thePlayer != null && this.mc.objectMouseOver != null && this.mc.theWorld != null && this.mc.playerController != null && this.mc.entityRenderer != null)
            {
                if (IndicatorUtilsEventHandler.AUTO_FISH_TICK % 4 == 0)
                {
                    ItemStack itemstack = this.mc.thePlayer.getCurrentEquippedItem();

                    if (itemstack != null && itemstack.getItem() == Items.fishing_rod)
                    {
                        if (this.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
                        {
                            int i = this.mc.objectMouseOver.blockX;
                            int j = this.mc.objectMouseOver.blockY;
                            int k = this.mc.objectMouseOver.blockZ;

                            if (!this.mc.theWorld.getBlock(i, j, k).isAir(this.mc.theWorld, i, j, k))
                            {
                                int l = itemstack != null ? itemstack.stackSize : 0;

                                if (this.mc.playerController.onPlayerRightClick(this.mc.thePlayer, this.mc.theWorld, itemstack, i, j, k, this.mc.objectMouseOver.sideHit, this.mc.objectMouseOver.hitVec))
                                {
                                    flag = false;
                                    this.mc.thePlayer.swingItem();
                                }
                                if (itemstack.stackSize == 0)
                                {
                                    this.mc.thePlayer.inventory.mainInventory[this.mc.thePlayer.inventory.currentItem] = null;
                                }
                                else if (itemstack.stackSize != l || this.mc.playerController.isInCreativeMode())
                                {
                                    this.mc.entityRenderer.itemRenderer.resetEquippedProgress();
                                }
                            }
                        }

                        if (flag)
                        {
                            ItemStack itemstack1 = this.mc.thePlayer.inventory.getCurrentItem();

                            if (itemstack1 != null && this.mc.playerController.sendUseItem(this.mc.thePlayer, this.mc.theWorld, itemstack1))
                            {
                                this.mc.entityRenderer.itemRenderer.resetEquippedProgress2();
                            }
                        }
                    }
                    else
                    {
                        IndicatorUtilsEventHandler.AUTO_FISH_ENABLED = false;
                        IndicatorUtilsEventHandler.AUTO_FISH_TICK = 0;
                        this.mc.thePlayer.addChatMessage(new JsonUtils().text("Stop using /autofish command, you must hold the fishing rod!"));
                        return;
                    }
                }
            }
        }
        else
        {
            IndicatorUtilsEventHandler.AUTO_FISH_TICK = 0;
        }
    }

    private void runAFK()
    {
        if (IndicatorUtilsEventHandler.AFK_ENABLED)
        {
            ++IndicatorUtilsEventHandler.AFK_TICK;
            int tick = IndicatorUtilsEventHandler.AFK_TICK;
            int messageMin = 1200 * ConfigManager.afkMessageTime;
            String s = "s";

            if (tick == 0)
            {
                s = "";
            }
            if (ConfigManager.enableAFKMessage)
            {
                if (tick % messageMin == 0)
                {
                    if (this.mc.thePlayer != null)
                    {
                        String reason = IndicatorUtilsEventHandler.AFK_REASON;

                        if (reason.isEmpty())
                        {
                            reason = "";
                        }
                        else
                        {
                            reason = ", Reason : " + reason;
                        }
                        this.mc.thePlayer.sendChatMessage("AFK : " + GameInfoHelper.INSTANCE.ticksToElapsedTime(tick) + " minute" + s + reason);
                    }
                }
            }

            if (IndicatorUtilsEventHandler.AFK_MODE.equalsIgnoreCase("idle"))
            {
                if (this.mc.thePlayer != null)
                {
                    float angle = 0;

                    if (tick % 2 == 0)
                    {
                        angle = 0.0001F;
                    }
                    else
                    {
                        angle = -0.0001F;
                    }
                    this.mc.thePlayer.setAngles(angle, angle);
                }
            }
            else if (IndicatorUtilsEventHandler.AFK_MODE.equalsIgnoreCase("360"))
            {
                if (this.mc.thePlayer != null)
                {
                    this.mc.thePlayer.setAngles(5.0F, 0.0F);
                }
            }
            else if (IndicatorUtilsEventHandler.AFK_MODE.equalsIgnoreCase("360move"))
            {
                if (this.mc.thePlayer != null)
                {
                    this.mc.thePlayer.setAngles(5.0F, 0.0F);
                }
                ++IndicatorUtilsEventHandler.AFK_MOVE_TICK;
                IndicatorUtilsEventHandler.AFK_MOVE_TICK %= 8;
            }
            else
            {
                if (this.mc.thePlayer != null)
                {
                    float angle = 0;

                    if (tick % 2 == 0)
                    {
                        angle = 0.0001F;
                    }
                    else
                    {
                        angle = -0.0001F;
                    }
                    this.mc.thePlayer.setAngles(angle, angle);
                }
                ++IndicatorUtilsEventHandler.AFK_MOVE_TICK;
                IndicatorUtilsEventHandler.AFK_MOVE_TICK %= 8;
            }
        }
        else
        {
            IndicatorUtilsEventHandler.AFK_TICK = 0;
        }
    }

    private void runAutoClearChat()
    {
        if (ExtendedModSettings.AUTO_CLEAR_CHAT)
        {
            ++this.clearChatTick;
            int chatTick = this.clearChatTick / 20;

            if (chatTick % ExtendedModSettings.AUTO_CLEAR_CHAT_TIME == 0)
            {
                if (this.mc.ingameGUI.getChatGUI() instanceof GuiNewChatIU)
                {
                    GuiNewChatIU chat = (GuiNewChatIU) this.mc.ingameGUI.getChatGUI();

                    if (ExtendedModSettings.AUTO_CLEAR_CHAT_MODE.equalsIgnoreCase("onlychat"))
                    {
                        chat.chatLines.clear();
                        chat.field_146253_i.clear();
                    }
                    else if (ExtendedModSettings.AUTO_CLEAR_CHAT_MODE.equalsIgnoreCase("onlysentmessage"))
                    {
                        chat.getSentMessages().clear();
                    }
                    else
                    {
                        chat.clearChatMessages();
                    }
                }
            }
        }
        else
        {
            this.clearChatTick = 0;
        }
    }

    private void replaceChatGUI()
    {
        if (this.mc.currentScreen != null)
        {
            if (this.mc.currentScreen instanceof GuiChat && !(this.mc.currentScreen instanceof GuiNewChatSettings || this.mc.currentScreen instanceof GuiSleepMP))
            {
                this.mc.displayGuiScreen(new GuiNewChatSettings());
            }
            if (this.mc.currentScreen instanceof GuiSleepMP && !(this.mc.currentScreen instanceof GuiNewSleepMP))
            {
                this.mc.displayGuiScreen(new GuiNewSleepMP());
            }
            if (this.mc.currentScreen instanceof GuiNewSleepMP && !this.mc.thePlayer.isPlayerSleeping())
            {
                this.mc.displayGuiScreen((GuiScreen)null);
            }
        }
    }

    private void stopCommandTick()
    {
        if (IndicatorUtilsEventHandler.AFK_ENABLED)
        {
            IndicatorUtilsEventHandler.AFK_ENABLED = false;
            IndicatorUtilsEventHandler.AFK_REASON = "";
            IndicatorUtilsEventHandler.AFK_TICK = 0;
            IndicatorUtilsEventHandler.AFK_MOVE_TICK = 0;
            IndicatorUtilsEventHandler.AFK_MODE = "idle";
            IULog.info("Stopping AFK Command");
        }
        if (IndicatorUtilsEventHandler.AUTO_FISH_ENABLED)
        {
            IndicatorUtilsEventHandler.AUTO_FISH_ENABLED = false;
            IndicatorUtilsEventHandler.AUTO_FISH_TICK = 0;
            IULog.info("Stopping AutoFish Command");
        }
    }

    private void initWindow()
    {
        if (IndicatorUtilsEventHandler.windowStartup)
        {
            new WindowGameXYZ().setVisible(ConfigManager.enableXYZWindow);
            IndicatorUtilsEventHandler.windowStartup = false;
        }

        String windowText1 = "";
        String windowText2 = "";

        if (this.mc.renderViewEntity != null)
        {
            int x = MathHelper.floor_double(this.mc.thePlayer.posX);
            int y = MathHelper.floor_double(this.mc.thePlayer.boundingBox.minY);
            int z = MathHelper.floor_double(this.mc.thePlayer.posZ);

            if (ConfigManager.enableOverworldCoordinate && this.mc.thePlayer.dimension == -1)
            {
                windowText1 = "Overworld " + "XYZ: " + x * 8 + " " + y + " " + z * 8;
            }
            String inNether = this.mc.thePlayer.dimension == -1 ? "Nether " : "";
            windowText2 = inNether + "XYZ: " + x + " " + y + " " + z;
            WindowGameXYZ.label.setText("<html>" + "<div style='text-align: center;'>" + windowText1 + "<br>" + windowText2 + "</div>" + "</html>");
        }
    }
}