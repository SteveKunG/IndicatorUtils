/*******************************************************************************
 *
 * Copyright 2016 Wasinthorn Suksri/SteveKunG - Indicator Utils
 *
 ******************************************************************************/

package stevekung.mods.indicatorutils.renderer;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import stevekung.mods.indicatorutils.utils.IULog;

@SideOnly(Side.CLIENT)
public class RenderPlayerMOD extends RenderPlayer
{
    public RenderPlayerMOD()
    {
        super();
        this.mainModel = new ModelBipedMOD(0.0F);
        this.modelBipedMain = (ModelBipedMOD)this.mainModel;
        this.modelArmorChestplate = new ModelBipedMOD(1.0F);
        this.modelArmor = new ModelBipedMOD(0.5F);
        IULog.info("New RenderPlayerMOD.class successfully loaded");
    }

    @Override
    protected int shouldRenderPass(AbstractClientPlayer p_77032_1_, int p_77032_2_, float p_77032_3_)
    {
        ItemStack itemstack = p_77032_1_.inventory.armorItemInSlot(3 - p_77032_2_);
        net.minecraftforge.client.event.RenderPlayerEvent.SetArmorModel event = new net.minecraftforge.client.event.RenderPlayerEvent.SetArmorModel(p_77032_1_, this, 3 - p_77032_2_, p_77032_3_, itemstack);
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event);

        if (event.result != -1)
        {
            return event.result;
        }

        if (itemstack != null)
        {
            Item item = itemstack.getItem();

            if (item instanceof ItemArmor)
            {
                ItemArmor itemarmor = (ItemArmor)item;
                this.bindTexture(RenderBiped.getArmorResource(p_77032_1_, itemstack, p_77032_2_, null));
                ModelBiped modelbiped = p_77032_2_ == 2 ? this.modelArmor : this.modelArmorChestplate;
                modelbiped.bipedHead.showModel = p_77032_2_ == 0;
                modelbiped.bipedHeadwear.showModel = p_77032_2_ == 0;
                modelbiped.bipedBody.showModel = p_77032_2_ == 1 || p_77032_2_ == 2;
                modelbiped.bipedRightArm.showModel = p_77032_2_ == 1;
                modelbiped.bipedLeftArm.showModel = p_77032_2_ == 1;
                modelbiped.bipedRightLeg.showModel = p_77032_2_ == 2 || p_77032_2_ == 3;
                modelbiped.bipedLeftLeg.showModel = p_77032_2_ == 2 || p_77032_2_ == 3;
                modelbiped = net.minecraftforge.client.ForgeHooksClient.getArmorModel(p_77032_1_, itemstack, p_77032_2_, modelbiped);
                this.setRenderPassModel(modelbiped);
                modelbiped.onGround = this.mainModel.onGround;
                modelbiped.isRiding = this.mainModel.isRiding;
                modelbiped.isChild = this.mainModel.isChild;

                //Move outside if to allow for more then just CLOTH
                int j = itemarmor.getColor(itemstack);

                if (j != -1)
                {
                    float f1 = (j >> 16 & 255) / 255.0F;
                    float f2 = (j >> 8 & 255) / 255.0F;
                    float f3 = (j & 255) / 255.0F;
                    GL11.glColor4f(f1, f2, f3, 0.75F);

                    if (itemstack.isItemEnchanted())
                    {
                        return 1;
                    }
                    GL11.glColor4f(f1, f2, f3, 1.0F);
                    return 16;
                }

                GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.75F);

                if (itemstack.isItemEnchanted())
                {
                    return 1;
                }
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                return 1;
            }
        }
        return -1;
    }

    @Override
    public void renderFirstPersonArm(EntityPlayer p_82441_1_)
    {
        float f = 1.0F;
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(770, 771);
        GL11.glColor3f(f, f, f);
        this.modelBipedMain.onGround = 0.0F;
        this.modelBipedMain.setRotationAngles(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F, p_82441_1_);
        this.modelBipedMain.bipedRightArm.render(0.0625F);
        GL11.glDisable(GL11.GL_BLEND);
    }
}