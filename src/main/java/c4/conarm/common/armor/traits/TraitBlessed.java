package c4.conarm.common.armor.traits;

import c4.conarm.common.armor.utils.ArmorHelper;
import c4.conarm.lib.traits.AbstractArmorTrait;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class TraitBlessed extends AbstractArmorTrait {

    private static final float MODIFIER = 0.2F;

    public TraitBlessed() {
        super("blessed", 0xffffff);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onLivingHurt(LivingHurtEvent evt) {

        if (evt.getEntityLiving() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) evt.getEntityLiving();
            if (evt.getSource().getImmediateSource() instanceof EntityLivingBase) {
                EntityLivingBase entity = (EntityLivingBase) evt.getSource().getImmediateSource();
                if (entity.isEntityUndead()) {
                    int level = (int) ArmorHelper.getArmorAbilityLevel(player, getModifierIdentifier());
                    if (level > 0) {
                        entity.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, 100 * level, level - 1));
                    }
                }
            }
        }
    }
}