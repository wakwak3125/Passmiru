package jp.co.wakwak.passmiru.Bus;

public class HideFragmentBus {
    final static String TAG = HideFragmentBus.class.getSimpleName();

    private boolean success;

    public HideFragmentBus(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }
}
