package jp.co.wakwak.passmiru.Bus;

public class RelodeCreatedListBus {
    final static String TAG = RelodeCreatedListBus.class.getSimpleName();

    private boolean success;

    public RelodeCreatedListBus(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }
}
