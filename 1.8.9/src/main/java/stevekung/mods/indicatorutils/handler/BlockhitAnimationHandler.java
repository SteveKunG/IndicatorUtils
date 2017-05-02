/*******************************************************************************
 *
 * Copyright 2016 Wasinthorn Suksri/SteveKunG - Indicator Utils
 *
 ******************************************************************************/

package stevekung.mods.indicatorutils.handler;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Project;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.world.storage.MapData;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import stevekung.mods.indicatorutils.config.ConfigManager;

@SuppressWarnings("deprecation")
public class BlockhitAnimationHandler
{
    private EntityRenderer entityRenderer;
    private float farPlaneDistance;
    private float fovModifierHandPrev;
    private float fovModifierHand;
    private float prevEquippedProgress;
    private float equippedProgress;
    private ItemRenderer itemRenderer;
    private ItemStack itemToRender;
    private RenderManager renderManager;
    private Minecraft mc;
    private GameSettings gameSettings;
    private KeyBinding zoom;

    public BlockhitAnimationHandler()
    {
        this.mc = Minecraft.getMinecraft();
        this.itemRenderer = this.mc.getItemRenderer();
        this.renderManager = this.mc.getRenderManager();
        this.gameSettings = this.mc.gameSettings;
        this.entityRenderer = this.mc.entityRenderer;

        for (KeyBinding key : this.gameSettings.keyBindings)
        {
            if (key.getKeyDescription().contains("of.key.zoom"))
            {
                this.zoom = key;
            }
        }
        this.farPlaneDistance = this.mc.gameSettings.renderDistanceChunks * 16;
    }

    @SubscribeEvent
    public void onRenderFirstHand(RenderHandEvent event)
    {
        this.initReflection();
        event.setCanceled(true);

        if (!this.isZoomed())
        {
            GlStateManager.clear(256);
            this.renderHand(event.partialTicks, event.renderPass);
            this.renderWorldDirections(event.partialTicks);
        }
    }


