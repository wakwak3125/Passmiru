package jp.co.wakwak.passmiru.Data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Event {

    public int      event_id;
    public String   title;
    public String   event_url;

    public Event(int id, String title, String event_url) {
        this.event_id = id;
        this.title = title;
        this.event_url = event_url;
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

    public String getEvent_url() {
        return event_url;
    }

    public void setEvent_url(String event_url) {
        this.event_url = event_url;
    }
}
