package helpfit;
import static helpfit.Panel.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import auxiliary.Print;
import helpfit_status.Order_status;

@Component
public class Order {

    // Поток который уведомляет всех пользователей о новом заказе
    public Thread order_mailing_carried_out = new Thread(new Runnable() { @Override public void run() {

            print.result("[Order] - ON\n");
            while(true) {
                while(panel.key_scan_orders) {
                    try {
                        Thread.sleep(panel.time_out);
                        print.key_print = false;
                        ResultSet created_order = sql.sql_callback("SELECT id_order, order_status FROM orders WHERE order_status = 'CREATED';");
                        while(created_order.next()) {

                            order.id_order = created_order.getInt("id_order");
                            order.order_status = created_order.getString("order_status");
                            new Bot().mailings_orders_everyone("🔥Появилася новый заказ[" + order.id_order + "]", order.id_order);
                        }
                    }
                    catch(SQLException e) { new Print().error("[Order] - " + e); }
                    catch(InterruptedException e) { e.printStackTrace(); }
                    catch(TelegramApiException e) { e.printStackTrace(); }
                }
            }
    } });

    // Таблица Orders
    public int id_order;
    public String date_time;
    public boolean mailing_carried_out;
    public int quantity_workers;
    public String address;
    public double latitude;
    public double longitude;
    public String task;
    public int profit;
    public String order_status;
    public int count;

    // order_on_activity
    public int id_activity_user_on_order;
    public int id_user;
    public String progress;

    public void set_order_status(Order_status order_status2) {

        order.order_status = order_status2.toString();
        sql.sql_update("UPDATE orders SET order_status = '" + order_status2 + "' WHERE id_user = " + users.id_user + ";");
    }
    public void set_date_time(String date_time2) {

        order.date_time = date_time2;
        sql.sql_update("UPDATE orders SET date_time = '" + date_time2 + "' WHERE id_user = " + users.id_user + ";");
    }
    public void set_quantity_workers(int quantity_workers2) {

        order.quantity_workers = quantity_workers2;
        sql.sql_update("UPDATE orders SET quantity_workers = " + quantity_workers2 + " WHERE id_user = " + users.id_user + ";");
    }
    public void set_address(String address2) {

        order.address = address2;
        sql.sql_update("UPDATE orders SET address = '" + address2 + "' WHERE id_user = " + users.id_user + ";");
    }
    public void set_location(double latitude2, double longitude2) {

        order.latitude = latitude2;
        order.longitude = longitude2;
        sql.sql_update("UPDATE orders SET latitude = " + latitude2 + ", longitude = " + longitude2 + " WHERE id_user = " + users.id_user + ";");
    }
    public void set_task(String task2) {

        order.task = task2;
        sql.sql_update("UPDATE orders SET task = '" + task2 + "' WHERE id_user = " + users.id_user + ";");
    }
    public void set_progress(String progress2) {

        order.progress = progress2;
        sql.sql_update("UPDATE orders SET progress = '" + progress2 + "' WHERE id_user = " + users.id_user + ";");
    }
    public void set_profit(int profit2) {

        order.profit = profit2;
        sql.sql_update("UPDATE orders SET profit = " + profit2 + " WHERE id_user = " + users.id_user + ";");
    }

    public ResultSet list_order = null;
    public void get_list_order_all() {
        order.list_order = null;
        order.list_order = sql.sql_callback("SELECT * FROM orders;");
        
    }
    public void get_list_orders_data(ResultSet list_orders) throws SQLException {

        order.count = list_orders.getInt("count");
        order.id_order = list_orders.getInt("id_order");
        order.latitude = list_orders.getDouble("latitude");
        order.longitude = list_orders.getDouble("longitude");
        order.address = list_orders.getString("address");
        order.quantity_workers = list_orders.getInt("quantity_workers");
        order.date_time = list_orders.getString("date_time");
        order.task = list_orders.getString("task");
        order.profit = list_orders.getInt("profit");
    }

}