package SimpleAuth.Provider;

import SimpleAuth.SimpleAuth;
import cn.nukkit.Player;
import cn.nukkit.utils.Config;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

public class YamlProvider implements Provider{

    private SimpleAuth plugin;

    public YamlProvider(SimpleAuth plugin){
        this.plugin = plugin;
    }

    @Override
    public Map<String, Object> registerPlayer(Player p, String hash){
        String name = p.getName().toLowerCase().trim();

        File path = new File(plugin.getDataFolder() + "/players/" + name.charAt(0));
        path.mkdirs();
        try{
            PrintWriter writer = new PrintWriter(path + "/" + name + ".yml", "UTF-8");
            writer.close();
        }catch(IOException e){
            e.printStackTrace(System.err);
        }

        /*if(!path.exists()){
            path.mkdirs();
        }
        if(!new File(path + "/" + name + ".yml").isFile()){
            try{
                PrintWriter writer = new PrintWriter(path + "/" + name + ".yml", "UTF-8");
                writer.close();
            }catch(IOException e){
                e.printStackTrace(System.err);
            }
        }*/
        Config cfg = new Config(path + "/" + name + ".yml", Config.YAML);

        cfg.set("registerdate", System.currentTimeMillis() / 1000);
        cfg.set("logindate", System.currentTimeMillis() / 1000);
        cfg.set("lastip", null);
        cfg.set("hash", hash);
        cfg.save();

        return cfg.getAll();
    }

    @Override
    public Map<String, Object> getPlayer(Player p){
        String name = p.getName().toLowerCase().trim();

        if(name.equals("")){
            return null;
        }

        File path = new File(plugin.getDataFolder() + "/players/" + name.charAt(0) + "/" + name + ".yml");
        if(!path.exists()){
            return null;
        }

        return new Config(path.getPath(), Config.YAML).getAll();
    }

    @Override
    public boolean isPlayerRegistered(Player p){
        String name = p.getName().toLowerCase().trim();

        return new File(plugin.getDataFolder() + "/players/" + name.charAt(0) + "/" + name + ".yml").isFile();
    }

    @Override
    public void unregisterPlayer(Player p){
        String name = p.getName().toLowerCase().trim();

        new File(plugin.getDataFolder() + "/players/" + name.charAt(0) + "/" + name + ".yml").delete();
    }
}
