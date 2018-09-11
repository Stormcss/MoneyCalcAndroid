package ru.strcss.projects.moneycalc.moneycalcandroid.componets.handlers;

import javax.inject.Inject;
import javax.inject.Singleton;

import ru.strcss.projects.moneycalc.moneycalcandroid.storage.EventBus;
import rx.functions.Action1;

import static ru.strcss.projects.moneycalc.moneycalcandroid.utils.ActivityUtils.snackBarAction;

@Singleton
public class ErrorHandler {
    private final EventBus eventBus;

    @Inject
    public ErrorHandler(EventBus eventBus) {
        this.eventBus = eventBus;

        eventBus.subscribeErrorEvent().subscribe(new Action1<String>() {
            @Override
            public void call(String errorMsg) {
                snackBarAction(errorMsg);
            }
        });
    }


}
