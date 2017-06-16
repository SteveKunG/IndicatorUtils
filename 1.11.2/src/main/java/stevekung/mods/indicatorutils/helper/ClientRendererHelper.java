/*******************************************************************************
 *
 * Copyright 2016 Wasinthorn Suksri/SteveKunG - Indicator Utils
 *
 ******************************************************************************/

package stevekung.mods.indicatorutils.helper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.*;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.*;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import stevekung.mods.indicatorutils.config.ExtendedModSettings;
import stevekung.mods.indicatorutils.utils.ArmorType;
import stevekung.mods.indicatorutils.utils.EnumTextColor;

public class ClientRendererHelper
{
    public static int to32BitColor(int a, int r, int g, int b)
    {
        a = a << 24;
        r = r << 16;
        g = g << 8;
        return a | r | g | b;
    }

    public static void drawGradientRect(int left, int top, int right, int bottom, int startColor, int endColor)
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

    public static void drawRect(double top, double bottom, double left, double right, int color)
    {
        float r = (color >> 16 & 255) / 255.0F;
        float g = (color >> 8 & 255) / 255.0F;
        float b = (color & 255) / 255.0F;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel(7425);
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer vertexbuffer = tessellator.getBuffer();
        vertexbuffer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        vertexbuffer.pos(right, top, 0.0D).color(r, g, b, 0.35F).endVertex();
        vertexbuffer.pos(left, top, 0.0D).color(r, g, b, 0.35F).endVertex();
        vertexbuffer.pos(left, bottom, 0.0D).color(r, g, b, 0.35F).endVertex();
        vertexbuffer.pos(right, bottom, 0.0D).color(r, g, b, 0.35F).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    public static void drawRectNew(int left, int top, int right, int bottom, int color, float alpha)
    {
        if (alpha > 0.1F)
        {
            if (left < right)
            {
                int i = left;
                left = right;
                right = i;
            }
            if (top < bottom)
            {
                int j = top;
                top = bottom;
                bottom = j;
            }
            float f = (color >> 16 & 255) / 255.0F;
            float f1 = (color >> 8 & 255) / 255.0F;
            float f2 = (color & 255) / 255.0F;
            Tessellator tessellator = Tessellator.getInstance();
            VertexBuffer vertexbuffer = tessellator.getBuffer();
            GlStateManager.enableBlend();
            GlStateManager.disableTexture2D();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.color(f, f1, f2, alpha);
            vertexbuffer.begin(7, DefaultVertexFormats.POSITION);
            vertexbuffer.pos(left, bottom, 0.0D).endVertex();
            vertexbuffer.pos(right, bottom, 0.0D).endVertex();
            vertexbuffer.pos(right, top, 0.0D).endVertex();
            vertexbuffer.pos(left, top, 0.0D).endVertex();
            tessellator.draw();
            GlStateManager.enableTexture2D();
            GlStateManager.disableBlend();
        }
    }

    public static void drawString(String message, float x, float y, EnumTextColor color, boolean shadow)
    {
        ClientRendererHelper.drawString(message, x, y, color.getColor(), shadow);
    }

    public static void drawString(String message, float x, float y, int color, boolean shadow)
    {
        Minecraft.getMinecraft().fontRendererObj.drawString(message, x, y, color, shadow);
    }

    public static void drawStringAtRecord(String text, float partialTicks)
    {
        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution scaledresolution = new ScaledResolution(mc);
        boolean isCreative = mc.player.capabilities.isCreativeMode;
        int height = isCreative ? 60 : 80;
        int i = scaledresolution.getScaledWidth();
        int j = scaledresolution.getScaledHeight();
        float f2 = 256 - partialTicks;
        int l1 = (int)(f2 * 255.0F / 20.0F);

        if (l1 > 255)
        {
            l1 = 255;
        }
        if (l1 > 8)
        {
            GlStateManager.pushMatrix();
            GlStateManager.translate(i / 2, j - height + -0.5F, 0.0F);
            ClientRendererHelper.drawRect(6, -6, mc.fontRendererObj.getStringWidth(text) / 2 + 2, -mc.fontRendererObj.getStringWidth(text) / 2 - 2, 16777216);
            GlStateManager.popMatrix();

            GlStateManager.pushMatrix();
            GlStateManager.translate(i / 2, j - height, 0.0F);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            int l = 16777215;
            ClientRendererHelper.drawString(text, -mc.fontRendererObj.getStringWidth(text) / 2, -4, l + (l1 << 24 & -16777216), true);
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
        }
    }

    public static void renderArmorWithEffect(ArmorType type, int x, int y)
    {
        RenderHelper.enableGUIStandardItemLighting();
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(StatusRendererHelper.INSTANCE.getArmorType(type), x, y);
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableBlend();
        RenderHelper.disableStandardItemLighting();
        RenderHelper.enableGUIStandardItemLighting();
        GlStateManager.disableLighting();
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableColorMaterial();
        GlStateManager.disableLighting();
        GlStateManager.enableCull();
        Minecraft.getMinecraft().getRenderItem().renderItemOverlays(Minecraft.getMinecraft().fontRendererObj, StatusRendererHelper.INSTANCE.getArmorType(type), x, y);
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.disableLighting();
    }

    public static void renderItemWithEffect(ItemStack itemStack, int x, int y)
    {
        RenderHelper.enableGUIStandardItemLighting();
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(itemStack, x, y);
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableBlend();
        RenderHelper.disableStandardItemLighting();

        if (itemStack.isItemStackDamageable())
        {
            RenderHelper.enableGUIStandardItemLighting();
            GlStateManager.disableLighting();
            GlStateManager.enableRescaleNormal();
            GlStateManager.enableColorMaterial();
            GlStateManager.disableLighting();
            GlStateManager.enableCull();
            Minecraft.getMinecraft().getRenderItem().renderItemOverlays(Minecraft.getMinecraft().fontRendererObj, itemStack, x, y);
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            GlStateManager.disableLighting();
        }
    }

    public static void bindKeystrokeTexture(String texture)
    {
        Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("indicatorutils:textures/gui/" + texture + ".png"));
        GlStateManager.color(1.0F, 1.0F, 1.0F);
    }

