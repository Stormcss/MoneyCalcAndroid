package ru.strcss.projects.moneycalc.moneycalcandroid.componets.history.historyfilter;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
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
import ru.strcss.projects.moneycalc.moneycalcandroid.storage.DataStorage;

import static ru.strcss.projects.moneycalc.moneycalcandroid.AppConstants.TRANSACTIONS_SEARCH_CONTAINER;
import static ru.strcss.projects.moneycalc.moneycalcandroid.utils.ActivityUtils.changeActivityOnCondition;
import static ru.strcss.projects.moneycalc.moneycalcandroid.utils.DatesUtils.getCalendarFromString;
import static ru.strcss.projects.moneycalc.moneycalcandroid.utils.DatesUtils.getIsoDate;
import static ru.strcss.projects.moneycalc.moneycalcandroid.utils.DatesUtils.getStringFromCalendar;

public class HistoryFilterActivity extends DaggerAppCompatActivity {

    private TextView twDateFrom;
    private TextView twDateTo;
    private RecyclerView sectionsRv;
    private Button btnSectionsCheckAll;
    private Button btnSectionsUncheckAll;
    private EditText etTitle;
    private EditText etDesc;

    private ActionBar mActionBar;

    private TransactionsSearchContainerLegacy filter;

    @Inject
    HistoryFilterContract.Presenter presenter;

    @Inject
    public HistoryFilterActivity() {
    }

    @Inject
    MoneyCalcServerDAO moneyCalcServerDAO;

    @Inject
    DataStorage dataStorage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        changeActivityOnCondition(moneyCalcServerDAO.getToken() == null, HistoryFilterActivity.this, LoginActivity.class);

        setContentView(R.layout.history_filter_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setDisplayShowHomeEnabled(true);
        mActionBar.setTitle(R.string.menu_history_filter);

        twDateFrom = findViewById(R.id.history_filter_date_from);
        twDateTo = findViewById(R.id.history_filter_date_to);
        sectionsRv = findViewById(R.id.history_filter_sections);
        btnSectionsCheckAll = findViewById(R.id.history_filter_section_check_all_button);
        btnSectionsUncheckAll = findViewById(R.id.history_filter_section_uncheck_all_button);
        etTitle = findViewById(R.id.history_filter_title);
        etDesc = findViewById(R.id.history_filter_desc);

        if (getIntent().getExtras() != null) {
            filter = (TransactionsSearchContainerLegacy) getIntent().getExtras().getSerializable(TRANSACTIONS_SEARCH_CONTAINER);
        }

        if (filter == null) {
            filter = new TransactionsSearchContainerLegacy(dataStorage.getSettings().getPeriodFrom(),
                    dataStorage.getSettings().getPeriodTo(), null);
        }

        setTransactionPeriodListener(filter);
    }

    @Override
    public void onBackPressed() {
        if (filter != null) {
            System.out.println("filter = " + filter);
            presenter.requestFilteredTransactions(filter);
        }
        super.onBackPressed();
    }

    final DatePickerDialog.OnDateSetListener onDateFromSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            String date = getIsoDate(year, month + 1, dayOfMonth);
            twDateFrom.setText(date);
            filter.setRangeFrom(date);
        }
    };

    final DatePickerDialog.OnDateSetListener onDateToSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            String date = getIsoDate(year, month + 1, dayOfMonth);
            twDateTo.setText(date);
            filter.setRangeTo(date);
        }
    };

    private void setTransactionPeriodListener(final TransactionsSearchContainerLegacy savedFilter) {
        String dateFrom = null;
        String dateTo = null;

        if (savedFilter != null) {
            dateFrom = savedFilter.getRangeFrom();
            dateTo = savedFilter.getRangeTo();
            twDateFrom.setText(dateFrom);
            twDateTo.setText(dateTo);
        } else {
            String calendar = getStringFromCalendar(Calendar.getInstance());
            twDateFrom.setText(calendar);
            twDateTo.setText(calendar);
        }

        twDateFrom.setOnClickListener(getDateClickListener(dateFrom, onDateFromSetListener));
        twDateTo.setOnClickListener(getDateClickListener(dateTo, onDateToSetListener));
    }

    private View.OnClickListener getDateClickListener(final String date,
                                                      final DatePickerDialog.OnDateSetListener onDateSetListener) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar;
                if (date != null) {
                    calendar = getCalendarFromString(date);
                } else {
                    calendar = Calendar.getInstance();
                }
                getDatePickerDialog(onDateSetListener, calendar).show();
            }
        };
    }

    private DatePickerDialog getDatePickerDialog(DatePickerDialog.OnDateSetListener listener, Calendar calendar) {
        return new DatePickerDialog(this, listener,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
