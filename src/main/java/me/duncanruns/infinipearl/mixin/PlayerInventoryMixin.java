package me.duncanruns.infinipearl.mixin;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Nameable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(PlayerInventory.class)
public abstract class PlayerInventoryMixin implements Inventory, Nameable {

    @Redirect(method = "dropAll", at = @At(value = "INVOKE", target = "Ljava/util/List;get(I)Ljava/lang/Object;"))
    private Object thereIsNoPearl(List<ItemStack> instance, int i) {
        ItemStack stack = instance.get(i);
        if (stack.getItem().equals(Items.ENDER_PEARL) & !stack.getEnchantments().isEmpty()) {
            return ItemStack.EMPTY;
        }
        return stack;
    }
}
