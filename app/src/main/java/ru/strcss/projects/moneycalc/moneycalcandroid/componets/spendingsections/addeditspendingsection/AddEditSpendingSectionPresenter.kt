package ru.strcss.projects.moneycalc.moneycalcandroid.componets.spendingsections.addeditspendingsection

import ru.strcss.projects.moneycalc.moneycalcandroid.App.getAppContext
import ru.strcss.projects.moneycalc.moneycalcandroid.api.MoneyCalcServerDAO
import ru.strcss.projects.moneycalc.moneycalcandroid.storage.EventBus
import ru.strcss.projects.moneycalc.moneycalcandroid.utils.ActivityUtils.Companion.snackBarAction
import ru.strcss.projects.moneycalc.moneycalcandroid.utils.events.CrudEvent.ADDED
import ru.strcss.projects.moneycalc.moneycalcandroid.utils.events.CrudEvent.EDITED
import ru.strcss.projects.moneycalc.moneycalcandroid.utils.logic.ComponentsUtils.Companion.getErrorBodyMessage
import ru.strcss.projects.moneycalc.moneycalcdto.dto.crudcontainers.settings.SpendingSectionUpdateContainer
import ru.strcss.projects.moneycalc.moneycalcdto.dto.crudcontainers.spendingsections.SpendingSectionsSearchRs
import ru.strcss.projects.moneycalc.moneycalcdto.entities.SpendingSection
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AddEditSpendingSectionPresenter @Inject
internal constructor(private val moneyCalcServerDAO: MoneyCalcServerDAO, private val eventBus: EventBus) : AddEditSpendingSectionContract.Presenter {

    private var view: AddEditSpendingSectionContract.View? = null

    override fun takeView(view: AddEditSpendingSectionContract.View) {
        this.view = view
    }

    override fun dropView() {
        view = null
    }

    override fun addSpendingSection(spendingSection: SpendingSection) {
        moneyCalcServerDAO.addSpendingSection(spendingSection)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<SpendingSectionsSearchRs> {
                    override fun onCompleted() {}

                    override fun onError(ex: Throwable) {
                        ex.printStackTrace()
                        snackBarAction(getAppContext(), getErrorBodyMessage(ex))
                        eventBus.addErrorEvent(getErrorBodyMessage(ex))
                    }

                    override fun onNext(addSectionRs: SpendingSectionsSearchRs) {
//                        if (addSectionRs.isSuccessful) {
                            eventBus.addSpendingSectionEvent(ADDED)
                            //                            view.showAddSuccess();
//                        } else {
//                            eventBus.addErrorEvent(addSectionRs.message)
                            //                            view.showErrorMessage(addSectionRs.getMessage());
//                        }
                    }
                })
    }

    override fun editSpendingSection(id: Int?, spendingSection: SpendingSection) {
        moneyCalcServerDAO.updateSpendingSection(SpendingSectionUpdateContainer(id, spendingSection))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<SpendingSectionsSearchRs> {
                    override fun onCompleted() {}

                    override fun onError(ex: Throwable) {
                        ex.printStackTrace()
                        snackBarAction(getAppContext(), getErrorBodyMessage(ex))
                        eventBus.addErrorEvent(getErrorBodyMessage(ex))
                    }

                    override fun onNext(editSectionRs: SpendingSectionsSearchRs) {
//                        if (editSectionRs.isSuccessful) {
                            eventBus.addSpendingSectionEvent(EDITED)
                            //                            view.showEditSuccess();
//                        } else {
//                            eventBus.addErrorEvent(editSectionRs.message)
                            //                            view.showErrorMessage(editSectionRs.getMessage());
//                        }
                    }
                })
    }
}
