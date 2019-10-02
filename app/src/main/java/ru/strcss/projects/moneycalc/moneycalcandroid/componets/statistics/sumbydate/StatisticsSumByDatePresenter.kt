package ru.strcss.projects.moneycalc.moneycalcandroid.componets.statistics.sumbydate

import retrofit2.HttpException
import ru.strcss.projects.moneycalc.moneycalcandroid.App.getAppContext
import ru.strcss.projects.moneycalc.moneycalcandroid.api.MoneyCalcServerDAO
import ru.strcss.projects.moneycalc.moneycalcandroid.utils.ActivityUtils.Companion.snackBarAction
import ru.strcss.projects.moneycalc.moneycalcandroid.utils.logic.ComponentsUtils.Companion.getErrorBodyMessage
import ru.strcss.projects.moneycalc.moneycalcdto.dto.crudcontainers.ItemsContainer
import ru.strcss.projects.moneycalc.moneycalcdto.dto.crudcontainers.statistics.StatisticsFilterLegacy
import ru.strcss.projects.moneycalc.moneycalcdto.entities.statistics.SumByDateLegacy
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Stormcss
 * Date: 30.05.2019
 */
@Singleton
class StatisticsSumByDatePresenter @Inject
internal constructor(private val moneyCalcServerDAO: MoneyCalcServerDAO) : StatisticsSumByDateContract.Presenter {
    private var view: StatisticsSumByDateContract.View? = null

    private val filter: StatisticsFilterLegacy = StatisticsFilterLegacy()

    override fun takeView(view: StatisticsSumByDateContract.View) {
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
            requestStatsByDateSum()
    }

    override fun requestStatsByDateSum() {
        if (filter.isValid.isValidated)
            moneyCalcServerDAO.getStatsByDateSum(filter)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Observer<ItemsContainer<SumByDateLegacy>> {
                        override fun onCompleted() {}

                        override fun onError(ex: Throwable) {

                            println("requestStatsByDateSum onError!!!!! " + ex.message)
                            val message = getErrorBodyMessage(ex)
                            snackBarAction(getAppContext(), message)
                            println("getErrorBodyMessage(ex) - " + message)

                            if (ex is HttpException && ex.code() == 403) {
                                moneyCalcServerDAO.token = null
                                view?.navigateToLoginActivity()
                            }
                        }

                        override fun onNext(statsItems: ItemsContainer<SumByDateLegacy>) {
                            println("requestStatsBySectionSum. statsItems = $statsItems")

                            view?.showStatsSumByDate(statsItems)
                            view?.hideSpinner()
                        }
                    })
    }
}
