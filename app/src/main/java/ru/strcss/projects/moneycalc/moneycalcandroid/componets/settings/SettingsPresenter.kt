package ru.strcss.projects.moneycalc.moneycalcandroid.componets.settings

import ru.strcss.projects.moneycalc.moneycalcandroid.App.getAppContext
import ru.strcss.projects.moneycalc.moneycalcandroid.api.MoneyCalcServerDAO
import ru.strcss.projects.moneycalc.moneycalcandroid.storage.EventBus
import ru.strcss.projects.moneycalc.moneycalcandroid.utils.ActivityUtils.snackBarAction
import ru.strcss.projects.moneycalc.moneycalcandroid.utils.events.Event.SETTING_UPDATED
import ru.strcss.projects.moneycalc.moneycalcandroid.utils.logic.ComponentsUtils.Companion.getErrorBodyMessage
import ru.strcss.projects.moneycalc.moneycalcdto.dto.MoneyCalcRs
import ru.strcss.projects.moneycalc.moneycalcdto.entities.SettingsLegacy
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsPresenter @Inject
internal constructor(private val moneyCalcServerDAO: MoneyCalcServerDAO, private val eventBus: EventBus) : SettingsContract.Presenter {

    private var view: SettingsContract.View? = null

    override fun takeView(view: SettingsContract.View) {
        this.view = view
    }

    override fun dropView() {
        view = null
    }

    override fun updateSettings(settings: SettingsLegacy) {
        moneyCalcServerDAO.updateSettings(settings)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<MoneyCalcRs<SettingsLegacy>> {
                    override fun onCompleted() {}

                    override fun onError(ex: Throwable) {
                        ex.printStackTrace()
                        snackBarAction(getAppContext(), getErrorBodyMessage(ex))
                    }

                    override fun onNext(updateRs: MoneyCalcRs<SettingsLegacy>) {
                        if (updateRs.isSuccessful) {
                            eventBus.addEvent(SETTING_UPDATED)
                            view?.showUpdateSuccess()
                        } else {
                            eventBus.addErrorEvent(updateRs.message)
                        }
                    }
                })
    }
}
