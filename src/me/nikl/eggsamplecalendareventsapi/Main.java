package me.nikl.eggsamplecalendareventsapi;

import me.nikl.calendarevents.APICalendarEvents;
import me.nikl.calendarevents.CalendarEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.ZonedDateTime;
import java.util.logging.Level;

/**
 * Example plugin that uses CalendarEvents API to schedule real time events and runs code on them.
 */
public class Main extends JavaPlugin implements Listener{

    private APICalendarEvents api;


    public void onEnable(){
        /*
         CalendarEvents is set as a dependency in the plugin.yml
         So if it is running on this server it should be enabled now
          */
        me.nikl.calendarevents.Main calendarEvents = (me.nikl.calendarevents.Main) Bukkit.getPluginManager().getPlugin("CalendarEvents");

        if(calendarEvents == null || !calendarEvents.isEnabled()){
            Bukkit.getLogger().log(Level.SEVERE, "[EggsampleCalendarEventsAPI] CalendarEvents is not running!");
            return;
        }

        // get the time in two minutes in the format 'hh:mm'
        ZonedDateTime now = ZonedDateTime.now();
        String inTwoMin = now.getHour() < 10 ? "0" + now.getHour() : String.valueOf(now.getHour()) + ":" + ((now.getMinute() + 2)< 10 ? "0" + (now.getMinute() + 2) : String.valueOf(now.getMinute() + 2));

        // get the API
        api = calendarEvents.getApi();

        // we will now add an event that will be fired 2 minutes later and at two different timings just to show you what is possible
        if(api.addEvent("OurTestLabelShouldBeUnique", "every day", inTwoMin + ", 17:45, 21:12")) {
            Bukkit.getConsoleSender().sendMessage("[EggsampleCalendarEventsAPI] an event with the occasions: every day       and the timings: " + inTwoMin + ", 17:45, 21:12     was added");
        } else {
            Bukkit.getConsoleSender().sendMessage("[EggsampleCalendarEventsAPI] failed adding the event. Is the label already in use?");
        }

        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onCustomEvent(CalendarEvent event){
        if(!event.getLabels().contains("OurTestLabelShouldBeUnique")){
            // not our event!
            return;
        }
        // it is our event!
        Bukkit.getConsoleSender().sendMessage("[EggsampleCalendarEventsAPI] Example event was fired! Servertime: " + event.getTime());
    }


    public void onDisable(){

    }

}
