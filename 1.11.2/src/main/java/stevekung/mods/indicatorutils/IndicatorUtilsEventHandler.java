/*******************************************************************************
 *
 * Copyright 2016 Wasinthorn Suksri/SteveKunG - Indicator Utils
 *
 ******************************************************************************/

package stevekung.mods.indicatorutils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiBossOverlay;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.BossInfoLerping;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientDisconnectionFromServerEvent;
import stevekung.mods.indicatorutils.keybinding.KeyBindingHandler;
import stevekung.mods.indicatorutils.renderer.KeystrokeRenderer;
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
import stevekung.mods.indicatorutils.utils.gui.GuiBossOverlayIU;
import stevekung.mods.indicatorutils.utils.gui.GuiNewChatSettings;
import stevekung.mods.indicatorutils.utils.gui.GuiPlayerTabOverlayIU;
import stevekung.mods.indicatorutils.utils.helper.ClientRendererHelper;
import stevekung.mods.indicatorutils.utils.helper.GameInfoHelper;
import stevekung.mods.indicatorutils.utils.helper.ObjectModeHelper;
import stevekung.mods.indicatorutils.utils.helper.ObjectModeHelper.EnumDisplayMode;
import stevekung.mods.indicatorutils.utils.helper.StatusRendererHelper;

public class IndicatorUtilsEventHandler
{
    public static boolean CHECK_UUID = false;
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
    private JsonMessageUtils json;

    private GuiPlayerTabOverlayIU overlayPlayerList;
    private GuiBossOverlayIU overlayBoss;
    public static Map<UUID, BossInfoLerping> MAP_BOSS_INFOS;

