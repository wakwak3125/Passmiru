package jp.co.wakwak.passmiru.Bus;

public class UserEventDetailBus {
    final static String TAG = UserEventDetailBus.class.getSimpleName();

    private boolean success;
    private int eventId;
    private String eventTitle;
    private String description;
    private String imgUrl;
    private String updated_at;
    private String catchMsg;
    private String eventPlace;
    private String latitude;
    private String longitude;
    private String startedAt;
    private String address;
    private String ownerNickname;
    private String ownerDisplayName;
    private String hashTag;
    private String eventType;

    public UserEventDetailBus(boolean success, int eventId, String eventTitle, String description, String imgUrl, String updated_at, String catchMsg, String eventPlace, String latitude, String longitude, String startedAt,
                              String address, String ownerNickname, String ownerDisplayName, String hashTag, String eventType) {
        this.success          = success;
        this.eventId          = eventId;
        this.eventTitle       = eventTitle;
        this.description      = description;
        this.imgUrl           = imgUrl;
        this.updated_at       = updated_at;
        this.catchMsg         = catchMsg;
        this.eventPlace       = eventPlace;
        this.latitude         = latitude;
        this.longitude        = longitude;
        this.startedAt        = startedAt;
        this.address          = address;
        this.ownerNickname    = ownerNickname;
        this.ownerDisplayName = ownerDisplayName;
        this.hashTag          = hashTag;
        this.eventType        = eventType;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getCatchMsg() {
        return catchMsg;
    }

    public void setCatchMsg(String catchMsg) {
        this.catchMsg = catchMsg;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getEventPlace() {
        return eventPlace;
    }

    public void setEventPlace(String eventPlace) {
        this.eventPlace = eventPlace;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(String startedAt) {
        this.startedAt = startedAt;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOwnerNickname() {
        return ownerNickname;
    }

    public void setOwnerNickname(String ownerNickname) {
        this.ownerNickname = ownerNickname;
    }

    public String getOwnerDisplayName() {
        return ownerDisplayName;
    }

    public void setOwnerDisplayName(String ownerDisplayName) {
        this.ownerDisplayName = ownerDisplayName;
    }

    public String getHashTag() {
        return hashTag;
    }

    public void setHashTag(String hashTag) {
        this.hashTag = hashTag;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }
}
