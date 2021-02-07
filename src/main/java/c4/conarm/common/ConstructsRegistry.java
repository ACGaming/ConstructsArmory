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

package c4.conarm.common;

import c4.conarm.common.blocks.BlockArmorForge;
import c4.conarm.common.blocks.BlockArmorStation;
import c4.conarm.common.blocks.BlockSoftObsidian;
import c4.conarm.common.items.*;
import c4.conarm.common.items.armor.Boots;
import c4.conarm.common.items.armor.Chestplate;
import c4.conarm.common.items.armor.Helmet;
import c4.conarm.common.items.armor.Leggings;
import c4.conarm.lib.ArmoryRegistry;
import c4.conarm.lib.armor.ArmorCore;
import c4.conarm.lib.armor.ArmorPart;
import c4.conarm.lib.utils.ConstructUtils;
import com.google.common.collect.Lists;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.commons.lang3.tuple.Pair;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.tinkering.PartMaterialType;
import slimeknights.tconstruct.library.tools.IPattern;
import slimeknights.tconstruct.library.tools.IToolPart;
import slimeknights.tconstruct.library.tools.Pattern;
import slimeknights.tconstruct.tools.TinkerTools;

import java.util.List;

public class ConstructsRegistry {

    //Helper
    public static List<Pair<Item, ArmorPart>> armorPartPatterns = Lists.newLinkedList();

    //Accessories
    public static AccessoryBase travelBelt;
    public static AccessoryBase travelPotion;
    public static AccessoryBase travelSack;
    public static AccessoryBase travelGoggles;
    public static AccessoryBase travelNight;
    public static AccessoryBase travelSoul;
    public static AccessoryBase travelSneak;
    public static AccessoryBase travelSlowFall;

    //Blocks
    public static BlockArmorForge armorForge;
    public static BlockArmorStation armorStation;
    public static BlockSoftObsidian softObsidian;

    //Armor Parts
    public static ArmorPart helmetCore;
    public static ArmorPart armorTrim;
    public static ArmorPart armorPlate;
    public static ArmorPart chestCore;
    public static ArmorPart leggingsCore;
    public static ArmorPart bootsCore;

    //Armor
    public static ArmorCore helmet;
    public static ArmorCore chestplate;
    public static ArmorCore leggings;
    public static ArmorCore boots;

    //Items
    public static ItemArmoryBook book;
    public static ItemPolishingKit polishingKit;
    public static ItemBase invisibleInk;
    public static ItemBase resistMat;
    public static ItemBase fireResistMat;
    public static ItemBase projResistMat;
    public static ItemBase blastResistMat;
    public static ItemBase gauntletMat;
    public static ItemBase gauntletAttack;
    public static ItemBase gauntletSpeed;
    public static ItemBase gauntletReach;
    public static ItemBase frostySoles;
    public static ItemBase travelCloak;
    public static ItemBase travelBeltBase;
    public static ItemBase travelGogglesBase;

    public static void registerArmorParts(IForgeRegistry<Item> registry) {
        helmetCore = registerArmorPart(registry, new ArmorPart(ConfigHandler.ingotValue * 4, EntityEquipmentSlot.HEAD), "helmet_core");
        armorTrim = registerArmorPart(registry, new ArmorPart(ConfigHandler.ingotValue), "armor_trim");
        armorPlate = registerArmorPart(registry, new ArmorPart(ConfigHandler.ingotValue * 3), "armor_plate");
        chestCore = registerArmorPart(registry, new ArmorPart(ConfigHandler.ingotValue * 6, EntityEquipmentSlot.CHEST), "chest_core");
        leggingsCore = registerArmorPart(registry, new ArmorPart(ConfigHandler.ingotValue * 5, EntityEquipmentSlot.LEGS), "leggings_core");
        bootsCore = registerArmorPart(registry, new ArmorPart(ConfigHandler.ingotValue * 4, EntityEquipmentSlot.FEET), "boots_core");
    }

    public static void initModels() {
        travelBeltBase.initModel();
        travelBelt.initModel();
        travelPotion.initModel();
        travelSack.initModel();
        travelGogglesBase.initModel();
        travelGoggles.initModel();
        travelNight.initModel();
        travelSoul.initModel();
        travelCloak.initModel();
        travelSlowFall.initModel();
        travelSneak.initModel();
        book.initModel();
        resistMat.initModel();
        fireResistMat.initModel();
        projResistMat.initModel();
        blastResistMat.initModel();
        gauntletMat.initModel();
        gauntletAttack.initModel();
        gauntletSpeed.initModel();
        gauntletReach.initModel();
        invisibleInk.initModel();
        frostySoles.initModel();
    }

