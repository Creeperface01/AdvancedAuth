package SimpleAuth.Provider;

import cn.nukkit.Player;

import java.util.HashMap;
import java.util.Map;

public interface Provider{

    Map<String, Object> registerPlayer(Player p, String hash);

    Map<String, Object> getPlayer(Player p);

    boolean isPlayerRegistered(Player p);

    void unregisterPlayer(Player p);
}
