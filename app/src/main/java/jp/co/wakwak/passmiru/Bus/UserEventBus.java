package jp.co.wakwak.passmiru.Bus;

public class UserEventBus {

    final static String TAG = UserEventBus.class.getSimpleName();

    private boolean success;
    private int eventId;
    private String eventTitle;

    public UserEventBus(boolean success, int event_id, String title) {
        this.success = success;
        this.eventId = event_id;
        this.eventTitle = title;
    }

    public boolean isSuccess() {
        return success;
    }

    public int getEventId() {
        return eventId;
    }

    public String getEventTitle() {
        return eventTitle;
    }
}
