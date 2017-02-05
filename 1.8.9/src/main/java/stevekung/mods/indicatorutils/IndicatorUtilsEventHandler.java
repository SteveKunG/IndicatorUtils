/*******************************************************************************
 *
 * Copyright 2016 Wasinthorn Suksri/SteveKunG - Indicator Utils
 *
 ******************************************************************************/

package stevekung.mods.indicatorutils;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSleepMP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientDisconnectionFromServerEvent;
import stevekung.mods.indicatorutils.renderer.KeyStrokeRenderer;
import stevekung.mods.indicatorutils.renderer.statusmode.CommandBlockStatusRenderer;
import stevekung.mods.indicatorutils.renderer.statusmode.GlobalStatusRenderer;
import stevekung.mods.indicatorutils.renderer.statusmode.PvPStatusRenderer;
import stevekung.mods.indicatorutils.renderer.statusmode.UHCStatusRenderer;
import stevekung.mods.indicatorutils.utils.EnumTextColor;
import stevekung.mods.indicatorutils.utils.GuiCapeDownloader;
import stevekung.mods.indicatorutils.utils.IULog;
import stevekung.mods.indicatorutils.utils.JsonMessageUtils;
import stevekung.mods.indicatorutils.utils.MovementInputFromOptionsIU;
import stevekung.mods.indicatorutils.utils.ReflectionUtils;
import stevekung.mods.indicatorutils.utils.gui.GuiNewChatSettings;
import stevekung.mods.indicatorutils.utils.gui.GuiNewSleepMP;
import stevekung.mods.indicatorutils.utils.gui.GuiPlayerTabOverlayIU;
import stevekung.mods.indicatorutils.utils.helper.GameInfoHelper;
import stevekung.mods.indicatorutils.utils.helper.StatusRendererHelper;

public class IndicatorUtilsEventHandler
{
    public static boolean checkUUID = false;
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
    private Minecraft mc;
    private GuiPlayerTabOverlayIU overlayPlayerList;

