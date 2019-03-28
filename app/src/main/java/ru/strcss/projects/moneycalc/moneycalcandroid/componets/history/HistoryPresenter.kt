package ru.strcss.projects.moneycalc.moneycalcandroid.componets.history

import ru.strcss.projects.moneycalc.moneycalcandroid.App.getAppContext
import ru.strcss.projects.moneycalc.moneycalcandroid.api.MoneyCalcServerDAO
import ru.strcss.projects.moneycalc.moneycalcandroid.storage.DataStorage
import ru.strcss.projects.moneycalc.moneycalcandroid.storage.EventBus
import ru.strcss.projects.moneycalc.moneycalcandroid.utils.ActivityUtils.snackBarAction
import ru.strcss.projects.moneycalc.moneycalcandroid.utils.events.CrudEvent.*
import ru.strcss.projects.moneycalc.moneycalcandroid.utils.logic.ComponentsUtils.Companion.getErrorBodyMessage
import ru.strcss.projects.moneycalc.moneycalcdto.dto.MoneyCalcRs
import ru.strcss.projects.moneycalc.moneycalcdto.entities.TransactionLegacy
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HistoryPresenter @Inject
internal constructor(private val moneyCalcServerDAO: MoneyCalcServerDAO,
                     private val dataStorage: DataStorage,
                     private val eventBus: EventBus) : HistoryContract.Presenter {

    private var view: HistoryContract.View? = null

    init {

        eventBus.subscribeTransactionEvent()
                .subscribe { transactionEvent ->
                    if (transactionEvent == ADDED || transactionEvent == DELETED
                            || transactionEvent == EDITED)
                        requestTransactions()
                }

        eventBus.subscribeTransactionEvent()
                .subscribe { transactionEvent ->
                    if (transactionEvent == REQUESTED) {
                        view?.showTransactions(dataStorage.transactionList)
                        view?.showFilterWindow()

                    }
                }
    }

    override fun takeView(view: HistoryContract.View) {
        this.view = view
    }

    override fun dropView() {
        view = null
    }

    override fun requestTransactions() {
        moneyCalcServerDAO.transactions
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
                        println("requestTransactions. transactionsListRs = $transactionsListRs")
                        if (transactionsListRs.isSuccessful) {
                            dataStorage.transactionList = transactionsListRs.payload
                            view?.showTransactions(transactionsListRs.payload)
                        } else {
                            eventBus.addErrorEvent(transactionsListRs.message)
                            //                            view.showErrorMessage(transactionsListRs.getMessage());
                        }
                        view?.hideSpinner()
                    }
                })
    }

    override fun deleteTransaction(id: Int?) {

        moneyCalcServerDAO.deleteTransaction(id)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<MoneyCalcRs<Void>> {
                    override fun onCompleted() {}

                    override fun onError(ex: Throwable) {
                        println("requestTransactions onError!!!!! " + ex.message)
                        snackBarAction(getAppContext(), getErrorBodyMessage(ex))
                        //                        view.showErrorMessage(ex.getMessage());
                    }

                    override fun onNext(rs: MoneyCalcRs<Void>) {
                        println("requestTransactions. rs = $rs")
                        if (rs.isSuccessful) {
                            dataStorage.transactionList!!.clear()
                            eventBus.addTransactionEvent(DELETED)
                            view!!.showDeleteSuccess()
                            //                            view.showDeleteSuccess(getContext().getString(R.string.transaction_delete_success));
                        } else {
                            view!!.showErrorMessage(rs.message)
                        }
                        view!!.hideSpinner()
                    }
                })

    }
}
