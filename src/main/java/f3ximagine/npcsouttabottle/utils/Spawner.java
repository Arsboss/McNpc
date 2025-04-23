package f3ximagine.npcsouttabottle.utils;

import com.mojang.authlib.GameProfile;
import f3ximagine.npcsouttabottle.NpcsOuttaBottle;
import f3ximagine.npcsouttabottle.network.FixedConnection;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ClientInformation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Spawner {
    public static ServerPlayer ClientboundSpawner(String name){
        MinecraftServer server = MinecraftServer.getServer();
        ServerLevel level = server.overworld();
        UUID uuid = UUID.randomUUID();
        GameProfile npcprofile = new GameProfile(uuid, name);
        ClientInformation npcci = ClientInformation.createDefault();
        ServerPlayer npc = new ServerPlayer(server, level, npcprofile, npcci);
        npc.connection = new ServerGamePacketListenerImpl(server, new FixedConnection(PacketFlow.SERVERBOUND), npc, new CommonListenerCookie(npcprofile, 10, npcci));
        //level.addFreshEntity(npc); <-- don't uncomment this shit causes massive desync and i've wasted whole 6 hours just to find out that this fcking line caused it all
        //level.addNewPlayer(npc); <-- don't uncomment this shit causes massive desync and i've wasted whole 6 hours just to find out that this fcking line caused it all
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            ServerPlayer nmsPlayer = ((CraftPlayer) onlinePlayer).getHandle();
            //npc.setPos(onlinePlayer.getLocation().getX(),onlinePlayer.getLocation().getY(),onlinePlayer.getLocation().getZ());
            nmsPlayer.connection.send(new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER, npc));
            nmsPlayer.connection.send(new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.UPDATE_LISTED, npc));
            nmsPlayer.connection.send(npc.getAddEntityPacket());
        }


        NpcsOuttaBottle.getPlugin(NpcsOuttaBottle.class).getServer().getConsoleSender().sendMessage(npc.getStringUUID());

        return npc;
    }
}
