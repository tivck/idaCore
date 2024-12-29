package net.idalya.core.server;

import net.idalya.core.Core;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class IdaServer {

    private ServerStatus status;
    private final String host;
    private final ServerType serverType;
    private final int port;
    private final int slots;
    private final List<UUID> players;

    public IdaServer( ServerStatus status, String host, ServerType serverType, int port, int slots) {
        this.status = status;
        this.host = host;
        this.serverType = serverType;
        this.port = port;
        this.slots = slots;
        this.players = new ArrayList<>();
    }

    public Document toBson() {
        return new Document().append("status", this.status.name())
                .append("host", this.host)
                .append("type", this.serverType.getName())
                .append("port", this.port)
                .append("slots", this.slots);
    }


    public ServerStatus getStatus() {
        return status;
    }

    public String getHost() {
        return host;
    }

    public ServerType getServerType() {
        return serverType;
    }

    public int getPort() {
        return port;
    }

    public List<UUID> getPlayers() {
        return players;
    }

    public int getSlots() {
        return slots;
    }

    public void setStatus(ServerStatus status) {
        this.status = status;
        Core.getInstance().getServerManager().addOrRefreshServer(this);
    }
}
