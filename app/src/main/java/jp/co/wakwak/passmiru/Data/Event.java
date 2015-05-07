package jp.co.wakwak.passmiru.Data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Event {

    public int      event_id;
    public String   title;

    public Event(int id, String title) {
        this.event_id = id;
        this.title = title;
    }

    public Event() {

    }

    public int getEvent_id() {
        return event_id;
    }

    public void setEvent_id(int event_id) {
        this.event_id = event_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
