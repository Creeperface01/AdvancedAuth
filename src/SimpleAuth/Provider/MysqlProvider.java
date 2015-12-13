package SimpleAuth.Provider;


import cn.nukkit.Player;

import java.util.HashMap;
import java.util.Map;

public class MysqlProvider implements Provider{

    public Map<String, Object> registerPlayer(Player p, String hash){
        return new HashMap<>();
    }

    public Map<String, Object> getPlayer(Player p){
        return new HashMap<>();
    }

    public boolean isPlayerRegistered(Player p){
        return true;
    }

    public void unregisterPlayer(Player p){

    }
}
