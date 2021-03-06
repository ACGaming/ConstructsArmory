/*
 * Copyright (c) 2018-2020 C4
 *
 * This file is part of Construct's Armory, a mod made for Minecraft.
 *
 * Construct's Armory is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Construct's Armory is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Construct's Armory.  If not, see <https://www.gnu.org/licenses/>.
 */

package c4.conarm.integrations.tinkertoolleveling;

import c4.conarm.common.ConfigHandler;
import c4.conarm.lib.events.ArmoryEvent;
import c4.conarm.lib.modifiers.ArmorModifierTrait;
import c4.conarm.lib.tinkering.ArmorBuilder;
import c4.conarm.lib.tinkering.TinkersArmor;
import com.google.common.collect.Lists;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import slimeknights.tconstruct.library.client.CustomFontColor;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.modifiers.ModifierAspect;
import slimeknights.tconstruct.library.modifiers.TinkerGuiException;
import slimeknights.tconstruct.library.utils.TagUtil;
import slimeknights.tconstruct.library.utils.Tags;
import slimeknights.tconstruct.library.utils.TinkerUtil;
import slimeknights.toolleveling.TinkerToolLeveling;
import slimeknights.toolleveling.ToolLevelNBT;

import java.awt.*;
import java.util.List;

/*
 * Base code is from Tinkers' Tool Leveling by boni
 * Tinkers' Tool Leveling is open source and distributed under the MIT License
 * View the source code on github: https://github.com/SlimeKnights/TinkersToolLeveling
 * View the MIT License here: https://tldrlegal.com/license/mit-license
 */
public class ModArmorLeveling extends ArmorModifierTrait {

    public static ModArmorLeveling modArmorLeveling;

    public static int newArmorMinModifiers;
    public static int maximumLevels;
    public static int baseXP;
    public static float levelMultiplier;
    public static int xpGainCap;
    public static float damageToXP;

    public ModArmorLeveling() {
        super("leveling", 0xffffff);
        aspects.clear();
        addAspects(new ModifierAspect.DataAspect(this));
        MinecraftForge.EVENT_BUS.register(this);

        newArmorMinModifiers = ConfigHandler.leveling.newArmorMinModifiers;
        maximumLevels = ConfigHandler.leveling.maximumLevels;
        baseXP = ConfigHandler.leveling.baseXP;
        levelMultiplier = ConfigHandler.leveling.levelMultiplier;
        xpGainCap = ConfigHandler.leveling.xpGainCap;
        damageToXP = ConfigHandler.leveling.damageToXP;
    }

