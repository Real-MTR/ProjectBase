package xyz.crystaldev.base.profile;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import xyz.crystaldev.base.ProjectBase;

import java.util.concurrent.CompletableFuture;

public class ProfileListener implements Listener {

    private final ProjectBase instance;

    public ProfileListener(ProjectBase instance) {
        this.instance = instance;

        this.instance.getServer().getPluginManager().registerEvents(this, instance);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onAsyncPreLogin(AsyncPlayerPreLoginEvent event) {
        CompletableFuture.runAsync(() -> {
            Profile profile = instance.getProfileManager().getProfile(event.getUniqueId());

            if(profile == null) {
                instance.getProfileManager().save(new Profile(event.getUniqueId()));
            }
        });
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onQuit(PlayerQuitEvent event) {
        CompletableFuture.runAsync(() -> {
            Profile profile = instance.getProfileManager().getProfile(event.getPlayer().getUniqueId());

            if(profile != null) {
                instance.getProfileManager().save(profile);
            }
        });
    }
}
