package helpfit;
import static helpfit.Panel.*;
import helpfit_status.Bot_status;
import helpfit_status.Order_status;
import helpfit_status.User_status;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ActionType;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.pinnedmessages.PinChatMessage;
import org.telegram.telegrambots.meta.api.methods.pinnedmessages.UnpinChatMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendVenue;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class Bot extends TelegramLongPollingBot {

    public Bot() { super(panel.token); }
    @Override public String getBotUsername() { return panel.username; }
    @Override public void onUpdateReceived(Update update) {
        try {

            new Users(update);
            if(users.is_blocked) {

                print.result("[BLOCKED=" + users.cmd + "]");
                print.key_print = false;
                String text = "🔴Извините, вы заблокированы, в связи с нарушением правил. \nВы можете обратьтся в [службу поддержки](" + panel.support_contact + ").";

                users.set_msg_id(users.o.getMessageId() + 1);
                send_msg(text, null, false, Switch_sending.INLINE);

                del_all_msg_to_chat(users.msg_id - 2);
            }

            // 📥Received callback
            else if(update.hasCallbackQuery()) {

                String buffer_callback = "";
                boolean key1 = false;
                for(int i = 0; i < users.c_cmd.length; i++) {
                    buffer_callback += users.c_cmd[i];
                    if(buffer_callback.equals("show_list_textile_company_")) { key1 = true; break; }
                }

                print.way("[has_callback=" + users.data + "]");
                if(users.data.equals("menu_edit")) {

                    if(users.bot_status.equals(Bot_status.ORDER_CATCH_ADDRESS.toString())) {
                        sql.sql_update("DELETE FROM orders WHERE id_user = " + users.id_user);
                    }
                    else if(users.bot_status.equals(Bot_status.ORDER_CATCH_DATE_TIME.toString())) {
                        sql.sql_update("DELETE FROM orders WHERE id_user = " + users.id_user);
                    }
                    else if(users.bot_status.equals(Bot_status.ORDER_CATCH_TASK.toString())) {
                        sql.sql_update("DELETE FROM orders WHERE id_user = " + users.id_user);
                    }
                    get_menu(Switch_sending.EDIT_M);
                    del_all_msg_to_chat(users.msg_id - 2);
                }
                else if(key1) { }
                else if(users.data.equals(panel.keyword)) { }
                else if(users.data.equals("menu_send")) {   get_menu(Switch_sending.SEND_M); }
                else if(users.data.equals("edit_name")) {

                    users.set_bot_status(Bot_status.EDIT_NAME);
                    String text = "✏️<b>Имя</b>\n\nВпишите другое имя";
                    edit_msg(text, keyboard.get_info_user());
                    print.result("[EDIT_NAME]");
                }
                else if(users.data.equals("edit_contact")) {

                    users.set_bot_status(Bot_status.EDIT_CONTACT);
                    String text = "✏️<b>Контакт</b>\n\nПоделитесь номером телефона или напишите\nВ виде: <code>+79990000000</code>";
                    send_msg(text, keyboard.send_contact(), false, Switch_sending.REPLY);
                    print.result("[EDIT_CONTACT]");
                }
                else if(users.data.equals("edit_card")) {

                    users.set_bot_status(Bot_status.EDIT_CARD);
                    String text = "✏️<b>Номер карты</b>\n\nВпишите номер карты получателя\nВ виде: <code>#### #### #### ####</code>";
                    edit_msg(text, keyboard.get_info_user());
                    print.result("[EDIT_CARD]");
                }
                else if(users.data.equals("settings")) {    get_settings(Switch_sending.EDIT_M); }
                else if(users.data.equals("info")) {

                    String info = ""; {
                        info =
                        "🦺 Я бот для рассылок заказов на работу\n" +
                        "📈 Я буду оценивать рейтинг, чтобы Ваши доходы расти\n" +
                        "💰 Получайте зарплату сразу после выполнения заказа\n\n" +
            
                        "📜 Правила работы:" + "\n" +
                        "1. Необходимо добросовестно выполнять работу на заказе.\n" +
                        "2. За пропуск грозит блокировка бот.";
                    }
                    edit_msg(info, keyboard.get_info_user());
                    print.result("[INFO]");
                }
                else if(users.data.equals("get_orders")) {  get_orders(Switch_order.START); }
                else if(users.data.equals("create_order")) { create_order(Bot_status.ORDER_CATCH_QUANTITY_WORKERS); }
                else if(users.data.equals("flipping_through_back")) { get_orders(Switch_order.FLIPPING_THROUGH_BACK); }
                else if(users.data.equals("flipping_through_next")) { get_orders(Switch_order.FLIPPING_THROUGH_NEXT); }
                else if(users.data.equals("ready_1")) {     get_orders(Switch_order.READY_1); }
                else if(users.data.equals("send_venue")) {

                    int id_order2 = search_number(users.bot_status);
                    ResultSet order2 = sql.sql_callback("SELECT * FROM orders WHERE id_order = " + id_order2);
                    order2.next();
                    send_venue("📍 " + order2.getString("address"), "  ", order2.getDouble("latitude"), order2.getDouble("longitude"), keyboard.hide_location());
                }
                else if(users.data.equals("hide_location")) { del_msg(users.o.getMessageId()); }
                else if(users.data.equals("share_order")) { answer_callback("Функция временно не работает"); }
                else if(users.data.equals("auto_delete")) { answer_callback("Функция временно не работает"); }
                else if(users.data.equals("help")) {        answer_callback("Функция временно не работает"); }
                else if(users.data.equals("pin_messages")) {answer_callback("Функция временно не работает"); }
                else if(users.data.equals("priority")) {    answer_callback("Функция временно не работает"); }
                else if(users.data.equals("priority_part_time_job")) { answer_callback("Функция временно не работает"); }
                else if(users.data.equals("priority_help")) {answer_callback("Функция временно не работает"); }
                else { anythink_msg(); }
            }

            // ✉️Received message
            else if(update.getMessage().hasText()) {

                print.way("[has_text=" + users.cmd + "]");
                if(users.cmd.equals("/start")) { get_menu(Switch_sending.SEND_M); }
                else if(users.bot_status.equals(Bot_status.EDIT_NAME.toString())) {

                    users.set_name(users.cmd);
                    get_settings(Switch_sending.EDIT_M);
                    del_msg(users.o.getMessageId());
                }
                else if(users.bot_status.equals(Bot_status.EDIT_CONTACT.toString())) {

                    users.set_contact(users.cmd);
                    del_msg(users.o.getMessageId());
                    del_msg(users.o.getMessageId() - 1);
                }
                else if(users.bot_status.equals(Bot_status.EDIT_CARD.toString())) {

                    users.set_card(users.cmd);
                    get_settings(Switch_sending.EDIT_M);
                    del_msg(users.o.getMessageId());
                }
                else if(users.bot_status.equals(Bot_status.ORDER_CATCH_QUANTITY_WORKERS.toString())) { create_order(Bot_status.ORDER_CATCH_ADDRESS); }
                else if(users.bot_status.equals(Bot_status.ORDER_CATCH_ADDRESS.toString())) { create_order(Bot_status.ORDER_CATCH_DATE_TIME); }
                else if(users.bot_status.equals(Bot_status.ORDER_CATCH_DATE_TIME.toString())) { create_order(Bot_status.ORDER_CATCH_TASK); }
                else if(users.bot_status.equals(Bot_status.ORDER_CATCH_TASK.toString())) { create_order(Bot_status.ORDER_CATCH_VALIDATION); }
                else if(users.cmd.equals(panel.keyword)) { }
                else { anythink_msg(); }
            }

            // 📞Received contact
            else if(users.o.hasContact()) {

                users.set_contact(users.o.getContact().getPhoneNumber());
                del_msg(users.o.getMessageId());
                del_msg(users.o.getMessageId() - 1);
                print.result("[GET_CONTACT]");
            }

            // Not received
            else { anythink_msg(); }
        }
        catch(TelegramApiException e) { print.error("\n[BOT] - " + e + "\n"); }
        catch(Exception e) { print.error("\n[BOT] - " + e + "\n"); }
    }

    // Short methods:
    public void anythink_msg() throws TelegramApiException {
        send_msg("Я не понимааю тебя😁", null, true, Switch_sending.SEND_M);

        try { Thread.sleep(1200); }
        catch(InterruptedException e) { print.error("anythink msg thread"); }

        del_msg(users.o.getMessageId());
        del_msg(users.o.getMessageId() + 1);
        print.result("[Anythink_msg]");
    }
    public void del_all_msg_to_chat(int msg_id) {

        int j = 0;
        for(int i = msg_id; i <= msg_id; i--) {
            try {
                if(j != panel.delete_last_quantity) { del_msg(i); }
                else { break; }
            }
            catch(TelegramApiException e) { j++; }
        }
        print.result("[Del_all_msg_to_chat]");
    }
    public void get_menu(Switch_sending switch_sending) throws TelegramApiException {

        String text = ""; {
               text =
            "🦺 Я бот для рассылок заказов на работу\n" +
            "📈 Оцнка рейтинга, чтобы доходы расти\n" +
            "💰 Получай зарплату сразу после выполнения заказа\n\n" +

            "📜 Правила работы:" + "\n" +
            "1. Необходимо добросовестно выполнять работу на заказе\n" +
            "2. За пропуск грозит блокировка бот\n\n" +

            "Ваши данные:\n" +
            "🔷 Имя: <code>" + users.name + "</code>\n" +
            "📞 Телефон: <code>" + users.contact + "</code>\n" +
            "💳 Номер карты: <code>" + users.card + "</code>\n" +
            "🟢 Выполненых заказов: <code>" + users.quantity + "</code>\n\n" +

            "📈 Рейтинг: <code>" + users.rating + "</code>";
        }

        users.set_bot_status(Bot_status.MENU);
        users.set_user_status(User_status.DOING_SOMETHING);

        if(switch_sending.equals(Switch_sending.EDIT_M)) {

            print.result("[menu_edit]");
            edit_msg(text, keyboard.get_menu());
        }
        else {

            print.result("[menu_send]");
            users.set_msg_id(users.o.getMessageId() + 1);
            send_msg(text, keyboard.get_menu(), false, Switch_sending.INLINE);
        }
        del_all_msg_to_chat(users.msg_id - 2);
    }
    public void get_settings(Switch_sending switch_sending) throws TelegramApiException {

        users.set_bot_status(Bot_status.SETTINGS);
        String text =
                    "⚙️<b>Настройки</b>\n\n" +
                    "📌 Вы пригласили 0 челове\n" +
                    "🔗 Cсылка для приглашения друзей: https://t.me/" + panel.username + "?start=" + users.chat_id;
        if(switch_sending.equals(Switch_sending.EDIT_M)) { edit_msg(text, keyboard.get_settings()); }
        else { send_msg(text, keyboard.get_settings(), false, Switch_sending.INLINE); }
        print.result("[settings]");
    }
    public void mailings_orders_everyone(String text, int id_order) throws TelegramApiException {

        ResultSet has_last_check_order_user = sql.sql_callback("SELECT chat_id, last_check_order FROM users;");
        try {
            while(has_last_check_order_user.next()) {
                if(has_last_check_order_user.getInt("last_check_order") < id_order) {
                    
                    execute(new SendMessage(has_last_check_order_user.getString("chat_id"), text));
                    new Users().set_last_check_order(id_order, has_last_check_order_user.getString("chat_id"));
                }
            }
        }
        catch(SQLException eSQL) { print.error("[Bot] - SQL"); }
    }
    public void create_order(Bot_status bot_status) throws TelegramApiException, InterruptedException {

        if(users.contact.equals("отсутствует")) {

            answer_callback("Вы не зарегистрировали свой контакт.");
            print.result("[create_order=0]");
        }
        else {

            users.set_bot_status(bot_status);
            order.get_list_order_all();
            String text = "📥<b>Создание заявки</b>\n\nЧтобы создать заявку, заполните следующие поля,\nзатем с Вами свяжется модератор, чтобы окончательно утвердить заявку.\n";
            if(!users.bot_status.equals(Bot_status.ORDER_CATCH_QUANTITY_WORKERS.toString())) { del_msg(users.o.getMessageId()); }
            if(users.bot_status.equals(Bot_status.ORDER_CATCH_QUANTITY_WORKERS.toString())) {

                text += "♻️Укажите требуемое количество рабочих\n";
                text += "☑️Адрес\n";
                text += "☑️Дата и время\n";
                text += "☑️Задача";
                edit_msg(text, keyboard.create_order());
            }
            if(users.bot_status.equals(Bot_status.ORDER_CATCH_ADDRESS.toString())) {

                sql.sql_update("INSERT INTO orders(id_user, order_status, quantity_workers) VALUES(" +
                    users.id_user + ", '" +
                    Order_status.CREATING.toString() + "', " +
                    Integer.valueOf(users.cmd) + ");"
                );
                order.set_quantity_workers(Integer.valueOf(users.cmd));
                text += "✅Количество рабочих: <code>" + order.quantity_workers + "</code>\n";
                text += "♻️Укажите адрес(г. Ярославль, ул. Свободы 1)\n";
                text += "☑️Дата и время\n";
                text += "☑️Задача";
                edit_msg(text, keyboard.create_order());
            }
            if(users.bot_status.equals(Bot_status.ORDER_CATCH_DATE_TIME.toString())) {

                order.set_address(users.cmd);
                text += "✅Колличество рабочих: <code>" + order.quantity_workers + "</code>\n";
                text += "✅Адрес: <code>" + order.address + "</code>\n";
                text += "♻️Укажите желаемую дату и время(01.01.24; 09.00-18.00)\n";
                text += "☑️Задача";
                edit_msg(text, keyboard.create_order());
            }
            if(users.bot_status.equals(Bot_status.ORDER_CATCH_TASK.toString())) {

                order.set_date_time(users.cmd);
                text += "✅Колличество рабочих: <code>" + order.quantity_workers + "</code>\n";
                text += "✅Адрес: <code>" + order.address + "</code>\n";
                text += "✅Дата и время: <code>" + order.date_time + "</code>\n";
                text += "♻️Кратко опишите задачу";
                edit_msg(text, keyboard.create_order());
            }
            if(users.bot_status.equals(Bot_status.ORDER_CATCH_VALIDATION.toString())) {

                order.set_task(users.cmd);
                text = "✅ <b>Заявка создана!</b>\n\n";
                text += "Пожалуйста, дождитесь, звонка от модератора.";
                edit_msg(text, null);
                order.set_order_status(Order_status.MODERATION);
            }

            print.result("[create_order=1]");
        }
    }
    public void get_orders(Switch_order switch_order) throws TelegramApiException {

        String text = "Извините, заказов пока нет. Вам прийдет уведомление если оно появится.";
        int i = 0;
        boolean key = true;
        Switch_order switch_order_for_keyboard = Switch_order.NONE;
        try {

            // Получение списков заказов
            String sql_query = "SELECT *, (SELECT COUNT(*) FROM orders WHERE order_status = ?) AS count " +
                               "FROM orders WHERE order_status = ?;";
            ResultSet list_orders = null;
            try {
                PreparedStatement param_stat = sql.get_conn().prepareStatement(sql_query, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                param_stat.setString(1, "CREATED");
                param_stat.setString(2, "CREATED");
                list_orders = param_stat.executeQuery();
                if(list_orders.equals(null)) { key = false; }
            }
            catch(SQLException e) { print.error("[sql_prepared_statement] - " + e); key = false; }


            while(key && list_orders.next()) {

                i++;
                order.get_list_orders_data(list_orders);
                if(users.bot_status.equals(Bot_status.MENU.toString())) { users.set_bot_status("ORDER_" + order.id_order); }

                // Для начала нахожу тот самый заказ из всех или самый первый
                while(key && search_number(users.bot_status) == order.id_order) {
                    
                    if(list_orders.isFirst()) { switch_order_for_keyboard = Switch_order.FIRST; }
                    if(list_orders.isLast()) { switch_order_for_keyboard = Switch_order.LAST; }
                    if(!list_orders.isLast() && !list_orders.isFirst()) { switch_order_for_keyboard = Switch_order.MEDIUM; }
                    if(order.count == 1) { switch_order_for_keyboard = Switch_order.DISPOSABLE; }

                    if(switch_order.equals(Switch_order.START)) {
                        print.debag("[START]");
                        key = false;
                    }
                    if(switch_order.equals(Switch_order.FLIPPING_THROUGH_NEXT)) {
                        print.debag("[FLIPPING_THROUGH_NEXT]");
                        key = false;
                        list_orders.next();
                        i++;
                    }
                    if(switch_order.equals(Switch_order.FLIPPING_THROUGH_BACK)) {
                        print.debag("[FLIPPING_THROUGH_BACK]");
                        key = false;
                        list_orders.previous();
                        i--;
                    }
                    if(switch_order.equals(Switch_order.READY_1)) {
                        print.debag("[READY_1]");
                        text = "Хорошо, завтра выходишь на работу по заказу " + order.id_order;
                        String sql_query2 = "";

                        // INSERT INTO users(tag, chat_id, msg_id, name, contact) VALUES('"
                               sql_query2 += "INSERT INTO activity_user_on_order(id_order, id_user) VALUES(";
                               sql_query2 += "";
                        sql.sql_update(sql_query2);
                    }
                }

            }

            if(!key) {
                order.get_list_orders_data(list_orders);
                users.set_bot_status("ORDER_" + order.id_order);
                text =
                    "<b>Активный заказ:</b> <code>" + i + "/" + order.count + "</code>\n" +
                    "⏰ <code>" + order.date_time +
                    "</code>\n📍 Адрес: <code>" + order.address +
                    "</code>\n🦺 Требуется рабочих: <code>" + order.quantity_workers +
                    "</code>\n💰 Заработок: <code>" + order.profit + "р" +
                    "</code>\n\n❓ Задача: <code>" + order.task + "</code>";
            }

            print.debag(switch_order_for_keyboard.toString());
            edit_msg(text, keyboard.get_console_for_mailing(switch_order_for_keyboard));
        }
        catch(Exception e) {

            // Возникла ошибка по одной из причин:
            // 1. Кто-то быстрее усплел забрать заказ и теперь нельзя получить тот заказ
            // 2. Изначалильно при попытке получить хоть какой-то заказ, заказов и не было
            // 3. При попытке посмотреть следующий или предыдущий заказ, обвенчается неудачей, так как кто-то успел забрать заказ первее

            // Все эти ошибки будут обрабатыватся здесь
            print.debag("[" + e);

            send_msg(text, null, false, Switch_sending.SEND_M);
        }
        
        print.result("[GET_ORDER]");
    }
    private static int search_number(String text) {

        try {
            String buffer = "";
            char[] c_text = text.toCharArray();
            try {
                for(int i = c_text.length - 1; 0 < i; --i) {
                    if(!String.valueOf(c_text[i]).equals("_")) { buffer += String.valueOf(c_text[i]); } else { break; }
                }
            }
            catch(Exception e) { buffer = "0"; }
            c_text = buffer.toCharArray(); buffer = "";
            for(int i = c_text.length - 1; 0 <= i; --i) { buffer += c_text[i]; }
            return Integer.valueOf(buffer);
        }
        catch(Exception e) { print.error("[search_number]"); return 0; }
    }

    // Action with chat:
    public void TYPING() throws TelegramApiException {
        execute(SendChatAction.builder().action(ActionType.TYPING.name()).chatId(users.chat_id).build());
    }
    public void edit_msg(String text, InlineKeyboardMarkup inline_keyboard) throws TelegramApiException {

        EditMessageText edit_text = new EditMessageText();
        edit_text.setChatId(users.chat_id);
        edit_text.setText(text);
        edit_text.setParseMode(ParseMode.HTML);
        edit_text.enableHtml(true);
        edit_text.setMessageId(users.msg_id);
        if(inline_keyboard != null) { edit_text.setReplyMarkup(inline_keyboard); }
        execute(edit_text);
    }
    public void send_msg(String text, Object keyboard, boolean replyToMessageId, Switch_sending switch_sending) throws TelegramApiException {

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(users.chat_id);
        sendMessage.setText(text);
        sendMessage.setParseMode(ParseMode.HTML);
        sendMessage.enableHtml(true);
        if(replyToMessageId) { sendMessage.setReplyToMessageId(users.o.getMessageId()); }
        if(keyboard != null) {
            if(switch_sending.equals(Switch_sending.REPLY)) { sendMessage.setReplyMarkup((ReplyKeyboardMarkup) keyboard); }
            else { sendMessage.setReplyMarkup((InlineKeyboardMarkup) keyboard); }
        }
        // else { sendMessage.setAllowSendingWithoutReply(true); }
        execute(sendMessage);
    }
    public void del_msg(int msg_id) throws TelegramApiException {
        execute(new DeleteMessage(users.chat_id, msg_id));
    }
    public void send_venue(String address, String text, double latitude, double longitude, InlineKeyboardMarkup inlineKeyboardMarkup) throws TelegramApiException {

        SendVenue sendVenue = new SendVenue();
        sendVenue.setTitle(address);
        sendVenue.setAddress(text);
        sendVenue.setLatitude(latitude);
        sendVenue.setLongitude(longitude);
        sendVenue.setChatId(users.chat_id);
        sendVenue.setAllowSendingWithoutReply(true);
        sendVenue.enableNotification();
        // sendVenue.setParseMode(ParseMode.MARKDOWN);
        // sendVenue.setMessageId(users.msg_id);
        if(inlineKeyboardMarkup != null) { sendVenue.setReplyMarkup(inlineKeyboardMarkup); }
        execute(sendVenue);
    }
    public void pin_msg(int msg_id) throws TelegramApiException {
        execute(PinChatMessage.builder().messageId(msg_id).chatId(users.chat_id).build());
    }
    public void unpin_msg(int msg_id) throws TelegramApiException {
        execute(UnpinChatMessage.builder().messageId(msg_id).chatId(users.chat_id).build());
    }
    public void answer_callback(String text) throws TelegramApiException {
        execute(AnswerCallbackQuery.builder().text(text).callbackQueryId(users.callback.getId()).build());
        print.result("[answer_callback]");
    }

    private enum Switch_sending { SEND_M, EDIT_M, REPLY, INLINE; Switch_sending() { } }
    public enum Switch_order {
        START,
        FLIPPING_THROUGH_BACK,
        READY_1,
        FLIPPING_THROUGH_NEXT,

        NONE,
        FIRST,
        MEDIUM,
        LAST,
        DISPOSABLE;

        Switch_order() { }
        @Override public String toString() { return Switch_order.this.name(); }
    }
}