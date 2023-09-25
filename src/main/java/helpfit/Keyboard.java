package helpfit;
import static helpfit.Panel.*;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.LoginUrl;
import org.telegram.telegrambots.meta.api.objects.games.CallbackGame;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButtonPollType;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButtonRequestChat;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButtonRequestUser;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.api.objects.webapp.WebAppInfo;

import helpfit.Bot.Switch_order;

@Component
public class Keyboard {

    // ✅ 📌 1️⃣ 2️⃣ ⬅️ 👷🏻 🗑 ℹ️ ✏️ ⚙️ 📞 ✉️ ❓ ⏰ 🦺 💰 🔴 🟢 🔷 🔹 📥 😁 📜 📈 💳 🔗 🔥 ♻️ 📍
    public ReplyKeyboardMarkup  send_contact() {

        update_keybord_row_list();
        row_Keyboard.add(Switch_button.get_btn("📞Поделится контактом", Switch_button.CONTACT, null));

            keyboard.add(row_Keyboard);

        return get_reply_btn(keyboard, "Или напишите...");
    }
    public InlineKeyboardMarkup get_menu() {

        update_keybord_row_list();
        row1.add(get_inline_btn("👷🏻Заказы", "get_orders"));
        row1.add(get_inline_btn("⚙️Настройки", "settings"));
        row2.add(get_inline_btn("📥Создать заявку", "create_order"));
        row2.add(get_inline_btn("ℹ️Информация", "info"));

            rowList.add(row1);
            rowList.add(row2);

        return new InlineKeyboardMarkup(rowList);
    }
    public InlineKeyboardMarkup get_settings() {

        update_keybord_row_list();
        row1.add(get_inline_btn("✅ Авто удаление сообщений", "auto_delete"));
        row1.add(get_inline_btn("✅ Закреплять сообщения", "pin_messages"));
        row3.add(get_inline_btn("✏️Имя", "edit_name"));
        row3.add(get_inline_btn("✏️Контакт", "edit_contact"));
        row3.add(get_inline_btn("✏️Номер карты", "edit_card"));
        row4.add(get_inline_btn("Приоритет:", "priority"));
        row5.add(get_inline_btn("1️⃣ Подработка", "priority_part_time_job"));
        row5.add(get_inline_btn("2️⃣ Помощь", "priority_help"));
        row6.add(get_inline_btn("⚙️Меню", "menu_edit"));

            rowList.add(row1);
            rowList.add(row2);
            rowList.add(row3);
            rowList.add(row4);
            rowList.add(row5);
            rowList.add(row6);

        return new InlineKeyboardMarkup(rowList);
    }
    public InlineKeyboardMarkup get_console_for_mailing(Switch_order switch_order_for_keyboard) {

        update_keybord_row_list();
        if(switch_order_for_keyboard.equals(Switch_order.FIRST)) {
            row1.add(get_inline_btn("✅Откликнуться", "ready_1"));
            row1.add(get_inline_btn("▶️", "flipping_through_next"));
        }
        if(switch_order_for_keyboard.equals(Switch_order.MEDIUM)) {
            row1.add(get_inline_btn("◀️", "flipping_through_back"));
            row1.add(get_inline_btn("✅Откликнуться", "ready_1"));
            row1.add(get_inline_btn("▶️", "flipping_through_next"));
        }
        if(switch_order_for_keyboard.equals(Switch_order.DISPOSABLE)) {
            row1.add(get_inline_btn("✅Откликнуться", "ready_1"));
        }
        if(switch_order_for_keyboard.equals(Switch_order.LAST)) {
            row1.add(get_inline_btn("◀️", "flipping_through_back"));
            row1.add(get_inline_btn("✅Откликнуться", "ready_1"));
        }

        row2.add(get_inline_btn("⚙️Меню", "menu_send"));
        row2.add(get_inline_btn("🔗Поделится", "share_order"));
        row2.add(get_inline_btn("📍Карта", "send_venue"));

            rowList.add(row1);
            rowList.add(row2);

        return new InlineKeyboardMarkup(rowList);
    }
    public InlineKeyboardMarkup get_info_user() {

        update_keybord_row_list();
        row1.add(get_inline_btn("⚙️Настройки", "menu_edit"));

            rowList.add(row1);

        return new InlineKeyboardMarkup(rowList);
    }
    public InlineKeyboardMarkup create_order() {

        update_keybord_row_list();
        row1.add(get_inline_btn("⚙️Меню", "menu_edit"));

            rowList.add(row1);

        return new InlineKeyboardMarkup(rowList);
    }
    public InlineKeyboardMarkup hide_location() {

        update_keybord_row_list();
        row1.add(get_inline_btn("❌Скрыть", "hide_location"));

            rowList.add(row1);
        return new InlineKeyboardMarkup(rowList);
    }

    // Constructors:
    private InlineKeyboardButton get_inline_btn(String text, String callback) {

        String url = null;
        CallbackGame callback_game = null;
        String switchInlineQuery = null;
        String switchInlineQueryCurrentChat = null;
        boolean pay = false;
        LoginUrl loginUrl = null;
        WebAppInfo webApp = null;

        return new InlineKeyboardButton(text, url, callback, callback_game, switchInlineQuery, switchInlineQueryCurrentChat, pay, loginUrl, webApp);
    }
    private ReplyKeyboardMarkup get_reply_btn(List<KeyboardRow> keyboard, String place_holder) {

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        replyKeyboardMarkup.setKeyboard(keyboard);
        replyKeyboardMarkup.setResizeKeyboard(true);
        if(!place_holder.equals(null)) { replyKeyboardMarkup.setInputFieldPlaceholder(place_holder); }

        return replyKeyboardMarkup;
    }
    private static List<InlineKeyboardButton> row1 = new ArrayList<>();
    private static List<InlineKeyboardButton> row2 = new ArrayList<>();
    private static List<InlineKeyboardButton> row3 = new ArrayList<>();
    private static List<InlineKeyboardButton> row4 = new ArrayList<>();
    private static List<InlineKeyboardButton> row5 = new ArrayList<>();
    private static List<InlineKeyboardButton> row6 = new ArrayList<>();
    private static List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
    private static List<KeyboardRow> keyboard = new ArrayList<>();
    private static KeyboardRow row_Keyboard = new KeyboardRow();
    private static void update_keybord_row_list() {

        row1 = new ArrayList<>();
        row2 = new ArrayList<>();
        row3 = new ArrayList<>();
        row4 = new ArrayList<>();
        row5 = new ArrayList<>();
        row6 = new ArrayList<>();
        rowList = new ArrayList<>();
        keyboard = new ArrayList<>();
        row_Keyboard = new KeyboardRow();
    }
    private enum Switch_button {
    
        CONTACT, LOCATION, POLL, WEBAPP, USER, CHAT;
    
        Switch_button() { }
    
        static KeyboardButton get_btn(String text, Switch_button code, Object o) {
            KeyboardButton btn = new KeyboardButton(text);
            try {
                switch(code) {
                    case CONTACT:   { btn.setRequestContact(true); break; }
                    case LOCATION:  { btn.setRequestLocation(true); break; }
                    case POLL:      { btn.setRequestPoll((KeyboardButtonPollType) o); break; }
                    case WEBAPP:    { btn.setWebApp((WebAppInfo) o); break; }
                    case USER:      { btn.setRequestUser((KeyboardButtonRequestUser) o); break; }
                    case CHAT:      { btn.setRequestChat((KeyboardButtonRequestChat) o); break; }
                }
            }
            catch(Exception e) { print.error(" # get_btn "); }
            return btn;
        }
    }

    public Keyboard() { }

}