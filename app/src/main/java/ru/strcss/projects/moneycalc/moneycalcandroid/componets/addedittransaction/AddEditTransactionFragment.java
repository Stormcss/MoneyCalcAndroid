package ru.strcss.projects.moneycalc.moneycalcandroid.componets.addedittransaction;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;
import moneycalcandroid.moneycalc.projects.strcss.ru.moneycalc.R;
import ru.strcss.projects.moneycalc.moneycalcandroid.componets.common.spendingsecionrvadapters.SpendingSectionRVSingleChooseAdapter;
import ru.strcss.projects.moneycalc.moneycalcandroid.componets.settings.OnKeyboardVisibilityListener;
import ru.strcss.projects.moneycalc.moneycalcandroid.utils.DatesUtils;
import ru.strcss.projects.moneycalc.moneycalcandroid.utils.view.SnackbarWrapper;
import ru.strcss.projects.moneycalc.moneycalcandroid.utils.view.UiUtils;
import ru.strcss.projects.moneycalc.moneycalcdto.entities.SpendingSection;
import ru.strcss.projects.moneycalc.moneycalcdto.entities.TransactionLegacy;

import static ru.strcss.projects.moneycalc.moneycalcandroid.AppConstants.TRANSACTION;
import static ru.strcss.projects.moneycalc.moneycalcandroid.utils.ActivityUtils.hideSoftKeyboard;
import static ru.strcss.projects.moneycalc.moneycalcandroid.utils.ActivityUtils.snackBarAction;
import static ru.strcss.projects.moneycalc.moneycalcandroid.utils.DatesUtils.getCalendarFromString;
import static ru.strcss.projects.moneycalc.moneycalcandroid.utils.DatesUtils.getIsoDate;
import static ru.strcss.projects.moneycalc.moneycalcandroid.utils.logic.ComponentsUtils.getPositionBySpendingSectionInnerId;
import static ru.strcss.projects.moneycalc.moneycalcandroid.utils.view.UiUtils.setKeyboardVisibilityListener;

