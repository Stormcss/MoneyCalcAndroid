package ru.strcss.projects.moneycalc.moneycalcandroid.componets.statistics.sumbysection

import retrofit2.HttpException
import ru.strcss.projects.moneycalc.moneycalcandroid.App.getAppContext
import ru.strcss.projects.moneycalc.moneycalcandroid.api.MoneyCalcServerDAO
import ru.strcss.projects.moneycalc.moneycalcandroid.storage.DataStorage
import ru.strcss.projects.moneycalc.moneycalcandroid.utils.ActivityUtils.Companion.snackBarAction
import ru.strcss.projects.moneycalc.moneycalcandroid.utils.logic.ComponentsUtils.Companion.getErrorBodyMessage
import ru.strcss.projects.moneycalc.moneycalcdto.dto.crudcontainers.ItemsContainer
import ru.strcss.projects.moneycalc.moneycalcdto.dto.crudcontainers.statistics.StatisticsFilterLegacy
import ru.strcss.projects.moneycalc.moneycalcdto.entities.statistics.SumBySection
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
class StatisticsSumBySectionPresenter @Inject
internal constructor(private val moneyCalcServerDAO: MoneyCalcServerDAO,
                     private val dataStorage: DataStorage) : StatisticsSumBySectionContract.Presenter {
    private var view: StatisticsSumBySectionContract.View? = null

    private val filter: StatisticsFilterLegacy = StatisticsFilterLegacy()

    override fun takeView(view: StatisticsSumBySectionContract.View) {
        this.view = view
    }

    override fun dropView() {
        view = null
    }

    override fun setFilter(filter: StatisticsFilterLegacy, isStatsUpdateRequired: Boolean) {
        this.filter.dateFrom = filter.dateFrom
        this.filter.dateTo = filter.dateTo
        this.filter.sectionIds = filter.sectionIds

        if (isStatsUpdateRequired)
            requestStatsSumBySection()
    }

    override fun requestStatsSumBySection() {
        moneyCalcServerDAO.getStatsBySectionSum(filter)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<ItemsContainer<SumBySection>> {
                    override fun onCompleted() {}

                    override fun onError(ex: Throwable) {

                        println("requestStatsBySectionSum onError!!!!! " + ex.message)
                        val message = getErrorBodyMessage(ex)
                        snackBarAction(getAppContext(), message)
                        println("getErrorBodyMessage(ex) - " + message)

                        if (ex is HttpException && ex.code() == 403) {
                            moneyCalcServerDAO.token = null
                            view?.navigateToLoginActivity()
                        }
                    }

                    override fun onNext(statsItems: ItemsContainer<SumBySection>) {
                        println("requestStatsBySectionSum. statsItems = $statsItems")

                        view?.showStatsSumBySection(statsItems)
                        view?.hideSpinner()
                    }
                })
    }

//    override fun requestStatsSumBySection(filter: StatisticsFilterLegacy) {
//        moneyCalcServerDAO.getStatsBySectionSum(filter)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(object : Observer<ItemsContainer<SumBySection>> {
//                    override fun onCompleted() {}
//
//                    override fun onError(ex: Throwable) {
//
//                        println("requestStatsBySectionSum onError!!!!! " + ex.message)
//                        val message = getErrorBodyMessage(ex)
//                        snackBarAction(getAppContext(), message)
//                        println("getErrorBodyMessage(ex) - " + message)
//
//                        if (ex is HttpException && ex.code() == 403) {
//                            moneyCalcServerDAO.token = null
//                            view?.navigateToLoginActivity()
//                        }
//                    }
//
//                    override fun onNext(statsItems: ItemsContainer<SumBySection>) {
//                        println("requestStatsBySectionSum. statsItems = $statsItems")
//
//                        view?.showStatsSumBySection(statsItems)
//                        view?.hideSpinner()
//                    }
//                })
//    }
}
