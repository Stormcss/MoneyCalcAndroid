package ru.strcss.projects.moneycalc.moneycalcandroid.utils.logic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.HttpException;
import ru.strcss.projects.moneycalc.enitities.FinanceSummaryBySection;
import ru.strcss.projects.moneycalc.enitities.SpendingSection;
import ru.strcss.projects.moneycalc.moneycalcandroid.BaseView;

public class ComponentsUtils {
    private final static String messageRegex = "\"message\":\"(.*?)\"";
    private final static Pattern messageGetterPattern = Pattern.compile(messageRegex);

    public static List<Integer> getSpendingSectionIds(List<SpendingSection> sections) {
        List<Integer> ids = new ArrayList<>();
        for (SpendingSection section : sections)
            ids.add(section.getId());
        return ids;
    }

    public static SpendingSection getSpendingSectionById(List<SpendingSection> spendingSections, int id) {
        for (SpendingSection spendingSection : spendingSections) {
            if (spendingSection.getId() == id)
                return spendingSection;
        }
        return null;
    }

    public static FinanceSummaryBySection getFinanceSummaryBySectionById(List<FinanceSummaryBySection> financeList, int id) {
        for (FinanceSummaryBySection financeSummaryBySection : financeList) {
            if (financeSummaryBySection.getSectionId() == id)
                return financeSummaryBySection;
        }
        return null;
    }

    public static String getErrorBodyMessage(Throwable t) {
        if (t instanceof HttpException)
            return getErrorBodyMessage((HttpException) t);
        else
            return t.getMessage();
    }

    public static String getErrorBodyMessage(HttpException ex) {
        try {
            String errorJSON = ex.response().errorBody().string();
            System.err.println("errorJSON = " + errorJSON);
            final Matcher messageMatcher = messageGetterPattern.matcher(errorJSON);

            if (messageMatcher.find()) {
                return messageMatcher.group(1);
            } else {
                return errorJSON;
            }
        } catch (IOException e1) {
            e1.printStackTrace();
            return ex.getMessage();
        }
    }

    @Deprecated
    public static void showErrorMessageFromException(Throwable ex, BaseView view) {
        if (ex instanceof HttpException)
            view.showErrorMessage(getErrorBodyMessage((HttpException) ex));
        else
            view.showErrorMessage(ex.getMessage());
    }
}
