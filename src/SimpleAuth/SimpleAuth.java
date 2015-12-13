package SimpleAuth;

import SimpleAuth.Provider.MysqlProvider;
import SimpleAuth.Provider.Provider;
import SimpleAuth.Provider.YamlProvider;
import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.event.Listener;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.TextFormat;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class SimpleAuth extends PluginBase implements Listener{

    public HashMap<String, Player> unauthed = new HashMap<String, Player>();

    public Provider provider;

    public HashMap<String, String> data = new HashMap<String, String>();

    private EventListener eventListener;

    @Override
    public void onEnable(){
        saveResource("config.yml");
        initConfig();
        setProvider();
        eventListener = new EventListener(this);
        this.getServer().getPluginManager().registerEvents(this.eventListener, this);
    }

    @Override
    public void onDisable(){

    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        if(!(sender instanceof Player)){
            sender.sendMessage(TextFormat.RED + "This command works in game only");
            return false;
        }

        switch (cmd.getName().toLowerCase()){
            case "register":
                if(isAuthed((Player) sender) || provider.isPlayerRegistered((Player) sender) || provider.getPlayer((Player) sender) != null){
                    sender.sendMessage(TextFormat.RED + "You are already registered.");
                    break;
                }
                if(args.length != 2){
                    sender.sendMessage(TextFormat.RED + "Use /register <password> <password>");
                    break;
                }
                if(args[0].equals(args[1])){
                    sender.sendMessage(TextFormat.RED + "Both passwords must be equal!");
                    break;
                }

                String password = hash(sender.getName().toLowerCase(), args[0]);

                register((Player) sender, password);
                break;
            case "login":
                if(isAuthed((Player) sender)){
                    sender.sendMessage(TextFormat.RED + "You are already logged in.");
                    break;
                }
                if(!provider.isPlayerRegistered((Player) sender) || provider.getPlayer((Player) sender) == null){
                    sender.sendMessage(TextFormat.RED + "You are not registered!");
                    break;
                }
                if(args.length != 1){
                    sender.sendMessage(TextFormat.RED + "Use /login <password>");
                    break;
                }

                login((Player) sender, args[0]);
                break;
            case "changepassword":
                if(args.length != 3){
                    sender.sendMessage(TextFormat.RED + "Use /changepassword <old password> <new password> <new password>");
                    break;
                }
                break;
            case "unregister":
                if(args.length != 1){
                    sender.sendMessage(TextFormat.RED + "Use /unregister <password>");
                    break;
                }
                break;
        }

        return true;
    }

    public boolean register(Player p, String pass){
        if(provider.registerPlayer(p, pass) == null){
            return false;
        }
        p.sendMessage(TextFormat.GREEN + "You have been authenticated.");

        return true;
    }

    public boolean login(Player p, String pass){
        Map<String, Object> data = provider.getPlayer(p);
        if(data == null){
            p.sendMessage(TextFormat.RED + "Error during authentication.");
            return false;
        }
        if(data.get("hash") == null){
            p.sendMessage(TextFormat.RED + "Error during authentication.");
            return false;
        }
        if(!data.get("hash").equals(pass)){
            p.sendMessage(TextFormat.RED + "Incorrect password!");
            return false;
        }

        unauthed.remove(p.getName().toLowerCase());
        p.sendMessage(TextFormat.GREEN + "You have been authenticated.");
        return true;
    }

    public boolean unregister(Player p){
        return true;
    }

    public boolean changePassword(Player p, String oldPass, String newPass){
        return true;
    }

    public void initConfig(){
        Config cfg = new Config(this.getDataFolder() + "config.yml");

        data.put("dataProvider", cfg.get("dataProvider", "yaml").toLowerCase());
        data.put("dataProviderSettings", cfg.get("dataProviderSettings", "").toLowerCase());
        data.put("minPasswordLength", cfg.get("minPasswordLength", "6"));
        data.put("maxPasswordLength", cfg.get("maxPasswordLength", "20"));
        data.put("authenticateByLastUniqueId", cfg.get("authenticateByLastUniqueId", "false").toLowerCase());
        data.put("authenticateTimeout", cfg.get("authenticateTimeout", "120"));
    }

    private void setProvider(){
        switch(data.get("dataProvider")){
            case "mysql":
                this.provider = new MysqlProvider();
                break;
            default:
                this.provider = new YamlProvider(this);
                break;
        }
    }

    private String hash(String salt, String password){
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.reset();
            md.update((password + salt).getBytes());
            String one = new String(md.digest());

            md = MessageDigest.getInstance("whirlpool");
            md.reset();
            md.update((salt + password).getBytes());
            String two = new String(md.digest());

            byte[] sb = one.getBytes(), wb = two.getBytes();
            assert sb.length == wb.length;
            int[] output = new int[sb.length];
            for(int i = 0; i < output.length; i++){ //thx to PEMapModder
                output[i] = sb[i] ^ wb[i];
            }
            StringBuilder outb = new StringBuilder();
            for(int bite : output){
                outb.append(String.format("%2x", bite | 0)); // | 0: to convert to int without making it negative
            }

            return outb.toString();

            //output = String.format("%21X", Long.parseLong(one + two, 2));
        }catch (NoSuchAlgorithmException e) {
            e.printStackTrace(System.err);
            return null;
        }
    }

    public boolean isAuthed(Player p){
        return !unauthed.containsKey(p.getName().toLowerCase());
    }
}
