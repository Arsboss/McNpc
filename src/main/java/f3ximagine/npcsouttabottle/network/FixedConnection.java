package f3ximagine.npcsouttabottle.network;

import net.minecraft.network.Connection;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.PacketFlow;

import java.lang.reflect.Field;
import java.net.SocketAddress;

public class FixedConnection extends Connection {

    public FixedConnection(PacketFlow enumprotocoldirection) {
        super(enumprotocoldirection);
        channel = new EmptyChannel(null);
        address = new SocketAddress() {
            private static final long serialVersionUID = 8207338859896320185L;
        };
    }

    @Override
    public void setListener(PacketListener packetlistener) {
        Connection cinstance = new Connection(PacketFlow.SERVERBOUND);
        try {
            Field plist = Connection.class.getDeclaredField("q");
            plist.setAccessible(true);
            plist.set(cinstance, packetlistener);

            Field dlist = Connection.class.getDeclaredField("p");
            dlist.setAccessible(true);
            dlist.set(cinstance, packetlistener);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
