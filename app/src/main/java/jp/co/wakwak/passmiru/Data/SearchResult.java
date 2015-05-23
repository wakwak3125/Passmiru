package jp.co.wakwak.passmiru.Data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchResult {
    final static String TAG = SearchResult.class.getSimpleName();

    public int      event_id;
    public String   title;
    public String   event_url;
    public String   updated_at;
    public String   limit;
    public String   accepted;
    public String   owner_nickname;
    public String   imgUrl;

    public SearchResult(int event_id, String title, String event_url, String updated_at, String limit, String accepted, String owner_nickname, String imgUrl) {
        this.event_id = event_id;
        this.title = title;
        this.event_url = event_url;
        this.updated_at = updated_at;
        this.limit = limit;
        this.accepted = accepted;
        this.owner_nickname = owner_nickname;
        this.imgUrl = imgUrl;
    }

    public SearchResult() {}

    public int getEvent_id() {
        return event_id;
    }

    public void setEvent_id(int event_id) {
        this.event_id = event_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEvent_url() {
        return event_url;
    }

    public void setEvent_url(String event_url) {
        this.event_url = event_url;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    public String getAccepted() {
        return accepted;
    }

    public void setAccepted(String accepted) {
        this.accepted = accepted;
    }

    public String getOwner_nickname() {
        return owner_nickname;
    }

    public void setOwner_nickname(String owner_nickname) {
        this.owner_nickname = owner_nickname;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