public class AddEditTransactionFragment extends DaggerFragment
        implements AddEditTransactionContract.View,
        SpendingSectionRVSingleChooseAdapter.ItemClickListener, OnKeyboardVisibilityListener {

    @Inject
    AddEditTransactionContract.Presenter presenter;

    @Inject
    public AddEditTransactionFragment() {
    }

    // UI references
    private TextView twTransactionDate;
    private EditText etTransactionSum;
    private EditText etTransactionTitle;
    private EditText etTransactionDesc;
    private RecyclerView rvTransactionSection;

    private SpendingSection selectedSection;
    private String transactionDate;
    private List<SpendingSection> spendingSectionsList = new ArrayList<>();
    private SpendingSectionRVSingleChooseAdapter ssAdapter;

    private AtomicInteger selectedRecyclerViewItem = new AtomicInteger(-1);
    private boolean isEditingTransaction;
    private int updatedTransactionSectionId;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle bundle = this.getArguments();

        final TransactionLegacy updatedTransactionData = (TransactionLegacy) bundle.get(TRANSACTION);
        FloatingActionButton fabAddTransaction = getActivity().findViewById(R.id.fab_addEditTransaction_done);

        twTransactionDate.setText(DatesUtils.formatDateToIsoString(new Date()));

        isEditingTransaction = updatedTransactionData != null;

        setKeyboardVisibilityListener(this, (ViewGroup) getView().getParent());

        fabAddTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sum = etTransactionSum.getText().toString();
                String title = etTransactionTitle.getText().toString();
                String description = etTransactionDesc.getText().toString();

                if (!sum.isEmpty() && selectedSection != null) {
                    TransactionLegacy transaction = TransactionLegacy.builder()
                            .date(transactionDate)
                            .sum(Integer.parseInt(sum))
                            .description(description)
                            .title(title)
                            .sectionId(selectedSection.getSectionId())
                            .build();
                    if (isEditingTransaction) {
                        presenter.editTransaction(updatedTransactionData.getId(), transaction);
                    } else {
                        presenter.addTransaction(transaction);
                    }
                    hideSoftKeyboard(getActivity());
                    getActivity().finish();
                }
            }
        });

        final DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String date = getIsoDate(year, month + 1, dayOfMonth);
                twTransactionDate.setText(date);
                transactionDate = date;
            }
        };

        twTransactionDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar;
                if (isEditingTransaction) {
                    calendar = getCalendarFromString(updatedTransactionData.getDate());
                } else {
                    calendar = Calendar.getInstance();
                }
                new DatePickerDialog(getActivity(), onDateSetListener,
                        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        ssAdapter = new SpendingSectionRVSingleChooseAdapter(getActivity(), spendingSectionsList, selectedRecyclerViewItem);
        ssAdapter.setClickListener(this);
        rvTransactionSection.setAdapter(ssAdapter);
        rvTransactionSection.setLayoutManager(new GridLayoutManager(getActivity(), 3));

        if (isEditingTransaction) {
            setUpdatedTransactionData(updatedTransactionData);
            fabAddTransaction.setImageResource(R.drawable.ic_edit_white_24dp);
        }
        presenter.requestSpendingSectionsList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.addedittransaction_frag, container, false);

        etTransactionSum = root.findViewById(R.id.ae_transaction_sum);
        etTransactionTitle = root.findViewById(R.id.ae_transaction_title);
        etTransactionDesc = root.findViewById(R.id.ae_transaction_desc);
        twTransactionDate = root.findViewById(R.id.ae_transaction_date);
        rvTransactionSection = root.findViewById(R.id.ae_transaction_section_recyclerview);

        return root;
    }

    private void setUpdatedTransactionData(TransactionLegacy transaction) {
        etTransactionSum.setText(String.valueOf(transaction.getSum()));
        etTransactionTitle.setText(transaction.getTitle());
        etTransactionDesc.setText(transaction.getDescription());
        twTransactionDate.setText(transaction.getDate());

        updatedTransactionSectionId = transaction.getSectionId();
    }

    @Override
    public void showErrorMessage(String msg) {
        System.out.println("showErrorMessage! " + msg);
        snackBarAction(getActivity().getApplicationContext(), msg);
    }

    @Override
    public void showAddSuccess() {
        snackBarAction(getActivity().getApplicationContext(), R.string.transaction_added, R.string.transaction_cancel);
    }

    @Override
    public void showEditSuccess() {
        final Context context = getActivity().getApplicationContext();
        final SnackbarWrapper snackbarWrapper = SnackbarWrapper.make(context,
                getContext().getText(R.string.transaction_edited), 3000);

        snackbarWrapper.setAction(getContext().getText(R.string.transaction_cancel),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, "CANCEL!!!",
                                Toast.LENGTH_SHORT).show();
                    }
                });

        snackbarWrapper.show();
    }

    @Override
    public void showSpendingSections(List<SpendingSection> spendingSections) {
        spendingSectionsList.clear();
        spendingSectionsList.addAll(spendingSections);

        if (isEditingTransaction) {
            int position = getPositionBySpendingSectionInnerId(spendingSectionsList, updatedTransactionSectionId);
            this.onItemClick(null, position);
        }
        ssAdapter.notifyDataSetChanged();
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
    public void onItemClick(View view, int position) {
        selectedSection = ssAdapter.getItem(position);
        System.out.println("item! = " + selectedSection);
        selectedRecyclerViewItem.set(position);
        ssAdapter.notifyItemChanged(position);
    }

    @Override
    public void onVisibilityChanged(boolean visible) {
        if (visible) {
            UiUtils.collapseView(rvTransactionSection, 100, 200);
        } else
            UiUtils.expandView(rvTransactionSection, 100, 500);
    }
}
