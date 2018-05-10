package ru.strcss.projects.moneycalc.moneycalcandroid.utils.logic;

import java.util.ArrayList;
import java.util.List;

import ru.strcss.projects.moneycalc.enitities.SpendingSection;

public class ComponetsUtils {
    public static List<Integer> getSpendingSectionIds(List<SpendingSection> sections) {
        List<Integer> ids = new ArrayList<>();
        for (SpendingSection section : sections) {
            ids.add(section.getId());
        }
        return ids;
    }

    public static SpendingSection findSpendingSectionById(List<SpendingSection> spendingSections, int id) {
        for (SpendingSection spendingSection : spendingSections) {
            if (spendingSection.getId() == id)
                return spendingSection;
        }
        return null;
    }

}
