package net.idalya.core.server;

public enum ServerType {

    CLASSIC("CLASSIC"),
    LG("LG UHC");

    private String name;

    ServerType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static ServerType getByName(String name) {
        for(ServerType serverType : ServerType.values()) {
            if(serverType.getName().equals(name)) {
                return serverType;
            }
        }
        return null;
    }
}
