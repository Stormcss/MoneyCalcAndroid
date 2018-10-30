package ru.strcss.projects.moneycalc.moneycalcandroid.componets.history.historyfilter;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;
import moneycalcandroid.moneycalc.projects.strcss.ru.moneycalc.R;
import ru.strcss.projects.moneycalc.dto.crudcontainers.transactions.TransactionsSearchContainerLegacy;
import ru.strcss.projects.moneycalc.enitities.SpendingSection;
import ru.strcss.projects.moneycalc.moneycalcandroid.api.MoneyCalcServerDAO;
import ru.strcss.projects.moneycalc.moneycalcandroid.componets.common.spendingsecionrvadapters.SpendingSectionRVMultiChooseAdapter;
import ru.strcss.projects.moneycalc.moneycalcandroid.componets.common.spendingsecionrvadapters.SpendingSectionRVSingleChooseAdapter;
import ru.strcss.projects.moneycalc.moneycalcandroid.componets.login.LoginActivity;
import ru.strcss.projects.moneycalc.moneycalcandroid.componets.settings.OnKeyboardVisibilityListener;
import ru.strcss.projects.moneycalc.moneycalcandroid.storage.DataStorage;
import ru.strcss.projects.moneycalc.moneycalcandroid.utils.view.UiUtils;

import static ru.strcss.projects.moneycalc.moneycalcandroid.utils.ActivityUtils.changeActivityOnCondition;
import static ru.strcss.projects.moneycalc.moneycalcandroid.utils.DatesUtils.formatDateToIso;
import static ru.strcss.projects.moneycalc.moneycalcandroid.utils.DatesUtils.formatDateToPretty;
import static ru.strcss.projects.moneycalc.moneycalcandroid.utils.DatesUtils.getCalendarFromString;
import static ru.strcss.projects.moneycalc.moneycalcandroid.utils.DatesUtils.getIsoDate;
import static ru.strcss.projects.moneycalc.moneycalcandroid.utils.DatesUtils.getStringIsoDateFromCalendar;
import static ru.strcss.projects.moneycalc.moneycalcandroid.utils.view.UiUtils.setKeyboardVisibilityListener;

public class HistoryFilterActivity extends DaggerAppCompatActivity implements HistoryFilterContract.View,
        SpendingSectionRVSingleChooseAdapter.ItemClickListener, OnKeyboardVisibilityListener {

    private TextView twDateFrom;
    private TextView twDateTo;
    private RecyclerView rvSections;
    private Button btnSectionsCheckAll;
    private Button btnSectionsUncheckAll;
    private LinearLayout layoutSectionButtonsPane;
    private EditText etTitle;
    private EditText etDesc;

    private ActionBar mActionBar;

    private TransactionsSearchContainerLegacy filter;

    private List<SpendingSection> spendingSectionsList = new ArrayList<>();
    private SpendingSectionRVMultiChooseAdapter ssAdapter;
    private Set<Integer> selectedRecyclerViewItems = new HashSet<>();

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
        setKeyboardVisibilityListener(this, (ViewGroup) findViewById(R.id.history_filter_root_view));

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setDisplayShowHomeEnabled(true);
        mActionBar.setTitle(R.string.menu_history_filter);

        twDateFrom = findViewById(R.id.history_filter_date_from);
        twDateTo = findViewById(R.id.history_filter_date_to);
        rvSections = findViewById(R.id.history_filter_sections);
        btnSectionsCheckAll = findViewById(R.id.history_filter_section_check_all_button);
        btnSectionsUncheckAll = findViewById(R.id.history_filter_section_uncheck_all_button);
        etTitle = findViewById(R.id.history_filter_title);
        etDesc = findViewById(R.id.history_filter_desc);
        layoutSectionButtonsPane = findViewById(R.id.history_filter_section_buttons_pane);

        filter = dataStorage.getTransactionsFilter();

        if (filter == null) {
            filter = new TransactionsSearchContainerLegacy();
            filter.setRangeFrom(dataStorage.getSettings().getPeriodFrom());
            filter.setRangeTo(dataStorage.getSettings().getPeriodTo());
        }

        setTransactionPeriodListener(filter);

        setSectionsCheckButtons();

        ssAdapter = new SpendingSectionRVMultiChooseAdapter(this, spendingSectionsList, selectedRecyclerViewItems);
        ssAdapter.setClickListener(this);
        rvSections.setAdapter(ssAdapter);
        rvSections.setLayoutManager(new GridLayoutManager(this, 3));

        presenter.requestSpendingSectionsList();
    }

    private void setSectionsCheckButtons() {
        btnSectionsCheckAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (SpendingSection spendingSection : spendingSectionsList) {
                    selectedRecyclerViewItems.add(spendingSection.getSectionId());
                }
                ssAdapter.notifyDataSetChanged();
            }
        });
        btnSectionsUncheckAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedRecyclerViewItems.clear();
                ssAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (filter != null) {
            filter.setRequiredSections(new ArrayList<>(selectedRecyclerViewItems));
            String title = etTitle.getText().toString();
            String description = etDesc.getText().toString();
            if (!title.isEmpty())
                filter.setTitle(title);
            if (!description.isEmpty())
                filter.setDescription(description);

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
            dateFrom = formatDateToPretty(savedFilter.getRangeFrom());
            dateTo = formatDateToPretty(savedFilter.getRangeTo());
            twDateFrom.setText(dateFrom);
            twDateTo.setText(dateTo);
        } else {
            String calendar = formatDateToPretty(getStringIsoDateFromCalendar(Calendar.getInstance()));
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
                    calendar = getCalendarFromString(formatDateToIso(date));
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

    @Override
    public void onItemClick(View view, int position) {
        ssAdapter.notifyItemChanged(position);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.takeView(this);
    }

    @Override
    public void onDestroy() {
        presenter.dropView();
        super.onDestroy();
    }

    @Override
    public void showErrorMessage(String msg) {

    }

    @Override
    public void showSpendingSections(List<SpendingSection> spendingSections) {
        spendingSectionsList.clear();
        spendingSectionsList.addAll(spendingSections);

        if (filter != null && filter.getRequiredSections() != null) {
            selectedRecyclerViewItems.addAll(filter.getRequiredSections());
        }

        ssAdapter.notifyDataSetChanged();
    }

    @Override
    public void onVisibilityChanged(boolean visible) {
        if (visible) {
            UiUtils.collapseView(rvSections, 100, 100);
            UiUtils.collapseView(layoutSectionButtonsPane, 100, 0);
        } else {
            UiUtils.expandView(rvSections, 100, 500);
            UiUtils.expandView(layoutSectionButtonsPane, 100, 180);
        }
    }
}
