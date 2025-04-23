package f3ximagine.npcsouttabottle.utils;

import f3ximagine.npcsouttabottle.NpcsOuttaBottle;
import f3ximagine.npcsouttabottle.ai.PathGenerator;
import f3ximagine.npcsouttabottle.ai.Raycaster;
import net.minecraft.network.protocol.game.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Manager {
    public static HashSet<ServerPlayer> npcKdBlocked = new HashSet<>();

    public static void ClientboundTp(ServerPlayer npc, Player p1, double x1, double y1, double z1){
        ClientboundTeleportEntityPacket tp = new ClientboundTeleportEntityPacket(npc);
        try {
            Field id = tp.getClass().getDeclaredField("a");
            Field x = tp.getClass().getDeclaredField("b");
            Field y = tp.getClass().getDeclaredField("c");
            Field z = tp.getClass().getDeclaredField("d");
            Field yRot = tp.getClass().getDeclaredField("e");
            Field xRot = tp.getClass().getDeclaredField("f");
            Field onGround = tp.getClass().getDeclaredField("g");
            id.setAccessible(true);
            x.setAccessible(true);
            y.setAccessible(true);
            z.setAccessible(true);
            yRot.setAccessible(true);
            xRot.setAccessible(true);
            onGround.setAccessible(true);

            id.set(tp, npc.getBukkitEntity().getEntityId());
            x.set(tp, x1);
            y.set(tp, y1);
            z.set(tp, z1);
            yRot.set(tp, (byte)0x00);
            xRot.set(tp, (byte)0x00);
            onGround.set(tp, true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        ServerPlayer plrNms = ((CraftPlayer) p1).getHandle();
        Bukkit.getServer().getConsoleSender().sendMessage(String.valueOf(tp.getX()));
        Bukkit.getServer().getConsoleSender().sendMessage(String.valueOf(tp.getY()));
        Bukkit.getServer().getConsoleSender().sendMessage(String.valueOf(tp.getZ()));
        plrNms.connection.send(tp);
    }

    public static void ClientboundHeadRot(ServerPlayer npc, Player pl, byte headYaw, boolean multicast){
        ServerPlayer plrNms = ((CraftPlayer) pl).getHandle();
        plrNms.connection.send(new ClientboundRotateHeadPacket(npc, headYaw));
        if(multicast){
            Syncer.multicastPacketSender(new ClientboundRotateHeadPacket(npc, headYaw), pl);
        }
    }

    public static void ClientboundMove(ServerPlayer npc, Player pl, short x1, short y1, short z1, byte yaw, byte pitch, boolean onGround, boolean multicast){
        ServerPlayer plrNms = ((CraftPlayer) pl).getHandle();
        plrNms.connection.send(new ClientboundMoveEntityPacket.PosRot(npc.getBukkitEntity().getEntityId(), x1, y1, z1, yaw, pitch, onGround));
        if(multicast){
            Syncer.multicastPacketSender(new ClientboundMoveEntityPacket.PosRot(npc.getBukkitEntity().getEntityId(), x1, y1, z1, yaw, pitch, onGround), pl);
        }
    }

    public static boolean ClientboundMove1BlockWest(ServerPlayer npc, Player pl, boolean multicast){
        for (int y = 0; y<4; y++){
            new BukkitRunnable() {
                @Override
                public void run() {
                    Manager.ClientboundMove(npc, pl, (short)-1024, (short)0, (short)0, (byte)0, (byte)0, true, true);
                    if(multicast){
                        Syncer.multicastPacketSender(new ClientboundMoveEntityPacket.PosRot(npc.getBukkitEntity().getEntityId(), (short)-1024, (short)0, (short)0, (byte)0, (byte)0, true), pl);
                    }
                }
            }.runTaskLater(NpcsOuttaBottle.getPlugin(NpcsOuttaBottle.class), y);
        }
        return true;
    }

    public static boolean ClientboundMove1BlockEast(ServerPlayer npc, Player pl, boolean multicast){
        for (int y = 0; y<4; y++){
            new BukkitRunnable() {
                @Override
                public void run() {
                    Manager.ClientboundMove(npc, pl, (short)1024, (short)0, (short)0, (byte)0, (byte)0, true, true);
                    if(multicast){
                        Syncer.multicastPacketSender(new ClientboundMoveEntityPacket.PosRot(npc.getBukkitEntity().getEntityId(), (short)1024, (short)0, (short)0, (byte)0, (byte)0, true), pl);
                    }
                }
            }.runTaskLater(NpcsOuttaBottle.getPlugin(NpcsOuttaBottle.class), y);
        }
        return true;
    }

    public static boolean ClientboundMove1BlockNorth(ServerPlayer npc, Player pl, boolean multicast){
        for (int y = 0; y<4; y++){
            new BukkitRunnable() {
                @Override
                public void run() {
                    Manager.ClientboundMove(npc, pl, (short)0, (short)0, (short)-1024, (byte)0, (byte)0, true, true);
                    if(multicast){
                        Syncer.multicastPacketSender(new ClientboundMoveEntityPacket.PosRot(npc.getBukkitEntity().getEntityId(), (short)0, (short)0, (short)-1024, (byte)0, (byte)0, true), pl);
                    }
                }
            }.runTaskLater(NpcsOuttaBottle.getPlugin(NpcsOuttaBottle.class), y);
        }
        return true;
    }

    public static boolean ClientboundMove1BlockSouth(ServerPlayer npc, Player pl, boolean multicast){
        for (int y = 0; y<4; y++){
            new BukkitRunnable() {
                @Override
                public void run() {
                    Manager.ClientboundMove(npc, pl, (short)0, (short)0, (short)1024, (byte)0, (byte)0, true, true);
                    if(multicast){
                        Syncer.multicastPacketSender(new ClientboundMoveEntityPacket.PosRot(npc.getBukkitEntity().getEntityId(), (short)0, (short)0, (short)1024, (byte)0, (byte)0, true), pl);
                    }
                }
            }.runTaskLater(NpcsOuttaBottle.getPlugin(NpcsOuttaBottle.class), y);
        }
        return true;
    }

    public static boolean ClientboundFall1Block(ServerPlayer npc, Player pl, boolean multicast){
        for (int y = 0; y<4; y++){
            new BukkitRunnable() {
                @Override
                public void run() {
                    Manager.ClientboundMove(npc, pl, (short)0, (short)-1024, (short)0, (byte)0, (byte)0, true, true);
                    if(multicast){
                        Syncer.multicastPacketSender(new ClientboundMoveEntityPacket.PosRot(npc.getBukkitEntity().getEntityId(), (short)0, (short)-1024, (short)0, (byte)0, (byte)0, true), pl);
                    }
                }
            }.runTaskLater(NpcsOuttaBottle.getPlugin(NpcsOuttaBottle.class), y);
        }
        return true;
    }

    public static boolean ClientboundFall2Block(ServerPlayer npc, Player pl, boolean multicast){
        for (int y = 0; y<4; y++){
            new BukkitRunnable() {
                @Override
                public void run() {
                    Manager.ClientboundMove(npc, pl, (short)0, (short)-2048, (short)0, (byte)0, (byte)0, true, true);
                    if(multicast){
                        Syncer.multicastPacketSender(new ClientboundMoveEntityPacket.PosRot(npc.getBukkitEntity().getEntityId(), (short)0, (short)-2048, (short)0, (byte)0, (byte)0, true), pl);
                    }
                }
            }.runTaskLater(NpcsOuttaBottle.getPlugin(NpcsOuttaBottle.class), y);
        }
        return true;
    }

    public static boolean ClientboundJumpNoDown(ServerPlayer npc, Player pl, boolean multicast){
        for (int y = 0; y<4; y++){
            new BukkitRunnable() {
                @Override
                public void run() {
                    Manager.ClientboundMove(npc, pl, (short)0, (short)1024, (short)0, (byte)0, (byte)0, true, true);
                    if(multicast){
                        Syncer.multicastPacketSender(new ClientboundMoveEntityPacket.PosRot(npc.getBukkitEntity().getEntityId(), (short)0, (short)1024, (short)0, (byte)0, (byte)0, true), pl);
                    }
                }
            }.runTaskLater(NpcsOuttaBottle.getPlugin(NpcsOuttaBottle.class), y);
        }
        return true;
    }

    public static void ClientboundArmSwing(ServerPlayer npc, Player pl, boolean multicast){
        ServerPlayer plrNms = ((CraftPlayer) pl).getHandle();
        plrNms.connection.send(new ClientboundAnimatePacket((Entity)npc, 0));
        if(multicast){
            Syncer.multicastPacketSender(new ClientboundAnimatePacket((Entity)npc, 0), pl);
        }
    }

    public static void ClientboundHurt(ServerPlayer npc, Player pl, boolean multicast){
        ServerPlayer plrNms = ((CraftPlayer) pl).getHandle();
        plrNms.connection.send(new ClientboundHurtAnimationPacket(npc));
        if(multicast){
            Syncer.multicastPacketSender(new ClientboundHurtAnimationPacket(npc), pl);
        }
    }

    public static void TakeKB(ServerPlayer npc, Player pl, Vector movement, boolean multicast){
        double XCalc = movement.clone().normalize().getX() * 256;
        double ZCalc = movement.clone().normalize().getZ() * 256;
        Location afterKB = npc.getBukkitEntity().getLocation().clone().add(movement.clone().normalize().setY(0));

        if(!npcKdBlocked.contains(npc)){
            npcKdBlocked.add(npc);
            for(int y = 0; y<8; y++){
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        Manager.ClientboundMove(npc, pl, (short)XCalc, (short)0, (short)ZCalc, (byte)0, (byte)0, true, true);
                        if(multicast){
                            Manager.ClientboundMove(npc, pl, (short)XCalc, (short)0, (short)ZCalc, (byte)0, (byte)0, true, true);
                        }
                    }
                }.runTaskLater(NpcsOuttaBottle.getPlugin(NpcsOuttaBottle.class), y);
            }
            new BukkitRunnable() {
                @Override
                public void run() {
                    //Main.getPlugin(Main.class).getServer().broadcastMessage(String.valueOf(movement));
                    npc.getBukkitEntity().teleport(afterKB);
                    //Main.getPlugin(Main.class).getServer().broadcastMessage(String.valueOf(npc.getBukkitEntity().getLocation().getX())+ " dasa " + String.valueOf(npc.getBukkitEntity().getLocation().getZ()));
                    npcKdBlocked.remove(npc);
                }
            }.runTaskLater(NpcsOuttaBottle.getPlugin(NpcsOuttaBottle.class), 8L);
        }
    }

    public static int createFollowPlayerTask(ServerPlayer npc, Player pl, boolean multicast){
        BukkitTask bt = new BukkitRunnable() {
            @Override
            public void run() {
                double deltaX = pl.getLocation().getX() - npc.getBukkitEntity().getLocation().getX();
                double deltaY = pl.getLocation().getY() - npc.getBukkitEntity().getLocation().getY();
                double deltaZ = pl.getLocation().getZ() - npc.getBukkitEntity().getLocation().getZ();
                ClientboundMove(npc, pl, (short) (deltaX), (short) (deltaY), (short) (deltaZ), (byte)0, (byte)0, true, multicast);
                if(!pl.isOnline()){
                    cancel();
                }
                npc.getBukkitEntity().teleport(pl.getLocation());
            }
        }.runTaskTimer(NpcsOuttaBottle.getPlugin(NpcsOuttaBottle.class), 0L, 1L);

        return bt.getTaskId();
    }

    public static void pathfindToLoc(ServerPlayer npc, Player invoker, Location target){
        Raycaster caster = new Raycaster();
        caster.cast(npc, target, invoker);
    }

    public static BukkitTask npcAutoAttackSetup(ServerPlayer npc, Player pl){
        BukkitTask tk = new BukkitRunnable() {
            @Override
            public void run() {

            }
        }.runTaskTimer(NpcsOuttaBottle.getPlugin(NpcsOuttaBottle.class), 0L, 1L);
        return tk;
    }

    public static BukkitTask PlayerPathfinding(ServerPlayer npc, Player pl){
        Raycaster caster = new Raycaster();
        BukkitTask tk = new BukkitRunnable() {
            @Override
            public void run() {
                if(!pl.isOnline()){
                    this.cancel();
                }
                Set<Location> movs = PathGenerator.getPath(npc.getBukkitEntity().getLocation(), pl.getLocation());
                Iterator<Location> fmov = movs.iterator();
                caster.cast(npc, fmov.next(), pl);
            }
        }.runTaskTimer(NpcsOuttaBottle.getPlugin(NpcsOuttaBottle.class), 0L, 5L);
        return tk;
    }
}
