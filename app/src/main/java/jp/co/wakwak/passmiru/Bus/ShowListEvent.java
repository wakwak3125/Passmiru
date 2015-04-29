package jp.co.wakwak.passmiru.Bus;

/**
 * Created by RyoSakaguchi on 15/04/29.
 */
public class ShowListEvent {

    private boolean success;

    public ShowListEvent(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }
}