    public IndicatorUtilsEventHandler()
    {
        this.mc = Minecraft.getMinecraft();
        this.json = new JsonMessageUtils();
    }

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent event)
    {
        if (event.getModID().equalsIgnoreCase(IndicatorUtils.MOD_ID))
        {
            ConfigManager.syncConfig(false);
        }
    }

    @SubscribeEvent
    public void onServerChat(ServerChatEvent event)
    {
        if (ExtendedModSettings.CHAT_MODE.equalsIgnoreCase("mineplex_party_chat"))
        {
            ITextComponent itextcomponent = new TextComponentTranslation("chat.type.text", event.getPlayer().getDisplayName(), ForgeHooks.newChatWithLinks("@" + event.getMessage()));
            event.setComponent(itextcomponent);
        }
    }

    @SubscribeEvent
    public void onClientTick(ClientTickEvent event)
    {
        this.initReflection();
        this.playerDetectorGlowingMode();

        StatusRendererHelper.initEntityDetectorWithGlowing();

        if (this.mc.currentScreen != null)
        {
            if (this.mc.currentScreen instanceof GuiChat && !(this.mc.currentScreen instanceof GuiNewChatSettings))
            {
                this.mc.displayGuiScreen(new GuiNewChatSettings());
            }
        }

        if (event.phase == Phase.START)
        {
            this.afkForPlayers();
            this.autoFish();
            this.autoClearChat();

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
        if (this.mc.player != null && !(this.mc.player.movementInput instanceof MovementInputFromOptionsIU))
        {
            this.mc.player.movementInput = new MovementInputFromOptionsIU(this.mc.gameSettings);
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
    }

    @SubscribeEvent
    public void onMouseClick(MouseEvent event)
    {
        if (event.getButton() == 0 && event.isButtonstate())
        {
            IndicatorUtilsEventHandler.L_CLICK.add(Long.valueOf(System.currentTimeMillis()));
        }
        if (event.getButton() == 1 && event.isButtonstate())
        {
            IndicatorUtilsEventHandler.R_CLICK.add(Long.valueOf(System.currentTimeMillis()));
        }
    }

    @SubscribeEvent
    public void onPreRender(RenderGameOverlayEvent.Pre event)
    {
        if (event.getType() == ElementType.PLAYER_LIST)
        {
            event.setCanceled(true);
            ScoreObjective scoreobjective = this.mc.world.getScoreboard().getObjectiveInDisplaySlot(0);
            NetHandlerPlayClient handler = this.mc.player.connection;

            if (this.mc.gameSettings.keyBindPlayerList.isKeyDown() && (!this.mc.isIntegratedServerRunning() || handler.getPlayerInfoMap().size() > 1 || scoreobjective != null))
            {
                this.overlayPlayerList.updatePlayerList(true);
                this.overlayPlayerList.renderPlayerlist(event.getResolution().getScaledWidth(), this.mc.world.getScoreboard(), scoreobjective);
            }
            else
            {
                this.overlayPlayerList.updatePlayerList(false);
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
    public void onRenderIndicator(RenderGameOverlayEvent event)
    {
        if (event.getType() == ElementType.TEXT)
        {
            if (ConfigManager.enableAllRenderInfo)
            {
                if (ObjectModeHelper.getDisplayMode(EnumDisplayMode.UHC))
                {
                    UHCStatusRenderer.init(this.mc);
                }
                else if (ObjectModeHelper.getDisplayMode(EnumDisplayMode.PVP))
                {
                    PvPStatusRenderer.init(this.mc);
                }
                else if (ObjectModeHelper.getDisplayMode(EnumDisplayMode.COMMAND_BLOCK))
                {
                    CommandBlockStatusRenderer.init(this.mc);
                }
                else
                {
                    GlobalStatusRenderer.init(this.mc);
                }

                if (ConfigManager.enableCPS)
                {
                    String cps = this.json.text("CPS: ").setStyle(this.json.colorFromConfig(ConfigManager.customColorCPS)).getFormattedText();
                    String rps = ConfigManager.enableRPS ? this.json.text(" RPS: ").setStyle(this.json.colorFromConfig(ConfigManager.customColorRPS)).getFormattedText() : "";
                    String cpsValue = this.json.text(String.valueOf(GameInfoHelper.INSTANCE.getCPS())).setStyle(this.json.colorFromConfig(ConfigManager.customColorCPSValue)).getFormattedText();
                    String rpsValue = ConfigManager.enableRPS ? this.json.text(String.valueOf(GameInfoHelper.INSTANCE.getRPS())).setStyle(this.json.colorFromConfig(ConfigManager.customColorRPSValue)).getFormattedText() : "";

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
                        StatusRendererHelper.INSTANCE.drawStringAtRecord(cps + cpsValue + rps + rpsValue, event.getPartialTicks());
                    }
                    if (ExtendedModSettings.CPS_POSITION.equalsIgnoreCase("custom"))
                    {
                        StatusRendererHelper.INSTANCE.drawRectNew(ExtendedModSettings.CPS_X_OFFSET, ExtendedModSettings.CPS_Y_OFFSET, ExtendedModSettings.CPS_X_OFFSET + this.mc.fontRendererObj.getStringWidth(cps + cpsValue + rps + rpsValue) + 4, ExtendedModSettings.CPS_Y_OFFSET + 11, 16777216, ExtendedModSettings.CPS_OPACITY);
                        this.mc.fontRendererObj.drawString(cps + cpsValue + rps + rpsValue, ExtendedModSettings.CPS_X_OFFSET + 2, ExtendedModSettings.CPS_Y_OFFSET + 2, 16777215, true);
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
                    StatusRendererHelper.INSTANCE.drawString("REC: " + GameInfoHelper.INSTANCE.ticksToElapsedTime(this.recTick), res.getScaledWidth() - this.mc.fontRendererObj.getStringWidth("REC: " + GameInfoHelper.INSTANCE.ticksToElapsedTime(this.recTick)) - 2, res.getScaledHeight() - 10, color, true);
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
                    GameInfoHelper.INSTANCE.setActionBarMessage(this.json.text("Toggle Sprint Disabled").getFormattedText(), false);
                }
                else
                {
                    ExtendedModSettings.TOGGLE_SPRINT = true;
                    GameInfoHelper.INSTANCE.setActionBarMessage(this.json.text("Toggle Sprint Enabled").getFormattedText(), false);
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
                    GameInfoHelper.INSTANCE.setActionBarMessage(this.json.text("Toggle Sneak Disabled").getFormattedText(), false);
                }
                else
                {
                    ExtendedModSettings.TOGGLE_SNEAK = true;
                    GameInfoHelper.INSTANCE.setActionBarMessage(this.json.text("Toggle Sneak Enabled").getFormattedText(), false);
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
                    GameInfoHelper.INSTANCE.setActionBarMessage(this.json.text("Auto Swim Disabled").getFormattedText(), false);
                }
                else
                {
                    ExtendedModSettings.AUTO_SWIM = true;
                    GameInfoHelper.INSTANCE.setActionBarMessage(this.json.text("Auto Swim Enabled").getFormattedText(), false);
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
                this.mc.player.sendChatMessage(ConfigManager.endGameChatMessage);
            }
        }
    }

    @SubscribeEvent
    public void onRenderHealthStatus(RenderLivingEvent.Post event)
    {
        EntityLivingBase entity = event.getEntity();
        float health = entity.getHealth() + entity.getAbsorptionAmount();
        boolean halfHealth = health <= entity.getMaxHealth() / 2F;
        boolean halfHealth1 = health <= entity.getMaxHealth() / 4F;
        float range = entity.isSneaking() ? RenderLivingBase.NAME_TAG_RANGE_SNEAK : RenderLivingBase.NAME_TAG_RANGE;
        double distance = entity.getDistanceSqToEntity(this.mc.getRenderViewEntity());
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

        if (status.equalsIgnoreCase("DISABLE"))
        {
            flag = false;
        }
        else if (status.equalsIgnoreCase("POINTED"))
        {
            flag = entity == this.mc.pointedEntity;
        }

        if (distance < range * range)
        {
            if (!this.mc.gameSettings.hideGUI && !entity.isInvisible() && flag)
            {
                String heart = this.json.text("\u2764 ").setStyle(this.json.colorFromConfig(color)).getFormattedText();
                StatusRendererHelper.renderHealthStatus(entity, heart + String.format("%.1f", health), event.getX(), event.getY(), event.getZ(), entity.getDistanceSqToEntity(this.mc.getRenderViewEntity()));
            }
        }
    }

    @SubscribeEvent
    public void onRenderTick(RenderTickEvent event)
    {
        if (event.phase == Phase.END)
        {
            if (!this.mc.gameSettings.hideGUI)
            {
                if (ConfigManager.enableKeystroke)
                {
                    if (this.mc.currentScreen == null || this.mc.currentScreen instanceof GuiChat)
                    {
                        KeystrokeRenderer.init(this.mc);
                    }
                }
            }
            if (this.mc.currentScreen instanceof GuiIngameMenu)
            {
                int i = Mouse.getEventX() * this.mc.currentScreen.width / this.mc.displayWidth;
                int j = this.mc.currentScreen.height - Mouse.getEventY() * this.mc.currentScreen.height / this.mc.displayHeight - 1;
                int k = Mouse.getEventButton();
                int deltaColor = 0;
                boolean galacticraft = Loader.isModLoaded("GalacticraftCore");
                float defaultval = galacticraft ? -35.0F : 0.0F;
                boolean height = galacticraft ? j > this.mc.currentScreen.height - 70 && j < this.mc.currentScreen.height - 35 : j > this.mc.currentScreen.height - 35;

                if (Minecraft.IS_RUNNING_ON_MAC && k == 0 && (Keyboard.isKeyDown(29) || Keyboard.isKeyDown(157)))
                {
                    k = 1;
                }
                if (i > this.mc.currentScreen.width - 101 && height)
                {
                    deltaColor = 50;

                    if (k == 0)
                    {
                        if (Mouse.getEventButtonState())
                        {
                            this.mc.displayGuiScreen(new ConfigGuiFactory.ConfigGUI(this.mc.currentScreen));
                            this.mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                        }
                    }
                }
                GlStateManager.translate(0.0F, defaultval, 0.0F);
                ClientRendererHelper.drawGradientRect(this.mc.currentScreen.width - 100, this.mc.currentScreen.height - 35, this.mc.currentScreen.width, this.mc.currentScreen.height, ClientRendererHelper.to32BitColor(150, 10 + deltaColor, 10 + deltaColor, 10 + deltaColor), ClientRendererHelper.to32BitColor(250, 10 + deltaColor, 10 + deltaColor, 10 + deltaColor));
                this.mc.fontRendererObj.drawString(I18n.format("gui.indicatorutils.config.name0"), this.mc.currentScreen.width - 50 - this.mc.fontRendererObj.getStringWidth(I18n.format("gui.indicatorutils.config.name0")) / 2, this.mc.currentScreen.height - 26, ClientRendererHelper.to32BitColor(255, 240, 240, 240));
                this.mc.fontRendererObj.drawString(I18n.format("gui.indicatorutils.config.name1"), this.mc.currentScreen.width - 50 - this.mc.fontRendererObj.getStringWidth(I18n.format("gui.indicatorutils.config.name1")) / 2, this.mc.currentScreen.height - 16, ClientRendererHelper.to32BitColor(255, 240, 240, 240));
                Gui.drawRect(this.mc.currentScreen.width - 100, this.mc.currentScreen.height - 35, this.mc.currentScreen.width - 99, this.mc.currentScreen.height, ClientRendererHelper.to32BitColor(255, 0, 0, 0));
                Gui.drawRect(this.mc.currentScreen.width - 100, this.mc.currentScreen.height - 35, this.mc.currentScreen.width, this.mc.currentScreen.height - 34, ClientRendererHelper.to32BitColor(255, 0, 0, 0));
            }
        }
    }

    private void initReflection()
    {
        this.sentMessages = ReflectionUtils.get("sentMessages", "field_146248_g", GuiNewChat.class, this.mc.ingameGUI.getChatGUI());
        this.chatLines = ReflectionUtils.get("chatLines", "field_146252_h", GuiNewChat.class, this.mc.ingameGUI.getChatGUI());
        this.drawnChatLines = ReflectionUtils.get("drawnChatLines", "field_146253_i", GuiNewChat.class, this.mc.ingameGUI.getChatGUI());
        IndicatorUtilsEventHandler.MAP_BOSS_INFOS = ReflectionUtils.get("mapBossInfos", "field_184060_g", GuiBossOverlay.class, this.mc.ingameGUI.getBossOverlay());
        this.overlayBoss = new GuiBossOverlayIU(this.mc);
        this.overlayPlayerList = new GuiPlayerTabOverlayIU(this.mc, this.mc.ingameGUI);
    }

    private void playerDetectorGlowingMode()
    {
        if (ConfigManager.enablePlayerDetector)
        {
            if (ConfigManager.playerDetectorMode.equalsIgnoreCase("GLOWING"))
            {
                if (this.mc.world != null)
                {
                    for (EntityPlayer playerList : this.mc.world.playerEntities)
                    {
                        if (playerList instanceof EntityOtherPlayerMP)
                        {
                            ((EntityOtherPlayerMP)playerList).setGlowing(true);
                        }
                    }
                }
            }
        }
    }

    private void autoFish()
    {
        if (IndicatorUtilsEventHandler.AUTO_FISH_ENABLED)
        {
            ++IndicatorUtilsEventHandler.AUTO_FISH_TICK;
            IndicatorUtilsEventHandler.AUTO_FISH_TICK %= 4;

            if (this.mc.player != null && this.mc.objectMouseOver != null && this.mc.world != null && this.mc.playerController != null && this.mc.entityRenderer != null)
            {
                if (IndicatorUtilsEventHandler.AUTO_FISH_TICK % 4 == 0)
                {
                    for (EnumHand enumhand : EnumHand.values())
                    {
                        ItemStack itemstack = this.mc.player.getHeldItem(enumhand);

                        if (itemstack.getItem() == Items.FISHING_ROD)
                        {
                            if (this.mc.objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK)
                            {
                                BlockPos blockpos = this.mc.objectMouseOver.getBlockPos();

                                if (this.mc.world.getBlockState(blockpos).getMaterial() != Material.AIR)
                                {
                                    int i = itemstack.getCount();
                                    EnumActionResult enumactionresult = this.mc.playerController.processRightClickBlock(this.mc.player, this.mc.world, blockpos, this.mc.objectMouseOver.sideHit, this.mc.objectMouseOver.hitVec, enumhand);

                                    if (enumactionresult == EnumActionResult.SUCCESS)
                                    {
                                        this.mc.player.swingArm(enumhand);

                                        if (!itemstack.isEmpty() && (itemstack.getCount() != i || this.mc.playerController.isInCreativeMode()))
                                        {
                                            this.mc.entityRenderer.itemRenderer.resetEquippedProgress(enumhand);
                                        }
                                        return;
                                    }
                                }
                            }
                            if (!itemstack.isEmpty() && this.mc.playerController.processRightClick(this.mc.player, this.mc.world, enumhand) == EnumActionResult.SUCCESS)
                            {
                                this.mc.entityRenderer.itemRenderer.resetEquippedProgress(enumhand);
                                return;
                            }
                        }
                        else
                        {
                            IndicatorUtilsEventHandler.AUTO_FISH_ENABLED = false;
                            IndicatorUtilsEventHandler.AUTO_FISH_TICK = 0;
                            this.mc.player.sendMessage(this.json.text("Stop using /autofish command, you must hold the fishing rod!"));
                            return;
                        }
                    }
                }
            }
        }
        else
        {
            IndicatorUtilsEventHandler.AUTO_FISH_TICK = 0;
        }
    }

    private void afkForPlayers()
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
                    if (this.mc.player != null)
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
                        this.mc.player.sendChatMessage("AFK : " + GameInfoHelper.INSTANCE.ticksToElapsedTime(tick) + " minute" + s + reason);
                    }
                }
            }

            if (IndicatorUtilsEventHandler.AFK_MODE.equalsIgnoreCase("idle"))
            {
                if (this.mc.player != null)
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
                    this.mc.player.turn(angle, angle);
                }
            }
            else
            {
                if (this.mc.player != null)
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
                    this.mc.player.turn(angle, angle);
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

    private void autoClearChat()
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