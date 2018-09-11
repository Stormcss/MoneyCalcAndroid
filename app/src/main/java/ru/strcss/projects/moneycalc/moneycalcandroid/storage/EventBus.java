package ru.strcss.projects.moneycalc.moneycalcandroid.storage;

import ru.strcss.projects.moneycalc.moneycalcandroid.utils.events.CrudEvent;
import ru.strcss.projects.moneycalc.moneycalcandroid.utils.events.Event;
import rx.Observable;
import rx.subjects.PublishSubject;

public class EventBus {
    private PublishSubject<CrudEvent> transactionSubject = PublishSubject.create();
    private PublishSubject<CrudEvent> spendingSectionSubject = PublishSubject.create();
    private PublishSubject<Event> eventSubject = PublishSubject.create();

    private PublishSubject<String> errorsSubject = PublishSubject.create();

    /**
     * Transactions
     */
    public void addTransactionEvent(CrudEvent transactionEvent) {
        transactionSubject.onNext(transactionEvent);
    }

    public Observable<CrudEvent> subscribeTransactionEvent() {
        return transactionSubject;
    }

    /**
     * Spending Sections
     */
    public void addSpendingSectionEvent(CrudEvent sectionEvent) {
        spendingSectionSubject.onNext(sectionEvent);
    }

    public Observable<CrudEvent> subscribeSpendingSectionEvent() {
        return spendingSectionSubject;
    }

    /**
     * General events
     */
    public void addEvent(Event event) {
        eventSubject.onNext(event);
    }

    public Observable<Event> subscribeEvent() {
        return eventSubject;
    }

    /**
     * Errors
     */
    public void addErrorEvent(String errorEvent) {
        errorsSubject.onNext(errorEvent);
    }

    public Observable<String> subscribeErrorEvent() {
        return errorsSubject;
    }
}
