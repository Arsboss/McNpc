package f3ximagine.npcsouttabottle.ai;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import f3ximagine.npcsouttabottle.NpcsOuttaBottle;
import f3ximagine.npcsouttabottle.utils.Manager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.HashSet;

public class Raycaster {
    private HashSet<Material> forwarded = new HashSet<>();
    private HashSet<Material> downwarded = new HashSet<>();

    boolean done = false;

    public void cast(ServerPlayer npc, Location target, Player invoker){
        forwarded.clear();
        downwarded.clear();
        /*for(int x = 0; x<2; x++){
            for(int y = 0; y<2; y++){
                for(int z = 0; z<2; z++){
                    Location forward = npc.getBukkitEntity().getLocation().clone().add(x, y, z);
                    Location downward = npc.getBukkitEntity().getLocation().clone().subtract(x, y, z);

                    forwarded.add(forward.getBlock().getType());
                    downwarded.add(downward.getBlock().getType());
                }
            }
        }
         */
        Location loc = npc.getBukkitEntity().getLocation();
        Vector direction = loc.getDirection().normalize();
        for (int i = 0; i < 2; i++) {
            Vector dir2 = direction.setY(0).multiply(-1);
            Location blockLocation = loc.clone().add(dir2.setY(direction.getY()+i));
            if(!blockLocation.getBlock().isPassable()){
                forwarded.add(blockLocation.getBlock().getType());
            }
        }
        predict(forwarded, downwarded, npc, target, invoker);
    }