    public static void registerArmorPieces(IForgeRegistry<Item> registry) {
        helmet = registerArmorPiece(registry, new Helmet("classic"), "helmet");
        chestplate = registerArmorPiece(registry, new Chestplate("classic"), "chestplate");
        leggings = registerArmorPiece(registry, new Leggings("classic"), "leggings");
        boots = registerArmorPiece(registry, new Boots("classic"), "boots");
    }

    public static void registerItems(IForgeRegistry<Item> registry) {
        book = ConstructUtils.registerItem(registry, new ItemArmoryBook(), "book");
        invisibleInk = ConstructUtils.registerItem(registry, new ItemConarmMat(), "invisible_ink");
        resistMat = ConstructUtils.registerItem(registry, new ItemConarmMat(), "resist_mat");
        fireResistMat = ConstructUtils.registerItem(registry, new ItemConarmMat(), "resist_mat_fire");
        projResistMat = ConstructUtils.registerItem(registry, new ItemConarmMat(), "resist_mat_proj");
        blastResistMat = ConstructUtils.registerItem(registry, new ItemConarmMat(), "resist_mat_blast");
        gauntletMat = ConstructUtils.registerItem(registry, new ItemBase(), "gauntlet_mat");
        gauntletSpeed = ConstructUtils.registerItem(registry, new ItemConarmMat(), "gauntlet_mat_speed");
        gauntletAttack = ConstructUtils.registerItem(registry, new ItemConarmMat(), "gauntlet_mat_attack");
        gauntletReach = ConstructUtils.registerItem(registry, new ItemConarmMat(), "gauntlet_mat_reach");
        frostySoles = ConstructUtils.registerItem(registry, new ItemConarmMat(), "frosty_soles");
        travelBeltBase = ConstructUtils.registerItem(registry, new ItemBase(), "travel_belt_base");
        travelBelt = ConstructUtils.registerItem(registry, new AccessoryBase(EntityEquipmentSlot.LEGS), "travel_belt");
        travelPotion = ConstructUtils.registerItem(registry, new AccessoryBase(EntityEquipmentSlot.LEGS), "travel_potion");
        travelSack = ConstructUtils.registerItem(registry, new AccessoryBase(EntityEquipmentSlot.CHEST), "travel_sack");
        travelGogglesBase = ConstructUtils.registerItem(registry, new ItemBase(), "travel_goggles_base");
        travelGoggles = ConstructUtils.registerItem(registry, new AccessoryBase(EntityEquipmentSlot.HEAD), "travel_goggles");
        travelNight = ConstructUtils.registerItem(registry, new AccessoryBase(EntityEquipmentSlot.HEAD), "travel_night");
        travelCloak = ConstructUtils.registerItem(registry, new ItemBase(), "travel_cloak");
        travelSneak = ConstructUtils.registerItem(registry, new AccessoryBase(EntityEquipmentSlot.CHEST), "travel_sneak");
        travelSlowFall = ConstructUtils.registerItem(registry, new AccessoryBase(EntityEquipmentSlot.CHEST), "travel_slowfall");
        travelSoul = ConstructUtils.registerItem(registry, new AccessoryBase(EntityEquipmentSlot.HEAD), "travel_soul");
        polishingKit = (ItemPolishingKit) registerArmorPart(registry, new ItemPolishingKit(), "polishing_kit");
        polishingKit.setCreativeTab(TinkerRegistry.tabParts);
        TinkerRegistry.registerToolPart(polishingKit);
        TinkerRegistry.registerStencilTableCrafting(Pattern.setTagForPart(new ItemStack(TinkerTools.pattern), polishingKit));
    }

    private static <T extends ArmorCore> T registerArmorPiece(IForgeRegistry<Item> registry, T item, String name) {
        for(PartMaterialType pmt : item.getRequiredComponents()) {
            for (IToolPart ap : pmt.getPossibleParts()) {
                TinkerRegistry.registerToolPart(ap);
            }
        }
        return ConstructUtils.registerItem(registry, item, name);
    }

    private static ArmorPart registerArmorPart(IForgeRegistry<Item> registry, ArmorPart part, String name) {
        return registerArmorPart(registry, part, name, TinkerTools.pattern);
    }

    private static <T extends Item & IPattern> ArmorPart registerArmorPart(IForgeRegistry<Item> registry, ArmorPart part, String name, T pattern) {
        ArmorPart ret = ConstructUtils.registerItem(registry, part, name);
        ArmoryRegistry.armorParts.add(ret);
        if (pattern != null) {
            armorPartPatterns.add(Pair.of(pattern, ret));
        }
        return ret;
    }
}
