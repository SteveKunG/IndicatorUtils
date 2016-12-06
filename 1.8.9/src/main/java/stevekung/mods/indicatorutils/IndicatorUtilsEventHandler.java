/*******************************************************************************
 *
 * Copyright 2016 Wasinthorn Suksri/SteveKunG - Indicator Utils
 *
 ******************************************************************************/

package stevekung.mods.indicatorutils;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
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
import stevekung.mods.indicatorutils.renderer.KeyStrokeRenderer;
import stevekung.mods.indicatorutils.renderer.statusmode.CommandBlockStatusRenderer;
import stevekung.mods.indicatorutils.renderer.statusmode.GlobalStatusRenderer;
import stevekung.mods.indicatorutils.renderer.statusmode.PvPStatusRenderer;
import stevekung.mods.indicatorutils.renderer.statusmode.UHCStatusRenderer;
import stevekung.mods.indicatorutils.utils.EnumTextColor;
import stevekung.mods.indicatorutils.utils.GuiCapeDownloader;
import stevekung.mods.indicatorutils.utils.GuiIngameForgeIU;
import stevekung.mods.indicatorutils.utils.IULog;
import stevekung.mods.indicatorutils.utils.JsonMessageUtils;
import stevekung.mods.indicatorutils.utils.MovementInputFromOptionsIU;
import stevekung.mods.indicatorutils.utils.ReflectionUtils;
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
    private static boolean setNewGUI = false;

    @SubscribeEvent
    public void onClientTick(ClientTickEvent event)
    {
        this.initReflection();
        Minecraft mc = Minecraft.getMinecraft();

        if (ConfigManager.replaceIngameGUI)
        {
            if (!IndicatorUtilsEventHandler.setNewGUI && mc.ingameGUI != null && !(mc.ingameGUI instanceof GuiIngameForgeIU))
            {
                ReflectionUtils.set(IndicatorUtils.isObfuscatedEnvironment() ? "ingameGUI" : "field_71456_v", new GuiIngameForgeIU(mc), Minecraft.class, this.mc);
                IULog.info(mc.ingameGUI.toString());
                IndicatorUtilsEventHandler.setNewGUI = true;
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
                                ;
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

        GuiIngameForgeIU.renderObjective = ConfigManager.renderScoreboard;
        GuiIngameForgeIU.renderBossHealth = ConfigManager.renderBossHealthBar;
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
        if (event.button != 0)
        {
            return;
        }
        if (event.buttonstate)
        {
            IndicatorUtilsEventHandler.clicks.add(Long.valueOf(System.currentTimeMillis()));
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
                    if (ExtendedModSettings.CPS_POSITION.equalsIgnoreCase("record"))
                    {
                        String cps = JsonMessageUtils.textToJson("CPS: ", ConfigManager.customColorCPS).getFormattedText();
                        String cpsValue = JsonMessageUtils.textToJson(String.valueOf(GameInfoHelper.INSTANCE.getCPS()), ConfigManager.customColorCPSValue).getFormattedText();

                        if (ConfigManager.useCustomTextCPS)
                        {
                            cps = JsonMessageUtils.rawTextToJson(ConfigManager.customTextCPS).getFormattedText();
                        }
                        StatusRendererHelper.INSTANCE.drawStringAtRecord(cps + cpsValue, event.partialTicks);
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
            if (KeyBindingHandler.KEY_TOGGLE_SNEAK.isKeyDown())
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
            if (KeyBindingHandler.KEY_AUTO_SWIM.isKeyDown())
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
    }
}