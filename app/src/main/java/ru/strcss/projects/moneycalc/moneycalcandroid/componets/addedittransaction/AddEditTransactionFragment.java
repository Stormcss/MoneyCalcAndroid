package ru.strcss.projects.moneycalc.moneycalcandroid.componets.addedittransaction;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import static ru.strcss.projects.moneycalc.moneycalcandroid.AppConstants.TRANSACTION;
import static ru.strcss.projects.moneycalc.moneycalcandroid.utils.ActivityUtils.hideSoftKeyboard;
import static ru.strcss.projects.moneycalc.moneycalcandroid.utils.ActivityUtils.snackBarAction;

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

        Bundle bundle = this.getArguments();

        final Transaction updatedTransactionData = (Transaction) bundle.get(TRANSACTION);
        FloatingActionButton fabAddTransaction = getActivity().findViewById(R.id.fab_addEditTransaction_done);

        final boolean isEditingTransaction = updatedTransactionData != null;

        if (isEditingTransaction) {
            setUpdatedTransactionData(updatedTransactionData);
            fabAddTransaction.setImageResource(R.drawable.ic_edit_white_24dp);
        }

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
                    if (isEditingTransaction) {
                        presenter.editTransaction(updatedTransactionData.get_id(), transaction);
                    } else {
                        presenter.addTransaction(transaction);
                    }
                    hideSoftKeyboard(getActivity());
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

    private void setUpdatedTransactionData(Transaction transaction) {
        etTransactionSum.setText(String.valueOf(transaction.getSum()));
        etTransactionDesc.setText(transaction.getDescription());
    }

    @Override
    public void showErrorMessage(String msg) {
        System.out.println("showErrorMessage! " + msg);
        Snackbar.make(getActivity().findViewById(android.R.id.content), msg, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showAddSuccess() {
        snackBarAction(getActivity().getApplicationContext(), R.string.transaction_added, R.string.transaction_cancel);

        //        final Context context = getActivity().getApplicationContext();
//        final SnackbarWrapper snackbarWrapper = SnackbarWrapper.make(context,
//                getContext().getText(R.string.transaction_added), 3000);
//
//        snackbarWrapper.setAction(getContext().getText(R.string.transaction_cancel),
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Toast.makeText(context, "CANCEL!!!",
//                                Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//        snackbarWrapper.show();
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

//    /**
//     * Hides the soft keyboard
//     */
//    public void hideSoftKeyboard() {
//        if (getActivity().getCurrentFocus() != null) {
//            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
//            inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
//        }
//    }
}
