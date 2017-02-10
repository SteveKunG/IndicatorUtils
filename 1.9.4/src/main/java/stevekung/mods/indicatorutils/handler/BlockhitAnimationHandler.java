/*******************************************************************************
 *
 * Copyright 2016 Wasinthorn Suksri/SteveKunG - Indicator Utils
 *
 ******************************************************************************/

package stevekung.mods.indicatorutils.handler;

import javax.annotation.Nullable;

import org.lwjgl.util.glu.Project;

import com.google.common.base.Objects;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.storage.MapData;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import stevekung.mods.indicatorutils.config.ConfigManager;
import stevekung.mods.indicatorutils.utils.ReflectionUtils;

public class BlockhitAnimationHandler
{
    private EntityRenderer entityRenderer;
    private float farPlaneDistance;
    private float fovModifierHandPrev;
    private float fovModifierHand;

    private ItemStack itemStackMainHand;
    private ItemStack itemStackOffHand;
    private float equippedProgressMainHand;
    private float prevEquippedProgressMainHand;
    private float equippedProgressOffHand;
    private float prevEquippedProgressOffHand;

    private ItemRenderer itemRenderer;
    private RenderManager renderManager;
    private Minecraft mc;
    private GameSettings gameSettings;
    private boolean isOF = false;
    private boolean init = false;
    private KeyBinding zoom;

    @SubscribeEvent
    public void onRenderFirstHand(RenderHandEvent event)
    {
        this.init();
        this.initReflection();

        if (this.isZoomed())
        {
            event.setCanceled(true);
            return;
        }
        else
        {
            event.setCanceled(true);
            GlStateManager.clear(256);
            this.renderHand(event.getPartialTicks(), event.getRenderPass());
        }
    }

    private void renderHand(float partialTicks, int pass)
    {
        GlStateManager.matrixMode(5889);
        GlStateManager.loadIdentity();

        if (this.mc.gameSettings.anaglyph)
        {
            GlStateManager.translate(-(pass * 2 - 1) * 0.07F, 0.0F, 0.0F);
        }

        Project.gluPerspective(this.getFOVModifier(partialTicks, false), (float)this.mc.displayWidth / (float)this.mc.displayHeight, 0.05F, this.farPlaneDistance * 2.0F);
        GlStateManager.matrixMode(5888);
        GlStateManager.loadIdentity();

        if (this.mc.gameSettings.anaglyph)
        {
            GlStateManager.translate((pass * 2 - 1) * 0.1F, 0.0F, 0.0F);
        }

        GlStateManager.pushMatrix();
        this.hurtCameraEffect(partialTicks);

        if (this.mc.gameSettings.viewBobbing)
        {
            this.setupViewBobbing(partialTicks);
        }

        boolean flag = this.mc.getRenderViewEntity() instanceof EntityLivingBase && ((EntityLivingBase)this.mc.getRenderViewEntity()).isPlayerSleeping();

        if (this.mc.gameSettings.thirdPersonView == 0 && !flag && !this.mc.gameSettings.hideGUI && !this.mc.playerController.isSpectator())
        {
            this.entityRenderer.enableLightmap();

            if (ConfigManager.enableBlockhitAnimation)
            {
                this.renderItemInFirstPerson(partialTicks);
            }
            else
            {
                this.itemRenderer.renderItemInFirstPerson(partialTicks);
            }
            this.entityRenderer.disableLightmap();
        }

        GlStateManager.popMatrix();

        if (this.mc.gameSettings.thirdPersonView == 0 && !flag)
        {
            this.itemRenderer.renderOverlays(partialTicks);
            this.hurtCameraEffect(partialTicks);
        }

        if (this.mc.gameSettings.viewBobbing)
        {
            this.setupViewBobbing(partialTicks);
        }
    }

