package helpfit_status;

public enum Order_status {

    CREATING("Создается", 1),
    CREATED("Заявка создана", 2),
    MODERATION("На модерации", 3),
    COLLECTED_GROUP("Состав собран", 4),
    IN_PROCESS("В процессе выполнения", 5),
    DONE("Выполнено", 6);

    String progress;
    int level;
    Order_status(String progress, int level) {
        this.progress = progress;
        this.level = level;
    }

    public String get_progress() { return Order_status.this.progress; }

    @Override public String toString() { return Order_status.this.name(); }

}