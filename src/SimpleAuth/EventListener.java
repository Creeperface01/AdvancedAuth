package SimpleAuth;

import cn.nukkit.Player;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.block.BlockPlaceEvent;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.utils.TextFormat;

import java.io.Console;

public class EventListener implements Listener{

    private SimpleAuth plugin;

    public EventListener(SimpleAuth plugin){
        this.plugin = plugin;
    }

    public void onBlockBreak(BlockBreakEvent e){
        Player p = e.getPlayer();
        p.sendMessage("place");

        if(!plugin.isAuthed(p)){
            p.sendMessage(TextFormat.RED + "You are not logged in.");
            e.setCancelled();
        }
    }

    public void onBlockPlace(BlockPlaceEvent e){
        Player p = e.getPlayer();

        if(!plugin.isAuthed(p)){
            p.sendMessage(TextFormat.RED + "You are not logged in.");
            e.setCancelled();
        }
    }

    public void onJoin(PlayerJoinEvent e){
        Player p = e.getPlayer();

        if(plugin.provider.isPlayerRegistered(p)){
            p.sendMessage(TextFormat.YELLOW + "This account is registered\n" + TextFormat.GREEN + "Please login using /login <password>");
        }else{
            p.sendMessage(TextFormat.GREEN + "Please register using /register <password> <password>");
        }

        plugin.unauthed.put(p.getName().toLowerCase(), p);
    }

    public void onQuit(PlayerQuitEvent e){
        Player p = e.getPlayer();

        if(!plugin.isAuthed(p)){
            plugin.unauthed.remove(p.getName().toLowerCase());
        }
    }
}
