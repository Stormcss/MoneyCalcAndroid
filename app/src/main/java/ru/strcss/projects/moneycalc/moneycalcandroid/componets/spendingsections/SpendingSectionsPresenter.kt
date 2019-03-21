package ru.strcss.projects.moneycalc.moneycalcandroid.componets.spendingsections

import moneycalcandroid.moneycalc.projects.strcss.ru.moneycalc.R
import ru.strcss.projects.moneycalc.moneycalcandroid.App.getAppContext
import ru.strcss.projects.moneycalc.moneycalcandroid.api.MoneyCalcServerDAO
import ru.strcss.projects.moneycalc.moneycalcandroid.storage.DataStorage
import ru.strcss.projects.moneycalc.moneycalcandroid.storage.EventBus
import ru.strcss.projects.moneycalc.moneycalcandroid.utils.ActivityUtils.snackBarAction
import ru.strcss.projects.moneycalc.moneycalcandroid.utils.events.CrudEvent.ADDED
import ru.strcss.projects.moneycalc.moneycalcandroid.utils.events.CrudEvent.EDITED
import ru.strcss.projects.moneycalc.moneycalcandroid.utils.logic.ComponentsUtils.getErrorBodyMessage
import ru.strcss.projects.moneycalc.moneycalcdto.dto.MoneyCalcRs
import ru.strcss.projects.moneycalc.moneycalcdto.entities.SpendingSection
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SpendingSectionsPresenter @Inject
internal constructor(private val moneyCalcServerDAO: MoneyCalcServerDAO, private val dataStorage: DataStorage, private val eventBus: EventBus) : SpendingSectionsContract.Presenter {

    private var view: SpendingSectionsContract.View? = null

    init {

        eventBus.subscribeSpendingSectionEvent()
                .subscribe { sectionEvent ->
                    requestSpendingSections()
                    if (sectionEvent == ADDED) {
                        snackBarAction(R.string.spending_section_added)
                    } else if (sectionEvent == EDITED) {
                        snackBarAction(R.string.spending_section_edit_success)
                    }
                }
    }

    override fun takeView(view: SpendingSectionsContract.View) {
        this.view = view
    }

    override fun dropView() {
        view = null
    }

    override fun requestSpendingSections() {
        moneyCalcServerDAO.spendingSections
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<MoneyCalcRs<List<SpendingSection>>> {
                    override fun onCompleted() {}

                    override fun onError(ex: Throwable) {
                        ex.printStackTrace()
                        snackBarAction(getAppContext(), getErrorBodyMessage(ex))
                        //                        showErrorMessageFromException(ex, view);
                    }

                    override fun onNext(getSpendingSectionsRs: MoneyCalcRs<List<SpendingSection>>) {
                        println("getSpendingSectionsRs = $getSpendingSectionsRs")
                        if (getSpendingSectionsRs.isSuccessful) {
                            dataStorage.spendingSections = getSpendingSectionsRs.payload
                            view!!.showSpendingSections(getSpendingSectionsRs.payload)
                        } else {
                            view!!.showErrorMessage(getSpendingSectionsRs.toString())
                        }
                        view!!.hideSpinner()
                    }
                })
    }

    override fun deleteSpendingSection(id: Int?) {
        moneyCalcServerDAO.deleteSpendingSection(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<MoneyCalcRs<List<SpendingSection>>> {
                    override fun onCompleted() {}

                    override fun onError(ex: Throwable) {
                        ex.printStackTrace()
                        snackBarAction(getAppContext(), getErrorBodyMessage(ex))
                        //                        snackBarAction(getActivity().getApplicationContext(), msg);
                    }

                    override fun onNext(sectionRs: MoneyCalcRs<List<SpendingSection>>) {
                        if (sectionRs.isSuccessful) {
                            //                            eventBus.addSpendingSectionEvent(DELETED);
                            view!!.showDeleteSuccess()
                            //                            System.out.println("deleteSpendingSection! sectionRs.getPayload() = " + sectionRs.getPayload());
                            //                            snackBarAction(view.getContext(), sectionRs.toString());
                        } else {
                            view!!.showErrorMessage(sectionRs.toString())
                            //                            snackBarAction(view.getContext(), sectionRs.toString());
                        }
                    }
                })
    }
}
