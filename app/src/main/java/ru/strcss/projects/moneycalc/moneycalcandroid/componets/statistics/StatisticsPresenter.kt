package ru.strcss.projects.moneycalc.moneycalcandroid.componets.statistics

import ru.strcss.projects.moneycalc.moneycalcandroid.App.getAppContext
import ru.strcss.projects.moneycalc.moneycalcandroid.api.MoneyCalcServerDAO
import ru.strcss.projects.moneycalc.moneycalcandroid.storage.DataStorage
import ru.strcss.projects.moneycalc.moneycalcandroid.utils.ActivityUtils.Companion.snackBarAction
import ru.strcss.projects.moneycalc.moneycalcandroid.utils.logic.ComponentsUtils.Companion.getErrorBodyMessage
import ru.strcss.projects.moneycalc.moneycalcdto.dto.crudcontainers.spendingsections.SpendingSectionsSearchRs
import ru.strcss.projects.moneycalc.moneycalcdto.entities.SettingsLegacy
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Stormcss
 * Date: 29.05.2019
 */
@Singleton
class StatisticsPresenter @Inject
internal constructor(private val moneyCalcServerDAO: MoneyCalcServerDAO,
                     private val dataStorage: DataStorage) : StatisticsContract.Presenter {
    private var view: StatisticsContract.View? = null

    override fun takeView(view: StatisticsContract.View) {
        this.view = view
    }

    override fun dropView() {
        view = null
    }

    override fun requestSettings() {
        moneyCalcServerDAO.settings
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<SettingsLegacy> {
                    override fun onCompleted() {}

                    override fun onError(ex: Throwable) {

                        println("requestSettings onError!!!!! " + ex.message)
                        val message = getErrorBodyMessage(ex)
                        snackBarAction(getAppContext(), message)
                        println("getErrorBodyMessage(ex) - " + message)
                    }

                    override fun onNext(settings: SettingsLegacy) {
                        println("requestSettings. statsItems = $settings")

                        dataStorage.settings = settings

                        view?.setUpStatisticsPeriodData(settings)
                        view?.drawStatisticsFragments()
//                        view?.hideSpinner()
                    }
                })
    }

    override fun requestSpendingSections() {
        moneyCalcServerDAO.spendingSections
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<SpendingSectionsSearchRs> {
                    override fun onCompleted() {}

                    override fun onError(ex: Throwable) {

                        println("requestSpendingSections onError!!!!! " + ex.message)
                        val message = getErrorBodyMessage(ex)
                        snackBarAction(getAppContext(), message)
                        println("getErrorBodyMessage(ex) - " + message)
                    }

                    override fun onNext(spendingSectionsSearchRs: SpendingSectionsSearchRs) {
                        println("requestSpendingSections. spendingSectionsSearchRs = $spendingSectionsSearchRs")

                        dataStorage.spendingSections = spendingSectionsSearchRs

                        view?.drawStatisticsFragments()
//                        view?.hideSpinner()
                    }
                })

    }

}
