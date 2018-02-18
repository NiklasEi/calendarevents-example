package me.nikl.examplecalendareventsapi;

import me.nikl.calendarevents.APICalendarEvents;
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
public class Main extends JavaPlugin implements Listener{
    private APICalendarEvents api;
    private String uniqueEventLabel = UUID.randomUUID().toString();

    public void onEnable(){
        me.nikl.calendarevents.Main calendarEvents = (me.nikl.calendarevents.Main) Bukkit.getPluginManager().getPlugin("CalendarEvents");

        if(calendarEvents == null || !calendarEvents.isEnabled()){
            Bukkit.getLogger().log(Level.SEVERE, " CalendarEvents is not running!");
            return;
        }

        // get the time in two minutes in the format 'hh:mm'
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime timeInTwoMin = now.plusMinutes(2);
        String inTwoMin = (timeInTwoMin.getHour() < 10 ? "0" + timeInTwoMin.getHour() : String.valueOf(timeInTwoMin.getHour()))
                + ":" + ((timeInTwoMin.getMinute())< 10 ? "0" + (timeInTwoMin.getMinute()) : String.valueOf(timeInTwoMin.getMinute()));

        // get the API
        api = calendarEvents.getApi();

        // we will now add an event that will be fired 2 minutes after server start and at two different timings just to show you what is possible
        if(api.addEvent(uniqueEventLabel, "every day", inTwoMin + ", 17:45, 21:12")) {
            getLogger().info(" An event with the occasions: every day       and the timings: " + inTwoMin + ", 17:45, 21:12     was added");
        } else {
            getLogger().info(" Failed adding the event. The label might already be in use...");
        }

        Bukkit.getPluginManager().registerEvents(this, this);
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
