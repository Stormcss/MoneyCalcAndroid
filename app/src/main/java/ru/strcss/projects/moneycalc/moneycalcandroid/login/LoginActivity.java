package ru.strcss.projects.moneycalc.moneycalcandroid.login;

import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;
import moneycalcandroid.moneycalc.projects.strcss.ru.moneycalc.R;
import ru.strcss.projects.moneycalc.moneycalcandroid.settings.OnKeyboardVisibilityListener;
import ru.strcss.projects.moneycalc.moneycalcandroid.utils.view.UIutils;

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
        setKeyboardVisibilityListener(this);

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
            UIutils.collapseView(signInLogo, 100, 0);
        } else
            UIutils.expandView(signInLogo, 100, signInLogoTargetSize);
//        Toast.makeText(LoginActivity.this, visible ? "Keyboard is active" : "Keyboard is Inactive", Toast.LENGTH_SHORT).show();
    }

    private void setKeyboardVisibilityListener(final OnKeyboardVisibilityListener onKeyboardVisibilityListener) {
        final View parentView = ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
        parentView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            private boolean alreadyOpen;
            private final int defaultKeyboardHeightDP = 100;
            private final int EstimatedKeyboardDP = defaultKeyboardHeightDP + (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? 48 : 0);
            private final Rect rect = new Rect();

            @Override
            public void onGlobalLayout() {
                int estimatedKeyboardHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, EstimatedKeyboardDP, parentView.getResources().getDisplayMetrics());
                parentView.getWindowVisibleDisplayFrame(rect);
                int heightDiff = parentView.getRootView().getHeight() - (rect.bottom - rect.top);
                boolean isShown = heightDiff >= estimatedKeyboardHeight;

                if (isShown == alreadyOpen) {
                    Log.i("Keyboard state", "Ignoring global layout change...");
                    return;
                }
                alreadyOpen = isShown;
                onKeyboardVisibilityListener.onVisibilityChanged(isShown);
            }
        });
    }
}

