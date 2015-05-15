package jp.co.wakwak.passmiru;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.ImageView;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.nineoldandroids.view.ViewHelper;
import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class EventDetailActivity extends AppCompatActivity implements ObservableScrollViewCallbacks {

    Intent intent;

    private int mParallaxImageHeight;

    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.eventImage)
    ImageView mEventImage;
    @InjectView(R.id.webView)
    WebView mWebView;
    @InjectView(R.id.scroll)
    ObservableScrollView mScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        ButterKnife.inject(this);

        intent = getIntent();
        String description = intent.getStringExtra("description");
        String imgUrl = intent.getStringExtra("imgUrl");
        String title = intent.getStringExtra("title");
        String updated_at = intent.getStringExtra("upsated_at");

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(title);

        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        toolbar.setBackgroundColor(ScrollUtils.getColorWithAlpha(0, getResources().getColor(R.color.primary)));
        mScrollView.setScrollViewCallbacks(EventDetailActivity.this);

        mParallaxImageHeight = getResources().getDimensionPixelSize(R.dimen.parallax_image_height);

        Picasso.with(this).load(imgUrl).into(mEventImage);

        mWebView.getSettings();
        mWebView.setBackgroundColor(getResources().getColor(R.color.background_material_light));
        mWebView.loadData(description, "text/html;charset=utf-8", "utf-8");

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
}
