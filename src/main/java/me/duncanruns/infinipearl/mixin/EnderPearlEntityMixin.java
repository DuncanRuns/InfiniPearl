package me.duncanruns.infinipearl.mixin;

import net.minecraft.component.EnchantmentEffectComponentTypes;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.EndermiteEntity;
import net.minecraft.entity.projectile.thrown.EnderPearlEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.network.packet.s2c.play.PositionFlag;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameRules;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;

@Mixin(EnderPearlEntity.class)
public abstract class EnderPearlEntityMixin extends ThrownItemEntity {

    public EnderPearlEntityMixin(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(at = @At("HEAD"), cancellable = true, method = "onCollision")
    private void onCollisionMixin(HitResult hitResult, CallbackInfo info) {
        boolean ifb = EnchantmentHelper.hasAnyEnchantmentsWith(
                this.getStack(),
                EnchantmentEffectComponentTypes.AMMO_USE
        );

        super.onCollision(hitResult);

        for(int i = 0; i < 32; ++i) {
            this.getWorld().addParticleClient(ParticleTypes.PORTAL, this.getX(), this.getY() + this.random.nextDouble() * (double)2.0F, this.getZ(), this.random.nextGaussian(), (double)0.0F, this.random.nextGaussian());
        }

        World var3 = this.getWorld();
        if (var3 instanceof ServerWorld serverWorld) {
            if (!this.isRemoved()) {
                Entity entity = this.getOwner();
                if (entity != null && canTeleportEntityTo(entity, serverWorld)) {
                    Vec3d vec3d = this.getLastRenderPos();
                    if (entity instanceof ServerPlayerEntity) {
                        ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity)entity;
                        if (serverPlayerEntity.networkHandler.isConnectionOpen()) {
                            if (!ifb && (this.random.nextFloat() < 0.05F && serverWorld.getGameRules().getBoolean(GameRules.DO_MOB_SPAWNING))) {
                                EndermiteEntity endermiteEntity = (EndermiteEntity)EntityType.ENDERMITE.create(serverWorld, SpawnReason.TRIGGERED);
                                if (endermiteEntity != null) {
                                    endermiteEntity.refreshPositionAndAngles(entity.getX(), entity.getY(), entity.getZ(), entity.getYaw(), entity.getPitch());
                                    serverWorld.spawnEntity(endermiteEntity);
                                }
                            }

                            if (this.hasPortalCooldown()) {
                                entity.resetPortalCooldown();
                            }

                            ServerPlayerEntity serverPlayerEntity2 = serverPlayerEntity.teleportTo(new TeleportTarget(serverWorld, vec3d, Vec3d.ZERO, 0.0F, 0.0F, PositionFlag.combine(new Set[]{PositionFlag.ROT, PositionFlag.DELTA}), TeleportTarget.NO_OP));
                            if (serverPlayerEntity2 != null) {
                                serverPlayerEntity2.onLanding();
                                serverPlayerEntity2.clearCurrentExplosion();
                                serverPlayerEntity2.damage(serverPlayerEntity.getWorld(), this.getDamageSources().enderPearl(), 5.0F);
                            }

                            this.playTeleportSound(serverWorld, vec3d);
                        }
                    } else {
                        Entity entity2 = entity.teleportTo(new TeleportTarget(serverWorld, vec3d, entity.getVelocity(), entity.getYaw(), entity.getPitch(), TeleportTarget.NO_OP));
                        if (entity2 != null) {
                            entity2.onLanding();
                        }

                        this.playTeleportSound(serverWorld, vec3d);
                    }

                    this.discard();
                    return;
                }

                this.discard();
                return;
            }
        }
            info.cancel();
    }

        @Shadow
        static boolean canTeleportEntityTo(Entity entity, World world) {
            return false;
        }

        @Shadow
        private void playTeleportSound(World world, Vec3d pos) {
        }

}
