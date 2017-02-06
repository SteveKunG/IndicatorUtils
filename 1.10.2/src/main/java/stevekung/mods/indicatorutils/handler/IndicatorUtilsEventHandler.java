/*******************************************************************************
 *
 * Copyright 2016 Wasinthorn Suksri/SteveKunG - Indicator Utils
 *
 ******************************************************************************/

package stevekung.mods.indicatorutils.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiBossOverlay;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSleepMP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.BossInfoLerping;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientDisconnectionFromServerEvent;
import stevekung.mods.indicatorutils.config.ConfigManager;
import stevekung.mods.indicatorutils.config.ExtendedModSettings;
import stevekung.mods.indicatorutils.gui.GuiBossOverlayIU;
import stevekung.mods.indicatorutils.gui.GuiCapeDownloader;
import stevekung.mods.indicatorutils.gui.GuiNewChatSettings;
import stevekung.mods.indicatorutils.gui.GuiNewSleepMP;
import stevekung.mods.indicatorutils.gui.GuiPlayerTabOverlayIU;
import stevekung.mods.indicatorutils.helper.GameInfoHelper;
import stevekung.mods.indicatorutils.helper.StatusRendererHelper;
import stevekung.mods.indicatorutils.keybinding.KeyBindingHandler;
import stevekung.mods.indicatorutils.renderer.KeystrokeRenderer;
import stevekung.mods.indicatorutils.renderer.mode.CommandBlock;
import stevekung.mods.indicatorutils.renderer.mode.Global;
import stevekung.mods.indicatorutils.renderer.mode.PvP;
import stevekung.mods.indicatorutils.renderer.mode.UHC;
import stevekung.mods.indicatorutils.utils.EnumTextColor;
import stevekung.mods.indicatorutils.utils.IULog;
import stevekung.mods.indicatorutils.utils.JsonUtils;
import stevekung.mods.indicatorutils.utils.MovementInputFromOptionsIU;
import stevekung.mods.indicatorutils.utils.ReflectionUtils;

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
    private GuiBossOverlayIU overlayBoss;
    public static Map<UUID, BossInfoLerping> mapBossInfos;

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

        StatusRendererHelper.initEntityDetectorWithGlowing();

        if (event.phase == Phase.START)
        {
            if (IndicatorUtilsEventHandler.autoFishEnabled)
            {
                ++IndicatorUtilsEventHandler.autoFishTick;
                IndicatorUtilsEventHandler.autoFishTick %= 4;

                if (mc.thePlayer != null && mc.objectMouseOver != null && mc.theWorld != null && mc.playerController != null && mc.entityRenderer != null)
                {
                    if (IndicatorUtilsEventHandler.autoFishTick % 4 == 0)
                    {
                        for (EnumHand enumhand : EnumHand.values())
                        {
                            ItemStack itemstack = mc.thePlayer.getHeldItem(enumhand);

                            if (itemstack != null && itemstack.getItem() == Items.FISHING_ROD)
                            {
                                if (mc.objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK)
                                {
                                    BlockPos blockpos = mc.objectMouseOver.getBlockPos();

                                    if (mc.theWorld.getBlockState(blockpos).getMaterial() != Material.AIR)
                                    {
                                        int i = itemstack != null ? itemstack.stackSize : 0;
                                        EnumActionResult enumactionresult = mc.playerController.processRightClickBlock(mc.thePlayer, mc.theWorld, itemstack, blockpos, mc.objectMouseOver.sideHit, mc.objectMouseOver.hitVec, enumhand);

                                        if (enumactionresult == EnumActionResult.SUCCESS)
                                        {
                                            mc.thePlayer.swingArm(enumhand);

                                            if (itemstack != null)
                                            {
                                                if (itemstack.stackSize == 0)
                                                {
                                                    mc.thePlayer.setHeldItem(enumhand, (ItemStack)null);
                                                }
                                                else if (itemstack.stackSize != i || mc.playerController.isInCreativeMode())
                                                {
                                                    mc.entityRenderer.itemRenderer.resetEquippedProgress(enumhand);
                                                }
                                            }
                                            return;
                                        }
                                    }
                                }
                                ItemStack itemstack1 = mc.thePlayer.getHeldItem(enumhand);

                                if (itemstack1 != null && mc.playerController.processRightClick(mc.thePlayer, mc.theWorld, itemstack1, enumhand) == EnumActionResult.SUCCESS)
                                {
                                    mc.entityRenderer.itemRenderer.resetEquippedProgress(enumhand);
                                    return;
                                }
                            }
                            else
                            {
                                IndicatorUtilsEventHandler.autoFishEnabled = false;
                                IndicatorUtilsEventHandler.autoFishTick = 0;
                                mc.thePlayer.addChatMessage(JsonUtils.textToJson("Stop using /autofish command, you must hold the fishing rod!"));
                                return;
                            }
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
        GuiIngameForge.renderBossHealth = ConfigManager.renderBossHealthBar;
        GuiIngameForge.renderObjective = ConfigManager.renderScoreboard;
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
        if (event.getButton() == 0 && event.isButtonstate())
        {
            IndicatorUtilsEventHandler.clicks.add(Long.valueOf(System.currentTimeMillis()));
        }
        if (event.getButton() == 1 && event.isButtonstate())
        {
            IndicatorUtilsEventHandler.Rclicks.add(Long.valueOf(System.currentTimeMillis()));
        }
    }

    @SubscribeEvent
    public void onPreRender(RenderGameOverlayEvent.Pre event)
    {
        if (event.getType() == ElementType.PLAYER_LIST)
        {
            if (ConfigManager.playerPingMode.equalsIgnoreCase("number"))
            {
                event.setCanceled(true);
                ScoreObjective scoreobjective = this.mc.theWorld.getScoreboard().getObjectiveInDisplaySlot(0);
                NetHandlerPlayClient handler = this.mc.thePlayer.connection;

                if (this.mc.gameSettings.keyBindPlayerList.isKeyDown() && (!this.mc.isIntegratedServerRunning() || handler.getPlayerInfoMap().size() > 1 || scoreobjective != null))
                {
                    this.overlayPlayerList.updatePlayerList(true);
                    this.overlayPlayerList.renderPlayerlist(event.getResolution().getScaledWidth(), this.mc.theWorld.getScoreboard(), scoreobjective);
                }
                else
                {
                    this.overlayPlayerList.updatePlayerList(false);
                }
            }
        }
        if (event.getType() == ElementType.CHAT)
        {
            event.setCanceled(true);
            GlStateManager.pushMatrix();
            GlStateManager.translate(0, event.getResolution().getScaledHeight() - 48, 0.0F);
            GlStateManager.disableDepth();
            this.mc.ingameGUI.getChatGUI().drawChat(this.mc.ingameGUI.getUpdateCounter());
            GlStateManager.enableDepth();
            GlStateManager.popMatrix();
        }
        if (event.getType() == ElementType.POTION_ICONS)
        {
            if (!ConfigManager.renderIngamePotionEffect)
            {
                event.setCanceled(true);
            }
        }
        if (event.getType() == ElementType.BOSSHEALTH)
        {
            event.setCanceled(true);
            this.mc.getTextureManager().bindTexture(Gui.ICONS);
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.enableBlend();
            this.overlayBoss.renderBossHealth();
            GlStateManager.disableBlend();
        }
    }

    @SubscribeEvent
    public void onRenderIndicator(RenderGameOverlayEvent.Text event)
    {
        if (event.getType() == ElementType.TEXT)
        {
            if (ConfigManager.enableAllRenderInfo)
            {
                if (ExtendedModSettings.DISPLAY_MODE.equalsIgnoreCase("uhc"))
                {
                    UHC.init(this.mc);
                }
                else if (ExtendedModSettings.DISPLAY_MODE.equalsIgnoreCase("pvp"))
                {
                    PvP.init(this.mc);
                }
                else if (ExtendedModSettings.DISPLAY_MODE.equalsIgnoreCase("command"))
                {
                    CommandBlock.init(this.mc);
                }
                else
                {
                    Global.init(this.mc);
                }
                if (ConfigManager.enableCPS)
                {
                    String cps = JsonUtils.textToJson("CPS: ", ConfigManager.customColorCPS).getFormattedText();
                    String rps = ConfigManager.enableRPS ? JsonUtils.textToJson(" RPS: ", ConfigManager.customColorRPS).getFormattedText() : "";
                    String cpsValue = JsonUtils.textToJson(String.valueOf(GameInfoHelper.INSTANCE.getCPS()), ConfigManager.customColorCPSValue).getFormattedText();
                    String rpsValue = ConfigManager.enableRPS ? JsonUtils.textToJson(String.valueOf(GameInfoHelper.INSTANCE.getRPS()), ConfigManager.customColorRPSValue).getFormattedText() : "";

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
                        StatusRendererHelper.INSTANCE.drawStringAtRecord(cps + cpsValue + rps + rpsValue, event.getPartialTicks());
                    }
                    if (ExtendedModSettings.CPS_POSITION.equalsIgnoreCase("custom"))
                    {
                        StatusRendererHelper.INSTANCE.drawRectNew(ExtendedModSettings.CPS_X_OFFSET, ExtendedModSettings.CPS_Y_OFFSET, ExtendedModSettings.CPS_X_OFFSET + this.mc.fontRendererObj.getStringWidth(cps + cpsValue + rps + rpsValue) + 4, ExtendedModSettings.CPS_Y_OFFSET + 11, 16777216, ExtendedModSettings.CPS_OPACITY);
                        this.mc.fontRendererObj.drawString(cps + cpsValue + rps + rpsValue, ExtendedModSettings.CPS_X_OFFSET + 2, ExtendedModSettings.CPS_Y_OFFSET + 2, 16777215, true);
                    }
                }

                if (IndicatorUtilsEventHandler.recEnabled)
                {
                    ScaledResolution sc = new ScaledResolution(this.mc);
                    EnumTextColor color = EnumTextColor.WHITE;

                    if (this.recTick % 24 >= 0 && this.recTick % 24 <= 12)
                    {
                        color = EnumTextColor.RED;
                    }
                    StatusRendererHelper.INSTANCE.drawString("REC: " + GameInfoHelper.INSTANCE.ticksToElapsedTime(this.recTick), sc.getScaledWidth() - this.mc.fontRendererObj.getStringWidth("REC: " + GameInfoHelper.INSTANCE.ticksToElapsedTime(this.recTick)) - 2, sc.getScaledHeight() - 10, color, true);
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
            if (KeyBindingHandler.KEY_DISPLAY_MODE_NEXT.isKeyDown())
            {
                StatusRendererHelper.INSTANCE.enumRenderMode = (StatusRendererHelper.INSTANCE.enumRenderMode + 1) % 4;
                StatusRendererHelper.INSTANCE.setDisplayMode(StatusRendererHelper.INSTANCE.enumRenderMode);
            }
            if (KeyBindingHandler.KEY_DISPLAY_MODE_PREVIOUS.isKeyDown())
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
            if (KeyBindingHandler.KEY_TOGGLE_SPRINT.isKeyDown())
            {
                if (ExtendedModSettings.TOGGLE_SPRINT)
                {
                    ExtendedModSettings.TOGGLE_SPRINT = false;
                    Minecraft.getMinecraft().ingameGUI.setRecordPlaying(JsonUtils.textToJson("Toggle Sprint Disabled").getFormattedText(), false);
                }
                else
                {
                    ExtendedModSettings.TOGGLE_SPRINT = true;
                    Minecraft.getMinecraft().ingameGUI.setRecordPlaying(JsonUtils.textToJson("Toggle Sprint Enabled").getFormattedText(), false);
                }
                ExtendedModSettings.saveExtendedSettings();
            }
        }
        if (ExtendedModSettings.TOGGLE_SNEAK_USE_MODE.equalsIgnoreCase("keybinding"))
        {
            if (KeyBindingHandler.KEY_TOGGLE_SNEAK.isKeyDown())
            {
                if (ExtendedModSettings.TOGGLE_SNEAK)
                {
                    ExtendedModSettings.TOGGLE_SNEAK = false;
                    Minecraft.getMinecraft().ingameGUI.setRecordPlaying(JsonUtils.textToJson("Toggle Sneak Disabled").getFormattedText(), false);
                }
                else
                {
                    ExtendedModSettings.TOGGLE_SNEAK = true;
                    Minecraft.getMinecraft().ingameGUI.setRecordPlaying(JsonUtils.textToJson("Toggle Sneak Enabled").getFormattedText(), false);
                }
                ExtendedModSettings.saveExtendedSettings();
            }
        }
        if (ExtendedModSettings.AUTO_SWIM_USE_MODE.equalsIgnoreCase("keybinding"))
        {
            if (KeyBindingHandler.KEY_AUTO_SWIM.isKeyDown())
            {
                if (ExtendedModSettings.AUTO_SWIM)
                {
                    ExtendedModSettings.AUTO_SWIM = false;
                    Minecraft.getMinecraft().ingameGUI.setRecordPlaying(JsonUtils.textToJson("Auto Swim Disabled").getFormattedText(), false);
                }
                else
                {
                    ExtendedModSettings.AUTO_SWIM = true;
                    Minecraft.getMinecraft().ingameGUI.setRecordPlaying(JsonUtils.textToJson("Auto Swim Enabled").getFormattedText(), false);
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
                        KeystrokeRenderer.init(mc);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onRenderHealthStatus(RenderLivingEvent.Post event)
    {
        Minecraft mc = Minecraft.getMinecraft();
        EntityLivingBase entity = event.getEntity();
        float health = entity.getHealth() + entity.getAbsorptionAmount();
        boolean halfHealth = health <= entity.getMaxHealth() / 2F;
        boolean halfHealth1 = health <= entity.getMaxHealth() / 4F;
        float range = entity.isSneaking() ? RenderLivingBase.NAME_TAG_RANGE_SNEAK : RenderLivingBase.NAME_TAG_RANGE;
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
                String heart = JsonUtils.textToJson("\u2764 ", color).getFormattedText();
                StatusRendererHelper.renderHealthStatus(entity, heart + String.format("%.1f", health), event.getX(), event.getY(), event.getZ(), entity.getDistanceSqToEntity(mc.getRenderViewEntity()));
            }
        }
    }

    private void initReflection()
    {
        this.mc = Minecraft.getMinecraft();
        this.chat = Minecraft.getMinecraft().ingameGUI.getChatGUI();
        this.sentMessages = ReflectionUtils.get("sentMessages", "field_146248_g", GuiNewChat.class, this.chat);
        this.chatLines = ReflectionUtils.get("chatLines", "field_146252_h", GuiNewChat.class, this.chat);
        this.drawnChatLines = ReflectionUtils.get("drawnChatLines", "field_146253_i", GuiNewChat.class, this.chat);
        IndicatorUtilsEventHandler.mapBossInfos = ReflectionUtils.get("mapBossInfos", "field_184060_g", GuiBossOverlay.class, this.mc.ingameGUI.getBossOverlay());
        this.overlayBoss = new GuiBossOverlayIU(this.mc);
        this.overlayPlayerList = new GuiPlayerTabOverlayIU(this.mc, this.mc.ingameGUI);
    }
}