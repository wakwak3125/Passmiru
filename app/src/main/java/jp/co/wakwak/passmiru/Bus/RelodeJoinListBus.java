package jp.co.wakwak.passmiru.Bus;

public class RelodeJoinListBus {
    final static String TAG = RelodeJoinListBus.class.getSimpleName();

    private boolean success;

    public RelodeJoinListBus(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }
}
