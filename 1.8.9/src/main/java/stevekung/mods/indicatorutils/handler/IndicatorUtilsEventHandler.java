/*******************************************************************************
 *
 * Copyright 2016 Wasinthorn Suksri/SteveKunG - Indicator Utils
 *
 ******************************************************************************/

package stevekung.mods.indicatorutils.handler;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSleepMP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.event.ClickEvent;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientDisconnectionFromServerEvent;
import stevekung.mods.indicatorutils.IndicatorUtils;
import stevekung.mods.indicatorutils.config.ConfigGuiFactory;
import stevekung.mods.indicatorutils.config.ConfigManager;
import stevekung.mods.indicatorutils.config.ExtendedModSettings;
import stevekung.mods.indicatorutils.gui.GuiCapeDownloader;
import stevekung.mods.indicatorutils.gui.GuiNewChatIU;
import stevekung.mods.indicatorutils.gui.GuiNewChatSettings;
import stevekung.mods.indicatorutils.gui.GuiNewSleepMP;
import stevekung.mods.indicatorutils.gui.GuiPlayerTabOverlayIU;
import stevekung.mods.indicatorutils.gui.GuiRenderStatusSettings;
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
import stevekung.mods.indicatorutils.utils.EnumTextColor;
import stevekung.mods.indicatorutils.utils.IULog;
import stevekung.mods.indicatorutils.utils.JsonUtils;
import stevekung.mods.indicatorutils.utils.MovementInputFromOptionsIU;
import stevekung.mods.indicatorutils.utils.ReflectionUtils;
import stevekung.mods.indicatorutils.utils.VersionChecker;

public class IndicatorUtilsEventHandler
{
    public static boolean CHECK_UUID = false;
    public static int CHECK_UUID_TICK;
    public static boolean AFK_ENABLED;
    public static String AFK_MODE = "idle";
    public static String AFK_REASON;
    public static int AFK_MOVE_TICK;
    public static int AFK_TICK;

    public static boolean REC_ENABLED;
    private int recTick;

    public static boolean AUTO_FISH_ENABLED;
    public static int AUTO_FISH_TICK;

    public static List<Long> L_CLICK = new ArrayList();
    public static List<Long> R_CLICK = new ArrayList();

    private int pressTime;
    private int pressOneTimeTick;
    private int pressTimeDelay;

    private int clearChatTick;
    private List<String> sentMessages;
    private List<ChatLine> chatLines;
    private List<ChatLine> drawnChatLines;

    private Minecraft mc;
    private JsonUtils json;

    private GuiPlayerTabOverlayIU overlayPlayerList;

    private long sneakTimeOld = 0L;
    private boolean sneakingOld = false;

    public IndicatorUtilsEventHandler()
    {
        this.mc = Minecraft.getMinecraft();
        this.json = new JsonUtils();
    }

