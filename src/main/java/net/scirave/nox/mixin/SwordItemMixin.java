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

package net.scirave.nox.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.registry.tag.BlockTags;
import net.scirave.nox.Nox;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SwordItem.class)
public class SwordItemMixin {

    @Inject(method = "isSuitableFor", at = @At(value = "HEAD"), cancellable = true)
    public void nox$isSuitableFor(BlockState state, CallbackInfoReturnable<Boolean> cir) {
        if(state.isOf(Nox.NOX_COBWEB)) cir.setReturnValue(true);
    }

    @Inject(method = "getMiningSpeedMultiplier", at = @At(value = "HEAD"), cancellable = true)
    public void nox$getSwordMiningSpeedMultiplier(ItemStack stack, BlockState state, CallbackInfoReturnable<Float> cir) {
        if(state.isOf(Nox.NOX_COBWEB)) cir.setReturnValue(15F);
    }
}