    public static void runGlowingEntityDetector()
    {
        Minecraft mc = Minecraft.getMinecraft();

        if (mc.world != null)
        {
            for (Entity list : mc.world.loadedEntityList)
            {
                if (ExtendedModSettings.ENTITY_DETECT_TYPE.equalsIgnoreCase("all"))
                {
                    if (!(list instanceof EntityPlayerSP))
                    {
                        list.setGlowing(true);
                    }
                    else
                    {
                        list.setGlowing(false);
                    }
                }
                else if (ExtendedModSettings.ENTITY_DETECT_TYPE.equalsIgnoreCase("only_mob"))
                {
                    if (list instanceof EntityMob || list instanceof IMob)
                    {
                        list.setGlowing(true);
                    }
                    else
                    {
                        list.setGlowing(false);
                    }
                }
                else if (ExtendedModSettings.ENTITY_DETECT_TYPE.equalsIgnoreCase("only_creature"))
                {
                    if (list instanceof EntityAnimal)
                    {
                        list.setGlowing(true);
                    }
                    else
                    {
                        list.setGlowing(false);
                    }
                }
                else if (ExtendedModSettings.ENTITY_DETECT_TYPE.equalsIgnoreCase("only_non_mob"))
                {
                    if (list instanceof EntityItem || list instanceof EntityXPOrb || list instanceof EntityArmorStand || list instanceof EntityBoat || list instanceof EntityMinecart)
                    {
                        list.setGlowing(true);
                    }
                    else
                    {
                        list.setGlowing(false);
                    }
                }
                else if (ExtendedModSettings.ENTITY_DETECT_TYPE.equalsIgnoreCase("only_player"))
                {
                    if (list instanceof EntityOtherPlayerMP)
                    {
                        list.setGlowing(true);
                    }
                    else
                    {
                        list.setGlowing(false);
                    }
                }
                else if (ExtendedModSettings.ENTITY_DETECT_TYPE.equalsIgnoreCase("zombie"))
                {
                    if (list instanceof EntityZombie)
                    {
                        list.setGlowing(true);
                    }
                    else
                    {
                        list.setGlowing(false);
                    }
                }
                else if (ExtendedModSettings.ENTITY_DETECT_TYPE.equalsIgnoreCase("zombie_villager"))
                {
                    if (list instanceof EntityZombieVillager)
                    {
                        list.setGlowing(true);
                    }
                    else
                    {
                        list.setGlowing(false);
                    }
                }
                else if (ExtendedModSettings.ENTITY_DETECT_TYPE.equalsIgnoreCase("husk"))
                {
                    if (list instanceof EntityHusk)
                    {
                        list.setGlowing(true);
                    }
                    else
                    {
                        list.setGlowing(false);
                    }
                }
                else if (ExtendedModSettings.ENTITY_DETECT_TYPE.equalsIgnoreCase("creeper"))
                {
                    if (list instanceof EntityCreeper)
                    {
                        list.setGlowing(true);
                    }
                    else
                    {
                        list.setGlowing(false);
                    }
                }
                else if (ExtendedModSettings.ENTITY_DETECT_TYPE.equalsIgnoreCase("skeleton"))
                {
                    if (list instanceof EntitySkeleton)
                    {
                        list.setGlowing(true);
                    }
                    else
                    {
                        list.setGlowing(false);
                    }
                }
                else if (ExtendedModSettings.ENTITY_DETECT_TYPE.equalsIgnoreCase("wither_skeleton"))
                {
                    if (list instanceof EntityWitherSkeleton)
                    {
                        list.setGlowing(true);
                    }
                    else
                    {
                        list.setGlowing(false);
                    }
                }
                else if (ExtendedModSettings.ENTITY_DETECT_TYPE.equalsIgnoreCase("stray"))
                {
                    if (list instanceof EntityStray)
                    {
                        list.setGlowing(true);
                    }
                    else
                    {
                        list.setGlowing(false);
                    }
                }
                else if (ExtendedModSettings.ENTITY_DETECT_TYPE.equalsIgnoreCase("spider"))
                {
                    if (list instanceof EntitySpider)
                    {
                        list.setGlowing(true);
                    }
                    else
                    {
                        list.setGlowing(false);
                    }
                }
                else if (ExtendedModSettings.ENTITY_DETECT_TYPE.equalsIgnoreCase("slime"))
                {
                    if (list instanceof EntitySlime)
                    {
                        list.setGlowing(true);
                    }
                    else
                    {
                        list.setGlowing(false);
                    }
                }
                else if (ExtendedModSettings.ENTITY_DETECT_TYPE.equalsIgnoreCase("magma_cube"))
                {
                    if (list instanceof EntityMagmaCube)
                    {
                        list.setGlowing(true);
                    }
                    else
                    {
                        list.setGlowing(false);
                    }
                }
                else if (ExtendedModSettings.ENTITY_DETECT_TYPE.equalsIgnoreCase("ghast"))
                {
                    if (list instanceof EntityGhast)
                    {
                        list.setGlowing(true);
                    }
                    else
                    {
                        list.setGlowing(false);
                    }
                }
                else if (ExtendedModSettings.ENTITY_DETECT_TYPE.equalsIgnoreCase("enderman"))
                {
                    if (list instanceof EntityEnderman)
                    {
                        list.setGlowing(true);
                    }
                    else
                    {
                        list.setGlowing(false);
                    }
                }
                else if (ExtendedModSettings.ENTITY_DETECT_TYPE.equalsIgnoreCase("silverfish"))
                {
                    if (list instanceof EntitySilverfish || list instanceof EntityEndermite)
                    {
                        list.setGlowing(true);
                    }
                    else
                    {
                        list.setGlowing(false);
                    }
                }
                else if (ExtendedModSettings.ENTITY_DETECT_TYPE.equalsIgnoreCase("blaze"))
                {
                    if (list instanceof EntityBlaze)
                    {
                        list.setGlowing(true);
                    }
                    else
                    {
                        list.setGlowing(false);
                    }
                }
                else if (ExtendedModSettings.ENTITY_DETECT_TYPE.equalsIgnoreCase("witch"))
                {
                    if (list instanceof EntityWitch)
                    {
                        list.setGlowing(true);
                    }
                    else
                    {
                        list.setGlowing(false);
                    }
                }
                else if (ExtendedModSettings.ENTITY_DETECT_TYPE.equalsIgnoreCase("guardian"))
                {
                    if (list instanceof EntityGuardian)
                    {
                        list.setGlowing(true);
                    }
                    else
                    {
                        list.setGlowing(false);
                    }
                }
                else if (ExtendedModSettings.ENTITY_DETECT_TYPE.equalsIgnoreCase("elder_guardian"))
                {
                    if (list instanceof EntityElderGuardian)
                    {
                        list.setGlowing(true);
                    }
                    else
                    {
                        list.setGlowing(false);
                    }
                }
                else if (ExtendedModSettings.ENTITY_DETECT_TYPE.equalsIgnoreCase("shulker"))
                {
                    if (list instanceof EntityShulker)
                    {
                        list.setGlowing(true);
                    }
                    else
                    {
                        list.setGlowing(false);
                    }
                }
                else if (ExtendedModSettings.ENTITY_DETECT_TYPE.equalsIgnoreCase("pig"))
                {
                    if (list instanceof EntityPig)
                    {
                        list.setGlowing(true);
                    }
                    else
                    {
                        list.setGlowing(false);
                    }
                }
                else if (ExtendedModSettings.ENTITY_DETECT_TYPE.equalsIgnoreCase("sheep"))
                {
                    if (list instanceof EntitySheep)
                    {
                        list.setGlowing(true);
                    }
                    else
                    {
                        list.setGlowing(false);
                    }
                }
                else if (ExtendedModSettings.ENTITY_DETECT_TYPE.equalsIgnoreCase("cow"))
                {
                    if (list instanceof EntityCow || list instanceof EntityMooshroom)
                    {
                        list.setGlowing(true);
                    }
                    else
                    {
                        list.setGlowing(false);
                    }
                }
                else if (ExtendedModSettings.ENTITY_DETECT_TYPE.equalsIgnoreCase("chicken"))
                {
                    if (list instanceof EntityChicken)
                    {
                        list.setGlowing(true);
                    }
                    else
                    {
                        list.setGlowing(false);
                    }
                }
                else if (ExtendedModSettings.ENTITY_DETECT_TYPE.equalsIgnoreCase("squid"))
                {
                    if (list instanceof EntitySquid)
                    {
                        list.setGlowing(true);
                    }
                    else
                    {
                        list.setGlowing(false);
                    }
                }
                else if (ExtendedModSettings.ENTITY_DETECT_TYPE.equalsIgnoreCase("wolf"))
                {
                    if (list instanceof EntityWolf)
                    {
                        list.setGlowing(true);
                    }
                    else
                    {
                        list.setGlowing(false);
                    }
                }
                else if (ExtendedModSettings.ENTITY_DETECT_TYPE.equalsIgnoreCase("snowman"))
                {
                    if (list instanceof EntitySnowman)
                    {
                        list.setGlowing(true);
                    }
                    else
                    {
                        list.setGlowing(false);
                    }
                }
                else if (ExtendedModSettings.ENTITY_DETECT_TYPE.equalsIgnoreCase("ocelot"))
                {
                    if (list instanceof EntityOcelot)
                    {
                        list.setGlowing(true);
                    }
                    else
                    {
                        list.setGlowing(false);
                    }
                }
                else if (ExtendedModSettings.ENTITY_DETECT_TYPE.equalsIgnoreCase("iron_golem"))
                {
                    if (list instanceof EntityIronGolem)
                    {
                        list.setGlowing(true);
                    }
                    else
                    {
                        list.setGlowing(false);
                    }
                }
                else if (ExtendedModSettings.ENTITY_DETECT_TYPE.equalsIgnoreCase("horse"))
                {
                    if (list instanceof EntityHorse)
                    {
                        list.setGlowing(true);
                    }
                    else
                    {
                        list.setGlowing(false);
                    }
                }
                else if (ExtendedModSettings.ENTITY_DETECT_TYPE.equalsIgnoreCase("donkey"))
                {
                    if (list instanceof EntityDonkey)
                    {
                        list.setGlowing(true);
                    }
                    else
                    {
                        list.setGlowing(false);
                    }
                }
                else if (ExtendedModSettings.ENTITY_DETECT_TYPE.equalsIgnoreCase("mule"))
                {
                    if (list instanceof EntityMule)
                    {
                        list.setGlowing(true);
                    }
                    else
                    {
                        list.setGlowing(false);
                    }
                }
                else if (ExtendedModSettings.ENTITY_DETECT_TYPE.equalsIgnoreCase("skeleton_horse"))
                {
                    if (list instanceof EntitySkeletonHorse)
                    {
                        list.setGlowing(true);
                    }
                    else
                    {
                        list.setGlowing(false);
                    }
                }
                else if (ExtendedModSettings.ENTITY_DETECT_TYPE.equalsIgnoreCase("zombie_horse"))
                {
                    if (list instanceof EntityZombieHorse)
                    {
                        list.setGlowing(true);
                    }
                    else
                    {
                        list.setGlowing(false);
                    }
                }
                else if (ExtendedModSettings.ENTITY_DETECT_TYPE.equalsIgnoreCase("rabbit"))
                {
                    if (list instanceof EntityRabbit)
                    {
                        list.setGlowing(true);
                    }
                    else
                    {
                        list.setGlowing(false);
                    }
                }
                else if (ExtendedModSettings.ENTITY_DETECT_TYPE.equalsIgnoreCase("polar_bear"))
                {
                    if (list instanceof EntityPolarBear)
                    {
                        list.setGlowing(true);
                    }
                    else
                    {
                        list.setGlowing(false);
                    }
                }
                else if (ExtendedModSettings.ENTITY_DETECT_TYPE.equalsIgnoreCase("llama"))
                {
                    if (list instanceof EntityLlama)
                    {
                        list.setGlowing(true);
                    }
                    else
                    {
                        list.setGlowing(false);
                    }
                }
                else if (ExtendedModSettings.ENTITY_DETECT_TYPE.equalsIgnoreCase("evoker"))
                {
                    if (list instanceof EntityEvoker)
                    {
                        list.setGlowing(true);
                    }
                    else
                    {
                        list.setGlowing(false);
                    }
                }
                else if (ExtendedModSettings.ENTITY_DETECT_TYPE.equalsIgnoreCase("vex"))
                {
                    if (list instanceof EntityVex)
                    {
                        list.setGlowing(true);
                    }
                    else
                    {
                        list.setGlowing(false);
                    }
                }
                else if (ExtendedModSettings.ENTITY_DETECT_TYPE.equalsIgnoreCase("vindicator"))
                {
                    if (list instanceof EntityVindicator)
                    {
                        list.setGlowing(true);
                    }
                    else
                    {
                        list.setGlowing(false);
                    }
                }
                else if (ExtendedModSettings.ENTITY_DETECT_TYPE.equalsIgnoreCase("villager"))
                {
                    if (list instanceof EntityVillager)
                    {
                        list.setGlowing(true);
                    }
                    else
                    {
                        list.setGlowing(false);
                    }
                }
                else if (ExtendedModSettings.ENTITY_DETECT_TYPE.equalsIgnoreCase("item"))
                {
                    if (list instanceof EntityItem)
                    {
                        list.setGlowing(true);
                    }
                    else
                    {
                        list.setGlowing(false);
                    }
                }
                else if (ExtendedModSettings.ENTITY_DETECT_TYPE.equalsIgnoreCase("xp"))
                {
                    if (list instanceof EntityXPOrb)
                    {
                        list.setGlowing(true);
                    }
                    else
                    {
                        list.setGlowing(false);
                    }
                }
                else if (ExtendedModSettings.ENTITY_DETECT_TYPE.equalsIgnoreCase("armor_stand"))
                {
                    if (list instanceof EntityArmorStand)
                    {
                        list.setGlowing(true);
                    }
                    else
                    {
                        list.setGlowing(false);
                    }
                }
                else if (ExtendedModSettings.ENTITY_DETECT_TYPE.equalsIgnoreCase("boat"))
                {
                    if (list instanceof EntityBoat)
                    {
                        list.setGlowing(true);
                    }
                    else
                    {
                        list.setGlowing(false);
                    }
                }
                else if (ExtendedModSettings.ENTITY_DETECT_TYPE.equalsIgnoreCase("minecart"))
                {
                    if (list instanceof EntityMinecart)
                    {
                        list.setGlowing(true);
                    }
                    else
                    {
                        list.setGlowing(false);
                    }
                }
                else if (ExtendedModSettings.ENTITY_DETECT_TYPE.equalsIgnoreCase("ender_crystal"))
                {
                    if (list instanceof EntityEnderCrystal)
                    {
                        list.setGlowing(true);
                    }
                    else
                    {
                        list.setGlowing(false);
                    }
                }
                else if (ExtendedModSettings.ENTITY_DETECT_TYPE.equalsIgnoreCase("reset"))
                {
                    if (list instanceof EntityLivingBase)
                    {
                        list.setGlowing(((EntityLivingBase)list).isPotionActive(MobEffects.GLOWING));
                    }
                    list.setGlowing(false);
                }
                else
                {
                    for (String name : GameInfoHelper.INSTANCE.getPlayerInfoListClient())
                    {
                        if (ExtendedModSettings.ENTITY_DETECT_TYPE.equalsIgnoreCase(name))
                        {
                            if (list instanceof EntityOtherPlayerMP && ((EntityOtherPlayerMP)list).getName().equalsIgnoreCase(ExtendedModSettings.ENTITY_DETECT_TYPE))
                            {
                                list.setGlowing(true);
                            }
                            else
                            {
                                list.setGlowing(false);
                            }
                        }
                    }
                }
            }
        }
    }
}