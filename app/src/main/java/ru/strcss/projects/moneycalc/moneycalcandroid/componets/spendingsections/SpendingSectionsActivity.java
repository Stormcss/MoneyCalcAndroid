package ru.strcss.projects.moneycalc.moneycalcandroid.componets.spendingsections;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import javax.inject.Inject;

import dagger.Lazy;
import dagger.android.support.DaggerAppCompatActivity;
import moneycalcandroid.moneycalc.projects.strcss.ru.moneycalc.R;
import ru.strcss.projects.moneycalc.moneycalcandroid.api.MoneyCalcServerDAO;
import ru.strcss.projects.moneycalc.moneycalcandroid.componets.history.HistoryActivity;
import ru.strcss.projects.moneycalc.moneycalcandroid.componets.home.HomeActivity;
import ru.strcss.projects.moneycalc.moneycalcandroid.componets.login.LoginActivity;
import ru.strcss.projects.moneycalc.moneycalcandroid.componets.settings.SettingsActivity;
import ru.strcss.projects.moneycalc.moneycalcandroid.storage.DataStorage;
import ru.strcss.projects.moneycalc.moneycalcandroid.utils.ActivityUtils;

import static ru.strcss.projects.moneycalc.moneycalcandroid.utils.ActivityUtils.changeActivityOnCondition;

public class SpendingSectionsActivity extends DaggerAppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @Inject
    SpendingSectionsPresenter presenter;

    @Inject
    Lazy<SpendingSectionsFragment> spendingSectionsFragmentProvider;

    @Inject
    MoneyCalcServerDAO moneyCalcServerDAO;

    @Inject
    DataStorage dataStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spendingsections_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.menu_spending_sections);
        setSupportActionBar(toolbar);

        changeActivityOnCondition(moneyCalcServerDAO.getToken() == null, SpendingSectionsActivity.this, LoginActivity.class);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        SpendingSectionsFragment spendingSectionsFragment =
                (SpendingSectionsFragment) getSupportFragmentManager().findFragmentById(R.id.spendingSections_contentFrame);
        if (spendingSectionsFragment == null) {
            // Get the fragment from dagger
            spendingSectionsFragment = spendingSectionsFragmentProvider.get();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), spendingSectionsFragment, R.id.spendingSections_contentFrame);
        }

        View headerView = navigationView.getHeaderView(0);

        TextView navHeaderUser = headerView.findViewById(R.id.nav_header_user);
        navHeaderUser.setText(dataStorage.getActiveUserData().getUserLogin());
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
                Intent i = new Intent(this, SettingsActivity.class);
                startActivity(i);
                break;
            case R.id.menu_refresh:
                presenter.requestSpendingSections();
                break;
            case R.id.menu_logout:
                moneyCalcServerDAO.setToken(null);
                changeActivityOnCondition(true, this, LoginActivity.class);
                break;
            default:
                break;
        }
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            startActivity(new Intent(SpendingSectionsActivity.this, HomeActivity.class));
            overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
        } else if (id == R.id.nav_history) {
            Intent intent =
                    new Intent(SpendingSectionsActivity.this, HistoryActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
//        } else if (id == R.id.nav_stats) {
//
//        } else if (id == R.id.nav_spending_sections) {
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
