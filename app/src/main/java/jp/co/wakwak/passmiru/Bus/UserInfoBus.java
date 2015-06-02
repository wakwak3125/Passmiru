package jp.co.wakwak.passmiru.Bus;

public class UserInfoBus {
    final static String TAG = UserInfoBus.class.getSimpleName();

    private boolean success;
    private String userName;
    private String profileImgUrl;
    private String profileDescription;

    public UserInfoBus(boolean success, String userName, String profileImgUrl, String profileDescription) {
        this.success = success;
        this.userName = userName;
        this.profileImgUrl = profileImgUrl;
        this.profileDescription = profileDescription;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getProfileImgUrl() {
        return profileImgUrl;
    }

    public void setProfileImgUrl(String profileImgUrl) {
        this.profileImgUrl = profileImgUrl;
    }

    public String getProfileDescription() {
        return profileDescription;
    }

    public void setProfileDescription(String profileDescription) {
        this.profileDescription = profileDescription;
    }
}