    // Credit to Jarbelar
    // 0 = ShowDesc, 1 = NoConnection, 2 = MissingUUID
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
            if (IndicatorUtils.STATUS_CHECK[2])
            {
                event.player.addChatMessage(json.text("Ping will display as n/a causes by /nick command in Hypixel").setChatStyle(json.red().setBold(true)));
                IndicatorUtils.STATUS_CHECK[2] = false;
            }
        }
    }

    @SubscribeEvent
    public void onClientConnectedToServer(ClientConnectedToServerEvent event)
    {
        ReflectionUtils.set(!IndicatorUtils.isObfuscatedEnvironment() ? "field_73840_e" : "persistantChatGUI", new GuiNewChatIU(Minecraft.getMinecraft()), GuiIngame.class, Minecraft.getMinecraft().ingameGUI);
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
        this.initReflection();
        this.replaceChatGUI();

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

            if (GameInfoHelper.INSTANCE.isHypixel())
            {
                if (IndicatorUtilsEventHandler.CHECK_UUID_TICK < 160)
                {
                    IndicatorUtilsEventHandler.CHECK_UUID_TICK++;
                }
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
        IndicatorUtilsEventHandler.CHECK_UUID_TICK = 0;
        IndicatorUtilsEventHandler.CHECK_UUID = false;
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
    public void onRenderOverlay(RenderGameOverlayEvent event)
    {
        if (event.type == ElementType.TEXT)
        {
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
                        ClientRendererHelper.drawRectNew(ExtendedModSettings.CPS_X_OFFSET, ExtendedModSettings.CPS_Y_OFFSET, ExtendedModSettings.CPS_X_OFFSET + this.mc.fontRendererObj.getStringWidth(cps + cpsValue + rps + rpsValue) + 4, ExtendedModSettings.CPS_Y_OFFSET + 11, 16777216, ExtendedModSettings.CPS_OPACITY);
                        ClientRendererHelper.drawString(cps + cpsValue + rps + rpsValue, ExtendedModSettings.CPS_X_OFFSET + 2, ExtendedModSettings.CPS_Y_OFFSET + 2, 16777215, true);
                    }
                }

                if (IndicatorUtilsEventHandler.REC_ENABLED)
                {
                    ScaledResolution res = new ScaledResolution(this.mc);
                    EnumTextColor color = EnumTextColor.WHITE;

                    if (this.recTick % 24 >= 0 && this.recTick % 24 <= 12)
                    {
                        color = EnumTextColor.RED;
                    }
                    ClientRendererHelper.drawString("REC: " + GameInfoHelper.INSTANCE.ticksToElapsedTime(this.recTick), res.getScaledWidth() - this.mc.fontRendererObj.getStringWidth("REC: " + GameInfoHelper.INSTANCE.ticksToElapsedTime(this.recTick)) - 2, res.getScaledHeight() - 10, color, true);
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
                    Minecraft.getMinecraft().ingameGUI.setRecordPlaying(new JsonUtils().text("Toggle Sprint Disabled").getFormattedText(), false);
                }
                else
                {
                    ExtendedModSettings.TOGGLE_SPRINT = true;
                    Minecraft.getMinecraft().ingameGUI.setRecordPlaying(new JsonUtils().text("Toggle Sprint Enabled").getFormattedText(), false);
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
                    Minecraft.getMinecraft().ingameGUI.setRecordPlaying(new JsonUtils().text("Toggle Sneak Disabled").getFormattedText(), false);
                }
                else
                {
                    ExtendedModSettings.TOGGLE_SNEAK = true;
                    Minecraft.getMinecraft().ingameGUI.setRecordPlaying(new JsonUtils().text("Toggle Sneak Enabled").getFormattedText(), false);
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
                    Minecraft.getMinecraft().ingameGUI.setRecordPlaying(new JsonUtils().text("Auto Swim Disabled").getFormattedText(), false);
                }
                else
                {
                    ExtendedModSettings.AUTO_SWIM = true;
                    Minecraft.getMinecraft().ingameGUI.setRecordPlaying(new JsonUtils().text("Auto Swim Enabled").getFormattedText(), false);
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
    public void onRenderHealthStatus(RenderLivingEvent.Specials.Post event)
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
                String heart = new JsonUtils().text("\u2764 ").setChatStyle(new JsonUtils().colorFromConfig(color)).getFormattedText();
                StatusRendererHelper.renderHealthStatus(entity, heart + String.format("%.1f", health), event.x, event.y, event.z, entity.getDistanceSqToEntity(mc.getRenderViewEntity()));
            }
        }
    }

    @SubscribeEvent
    public void onRenderTick(RenderTickEvent event)
    {
        Minecraft mc = Minecraft.getMinecraft();

        if (event.phase == Phase.START)
        {
            if (ConfigManager.enableOldSneakFeature)
            {
                if (mc.thePlayer != null)
                {
                    mc.thePlayer.eyeHeight = this.getOldEyeHeight(mc.thePlayer);
                }
            }
        }
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
                            mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
                        }
                    }
                }
                GlStateManager.translate(0.0F, defaultval, 0.0F);
                ClientRendererHelper.drawGradientRect(mc.currentScreen.width - 100, mc.currentScreen.height - 35, mc.currentScreen.width, mc.currentScreen.height, ClientRendererHelper.to32BitColor(150, 10 + deltaColor, 10 + deltaColor, 10 + deltaColor), ClientRendererHelper.to32BitColor(250, 10 + deltaColor, 10 + deltaColor, 10 + deltaColor));
                mc.fontRendererObj.drawString(I18n.format("gui.indicatorutils.config.name0"), mc.currentScreen.width - 50 - mc.fontRendererObj.getStringWidth(I18n.format("gui.indicatorutils.config.name0")) / 2, mc.currentScreen.height - 26, ClientRendererHelper.to32BitColor(255, 240, 240, 240));
                mc.fontRendererObj.drawString(I18n.format("gui.indicatorutils.config.name1"), mc.currentScreen.width - 50 - mc.fontRendererObj.getStringWidth(I18n.format("gui.indicatorutils.config.name1")) / 2, mc.currentScreen.height - 16, ClientRendererHelper.to32BitColor(255, 240, 240, 240));
                Gui.drawRect(mc.currentScreen.width - 100, mc.currentScreen.height - 35, mc.currentScreen.width - 99, mc.currentScreen.height, ClientRendererHelper.to32BitColor(255, 0, 0, 0));
                Gui.drawRect(mc.currentScreen.width - 100, mc.currentScreen.height - 35, mc.currentScreen.width, mc.currentScreen.height - 34, ClientRendererHelper.to32BitColor(255, 0, 0, 0));
            }
            if (!mc.gameSettings.hideGUI)
            {
                if (ConfigManager.enableKeystroke)
                {
                    if (mc.currentScreen == null || mc.currentScreen instanceof GuiChat || this.mc.currentScreen instanceof GuiRenderStatusSettings)
                    {
                        KeystrokeRenderer.init(mc);
                    }
                }
            }
        }
    }

    private void initReflection()
    {
        this.sentMessages = ReflectionUtils.get("sentMessages", "field_146248_g", GuiNewChat.class, this.mc.ingameGUI.getChatGUI());
        this.chatLines = ReflectionUtils.get("chatLines", "field_146252_h", GuiNewChat.class, this.mc.ingameGUI.getChatGUI());
        this.drawnChatLines = ReflectionUtils.get("drawnChatLines", "field_146253_i", GuiNewChat.class, this.mc.ingameGUI.getChatGUI());
        this.overlayPlayerList = new GuiPlayerTabOverlayIU(this.mc, this.mc.ingameGUI);
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
                            BlockPos blockpos = this.mc.objectMouseOver.getBlockPos();

                            if (!this.mc.theWorld.isAirBlock(blockpos))
                            {
                                int i = itemstack != null ? itemstack.stackSize : 0;

                                if (this.mc.playerController.onPlayerRightClick(this.mc.thePlayer, this.mc.theWorld, itemstack, blockpos, this.mc.objectMouseOver.sideHit, this.mc.objectMouseOver.hitVec))
                                {
                                    flag = false;
                                    this.mc.thePlayer.swingItem();
                                }
                                if (itemstack.stackSize == 0)
                                {
                                    this.mc.thePlayer.inventory.mainInventory[this.mc.thePlayer.inventory.currentItem] = null;
                                }
                                else if (itemstack.stackSize != i || this.mc.playerController.isInCreativeMode())
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

    private float getOldEyeHeight(EntityPlayer player)
    {
        if (this.sneakingOld != player.isSneaking() || this.sneakTimeOld <= 0L)
        {
            this.sneakTimeOld = System.currentTimeMillis();
        }

        this.sneakingOld = player.isSneaking();
        float defaultEyeHeight = 1.62F;
        double sneakPress = 0.0004D;
        double sneakValue = 0.015D;
        int sneakTime = -50;
        long systemTime = 58L;

        if (player.isSneaking())
        {
            int sneakSystemTime = (int)(this.sneakTimeOld + systemTime - System.currentTimeMillis());

            if (sneakSystemTime > sneakTime)
            {
                defaultEyeHeight += (float)(sneakSystemTime * sneakPress);

                if (defaultEyeHeight < 0.0F || defaultEyeHeight > 10.0F)
                {
                    defaultEyeHeight = 1.54F;
                }
            }
            else
            {
                defaultEyeHeight = (float)(defaultEyeHeight - sneakValue);
            }
        }
        else
        {
            int sneakSystemTime = (int)(this.sneakTimeOld + systemTime - System.currentTimeMillis());

            if (sneakSystemTime > sneakTime)
            {
                defaultEyeHeight -= (float)(sneakSystemTime * sneakPress);
                defaultEyeHeight = (float)(defaultEyeHeight - sneakValue);

                if (defaultEyeHeight < 0.0F)
                {
                    defaultEyeHeight = 1.62F;
                }
            }
            else
            {
                defaultEyeHeight -= 0.0F;
            }
        }
        return defaultEyeHeight;
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
}