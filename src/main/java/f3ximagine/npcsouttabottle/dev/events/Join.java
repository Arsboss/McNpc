package f3ximagine.npcsouttabottle.dev.events;

import f3ximagine.npcsouttabottle.NpcsOuttaBottle;
import f3ximagine.npcsouttabottle.utils.Manager;
import f3ximagine.npcsouttabottle.utils.Spawner;
import f3ximagine.npcsouttabottle.utils.Syncer;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class Join implements Listener{
    ServerPlayer npc;
    public static HashMap<ServerPlayer, Player> npcmappings = new HashMap<>();
    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        new BukkitRunnable() {
            @Override
            public void run() {
                npc = Spawner.ClientboundSpawner("mynigga");
                npcmappings.put(npc, e.getPlayer());
                //Manager.createFollowPlayerTask(npc, e.getPlayer(), true);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        //Manager.pathfindToLoc(npc, e.getPlayer(), new Location(e.getPlayer().getWorld(), -18, 71, 13));
                        Manager.PlayerPathfinding(npc, e.getPlayer());
                        //Manager.ClientboundHurt(npc, e.getPlayer(), true);
                    }
                }.runTaskLater(NpcsOuttaBottle.getPlugin(NpcsOuttaBottle.class), 40L);
            }
        }.runTaskLater(NpcsOuttaBottle.getPlugin(NpcsOuttaBottle.class), 80L);

        new BukkitRunnable(){
            @Override
            public void run() {
                Syncer.broadcastNpcToPlayers(npc);
                Syncer.broadcastExistingNpcsToPlayer(e.getPlayer(), npcmappings);

            }
        }.runTaskLater(NpcsOuttaBottle.getPlugin(NpcsOuttaBottle.class), 80L);
    }
}
