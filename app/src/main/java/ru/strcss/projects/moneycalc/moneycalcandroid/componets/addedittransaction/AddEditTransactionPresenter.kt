package ru.strcss.projects.moneycalc.moneycalcandroid.componets.addedittransaction

import ru.strcss.projects.moneycalc.moneycalcandroid.App.getAppContext
import ru.strcss.projects.moneycalc.moneycalcandroid.api.MoneyCalcServerDAO
import ru.strcss.projects.moneycalc.moneycalcandroid.storage.EventBus
import ru.strcss.projects.moneycalc.moneycalcandroid.utils.ActivityUtils.snackBarAction
import ru.strcss.projects.moneycalc.moneycalcandroid.utils.events.CrudEvent.ADDED
import ru.strcss.projects.moneycalc.moneycalcandroid.utils.events.CrudEvent.EDITED
import ru.strcss.projects.moneycalc.moneycalcandroid.utils.logic.ComponentsUtils.Companion.getErrorBodyMessage
import ru.strcss.projects.moneycalc.moneycalcdto.dto.MoneyCalcRs
import ru.strcss.projects.moneycalc.moneycalcdto.dto.crudcontainers.transactions.TransactionUpdateContainerLegacy
import ru.strcss.projects.moneycalc.moneycalcdto.entities.SpendingSection
import ru.strcss.projects.moneycalc.moneycalcdto.entities.TransactionLegacy
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AddEditTransactionPresenter @Inject
internal constructor(private val moneyCalcServerDAO: MoneyCalcServerDAO, private val eventBus: EventBus) : AddEditTransactionContract.Presenter {

    private var view: AddEditTransactionContract.View? = null

    override fun takeView(view: AddEditTransactionContract.View) {
        this.view = view
    }

    override fun dropView() {
        view = null
    }

    override fun addTransaction(transaction: TransactionLegacy) {
        moneyCalcServerDAO.addTransaction(transaction)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<MoneyCalcRs<TransactionLegacy>> {
                    override fun onCompleted() {}

                    override fun onError(ex: Throwable) {
                        ex.printStackTrace()
                        snackBarAction(getAppContext(), getErrorBodyMessage(ex))
                        //                        showErrorMessageFromException(ex, view);
                        //                        String errorBodyMessage = getErrorBodyMessage((HttpException) ex);
                        //                        System.err.println("showAddTransactionActivity onErro: " + errorBodyMessage);
                        //                        view.showErrorMessage(errorBodyMessage);
                    }

                    override fun onNext(transactionRs: MoneyCalcRs<TransactionLegacy>) {
                        if (transactionRs.isSuccessful) {
                            eventBus.addTransactionEvent(ADDED)
                            if (view != null) {
                                view!!.showAddSuccess()
                            }
                        } else {
                            eventBus.addErrorEvent(transactionRs.message)
                            //                            System.err.println(transactionRs);
                            //                            view.showErrorMessage(transactionRs.toString());
                        }
                    }
                })
    }

    override fun editTransaction(id: Int?, transaction: TransactionLegacy) {
        val container = TransactionUpdateContainerLegacy(id, transaction)
        moneyCalcServerDAO.updateTransaction(container)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<MoneyCalcRs<TransactionLegacy>> {
                    override fun onCompleted() {}

                    override fun onError(ex: Throwable) {
                        snackBarAction(getAppContext(), getErrorBodyMessage(ex))
                        //                        showErrorMessageFromException(ex, view);
                        //                        view.showErrorMessage(ex.getMessage());
                        //                        ex.printStackTrace();
                    }

                    override fun onNext(transactionRs: MoneyCalcRs<TransactionLegacy>) {
                        if (transactionRs.isSuccessful) {
                            eventBus.addTransactionEvent(EDITED)
                            if (view != null) {
                                view!!.showEditSuccess()
                            }
                        } else {
                            eventBus.addErrorEvent(transactionRs.message)
                            //                            System.err.println(transactionRs);
                            //                            view.showErrorMessage(transactionRs.toString());
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
                        //                        showErrorMessageFromException(ex, view);
                        //                        view.showErrorMessage(ex.getMessage());
                        //                        ex.printStackTrace();
                    }

                    override fun onNext(sectionsRs: MoneyCalcRs<List<SpendingSection>>) {
                        if (sectionsRs.isSuccessful) {
                            view!!.showSpendingSections(sectionsRs.payload)
                            //                            view.showSpendingSections(sortSpendingSectionListById(sectionsRs.getPayload()));
                        } else {
                            view!!.showErrorMessage(sectionsRs.toString())
                        }
                    }
                })
    }

    private fun sortSpendingSectionListById(sectionList: List<SpendingSection>): List<SpendingSection> {
        Collections.sort(sectionList) { o1, o2 -> (o1.id!! - o2.id!!).toInt() }
        return sectionList
    }


}
