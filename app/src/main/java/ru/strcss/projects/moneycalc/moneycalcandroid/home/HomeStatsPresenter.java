//package ru.strcss.projects.moneycalc.moneycalcandroid.home;
//
//import android.support.annotation.Nullable;
//
//import java.util.List;
//
//import javax.inject.Inject;
//import javax.inject.Singleton;
//
//import ru.strcss.projects.moneycalc.enitities.FinanceSummaryBySection;
//import ru.strcss.projects.moneycalc.moneycalcandroid.storage.DataStorage;
//
//@Singleton
//public class HomeStatsPresenter implements HomeStatsContract.Presenter {
//
//    private final DataStorage dataStorage;
//
//
//    @Nullable
//    private HomeStatsContract.View homeStatsView;
//
//    @Inject
//    HomeStatsPresenter(DataStorage dataStorage) {
//        this.dataStorage = dataStorage;
//    }
//
//
//    @Override
//    public void takeView(HomeStatsContract.View view) {
//        homeStatsView = view;
//    }
//
//    @Override
//    public void dropView() {
//        homeStatsView = null;
//    }
//
//    public void updateStatisticsSection(Integer id) {
//        if (dataStorage.getFinanceSummary() != null) {
//            FinanceSummaryBySection financeSummary = filterFinanceSummaryListById(dataStorage.getFinanceSummary(), id);
//            homeStatsView.setTodayBalance(financeSummary.getTodayBalance());
//        } else {
//            homeStatsView.showErrorMessage("Finance Summary not ready yet");
//        }
//    }
//
//}
