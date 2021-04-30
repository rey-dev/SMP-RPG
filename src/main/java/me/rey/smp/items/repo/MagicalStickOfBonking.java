package me.rey.smp.items.repo;

import me.rey.smp.items.CustomItem;
import me.rey.utils.items.Item;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.util.Vector;

public class MagicalStickOfBonking extends CustomItem {

    public MagicalStickOfBonking() {
        super(
                "MSB",
                new Item(Material.STICK).setName("&6Magical Stick of Bonking").setGlow(true)
        );
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onRightClickEntity(final PlayerInteractEntityEvent event) {
        final Player caster = event.getPlayer();
        final PlayerInventory playerInventory = caster.getInventory();
        if (!this.isPlayerHolding(caster)) {
            return;
        }

        final Entity entityHit = event.getRightClicked();
        this.bonk(entityHit, caster);
    }

    private void bonk(final Entity e, final Player p) {
        final Vector velocity = p.getLocation().getDirection().subtract(e.getLocation().getDirection()).multiply(1.5).normalize();
        e.setVelocity(velocity);

        // TODO: Message players
        // TODO: Effects & Sounds
    }

}
