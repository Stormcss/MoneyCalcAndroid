package ru.strcss.projects.moneycalc.moneycalcandroid.componets.history

import retrofit2.HttpException
import ru.strcss.projects.moneycalc.moneycalcandroid.App.getAppContext
import ru.strcss.projects.moneycalc.moneycalcandroid.api.MoneyCalcServerDAO
import ru.strcss.projects.moneycalc.moneycalcandroid.storage.DataStorage
import ru.strcss.projects.moneycalc.moneycalcandroid.storage.EventBus
import ru.strcss.projects.moneycalc.moneycalcandroid.utils.ActivityUtils.Companion.snackBarAction
import ru.strcss.projects.moneycalc.moneycalcandroid.utils.events.CrudEvent.*
import ru.strcss.projects.moneycalc.moneycalcandroid.utils.logic.ComponentsUtils.Companion.getErrorBodyMessage
import ru.strcss.projects.moneycalc.moneycalcdto.dto.crudcontainers.transactions.TransactionsSearchLegacyRs
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
                        view?.showTransactions(dataStorage.transactions)
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
                .subscribe(object : Observer<TransactionsSearchLegacyRs> {
                    override fun onCompleted() {}

                    override fun onError(ex: Throwable) {
                        println("requestTransactions onError!!!!! " + ex.message)
                        val message = getErrorBodyMessage(ex)
                        snackBarAction(getAppContext(), message)
                        println("getErrorBodyMessage(ex) - " + message)

                        eventBus.addErrorEvent(message)

                        if (ex is HttpException && ex.code() == 403) {
                            moneyCalcServerDAO.token = null
                            view?.navigateToLoginActivity()
                        }

                    }

                    override fun onNext(transactionsSearchRs: TransactionsSearchLegacyRs) {
                        println("requestTransactions. transactionsListRs = $transactionsSearchRs")

                        dataStorage.transactions = transactionsSearchRs
                        view?.showTransactions(transactionsSearchRs)
                        view?.hideSpinner()
                    }
                })
    }

    override fun deleteTransaction(id: Int?) {
        moneyCalcServerDAO.deleteTransaction(id)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Void> {
                    override fun onCompleted() {}

                    override fun onError(ex: Throwable) {
                        println("deleteTransaction onError!!!!! " + ex.message)
                        snackBarAction(getAppContext(), getErrorBodyMessage(ex))
                        //                        view.showErrorMessage(ex.getMessage());
                    }

                    override fun onNext(rs: Void?) {
//                        if (rs.isSuccessful) {
                        dataStorage.transactions?.items?.clear()
                        eventBus.addTransactionEvent(DELETED)
                        view?.showDeleteSuccess()
                        //                            view.showDeleteSuccess(getContext().getString(R.string.transaction_delete_success));
//                        } else {
//                            view!!.showErrorMessage(rs.message)
//                        }
                        view?.hideSpinner()
                    }
                })

    }
}
