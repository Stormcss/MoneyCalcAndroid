package ru.strcss.projects.moneycalc.moneycalcandroid.componets.login.applicationsettings

import ru.strcss.projects.moneycalc.moneycalcandroid.api.MoneyCalcServerDAO
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApplicationSettingsPresenter @Inject
internal constructor(private val moneyCalcServerDAO: MoneyCalcServerDAO) : ApplicationSettingsContract.Presenter {

    private var view: ApplicationSettingsContract.View? = null

    override fun takeView(view: ApplicationSettingsContract.View) {
        this.view = view
    }

    override fun dropView() {
        view = null
    }

    override fun updateServerIP(serverIP: String, serverPort: String) {
        moneyCalcServerDAO.saveServerIp(serverIP, serverPort)
    }
}
