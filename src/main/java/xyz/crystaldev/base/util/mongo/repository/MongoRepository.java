package xyz.crystaldev.base.util.mongo.repository;

import com.google.gson.Gson;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import xyz.crystaldev.base.util.mongo.MongoAPI;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Getter @Setter
public abstract class MongoRepository<T> {

    private MongoAPI mongoAPI;
    private Gson gson;
    private UpdateOptions updateOptions;
    private MongoCollection<Document> collection;

    public MongoRepository(MongoAPI mongoAPI, Gson gson) {
        this.mongoAPI = mongoAPI;
        this.gson = gson;
        this.updateOptions = new UpdateOptions().upsert(true);
    }

    public CompletableFuture<T> getData(String id, Type type) {
        return CompletableFuture.supplyAsync(() -> {
            Document document = this.collection.find(Filters.eq("_id", id)).first();
            if (document == null) return null;

            return gson.fromJson(document.toJson(), type);
        });
    }

    public CompletableFuture<T> getData(String key, Object value, Type type) {
        return CompletableFuture.supplyAsync(() -> {
            Document document = this.collection.find(Filters.eq(key, value)).first();
            if (document == null) return null;

            return gson.fromJson(document.toJson(), type);
        });
    }

    public CompletableFuture<List<CompletableFuture<T>>> getAll(Type type) {
        return CompletableFuture.supplyAsync(() -> {
            List<CompletableFuture<T>> typeList = new ArrayList<>();

            for (Document document : this.collection.find()) {
                if (document == null) return typeList;

                typeList.add(getData(document.getString("_id"), type));
            }

            return typeList;
        });
    }

    public void saveData(String id, T t) {
        CompletableFuture.supplyAsync(() -> this.collection.replaceOne(Filters.eq("_id", id),
                Document.parse(gson.toJson(t)),
                updateOptions));
    }
}