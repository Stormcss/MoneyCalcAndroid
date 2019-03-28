package ru.strcss.projects.moneycalc.moneycalcandroid.storage;

import android.util.SparseIntArray;

import moneycalcandroid.moneycalc.projects.strcss.ru.moneycalc.R;

public class DrawableStorage {

    static private SparseIntArray spendingSectionLogoStorage = new SparseIntArray(25);

    public static SparseIntArray getSpendingSectionLogoStorage() {
        return spendingSectionLogoStorage;
    }

    static {
        spendingSectionLogoStorage.put(0, R.drawable.ic_empty_drawable);
        spendingSectionLogoStorage.put(1, R.drawable.ic_restaurant_black_24dp);
        spendingSectionLogoStorage.put(2, R.drawable.ic_menu_manage);
        spendingSectionLogoStorage.put(3, R.drawable.ic_show_chart_black_24dp);
        spendingSectionLogoStorage.put(4, R.drawable.ic_sentiment_satisfied_black_24dp);
        spendingSectionLogoStorage.put(5, R.drawable.ic_directions_car_black_24dp);
        spendingSectionLogoStorage.put(6, R.drawable.ic_shopping_cart_black_24dp);
        spendingSectionLogoStorage.put(7, R.drawable.ic_local_hospital_black_24dp);
        spendingSectionLogoStorage.put(8, R.drawable.ic_favorite_black_24dp);
        spendingSectionLogoStorage.put(9, R.drawable.ic_pets_black_24dp);
        spendingSectionLogoStorage.put(10, R.drawable.ic_directions_bus_black_24dp);
        spendingSectionLogoStorage.put(11, R.drawable.ic_flight_black_24dp);
        spendingSectionLogoStorage.put(12, R.drawable.ic_local_gas_station_black_24dp);
        spendingSectionLogoStorage.put(13, R.drawable.ic_smoking_rooms_black_24dp);
        spendingSectionLogoStorage.put(14, R.drawable.ic_local_movies_black_24dp);
        spendingSectionLogoStorage.put(15, R.drawable.ic_delete_black_24dp);
        spendingSectionLogoStorage.put(16, R.drawable.ic_faucet_side_view_black);
        spendingSectionLogoStorage.put(17, R.drawable.ic_local_bar_black_24dp);
        spendingSectionLogoStorage.put(18, R.drawable.ic_fitness_center_black_24dp);
        spendingSectionLogoStorage.put(19, R.drawable.ic_home_black_24dp);
        spendingSectionLogoStorage.put(20, R.drawable.ic_local_parking_black_24dp);
    }
}
