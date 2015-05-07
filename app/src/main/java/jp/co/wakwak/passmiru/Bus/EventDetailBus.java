package jp.co.wakwak.passmiru.Bus;

public class EventDetailBus {
    final static String TAG = EventDetailBus.class.getSimpleName();

    private boolean success;
    private int eventId;
    private String eventTitle;
    private String description;

    public EventDetailBus(boolean success, int eventId, String eventTitle, String description) {
        this.success = success;
        this.eventId = eventId;
        this.eventTitle = eventTitle;
        this.description = description;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
