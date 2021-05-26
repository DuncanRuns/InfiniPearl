package me.duncanruns.infinipearl;

import net.fabricmc.api.ModInitializer;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class InfiniPearl implements ModInitializer {
//      ENDER_PEARL = register("ender_pearl", EntityType.Builder.create(EnderPearlEntity::new, SpawnGroup.MISC).setDimensions(0.25F, 0.25F).maxTrackingRange(4).trackingTickInterval(10));
    public static Logger LOGGER = LogManager.getLogger();

    public static final String MOD_ID = "infinipearl";
    public static final String MOD_NAME = "InfiniPearl";

    public static final Item INFINIPEARL_ITEM = new InfiniPearlItem((new Item.Settings()).maxCount(1).group(ItemGroup.TRANSPORTATION));

    @Override
    public void onInitialize() {
        log(Level.INFO, "Initializing");
        Registry.register(Registry.ITEM, new Identifier(MOD_ID,"infinipearl"), INFINIPEARL_ITEM);
    }

    public static void log(Level level, String message){
        LOGGER.log(level, "["+MOD_NAME+"] " + message);
    }

}