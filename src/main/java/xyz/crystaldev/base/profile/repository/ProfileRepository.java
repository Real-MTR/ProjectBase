package xyz.crystaldev.base.profile.repository;

import com.google.gson.Gson;
import xyz.crystaldev.base.profile.Profile;
import xyz.crystaldev.base.util.mongo.MongoAPI;
import xyz.crystaldev.base.util.mongo.repository.MongoRepository;

public class ProfileRepository extends MongoRepository<Profile> {

    public ProfileRepository(MongoAPI mongoAPI, Gson gson) {
        super(mongoAPI, gson);

        this.setCollection(mongoAPI.getMongoDatabase().getCollection("profiles"));
    }
}
