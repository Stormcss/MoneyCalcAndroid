package ru.strcss.projects.moneycalc.moneycalcandroid.componets.addtransaction;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;
import moneycalcandroid.moneycalc.projects.strcss.ru.moneycalc.R;
import ru.strcss.projects.moneycalc.enitities.SpendingSection;
import ru.strcss.projects.moneycalc.enitities.Transaction;
import ru.strcss.projects.moneycalc.moneycalcandroid.utils.DatesUtils;
import ru.strcss.projects.moneycalc.moneycalcandroid.utils.view.SnackbarWrapper;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class AddEditTransactionFragment extends DaggerFragment implements AddEditTransactionContract.View {

    @Inject
    AddEditTransactionContract.Presenter presenter;

    @Inject
    public AddEditTransactionFragment() {
    }

    // UI references
    private EditText etTransactionSum;
    private EditText etTransactionDesc;
    private ListView lvTransactionSection;

    private SpendingSection selectedSection;
    private List<SpendingSection> spendingSectionsList = new ArrayList<>();
    private ArrayAdapter<SpendingSection> ssAdapter;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FloatingActionButton fabAddTransaction = getActivity().findViewById(R.id.fab_addEditTransaction_done);
        fabAddTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sum = etTransactionSum.getText().toString();
                String description = etTransactionDesc.getText().toString();

                if (!sum.isEmpty() && selectedSection != null) {
                    Transaction transaction = Transaction.builder()
                            .date(DatesUtils.formatDateToString(new Date()))
                            .sum(Integer.parseInt(sum))
                            .description(description)
                            .sectionID(selectedSection.getId())
                            .build();
                    presenter.addTransaction(transaction);
                    hideSoftKeyboard();
                    getActivity().finish();
                }
            }
        });

        ssAdapter = new SpendingSectionArrayAdapter(getActivity(), R.layout.addedittransaction_spendingsection_card, spendingSectionsList);
        lvTransactionSection.setAdapter(ssAdapter);

        lvTransactionSection.setClickable(true);
        lvTransactionSection.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("SELECTED_IS: " + parent.getItemAtPosition(position));
                selectedSection = (SpendingSection) parent.getItemAtPosition(position);
            }
        });
        presenter.requestSpendingSectionsList();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.addedittransaction_frag, container, false);

        etTransactionSum = root.findViewById(R.id.ae_transaction_sum);
        etTransactionDesc = root.findViewById(R.id.ae_transaction_desc);
        lvTransactionSection = root.findViewById(R.id.ae_transaction_section_listview);

        return root;
    }

    @Override
    public void showErrorMessage(String msg) {
        System.out.println("showErrorMessage! " + msg);
        Snackbar.make(getActivity().findViewById(android.R.id.content), msg, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showAddSuccess() {
        final Context context = getActivity().getApplicationContext();
        final SnackbarWrapper snackbarWrapper = SnackbarWrapper.make(context,
                getContext().getText(R.string.transaction_added), 5000);

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
    public void showEditSuccess() {
        Snackbar.make(getActivity().findViewById(android.R.id.home), "Edited successfully!", Snackbar.LENGTH_LONG)
                .show();
    }

    @Override
    public void showSpendingSections(List<SpendingSection> spendingSections) {
        spendingSectionsList.clear();
        spendingSectionsList.addAll(spendingSections);
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

    /**
     * Hides the soft keyboard
     */
    public void hideSoftKeyboard() {
        if (getActivity().getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        }
    }
}
