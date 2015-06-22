package jp.co.wakwak.passmiru.Bus;

public class DestroyBus {
    final static String TAG = DestroyBus.class.getSimpleName();

    private boolean success;

    public DestroyBus(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }
}
