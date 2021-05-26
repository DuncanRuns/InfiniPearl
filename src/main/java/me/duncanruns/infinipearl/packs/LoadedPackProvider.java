package me.duncanruns.infinipearl.packs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import me.duncanruns.infinipearl.InfiniPearl;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resource.DirectoryResourcePack;
import net.minecraft.resource.ResourcePackProfile;
import net.minecraft.resource.ResourcePackProfile.Factory;
import net.minecraft.resource.ResourcePackProvider;
import net.minecraft.resource.ResourcePackSource;
import org.apache.logging.log4j.Level;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
public class LoadedPackProvider implements ResourcePackProvider {
    private final File packFolder;
    private final File itemModelFolder;
    private final File langFolder;

    public LoadedPackProvider() {
        packFolder = new File(FabricLoader.getInstance().getGameDir().toString(), "InfiniPearl Resources");
        itemModelFolder = new File(packFolder.getPath(), "assets/infinipearl/models/item/");
        langFolder = new File(packFolder.getPath(), "assets/infinipearl/lang/");

    }

    @Override
    public <T extends ResourcePackProfile> void register(Consumer<T> consumer, Factory<T> factory) {
        this.packFolder.mkdirs();
        this.itemModelFolder.mkdirs();
        this.langFolder.mkdirs();

        File metaFile = new File(packFolder, "pack.mcmeta");
        JsonObject object = new JsonObject();
        JsonObject pack = new JsonObject();
        pack.addProperty("pack_format", 5);
        pack.addProperty("description", "Resource loader by MarioAndWeegee3.");
        object.add("pack", pack);

        File itemFile = new File(itemModelFolder, "infinipearl.json");
        JsonObject parent = new JsonObject();
        parent.addProperty("parent", "minecraft:item/ender_pearl");

        File langFile = new File(langFolder, "en_us.json");
        //JsonObject infinipearlLang = new JsonObject();
        //infinipearlLang.addProperty("item.infinipearl.infinipearl", "\u00A7bInfiniPearl™");

        Gson gson1 = new GsonBuilder().setPrettyPrinting().create();
        Gson gson2 = new GsonBuilder().setPrettyPrinting().create();
        Gson gson3 = new GsonBuilder().setPrettyPrinting().create();

        try {
            FileWriter metaWriter = new FileWriter(metaFile);
            metaWriter.write(gson1.toJson(object));
            metaWriter.close();

            FileWriter itemWriter = new FileWriter(itemFile);
            itemWriter.write(gson2.toJson(parent));
            itemWriter.close();

            FileWriter langWriter = new FileWriter(langFile);
            langWriter.write("{\"item.infinipearl.infinipearl\": \"\\u00A7bInfiniPearl\\u2122\"}");
            langWriter.close();

        } catch (IOException e) {
            InfiniPearl.log(Level.ERROR, e.toString());
        }


        String name = "Resource Loader pack";
        T container = ResourcePackProfile.of(name, true, () -> new DirectoryResourcePack(packFolder), factory, ResourcePackProfile.InsertionPosition.TOP, ResourcePackSource.PACK_SOURCE_BUILTIN);
        if (container != null) {
            consumer.accept(container);
        } else {
            InfiniPearl.log(Level.ERROR, "Error loading resources");
        }
    }
}