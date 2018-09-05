package ru.strcss.projects.moneycalc.moneycalcandroid.componets.spendingsections.addeditspendingsection;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;
import moneycalcandroid.moneycalc.projects.strcss.ru.moneycalc.R;
import ru.strcss.projects.moneycalc.enitities.SpendingSection;
import ru.strcss.projects.moneycalc.moneycalcandroid.componets.spendingsections.SpendingSectionsContract;
import ru.strcss.projects.moneycalc.moneycalcandroid.utils.view.SnackbarWrapper;

import static ru.strcss.projects.moneycalc.moneycalcandroid.AppConstants.SPENDING_SECTION;
import static ru.strcss.projects.moneycalc.moneycalcandroid.utils.ActivityUtils.hideSoftKeyboard;

public class AddEditSpendingSectionFragment extends DaggerFragment implements AddEditSpendingSectionContract.View {

    @Inject
    AddEditSpendingSectionContract.Presenter presenter;

    @Inject
    SpendingSectionsContract.Presenter spendingSectionsPresenter;

    @Inject
    public AddEditSpendingSectionFragment() {
    }

    // UI references
    private EditText etSpendingSectionBudget;
    private EditText etSpendingSectionName;
//    private Check

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle bundle = this.getArguments();

        final SpendingSection updatedSectionData = (SpendingSection) bundle.get(SPENDING_SECTION);
        FloatingActionButton fabAddSpendingSection = getActivity().findViewById(R.id.fab_addEditSpendingSection_done);

        final boolean isEditingSpendingSection = updatedSectionData != null;

        if (isEditingSpendingSection) {
            setUpdatedSpendingSectionData(updatedSectionData);
            fabAddSpendingSection.setImageResource(R.drawable.ic_edit_white_24dp);
        }

        fabAddSpendingSection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etSpendingSectionName.getText().toString();
                String budget = etSpendingSectionBudget.getText().toString();

                if (!name.isEmpty() && !budget.isEmpty()) {
                    SpendingSection spendingSection = SpendingSection.builder()
                            .name(name)
                            .budget(Integer.parseInt(budget))
                            .isAdded(true)
                            .build();
                    if (isEditingSpendingSection) {
                        presenter.editSpendingSection(updatedSectionData.getId(), spendingSection);
                    } else {
                        presenter.addSpendingSection(spendingSection);
                    }
                    hideSoftKeyboard(getActivity());
                    getActivity().finish();
                }
            }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.addeditspendingsection_frag, container, false);

        etSpendingSectionName = root.findViewById(R.id.ae_spening_section_name);
        etSpendingSectionBudget = root.findViewById(R.id.ae_spening_section_budget);

        return root;
    }

    private void setUpdatedSpendingSectionData(SpendingSection selectedSection) {
        etSpendingSectionName.setText(String.valueOf(selectedSection.getName()));
        etSpendingSectionBudget.setText(String.valueOf(selectedSection.getBudget()));
    }

    @Override
    public void showErrorMessage(String msg) {
        System.out.println("showErrorMessage! " + msg);
        Snackbar.make(getActivity().findViewById(android.R.id.content), msg, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showAddSuccess() {
        spendingSectionsPresenter.requestSpendingSections();

        final Context context = getActivity().getApplicationContext();
        final SnackbarWrapper snackbarWrapper = SnackbarWrapper.make(context,
                getContext().getText(R.string.transaction_added), 3000);

        snackbarWrapper.setAction(getContext().getText(R.string.cancel),
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
        spendingSectionsPresenter.requestSpendingSections();

        final Context context = getActivity().getApplicationContext();
        final SnackbarWrapper snackbarWrapper = SnackbarWrapper.make(context,
                getContext().getText(R.string.spending_section_edit_success), 3000);

        snackbarWrapper.setAction(getContext().getText(R.string.cancel),
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