    public void predict(HashSet<Material> forward, HashSet<Material> downward, ServerPlayer npc, Location target, Player invoker){
        Object[] fwdata = forward.toArray();
        for (Object o : fwdata){
            System.out.println(o);
        }
        try{
            if(fwdata[0] == Material.AIR && fwdata[1] == Material.AIR){
                calcMovementAndPackets(npc, target, 0, invoker);
            }else if(fwdata[0] != Material.AIR){
                Manager.ClientboundJumpNoDown(npc, invoker, false);
                npc.getBukkitEntity().teleport(npc.getBukkitEntity().getLocation().clone().add(0.0, 1.0, 0.0));
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        calcMovementAndPackets(npc, target, 0, invoker);
                    }
                }.runTaskLater(NpcsOuttaBottle.getPlugin(NpcsOuttaBottle.class), 8L);
            }else{
                NpcsOuttaBottle.getPlugin(NpcsOuttaBottle.class).getServer().broadcastMessage("predikt failed");
            }
        }catch (ArrayIndexOutOfBoundsException e){
            NpcsOuttaBottle.getPlugin(NpcsOuttaBottle.class).getServer().broadcastMessage("ok to proceed (empty environment)");
            calcMovementAndPackets(npc, target, 0, invoker);
        }

    }

    public static boolean deviationCheck(double num1, double num2, double deviation) {
        return Math.abs(num1 - num2) <= deviation;
    }

    public void calcMovementAndPackets(ServerPlayer current, Location target, int action, Player invoker){
        //Main.getPlugin(Main.class).getServer().broadcastMessage(String.valueOf(current.getBukkitEntity().getFacing()));

        if(deviationCheck(target.getX(), current.getBukkitEntity().getLocation().getX(), 3.0)){
            if(deviationCheck(target.getY(),current.getBukkitEntity().getLocation().getY(), 300.0)){
                if(deviationCheck(target.getZ(), current.getBukkitEntity().getLocation().getZ(), 3.0)){
                    done = true;
                }
            }
        }

        if(current.getBukkitEntity().getWorld().getBlockAt(current.getBukkitEntity().getLocation().clone().subtract(0,1,0)).getType().equals(Material.AIR) && !current.getBukkitEntity().getWorld().getBlockAt(current.getBukkitEntity().getLocation().clone().subtract(0.0, 2.0, 0.0)).getType().equals(Material.AIR)){
            Manager.ClientboundFall1Block(current, invoker, false);
            new BukkitRunnable() {
                @Override
                public void run() {
                    Location newloc = current.getBukkitEntity().getLocation().clone();
                    current.getBukkitEntity().teleport(newloc.clone().add(0.0, -1.0, 0.0));
                }
            }.runTaskLater(NpcsOuttaBottle.getPlugin(NpcsOuttaBottle.class), 4L);
        }
        if(current.getBukkitEntity().getWorld().getBlockAt(current.getBukkitEntity().getLocation().clone().subtract(0,1,0)).getType().equals(Material.AIR) && current.getBukkitEntity().getWorld().getBlockAt(current.getBukkitEntity().getLocation().clone().subtract(0.0, 2.0, 0.0)).getType().equals(Material.AIR)){
            //.getPlugin(Main.class).getServer().broadcastMessage("bro is falling hold on");
            Manager.ClientboundFall2Block(current, invoker, false);
            new BukkitRunnable() {
                @Override
                public void run() {
                    Location newloc = current.getBukkitEntity().getLocation().clone();
                    current.getBukkitEntity().teleport(newloc.clone().add(0.0, -2.0, 0.0));
                }
            }.runTaskLater(NpcsOuttaBottle.getPlugin(NpcsOuttaBottle.class), 4L);
        }
        /*if(target.getY() > current.getBukkitEntity().getLocation().getY() && deviationCheck(target.getX(), current.getBukkitEntity().getLocation().getX(), 5.0) && deviationCheck(target.getZ(), current.getBukkitEntity().getLocation().getZ(), 5.0)){
            if(!deviationCheck(target.getY(), current.getBukkitEntity().getLocation().getY(), 1.0)){
                //Main.getPlugin(Main.class).getServer().broadcastMessage("bro is scaffolding hold on");
                Manager.ClientboundJumpNoDown(current, invoker, false);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        Location newloc = current.getBukkitEntity().getLocation().clone();
                        current.getBukkitEntity().teleport(newloc.clone().add(0.0, 1.0, 0.0));
                        current.getBukkitEntity().getWorld().getBlockAt(current.getBukkitEntity().getLocation().clone().subtract(0,1,0)).setType(Material.WHITE_WOOL);
                    }
                }.runTaskLater(NpcsOuttaBottle.getPlugin(NpcsOuttaBottle.class), 8L);
            }
        }

         */
        if (target.getX() < current.getBukkitEntity().getLocation().getX()){
            if(!deviationCheck(target.getX(), current.getBukkitEntity().getLocation().getX(), 1.0)){
                Manager.ClientboundMove1BlockWest(current, invoker, false);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        Location newloc = current.getBukkitEntity().getLocation().clone();
                        if(newloc.getYaw() != 90f){
                            //Manager.ClientboundMove(current, invoker, (short)0, (short)0, (short)0, (byte)90, (byte)0, true, true);
                            Manager.ClientboundHeadRot(current, invoker, (byte)90, false);
                            newloc.setYaw(90f);
                        }
                        current.getBukkitEntity().teleport(newloc.clone().add(-1, 0.0, 0.0));
                        //Bukkit.getServer().getWorlds().get(0).getBlockAt(current.getBukkitEntity().getLocation().clone().subtract(0,1,0)).setType(Material.WHITE_WOOL);
                    }
                }.runTaskLater(NpcsOuttaBottle.getPlugin(NpcsOuttaBottle.class), 4L);
            }
        }
        if (target.getX() > current.getBukkitEntity().getLocation().getX()){
            if(!deviationCheck(target.getX(), current.getBukkitEntity().getLocation().getX(), 1.0)){
                Manager.ClientboundMove1BlockEast(current, invoker, false);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        Location newloc = current.getBukkitEntity().getLocation().clone();
                        if(newloc.getYaw() != -90f){
                            //Manager.ClientboundMove(current, invoker, (short)0, (short)0, (short)0, (byte)-90, (byte)0, true, true);
                            Manager.ClientboundHeadRot(current, invoker, (byte)-90, false);
                            newloc.setYaw(-90f);
                        }
                        current.getBukkitEntity().teleport(newloc.clone().add(1, 0.0, 0.0));
                        //Bukkit.getServer().getWorlds().get(0).getBlockAt(current.getBukkitEntity().getLocation().clone().subtract(0,1,0)).setType(Material.WHITE_WOOL);
                    }
                }.runTaskLater(NpcsOuttaBottle.getPlugin(NpcsOuttaBottle.class), 4L);
            }
        }
        if(target.getZ() < current.getBukkitEntity().getLocation().getZ()){
            if(!deviationCheck(target.getZ(), current.getBukkitEntity().getLocation().getZ(), 1.0)){
                Manager.ClientboundMove1BlockNorth(current, invoker, false);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        Location newloc = current.getBukkitEntity().getLocation().clone();
                        if(newloc.getYaw() != 180f){
                            //Manager.ClientboundMove(current, invoker, (short)0, (short)0, (short)0, (byte)-90, (byte)0, true, true);
                            Manager.ClientboundHeadRot(current, invoker, (byte)180, false);
                            newloc.setYaw(180f);
                        }
                        current.getBukkitEntity().teleport(newloc.clone().add(0, 0.0, -1));
                        //Bukkit.getServer().getWorlds().get(0).getBlockAt(current.getBukkitEntity().getLocation().clone().subtract(0,1,0)).setType(Material.WHITE_WOOL);
                    }
                }.runTaskLater(NpcsOuttaBottle.getPlugin(NpcsOuttaBottle.class), 4L);
            }
        }
        if(target.getZ() > current.getBukkitEntity().getLocation().getZ()){
            if(!deviationCheck(target.getZ(), current.getBukkitEntity().getLocation().getZ(), 1.0)){
                Manager.ClientboundMove1BlockSouth(current, invoker, false);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        Location newloc = current.getBukkitEntity().getLocation().clone();
                        if(newloc.getYaw() != -180f){
                            //Manager.ClientboundMove(current, invoker, (short)0, (short)0, (short)0, (byte)-90, (byte)0, true, true);
                            Manager.ClientboundHeadRot(current, invoker, (byte)-180, false);
                            newloc.setYaw(-180f);
                        }
                        current.getBukkitEntity().teleport(newloc.clone().add(0, 0.0, 1));
                        //Bukkit.getServer().getWorlds().get(0).getBlockAt(current.getBukkitEntity().getLocation().clone().subtract(0,1,0)).setType(Material.WHITE_WOOL);
                    }
                }.runTaskLater(NpcsOuttaBottle.getPlugin(NpcsOuttaBottle.class), 4L);
            }
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                //Manager.ClientboundTp(current, invoker, target.getX(), target.getY(), target.getZ());
            }
        }.runTaskLater(NpcsOuttaBottle.getPlugin(NpcsOuttaBottle.class), 20L);
        if(!done){
            new BukkitRunnable() {
                @Override
                public void run() {
                    cast(current, target, invoker);
                }
            }.runTaskLater(NpcsOuttaBottle.getPlugin(NpcsOuttaBottle.class), 4L);
        }
    }
}