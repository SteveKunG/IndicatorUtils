/*******************************************************************************
 *
 * Copyright 2016 Wasinthorn Suksri/SteveKunG - Indicator Utils
 *
 ******************************************************************************/

package stevekung.mods.indicatorutils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent.KeyInputEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.RenderTickEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.ClientDisconnectionFromServerEvent;
import io.netty.channel.ChannelOption;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.gui.GuiPlayerInfo;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSleepMP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import stevekung.mods.indicatorutils.renderer.KeystrokeRenderer;
import stevekung.mods.indicatorutils.renderer.statusmode.CommandBlockStatusRenderer;
import stevekung.mods.indicatorutils.renderer.statusmode.GlobalStatusRenderer;
import stevekung.mods.indicatorutils.renderer.statusmode.PvPStatusRenderer;
import stevekung.mods.indicatorutils.renderer.statusmode.UHCStatusRenderer;
import stevekung.mods.indicatorutils.utils.EnumTextColor;
import stevekung.mods.indicatorutils.utils.GuiCapeDownloader;
import stevekung.mods.indicatorutils.utils.GuiNewChatSettings;
import stevekung.mods.indicatorutils.utils.GuiNewSleepMP;
import stevekung.mods.indicatorutils.utils.IULog;
import stevekung.mods.indicatorutils.utils.JsonMessageUtils;
import stevekung.mods.indicatorutils.utils.MovementInputFromOptionsIU;
import stevekung.mods.indicatorutils.utils.ReflectionUtils;
import stevekung.mods.indicatorutils.utils.helper.GameInfoHelper;
import stevekung.mods.indicatorutils.utils.helper.StatusRendererHelper;

public class IndicatorUtilsEventHandler
{
    public static boolean afkEnabled;
    public static boolean recEnabled;
    public static boolean autoFishEnabled;
    public static String afkMode = "idle";
    public static int afkMoveTick;
    public static int autoFishTick;
    public static List<Long> clicks = new ArrayList();
    public static List<Long> Rclicks = new ArrayList();
    private int pressTime;
    private int pressOneTimeTick;
    private int pressTimeDelay;
    public static int afkTick;
    private int recTick;
    private int clearChatTick;
    public static String afkReason;
    private List<String> sentMessages;
    private List<ChatLine> chatLines;
    private List<ChatLine> drawnChatLines;
    private GuiNewChat chat;
    public static List<String> playerList = Lists.<String>newArrayList();
    public static Map<String, Integer> playerPingMap = Maps.<String, Integer>newHashMap();
    public static boolean setTCPNoDelay = false;

