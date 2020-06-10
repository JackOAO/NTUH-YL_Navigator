package eos.waypointbasedindoornavigation;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ARModeTeachingActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;
    List<View> viewPages = new ArrayList<>();
    private ViewGroup viewGroup;
    private ImageView imageView;
    private ImageView[] imageViews;
    public String destinationName;
    public String destinationID;
    public String destinationRegion;
    private String language_option;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_armode_teaching);
        Bundle bundle = getIntent().getExtras();
        destinationName = bundle.getString("destinationName");
        destinationID = bundle.getString("destinationID");
        destinationRegion = bundle.getString("destinationRegion");
        Context appContext = GetApplicationContext.getAppContext();
        SharedPreferences languagePref = PreferenceManager.getDefaultSharedPreferences(appContext);
        language_option = languagePref.getString("language","繁體中文");
        if(language_option.equals("繁體中文"))
            setTitle("臺大雲林分院室內導航系統");
        else if(language_option.equals("English"))
            setTitle("NTUH-Yunlin");
        initView();
        initPagerAdapter();
        initPointer();
        initEvent();
        initButton();
    }

    private void initView()
    {
        viewPager = (ViewPager)findViewById(R.id.viewPager);
        viewGroup = (ViewGroup)findViewById(R.id.viewGroup);
    }

    private void initPagerAdapter()
    {
        LayoutInflater inflater = LayoutInflater.from(this);
        View page1 = inflater.inflate(R.layout.page1,null);
        View page2 = inflater.inflate(R.layout.page2,null);
        View page3 = inflater.inflate(R.layout.page3,null);
        View page4 = inflater.inflate(R.layout.page4,null);
        if(language_option.equals("English"))
        {
            TextView txtPg1 = page1.findViewById(R.id.textView);
            txtPg1.setText("In order to ensure the best navigation experience,  please keep the phone and perspective level.");
            txtPg1.setTextSize(20);

            TextView txtPg2 = page2.findViewById(R.id.textView);
            txtPg2.setText("At the beginning, please face the direction of the picture yo start navigation.");
            txtPg2.setTextSize(20);
            ImageView imgPg2 = page2.findViewById(R.id.imageViewPg);
            imgPg2.setImageResource(R.drawable.initial_direction_eng);

            TextView txtPg3 = page3.findViewById(R.id.textView);
            txtPg3.setText("After receiving the waypoint signal, please follow the prompts to face the correct direction.");
            txtPg3.setTextSize(20);
            ImageView imgPg3 = page3.findViewById(R.id.imageViewPg);
            imgPg3.setImageResource(R.drawable.direction_check_eng);

            TextView txtPg4 = page4.findViewById(R.id.textView);
            txtPg4.setText("Navigation instruction appear when the direction is correct.");
            txtPg4.setTextSize(20);
            ImageView imgPg4 = page4.findViewById(R.id.imageViewPg);
            imgPg4.setImageResource(R.drawable.instruction_display_eng);
        }
        viewPages.add(page1);
        viewPages.add(page2);
        viewPages.add(page3);
        viewPages.add(page4);
        pagerAdapter = new PagerAdapter() {
            @Override
            public int getCount() {
                return viewPages.size();
            }

            @NonNull
            @Override
            public Object instantiateItem(@NonNull ViewGroup container, int position) {
                View view = viewPages.get(position);
                container.addView(view);
                return view;
            }

            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                container.removeView(viewPages.get(position));
            }


            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
                return view == o;
            }
        };
    }

    private void initPointer()
    {
        imageViews = new ImageView[viewPages.size()];
        for(int i = 0 ; i < imageViews.length ; i++)
        {
            imageView = new ImageView(this);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(25,25));
            imageView.setPadding(20,0,20,0);
            imageViews[i] = imageView;
            if(i == 0)
                imageViews[i].setBackgroundResource(R.drawable.dot_white);
            else
                imageViews[i].setBackgroundResource(R.drawable.dot_black);
            viewGroup.addView(imageViews[i]);
        }
    }
    private void initButton()
    {
        View view = viewPages.get(3);
        Button btn = (Button)view.findViewById(R.id.OKButton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ARModeTeachingActivity.this,ARNavigationActivity.class);
                i.putExtra("destinationName", destinationName);
                i.putExtra("destinationID", destinationID);
                i.putExtra("destinationRegion", destinationRegion);
                startActivity(i);
                finish();
            }
        });

    }
    private void initEvent()
    {
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new GuildPageChangeListener());
    }

    public class GuildPageChangeListener implements ViewPager.OnPageChangeListener
    {

        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {
            for(int index= 0 ; index < imageViews.length ; index++)
            {
                imageViews[index].setBackgroundResource(R.drawable.dot_white);
                if(i != index)
                    imageViews[index].setBackgroundResource(R.drawable.dot_black);
            }
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //appendLog("EndNavigation");
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
            finish();
        }
        return true;
    }
}
