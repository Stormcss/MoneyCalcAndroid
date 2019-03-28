package ru.strcss.projects.moneycalc.moneycalcandroid.componets.home

import ru.strcss.projects.moneycalc.moneycalcandroid.App.getAppContext
import ru.strcss.projects.moneycalc.moneycalcandroid.api.MoneyCalcServerDAO
import ru.strcss.projects.moneycalc.moneycalcandroid.storage.DataStorage
import ru.strcss.projects.moneycalc.moneycalcandroid.storage.EventBus
import ru.strcss.projects.moneycalc.moneycalcandroid.utils.ActivityUtils.snackBarAction
import ru.strcss.projects.moneycalc.moneycalcandroid.utils.events.CrudEvent
import ru.strcss.projects.moneycalc.moneycalcandroid.utils.events.Event.SETTING_UPDATED
import ru.strcss.projects.moneycalc.moneycalcandroid.utils.logic.ComponentsUtils.Companion.getErrorBodyMessage
import ru.strcss.projects.moneycalc.moneycalcdto.dto.MoneyCalcRs
import ru.strcss.projects.moneycalc.moneycalcdto.dto.crudcontainers.statistics.FinanceSummaryFilter
import ru.strcss.projects.moneycalc.moneycalcdto.dto.crudcontainers.statistics.FinanceSummaryFilterLegacy
import ru.strcss.projects.moneycalc.moneycalcdto.entities.FinanceSummaryBySection
import ru.strcss.projects.moneycalc.moneycalcdto.entities.SettingsLegacy
import ru.strcss.projects.moneycalc.moneycalcdto.entities.SpendingSection
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomePresenter @Inject
internal constructor(private val moneyCalcServerDAO: MoneyCalcServerDAO,
                     private val dataStorage: DataStorage,
                     private val eventBus: EventBus) : HomeContract.Presenter {

    private var homeView: HomeContract.View? = null

    init {

        subscribeEvents()
    }

    override fun takeView(view: HomeContract.View) {
        homeView = view
    }

    override fun dropView() {
        homeView = null
    }

    override fun requestFinanceSummary(financeSummaryGetContainer: FinanceSummaryFilter) {}

    override fun requestSettings() {
        moneyCalcServerDAO.settings
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<MoneyCalcRs<SettingsLegacy>> {
                    override fun onCompleted() {}

                    override fun onError(ex: Throwable) {
                        ex.printStackTrace()
                        snackBarAction(getAppContext(), getErrorBodyMessage(ex))
                    }

                    override fun onNext(settingsRs: MoneyCalcRs<SettingsLegacy>) {
                        if (settingsRs.isSuccessful) {
                            dataStorage.settings = settingsRs.payload
                            if (homeView != null) {
                                homeView!!.showDatesRange(settingsRs.payload.periodFrom,
                                        settingsRs.payload.periodTo)
                            }
                        } else {
                            eventBus.addErrorEvent(settingsRs.message)
                            //                            homeView.showErrorMessage(settingsRs.toString());
                        }
                    }
                })
    }

    override fun requestSpendingSections() {
        moneyCalcServerDAO.spendingSections
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<MoneyCalcRs<List<SpendingSection>>> {

                    override fun onCompleted() {

                    }

                    override fun onError(ex: Throwable) {
                        ex.printStackTrace()
                        snackBarAction(getAppContext(), getErrorBodyMessage(ex))
                    }

                    override fun onNext(sectionsRs: MoneyCalcRs<List<SpendingSection>>) {
                        if (sectionsRs.isSuccessful) {
                            dataStorage.spendingSections = sectionsRs.payload
                            eventBus.receivedSpendingSectionEvent(CrudEvent.RECEIVED)
                        }
                    }
                })
    }

    override fun requestSectionStatistics() {
        moneyCalcServerDAO.financeSummaryBySection
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<MoneyCalcRs<List<FinanceSummaryBySection>>> {
                    override fun onCompleted() {}

                    override fun onError(ex: Throwable) {
                        ex.printStackTrace()
                        snackBarAction(getAppContext(), getErrorBodyMessage(ex))
                    }

                    override fun onNext(statsRs: MoneyCalcRs<List<FinanceSummaryBySection>>) {
                        if (statsRs.isSuccessful) {
                            dataStorage.financeSummary = statsRs.payload
                            if (homeView != null) {
                                homeView!!.showStatisticsSections(statsRs.payload)
                            }
                        } else {
                            eventBus.addErrorEvent(statsRs.message)
                        }
                    }
                })
    }

    override fun requestSectionStatistics(from: String, to: String, sections: List<Int>) {
        moneyCalcServerDAO.getFinanceSummaryBySection(FinanceSummaryFilterLegacy(from, to, sections))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<MoneyCalcRs<List<FinanceSummaryBySection>>> {
                    override fun onCompleted() {}

                    override fun onError(ex: Throwable) {
                        ex.printStackTrace()
                        snackBarAction(getAppContext(), getErrorBodyMessage(ex))
                    }

                    override fun onNext(statsRs: MoneyCalcRs<List<FinanceSummaryBySection>>) {
                        if (statsRs.isSuccessful) {
                            dataStorage.financeSummary = statsRs.payload
                            if (homeView != null) {
                                homeView!!.showStatisticsSections(statsRs.payload)
                            }
                        } else {
                            eventBus.addErrorEvent(statsRs.message)
                        }
                    }
                })
    }

    override fun showAddTransactionActivity() {
        if (homeView != null) {
            homeView!!.showAddTransactionActivity()
        }
    }

    private fun subscribeEvents() {
        this.eventBus.subscribeTransactionEvent().subscribe { event ->
            if (event == CrudEvent.ADDED || event == CrudEvent.DELETED ||
                    event == CrudEvent.EDITED) {
                requestSettings()
                requestSectionStatistics()
            }
        }
        this.eventBus.subscribeEvent().subscribe { event ->
            if (event == SETTING_UPDATED) {
                requestSettings()
                requestSectionStatistics()
            }
        }

        this.eventBus.subscribeSpendingSectionEvent().subscribe { event ->
            if (event == CrudEvent.RECEIVED) {
                homeView!!.redrawTabLogos()
            }
        }

    }
}
