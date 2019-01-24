package ru.strcss.projects.moneycalc.moneycalcandroid.componets.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;
import moneycalcandroid.moneycalc.projects.strcss.ru.moneycalc.R;
import ru.strcss.projects.moneycalc.moneycalcandroid.componets.home.HomeActivity;
import ru.strcss.projects.moneycalc.moneycalcandroid.componets.login.applicationsettings.ApplicationSettingsActivity;
import ru.strcss.projects.moneycalc.moneycalcandroid.componets.settings.OnKeyboardVisibilityListener;
import ru.strcss.projects.moneycalc.moneycalcandroid.utils.view.UiUtils;

import static ru.strcss.projects.moneycalc.moneycalcandroid.ApplicationStoragePreferenceKey.appl_storage_token;
import static ru.strcss.projects.moneycalc.moneycalcandroid.utils.view.UiUtils.setKeyboardVisibilityListener;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends DaggerAppCompatActivity implements OnKeyboardVisibilityListener {
    @Inject
    LoginPresenter loginPresenter;
    @Inject
    LoginFragment loginFragment;
    @Inject
    RegisterPresenter registerPresenter;
    @Inject
    RegisterFragment registerFragment;

    private ImageView signInLogo;
    private ImageView signInSettingsLogo;
    int signInLogoTargetSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (sharedPreferences.getString(appl_storage_token.name(), null) != null) {
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
        }

        getSupportActionBar().hide();
        setKeyboardVisibilityListener(this, (ViewGroup) findViewById(android.R.id.content));

        signInLogo = findViewById(R.id.signIn_logo);
        signInSettingsLogo = findViewById(R.id.signIn_settings);

        signInLogoTargetSize = signInLogo.getMaxHeight();

        signInSettingsLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ApplicationSettingsActivity.class));
            }
        });

        //fragments setup
        TabLayout tabLayout = findViewById(R.id.login_tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.title_activity_login)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.title_activity_register)));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = findViewById(R.id.login_viewPager);
        final LoginPagerAdapter adapter = new LoginPagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    @Override
    public void onVisibilityChanged(boolean visible) {
        if (visible) {
            signInLogoTargetSize = signInLogo.getHeight();
            UiUtils.collapseView(signInLogo, 100, 0);
        } else
            UiUtils.expandView(signInLogo, 100, signInLogoTargetSize);
    }
}

