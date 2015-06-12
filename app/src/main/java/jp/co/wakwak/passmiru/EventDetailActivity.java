package jp.co.wakwak.passmiru;

import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.util.Linkify;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nineoldandroids.view.ViewHelper;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.ButterKnife;
import butterknife.InjectView;
import jp.co.wakwak.passmiru.Commons.AppController;


public class EventDetailActivity extends AppCompatActivity implements ObservableScrollViewCallbacks {

    Intent intent;
    private int mParallaxImageHeight;
    private Double lat;
    private Double lon;
    private String eventID;
    private String eventType;

    @InjectView(R.id.title)
    TextView mTitle;
    @InjectView(R.id.catchMsg)
    TextView mCatchMsg;
    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.eventImage)
    ImageView mEventImage;
    @InjectView(R.id.webView)
    WebView mWebView;
    @InjectView(R.id.startAt)
    TextView mStartAt;
    @InjectView(R.id.address)
    TextView mAddress;
    @InjectView(R.id.place)
    TextView mPlace;
    @InjectView(R.id.owner_id)
    TextView mOwnerId;
    @InjectView(R.id.hashTag)
    TextView mHashTag;
    @InjectView(R.id.ownerNickName)
    TextView mOwnerNickName;
    @InjectView(R.id.scroll)
    ObservableScrollView mScrollView;
    @InjectView(R.id.openBrowserButton)
    BootstrapButton mOpenBrowserButton;
    @InjectView(R.id.joinButton)
    BootstrapButton mJoinButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        ButterKnife.inject(this);

        intent                  = getIntent();
        eventID                 = intent.getStringExtra("eventID");
        String description      = intent.getStringExtra("description");
        String imgUrl           = intent.getStringExtra("imgUrl");
        String title            = intent.getStringExtra("title");
        String updated_at       = intent.getStringExtra("updated_at");
        String catchMsg         = intent.getStringExtra("catch");
        String place            = intent.getStringExtra("eventPlace");
        String sLat             = intent.getStringExtra("lat");
        String sLon             = intent.getStringExtra("lon");
        String startedAt        = intent.getStringExtra("startedAt");
        String address          = intent.getStringExtra("address");
        String ownerNickName    = intent.getStringExtra("ownerNickName");
        String ownerDisplayName = intent.getStringExtra("ownerDisplayName");
        final String hashTag    = intent.getStringExtra("hashTag");
        eventType               = intent.getStringExtra("eventType");

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(title);

        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        toolbar.setBackgroundColor(ScrollUtils.getColorWithAlpha(0, getResources().getColor(R.color.primary)));
        mScrollView.setScrollViewCallbacks(EventDetailActivity.this);

        mParallaxImageHeight = getResources().getDimensionPixelSize(R.dimen.parallax_image_height);

        try {
            Picasso.with(this).load(imgUrl).into(mEventImage);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            Picasso.with(this).load(R.drawable.noimage).into(mEventImage);
        }

        // 各アイテムをViewにセット
        mTitle.setText(title);
        mCatchMsg.setText(catchMsg);
        mPlace.setText(place);
        mStartAt.setText(startedAt);
        mAddress.setText(address);
        mOwnerId.setText(ownerNickName);
        mOwnerNickName.setText(" (" + ownerDisplayName + ")");

        // ハッシュタグリンクの生成
        mHashTag.setText(hashTag);
        Pattern pattern = Pattern.compile(hashTag);
        final String hashTagLink = "http://twitter.com/search?q=%23" + hashTag;
        Linkify.TransformFilter filter = new Linkify.TransformFilter() {
            @Override
            public String transformUrl(Matcher match, String url) {
                return hashTagLink;
            }
        };
        Linkify.addLinks(mHashTag, pattern, hashTagLink, null, filter);

        // GoogleMapの処理
        MapView mMap = (MapView) findViewById(R.id.map);
        mMap.onCreate(savedInstanceState);
        GoogleMap googleMap = mMap.getMap();

        // latとlonが取得できなかった場合の回避処理
        if (sLat.equals("null")) {
            // 取得できなかった場合、マップを非表示にする。
            googleMap.setMapType(GoogleMap.MAP_TYPE_NONE);

        } else if (sLon.equals("null")) {
            // 取得できなかった場合、マップを非表示にする。
            googleMap.setMapType(GoogleMap.MAP_TYPE_NONE);

        } else {

            // 取得できた場合は通常処理
            lat = Double.parseDouble(sLat);
            lon = Double.parseDouble(sLon);

            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            googleMap.setMyLocationEnabled(false);

            MapsInitializer.initialize(this);

            CameraPosition eventPlace = new CameraPosition.Builder()
                    .target(new LatLng(lat, lon)).zoom(15.5f)
                    .bearing(0).tilt(0).build();
            CameraUpdate cameraUpdate = CameraUpdateFactory
                    .newCameraPosition(eventPlace);
            googleMap.moveCamera(cameraUpdate);

            LatLng eventLocation = new LatLng(lat, lon);
            MarkerOptions options = new MarkerOptions();
            options.position(eventLocation);
            options.title(place);
            googleMap.addMarker(options);

        }

        // イベント詳細表示用WebView
        mWebView.getSettings();
        mWebView.setBackgroundColor(getResources().getColor(R.color.cardview_light_background));
        mWebView.loadData(description, "text/html;charset=utf-8", "utf-8");

        // ブラウザで開くボタン
        mOpenBrowserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("http://connpass.com/event/"+ eventID +"/");
                Intent openBrowser = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(openBrowser);
            }
        });

        // イベントタイプが告知のみの場合の処理
        // ボタンのテキストを変更し、ユーザーに通知する。
        if(eventType.equals("advertisement")) {
            mJoinButton.setText("connpassでの参加受付なし");
            // mJoinButton.setBootstrapButtonEnabled(false);
        }

        // 参加ページにとぶボタン
        // イベントタイプが告知のみの場合は、参加登録ページに遷移させず、トーストにてユーザーに通知
        mJoinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(eventType.equals("advertisement")) {
                    Toast.makeText(AppController.getContext(),"このイベントはconnpassでの参加受付を行っておりません。",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Uri uri = Uri.parse("http://connpass.com/event/" + eventID + "/" + "join/");
                    Intent join = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(join);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        MapView mMap = (MapView) findViewById(R.id.map);
        mMap.onResume();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MapView mMap = (MapView) findViewById(R.id.map);
        mMap.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        MapView mMap = (MapView) findViewById(R.id.map);
        mMap.onLowMemory();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_event_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        onScrollChanged(mScrollView.getCurrentScrollY(), false, false);
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {

        int baseColor = getResources().getColor(R.color.primary);
        int baseTextColor = getResources().getColor(android.R.color.white);

        float alpha1 = 1 - (float) Math.max(0, mParallaxImageHeight - scrollY) / mParallaxImageHeight;
        float alpha2 = 1 - (float) Math.max(0, mParallaxImageHeight - scrollY) / mParallaxImageHeight;
        float alpha3 = 1 - (float) Math.max(0, mParallaxImageHeight - scrollY) / mParallaxImageHeight;

        toolbar.setBackgroundColor(ScrollUtils.getColorWithAlpha(alpha1, baseColor));
/*        toolbar.setTitleTextColor(ScrollUtils.getColorWithAlpha(alpha2, baseTextColor));*/
/*        toolbar.setSubtitleTextColor(ScrollUtils.getColorWithAlpha(alpha3, baseTextColor));*/
        ViewHelper.setTranslationY(mEventImage, scrollY / 2);
    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {

    }

    private class EventPlaceLocation implements LocationSource {

        private double latitude;
        private double longitude;

        public EventPlaceLocation(double latitude, double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }

        @Override
        public void activate(OnLocationChangedListener onLocationChangedListener) {
            Location location = new Location("EventLocation");
            location.setLatitude(latitude);
            location.setLongitude(longitude);
            onLocationChangedListener.onLocationChanged(location);
        }

        @Override
        public void deactivate() {

        }
    }
}