    @SubscribeEvent
    public void onClientTick(ClientTickEvent event)
    {
        this.initReflection();
        Minecraft mc = Minecraft.getMinecraft();

        if (mc.getNetHandler() != null && IndicatorUtilsEventHandler.setTCPNoDelay)
        {
            mc.getNetHandler().getNetworkManager().channel().config().setOption(ChannelOption.TCP_NODELAY, true);
            IULog.info("Set TCP_NODELAY to true");
            IndicatorUtilsEventHandler.setTCPNoDelay = false;
        }

        if (mc.currentScreen != null)
        {
            if (mc.currentScreen instanceof GuiChat && !(mc.currentScreen instanceof GuiNewChatSettings || mc.currentScreen instanceof GuiSleepMP))
            {
                mc.displayGuiScreen(new GuiNewChatSettings());
            }
            if (mc.currentScreen instanceof GuiSleepMP && !(mc.currentScreen instanceof GuiNewSleepMP))
            {
                mc.displayGuiScreen(new GuiNewSleepMP());
            }
            if (mc.currentScreen instanceof GuiNewSleepMP && !mc.thePlayer.isPlayerSleeping())
            {
                mc.displayGuiScreen((GuiScreen)null);
            }
        }

        if (mc.thePlayer != null)
        {
            NetHandlerPlayClient handler = mc.thePlayer.sendQueue;
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
            if (IndicatorUtilsEventHandler.autoFishEnabled)
            {
                ++IndicatorUtilsEventHandler.autoFishTick;
                IndicatorUtilsEventHandler.autoFishTick %= 4;
                boolean flag = true;

                if (mc.thePlayer != null && mc.objectMouseOver != null && mc.theWorld != null && mc.playerController != null && mc.entityRenderer != null)
                {
                    if (IndicatorUtilsEventHandler.autoFishTick % 4 == 0)
                    {
                        ItemStack itemstack = mc.thePlayer.getCurrentEquippedItem();

                        if (itemstack != null && itemstack.getItem() == Items.fishing_rod)
                        {
                            if (mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
                            {
                                int i = mc.objectMouseOver.blockX;
                                int j = mc.objectMouseOver.blockY;
                                int k = mc.objectMouseOver.blockZ;

                                if (!mc.theWorld.getBlock(i, j, k).isAir(mc.theWorld, i, j, k))
                                {
                                    int l = itemstack != null ? itemstack.stackSize : 0;

                                    if (mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, itemstack, i, j, k, mc.objectMouseOver.sideHit, mc.objectMouseOver.hitVec))
                                    {
                                        flag = false;
                                        mc.thePlayer.swingItem();
                                    }
                                    if (itemstack.stackSize == 0)
                                    {
                                        mc.thePlayer.inventory.mainInventory[mc.thePlayer.inventory.currentItem] = null;
                                    }
                                    else if (itemstack.stackSize != l || mc.playerController.isInCreativeMode())
                                    {
                                        mc.entityRenderer.itemRenderer.resetEquippedProgress();
                                    }
                                }
                            }

                            if (flag)
                            {
                                ItemStack itemstack1 = mc.thePlayer.inventory.getCurrentItem();

                                if (itemstack1 != null && mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, itemstack1))
                                {
                                    mc.entityRenderer.itemRenderer.resetEquippedProgress2();
                                }
                            }
                        }
                        else
                        {
                            IndicatorUtilsEventHandler.autoFishEnabled = false;
                            IndicatorUtilsEventHandler.autoFishTick = 0;
                            mc.thePlayer.addChatMessage(JsonMessageUtils.textToJson("Stop using /autofish command, you must hold the fishing rod!"));
                            return;
                        }
                    }
                }
            }
            else
            {
                IndicatorUtilsEventHandler.autoFishTick = 0;
            }

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

            if (IndicatorUtilsEventHandler.afkEnabled)
            {
                ++IndicatorUtilsEventHandler.afkTick;
                int tick = IndicatorUtilsEventHandler.afkTick;
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
                        if (mc.thePlayer != null)
                        {
                            String reason = IndicatorUtilsEventHandler.afkReason;

                            if (reason.isEmpty())
                            {
                                reason = "";
                            }
                            else
                            {
                                reason = ", Reason : " + reason;
                            }
                            mc.thePlayer.sendChatMessage("AFK : " + GameInfoHelper.INSTANCE.ticksToElapsedTime(tick) + " minute" + s + reason);
                        }
                    }
                }

