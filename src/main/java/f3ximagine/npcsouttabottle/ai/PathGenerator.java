package f3ximagine.npcsouttabottle.ai;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Set;

public class PathGenerator {
    public static Set<Location> getPath(Location start, Location end) {
        Set<Location> path = new HashSet<>();

        Vector direction = end.toVector().subtract(start.toVector()).normalize();
        double distance = start.distance(end);

        for (double i = 0; i <= distance; i++) {
            Vector step = direction.clone().multiply(i);
            Location currentLocation = start.clone().add(step);

            int x = currentLocation.getBlockX();
            int y = currentLocation.getBlockY();
            int z = currentLocation.getBlockZ();

            path.add(new Location(start.getWorld(), x, y, z));
        }

        return path;
    }
}
