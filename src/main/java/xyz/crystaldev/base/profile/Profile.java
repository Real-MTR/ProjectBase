package xyz.crystaldev.base.profile;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@RequiredArgsConstructor
@Getter @Setter
public class Profile {

    @SerializedName("_id")
    private final UUID uuid;

    private int kills = 0, deaths = 0, streak = 0;
}