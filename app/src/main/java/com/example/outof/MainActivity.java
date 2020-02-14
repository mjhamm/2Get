package com.example.outof;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.viewpager.widget.ViewPager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener, MakeListActivity.MakeListFragmentListener {

    private Context mContext;
    private MakeListActivity makeListActivity;
    private ViewListActivity viewListActivity;
    private DatabaseHelper myDB;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(this, initializationStatus -> {
        });
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest );

        ViewPager mViewPager = findViewById(R.id.viewPager);
        TabLayout mTabLayout = findViewById(R.id.tabLayout);
        makeListActivity = MakeListActivity.newInstance();
        viewListActivity = ViewListActivity.newInstance();
        mContext = getApplicationContext();

        myDB = DatabaseHelper.getInstance(mContext);
        setupViewPager(mViewPager);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    public void showPopup(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.setOnMenuItemClickListener(this);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.more_menu, popupMenu.getMenu());
        popupMenu.show();
    }

    private void setupViewPager(ViewPager viewPager) {
        MainPagerAdapter adapter = new MainPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(makeListActivity, "Make List");
        adapter.addFragment(viewListActivity, "View List");
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {

            //Clear List
            case R.id.clear:
                AlertDialog.Builder clearDialog = new AlertDialog.Builder(MainActivity.this);
                clearDialog.setMessage("Are you sure that you want to clear everything on your list?");
                clearDialog.setCancelable(false);

                //Clear List - YES
                clearDialog.setPositiveButton("Yes", (dialog, which) -> {
                    makeListActivity.clear();
                    viewListActivity.clearList();
                    myDB.clearData();
                    Toast.makeText(mContext, "Your List has been cleared.", Toast.LENGTH_SHORT).show();
                });

                //Cancel Clearing List - NO
                clearDialog.setNegativeButton("No", (dialog, which) -> dialog.cancel());

                AlertDialog alertDialog = clearDialog.create();
                alertDialog.show();
                return true;
            case R.id.share:
                if (!viewListActivity.exportList().toString().isEmpty()) {
                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Here's the Shopping List!");
                    shareIntent.putExtra(Intent.EXTRA_TEXT, "2Get Shopping List!\n\n" + viewListActivity.exportList().toString());
                    startActivity(Intent.createChooser(shareIntent, "Share..."));
                } else {
                    Toast.makeText(mContext, "You have nothing added to your List.", Toast.LENGTH_SHORT).show();
                }

                return true;
            case R.id.about:
                Intent aboutIntent = new Intent(MainActivity.this, About.class);
                startActivity(aboutIntent);
                return true;
            default:
                return false;
        }
    }

    @Override
    protected void onDestroy() {
        myDB.close();
        super.onDestroy();
    }

    @Override
    public void onSelectionASent(String selection) {
        viewListActivity.addItemToList(selection);
    }

    @Override
    public void onSelectionBSent(String selection) {
        viewListActivity.removeItemFromList(selection);
    }


}
