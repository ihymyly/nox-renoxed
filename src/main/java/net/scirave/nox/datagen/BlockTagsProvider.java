/*
 * -------------------------------------------------------------------
 * Nox
 * Copyright (c) 2024 SciRave
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * -------------------------------------------------------------------
 */

package net.scirave.nox.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalItemTags;
import net.minecraft.block.Blocks;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;
import net.scirave.nox.Nox;
import net.scirave.nox.util.NoxUtil;

import java.util.concurrent.CompletableFuture;

class BlockTagsProvider extends FabricTagProvider.BlockTagProvider {
    public BlockTagsProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {

        this.getOrCreateTagBuilder(BlockTags.SWORD_EFFICIENT)
                .add(Nox.NOX_COBWEB)
        ;

        this.getOrCreateTagBuilder(NoxUtil.NOX_ALWAYS_MINE)
                .addOptionalTag(BlockTags.WOODEN_DOORS)
                .addOptionalTag(BlockTags.WOODEN_TRAPDOORS)
                .addOptionalTag(BlockTags.PLANKS)
                .addOptionalTag(BlockTags.OVERWORLD_NATURAL_LOGS)
        ;

        this.getOrCreateTagBuilder(NoxUtil.NOX_CANT_MINE)
                .addOptionalTag(BlockTags.BANNERS)
                .addOptionalTag(BlockTags.BUTTONS)
                .addOptionalTag(BlockTags.CLIMBABLE)
                .addOptionalTag(BlockTags.CROPS)
                .addOptionalTag(BlockTags.PRESSURE_PLATES)
                .addOptionalTag(BlockTags.RAILS)
                .addOptionalTag(BlockTags.REPLACEABLE)
                .addOptionalTag(BlockTags.SAPLINGS)
                .addOptionalTag(BlockTags.FLOWERS)
                .addOptionalTag(BlockTags.ALL_SIGNS)
                .add(Blocks.TINTED_GLASS)
        ;

    }
}