                if (IndicatorUtilsEventHandler.afkMode.equals("idle"))
                {
                    if (mc.thePlayer != null)
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
                        mc.thePlayer.setAngles(angle, angle);
                    }
                }
                else
                {
                    if (mc.thePlayer != null)
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
                        mc.thePlayer.setAngles(angle, angle);
                    }
                    ++IndicatorUtilsEventHandler.afkMoveTick;
                    IndicatorUtilsEventHandler.afkMoveTick %= 8;
                }
            }
            else
            {
                IndicatorUtilsEventHandler.afkTick = 0;
            }

            if (IndicatorUtilsEventHandler.recEnabled)
            {
                ++this.recTick;
            }
            else
            {
                this.recTick = 0;
            }

            if (ExtendedModSettings.AUTO_CLEAR_CHAT)
            {
                ++this.clearChatTick;
                int chatTick = this.clearChatTick / 20;

                if (chatTick % ExtendedModSettings.AUTO_CLEAR_CHAT_TIME == 0)
                {
                    if (ExtendedModSettings.AUTO_CLEAR_CHAT_MODE.equalsIgnoreCase("onlychat"))
                    {
                        if (this.chatLines != null && this.drawnChatLines != null)
                        {
                            this.chatLines.clear();
                            this.drawnChatLines.clear();
                        }
                    }
                    else if (ExtendedModSettings.AUTO_CLEAR_CHAT_MODE.equalsIgnoreCase("onlysentmessage"))
                    {
                        if (this.sentMessages != null)
                        {
                            this.sentMessages.clear();
                        }
                    }
                    else
                    {
                        if (this.sentMessages != null && this.chatLines != null && this.drawnChatLines != null)
                        {
                            this.sentMessages.clear();
                            this.chatLines.clear();
                            this.drawnChatLines.clear();
                        }
                    }
                }
            }
            else
            {
                this.clearChatTick = 0;
            }
        }
        if (mc.thePlayer != null && !(mc.thePlayer.movementInput instanceof MovementInputFromOptionsIU))
        {
            mc.thePlayer.movementInput = new MovementInputFromOptionsIU(mc.gameSettings);
        }
        GuiIngameForge.renderObjective = ConfigManager.renderScoreboard;
        GuiIngameForge.renderBossHealth = ConfigManager.renderBossHealthBar;
    }

    @SubscribeEvent
    public void onPlayerLoggedOut(PlayerLoggedOutEvent event)
    {
        if (IndicatorUtilsEventHandler.afkEnabled == true)
        {
            IndicatorUtilsEventHandler.afkEnabled = false;
            IndicatorUtilsEventHandler.afkReason = "";
            IndicatorUtilsEventHandler.afkTick = 0;
            IndicatorUtilsEventHandler.afkMoveTick = 0;
            IndicatorUtilsEventHandler.afkMode = "idle";
            IULog.info("Stopping AFK Command");
        }
        if (IndicatorUtilsEventHandler.autoFishEnabled)
        {
            IndicatorUtilsEventHandler.autoFishEnabled = false;
            IndicatorUtilsEventHandler.autoFishTick = 0;
            IULog.info("Stopping AutoFish Command");
        }
    }

    @SubscribeEvent
    public void onPlayerRespawn(PlayerRespawnEvent event)
    {
        if (IndicatorUtilsEventHandler.afkEnabled == true)
        {
            IndicatorUtilsEventHandler.afkEnabled = false;
            IndicatorUtilsEventHandler.afkReason = "";
            IndicatorUtilsEventHandler.afkTick = 0;
            IndicatorUtilsEventHandler.afkMoveTick = 0;
            IULog.info("Stopping AFK Command");
        }
        if (IndicatorUtilsEventHandler.autoFishEnabled)
        {
            IndicatorUtilsEventHandler.autoFishEnabled = false;
            IndicatorUtilsEventHandler.autoFishTick = 0;
            IULog.info("Stopping AutoFish Command");
        }
    }

    @SubscribeEvent
    public void onDisconnectedFromServerEvent(ClientDisconnectionFromServerEvent event)
    {
        if (IndicatorUtilsEventHandler.afkEnabled)
        {
            IndicatorUtilsEventHandler.afkEnabled = false;
            IndicatorUtilsEventHandler.afkReason = "";
            IndicatorUtilsEventHandler.afkTick = 0;
            IndicatorUtilsEventHandler.afkMoveTick = 0;
            IndicatorUtilsEventHandler.afkMode = "idle";
            IULog.info("Stopping AFK Command");
        }
        if (IndicatorUtilsEventHandler.autoFishEnabled)
        {
            IndicatorUtilsEventHandler.autoFishEnabled = false;
            IndicatorUtilsEventHandler.autoFishTick = 0;
            IULog.info("Stopping AutoFish Command");
        }
        IndicatorUtilsEventHandler.playerList.clear();
    }

    @SubscribeEvent
    public void onMouseClick(MouseEvent event)
    {
        if (event.button == 0 && event.buttonstate)
        {
            IndicatorUtilsEventHandler.clicks.add(Long.valueOf(System.currentTimeMillis()));
        }
        if (event.button == 1 && event.buttonstate)
        {
            IndicatorUtilsEventHandler.Rclicks.add(Long.valueOf(System.currentTimeMillis()));
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
                            ReflectionUtils.set("zLevel", "field_73735_i", 100.0F, Gui.class, Minecraft.getMinecraft().ingameGUI);
                            mc.ingameGUI.drawTexturedModalRect(xPos + columnWidth - 12, yPos, 0, 176 + pingIndex * 8, 10, 8);
                            ReflectionUtils.set("zLevel", "field_73735_i", -100.0F, Gui.class, Minecraft.getMinecraft().ingameGUI);
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
                            StatusRendererHelper.INSTANCE.drawString(String.valueOf(ping), xPos + columnWidth - 1 - mc.fontRenderer.getStringWidth(String.valueOf(ping)), yPos + 0.5F, color, true);
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

                if (ConfigManager.hideBossHealthBar)
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
    public void onRenderIndicator(RenderGameOverlayEvent.Text event)
    {
        Minecraft mc = Minecraft.getMinecraft();

        if (event.type == ElementType.TEXT)
        {
            if (ConfigManager.enableAllRenderInfo)
            {
                if (ExtendedModSettings.DISPLAY_MODE.equalsIgnoreCase("uhc"))
                {
                    UHCStatusRenderer.init(mc);
                }
                else if (ExtendedModSettings.DISPLAY_MODE.equalsIgnoreCase("pvp"))
                {
                    PvPStatusRenderer.init(mc);
                }
                else if (ExtendedModSettings.DISPLAY_MODE.equalsIgnoreCase("command"))
                {
                    CommandBlockStatusRenderer.init(mc);
                }
                else
                {
                    GlobalStatusRenderer.init(mc);
                }
                if (ConfigManager.enableCPS)
                {
                    String cps = JsonMessageUtils.textToJson("CPS: ", ConfigManager.customColorCPS).getFormattedText();
                    String rps = ConfigManager.enableRPS ? JsonMessageUtils.textToJson(" RPS: ", ConfigManager.customColorRPS).getFormattedText() : "";
                    String cpsValue = JsonMessageUtils.textToJson(String.valueOf(GameInfoHelper.INSTANCE.getCPS()), ConfigManager.customColorCPSValue).getFormattedText();
                    String rpsValue = ConfigManager.enableRPS ? JsonMessageUtils.textToJson(String.valueOf(GameInfoHelper.INSTANCE.getRPS()), ConfigManager.customColorRPSValue).getFormattedText() : "";

                    if (ConfigManager.useCustomTextCPS)
                    {
                        cps = JsonMessageUtils.rawTextToJson(ConfigManager.customTextCPS).getFormattedText();
                    }
                    if (ConfigManager.useCustomTextRPS)
                    {
                        rps = JsonMessageUtils.rawTextToJson(ConfigManager.customTextRPS).getFormattedText();
                    }
                    if (ExtendedModSettings.CPS_POSITION.equalsIgnoreCase("record"))
                    {
                        StatusRendererHelper.INSTANCE.drawStringAtRecord(cps + cpsValue + rps + rpsValue, event.partialTicks);
                    }
                    if (ExtendedModSettings.CPS_POSITION.equalsIgnoreCase("custom"))
                    {
                        StatusRendererHelper.INSTANCE.drawRectNew(ExtendedModSettings.CPS_X_OFFSET, ExtendedModSettings.CPS_Y_OFFSET, ExtendedModSettings.CPS_X_OFFSET + mc.fontRenderer.getStringWidth(cps + cpsValue + rps + rpsValue) + 4, ExtendedModSettings.CPS_Y_OFFSET + 11, 16777216, ExtendedModSettings.CPS_OPACITY);
                        mc.fontRenderer.drawString(cps + cpsValue + rps + rpsValue, ExtendedModSettings.CPS_X_OFFSET + 2, ExtendedModSettings.CPS_Y_OFFSET + 2, 16777215, true);
                    }
                }
                if (IndicatorUtilsEventHandler.recEnabled)
                {
                    ScaledResolution sc = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
                    EnumTextColor color = EnumTextColor.WHITE;

                    if (this.recTick % 24 >= 0 && this.recTick % 24 <= 12)
                    {
                        color = EnumTextColor.RED;
                    }
                    StatusRendererHelper.INSTANCE.drawString("REC: " + GameInfoHelper.INSTANCE.ticksToElapsedTime(this.recTick), sc.getScaledWidth() - mc.fontRenderer.getStringWidth("REC: " + GameInfoHelper.INSTANCE.ticksToElapsedTime(this.recTick)) - 2, sc.getScaledHeight() - 10, color, true);
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
            if (IndicatorUtilsEventHandler.recEnabled == true)
            {
                IndicatorUtilsEventHandler.recEnabled = false;
            }
            else
            {
                IndicatorUtilsEventHandler.recEnabled = true;
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
                    Minecraft.getMinecraft().ingameGUI.func_110326_a(JsonMessageUtils.textToJson("Toggle Sprint Disabled").getFormattedText(), false);
                }
                else
                {
                    ExtendedModSettings.TOGGLE_SPRINT = true;
                    Minecraft.getMinecraft().ingameGUI.func_110326_a(JsonMessageUtils.textToJson("Toggle Sprint Enabled").getFormattedText(), false);
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
                    Minecraft.getMinecraft().ingameGUI.func_110326_a(JsonMessageUtils.textToJson("Toggle Sneak Disabled").getFormattedText(), false);
                }
                else
                {
                    ExtendedModSettings.TOGGLE_SNEAK = true;
                    Minecraft.getMinecraft().ingameGUI.func_110326_a(JsonMessageUtils.textToJson("Toggle Sneak Enabled").getFormattedText(), false);
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
                    Minecraft.getMinecraft().ingameGUI.func_110326_a(JsonMessageUtils.textToJson("Auto Swim Disabled").getFormattedText(), false);
                }
                else
                {
                    ExtendedModSettings.AUTO_SWIM = true;
                    Minecraft.getMinecraft().ingameGUI.func_110326_a(JsonMessageUtils.textToJson("Auto Swim Enabled").getFormattedText(), false);
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
    public void onRenderKeyStroke(RenderTickEvent event)
    {
        Minecraft mc = Minecraft.getMinecraft();

        if (event.phase == Phase.END)
        {
            if (!mc.gameSettings.hideGUI)
            {
                if (ConfigManager.enableKeystroke)
                {
                    if (mc.currentScreen == null || mc.currentScreen instanceof GuiChat)
                    {
                        KeystrokeRenderer.init(mc);
                    }
                }
            }
        }
    }

    private void initReflection()
    {
        this.chat = Minecraft.getMinecraft().ingameGUI.getChatGUI();
        this.sentMessages = ReflectionUtils.get("sentMessages", "field_146248_g", GuiNewChat.class, this.chat);
        this.chatLines = ReflectionUtils.get("chatLines", "field_146252_h", GuiNewChat.class, this.chat);
        this.drawnChatLines = ReflectionUtils.get("field_146253_i", "field_146253_i", GuiNewChat.class, this.chat);
    }
}