package helpfit;
import static helpfit.Panel.*;
import helpfit_status.Bot_status;
import helpfit_status.User_status;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class Users {

    // constructor
    public Users() { }
    public Users(Update update) throws SQLException, TelegramApiException {

        // Received message
        if(update.hasMessage()) {
            users.o = update.getMessage();
            users.cmd = users.o.getText();
            try { users.c_cmd = users.cmd.toCharArray(); buffer_c_cmd = ""; } catch(Exception e) { }
        }
        // Received callback
        else if(update.hasCallbackQuery()) {
            users.o = update.getCallbackQuery().getMessage();
            users.callback = update.getCallbackQuery();
            users.data = users.callback.getData();
            users.c_data = users.data.toCharArray();
        }
        // Not received
        else { users.o = null; users.o.setText("UNCLEAR"); }
        
        users.chat_id = users.o.getChatId().toString();
        new Bot().TYPING();
        print.key_print = true;
        users.tag = "@" + users.o.getFrom().getUserName();
        print.result("\n" + users.tag);
        print.key_print = false;

        users.contact = "отсутствует";
        try { users.contact = users.o.getContact().getPhoneNumber(); } catch(Exception e) { }
        users.name = users.o.getFrom().getFirstName();
        users.card = "отсутствует";
        users.rating = 0;
        users.quantity = 0;
        users.msg_id = users.o.getMessageId();
        users.is_authorized = false;
        users.is_blocked = false;
        Boolean key_outside = true, key_inside = false;

        // checking for authorization
        get_list_chat_id();
        while(users.list_chat_id.next() && key_outside) {

            if(users.chat_id.equals(users.list_chat_id.getString("chat_id"))) { key_outside = false; key_inside = true; }
            else { key_outside = true; key_inside = false; }

            // collecting info about user
            ResultSet user_info = sql.sql_callback("SELECT * FROM users WHERE chat_id = " + users.chat_id + ";");
            while(key_inside && user_info.next() && users.chat_id.equals(user_info.getString("chat_id"))) {
                key_outside = false;
                key_inside = false;
                users.is_authorized = true;

                users.id_user = user_info.getInt("id_user");
                users.tag = user_info.getString("tag");
                users.name = user_info.getString("name");
                users.contact = user_info.getString("contact");
                users.rating = user_info.getInt("rating");
                users.quantity = user_info.getInt("quantity");
                users.card = user_info.getString("card");
                users.msg_id = user_info.getInt("msg_id");
                users.user_status = user_info.getString("user_status");
                users.bot_status = user_info.getString("bot_status");

                // Заблокированые пользователи
                if(users.user_status.equals(User_status.BLOCKED.name())) { users.is_blocked = true; }
                print.key_print = true;
                print.way("[user_info]");
            }

        }

        // Если не авторизован то авторизуем
        if(!users.is_authorized) {

            // print.key_print = false;
            String query =
                "INSERT INTO users(tag, chat_id, msg_id, name, contact) VALUES('" +
                    users.tag  + "', '" +
                    users.chat_id + "', " +
                    users.msg_id + ", '" +
                    users.name + "', '" +
                    users.contact + "');";

            sql.sql_update(query);
            print.key_print = true;
            print.way("[registered]");
        }

    }

    // Variables
    public int id_user;
    public String chat_id;
    public String tag;
    public String name;
    public String contact;
    public int msg_id;
    public int quantity;
    public int rating;
    public String card;
    public String user_status;
    public String bot_status;
    public int last_check_order;
    public Message o = null;
    public CallbackQuery callback = null;
    public String cmd = "none";
    public String data = "none";
    public char[] c_data = null;
    public char[] c_cmd = null;
    public String buffer_c_cmd = "none";

    // setters to bd
    public void set_chat_id(String chat_id2) {

        users.chat_id = chat_id2;
        sql.sql_update("INSERT users(chat_id) VALUES(" + users.chat_id + ");");
    }
    public void set_tag(String tag2) {

        users.tag = tag2;
        sql.sql_update("UPDATE users SET tag = '" + tag2 + "' WHERE chat_id = " + users.chat_id + ";");
    }
    public void set_name(String name2) {
        users.name = name2;
        sql.sql_update("UPDATE users SET name = '" + name2 + "' WHERE chat_id = " + users.chat_id + ";");
    }
    public void set_contact(String contact2) {

        users.contact = contact2;
        sql.sql_update("UPDATE users SET contact = '" + contact2 + "' WHERE chat_id = " + users.chat_id + ";");
    }
    public void set_quantity(int quantity2) {

        users.quantity = quantity2;
        sql.sql_update("UPDATE users SET quantity = " + quantity2 + " WHERE chat_id = " + users.chat_id + ";");
    }
    public void set_rating(int rating2) {

        users.rating = rating2;
        sql.sql_update("UPDATE users SET rating = " + rating2 + " WHERE chat_id = " + users.chat_id + ";");
    }
    public void set_card(String card2) {

        users.card = card2;
        sql.sql_update("UPDATE users SET card = '" + card2 + "' WHERE chat_id = " + users.chat_id + ";");
    }
    public void set_msg_id(Integer msg_id2) {

        users.msg_id = msg_id2;
        sql.sql_update("UPDATE users SET msg_id = '" + msg_id2 + "' WHERE chat_id = " + users.chat_id + ";");
    }
    public void set_user_status(User_status user_status2) {

        users.user_status = user_status2.toString();
        sql.sql_update("UPDATE users SET user_status = '" + users.user_status + "' WHERE chat_id = " + users.chat_id + ";");
    }
    public void set_bot_status(Bot_status bot_status2) {

        users.bot_status = bot_status2.toString();
        sql.sql_update("UPDATE users SET bot_status = '" + users.bot_status + "' WHERE chat_id = " + users.chat_id + ";");
    }
    public void set_bot_status(String bot_status2) {

        users.bot_status = bot_status2;
        sql.sql_update("UPDATE users SET bot_status = '" + users.bot_status + "' WHERE chat_id = " + users.chat_id + ";");
    }
    public void set_last_check_order(int last_check_order2, String chat_id2) {

        users.last_check_order = last_check_order2;
        sql.sql_update("UPDATE users SET last_check_order = " + last_check_order2 + " WHERE chat_id = " + chat_id2 + ";");
    }

    // auxiliary variables:
    public boolean is_blocked = false;
    public boolean is_authorized = false;
    public ResultSet list_chat_id = null;
    public void get_list_chat_id() { users.list_chat_id = null; users.list_chat_id = sql.sql_callback("SELECT chat_id FROM users;"); }


}