    private void renderItemInFirstPerson(float partialTicks)
    {
        AbstractClientPlayer abstractclientplayer = this.mc.thePlayer;
        float f = abstractclientplayer.getSwingProgress(partialTicks);
        EnumHand enumhand = Objects.firstNonNull(abstractclientplayer.swingingHand, EnumHand.MAIN_HAND);
        float f1 = abstractclientplayer.prevRotationPitch + (abstractclientplayer.rotationPitch - abstractclientplayer.prevRotationPitch) * partialTicks;
        float f2 = abstractclientplayer.prevRotationYaw + (abstractclientplayer.rotationYaw - abstractclientplayer.prevRotationYaw) * partialTicks;
        boolean flag = true;
        boolean flag1 = true;

        if (abstractclientplayer.isHandActive())
        {
            ItemStack itemstack = abstractclientplayer.getActiveItemStack();

            if (itemstack != null && itemstack.getItem() == Items.BOW)
            {
                EnumHand enumhand1 = abstractclientplayer.getActiveHand();
                flag = enumhand1 == EnumHand.MAIN_HAND;
                flag1 = !flag;
            }
        }

        this.rotateArroundXAndY(f1, f2);
        this.setLightmap();
        this.rotateArm(partialTicks);
        GlStateManager.enableRescaleNormal();

        if (flag)
        {
            float f3 = enumhand == EnumHand.MAIN_HAND ? f : 0;
            float f5 = 1.0F - (this.prevEquippedProgressMainHand + (this.equippedProgressMainHand - this.prevEquippedProgressMainHand) * partialTicks);
            this.renderItemInFirstPerson(abstractclientplayer, partialTicks, f1, EnumHand.MAIN_HAND, f3, this.itemStackMainHand, f5);
        }
        if (flag1)
        {
            float f4 = enumhand == EnumHand.OFF_HAND ? f : 0;
            float f6 = 1.0F - (this.prevEquippedProgressOffHand + (this.equippedProgressOffHand - this.prevEquippedProgressOffHand) * partialTicks);
            this.renderItemInFirstPerson(abstractclientplayer, partialTicks, f1, EnumHand.OFF_HAND, f4, this.itemStackOffHand, f6);
        }
        GlStateManager.disableRescaleNormal();
        RenderHelper.disableStandardItemLighting();
    }

