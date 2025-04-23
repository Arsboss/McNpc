package f3ximagine.npcsouttabottle;

import f3ximagine.npcsouttabottle.dev.events.Join;
import org.bukkit.plugin.java.JavaPlugin;

public final class NpcsOuttaBottle extends JavaPlugin {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new Join(),this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