    @SubscribeEvent
    public void onArmorBuild(ArmoryEvent.OnItemBuilding evt) {

        List<Material> materials = Lists.newArrayList();
        for(int i = 0; i < evt.armor.getRequiredComponents().size(); i++) {
            materials.add(Material.UNKNOWN);
        }
        NBTTagCompound baseTag = evt.armor.buildTag(materials);

        int modifiers = baseTag.getInteger(Tags.FREE_MODIFIERS);
        int modifierDelta = getStartingModifiers() - modifiers;

        NBTTagCompound toolTag = TagUtil.getToolTag(evt.tag);
        modifiers = toolTag.getInteger(Tags.FREE_MODIFIERS);
        modifiers += modifierDelta;
        modifiers = Math.max(0, modifiers);
        toolTag.setInteger(Tags.FREE_MODIFIERS, modifiers);
        TagUtil.setToolTag(evt.tag, toolTag);

        if(TinkerUtil.getModifierTag(evt.tag, getModifierIdentifier()).isEmpty()) {
            apply(evt.tag);
        }

        if(!TinkerUtil.hasModifier(evt.tag, getModifierIdentifier())) {
            apply(evt.tag);
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onTooltip(ItemTooltipEvent evt) {
        Tooltips.addTooltips(evt.getItemStack(), evt.getToolTip());
    }

    @Override
    public float onHurt(ItemStack armor, EntityPlayer player, DamageSource source, float damage, float newDamage, LivingHurtEvent evt) {
        if (damage > 0 && !source.isUnblockable()) {
            addXp(armor, MathHelper.clamp((int) (damage * damageToXP), 1, xpGainCap), player);
        }
        return newDamage;
    }

    @Override
    public boolean isHidden() {
        return true;
    }

    @Override
    public boolean canApplyCustom(ItemStack stack) {
        return true;
    }

    @Override
    public void applyEffect(NBTTagCompound rootCompound, NBTTagCompound modifierTag) {
        super.applyEffect(rootCompound, modifierTag);

        ToolLevelNBT data = getLevelData(modifierTag);

        NBTTagCompound toolTag = TagUtil.getToolTag(rootCompound);
        int modifiers = toolTag.getInteger(Tags.FREE_MODIFIERS) + data.bonusModifiers;
        toolTag.setInteger(Tags.FREE_MODIFIERS, Math.max(0, modifiers));
        TagUtil.setToolTag(rootCompound, toolTag);
    }

    public void addXp(ItemStack armor, int amount, EntityPlayer player) {
        NBTTagList tagList = TagUtil.getModifiersTagList(armor);
        int index = TinkerUtil.getIndexInCompoundList(tagList, identifier);
        NBTTagCompound modifierTag = tagList.getCompoundTagAt(index);

        ToolLevelNBT data = getLevelData(modifierTag);
        data.xp += amount;

        if(!canLevelUp(data.level)) {
            return;
        }

        int xpForLevelup = getXpForLevelup(data.level);

        boolean leveledUp = false;
        if(data.xp >= xpForLevelup) {
            data.xp -= xpForLevelup;
            data.level++;
            data.bonusModifiers++;
            leveledUp = true;
        }

        data.write(modifierTag);
        TagUtil.setModifiersTagList(armor, tagList);

        if(leveledUp) {
            this.apply(armor);
            if(!player.world.isRemote) {
                TinkerToolLeveling.proxy.playLevelupDing(player);
                TinkerToolLeveling.proxy.sendLevelUpMessage(data.level, armor, player);
            }
            try {
                NBTTagCompound rootTag = TagUtil.getTagSafe(armor);
                ArmorBuilder.rebuildArmor(rootTag, (TinkersArmor) armor.getItem());
                armor.setTagCompound(rootTag);
            } catch(TinkerGuiException e) {
                e.printStackTrace();
            }
        }
    }

    public int getXpForLevelup(int level) {
        if(level <= 1) {
            return getBaseXp();
        }
        return (int) ((float) getXpForLevelup(level - 1) * getLevelMultiplier());
    }

    private ToolLevelNBT getLevelData(ItemStack itemStack) {
        return getLevelData(TinkerUtil.getModifierTag(itemStack, getModifierIdentifier()));
    }

    private ToolLevelNBT getLevelData(NBTTagCompound modifierNBT) {
        return new ToolLevelNBT(modifierNBT);
    }

    public static int getBaseXp() {
        return baseXP;
    }

    public static float getLevelMultiplier() {
        return levelMultiplier;
    }

    public static int getStartingModifiers() {
        return newArmorMinModifiers;
    }

    public static boolean canLevelUp(int currentLevel) {
        return maximumLevels < 0 || maximumLevels >= currentLevel;
    }

    private static class Tooltips {

        public static void addTooltips(ItemStack itemStack, List<String> tooltips) {
            NBTTagCompound tag = TinkerUtil.getModifierTag(itemStack, modArmorLeveling.getModifierIdentifier());
            if(!tag.isEmpty()) {
                ToolLevelNBT data = new ToolLevelNBT(tag);
                if(canLevelUp(data.level)) {
                    tooltips.add(1, getXpToolTip(data.xp, modArmorLeveling.getXpForLevelup(data.level)));
                }
                tooltips.add(1, getLevelTooltip(data.level));
            }
        }

        private static String getXpToolTip(int xp, int xpNeeded) {
            return String.format("%s: %s", I18n.translateToLocal("tooltip.xp"), getXpString(xp, xpNeeded));
        }

        private static String getXpString(int xp, int xpNeeded) {
            return TextFormatting.WHITE + String.format("%d / %d", xp, xpNeeded);
        }

        private static String getLevelTooltip(int level) {
            return String.format("%s: %s", I18n.translateToLocal("tooltip.level"), getLevelString(level));
        }

        public static String getLevelString(int level) {
            return getLevelColor(level) + getRawLevelString(level) + TextFormatting.RESET;
        }

        private static String getRawLevelString(int level) {
            if(level <= 0) {
                return "";
            }

            if(I18n.canTranslate("tooltip.level." + level)) {
                return I18n.translateToLocal("tooltip.level." + level);
            }

            int i = 1;
            while(I18n.canTranslate("tooltip.level." + i)) {
                i++;
            }

            StringBuilder str = new StringBuilder(I18n.translateToLocal("tooltip.level." + (level % i)));

            for(int j = level / i; j > 0; j--) {
                str.append('+');
            }

            return str.toString();
        }

        private static String getLevelColor(int level) {
            float hue = (0.277777f * level);
            hue = hue - (int) hue;
            return CustomFontColor.encodeColor(Color.HSBtoRGB(hue, 0.75f, 0.8f));
        }
    }
}