    private void rotateArroundXAndY(float angle, float angleY)
    {
        GlStateManager.pushMatrix();
        GlStateManager.rotate(angle, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(angleY, 0.0F, 1.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.popMatrix();
    }

    private void setLightmap()
    {
        AbstractClientPlayer abstractclientplayer = this.mc.thePlayer;
        int i = this.mc.theWorld.getCombinedLight(new BlockPos(abstractclientplayer.posX, abstractclientplayer.posY + abstractclientplayer.getEyeHeight(), abstractclientplayer.posZ), 0);
        float f = i & 65535;
        float f1 = i >> 16;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, f, f1);
    }

    private void rotateArm(float p_187458_1_)
    {
        EntityPlayerSP entityplayersp = this.mc.thePlayer;
        float f = entityplayersp.prevRenderArmPitch + (entityplayersp.renderArmPitch - entityplayersp.prevRenderArmPitch) * p_187458_1_;
        float f1 = entityplayersp.prevRenderArmYaw + (entityplayersp.renderArmYaw - entityplayersp.prevRenderArmYaw) * p_187458_1_;
        GlStateManager.rotate((entityplayersp.rotationPitch - f) * 0.1F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate((entityplayersp.rotationYaw - f1) * 0.1F, 0.0F, 1.0F, 0.0F);
    }

    private void renderItemInFirstPerson(AbstractClientPlayer player, float partialTicks, float rotationPitch, EnumHand hand, float swingProgress, @Nullable ItemStack itemStack, float equipProgress)
    {
        boolean flag = hand == EnumHand.MAIN_HAND;
        EnumHandSide enumhandside = flag ? player.getPrimaryHand() : player.getPrimaryHand().opposite();
        GlStateManager.pushMatrix();

        if (itemStack == null)
        {
            if (flag && !player.isInvisible())
            {
                this.renderArmFirstPerson(equipProgress, swingProgress, enumhandside);
            }
        }
        else if (itemStack.getItem() instanceof ItemMap)
        {
            if (flag && this.itemStackOffHand == null)
            {
                this.renderMapFirstPerson(rotationPitch, equipProgress, swingProgress);
            }
            else
            {
                this.renderMapFirstPersonSide(equipProgress, enumhandside, swingProgress, itemStack);
            }
        }
        else
        {
            boolean flag1 = enumhandside == EnumHandSide.RIGHT;

            if (player.isHandActive() && player.getItemInUseCount() > 0 && player.getActiveHand() == hand)
            {
                int j = flag1 ? 1 : -1;
                float f = MathHelper.sin(swingProgress * swingProgress * (float)Math.PI);
                float f1 = MathHelper.sin(MathHelper.sqrt_float(swingProgress) * (float)Math.PI);

                switch (itemStack.getItemUseAction())
                {
                case NONE:
                    this.transformSideFirstPerson(enumhandside, equipProgress);
                    //////
                    GlStateManager.translate(0.0F, equipProgress * 0.6F, 0.0F);
                    GlStateManager.rotate(0.0F, 0.0F, 1.0F, 0.0F);
                    GlStateManager.rotate(f * 20.0F, 0.0F, 1.0F, 0.0F);
                    GlStateManager.rotate(f1 * 20.0F, 0.0F, 0.0F, 1.0F);
                    GlStateManager.rotate(f1 * -80.0F, 1.0F, 0.0F, 0.0F);
                    break;
                case EAT:
                case DRINK:
                    this.transformEatFirstPerson(partialTicks, enumhandside, itemStack);
                    this.transformSideFirstPerson(enumhandside, equipProgress);
                    //////
                    GlStateManager.translate(0.0F, equipProgress * 0.6F, 0.0F);
                    GlStateManager.rotate(0.0F, 0.0F, 1.0F, 0.0F);
                    GlStateManager.rotate(f * 20.0F, 0.0F, 1.0F, 0.0F);
                    GlStateManager.rotate(f1 * 20.0F, 0.0F, 0.0F, 1.0F);
                    GlStateManager.rotate(f1 * -80.0F, 1.0F, 0.0F, 0.0F);
                    break;
                case BLOCK:
                    this.transformSideFirstPerson(enumhandside, equipProgress);
                    //////
                    GlStateManager.translate(0.0F, equipProgress * 0.6F, 0.0F);
                    GlStateManager.rotate(0.0F, 0.0F, 1.0F, 0.0F);
                    GlStateManager.rotate(f * 20.0F, 0.0F, 1.0F, 0.0F);
                    GlStateManager.rotate(f1 * 20.0F, 0.0F, 0.0F, 1.0F);
                    GlStateManager.rotate(f1 * -80.0F, 1.0F, 0.0F, 0.0F);
                    break;
                case BOW:
                    this.transformSideFirstPerson(enumhandside, equipProgress);
                    /////
                    GlStateManager.translate(0.0F, equipProgress * 0.6F, 0.0F);
                    GlStateManager.rotate(0.0F, 0.0F, 1.0F, 0.0F);
                    GlStateManager.rotate(f * 20.0F, 0.0F, 1.0F, 0.0F);
                    GlStateManager.rotate(f1 * 20.0F, 0.0F, 0.0F, 1.0F);
                    GlStateManager.rotate(f1 * -80.0F, 1.0F, 0.0F, 0.0F);

                    GlStateManager.translate(j * -0.2785682F, 0.18344387F, 0.15731531F);
                    GlStateManager.rotate(-13.935F, 1.0F, 0.0F, 0.0F);
                    GlStateManager.rotate(j * 35.3F, 0.0F, 1.0F, 0.0F);
                    GlStateManager.rotate(j * -9.785F, 0.0F, 0.0F, 1.0F);
                    float f5 = itemStack.getMaxItemUseDuration() - (this.mc.thePlayer.getItemInUseCount() - partialTicks + 1.0F);
                    float f6 = f5 / 20.0F;
                    f6 = (f6 * f6 + f6 * 2.0F) / 3.0F;

                    if (f6 > 1.0F)
                    {
                        f6 = 1.0F;
                    }

                    if (f6 > 0.1F)
                    {
                        float f7 = MathHelper.sin((f5 - 0.1F) * 1.3F);
                        float f3 = f6 - 0.1F;
                        float f4 = f7 * f3;
                        GlStateManager.translate(f4 * 0.0F, f4 * 0.004F, f4 * 0.0F);
                    }
                    GlStateManager.translate(f6 * 0.0F, f6 * 0.0F, f6 * 0.04F);
                    GlStateManager.scale(1.0F, 1.0F, 1.0F + f6 * 0.2F);
                    GlStateManager.rotate(j * 45.0F, 0.0F, -1.0F, 0.0F);
                }
            }
            else
            {
                float f = -0.4F * MathHelper.sin(MathHelper.sqrt_float(swingProgress) * (float)Math.PI);
                float f1 = 0.2F * MathHelper.sin(MathHelper.sqrt_float(swingProgress) * ((float)Math.PI * 2F));
                float f2 = -0.2F * MathHelper.sin(swingProgress * (float)Math.PI);
                int i = flag1 ? 1 : -1;
                GlStateManager.translate(i * f, f1, f2);
                this.transformSideFirstPerson(enumhandside, equipProgress);
                this.transformFirstPerson(enumhandside, swingProgress);
            }
            this.itemRenderer.renderItemSide(player, itemStack, flag1 ? ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND : ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND, !flag1);
        }
        GlStateManager.popMatrix();
    }

    private void renderArmFirstPerson(float p_187456_1_, float swingProgress, EnumHandSide p_187456_3_)
    {
        boolean flag = p_187456_3_ != EnumHandSide.LEFT;
        float f = flag ? 1.0F : -1.0F;
        float f1 = MathHelper.sqrt_float(swingProgress);
        float f2 = -0.3F * MathHelper.sin(f1 * (float)Math.PI);
        float f3 = 0.4F * MathHelper.sin(f1 * ((float)Math.PI * 2F));
        float f4 = -0.4F * MathHelper.sin(swingProgress * (float)Math.PI);
        GlStateManager.translate(f * (f2 + 0.64000005F), f3 + -0.6F + p_187456_1_ * -0.6F, f4 + -0.71999997F);
        GlStateManager.rotate(f * 45.0F, 0.0F, 1.0F, 0.0F);
        float f5 = MathHelper.sin(swingProgress * swingProgress * (float)Math.PI);
        float f6 = MathHelper.sin(f1 * (float)Math.PI);
        GlStateManager.rotate(f * f6 * 70.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(f * f5 * -20.0F, 0.0F, 0.0F, 1.0F);
        AbstractClientPlayer abstractclientplayer = this.mc.thePlayer;
        this.mc.getTextureManager().bindTexture(abstractclientplayer.getLocationSkin());
        GlStateManager.translate(f * -1.0F, 3.6F, 3.5F);
        GlStateManager.rotate(f * 120.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotate(200.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(f * -135.0F, 0.0F, 1.0F, 0.0F);

        GlStateManager.translate(f * 5.6F, 0.0F, 0.0F);
        RenderPlayer renderplayer = (RenderPlayer)this.renderManager.getEntityRenderObject(abstractclientplayer);
        GlStateManager.disableCull();

        if (flag)
        {
            renderplayer.renderRightArm(abstractclientplayer);
        }
        else
        {
            renderplayer.renderLeftArm(abstractclientplayer);
        }
        GlStateManager.enableCull();
    }

    private void transformSideFirstPerson(EnumHandSide p_187459_1_, float p_187459_2_)
    {
        int i = p_187459_1_ == EnumHandSide.RIGHT ? 1 : -1;
        GlStateManager.translate(i * 0.56F, -0.52F + p_187459_2_ * -0.6F, -0.72F);
    }

    private void transformEatFirstPerson(float p_187454_1_, EnumHandSide p_187454_2_, ItemStack p_187454_3_)
    {
        float f = this.mc.thePlayer.getItemInUseCount() - p_187454_1_ + 1.0F;
        float f1 = f / p_187454_3_.getMaxItemUseDuration();

        if (f1 < 0.8F)
        {
            float f2 = MathHelper.abs(MathHelper.cos(f / 4.0F * (float)Math.PI) * 0.1F);
            GlStateManager.translate(0.0F, f2, 0.0F);
        }

        float f3 = 1.0F - (float)Math.pow(f1, 27.0D);
        int i = p_187454_2_ == EnumHandSide.RIGHT ? 1 : -1;
        GlStateManager.translate(f3 * 0.6F * i, f3 * -0.5F, f3 * 0.0F);
        GlStateManager.rotate(i * f3 * 90.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(f3 * 10.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(i * f3 * 30.0F, 0.0F, 0.0F, 1.0F);
    }

    private void transformFirstPerson(EnumHandSide hand, float swingProgress)
    {
        int i = hand == EnumHandSide.RIGHT ? 1 : -1;
        float f = MathHelper.sin(swingProgress * swingProgress * (float)Math.PI);
        GlStateManager.rotate(i * (45.0F + f * -20.0F), 0.0F, 1.0F, 0.0F);
        float f1 = MathHelper.sin(MathHelper.sqrt_float(swingProgress) * (float)Math.PI);
        GlStateManager.rotate(i * f1 * -20.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotate(f1 * -80.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(i * -45.0F, 0.0F, 1.0F, 0.0F);
    }

    private void renderMapFirstPerson(float p_187463_1_, float p_187463_2_, float p_187463_3_)
    {
        float f = MathHelper.sqrt_float(p_187463_3_);
        float f1 = -0.2F * MathHelper.sin(p_187463_3_ * (float)Math.PI);
        float f2 = -0.4F * MathHelper.sin(f * (float)Math.PI);
        GlStateManager.translate(0.0F, -f1 / 2.0F, f2);
        float f3 = this.getMapAngleFromPitch(p_187463_1_);
        GlStateManager.translate(0.0F, 0.04F + p_187463_2_ * -1.2F + f3 * -0.5F, -0.72F);
        GlStateManager.rotate(f3 * -85.0F, 1.0F, 0.0F, 0.0F);
        this.renderArms();
        float f4 = MathHelper.sin(f * (float)Math.PI);
        GlStateManager.rotate(f4 * 20.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.scale(2.0F, 2.0F, 2.0F);
        this.renderMapFirstPerson(this.itemStackMainHand);
    }

    private float getMapAngleFromPitch(float pitch)
    {
        float f = 1.0F - pitch / 45.0F + 0.1F;
        f = MathHelper.clamp_float(f, 0.0F, 1.0F);
        f = -MathHelper.cos(f * (float)Math.PI) * 0.5F + 0.5F;
        return f;
    }

    private void renderArms()
    {
        if (!this.mc.thePlayer.isInvisible())
        {
            GlStateManager.disableCull();
            GlStateManager.pushMatrix();
            GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
            this.renderArm(EnumHandSide.RIGHT);
            this.renderArm(EnumHandSide.LEFT);
            GlStateManager.popMatrix();
            GlStateManager.enableCull();
        }
    }

    private void renderArm(EnumHandSide p_187455_1_)
    {
        this.mc.getTextureManager().bindTexture(this.mc.thePlayer.getLocationSkin());
        Render<AbstractClientPlayer> render = this.renderManager.<AbstractClientPlayer>getEntityRenderObject(this.mc.thePlayer);
        RenderPlayer renderplayer = (RenderPlayer)render;
        GlStateManager.pushMatrix();
        float f = p_187455_1_ == EnumHandSide.RIGHT ? 1.0F : -1.0F;
        GlStateManager.rotate(92.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(45.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(f * -41.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.translate(f * 0.3F, -1.1F, 0.45F);

        if (p_187455_1_ == EnumHandSide.RIGHT)
        {
            renderplayer.renderRightArm(this.mc.thePlayer);
        }
        else
        {
            renderplayer.renderLeftArm(this.mc.thePlayer);
        }
        GlStateManager.popMatrix();
    }

    private void renderMapFirstPerson(ItemStack stack)
    {
        GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.scale(0.38F, 0.38F, 0.38F);
        GlStateManager.disableLighting();
        this.mc.getTextureManager().bindTexture(new ResourceLocation("textures/map/map_background.png"));
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer vertexbuffer = tessellator.getBuffer();
        GlStateManager.translate(-0.5F, -0.5F, 0.0F);
        GlStateManager.scale(0.0078125F, 0.0078125F, 0.0078125F);
        vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
        vertexbuffer.pos(-7.0D, 135.0D, 0.0D).tex(0.0D, 1.0D).endVertex();
        vertexbuffer.pos(135.0D, 135.0D, 0.0D).tex(1.0D, 1.0D).endVertex();
        vertexbuffer.pos(135.0D, -7.0D, 0.0D).tex(1.0D, 0.0D).endVertex();
        vertexbuffer.pos(-7.0D, -7.0D, 0.0D).tex(0.0D, 0.0D).endVertex();
        tessellator.draw();
        MapData mapdata = Items.FILLED_MAP.getMapData(stack, this.mc.theWorld);

        if (mapdata != null)
        {
            this.mc.entityRenderer.getMapItemRenderer().renderMap(mapdata, false);
        }
        GlStateManager.enableLighting();
    }

    private void renderMapFirstPersonSide(float p_187465_1_, EnumHandSide p_187465_2_, float p_187465_3_, ItemStack p_187465_4_)
    {
        float f = p_187465_2_ == EnumHandSide.RIGHT ? 1.0F : -1.0F;
        GlStateManager.translate(f * 0.125F, -0.125F, 0.0F);

        if (!this.mc.thePlayer.isInvisible())
        {
            GlStateManager.pushMatrix();
            GlStateManager.rotate(f * 10.0F, 0.0F, 0.0F, 1.0F);
            this.renderArmFirstPerson(p_187465_1_, p_187465_3_, p_187465_2_);
            GlStateManager.popMatrix();
        }

        GlStateManager.pushMatrix();
        GlStateManager.translate(f * 0.51F, -0.08F + p_187465_1_ * -1.2F, -0.75F);
        float f1 = MathHelper.sqrt_float(p_187465_3_);
        float f2 = MathHelper.sin(f1 * (float)Math.PI);
        float f3 = -0.5F * f2;
        float f4 = 0.4F * MathHelper.sin(f1 * ((float)Math.PI * 2F));
        float f5 = -0.3F * MathHelper.sin(p_187465_3_ * (float)Math.PI);
        GlStateManager.translate(f * f3, f4 - 0.3F * f2, f5);
        GlStateManager.rotate(f2 * -45.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(f * f2 * -30.0F, 0.0F, 1.0F, 0.0F);
        this.renderMapFirstPerson(p_187465_4_);
        GlStateManager.popMatrix();
    }

    private float getFOVModifier(float partialTicks, boolean useFOVSetting)
    {
        Entity entity = this.mc.getRenderViewEntity();
        float f = 70.0F;

        if (useFOVSetting)
        {
            f = this.mc.gameSettings.fovSetting;
            f = f * (this.fovModifierHandPrev + (this.fovModifierHand - this.fovModifierHandPrev) * partialTicks);
        }

        if (entity instanceof EntityLivingBase && ((EntityLivingBase)entity).getHealth() <= 0.0F)
        {
            float f1 = ((EntityLivingBase)entity).deathTime + partialTicks;
            f /= (1.0F - 500.0F / (f1 + 500.0F)) * 2.0F + 1.0F;
        }

        IBlockState iblockstate = ActiveRenderInfo.getBlockStateAtEntityViewpoint(this.mc.theWorld, entity, partialTicks);

        if (iblockstate.getMaterial() == Material.WATER)
        {
            f = f * 60.0F / 70.0F;
        }
        return f;
    }

    private void hurtCameraEffect(float partialTicks)
    {
        if (this.mc.getRenderViewEntity() instanceof EntityLivingBase)
        {
            EntityLivingBase entitylivingbase = (EntityLivingBase)this.mc.getRenderViewEntity();
            float f = entitylivingbase.hurtTime - partialTicks;

            if (entitylivingbase.getHealth() <= 0.0F)
            {
                float f1 = entitylivingbase.deathTime + partialTicks;
                GlStateManager.rotate(40.0F - 8000.0F / (f1 + 200.0F), 0.0F, 0.0F, 1.0F);
            }

            if (f < 0.0F)
            {
                return;
            }
            f = f / entitylivingbase.maxHurtTime;
            f = MathHelper.sin(f * f * f * f * (float)Math.PI);
            float f2 = entitylivingbase.attackedAtYaw;
            GlStateManager.rotate(-f2, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(-f * 14.0F, 0.0F, 0.0F, 1.0F);
            GlStateManager.rotate(f2, 0.0F, 1.0F, 0.0F);
        }
    }

    private void setupViewBobbing(float partialTicks)
    {
        if (this.mc.getRenderViewEntity() instanceof EntityPlayer)
        {
            EntityPlayer entityplayer = (EntityPlayer)this.mc.getRenderViewEntity();
            float f = entityplayer.distanceWalkedModified - entityplayer.prevDistanceWalkedModified;
            float f1 = -(entityplayer.distanceWalkedModified + f * partialTicks);
            float f2 = entityplayer.prevCameraYaw + (entityplayer.cameraYaw - entityplayer.prevCameraYaw) * partialTicks;
            float f3 = entityplayer.prevCameraPitch + (entityplayer.cameraPitch - entityplayer.prevCameraPitch) * partialTicks;
            GlStateManager.translate(MathHelper.sin(f1 * (float)Math.PI) * f2 * 0.5F, -Math.abs(MathHelper.cos(f1 * (float)Math.PI) * f2), 0.0F);
            GlStateManager.rotate(MathHelper.sin(f1 * (float)Math.PI) * f2 * 3.0F, 0.0F, 0.0F, 1.0F);
            GlStateManager.rotate(Math.abs(MathHelper.cos(f1 * (float)Math.PI - 0.2F) * f2) * 5.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(f3, 1.0F, 0.0F, 0.0F);
        }
    }

    private void initReflection()
    {
        this.itemStackMainHand = (ItemStack)ReflectionUtils.get("itemStackMainHand", "field_187467_d", ItemRenderer.class, this.itemRenderer);
        this.itemStackOffHand = (ItemStack)ReflectionUtils.get("itemStackOffHand", "field_187468_e", ItemRenderer.class, this.itemRenderer);
        this.equippedProgressMainHand = ((Float)ReflectionUtils.get("equippedProgressMainHand", "field_187469_f", ItemRenderer.class, this.itemRenderer)).floatValue();
        this.prevEquippedProgressMainHand = ((Float)ReflectionUtils.get("prevEquippedProgressMainHand", "field_187470_g", ItemRenderer.class, this.itemRenderer)).floatValue();
        this.equippedProgressOffHand = ((Float)ReflectionUtils.get("equippedProgressOffHand", "field_187471_h", ItemRenderer.class, this.itemRenderer)).floatValue();
        this.prevEquippedProgressOffHand = ((Float)ReflectionUtils.get("prevEquippedProgressOffHand", "field_187472_i", ItemRenderer.class, this.itemRenderer)).floatValue();
        this.fovModifierHandPrev = ((Float)ReflectionUtils.get("fovModifierHandPrev", "field_78506_S", EntityRenderer.class, this.entityRenderer)).floatValue();
        this.fovModifierHand = ((Float)ReflectionUtils.get("fovModifierHand", "field_78507_R", EntityRenderer.class, this.entityRenderer)).floatValue();
    }

    private void init()
    {
        if (!this.init)
        {
            this.mc = Minecraft.getMinecraft();
            this.itemRenderer = this.mc.getItemRenderer();
            this.renderManager = this.mc.getRenderManager();
            this.gameSettings = this.mc.gameSettings;
            this.entityRenderer = this.mc.entityRenderer;
            KeyBinding[] k = this.gameSettings.keyBindings;

            for (KeyBinding key : k)
            {
                if (key.getKeyDescription().contains("zoom"))
                {
                    this.isOF = true;
                    this.zoom = key;
                }
            }
            this.init = true;
        }
        this.farPlaneDistance = this.mc.gameSettings.renderDistanceChunks * 16;
    }

    private boolean isZoomed()
    {
        if (this.isOF && (this.zoom.isKeyDown() || this.zoom.isPressed()))
        {
            return true;
        }
        return false;
    }
}