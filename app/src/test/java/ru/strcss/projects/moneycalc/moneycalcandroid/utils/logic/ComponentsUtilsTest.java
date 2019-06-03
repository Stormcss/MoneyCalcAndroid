package ru.strcss.projects.moneycalc.moneycalcandroid.utils.logic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import ru.strcss.projects.moneycalc.moneycalcdto.entities.SpendingSection;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by Stormcss
 * Date: 24.01.2019
 */
class ComponentsUtilsTest {

    private List<SpendingSection> sectionArrayList;

    @BeforeEach
    void prepareTestData() {
        sectionArrayList = new ArrayList<>();
        sectionArrayList.add(new SpendingSection(1L, 1L, 2, 10, "", true,
                false, 100L));
        sectionArrayList.add(new SpendingSection(2L, 1L, 4, 20, "", true,
                false, 100L));
        sectionArrayList.add(new SpendingSection(3L, 1L, 6, 30, "", true,
                false, 100L));

    }

    @Test
    void shouldGetSpendingSectionByInnerId() {
        assertEquals(1L, (long) ComponentsUtils.Companion.getSpendingSectionByInnerId(sectionArrayList, 2).getId(), "Not equal!");
        assertEquals(2L, (long) ComponentsUtils.Companion.getSpendingSectionByInnerId(sectionArrayList, 4).getId(), "Not equal!");
        assertEquals(3L, (long) ComponentsUtils.Companion.getSpendingSectionByInnerId(sectionArrayList, 6).getId(), "Not equal!");
    }

    @Test
    void shouldGetPositionBySpendingSectionInnerId() {
        assertEquals(0L, ComponentsUtils.Companion.getPositionBySpendingSectionInnerId(sectionArrayList, 2), "Not equal!");
        assertEquals(1L, ComponentsUtils.Companion.getPositionBySpendingSectionInnerId(sectionArrayList, 4), "Not equal!");
        assertEquals(2L, ComponentsUtils.Companion.getPositionBySpendingSectionInnerId(sectionArrayList, 6), "Not equal!");
    }

    @Test
    void shouldGetSpendingSectionInnerIdByPosition() {
        assertEquals(2L, (int) ComponentsUtils.Companion.getSpendingSectionInnerIdByPosition(sectionArrayList, 0), "Not equal!");
        assertEquals(4L, (int) ComponentsUtils.Companion.getSpendingSectionInnerIdByPosition(sectionArrayList, 1), "Not equal!");
        assertEquals(6L, (int) ComponentsUtils.Companion.getSpendingSectionInnerIdByPosition(sectionArrayList, 2), "Not equal!");
    }

    @Test
    void shouldGetLogoIdBySectionId() {
        assertEquals(Integer.valueOf(10), ComponentsUtils.Companion.getLogoIdBySectionId(sectionArrayList, 2), "Not equal!");
        assertEquals(Integer.valueOf(20), ComponentsUtils.Companion.getLogoIdBySectionId(sectionArrayList, 4), "Not equal!");
        assertEquals(Integer.valueOf(30), ComponentsUtils.Companion.getLogoIdBySectionId(sectionArrayList, 6), "Not equal!");
    }
}