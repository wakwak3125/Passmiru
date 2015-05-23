package jp.co.wakwak.passmiru.Bus;

/**
 * Created by RyoSakaguchi on 15/05/06.
 */
public class ResultListShowBus {
    final static String TAG = ResultListShowBus.class.getSimpleName();

    private boolean success;

    public ResultListShowBus(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }
}
