package me.duncanruns.infinipearl.mixin;

import com.mojang.authlib.GameProfile;
import me.duncanruns.infinipearl.InfiniPearl;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.ServerStatHandler;
import net.minecraft.stat.StatHandler;
import net.minecraft.stat.Stats;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity {
    public ServerPlayerEntityMixin(World world, BlockPos blockPos, GameProfile gameProfile) {
        super(world, blockPos, gameProfile);
    }

    @Shadow
    @Final
    private ServerStatHandler statHandler;

    @Inject(at = @At("TAIL"), method = "<init>")
    private void initMixin(MinecraftServer server, ServerWorld world, GameProfile profile, ServerPlayerInteractionManager interactionManager, CallbackInfo info) {
        if(statHandler.getStat(Stats.CUSTOM.getOrCreateStat(Stats.PLAY_ONE_MINUTE)) == 0){
            this.inventory.main.set(0,new ItemStack(InfiniPearl.INFINIPEARL_ITEM,1));
        }
    }
}
