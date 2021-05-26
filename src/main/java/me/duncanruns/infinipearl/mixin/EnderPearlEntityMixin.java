package me.duncanruns.infinipearl.mixin;

import me.duncanruns.infinipearl.InfiniPearl;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.EndermiteEntity;
import net.minecraft.entity.projectile.thrown.EnderPearlEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EnderPearlEntity.class)
public abstract class EnderPearlEntityMixin extends ThrownItemEntity {

    public EnderPearlEntityMixin(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(at=@At("HEAD"),cancellable = true,method="onCollision")
    private void onCollisionMixin(HitResult hitResult, CallbackInfo info) {

        boolean ifb = super.getItem().getItem().equals(InfiniPearl.INFINIPEARL_ITEM);

        super.onCollision(hitResult);
        Entity entity = this.getOwner();

        for (int i = 0; i < 32; ++i) {
            this.world.addParticle(ParticleTypes.PORTAL, this.getX(), this.getY() + this.random.nextDouble() * 2.0D, this.getZ(), this.random.nextGaussian(), 0.0D, this.random.nextGaussian());
        }

        if (!this.world.isClient && !this.removed) {
            if (entity instanceof ServerPlayerEntity) {
                ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) entity;
                if (serverPlayerEntity.networkHandler.getConnection().isOpen() && serverPlayerEntity.world == this.world && !serverPlayerEntity.isSleeping()) {
                    if (!ifb && (this.random.nextFloat() < 0.05F && this.world.getGameRules().getBoolean(GameRules.DO_MOB_SPAWNING))) {
                        EndermiteEntity endermiteEntity = (EndermiteEntity) EntityType.ENDERMITE.create(this.world);
                        endermiteEntity.setPlayerSpawned(true);
                        endermiteEntity.refreshPositionAndAngles(entity.getX(), entity.getY(), entity.getZ(), entity.yaw, entity.pitch);
                        this.world.spawnEntity(endermiteEntity);
                    }

                    if (entity.hasVehicle()) {
                        entity.stopRiding();
                    }

                    entity.requestTeleport(this.getX(), this.getY(), this.getZ());
                    entity.fallDistance = 0.0F;
                    if (!ifb) {
                        entity.damage(DamageSource.FALL, 5.0F);
                    }
                }
            } else if (entity != null) {
                entity.requestTeleport(this.getX(), this.getY(), this.getZ());
                entity.fallDistance = 0.0F;
            }

            this.remove();
        }

        info.cancel();
    }

}
