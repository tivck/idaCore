package net.idalya.core.cooldown;

import java.util.UUID;

public class Cooldown {

    private final long system;
    private final int latence;
    private final String name;
    private final UUID user;

    public Cooldown(long system, int latence, String name, UUID user) {
        this.system = system;
        this.latence = latence;
        this.name = name;
        this.user = user;
    }

    private boolean can() {
        if(getSystem() < System.currentTimeMillis() + latence * 1000L) {
            return true;
        }

        return false;
    }

    public int getLatence() {
        return latence;
    }

    public long getSystem() {
        return system;
    }

    public String getName() {
        return name;
    }

    public UUID getUser() {
        return user;
    }
}
