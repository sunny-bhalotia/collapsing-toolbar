package nkdroid.tutorial.collapsingtoolbar;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageView imageView,tabBg;
    private CollapsingToolbarLayout collapsingToolbar;
    private TabPagerAdapter tabPagerAdapter;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;

    int[] photos={R.drawable.photo1, R.drawable.phpto2,R.drawable.photo3};
    int[] photos2={R.drawable.med1, R.drawable.wp1,R.drawable.pat1};

    Future longRunningTaskFuture;
    Handler handler;
    Runnable runnable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView= (ImageView) findViewById(R.id.backdrop);
        tabBg= (ImageView) findViewById(R.id.tabBg);
        collapsingToolbar=(CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);

        setToolbar();
        keepchangingpics(0);

        mViewPager= (ViewPager) findViewById(R.id.viewpager);
        mTabLayout= (TabLayout) findViewById(R.id.detail_tabs);
        tabPagerAdapter=new TabPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(tabPagerAdapter);
        mTabLayout.setTabsFromPagerAdapter(tabPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                try
                {
                    if(position==0){
                        Glide.with(MainActivity.this).load(R.drawable.onet).into(imageView);
                        Glide.with(MainActivity.this).load(R.drawable.oneb).into(tabBg);
                        longRunningTaskFuture.cancel(true);
                        handler.removeCallbacks(runnable);
                        keepchangingpics(position);
                    } else {
                        Glide.with(MainActivity.this).load(R.drawable.twot).into(imageView);
                        Glide.with(MainActivity.this).load(R.drawable.twob).into(tabBg);
                        longRunningTaskFuture.cancel(true);
                        handler.removeCallbacks(runnable);
                        keepchangingpics(position);
                    }

                    //imageView.invalidate();
                }
                catch (Exception e){}

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void setToolbar() {
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {

            setSupportActionBar(toolbar);
        }
    }

    private void setImage() {
        //Glide.with(this).load(R.drawable.onet).into(imageView);
       // Glide.with(MainActivity.this).load(R.drawable.oneb).into(tabBg);
    }

    class TabPagerAdapter extends FragmentStatePagerAdapter {

        public TabPagerAdapter(FragmentManager fm){
            super(fm);
        }
        @Override
        public Fragment getItem(int position) {

            SampleFragment sampleFragment=SampleFragment.newInstance((++position)+"");
            return sampleFragment;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "Dummy Tab "+(++position) ;
        }
    }

    public void keepchangingpics(final int pageno)
    {
        Log.e("fj",Integer.toString(pageno));
        handler = new Handler();
        runnable = new Runnable() {
            int i=0;
            public void run() {
                // change images randomly
                Random ran=new Random();
                if(pageno==0)
                {
                    //ran.nextInt(photos.length);
                    //set image resources
                    Glide.with(MainActivity.this).load(photos[i]).into(imageView);
                    i++;
                    if(i>photos.length-1)
                    {
                        i=0;
                    }
                }
                else if( pageno==1)
                {
                    //set image resources
                    Glide.with(MainActivity.this).load(photos2[i]).into(imageView);
                    i++;
                    if(i>photos2.length-1)
                    {
                        i=0;
                    }
                }
                handler.postDelayed(this, 7000);  //for interval...
            }
        };
        handler.postDelayed(runnable, 500); //for initial delay..

        ExecutorService threadPoolExecutor = Executors.newSingleThreadExecutor();
        longRunningTaskFuture = threadPoolExecutor.submit(runnable);
    }
}
