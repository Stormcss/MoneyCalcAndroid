package ru.strcss.projects.moneycalc.moneycalcandroid.componets.handlers;

import javax.inject.Inject;
import javax.inject.Singleton;

import ru.strcss.projects.moneycalc.moneycalcandroid.storage.EventBus;
import ru.strcss.projects.moneycalc.moneycalcandroid.utils.ActivityUtils;
import rx.functions.Action1;


@Singleton
public class ErrorHandler {
    private final EventBus eventBus;

    @Inject
    public ErrorHandler(EventBus eventBus) {
        this.eventBus = eventBus;

        eventBus.subscribeErrorEvent().subscribe(new Action1<String>() {
            @Override
            public void call(String errorMsg) {
                ActivityUtils.Companion.snackBarAction(errorMsg);
            }
        });
    }


}
