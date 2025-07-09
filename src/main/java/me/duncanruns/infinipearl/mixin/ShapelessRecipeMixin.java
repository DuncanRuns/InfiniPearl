package me.duncanruns.infinipearl.mixin;

import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.EnderPearlItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.ShapelessRecipe;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;


@Mixin(ShapelessRecipe.class)
public abstract class ShapelessRecipeMixin implements CraftingRecipe {

    @Inject(at = @At("HEAD"), method = "matches", cancellable = true)
    private void cancelCraftingMixin(CraftingRecipeInput craftingRecipeInput, World world, CallbackInfoReturnable<Boolean> info) {
        List<ItemStack> itemStackList = craftingRecipeInput.getStacks();
        for (ItemStack stack : itemStackList) {
                if (stack.getItem() instanceof EnderPearlItem && stack.hasEnchantments()) {
                    info.setReturnValue(false);
                }
            }
    }
}
