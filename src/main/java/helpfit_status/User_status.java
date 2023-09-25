package helpfit_status;

public enum User_status {

    DOING_SOMETHING("Что-то делает", 1),
    READY("Готов принять заказ", 2),
    WORK("Работает", 3),
    WAITING_PRFIT("Ожидание оплаты", 4),
    BLOCKED("Заблокирован", 5);

    String progress;
    int level;
    User_status(String progress, int level) {
        this.progress = progress;
        this.level = level;
    }
    User_status() { }

    public String get_progress() { return User_status.this.progress; }

    @Override public String toString() { return User_status.this.name(); }

}