    @SubscribeEvent
    public void onClientTick(ClientTickEvent event)
    {
        this.initReflection();
        Minecraft mc = Minecraft.getMinecraft();

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
                                BlockPos blockpos = mc.objectMouseOver.getBlockPos();

                                if (!mc.theWorld.isAirBlock(blockpos))
                                {
                                    int i = itemstack != null ? itemstack.stackSize : 0;

                                    if (mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, itemstack, blockpos, mc.objectMouseOver.sideHit, mc.objectMouseOver.hitVec))
                                    {
                                        flag = false;
                                        mc.thePlayer.swingItem();
                                    }
                                    if (itemstack.stackSize == 0)
                                    {
                                        mc.thePlayer.inventory.mainInventory[mc.thePlayer.inventory.currentItem] = null;
                                    }
                                    else if (itemstack.stackSize != i || mc.playerController.isInCreativeMode())
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
    public void onServerChat(ServerChatEvent event)
    {
        if (ExtendedModSettings.CHAT_MODE.equalsIgnoreCase("mineplex_party_chat"))
        {
            IChatComponent itextcomponent = new ChatComponentTranslation("chat.type.text", event.player.getDisplayName(), ForgeHooks.newChatWithLinks("@" + event.message));
            event.setComponent(itextcomponent);
        }
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
        if (event.type == ElementType.PLAYER_LIST)
        {
            if (ConfigManager.playerPingMode.equalsIgnoreCase("number"))
            {
                event.setCanceled(true);
                ScoreObjective scoreobjective = this.mc.theWorld.getScoreboard().getObjectiveInDisplaySlot(0);
                NetHandlerPlayClient handler = this.mc.thePlayer.sendQueue;

                if (this.mc.gameSettings.keyBindPlayerList.isKeyDown() && (!this.mc.isIntegratedServerRunning() || handler.getPlayerInfoMap().size() > 1 || scoreobjective != null))
                {
                    this.overlayPlayerList.updatePlayerList(true);
                    this.overlayPlayerList.renderPlayerlist(event.resolution.getScaledWidth(), this.mc.theWorld.getScoreboard(), scoreobjective);
                }
                else
                {
                    this.overlayPlayerList.updatePlayerList(false);
                }
            }
        }
        if (event.type == ElementType.CHAT)
        {
            event.setCanceled(true);
            GlStateManager.pushMatrix();
            GlStateManager.translate(0, event.resolution.getScaledHeight() - 48, 0.0F);
            GlStateManager.disableDepth();
            this.mc.ingameGUI.getChatGUI().drawChat(this.mc.ingameGUI.getUpdateCounter());
            GlStateManager.enableDepth();
            GlStateManager.popMatrix();
        }
        if (event.type == ElementType.BOSSHEALTH)
        {
            event.setCanceled(true);
            this.mc.getTextureManager().bindTexture(Gui.icons);
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.enableBlend();

            if (BossStatus.bossName != null && BossStatus.statusBarTime > 0)
            {
                --BossStatus.statusBarTime;
                ScaledResolution scaledresolution = new ScaledResolution(this.mc);
                int i = scaledresolution.getScaledWidth();
                int j = 182;
                int k = i / 2 - j / 2;
                int l = (int)(BossStatus.healthScale * (j + 1));
                int i1 = 12;

                if (ConfigManager.hideBossHealthBar)
                {
                    this.mc.ingameGUI.drawTexturedModalRect(k, i1, 0, 74, j, 5);
                    this.mc.ingameGUI.drawTexturedModalRect(k, i1, 0, 74, j, 5);

                    if (l > 0)
                    {
                        this.mc.ingameGUI.drawTexturedModalRect(k, i1, 0, 79, l, 5);
                    }
                }
                String s = BossStatus.bossName;
                this.mc.ingameGUI.getFontRenderer().drawStringWithShadow(s, i / 2 - this.mc.ingameGUI.getFontRenderer().getStringWidth(s) / 2, i1 - 10, 16777215);
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                this.mc.getTextureManager().bindTexture(Gui.icons);
            }
            GlStateManager.disableBlend();
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
                        StatusRendererHelper.INSTANCE.drawRectNew(ExtendedModSettings.CPS_X_OFFSET, ExtendedModSettings.CPS_Y_OFFSET, ExtendedModSettings.CPS_X_OFFSET + this.mc.fontRendererObj.getStringWidth(cps + cpsValue + rps + rpsValue) + 4, ExtendedModSettings.CPS_Y_OFFSET + 11, 16777216, ExtendedModSettings.CPS_OPACITY);
                        this.mc.fontRendererObj.drawString(cps + cpsValue + rps + rpsValue, ExtendedModSettings.CPS_X_OFFSET + 2, ExtendedModSettings.CPS_Y_OFFSET + 2, 16777215, true);
                    }
                }

                if (IndicatorUtilsEventHandler.recEnabled)
                {
                    ScaledResolution sc = new ScaledResolution(mc);
                    EnumTextColor color = EnumTextColor.WHITE;

                    if (this.recTick % 24 >= 0 && this.recTick % 24 <= 12)
                    {
                        color = EnumTextColor.RED;
                    }
                    StatusRendererHelper.INSTANCE.drawString("REC: " + GameInfoHelper.INSTANCE.ticksToElapsedTime(this.recTick), sc.getScaledWidth() - mc.fontRendererObj.getStringWidth("REC: " + GameInfoHelper.INSTANCE.ticksToElapsedTime(this.recTick)) - 2, sc.getScaledHeight() - 10, color, true);
                }
            }
        }
    }

    @SubscribeEvent
    public void onPressKey(KeyInputEvent event)
    {
        if (this.mc.currentScreen == null && this.mc.gameSettings.keyBindCommand.isPressed())
        {
            this.mc.displayGuiScreen(new GuiNewChatSettings("/"));
        }
        if (ConfigManager.enableCustomCapeFeature)
        {
            if (KeyBindingHandler.KEY_OPEN_CAPE_DOWNLOADER_GUI != null && KeyBindingHandler.KEY_OPEN_CAPE_DOWNLOADER_GUI.isKeyDown())
            {
                this.mc.displayGuiScreen(new GuiCapeDownloader());
            }
        }
        if (KeyBindingHandler.KEY_REC_COMMAND.isKeyDown())
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
                    Minecraft.getMinecraft().ingameGUI.setRecordPlaying(JsonMessageUtils.textToJson("Toggle Sprint Disabled").getFormattedText(), false);
                }
                else
                {
                    ExtendedModSettings.TOGGLE_SPRINT = true;
                    Minecraft.getMinecraft().ingameGUI.setRecordPlaying(JsonMessageUtils.textToJson("Toggle Sprint Enabled").getFormattedText(), false);
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
                    Minecraft.getMinecraft().ingameGUI.setRecordPlaying(JsonMessageUtils.textToJson("Toggle Sneak Disabled").getFormattedText(), false);
                }
                else
                {
                    ExtendedModSettings.TOGGLE_SNEAK = true;
                    Minecraft.getMinecraft().ingameGUI.setRecordPlaying(JsonMessageUtils.textToJson("Toggle Sneak Enabled").getFormattedText(), false);
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
                    Minecraft.getMinecraft().ingameGUI.setRecordPlaying(JsonMessageUtils.textToJson("Auto Swim Disabled").getFormattedText(), false);
                }
                else
                {
                    ExtendedModSettings.AUTO_SWIM = true;
                    Minecraft.getMinecraft().ingameGUI.setRecordPlaying(JsonMessageUtils.textToJson("Auto Swim Enabled").getFormattedText(), false);
                }
                ExtendedModSettings.saveExtendedSettings();
            }
        }
        if (ConfigManager.enableEndGameChatMessage)
        {
            if (KeyBindingHandler.KEY_END_GAME_MESSAGE.isKeyDown())
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
                this.pressTime = 0;
                this.pressTimeDelay = 200;
                this.mc.thePlayer.sendChatMessage(ConfigManager.endGameChatMessage);
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
                        KeyStrokeRenderer.init(mc);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onRenderHealthStatus(RenderLivingEvent.Post event)
    {
        Minecraft mc = Minecraft.getMinecraft();
        EntityLivingBase entity = event.entity;
        float health = entity.getHealth() + entity.getAbsorptionAmount();
        boolean halfHealth = health <= entity.getMaxHealth() / 2F;
        boolean halfHealth1 = health <= entity.getMaxHealth() / 4F;
        float range = entity.isSneaking() ? RendererLivingEntity.NAME_TAG_RANGE_SNEAK : RendererLivingEntity.NAME_TAG_RANGE;
        double distance = entity.getDistanceSqToEntity(mc.getRenderViewEntity());
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
                String heart = JsonMessageUtils.textToJson("\u2764 ", color).getFormattedText();
                StatusRendererHelper.renderHealthStatus(entity, heart + String.format("%.1f", health), event.x, event.y, event.z, entity.getDistanceSqToEntity(mc.getRenderViewEntity()));
            }
        }
    }

    private void initReflection()
    {
        this.mc = Minecraft.getMinecraft();
        this.chat = Minecraft.getMinecraft().ingameGUI.getChatGUI();
        this.sentMessages = ReflectionUtils.get("sentMessages", "field_146248_g", GuiNewChat.class, this.chat);
        this.chatLines = ReflectionUtils.get("chatLines", "field_146252_h", GuiNewChat.class, this.chat);
        this.drawnChatLines = ReflectionUtils.get("field_146253_i", "field_146253_i", GuiNewChat.class, this.chat);
        this.overlayPlayerList = new GuiPlayerTabOverlayIU(this.mc, this.mc.ingameGUI);
    }
}