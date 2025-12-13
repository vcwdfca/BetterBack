package com.vcwdfca.bb.mixin.betterquesting.questing.tasks;

import betterquesting.questing.tasks.TaskAdvancement;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = TaskAdvancement.class)
public class MixinTaskAdvancement {
    @WrapOperation(method = "getTextForSearch", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/ResourceLocation;toString()Ljava/lang/String;"))
    private String guardToString(ResourceLocation instance, Operation<String> original) {
        return instance == null ? "" : original.call(instance);
    }
}
