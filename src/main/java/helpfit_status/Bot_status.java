package helpfit_status;

/** Before status MENU, go to back iposible, but after MENU, allow status can be any. */
public enum Bot_status {

    MENU("В меню", 1),
    EDIT_NAME("Измеяет имя", 2),
    EDIT_CONTACT("Измеяет контакт", 3),
    EDIT_CARD("Измеяет карту", 4),
    SETTINGS("В настройках", 5),

    SEARCH_JOB("Ищет работу", 6),
    READY("Готов принять заказ", 7),
    WORK("Работает", 8),
    WAITING_PRFIT("Ожидание оплаты", 9),

    BLOCKED("Заблокирован", 10),

    ORDER_CATCH_QUANTITY_WORKERS("Заявка: ожидается кол. рабочих", 11),
    ORDER_CATCH_ADDRESS("Заявка: ожидается адрес", 12),
    ORDER_CATCH_DATE_TIME("Заявка: ожидается дата и время", 13),
    ORDER_CATCH_TASK("Заявка: ожидается задача", 14),
    ORDER_CATCH_VALIDATION("Заявка: ожидается одобрения", 15),
    ORDER_CATCH_PAYMENT("Заявка: ожидается оплата", 16);

    String progress;
    int level;
    Bot_status(String progress, int level) {
        this.progress = progress;
        this.level = level;
    }

    public String get_progress() { return Bot_status.this.progress; }

    @Override public String toString() { return Bot_status.this.name(); }

}