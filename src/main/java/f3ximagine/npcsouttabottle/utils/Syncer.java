package f3ximagine.npcsouttabottle.utils;

import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class Syncer {
    //private static HashMap<ServerPlayer, Player> synceds = Join.npcmappings;

    public static void multicastPacketSender(Packet packet, Player ignore){
        for(Player p: Bukkit.getOnlinePlayers()){
            if(!p.getName().equals(ignore.getName())){
                ServerPlayer nmsPlayer = ((CraftPlayer) p).getHandle();
                nmsPlayer.connection.send(packet);
            }
        }
    }

    public static void broadcastExistingNpcsToPlayer(Player p, HashMap<ServerPlayer, Player> synceds){
        for (Map.Entry<ServerPlayer, Player> entry : synceds.entrySet()){
            ServerPlayer nmsPlayer = ((CraftPlayer) p).getHandle();
            nmsPlayer.connection.send(new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER, entry.getKey()));
            nmsPlayer.connection.send(new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.UPDATE_LISTED, entry.getKey()));
            nmsPlayer.connection.send(entry.getKey().getAddEntityPacket());
        }
    }

    // one time sync for npc to appear to other players
    public static void broadcastNpcToPlayers(ServerPlayer npc){
        for(Player p : Bukkit.getOnlinePlayers()){
            ServerPlayer plrNms = ((CraftPlayer) p).getHandle();
            plrNms.connection.send(new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER, npc));
            plrNms.connection.send(new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.UPDATE_LISTED, npc));
            plrNms.connection.send(npc.getAddEntityPacket());
        }
    }
}
