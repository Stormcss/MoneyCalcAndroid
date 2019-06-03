package ru.strcss.projects.moneycalc.moneycalcandroid.storage

import ru.strcss.projects.moneycalc.moneycalcandroid.utils.events.CrudEvent
import ru.strcss.projects.moneycalc.moneycalcandroid.utils.events.Event
import rx.Observable
import rx.subjects.PublishSubject

class EventBus {
    private val transactionSubject = PublishSubject.create<CrudEvent>()
    private val spendingSectionSubject = PublishSubject.create<CrudEvent>()
    private val eventSubject = PublishSubject.create<Event>()

    private val errorsSubject = PublishSubject.create<String>()

    /**
     * Transactions
     */
    fun addTransactionEvent(transactionEvent: CrudEvent) {
        transactionSubject.onNext(transactionEvent)
    }

    fun subscribeTransactionEvent(): Observable<CrudEvent> {
        return transactionSubject
    }

    /**
     * Spending Sections
     */
    fun addSpendingSectionEvent(sectionEvent: CrudEvent) {
        spendingSectionSubject.onNext(sectionEvent)
    }

    fun receivedSpendingSectionEvent(sectionEvent: CrudEvent) {
        spendingSectionSubject.onNext(sectionEvent)
    }

    fun subscribeSpendingSectionEvent(): Observable<CrudEvent> {
        return spendingSectionSubject
    }

    /**
     * General events
     */
    fun addEvent(event: Event) {
        eventSubject.onNext(event)
    }

    fun subscribeEvent(): Observable<Event> {
        return eventSubject
    }

    /**
     * Errors
     */
    fun addErrorEvent(errorEvent: String) {
        errorsSubject.onNext(errorEvent)
    }

    fun subscribeErrorEvent(): Observable<String> {
        return errorsSubject
    }
}
