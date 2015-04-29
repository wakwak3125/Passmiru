package jp.co.wakwak.passmiru.Data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by RyoSakaguchi on 15/04/28.
 */
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

    public int getId() {
        return event_id;
    }

    public String getTitle() {
        return title;
    }
}
