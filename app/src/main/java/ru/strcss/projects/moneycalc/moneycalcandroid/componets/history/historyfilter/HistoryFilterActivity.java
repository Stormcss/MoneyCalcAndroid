package ru.strcss.projects.moneycalc.moneycalcandroid.componets.history.historyfilter;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;
import moneycalcandroid.moneycalc.projects.strcss.ru.moneycalc.R;
import ru.strcss.projects.moneycalc.dto.crudcontainers.transactions.TransactionsSearchContainerLegacy;
import ru.strcss.projects.moneycalc.moneycalcandroid.api.MoneyCalcServerDAO;
import ru.strcss.projects.moneycalc.moneycalcandroid.componets.login.LoginActivity;

import static ru.strcss.projects.moneycalc.moneycalcandroid.AppConstants.TRANSACTIONS_SEARCH_CONTAINER;
import static ru.strcss.projects.moneycalc.moneycalcandroid.utils.ActivityUtils.changeActivityOnCondition;
import static ru.strcss.projects.moneycalc.moneycalcandroid.utils.DatesUtils.getCalendarFromString;
import static ru.strcss.projects.moneycalc.moneycalcandroid.utils.DatesUtils.getIsoDate;

public class HistoryFilterActivity extends DaggerAppCompatActivity {

    private TextView dateFrom;
    private TextView dateTo;
    private RecyclerView sectionsRv;
    private Button btnSectionsCheckAll;
    private Button btnSectionsUncheckAll;
    private EditText etTitle;
    private EditText etDesc;

    private TransactionsSearchContainerLegacy filter;
    private boolean isFilterApplied;
    @Inject
    MoneyCalcServerDAO moneyCalcServerDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_filter_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.menu_history_filter);
        setSupportActionBar(toolbar);

        changeActivityOnCondition(moneyCalcServerDAO.getToken() == null, HistoryFilterActivity.this, LoginActivity.class);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        dateFrom = findViewById(R.id.history_filter_date_from);
        dateTo = findViewById(R.id.history_filter_date_to);
        sectionsRv = findViewById(R.id.history_filter_sections);
        btnSectionsCheckAll = findViewById(R.id.history_filter_section_check_all_button);
        btnSectionsUncheckAll = findViewById(R.id.history_filter_section_uncheck_all_button);
        etTitle = findViewById(R.id.history_filter_title);
        etDesc = findViewById(R.id.history_filter_desc);


        final TransactionsSearchContainerLegacy savedFilter =
                (TransactionsSearchContainerLegacy) savedInstanceState.get(TRANSACTIONS_SEARCH_CONTAINER);

        isFilterApplied = savedFilter != null;

        setTransactionPeriodListener(savedFilter);

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

    final DatePickerDialog.OnDateSetListener onDateFromSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            String date = getIsoDate(year, month + 1, dayOfMonth);
            dateFrom.setText(date);
            filter.setRangeFrom(date);
        }
    };

    final DatePickerDialog.OnDateSetListener onDateToSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            String date = getIsoDate(year, month + 1, dayOfMonth);
            dateTo.setText(date);
            filter.setRangeTo(date);
        }
    };

    private void setTransactionPeriodListener(final TransactionsSearchContainerLegacy savedFilter) {
        dateFrom.setOnClickListener(getDateClickListener(savedFilter.getRangeFrom(), onDateFromSetListener));
        dateTo.setOnClickListener(getDateClickListener(savedFilter.getRangeTo(), onDateToSetListener));
    }

    private View.OnClickListener getDateClickListener(final String date,
                                                      final DatePickerDialog.OnDateSetListener onDateSetListener) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar;
                if (isFilterApplied) {
                    calendar = getCalendarFromString(date);
                } else {
                    calendar = Calendar.getInstance();
                }
                new DatePickerDialog(getParent(), onDateSetListener,
                        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        };
    }
}
