package ru.strcss.projects.moneycalc.moneycalcandroid.componets.login;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;
import android.widget.ImageView;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;
import moneycalcandroid.moneycalc.projects.strcss.ru.moneycalc.R;
import ru.strcss.projects.moneycalc.moneycalcandroid.componets.settings.OnKeyboardVisibilityListener;
import ru.strcss.projects.moneycalc.moneycalcandroid.utils.view.UiUtils;

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

    ImageView signInLogo;
    int signInLogoTargetSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        getSupportActionBar().hide();
        setKeyboardVisibilityListener(this, (ViewGroup) findViewById(android.R.id.content));

        signInLogo = findViewById(R.id.signIn_logo);

        signInLogoTargetSize = signInLogo.getMaxHeight();

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

