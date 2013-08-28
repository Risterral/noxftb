package com.gmail.risterral.openingeventplugin.openingevent;

import com.gmail.risterral.openingeventplugin.OpeningEventPlugin;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.List;

public class OpeningEventsPresenter {
    private final JavaPlugin plugin;
    private final HashMap<Location, OpeningEvent> openingEvents;

    public OpeningEventsPresenter(JavaPlugin plugin) {
        this.plugin = plugin;
        this.openingEvents = ((OpeningEventPlugin) plugin).getDatabasePresenter().getOpeningEvents();
    }

    public OpeningEvent getOpeningEventById(Integer id) {
        for (Location key : openingEvents.keySet()) {
            if (id == openingEvents.get(key).getId()) {
                return openingEvents.get(key);
            }
        }
        return null;
    }

    public OpeningEvent getOpeningEventByName(String name) {
        for (Location key : openingEvents.keySet()) {
            if (name.equals(openingEvents.get(key).getName())) {
                return openingEvents.get(key);
            }
        }
        return null;
    }

    public HashMap<Location, OpeningEvent> getOpeningEvents() {
        return openingEvents;
    }
}
