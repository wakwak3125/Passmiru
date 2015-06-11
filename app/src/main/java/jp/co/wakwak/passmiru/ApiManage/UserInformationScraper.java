package jp.co.wakwak.passmiru.ApiManage;

import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

import de.greenrobot.event.EventBus;
import jp.co.wakwak.passmiru.Bus.UserInfoBus;

public class UserInformationScraper extends AsyncTask<Void, Void, Void> {
    final static String TAG = UserInformationScraper.class.getSimpleName();

    private String userNickName;
    private String userName;
    private String profileImgUrl;
    private String profileDescription;

    public UserInformationScraper(String userNickName) {
        this.userNickName = userNickName;
    }

    @Override
    protected Void doInBackground(Void... params) {
        userName = null;
        profileImgUrl = null;

        String url = "http://connpass.com/user/";
        try {
            Document document = Jsoup.connect(url + userNickName).get();
            Elements elements = document.select("div#side_area").select("a[class=image_link]");
            profileImgUrl = elements.attr("href");

        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Document document = Jsoup.connect(url + userNickName).get();
            Elements elements = document.select("div#side_area").select("img");
            userName = elements.attr("title");

        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Document document = Jsoup.connect(url + userNickName).get();
            Elements elements = document.select("div.profile_header_area.mb_20.clearfix").select("p");
            profileDescription = elements.text();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if (userName != null && profileImgUrl != null) {
            EventBus.getDefault().post(new UserInfoBus(true, userName, profileImgUrl, profileDescription));
        } else {
            EventBus.getDefault().post(new UserInfoBus(false, "Error", "https://connpass.com/static/img/common/no_image_108x72.gif", "Error"));
        }
    }
}
