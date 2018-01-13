package com.example.joshmitchell.noteapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.joshmitchell.noteapp.R;

import static com.example.joshmitchell.noteapp.Activity.NoteListActivity.LIST_FILTER;

/**
 * Created by Josh Mitchell on 27/12/2017.
 */

public abstract class SingleFragmentActivity extends AppCompatActivity {

    protected abstract Fragment createFragment();

    private NavigationView nvDrawer;

    private void setupDrawerContent(NavigationView navigationView){
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        selectDrawerItem(item);
                        return true;
                    }
                }
        );
    }

    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        Fragment fragment = null;
        Class fragmentClass;
        switch(menuItem.getItemId()) {
            case R.id.nav_first_fragment:
                Intent i = new Intent(SingleFragmentActivity.this,
                        NoteListActivity.class);
                startActivity(i);
                break;
            case R.id.nav_second_fragment:
                Intent archivedIntent = new Intent(SingleFragmentActivity.this,
                        NoteListActivity.class);
                archivedIntent.putExtra(LIST_FILTER, 1);
                startActivity(archivedIntent);
                break;
            case R.id.nav_third_fragment:
                ;
                break;
            default:
                ;
        }

        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        nvDrawer = (NavigationView) findViewById(R.id.nvView);
        setupDrawerContent(nvDrawer);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);

        if (fragment == null){
            fragment = createFragment();
            fm.beginTransaction()
                    .add(R.id.fragmentContainer, fragment)
                    .commit();
        }
    }
}
