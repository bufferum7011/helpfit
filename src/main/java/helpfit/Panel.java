package helpfit;
import java.sql.SQLException;
import java.util.Scanner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import auxiliary.Exec_sql;
import auxiliary.Print;

@Component
public class Panel {

    // variables
    @Value("${deft_conditions_bot.keyword}")                public String keyword;
    @Value("${deft_conditions_bot.auto_start}")             public boolean auto_start;
    @Value("${deft_conditions_bot.time_out}")               public long time_out;
    @Value("${deft_conditions_bot.key_scan_orders}")        public boolean key_scan_orders;
    @Value("${deft_conditions_bot.support_contact}")        public String support_contact;
    @Value("${deft_conditions_bot.delete_last_quantity}")   public int delete_last_quantity;
    @Value("${bot.username}")                               public String username;
    @Value("${bot.token}")                                  public String token;
    @Value("${mysql.server}")                               public String mysql_server;
    @Value("${mysql.user}")                                 public String mysql_user;
    @Value("${mysql.password}")                             public String mysql_password;

    private static AnnotationConfigApplicationContext context;
    public static Panel panel;
    public static Users users;
    public static Print print;
    public static Keyboard keyboard;
    public static Exec_sql sql;
    public static Order order;
    
    public static void main(String[] args) throws TelegramApiException, SQLException, ClassNotFoundException, InterruptedException {

        // pulling out bean from the pool
        context =           new AnnotationConfigApplicationContext(Spring_config.class);
        print =             context.getBean("print", Print.class);
        panel =             context.getBean("panel", Panel.class);
        print.result("[Panel] - ON\n");
        sql =               context.getBean("sql", Exec_sql.class);
        users =             context.getBean("users", Users.class);
        keyboard =          context.getBean("keyboard", Keyboard.class);
        order =             context.getBean("order", Order.class);

        //conditions - условия
        if(panel.auto_start) {
            new TelegramBotsApi(DefaultBotSession.class).registerBot(new Bot());
            order.order_mailing_carried_out.start();
        }

        // stimulation of waiting for commands in the console in my thread
        boolean key = true;
        while(key) {
            switch(new Scanner(System.in).nextLine()) {
                case "bot_on":      {

                    new TelegramBotsApi(DefaultBotSession.class).registerBot(new Bot());
                    order.order_mailing_carried_out.start();
                    break;
                }
                case "bot_off":     { break; }
                case "truncate":    {

                    sql.sql_update("CALL reset_tables;");
                    print.result("[truncate]\n");
                    break;
                }
                case "exit":    {
                    
                    context.close();
                    key = false;
                    print.result("[Panel] - OFF");
                    System.exit(0);
                }
                case "send": {

                    String date_time = "17.07.2023; 08:00-17:00";
                    int quantity_workers = 3;
                    String address = "Ул. Менделеева, 10";
                    String task = "Нужны разнорабочие на стройку. Карасить, ностить.";
                    int profit = 1500;
                    double latitude = 57.583233;
                    double longitude = 39.840867;
                    int id_user = 1;
                    
                    String query =
                        "INSERT INTO orders(id_user, date_time, quantity_workers, address, latitude, longitude, task, profit) VALUES(" +
                            id_user + ", '" +
                            date_time  + "', " +
                            quantity_workers + ", '" +
                            address + "', " +
                            latitude + ", " +
                            longitude + ", '" +
                            task + "', " +
                            profit + ");";
                    sql.sql_update(query);
                    break;
                }
                case "mailing_on": { panel.key_scan_orders = true; break; }
                case "mailing_off": { panel.key_scan_orders = false; break; }
                default: { print.error("[Panel] - UNCLEAR\n"); break; }
            }
        }

    }

}