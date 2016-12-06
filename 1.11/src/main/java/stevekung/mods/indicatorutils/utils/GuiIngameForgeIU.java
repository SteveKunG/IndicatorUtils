/*
 * Minecraft Forge
 * Copyright (c) 2016.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation version 2.1
 * of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package stevekung.mods.indicatorutils.utils;

import static net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType.AIR;
import static net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType.ALL;
import static net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType.ARMOR;
import static net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType.BOSSHEALTH;
import static net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType.CHAT;
import static net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType.CROSSHAIRS;
import static net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType.DEBUG;
import static net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType.EXPERIENCE;
import static net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType.FOOD;
import static net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType.FPS_GRAPH;
import static net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType.HEALTH;
import static net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType.HEALTHMOUNT;
import static net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType.HELMET;
import static net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType.HOTBAR;
import static net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType.JUMPBAR;
import static net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType.PLAYER_LIST;
import static net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType.PORTAL;
import static net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType.POTION_ICONS;
import static net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType.SUBTITLES;
import static net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType.TEXT;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.google.common.collect.Ordering;

import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiBossOverlay;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiOverlayDebug;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.FoodStats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfoLerping;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;
import stevekung.mods.indicatorutils.ConfigManager;
import stevekung.mods.indicatorutils.utils.helper.StatusRendererHelper;

public class GuiIngameForgeIU extends GuiIngame
{
    private int WHITE = 0xFFFFFF;
    public static boolean renderHelmet = true;
    public static boolean renderPortal = true;
    public static boolean renderHotbar = true;
    public static boolean renderCrosshairs = true;
    public static boolean renderBossHealth = true;
    public static boolean renderHealth = true;
    public static boolean renderArmor = true;
    public static boolean renderFood = true;
    public static boolean renderHealthMount = true;
    public static boolean renderAir = true;
    public static boolean renderExperiance = true;
    public static boolean renderJumpBar = true;
    public static boolean renderObjective = true;
    public static int left_height = 39;
    public static int right_height = 39;
    private ScaledResolution res = null;
    private FontRenderer fontrenderer = null;
    private RenderGameOverlayEvent eventParent;
    private GuiOverlayDebugForge debugOverlay;
    private boolean recordShadow;
    private GuiBossOverlayForge overlayBossForge;
    public static Map<UUID, BossInfoLerping> mapBossInfos;
    private boolean initReflection = false;
    protected GuiPlayerTabOverlayForge overlayPlayerListForge;

    public GuiIngameForgeIU(Minecraft mc)
    {
        super(mc);
        this.debugOverlay = new GuiOverlayDebugForge(mc);
        this.overlayBossForge = new GuiBossOverlayForge(mc);
        this.overlayPlayerListForge = new GuiPlayerTabOverlayForge(mc, this);

        if (!this.initReflection)
        {
            GuiIngameForgeIU.mapBossInfos = ReflectionUtils.get("mapBossInfos", "field_184060_g", GuiBossOverlay.class, this.overlayBoss);
            this.initReflection = true;
        }
    }

    @Override
    public void renderGameOverlay(float partialTicks)
    {
        this.res = new ScaledResolution(this.mc);
        this.eventParent = new RenderGameOverlayEvent(partialTicks, this.res);
        int width = this.res.getScaledWidth();
        int height = this.res.getScaledHeight();
        renderHealthMount = this.mc.thePlayer.getRidingEntity() instanceof EntityLivingBase;
        renderFood = this.mc.thePlayer.getRidingEntity() == null;
        renderJumpBar = this.mc.thePlayer.isRidingHorse();
        right_height = 39;
        left_height = 39;

        if (this.pre(ALL))
        {
            return;
        }

        this.fontrenderer = this.mc.fontRendererObj;
        this.mc.entityRenderer.setupOverlayRendering();
        GlStateManager.enableBlend();

        if (Minecraft.isFancyGraphicsEnabled())
        {
            this.renderVignette(this.mc.thePlayer.getBrightness(partialTicks), this.res);
        }
        else
        {
            GlStateManager.enableDepth();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        }

        if (renderHelmet)
        {
            this.renderHelmet(this.res, partialTicks);
        }
        if (renderPortal && !this.mc.thePlayer.isPotionActive(MobEffects.NAUSEA))
        {
            this.renderPortal(this.res, partialTicks);
        }
        if (renderHotbar)
        {
            this.renderHotbar(this.res, partialTicks);
        }

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.zLevel = -90.0F;
        this.rand.setSeed(this.updateCounter * 312871);

        if (renderCrosshairs)
        {
            this.renderCrosshairs(partialTicks);
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        }
        if (renderBossHealth)
        {
            this.renderBossHealth();
        }

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        if (this.mc.playerController.shouldDrawHUD() && this.mc.getRenderViewEntity() instanceof EntityPlayer)
        {
            if (renderHealth)
            {
                this.renderHealth(width, height);
            }
            if (renderArmor)
            {
                this.renderArmor(width, height);
            }
            if (renderFood)
            {
                this.renderFood(width, height);
            }
            if (renderHealthMount)
            {
                this.renderHealthMount(width, height);
            }
            if (renderAir)
            {
                this.renderAir(width, height);
            }
        }

        this.renderSleepFade(width, height);

        if (renderJumpBar)
        {
            this.renderJumpBar(width, height);
        }
        else if (renderExperiance)
        {
            this.renderExperience(width, height);
        }

        this.renderToolHighlight(this.res);
        this.renderHUDText(width, height);
        this.renderFPSGraph();

        if (ConfigManager.renderIngamePotionEffect)
        {
            this.renderPotionIcons(this.res);
        }

        this.renderRecordOverlay(width, height, partialTicks);
        this.renderSubtitles(this.res);
        this.renderTitle(width, height, partialTicks);
        Scoreboard scoreboard = this.mc.theWorld.getScoreboard();
        ScoreObjective objective = null;
        ScorePlayerTeam scoreplayerteam = scoreboard.getPlayersTeam(this.mc.thePlayer.getName());

        if (scoreplayerteam != null)
        {
            int slot = scoreplayerteam.getChatFormat().getColorIndex();

            if (slot >= 0)
            {
                objective = scoreboard.getObjectiveInDisplaySlot(3 + slot);
            }
        }
        ScoreObjective scoreobjective1 = objective != null ? objective : scoreboard.getObjectiveInDisplaySlot(1);

        if (renderObjective && scoreobjective1 != null)
        {
            this.renderScoreboard(scoreobjective1, this.res);
        }
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.disableAlpha();
        this.renderChat(width, height);
        this.renderPlayerList(width, height);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.disableLighting();
        GlStateManager.enableAlpha();
        this.post(ALL);
    }

    public ScaledResolution getResolution()
    {
        return this.res;
    }

    protected void renderCrosshairs(float partialTicks)
    {
        if (this.pre(CROSSHAIRS))
        {
            return;
        }
        this.bind(Gui.ICONS);
        GlStateManager.enableBlend();
        super.renderAttackIndicator(partialTicks, this.res);
        this.post(CROSSHAIRS);
    }

    protected void renderPotionIcons(ScaledResolution resolution)
    {
        if (this.pre(POTION_ICONS))
        {
            return;
        }
        this.renderPotionEffects(resolution);
        this.post(POTION_ICONS);
    }

    @Override
    protected void renderPotionEffects(ScaledResolution resolution)
    {
        Collection<PotionEffect> collection = this.mc.thePlayer.getActivePotionEffects();

        if (!collection.isEmpty())
        {
            this.mc.getTextureManager().bindTexture(GuiContainer.INVENTORY_BACKGROUND);
            GlStateManager.enableBlend();
            int i = 0;
            int j = 0;

            for (PotionEffect potioneffect : Ordering.natural().reverse().sortedCopy(collection))
            {
                Potion potion = potioneffect.getPotion();

                if (!potion.shouldRenderHUD(potioneffect))
                {
                    continue;
                }
                // Rebind in case previous renderHUDEffect changed texture
                this.mc.getTextureManager().bindTexture(GuiContainer.INVENTORY_BACKGROUND);
                if (potioneffect.doesShowParticles())
                {
                    int k = resolution.getScaledWidth();
                    int l = 1;

                    if (this.mc.isDemo())
                    {
                        l += 15;
                    }

                    int i1 = potion.getStatusIconIndex();

                    if (potion.isBeneficial())
                    {
                        ++i;
                        k = k - 25 * i;
                    }
                    else
                    {
                        ++j;
                        k = k - 25 * j;
                        l += 26;
                    }

                    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                    float f = 1.0F;

                    if (potioneffect.getIsAmbient())
                    {
                        this.drawTexturedModalRect(k, l, 165, 166, 24, 24);

                        if (potioneffect.getDuration() <= 200)
                        {
                            int j1 = 10 - potioneffect.getDuration() / 20;
                            f = MathHelper.clamp_float(potioneffect.getDuration() / 10.0F / 5.0F * 0.5F, 0.0F, 0.5F) + MathHelper.cos(potioneffect.getDuration() * (float)Math.PI / 5.0F) * MathHelper.clamp_float(j1 / 10.0F * 0.25F, 0.0F, 0.25F);
                        }
                    }
                    else
                    {
                        this.drawTexturedModalRect(k, l, 141, 166, 24, 24);

                        if (potioneffect.getDuration() <= 200)
                        {
                            int j1 = 10 - potioneffect.getDuration() / 20;
                            f = MathHelper.clamp_float(potioneffect.getDuration() / 10.0F / 5.0F * 0.5F, 0.0F, 0.5F) + MathHelper.cos(potioneffect.getDuration() * (float)Math.PI / 5.0F) * MathHelper.clamp_float(j1 / 10.0F * 0.25F, 0.0F, 0.25F);
                        }
                    }

                    GlStateManager.color(1.0F, 1.0F, 1.0F, f);
                    // FORGE - Move status icon check down from above so renderHUDEffect will still be called without a status icon
                    if (potion.hasStatusIcon())
                    {
                        this.drawTexturedModalRect(k + 3, l + 3, i1 % 8 * 18, 198 + i1 / 8 * 18, 18, 18);
                    }
                    potion.renderHUDEffect(k, l, potioneffect, this.mc, f);
                }
            }
        }
    }

    protected void renderSubtitles(ScaledResolution resolution)
    {
        if (this.pre(SUBTITLES))
        {
            return;
        }
        this.overlaySubtitle.renderSubtitles(this.res);
        this.post(SUBTITLES);
    }

    protected void renderBossHealth()
    {
        if (this.pre(BOSSHEALTH))
        {
            return;
        }
        this.bind(Gui.ICONS);
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        this.mc.mcProfiler.startSection("bossHealth");
        GlStateManager.enableBlend();
        this.overlayBossForge.renderBossHealth();
        GlStateManager.disableBlend();
        this.mc.mcProfiler.endSection();
        this.post(BOSSHEALTH);
    }

    private void renderHelmet(ScaledResolution res, float partialTicks)
    {
        if (this.pre(HELMET))
        {
            return;
        }

        ItemStack itemstack = this.mc.thePlayer.inventory.armorItemInSlot(3);

        if (this.mc.gameSettings.thirdPersonView == 0 && itemstack != null && itemstack.getItem() != null)
        {
            if (itemstack.getItem() == Item.getItemFromBlock(Blocks.PUMPKIN))
            {
                this.renderPumpkinOverlay(res);
            }
            else
            {
                itemstack.getItem().renderHelmetOverlay(itemstack, this.mc.thePlayer, res, partialTicks);
            }
        }
        this.post(HELMET);
    }

    protected void renderArmor(int width, int height)
    {
        if (this.pre(ARMOR))
        {
            return;
        }

        this.mc.mcProfiler.startSection("armor");
        GlStateManager.enableBlend();
        int left = width / 2 - 91;
        int top = height - left_height;
        int level = ForgeHooks.getTotalArmorValue(this.mc.thePlayer);

        for (int i = 1; level > 0 && i < 20; i += 2)
        {
            if (i < level)
            {
                this.drawTexturedModalRect(left, top, 34, 9, 9, 9);
            }
            else if (i == level)
            {
                this.drawTexturedModalRect(left, top, 25, 9, 9, 9);
            }
            else if (i > level)
            {
                this.drawTexturedModalRect(left, top, 16, 9, 9, 9);
            }
            left += 8;
        }
        left_height += 10;
        GlStateManager.disableBlend();
        this.mc.mcProfiler.endSection();
        this.post(ARMOR);
    }

    protected void renderPortal(ScaledResolution res, float partialTicks)
    {
        if (this.pre(PORTAL))
        {
            return;
        }

        float f1 = this.mc.thePlayer.prevTimeInPortal + (this.mc.thePlayer.timeInPortal - this.mc.thePlayer.prevTimeInPortal) * partialTicks;

        if (f1 > 0.0F)
        {
            this.renderPortal(f1, res);
        }
        this.post(PORTAL);
    }

    @Override
    protected void renderHotbar(ScaledResolution res, float partialTicks)
    {
        if (this.pre(HOTBAR))
        {
            return;
        }

        if (this.mc.playerController.isSpectator())
        {
            this.spectatorGui.renderTooltip(res, partialTicks);
        }
        else
        {
            super.renderHotbar(res, partialTicks);
        }
        this.post(HOTBAR);
    }

    protected void renderAir(int width, int height)
    {
        if (this.pre(AIR))
        {
            return;
        }

        this.mc.mcProfiler.startSection("air");
        EntityPlayer player = (EntityPlayer)this.mc.getRenderViewEntity();
        GlStateManager.enableBlend();
        int left = width / 2 + 91;
        int top = height - right_height;

        if (player.isInsideOfMaterial(Material.WATER))
        {
            int air = player.getAir();
            int full = MathHelper.ceiling_double_int((air - 2) * 10.0D / 300.0D);
            int partial = MathHelper.ceiling_double_int(air * 10.0D / 300.0D) - full;

            for (int i = 0; i < full + partial; ++i)
            {
                this.drawTexturedModalRect(left - i * 8 - 9, top, i < full ? 16 : 25, 18, 9, 9);
            }
            right_height += 10;
        }
        GlStateManager.disableBlend();
        this.mc.mcProfiler.endSection();
        this.post(AIR);
    }

    public void renderHealth(int width, int height)
    {
        this.bind(ICONS);

        if (this.pre(HEALTH))
        {
            return;
        }

        this.mc.mcProfiler.startSection("health");
        GlStateManager.enableBlend();
        EntityPlayer player = (EntityPlayer)this.mc.getRenderViewEntity();
        int health = MathHelper.ceiling_float_int(player.getHealth());
        boolean highlight = this.healthUpdateCounter > this.updateCounter && (this.healthUpdateCounter - this.updateCounter) / 3L %2L == 1L;

        if (health < this.playerHealth && player.hurtResistantTime > 0)
        {
            this.lastSystemTime = Minecraft.getSystemTime();
            this.healthUpdateCounter = this.updateCounter + 20;
        }
        else if (health > this.playerHealth && player.hurtResistantTime > 0)
        {
            this.lastSystemTime = Minecraft.getSystemTime();
            this.healthUpdateCounter = this.updateCounter + 10;
        }

        if (Minecraft.getSystemTime() - this.lastSystemTime > 1000L)
        {
            this.playerHealth = health;
            this.lastPlayerHealth = health;
            this.lastSystemTime = Minecraft.getSystemTime();
        }

        this.playerHealth = health;
        int healthLast = this.lastPlayerHealth;
        IAttributeInstance attrMaxHealth = player.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH);
        float healthMax = (float)attrMaxHealth.getAttributeValue();
        float absorb = MathHelper.ceiling_float_int(player.getAbsorptionAmount());
        int healthRows = MathHelper.ceiling_float_int((healthMax + absorb) / 2.0F / 10.0F);
        int rowHeight = Math.max(10 - (healthRows - 2), 3);
        this.rand.setSeed(this.updateCounter * 312871);
        int left = width / 2 - 91;
        int top = height - left_height;
        left_height += healthRows * rowHeight;

        if (rowHeight != 10)
        {
            left_height += 10 - rowHeight;
        }

        int regen = -1;

        if (player.isPotionActive(MobEffects.REGENERATION))
        {
            regen = this.updateCounter % 25;
        }

        final int TOP =  9 * (this.mc.theWorld.getWorldInfo().isHardcoreModeEnabled() ? 5 : 0);
        final int BACKGROUND = highlight ? 25 : 16;
        int MARGIN = 16;

        if (player.isPotionActive(MobEffects.POISON))
        {
            MARGIN += 36;
        }
        else if (player.isPotionActive(MobEffects.WITHER))
        {
            MARGIN += 72;
        }

        float absorbRemaining = absorb;

        for (int i = MathHelper.ceiling_float_int((healthMax + absorb) / 2.0F) - 1; i >= 0; --i)
        {
            int row = MathHelper.ceiling_float_int((i + 1) / 10.0F) - 1;
            int x = left + i % 10 * 8;
            int y = top - row * rowHeight;

            if (health <= 4)
            {
                y += this.rand.nextInt(2);
            }
            if (i == regen)
            {
                y -= 2;
            }

            this.drawTexturedModalRect(x, y, BACKGROUND, TOP, 9, 9);

            if (highlight)
            {
                if (i * 2 + 1 < healthLast)
                {
                    this.drawTexturedModalRect(x, y, MARGIN + 54, TOP, 9, 9); //6
                }
                else if (i * 2 + 1 == healthLast)
                {
                    this.drawTexturedModalRect(x, y, MARGIN + 63, TOP, 9, 9); //7
                }
            }

            if (absorbRemaining > 0.0F)
            {
                if (absorbRemaining == absorb && absorb % 2.0F == 1.0F)
                {
                    this.drawTexturedModalRect(x, y, MARGIN + 153, TOP, 9, 9); //17
                    absorbRemaining -= 1.0F;
                }
                else
                {
                    this.drawTexturedModalRect(x, y, MARGIN + 144, TOP, 9, 9); //16
                    absorbRemaining -= 2.0F;
                }
            }
            else
            {
                if (i * 2 + 1 < health)
                {
                    this.drawTexturedModalRect(x, y, MARGIN + 36, TOP, 9, 9); //4
                }
                else if (i * 2 + 1 == health)
                {
                    this.drawTexturedModalRect(x, y, MARGIN + 45, TOP, 9, 9); //5
                }
            }
        }
        GlStateManager.disableBlend();
        this.mc.mcProfiler.endSection();
        this.post(HEALTH);
    }

    public void renderFood(int width, int height)
    {
        if (this.pre(FOOD))
        {
            return;
        }

        this.mc.mcProfiler.startSection("food");
        EntityPlayer player = (EntityPlayer)this.mc.getRenderViewEntity();
        GlStateManager.enableBlend();
        int left = width / 2 + 91;
        int top = height - right_height;
        right_height += 10;
        boolean unused = false;// Unused flag in vanilla, seems to be part of a 'fade out' mechanic
        FoodStats stats = this.mc.thePlayer.getFoodStats();
        int level = stats.getFoodLevel();

        for (int i = 0; i < 10; ++i)
        {
            int idx = i * 2 + 1;
            int x = left - i * 8 - 9;
            int y = top;
            int icon = 16;
            byte background = 0;

            if (this.mc.thePlayer.isPotionActive(MobEffects.HUNGER))
            {
                icon += 36;
                background = 13;
            }
            if (unused)
            {
                background = 1; //Probably should be a += 1 but vanilla never uses this
            }

            if (player.getFoodStats().getSaturationLevel() <= 0.0F && this.updateCounter % (level * 3 + 1) == 0)
            {
                y = top + this.rand.nextInt(3) - 1;
            }

            this.drawTexturedModalRect(x, y, 16 + background * 9, 27, 9, 9);

            if (idx < level)
            {
                this.drawTexturedModalRect(x, y, icon + 36, 27, 9, 9);
            }
            else if (idx == level)
            {
                this.drawTexturedModalRect(x, y, icon + 45, 27, 9, 9);
            }
        }
        GlStateManager.disableBlend();
        this.mc.mcProfiler.endSection();
        this.post(FOOD);
    }

    protected void renderSleepFade(int width, int height)
    {
        if (this.mc.thePlayer.getSleepTimer() > 0)
        {
            this.mc.mcProfiler.startSection("sleep");
            GlStateManager.disableDepth();
            GlStateManager.disableAlpha();
            int sleepTime = this.mc.thePlayer.getSleepTimer();
            float opacity = sleepTime / 100.0F;

            if (opacity > 1.0F)
            {
                opacity = 1.0F - (sleepTime - 100) / 10.0F;
            }
            int color = (int)(220.0F * opacity) << 24 | 1052704;
            drawRect(0, 0, width, height, color);
            GlStateManager.enableAlpha();
            GlStateManager.enableDepth();
            this.mc.mcProfiler.endSection();
        }
    }

    protected void renderExperience(int width, int height)
    {
        this.bind(ICONS);

        if (this.pre(EXPERIENCE))
        {
            return;
        }

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.disableBlend();

        if (this.mc.playerController.gameIsSurvivalOrAdventure())
        {
            this.mc.mcProfiler.startSection("expBar");
            int cap = this.mc.thePlayer.xpBarCap();
            int left = width / 2 - 91;

            if (cap > 0)
            {
                short barWidth = 182;
                int filled = (int)(this.mc.thePlayer.experience * (barWidth + 1));
                int top = height - 32 + 3;
                this.drawTexturedModalRect(left, top, 0, 64, barWidth, 5);

                if (filled > 0)
                {
                    this.drawTexturedModalRect(left, top, 0, 69, filled, 5);
                }
            }

            this.mc.mcProfiler.endSection();

            if (this.mc.playerController.gameIsSurvivalOrAdventure() && this.mc.thePlayer.experienceLevel > 0)
            {
                this.mc.mcProfiler.startSection("expLevel");
                boolean flag1 = false;
                int color = flag1 ? 16777215 : 8453920;
                String text = "" + this.mc.thePlayer.experienceLevel;
                int x = (width - this.fontrenderer.getStringWidth(text)) / 2;
                int y = height - 31 - 4;
                this.fontrenderer.drawString(text, x + 1, y, 0);
                this.fontrenderer.drawString(text, x - 1, y, 0);
                this.fontrenderer.drawString(text, x, y + 1, 0);
                this.fontrenderer.drawString(text, x, y - 1, 0);
                this.fontrenderer.drawString(text, x, y, color);
                this.mc.mcProfiler.endSection();
            }
        }
        GlStateManager.enableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.post(EXPERIENCE);
    }

    protected void renderJumpBar(int width, int height)
    {
        this.bind(ICONS);

        if (this.pre(JUMPBAR))
        {
            return;
        }

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.disableBlend();
        this.mc.mcProfiler.startSection("jumpBar");
        float charge = this.mc.thePlayer.getHorseJumpPower();
        final int barWidth = 182;
        int x = width / 2 - barWidth / 2;
        int filled = (int)(charge * (barWidth + 1));
        int top = height - 32 + 3;
        this.drawTexturedModalRect(x, top, 0, 84, barWidth, 5);

        if (filled > 0)
        {
            this.drawTexturedModalRect(x, top, 0, 89, filled, 5);
        }
        GlStateManager.enableBlend();
        this.mc.mcProfiler.endSection();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.post(JUMPBAR);
    }

    protected void renderToolHighlight(ScaledResolution res)
    {
        if (this.mc.gameSettings.heldItemTooltips && !this.mc.playerController.isSpectator())
        {
            this.mc.mcProfiler.startSection("toolHighlight");

            if (this.remainingHighlightTicks > 0 && this.highlightingItemStack != null)
            {
                String name = this.highlightingItemStack.getDisplayName();

                if (this.highlightingItemStack.hasDisplayName())
                {
                    name = TextFormatting.ITALIC + name;
                }

                name = this.highlightingItemStack.getItem().getHighlightTip(this.highlightingItemStack, name);
                int opacity = (int)(this.remainingHighlightTicks * 256.0F / 10.0F);

                if (opacity > 255)
                {
                    opacity = 255;
                }

                if (opacity > 0)
                {
                    int y = res.getScaledHeight() - 59;

                    if (!this.mc.playerController.shouldDrawHUD())
                    {
                        y += 14;
                    }

                    GlStateManager.pushMatrix();
                    GlStateManager.enableBlend();
                    GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
                    FontRenderer font = this.highlightingItemStack.getItem().getFontRenderer(this.highlightingItemStack);

                    if (font != null)
                    {
                        int x = (res.getScaledWidth() - font.getStringWidth(name)) / 2;
                        font.drawStringWithShadow(name, x, y, this.WHITE | opacity << 24);
                    }
                    else
                    {
                        int x = (res.getScaledWidth() - this.fontrenderer.getStringWidth(name)) / 2;
                        this.fontrenderer.drawStringWithShadow(name, x, y, this.WHITE | opacity << 24);
                    }
                    GlStateManager.disableBlend();
                    GlStateManager.popMatrix();
                }
            }
            this.mc.mcProfiler.endSection();
        }
        else if (this.mc.thePlayer.isSpectator())
        {
            this.spectatorGui.renderSelectedItem(res);
        }
    }

    protected void renderHUDText(int width, int height)
    {
        this.mc.mcProfiler.startSection("forgeHudText");
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        ArrayList<String> listL = new ArrayList<String>();
        ArrayList<String> listR = new ArrayList<String>();

        if (this.mc.isDemo())
        {
            long time = this.mc.theWorld.getTotalWorldTime();

            if (time >= 120500L)
            {
                listR.add(I18n.format("demo.demoExpired"));
            }
            else
            {
                listR.add(I18n.format("demo.remainingTime", StringUtils.ticksToElapsedTime((int)(120500L - time))));
            }
        }

        if (this.mc.gameSettings.showDebugInfo && !this.pre(DEBUG))
        {
            listL.addAll(this.debugOverlay.getLeft());
            listR.addAll(this.debugOverlay.getRight());
            this.post(DEBUG);
        }

        RenderGameOverlayEvent.Text event = new RenderGameOverlayEvent.Text(this.eventParent, listL, listR);

        if (!MinecraftForge.EVENT_BUS.post(event))
        {
            int top = 2;

            for (String msg : listL)
            {
                if (msg == null)
                {
                    continue;
                }
                drawRect(1, top - 1, 2 + this.fontrenderer.getStringWidth(msg) + 1, top + this.fontrenderer.FONT_HEIGHT - 1, -1873784752);
                this.fontrenderer.drawString(msg, 2, top, 14737632);
                top += this.fontrenderer.FONT_HEIGHT;
            }

            top = 2;

            for (String msg : listR)
            {
                if (msg == null)
                {
                    continue;
                }
                int w = this.fontrenderer.getStringWidth(msg);
                int left = width - 2 - w;
                drawRect(left - 1, top - 1, left + w + 1, top + this.fontrenderer.FONT_HEIGHT - 1, -1873784752);
                this.fontrenderer.drawString(msg, left, top, 14737632);
                top += this.fontrenderer.FONT_HEIGHT;
            }
        }
        this.mc.mcProfiler.endSection();
        this.post(TEXT);
    }

    protected void renderFPSGraph()
    {
        if (this.mc.gameSettings.showDebugInfo && this.mc.gameSettings.showLagometer && !this.pre(FPS_GRAPH))
        {
            this.debugOverlay.renderLagometer();
            this.post(FPS_GRAPH);
        }
    }

    protected void renderRecordOverlay(int width, int height, float partialTicks)
    {
        if (this.recordPlayingUpFor > 0)
        {
            this.mc.mcProfiler.startSection("overlayMessage");
            float hue = this.recordPlayingUpFor - partialTicks;
            int opacity = (int)(hue * 256.0F / 20.0F);

            if (opacity > 255)
            {
                opacity = 255;
            }
            if (opacity > 0)
            {
                GlStateManager.pushMatrix();
                GlStateManager.translate(width / 2, height - 68, 0.0F);
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
                int color = this.recordIsPlaying ? Color.HSBtoRGB(hue / 50.0F, 0.7F, 0.6F) & this.WHITE : this.WHITE;
                this.fontrenderer.drawString(this.recordPlaying, -this.fontrenderer.getStringWidth(this.recordPlaying) / 2, -4, color | opacity << 24, this.recordShadow);
                GlStateManager.disableBlend();
                GlStateManager.popMatrix();
            }
            this.mc.mcProfiler.endSection();
        }
    }

    public void setRecordPlaying(String message, boolean isPlaying, boolean shadow)
    {
        this.recordPlaying = message;
        this.recordPlayingUpFor = 60;
        this.recordIsPlaying = isPlaying;
        this.recordShadow = shadow;
    }

    public void setRecordPlaying(ITextComponent component, boolean isPlaying, boolean shadow)
    {
        this.setRecordPlaying(component.getUnformattedText(), isPlaying, shadow);
    }

    protected void renderTitle(int width, int height, float partialTicks)
    {
        if (this.titlesTimer > 0)
        {
            this.mc.mcProfiler.startSection("titleAndSubtitle");
            float age = this.titlesTimer - partialTicks;
            int opacity = 255;

            if (this.titlesTimer > this.titleFadeOut + this.titleDisplayTime)
            {
                float f3 = this.titleFadeIn + this.titleDisplayTime + this.titleFadeOut - age;
                opacity = (int)(f3 * 255.0F / this.titleFadeIn);
            }
            if (this.titlesTimer <= this.titleFadeOut)
            {
                opacity = (int)(age * 255.0F / this.titleFadeOut);
            }

            opacity = MathHelper.clamp_int(opacity, 0, 255);

            if (opacity > 8)
            {
                GlStateManager.pushMatrix();
                GlStateManager.translate(width / 2, height / 2, 0.0F);
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
                GlStateManager.pushMatrix();
                GlStateManager.scale(4.0F, 4.0F, 4.0F);
                int l = opacity << 24 & -16777216;
                this.getFontRenderer().drawString(this.displayedTitle, -this.getFontRenderer().getStringWidth(this.displayedTitle) / 2, -10.0F, 16777215 | l, true);
                GlStateManager.popMatrix();
                GlStateManager.pushMatrix();
                GlStateManager.scale(2.0F, 2.0F, 2.0F);
                this.getFontRenderer().drawString(this.displayedSubTitle, -this.getFontRenderer().getStringWidth(this.displayedSubTitle) / 2, 5.0F, 16777215 | l, true);
                GlStateManager.popMatrix();
                GlStateManager.disableBlend();
                GlStateManager.popMatrix();
            }
            this.mc.mcProfiler.endSection();
        }
    }

    protected void renderChat(int width, int height)
    {
        this.mc.mcProfiler.startSection("chat");
        RenderGameOverlayEvent.Chat event = new RenderGameOverlayEvent.Chat(this.eventParent, 0, height - 48);

        if (MinecraftForge.EVENT_BUS.post(event))
        {
            return;
        }
        GlStateManager.pushMatrix();
        GlStateManager.translate(event.getPosX(), event.getPosY(), 0.0F);
        GlStateManager.disableDepth();
        this.persistantChatGUI.drawChat(this.updateCounter);
        GlStateManager.enableDepth();
        GlStateManager.popMatrix();
        this.post(CHAT);
        this.mc.mcProfiler.endSection();
    }

    protected void renderPlayerList(int width, int height)
    {
        ScoreObjective scoreobjective = this.mc.theWorld.getScoreboard().getObjectiveInDisplaySlot(0);
        NetHandlerPlayClient handler = this.mc.thePlayer.connection;

        if (this.mc.gameSettings.keyBindPlayerList.isKeyDown() && (!this.mc.isIntegratedServerRunning() || handler.getPlayerInfoMap().size() > 1 || scoreobjective != null))
        {
            this.overlayPlayerListForge.updatePlayerList(true);

            if (this.pre(PLAYER_LIST))
            {
                return;
            }
            this.overlayPlayerListForge.renderPlayerlist(width, this.mc.theWorld.getScoreboard(), scoreobjective);
            this.post(PLAYER_LIST);
        }
        else
        {
            this.overlayPlayerListForge.updatePlayerList(false);
        }
    }

    protected void renderHealthMount(int width, int height)
    {
        EntityPlayer player = (EntityPlayer)this.mc.getRenderViewEntity();
        Entity tmp = player.getRidingEntity();

        if (!(tmp instanceof EntityLivingBase))
        {
            return;
        }

        this.bind(ICONS);

        if (this.pre(HEALTHMOUNT))
        {
            return;
        }

        boolean unused = false;
        int left_align = width / 2 + 91;
        this.mc.mcProfiler.endStartSection("mountHealth");
        GlStateManager.enableBlend();
        EntityLivingBase mount = (EntityLivingBase)tmp;
        int health = (int)Math.ceil(mount.getHealth());
        float healthMax = mount.getMaxHealth();
        int hearts = (int)(healthMax + 0.5F) / 2;

        if (hearts > 30)
        {
            hearts = 30;
        }

        final int MARGIN = 52;
        final int BACKGROUND = MARGIN + (unused ? 1 : 0);
        final int HALF = MARGIN + 45;
        final int FULL = MARGIN + 36;

        for (int heart = 0; hearts > 0; heart += 20)
        {
            int top = height - right_height;
            int rowCount = Math.min(hearts, 10);
            hearts -= rowCount;

            for (int i = 0; i < rowCount; ++i)
            {
                int x = left_align - i * 8 - 9;
                this.drawTexturedModalRect(x, top, BACKGROUND, 9, 9, 9);

                if (i * 2 + 1 + heart < health)
                {
                    this.drawTexturedModalRect(x, top, FULL, 9, 9, 9);
                }
                else if (i * 2 + 1 + heart == health)
                {
                    this.drawTexturedModalRect(x, top, HALF, 9, 9, 9);
                }
            }
            right_height += 10;
        }
        GlStateManager.disableBlend();
        this.post(HEALTHMOUNT);
    }

    //Helper macros
    private boolean pre(ElementType type)
    {
        return MinecraftForge.EVENT_BUS.post(new RenderGameOverlayEvent.Pre(this.eventParent, type));
    }

    private void post(ElementType type)
    {
        MinecraftForge.EVENT_BUS.post(new RenderGameOverlayEvent.Post(this.eventParent, type));
    }

    private void bind(ResourceLocation res)
    {
        this.mc.getTextureManager().bindTexture(res);
    }

    private class GuiOverlayDebugForge extends GuiOverlayDebug
    {
        private Minecraft mc;

        private GuiOverlayDebugForge(Minecraft mc)
        {
            super(mc);
            this.mc = mc;
        }

        @Override
        protected void renderDebugInfoLeft() {}

        @Override
        protected void renderDebugInfoRight(ScaledResolution res) {}

        private List<String> getLeft()
        {
            List<String> ret = this.call();
            ret.add("");
            ret.add("Debug: Pie [shift]: " + (this.mc.gameSettings.showDebugProfilerChart ? "visible" : "hidden") + " FPS [alt]: " + (this.mc.gameSettings.showLagometer ? "visible" : "hidden"));
            ret.add("For help: press F3 + Q");
            return ret;
        }

        private List<String> getRight()
        {
            return this.getDebugInfoRight();
        }
    }

    private class GuiBossOverlayForge extends GuiBossOverlay
    {
        private ResourceLocation GUI_BARS_TEXTURES = new ResourceLocation("textures/gui/bars.png");
        private Minecraft mc;

        public GuiBossOverlayForge(Minecraft mc)
        {
            super(mc);
            this.mc = mc;
        }

        @Override
        public void renderBossHealth()
        {
            if (!GuiIngameForgeIU.mapBossInfos.isEmpty())
            {
                ScaledResolution scaledresolution = new ScaledResolution(this.mc);
                int i = scaledresolution.getScaledWidth();
                int j = 12;

                for (BossInfoLerping bossinfolerping : GuiIngameForgeIU.mapBossInfos.values())
                {
                    int k = i / 2 - 91;
                    RenderGameOverlayEvent.BossInfo event = ForgeHooksClient.bossBarRenderPre(scaledresolution, bossinfolerping, k, j, 10 + this.mc.fontRendererObj.FONT_HEIGHT);

                    if (!event.isCanceled())
                    {
                        if (ConfigManager.hideBossHealthBar)
                        {
                            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                            this.mc.getTextureManager().bindTexture(this.GUI_BARS_TEXTURES);
                            this.render(k, j, bossinfolerping);
                        }
                        String s = bossinfolerping.getName().getFormattedText();
                        this.mc.fontRendererObj.drawStringWithShadow(s, i / 2 - this.mc.fontRendererObj.getStringWidth(s) / 2, j - 9, 16777215);
                    }

                    if (!ConfigManager.hideBossHealthBar)
                    {
                        j += 12;
                    }
                    else
                    {
                        j += event.getIncrement();
                    }

                    ForgeHooksClient.bossBarRenderPost(scaledresolution);

                    if (j >= scaledresolution.getScaledHeight() / 3)
                    {
                        break;
                    }
                }
            }
        }

        private void render(int x, int y, BossInfo info)
        {
            this.drawTexturedModalRect(x, y, 0, info.getColor().ordinal() * 5 * 2, 182, 5);

            if (info.getOverlay() != BossInfo.Overlay.PROGRESS)
            {
                this.drawTexturedModalRect(x, y, 0, 80 + (info.getOverlay().ordinal() - 1) * 5 * 2, 182, 5);
            }

            int i = (int)(info.getPercent() * 183.0F);

            if (i > 0)
            {
                this.drawTexturedModalRect(x, y, 0, info.getColor().ordinal() * 5 * 2 + 5, i, 5);

                if (info.getOverlay() != BossInfo.Overlay.PROGRESS)
                {
                    this.drawTexturedModalRect(x, y, 0, 80 + (info.getOverlay().ordinal() - 1) * 5 * 2 + 5, i, 5);
                }
            }
        }
    }

    private class GuiPlayerTabOverlayForge extends GuiPlayerTabOverlay
    {
        private Minecraft mc;

        public GuiPlayerTabOverlayForge(Minecraft mc, GuiIngame guiIngame)
        {
            super(mc, guiIngame);
            this.mc = mc;
        }

        @Override
        protected void drawPing(int x1, int x2, int y, NetworkPlayerInfo networkPlayerInfoIn)
        {
            if (ConfigManager.playerPingMode.equalsIgnoreCase("number"))
            {
                EnumTextColor color = EnumTextColor.GREEN;

                if (networkPlayerInfoIn.getResponseTime() >= 200 && networkPlayerInfoIn.getResponseTime() <= 300)
                {
                    color = EnumTextColor.YELLOW;
                }
                else if (networkPlayerInfoIn.getResponseTime() >= 301 && networkPlayerInfoIn.getResponseTime() <= 499)
                {
                    color = EnumTextColor.RED;
                }
                else if (networkPlayerInfoIn.getResponseTime() >= 500)
                {
                    color = EnumTextColor.DARK_RED;
                }
                StatusRendererHelper.INSTANCE.drawString(String.valueOf(networkPlayerInfoIn.getResponseTime()), x1 + x2 - this.mc.fontRendererObj.getStringWidth(String.valueOf(networkPlayerInfoIn.getResponseTime())), y + 0.5F, color, true);
            }
            else
            {
                super.drawPing(x1, x2, y, networkPlayerInfoIn);
            }
        }
    }
}