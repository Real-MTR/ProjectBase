package xyz.crystaldev.base.profile.manager;

import com.mongodb.client.model.Filters;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import xyz.crystaldev.base.ProjectBase;
import xyz.crystaldev.base.profile.Profile;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

@RequiredArgsConstructor
public class ProfileManager {

    private final ProjectBase instance;
    @Getter private final Map<UUID, Profile> profileMap = new ConcurrentHashMap<>();

    public void init() {
        for (CompletableFuture<Profile> profile : instance.getProfileRepository().getAll(Profile.class).join()) {
            try {
                profileMap.putIfAbsent(profile.get().getUuid(), profile.get())
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public Profile getProfile(UUID uuid) {
        return profileMap.get(uuid);
    }

    public void save(Profile profile) {
        CompletableFuture.runAsync(() -> {
            profileMap.putIfAbsent(profile.getUuid(), profile);

            instance.getProfileRepository().saveData(profile.getUuid().toString(), profile);
        });
    }

    public void delete(Profile profile) {
        CompletableFuture.runAsync(() -> {
            profileMap.remove(profile.getUuid(), profile);

            instance.getProfileRepository().getCollection().deleteOne(Filters.eq("_id", profile.getUuid().toString()));
        });
    }
}