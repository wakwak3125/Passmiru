package jp.co.wakwak.passmiru.Bus;

/**
 * Created by RyoSakaguchi on 15/05/06.
 */
public class ListShowBus {
    final static String TAG = ListShowBus.class.getSimpleName();

    private boolean success;

    public ListShowBus(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }
}
