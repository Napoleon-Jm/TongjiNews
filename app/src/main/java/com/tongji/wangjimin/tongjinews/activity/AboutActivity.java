package com.tongji.wangjimin.tongjinews.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.MenuItem;
import android.widget.TextView;

import com.tongji.wangjimin.tongjinews.R;


public class AboutActivity extends AppCompatActivity {

    private TextView mAboutText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        /* work */
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("About App");
        }
        mAboutText = (TextView)findViewById(R.id.about_textview);
        Spanned result;
        /* TextView 不支持 align 属性。*/
        String aboutApp = "<p>v1.0.0</p>\n" +
                "    <p>It's a gift for 110~(th) anniversary. Main function:</p>\n" +
                "    <p>1. View important news.</p>\n" +
                "    <p>2. View news image list.    </p>\n" +
                "    <p>3. Collect news that you like.       </p>\n" +
                "    <p>4. Quick search news.        </p>\n" +
                "    <p>Developed by:</p>\n" +
                "    <p>@Wangjimin, study in TongJi for 7 years.</p>\n" +
                "    <p>Email: wangjimin.c@hotmail.com</p>\n" +
                "    <p>Thanks for Jsoup, that used to crawl news on </p>\n" +
                "    <a href=\"www.tongji.edu.cn\">www.tongji.edu.cn\n";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(aboutApp,Html.FROM_HTML_MODE_LEGACY);
        } else {
            //noinspection deprecation
            result = Html.fromHtml(aboutApp);
        }
        mAboutText.setText(result);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
