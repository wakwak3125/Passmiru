package jp.co.wakwak.passmiru.Bus;

/**
 * Created by RyoSakaguchi on 15/05/06.
 */
public class PrefcSetResultBus {
    final static String TAG = PrefcSetResultBus.class.getSimpleName();

    private boolean success;

    public PrefcSetResultBus(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }
}
