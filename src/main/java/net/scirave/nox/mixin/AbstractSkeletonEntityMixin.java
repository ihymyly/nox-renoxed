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

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Position;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.scirave.nox.config.NoxConfig;
import net.scirave.nox.goals.Nox$FleeSunlightGoal;
import net.scirave.nox.util.Nox$SwimGoalInterface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractSkeletonEntity.class)
public abstract class AbstractSkeletonEntityMixin extends HostileEntity implements Nox$SwimGoalInterface, RangedAttackMob{

    @Unique
    private Vec3d nox$targetVelocity = Vec3d.ZERO;
    @Unique
    private Vec3d nox$lastTargetVelocity = Vec3d.ZERO;
    @Unique
    private Vec3d nox$velocityDifference = Vec3d.ZERO;

    protected AbstractSkeletonEntityMixin(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "initGoals", at = @At("HEAD"))
    public void nox$skeletonInitGoals(CallbackInfo ci) {
        this.goalSelector.add(0, new Nox$FleeSunlightGoal((AbstractSkeletonEntity) (Object) this, 1.0F));
        this.goalSelector.add(1, new SwimGoal((AbstractSkeletonEntity) (Object) this));
    }

    @ModifyExpressionValue(method = "shootAt", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getX()D", ordinal = 0))
    private double addXArrowVelocity(double original, @Local(argsOnly = true) LivingEntity entity) {
        return NoxConfig.skeletonImprovedAim ? original + this.nox$targetVelocity.x * 10 : original;
    }

    @ModifyExpressionValue(method = "shootAt", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getZ()D", ordinal = 0))
    private double addZArrowVelocity(double original, @Local(argsOnly = true) LivingEntity entity) {
        return NoxConfig.skeletonImprovedAim ? original + this.nox$targetVelocity.z * 10 : original;
    }

    @ModifyArg(method = "shootAt", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/PersistentProjectileEntity;setVelocity(DDDFF)V"), index = 3)
    private float changeArrowPower(float original) {
        return NoxConfig.skeletonShootArrowPower;
    }


    @Inject(method = "tickMovement", at = @At("HEAD"))
    public void nox$onTick(CallbackInfo ci) {
        if (NoxConfig.skeletonImprovedAim && this.getTarget() != null) {
            this.nox$velocityDifference = this.nox$velocityDifference.multiply(3).add(this.getTarget().getVelocity().subtract(this.nox$lastTargetVelocity)).multiply(0.25);
            this.nox$targetVelocity = this.nox$targetVelocity.multiply(3).add(this.getTarget().getVelocity().add(this.nox$velocityDifference.multiply(5))).multiply(0.25);
            this.nox$lastTargetVelocity = this.getTarget().getVelocity();
        }
    }

    @Override
    public void nox$modifyAttributes(EntityType<?> entityType, World world, CallbackInfo ci) {
        if (NoxConfig.skeletonSpeedMultiplier > 1) {
            this.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).addTemporaryModifier(new EntityAttributeModifier(Identifier.of("nox:generic_skeleton_bonus"), NoxConfig.skeletonSpeedMultiplier - 1, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE));
        }
    }

    @Override
    public boolean nox$canSwim() {
        return NoxConfig.skeletonsCanSwim;
    }


    public boolean nox$isAllowedToMine() {
        return false;
    }
}
