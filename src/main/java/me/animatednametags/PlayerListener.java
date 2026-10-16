package me.animatednametags;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRegisterChannelEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public final class PlayerListener implements Listener {

    private final NametagManager manager;

    public PlayerListener(NametagManager manager) {
        this.manager = manager;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        manager.onJoin(event.getPlayer());
    }

    // Lunar Client registra el canal "lunarclient:apollo" al entrar.
    @EventHandler
    public void onRegisterChannel(PlayerRegisterChannelEvent event) {
        if (NametagManager.LUNAR_CHANNEL.equals(event.getChannel())) {
            manager.markLunar(event.getPlayer());
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        manager.onQuit(event.getPlayer());
    }

    // ANTES DEL TELETRANSPORTE: Desmontamos y removemos el nametag para que no bloquee el TP
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPreTeleport(PlayerTeleportEvent event) {
        manager.remove(event.getPlayer());
    }

    // DESPUÉS DEL TELETRANSPORTE: Volvemos a montar y refrescar el nametag en el destino
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onTeleport(PlayerTeleportEvent event) {
        manager.scheduleRefresh(event.getPlayer());
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        manager.scheduleRefresh(event.getPlayer());
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        manager.remove(event.getEntity());
    }
}
