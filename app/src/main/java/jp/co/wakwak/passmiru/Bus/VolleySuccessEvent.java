package jp.co.wakwak.passmiru.Bus;

/**
 * Created by RyoSakaguchi on 15/05/06.
 */
public class VolleySuccessEvent {
    final static String TAG = VolleySuccessEvent.class.getSimpleName();

    private boolean success;

    public VolleySuccessEvent(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }
}
