package jp.co.wakwak.passmiru.Bus;

public class SwipeFinishBus {
    final static String TAG = SwipeFinishBus.class.getSimpleName();

    private boolean success;

    public SwipeFinishBus(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

}