    private void renderHand(float partialTicks, int xOffset)
    {
        GlStateManager.matrixMode(5889);
        GlStateManager.loadIdentity();
        float f = 0.07F;

        if (this.mc.gameSettings.anaglyph)
        {
            GlStateManager.translate(-(xOffset * 2 - 1) * f, 0.0F, 0.0F);
        }

        Project.gluPerspective(this.getFOVModifier(partialTicks, false), (float)this.mc.displayWidth / (float)this.mc.displayHeight, 0.05F, this.farPlaneDistance * 2.0F);
        GlStateManager.matrixMode(5888);
        GlStateManager.loadIdentity();

        if (this.mc.gameSettings.anaglyph)
        {
            GlStateManager.translate((xOffset * 2 - 1) * 0.1F, 0.0F, 0.0F);
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

    private void renderWorldDirections(float partialTicks)
    {
        if (this.mc.gameSettings.showDebugInfo && !this.mc.gameSettings.hideGUI && !this.mc.thePlayer.hasReducedDebug() && !this.mc.gameSettings.reducedDebugInfo)
        {
            Entity entity = this.mc.getRenderViewEntity();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GL11.glLineWidth(1.0F);
            GlStateManager.disableTexture2D();
            GlStateManager.depthMask(false);
            GlStateManager.pushMatrix();
            GlStateManager.matrixMode(5888);
            GlStateManager.loadIdentity();
            this.orientCamera(partialTicks);
            GlStateManager.translate(0.0F, entity.getEyeHeight(), 0.0F);
            RenderGlobal.drawOutlinedBoundingBox(new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.005D, 1.0E-4D, 1.0E-4D), 255, 0, 0, 255);
            RenderGlobal.drawOutlinedBoundingBox(new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0E-4D, 1.0E-4D, 0.005D), 0, 0, 255, 255);
            RenderGlobal.drawOutlinedBoundingBox(new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0E-4D, 0.0033D, 1.0E-4D), 0, 255, 0, 255);
            GlStateManager.popMatrix();
            GlStateManager.depthMask(true);
            GlStateManager.enableTexture2D();
            GlStateManager.disableBlend();
        }
    }

    private void orientCamera(float p_78467_1_)
    {
        Entity entity = this.mc.getRenderViewEntity();
        float f1 = entity.getEyeHeight();
        double d0 = entity.prevPosX + (entity.posX - entity.prevPosX) * p_78467_1_;
        double d1 = entity.prevPosY + (entity.posY - entity.prevPosY) * p_78467_1_ + f1;
        double d2 = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * p_78467_1_;

        if (entity instanceof EntityLivingBase && ((EntityLivingBase)entity).isPlayerSleeping())
        {
            f1 = (float)(f1 + 1.0D);
            GlStateManager.translate(0.0F, 0.3F, 0.0F);

            if (!this.gameSettings.debugCamEnable)
            {
                BlockPos blockpos = new BlockPos(entity);
                IBlockState iblockstate = this.mc.theWorld.getBlockState(blockpos);
                ForgeHooksClient.orientBedCamera(this.mc.theWorld, blockpos, iblockstate, entity);
                GlStateManager.rotate(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * p_78467_1_ + 180.0F, 0.0F, -1.0F, 0.0F);
                GlStateManager.rotate(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * p_78467_1_, -1.0F, 0.0F, 0.0F);
            }
        }
        else if (this.gameSettings.thirdPersonView > 0)
        {
            double d3 = 4.0D;

            if (this.gameSettings.debugCamEnable)
            {
                GlStateManager.translate(0.0F, 0.0F, (float)-d3);
            }
            else
            {
                float f2 = entity.rotationYaw;
                float f3 = entity.rotationPitch;

                if (this.gameSettings.thirdPersonView == 2)
                {
                    f3 += 180.0F;
                }

                double d4 = -MathHelper.sin(f2 / 180.0F * 3.1415927F) * MathHelper.cos(f3 / 180.0F * 3.1415927F) * d3;
                double d5 = MathHelper.cos(f2 / 180.0F * 3.1415927F) * MathHelper.cos(f3 / 180.0F * 3.1415927F) * d3;
                double d6 = -MathHelper.sin(f3 / 180.0F * 3.1415927F) * d3;

                for (int i = 0; i < 8; i++)
                {
                    float f4 = (i & 0x1) * 2 - 1;
                    float f5 = (i >> 1 & 0x1) * 2 - 1;
                    float f6 = (i >> 2 & 0x1) * 2 - 1;
                    f4 *= 0.1F;
                    f5 *= 0.1F;
                    f6 *= 0.1F;
                    MovingObjectPosition movingobjectposition = this.mc.theWorld.rayTraceBlocks(new Vec3(d0 + f4, d1 + f5, d2 + f6), new Vec3(d0 - d4 + f4 + f6, d1 - d6 + f5, d2 - d5 + f6));

                    if (movingobjectposition != null)
                    {
                        double d7 = movingobjectposition.hitVec.distanceTo(new Vec3(d0, d1, d2));

                        if (d7 < d3)
                        {
                            d3 = d7;
                        }
                    }
                }
                if (this.gameSettings.thirdPersonView == 2)
                {
                    GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
                }
                GlStateManager.rotate(entity.rotationPitch - f3, 1.0F, 0.0F, 0.0F);
                GlStateManager.rotate(entity.rotationYaw - f2, 0.0F, 1.0F, 0.0F);
                GlStateManager.translate(0.0F, 0.0F, (float)-d3);
                GlStateManager.rotate(f2 - entity.rotationYaw, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotate(f3 - entity.rotationPitch, 1.0F, 0.0F, 0.0F);
            }
        }
        else
        {
            GlStateManager.translate(0.0F, 0.0F, -0.1F);
        }
        if (!this.gameSettings.debugCamEnable)
        {
            GlStateManager.rotate(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * p_78467_1_, 1.0F, 0.0F, 0.0F);

            if (entity instanceof EntityAnimal)
            {
                EntityAnimal entityanimal = (EntityAnimal)entity;
                GlStateManager.rotate(entityanimal.prevRotationYawHead + (entityanimal.rotationYawHead - entityanimal.prevRotationYawHead) * p_78467_1_ + 180.0F, 0.0F, 1.0F, 0.0F);
            }
            else
            {
                GlStateManager.rotate(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * p_78467_1_ + 180.0F, 0.0F, 1.0F, 0.0F);
            }
        }
        GlStateManager.translate(0.0F, -f1, 0.0F);
        d0 = entity.prevPosX + (entity.posX - entity.prevPosX) * p_78467_1_;
        d1 = entity.prevPosY + (entity.posY - entity.prevPosY) * p_78467_1_ + f1;
        d2 = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * p_78467_1_;
        this.entityRenderer.cloudFog = this.mc.renderGlobal.hasCloudFog(d0, d1, d2, p_78467_1_);
    }

    private void renderItemInFirstPerson(float partialTicks)
    {
        float f = 1.0F - (this.prevEquippedProgress + (this.equippedProgress - this.prevEquippedProgress) * partialTicks);
        AbstractClientPlayer abstractclientplayer = this.mc.thePlayer;
        float f1 = abstractclientplayer.getSwingProgress(partialTicks);
        float f2 = abstractclientplayer.prevRotationPitch + (abstractclientplayer.rotationPitch - abstractclientplayer.prevRotationPitch) * partialTicks;
        float f3 = abstractclientplayer.prevRotationYaw + (abstractclientplayer.rotationYaw - abstractclientplayer.prevRotationYaw) * partialTicks;
        this.func_178101_a(f2, f3);
        this.func_178109_a(abstractclientplayer);
        this.func_178110_a((EntityPlayerSP)abstractclientplayer, partialTicks);
        GlStateManager.enableRescaleNormal();
        GlStateManager.pushMatrix();

        if (this.itemToRender != null)
        {
            if (this.itemToRender.getItem() instanceof ItemMap)
            {
                this.renderItemMap(abstractclientplayer, f2, f, f1);
            }
            else if (abstractclientplayer.getItemInUseCount() > 0)
            {
                EnumAction enumaction = this.itemToRender.getItemUseAction();

                switch (enumaction)
                {
                case NONE:
                    this.transformFirstPersonItem(f, f1);
                    break;
                case EAT:
                case DRINK:
                    this.func_178104_a(abstractclientplayer, partialTicks);
                    this.transformFirstPersonItem(f, f1);
                    break;
                case BLOCK:
                    this.transformFirstPersonItem(f, f1);
                    this.func_178103_d();
                    break;
                case BOW:
                    this.transformFirstPersonItem(f, f1);
                    this.func_178098_a(partialTicks, abstractclientplayer);
                }
            }
            else
            {
                this.func_178105_d(f1);
                this.transformFirstPersonItem(f, f1);
            }
            this.itemRenderer.renderItem(abstractclientplayer, this.itemToRender, TransformType.FIRST_PERSON);
        }
        else if (!abstractclientplayer.isInvisible())
        {
            this.func_178095_a(abstractclientplayer, f, f1);
        }
        GlStateManager.popMatrix();
        GlStateManager.disableRescaleNormal();
        RenderHelper.disableStandardItemLighting();
    }

    private void func_178098_a(float p_178098_1_, AbstractClientPlayer clientPlayer)
    {
        GlStateManager.rotate(-18.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotate(-12.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(-8.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.translate(-0.9F, 0.2F, 0.0F);
        float f = this.itemToRender.getMaxItemUseDuration() - (clientPlayer.getItemInUseCount() - p_178098_1_ + 1.0F);
        float f1 = f / 20.0F;
        f1 = (f1 * f1 + f1 * 2.0F) / 3.0F;

        if (f1 > 1.0F)
        {
            f1 = 1.0F;
        }
        if (f1 > 0.1F)
        {
            float f2 = MathHelper.sin((f - 0.1F) * 1.3F);
            float f3 = f1 - 0.1F;
            float f4 = f2 * f3;
            GlStateManager.translate(f4 * 0.0F, f4 * 0.01F, f4 * 0.0F);
        }
        GlStateManager.translate(f1 * 0.0F, f1 * 0.0F, f1 * 0.1F);
        GlStateManager.scale(1.0F, 1.0F, 1.0F + f1 * 0.2F);
    }

    private float getFOVModifier(float partialTicks, boolean p_78481_2_)
    {
        Entity entity = this.mc.getRenderViewEntity();
        float f = 70.0F;

        if (p_78481_2_)
        {
            f = this.mc.gameSettings.fovSetting;
            f = f * (this.fovModifierHandPrev + (this.fovModifierHand - this.fovModifierHandPrev) * partialTicks);
        }

        if (entity instanceof EntityLivingBase && ((EntityLivingBase)entity).getHealth() <= 0.0F)
        {
            float f1 = ((EntityLivingBase)entity).deathTime + partialTicks;
            f /= (1.0F - 500.0F / (f1 + 500.0F)) * 2.0F + 1.0F;
        }

        Block block = ActiveRenderInfo.getBlockAtEntityViewpoint(this.mc.theWorld, entity, partialTicks);

        if (block.getMaterial() == Material.water)
        {
            f = f * 60.0F / 70.0F;
        }
        return ForgeHooksClient.getFOVModifier(this.entityRenderer, entity, block, partialTicks, f);
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

    private float func_178100_c(float p_178100_1_)
    {
        float f1 = 1.0F - p_178100_1_ / 45.0F + 0.1F;
        f1 = MathHelper.clamp_float(f1, 0.0F, 1.0F);
        f1 = -MathHelper.cos(f1 * 3.1415927F) * 0.5F + 0.5F;
        return f1;
    }

    private void func_178095_a(AbstractClientPlayer p_178095_1_, float p_178095_2_, float p_178095_3_)
    {
        float f = -0.3F * MathHelper.sin(MathHelper.sqrt_float(p_178095_3_) * (float)Math.PI);
        float f1 = 0.4F * MathHelper.sin(MathHelper.sqrt_float(p_178095_3_) * (float)Math.PI * 2.0F);
        float f2 = -0.4F * MathHelper.sin(p_178095_3_ * (float)Math.PI);
        GlStateManager.translate(f, f1, f2);
        GlStateManager.translate(0.64000005F, -0.6F, -0.71999997F);
        GlStateManager.translate(0.0F, p_178095_2_ * -0.6F, 0.0F);
        GlStateManager.rotate(45.0F, 0.0F, 1.0F, 0.0F);
        float f3 = MathHelper.sin(p_178095_3_ * p_178095_3_ * (float)Math.PI);
        float f4 = MathHelper.sin(MathHelper.sqrt_float(p_178095_3_) * (float)Math.PI);
        GlStateManager.rotate(f4 * 70.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(f3 * -20.0F, 0.0F, 0.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(p_178095_1_.getLocationSkin());
        GlStateManager.translate(-1.0F, 3.6F, 3.5F);
        GlStateManager.rotate(120.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotate(200.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(-135.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.scale(1.0F, 1.0F, 1.0F);
        GlStateManager.translate(5.6F, 0.0F, 0.0F);
        Render<AbstractClientPlayer> render = this.renderManager.<AbstractClientPlayer>getEntityRenderObject(this.mc.thePlayer);
        GlStateManager.disableCull();
        RenderPlayer renderplayer = (RenderPlayer)render;
        renderplayer.renderRightArm(this.mc.thePlayer);
        GlStateManager.enableCull();
    }

    private void func_178101_a(float angle, float p_178101_2_)
    {
        GlStateManager.pushMatrix();
        GlStateManager.rotate(angle, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(p_178101_2_, 0.0F, 1.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.popMatrix();
    }

    private void func_178109_a(AbstractClientPlayer player)
    {
        int i = this.mc.theWorld.getCombinedLight(new BlockPos(player.posX, player.posY + player.getEyeHeight(), player.posZ), 0);
        float f = i & 65535;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, f, i >> 16);
    }

    private void func_178110_a(EntityPlayerSP player, float partialTicks)
    {
        float f = player.prevRenderArmPitch + (player.renderArmPitch - player.prevRenderArmPitch) * partialTicks;
        float f1 = player.prevRenderArmYaw + (player.renderArmYaw - player.prevRenderArmYaw) * partialTicks;
        GlStateManager.rotate((player.rotationPitch - f) * 0.1F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate((player.rotationYaw - f1) * 0.1F, 0.0F, 1.0F, 0.0F);
    }

    private void func_178104_a(AbstractClientPlayer player, float partialTicks)
    {
        float f = player.getItemInUseCount() - partialTicks + 1.0F;
        float f1 = f / this.itemToRender.getMaxItemUseDuration();
        float f2 = MathHelper.abs(MathHelper.cos(f / 4.0F * (float)Math.PI) * 0.1F);

        if (f1 >= 0.8F)
        {
            f2 = 0.0F;
        }
        GlStateManager.translate(0.0F, f2, 0.0F);
        float f3 = 1.0F - (float)Math.pow(f1, 27.0D);
        GlStateManager.translate(f3 * 0.6F, f3 * -0.5F, f3 * 0.0F);
        GlStateManager.rotate(f3 * 90.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(f3 * 10.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(f3 * 30.0F, 0.0F, 0.0F, 1.0F);
    }

    private void func_178103_d()
    {
        GlStateManager.translate(-0.5F, 0.2F, 0.0F);
        GlStateManager.rotate(30.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(-80.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(60.0F, 0.0F, 1.0F, 0.0F);
    }

    private void func_178105_d(float p_178105_1_)
    {
        float f = -0.4F * MathHelper.sin(MathHelper.sqrt_float(p_178105_1_) * (float)Math.PI);
        float f1 = 0.2F * MathHelper.sin(MathHelper.sqrt_float(p_178105_1_) * (float)Math.PI * 2.0F);
        float f2 = -0.2F * MathHelper.sin(p_178105_1_ * (float)Math.PI);
        GlStateManager.translate(f, f1, f2);
    }

    private void transformFirstPersonItem(float equipProgress, float swingProgress)
    {
        GlStateManager.translate(0.56F, -0.52F, -0.71999997F);
        GlStateManager.translate(0.0F, equipProgress * -0.6F, 0.0F);
        GlStateManager.rotate(45.0F, 0.0F, 1.0F, 0.0F);
        float f = MathHelper.sin(swingProgress * swingProgress * (float)Math.PI);
        float f1 = MathHelper.sin(MathHelper.sqrt_float(swingProgress) * (float)Math.PI);
        GlStateManager.rotate(f * -20.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(f1 * -20.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotate(f1 * -80.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.scale(0.4F, 0.4F, 0.4F);
    }

    private void renderItemMap(AbstractClientPlayer clientPlayer, float p_178097_2_, float p_178097_3_, float p_178097_4_)
    {
        float f = -0.4F * MathHelper.sin(MathHelper.sqrt_float(p_178097_4_) * (float)Math.PI);
        float f1 = 0.2F * MathHelper.sin(MathHelper.sqrt_float(p_178097_4_) * (float)Math.PI * 2.0F);
        float f2 = -0.2F * MathHelper.sin(p_178097_4_ * (float)Math.PI);
        GlStateManager.translate(f, f1, f2);
        float f3 = this.func_178100_c(p_178097_2_);
        GlStateManager.translate(0.0F, 0.04F, -0.72F);
        GlStateManager.translate(0.0F, p_178097_3_ * -1.2F, 0.0F);
        GlStateManager.translate(0.0F, f3 * -0.5F, 0.0F);
        GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(f3 * -85.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotate(0.0F, 1.0F, 0.0F, 0.0F);
        this.renderPlayerArms(clientPlayer);
        float f4 = MathHelper.sin(p_178097_4_ * p_178097_4_ * (float)Math.PI);
        float f5 = MathHelper.sin(MathHelper.sqrt_float(p_178097_4_) * (float)Math.PI);
        GlStateManager.rotate(f4 * -20.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(f5 * -20.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotate(f5 * -80.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.scale(0.38F, 0.38F, 0.38F);
        GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotate(0.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.translate(-1.0F, -1.0F, 0.0F);
        GlStateManager.scale(0.015625F, 0.015625F, 0.015625F);
        this.mc.getTextureManager().bindTexture(new ResourceLocation("textures/map/map_background.png"));
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GL11.glNormal3f(0.0F, 0.0F, -1.0F);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(-7.0D, 135.0D, 0.0D).tex(0.0D, 1.0D).endVertex();
        worldrenderer.pos(135.0D, 135.0D, 0.0D).tex(1.0D, 1.0D).endVertex();
        worldrenderer.pos(135.0D, -7.0D, 0.0D).tex(1.0D, 0.0D).endVertex();
        worldrenderer.pos(-7.0D, -7.0D, 0.0D).tex(0.0D, 0.0D).endVertex();
        tessellator.draw();
        MapData mapdata = Items.filled_map.getMapData(this.itemToRender, this.mc.theWorld);

        if (mapdata != null)
        {
            this.mc.entityRenderer.getMapItemRenderer().renderMap(mapdata, false);
        }
    }

    private void renderPlayerArms(AbstractClientPlayer clientPlayer)
    {
        this.mc.getTextureManager().bindTexture(clientPlayer.getLocationSkin());
        Render<AbstractClientPlayer> render = this.renderManager.<AbstractClientPlayer>getEntityRenderObject(this.mc.thePlayer);
        RenderPlayer renderplayer = (RenderPlayer)render;

        if (!clientPlayer.isInvisible())
        {
            GlStateManager.disableCull();
            this.renderRightArm(renderplayer);
            this.renderLeftArm(renderplayer);
            GlStateManager.enableCull();
        }
    }

    private void renderRightArm(RenderPlayer renderPlayerIn)
    {
        GlStateManager.pushMatrix();
        GlStateManager.rotate(54.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(64.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(-62.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.translate(0.25F, -0.85F, 0.75F);
        renderPlayerIn.renderRightArm(this.mc.thePlayer);
        GlStateManager.popMatrix();
    }

    private void renderLeftArm(RenderPlayer renderPlayerIn)
    {
        GlStateManager.pushMatrix();
        GlStateManager.rotate(92.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(45.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(41.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.translate(-0.3F, -1.1F, 0.45F);
        renderPlayerIn.renderLeftArm(this.mc.thePlayer);
        GlStateManager.popMatrix();
    }

    private void initReflection()
    {
        this.itemToRender = this.itemRenderer.itemToRender;
        this.prevEquippedProgress = this.itemRenderer.prevEquippedProgress;
        this.equippedProgress = this.itemRenderer.equippedProgress;
        this.fovModifierHandPrev = this.entityRenderer.fovModifierHandPrev;
        this.fovModifierHand = this.entityRenderer.fovModifierHand;
    }

    private boolean isZoomed()
    {
        if (FMLClientHandler.instance().hasOptifine() && this.zoom.isKeyDown())
        {
            return true;
        }
        return false;
    }
}