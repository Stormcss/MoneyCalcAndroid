package ru.strcss.projects.moneycalc.moneycalcandroid.componets.login

import retrofit2.Response
import ru.strcss.projects.moneycalc.moneycalcandroid.App.getAppContext
import ru.strcss.projects.moneycalc.moneycalcandroid.api.MoneyCalcServerDAO
import ru.strcss.projects.moneycalc.moneycalcandroid.storage.DataStorage
import ru.strcss.projects.moneycalc.moneycalcandroid.utils.ActivityUtils.Companion.snackBarAction
import ru.strcss.projects.moneycalc.moneycalcandroid.utils.logic.ComponentsUtils.Companion.getErrorBodyMessage
import ru.strcss.projects.moneycalc.moneycalcdto.dto.Credentials
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

class RegisterPresenter @Inject
internal constructor(private val moneyCalcServerDAO: MoneyCalcServerDAO) : RegisterContract.Presenter {

    @Inject
    lateinit var dataStorage: DataStorage

    private var registerView: RegisterContract.View? = null

    override fun attemptRegister(credentials: Credentials) {
        moneyCalcServerDAO.registerPerson(credentials)
                .subscribeOn(Schedulers.io())
                .flatMap { loginRs ->
                    println("loginRs = $loginRs")
//                    if (loginRs.isSuccessful) {
                    moneyCalcServerDAO.login(credentials.access)
//                    } else {
//                        throw RuntimeException(loginRs.message)
//                    }
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Response<Void>> {
                    override fun onCompleted() {}

                    override fun onError(ex: Throwable) {
                        registerView!!.hideSpinner()
                        snackBarAction(getAppContext(), getErrorBodyMessage(ex))
                    }

                    override fun onNext(rs: Response<Void>) {
                        if (rs.isSuccessful) {
                            val token = rs.headers().get("Authorization")
                            moneyCalcServerDAO.token = token

                            val login = credentials.access.login
                            dataStorage.activeUserData.userLogin = login
                            registerView!!.saveLoginToPreferences(login)
                            println("token = " + token!!)

                            registerView!!.showMainActivity()
                        } else {
                            println("rs = $rs")
                            registerView!!.showErrorMessage(rs.message())
                        }
                        registerView!!.hideSpinner()
                    }
                })
    }

    override fun takeView(view: RegisterContract.View) {
        registerView = view
    }

    override fun dropView() {
        registerView = null
    }
}
