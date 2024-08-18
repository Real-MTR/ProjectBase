package xyz.crystaldev.base;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.LongSerializationPolicy;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.crystaldev.base.profile.Profile;
import xyz.crystaldev.base.profile.manager.ProfileManager;
import xyz.crystaldev.base.profile.repository.ProfileRepository;
import xyz.crystaldev.base.util.mongo.MongoAPI;
import xyz.crystaldev.base.util.mongo.credentials.MongoCredentials;
import xyz.crystaldev.base.util.mongo.repository.MongoRepository;

@Getter
public final class ProjectBase extends JavaPlugin {

    private MongoAPI mongoAPI;
    private Gson gson;
    private MongoRepository<Profile> profileRepository;
    private ProfileManager profileManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        initMongo();
        initProfiles();

        registerListeners();
    }

    private void initMongo() {
        this.mongoAPI = new MongoAPI(new MongoCredentials(getConfig().getString("mongo.host"),
                getConfig().getString("mongo.database"),
                getConfig().getInt("mongo.port"),
                getConfig().getBoolean("mongo.auth"),
                getConfig().getString("mongo.user"),
                getConfig().getString("mongo.password")));
        this.gson = new GsonBuilder()
                .setLongSerializationPolicy(LongSerializationPolicy.STRING)
                .setPrettyPrinting().create();
    }

    private void initProfiles() {
        this.profileRepository = new ProfileRepository(mongoAPI, gson);
        this.profileManager = new ProfileManager(this);

        this.profileManager.init();
    }

    private void registerListeners() {

    }

    @Override
    public void onDisable() {
        for (Profile profile : profileManager.getProfileMap().values()) {
            profileManager.save(profile);
        }
    }
}
