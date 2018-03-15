package me.nikl;

import me.nikl.calendarevents.CalendarEvents;
import me.nikl.calendarevents.CalendarEventsApi;
import me.nikl.calendarevents.CalendarEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.logging.Level;

/**
 * Example plugin that uses the API of CalendarEvents to schedule an event
 * that gets called at specific server times and dates.
 * A message is printed when the event is called.
 *
 * See CalendarEvents: https://github.com/NiklasEi/CalendarEvents
 */
public class CalendarEventsExample extends JavaPlugin implements Listener{
    private CalendarEventsApi api;
    private String uniqueEventLabel = UUID.randomUUID().toString();

    public void onEnable(){
        CalendarEvents calendarEvents = (CalendarEvents) Bukkit.getPluginManager().getPlugin("CalendarEvents");
        if(calendarEvents == null || !calendarEvents.isEnabled()){
            Bukkit.getLogger().log(Level.SEVERE, " CalendarEvents is not running!");
            return;
        }
        Bukkit.getPluginManager().registerEvents(this, this);
        registerCalendarEvent(calendarEvents);
    }

    private void registerCalendarEvent(CalendarEvents calendarEvents) {
        // get the time in two minutes in the format 'hh:mm'
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime timeInTwoMin = now.plusMinutes(2);
        // convert time in two min to hh:mm
        String inTwoMin = (timeInTwoMin.getHour() < 10 ? "0" + timeInTwoMin.getHour() : String.valueOf(timeInTwoMin.getHour()))
                + ":" + ((timeInTwoMin.getMinute())< 10 ? "0" + (timeInTwoMin.getMinute()) : String.valueOf(timeInTwoMin.getMinute()));
        // get the API
        api = calendarEvents.getApi();
        // we will now add an event that will be fired 2 minutes after server start and at two different timings
        // the last two parameters are the same, when you add this event in the configuration file in CalendarEvents (occasion and timing)
        if(api.addEvent(uniqueEventLabel, "every day", inTwoMin + ", 17:45, 21:12")) {
            getLogger().info(" An event with the occasions: every day       and the timings: " + inTwoMin + ", 17:45, 21:12     was added");
        } else {
            getLogger().info(" Failed adding the event. The label might already be in use...");
        }
    }

    @EventHandler
    public void onCustomEvent(CalendarEvent event){
        if(!event.getLabels().contains(uniqueEventLabel)){
            // not our event!
            return;
        }
        // it is our event!
        getLogger().info(" Example event was fired! Server time: " + event.getTime());
    }

    public void onDisable(){
        // You should remove your events here.
        api.removeEvent(uniqueEventLabel);
    }
}
