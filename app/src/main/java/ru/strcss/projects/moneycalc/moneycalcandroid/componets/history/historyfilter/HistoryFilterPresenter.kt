package ru.strcss.projects.moneycalc.moneycalcandroid.componets.history.historyfilter

import ru.strcss.projects.moneycalc.moneycalcandroid.App.getAppContext
import ru.strcss.projects.moneycalc.moneycalcandroid.api.MoneyCalcServerDAO
import ru.strcss.projects.moneycalc.moneycalcandroid.storage.DataStorage
import ru.strcss.projects.moneycalc.moneycalcandroid.storage.EventBus
import ru.strcss.projects.moneycalc.moneycalcandroid.utils.ActivityUtils.snackBarAction
import ru.strcss.projects.moneycalc.moneycalcandroid.utils.events.CrudEvent
import ru.strcss.projects.moneycalc.moneycalcandroid.utils.logic.ComponentsUtils.Companion.getErrorBodyMessage
import ru.strcss.projects.moneycalc.moneycalcdto.dto.MoneyCalcRs
import ru.strcss.projects.moneycalc.moneycalcdto.dto.crudcontainers.transactions.TransactionsSearchFilterLegacy
import ru.strcss.projects.moneycalc.moneycalcdto.entities.SpendingSection
import ru.strcss.projects.moneycalc.moneycalcdto.entities.TransactionLegacy
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HistoryFilterPresenter @Inject
internal constructor(private val moneyCalcServerDAO: MoneyCalcServerDAO, private val dataStorage: DataStorage, private val eventBus: EventBus) : HistoryFilterContract.Presenter {

    private var view: HistoryFilterContract.View? = null

    override fun takeView(view: HistoryFilterContract.View) {
        this.view = view
    }

    override fun dropView() {
        view = null
    }

    override fun requestFilteredTransactions(filter: TransactionsSearchFilterLegacy) {
        moneyCalcServerDAO.getTransactions(filter)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<MoneyCalcRs<MutableList<TransactionLegacy>>> {
                    override fun onCompleted() {}

                    override fun onError(ex: Throwable) {
                        println("requestTransactions onError!!!!! " + ex.message)
                        snackBarAction(getAppContext(), getErrorBodyMessage(ex))
                        //                        view.showErrorMessage(ex.getMessage());
                    }

                    override fun onNext(transactionsListRs: MoneyCalcRs<MutableList<TransactionLegacy>>) {
                        println("requestTransactions FILTERED! transactionsListRs = $transactionsListRs")
                        println("transactionsListRs.getPayload().size() = " + transactionsListRs.payload.size)
                        if (transactionsListRs.isSuccessful) {
                            dataStorage.transactionList = transactionsListRs.getPayload()
                            dataStorage.transactionsFilter = filter
                            eventBus.addTransactionEvent(CrudEvent.REQUESTED)
                        } else {
                            eventBus.addErrorEvent(transactionsListRs.message)
                        }
                    }
                })

    }

    override fun requestSpendingSectionsList() {
        moneyCalcServerDAO.spendingSections
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<MoneyCalcRs<List<SpendingSection>>> {
                    override fun onCompleted() {}

                    override fun onError(ex: Throwable) {
                        snackBarAction(getAppContext(), getErrorBodyMessage(ex))
                    }

                    override fun onNext(sectionsRs: MoneyCalcRs<List<SpendingSection>>) {
                        if (sectionsRs.isSuccessful) {
                            view!!.showSpendingSections(sectionsRs.payload)
                        } else {
                            view!!.showErrorMessage(sectionsRs.toString())
                        }
                    }
                })

    }
}
