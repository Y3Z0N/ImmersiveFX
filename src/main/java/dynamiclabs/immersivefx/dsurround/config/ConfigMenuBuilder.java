/*
 * Dynamic Surroundings
 * Copyright (C) 2020  OreCruncher
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>
 */

package dynamiclabs.immersivefx.dsurround.config;

import dynamiclabs.immersivefx.lib.config.ClothAPIFactory;
import dynamiclabs.immersivefx.mobeffects.config.ConfigGenerator;
import me.shedaniel.clothconfig2.forge.api.ConfigBuilder;
import me.shedaniel.clothconfig2.forge.api.ConfigCategory;
import me.shedaniel.clothconfig2.forge.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.forge.impl.builders.SubCategoryBuilder;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import dynamiclabs.immersivefx.dsurround.huds.lightlevel.LightLevelHUD;

import javax.annotation.Nonnull;

@OnlyIn(Dist.CLIENT)
public class ConfigMenuBuilder extends ClothAPIFactory {

    public ConfigMenuBuilder() {
        super(new TranslationTextComponent("dsurround.modname"), () -> {
                    Config.SPEC.save();
                    dynamiclabs.immersivefx.sndctrl.config.Config.SPEC.save();
                    dynamiclabs.immersivefx.environs.config.Config.SPEC.save();
                    dynamiclabs.immersivefx.mobeffects.config.Config.SPEC.save();
                },
                new ResourceLocation("minecraft:textures/block/cobblestone.png"));
    }

    @Override
    protected void generate(@Nonnull final ConfigBuilder builder) {
        final ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        ConfigCategory category = createRootCategory(builder);
        SubCategoryBuilder modRoot = createSubCategory(entryBuilder, "dsurround.modname", TextFormatting.GOLD, false);

        SubCategoryBuilder subCategory = createSubCategory(entryBuilder, "dsurround.cfg.logging", TextFormatting.YELLOW, false);

        subCategory.add(
                createBoolean(
                        builder,
                        Config.CLIENT.logging.onlineVersionCheck));

        subCategory.add(
                createBoolean(
                        builder,
                        Config.CLIENT.logging.enableLogging));

        subCategory.add(
                createInteger(
                        builder,
                        Config.CLIENT.logging.flagMask));

        modRoot.add(subCategory.build());

        subCategory = createSubCategory(entryBuilder, "dsurround.cfg.lightlevel", TextFormatting.YELLOW, false);

        subCategory.add(
                createEnumList(
                        builder,
                        LightLevelHUD.ColorSet.class,
                        Config.CLIENT.lightLevel.colorSet));

        subCategory.add(
                createEnumList(
                        builder,
                        LightLevelHUD.Mode.class,
                        Config.CLIENT.lightLevel.mode));

        subCategory.add(
                createInteger(
                        builder,
                        Config.CLIENT.lightLevel.range));

        subCategory.add(
                createInteger(
                        builder,
                        Config.CLIENT.lightLevel.lightSpawnThreshold));

        subCategory.add(
                createBoolean(
                        builder,
                        Config.CLIENT.lightLevel.hideSafe));

        modRoot.add(subCategory.build());

        category.addEntry(modRoot.build());

        // Build child mod menus
        category.addEntry(dynamiclabs.immersivefx.sndctrl.config.ConfigGenerator.generate(builder, entryBuilder).build());
        category.addEntry(dynamiclabs.immersivefx.environs.config.ConfigGenerator.generate(builder, entryBuilder).build());
        category.addEntry(ConfigGenerator.generate(builder, entryBuilder).build());
    }
}
