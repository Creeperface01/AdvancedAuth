package SimpleAuth;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.block.BlockPlaceEvent;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.entity.ProjectileLaunchEvent;
import cn.nukkit.event.inventory.InventoryPickupArrowEvent;
import cn.nukkit.event.inventory.InventoryPickupItemEvent;
import cn.nukkit.event.player.*;
import cn.nukkit.utils.TextFormat;

import java.io.Console;

public class EventListener implements Listener{

    private SimpleAuth plugin;

    public EventListener(SimpleAuth plugin){
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = false)
    public void onBlockBreak(BlockBreakEvent e){
        Player p = e.getPlayer();

        if(!plugin.isAuthed(p)){
            p.sendMessage(TextFormat.RED + "You are not logged in.");
            e.setCancelled();
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = false)
    public void onBlockPlace(BlockPlaceEvent e){
        Player p = e.getPlayer();

        if(!plugin.isAuthed(p)){
            p.sendMessage(TextFormat.RED + "You are not logged in.");
            e.setCancelled();
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = false)
    public void onJoin(PlayerJoinEvent e){
        Player p = e.getPlayer();

        if(plugin.provider.isPlayerRegistered(p)){
            p.sendMessage(TextFormat.YELLOW + "This account is registered\n" + TextFormat.GREEN + "Please login using /login <password>");
        }else{
            p.sendMessage(TextFormat.GREEN + "Please register using /register <password> <password>");
        }

        plugin.unauthed.put(p.getName().toLowerCase(), p);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = false)
    public void onQuit(PlayerQuitEvent e){
        Player p = e.getPlayer();

        if(!plugin.isAuthed(p)){
            plugin.unauthed.remove(p.getName().toLowerCase());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = false)
    public void onInteract(PlayerInteractEvent e){
        Player p = e.getPlayer();

        if(!plugin.isAuthed(p)){
            p.sendMessage(TextFormat.RED + "You are not logged in.");
            e.setCancelled();
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = false)
    public void onItemConsume(PlayerItemConsumeEvent e){
        Player p = e.getPlayer();

        if(!plugin.isAuthed(p)){
            //p.sendMessage(TextFormat.RED + "You are not logged in.");
            e.setCancelled();
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = false)
    public void onMove(PlayerMoveEvent e){
        Player p = e.getPlayer();

        if(!plugin.isAuthed(p)){
            e.setCancelled();
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = false)
    public void onItemDrop(PlayerDropItemEvent e){
        Player p = e.getPlayer();

        if(!plugin.isAuthed(p)){
            //p.sendMessage(TextFormat.RED + "You are not logged in.");
            e.setCancelled();
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = false)
    public void onPickupItem(InventoryPickupItemEvent e){
        Player p;
        if(e.getInventory().getHolder() instanceof Player){
            p = (Player) e.getInventory().getHolder();
        }else{
            return;
        }

        if(!plugin.isAuthed(p)){
            //p.sendMessage(TextFormat.RED + "You are not logged in.");
            e.setCancelled();
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = false)
    public void onPickup(InventoryPickupArrowEvent e){
        Player p;
        if(e.getInventory().getHolder() instanceof Player){
            p = (Player) e.getInventory().getHolder();
        }else{
            return;
        }

        if(!plugin.isAuthed(p)){
            //(p).sendMessage(TextFormat.RED + "You are not logged in.");
            e.setCancelled();
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = false)
    public void onHit(EntityDamageEvent e){
        Entity ent = e.getEntity();

        if(ent instanceof Player && !plugin.isAuthed((Player) ent)){
            //((Player) ent).sendMessage(TextFormat.RED + "You are not logged in.");
            e.setCancelled();
        }else if(e instanceof EntityDamageByEntityEvent){
            Entity killer = ((EntityDamageByEntityEvent) e).getDamager();

            if(killer instanceof Player && !plugin.isAuthed((Player) killer)){
                ((Player) killer).sendMessage(TextFormat.RED + "You are not logged in.");
                e.setCancelled();
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = false)
    public void onProjectileLaunch(ProjectileLaunchEvent e){
        Entity ent = e.getEntity().shootingEntity;

        if(ent instanceof Player && !plugin.isAuthed((Player) ent)){
            //((Player) ent).sendMessage(TextFormat.RED + "You are not logged in.");
            e.setCancelled();
        }
    }
}
