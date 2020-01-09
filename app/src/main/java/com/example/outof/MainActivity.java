package com.example.outof;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Toast;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    private Context mContext;
    private MakeListActivity makeListActivity;
    private ViewListActivity viewListActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager mViewPager = findViewById(R.id.viewPager);
        TabLayout mTabLayout = findViewById(R.id.tabLayout);
        makeListActivity = MakeListActivity.newInstance();
        viewListActivity = ViewListActivity.newInstance();
        mContext = getApplicationContext();
        setupViewPager(mViewPager);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    public void showPopup(View view) {
        hideSoftKeyboard(view);
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.setOnMenuItemClickListener(this);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.more_menu, popupMenu.getMenu());
        popupMenu.show();
    }

    public void hideSoftKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
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
                clearDialog.setPositiveButton("Yes", (dialog, which) -> makeListActivity.clear());

                //Cancel Clearing List - NO
                clearDialog.setNegativeButton("No", (dialog, which) -> dialog.cancel());

                AlertDialog alertDialog = clearDialog.create();
                alertDialog.show();
                //makeListActivity.clear();
                return true;
            case R.id.share:
                Toast.makeText(mContext, "Share List", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.print:
                Toast.makeText(mContext, "Print List", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.help:
                Toast.makeText(mContext, "Help", Toast.LENGTH_SHORT).show();
                Intent helpIntent = new Intent(MainActivity.this, Help.class);
                startActivity(helpIntent);
                return true;
            case R.id.about:
                Toast.makeText(mContext, "About", Toast.LENGTH_SHORT).show();
                Intent aboutIntent = new Intent(MainActivity.this, About.class);
                startActivity(aboutIntent);
                return true;
            default:
                return false;
        }
    }
}
