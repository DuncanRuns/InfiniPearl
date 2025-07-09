package me.duncanruns.infinipearl.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.component.Component;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.common.SyncedClientOptions;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.ServerStatHandler;
import net.minecraft.stat.Stats;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity {
    @Shadow
    @Final
    private ServerStatHandler statHandler;

    public ServerPlayerEntityMixin(World world, GameProfile gameProfile) {
        super(world, gameProfile);
    }

    @Inject(at = @At("TAIL"), method = "<init>")
    private void initMixin(MinecraftServer server, ServerWorld world, GameProfile profile, SyncedClientOptions clientOptions, CallbackInfo ci) {
        if (statHandler != null && statHandler.getStat(Stats.CUSTOM.getOrCreateStat(Stats.PLAY_TIME)) == 0) {

            ItemStack infiniPearl = new ItemStack(Items.ENDER_PEARL, 1);
            RegistryEntry<Enchantment> infinityEntry = world.getRegistryManager().getEntryOrThrow(Enchantments.INFINITY);

            infiniPearl.addEnchantment(infinityEntry, 1);
            MutableText text = Text.literal("InfiniPearl™");
            text.setStyle(text.getStyle().withItalic(false));
            infiniPearl.set(DataComponentTypes.CUSTOM_NAME, text);

            this.inventory.main.set(0, infiniPearl);
        }
    }
}
