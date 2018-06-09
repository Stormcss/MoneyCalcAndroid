package ru.strcss.projects.moneycalc.moneycalcandroid.componets.history;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import javax.inject.Inject;

import dagger.Lazy;
import dagger.android.support.DaggerAppCompatActivity;
import moneycalcandroid.moneycalc.projects.strcss.ru.moneycalc.R;
import ru.strcss.projects.moneycalc.moneycalcandroid.componets.home.HomeActivity;
import ru.strcss.projects.moneycalc.moneycalcandroid.componets.spendingsections.SpendingSectionsActivity;
import ru.strcss.projects.moneycalc.moneycalcandroid.utils.ActivityUtils;

public class HistoryActivity extends DaggerAppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @Inject
    HistoryPresenter presenter;

    @Inject
    Lazy<HistoryFragment> historyFragmentProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        HistoryFragment historyFragment =
                (HistoryFragment) getSupportFragmentManager().findFragmentById(R.id.history_contentFrame);
        if (historyFragment == null) {
            // Get the fragment from dagger
            historyFragment = historyFragmentProvider.get();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), historyFragment, R.id.history_contentFrame);
        }


        // Load previously saved state, if available.
//        if (savedInstanceState != null) {
//            TasksFilterType currentFiltering =
//                    (TasksFilterType) savedInstanceState.getSerializable(CURRENT_FILTERING_KEY);
//            mTasksPresenter.setFiltering(currentFiltering);
//        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_settings:
                break;
            case R.id.menu_refresh:
                break;
            default:
                break;
        }
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            startActivity(new Intent(HistoryActivity.this, HomeActivity.class));
            overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
        } else if (id == R.id.nav_stats) {

        } else if (id == R.id.nav_finance) {

        } else if (id == R.id.nav_spending_sections) {
            Intent intent =
                    new Intent(HistoryActivity.this, SpendingSectionsActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.abc_popup_enter, R.anim.abc_popup_exit);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
