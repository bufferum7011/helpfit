-- Active: 1688308701924@@151.248.117.244@3306@simple_profit
DROP PROCEDURE reset_tables;
CREATE DEFINER=`bufferum`@`%` PROCEDURE `reset_tables`()
BEGIN

    -- Удаляем таблицы, если они ещё существуют
    SET FOREIGN_KEY_CHECKS = 0;
        DROP TABLE IF EXISTS users;
        DROP TABLE IF EXISTS orders;
        DROP TABLE IF EXISTS order_on_activity;
        DROP TABLE IF EXISTS settings;
    SET FOREIGN_KEY_CHECKS = 1;

    -- Создание таблицы пользователей
    CREATE TABLE users (
        id_user INT AUTO_INCREMENT PRIMARY KEY,             -- PRIMARY KEY(id_user)
        tag VARCHAR(100) NULL DEFAULT 'отсутствует',        -- Тег в тг пользователя
        chat_id VARCHAR(50) NOT NULL,                       -- chat_id пользователя
        msg_id INT NULL DEFAULT NULL,                       -- msg_id пользователя
        user_status VARCHAR(100) NULL DEFAULT 'MENU',       -- Статут пользователя
        bot_status VARCHAR(100) NULL DEFAULT 'SERCH_JOB',   -- Статус бота
        name VARCHAR(30) NULL DEFAULT 'no_name',            -- Имя пользователя
        contact VARCHAR(30) NULL DEFAULT 'отсутствует',     -- Номер телефона пользователя
        rating DECIMAL(3,2) NULL DEFAULT 0.0,               -- Рейтинг
        quantity INT(100) NULL DEFAULT 0,                   -- Количество всех заказов
        card VARCHAR(50) NULL DEFAULT 'отсутствует',        -- Карта пользователя
        last_check_order INT NULL DEFAULT 0                 -- Последний заказ о котором мы уведомили пользователя
    );

    -- Создание таблицы заказов
    CREATE TABLE orders (
        id_order INT AUTO_INCREMENT PRIMARY KEY,            -- PRIMARY KEY(id_order)
        id_user INT NOT NULL,                               -- id_user(СВЯЗАН)
        order_status VARCHAR(100) NULL DEFAULT "CREATING",  -- Статус заявки
        latitude DECIMAL(8, 6) NULL DEFAULT 57.626312,      -- Ширина адреса
        longitude DECIMAL(8, 6) NULL DEFAULT 39.884513,     -- Долгота адреса
        profit INT NULL DEFAULT NULL,                       -- Заработок
		date_time VARCHAR(100) NULL DEFAULT "01.01.24; 08:00-17:00",-- Дата и время когда на работу('dd-mm-yy; hh:mi')
        quantity_workers INT NULL DEFAULT 1,                -- Количество рабочих
		address VARCHAR(100) NULL DEFAULT NULL,             -- Адресс заказа
		task VARCHAR(300) NULL DEFAULT NULL,                -- Какая задача

        FOREIGN KEY (id_user) REFERENCES users(id_user) ON DELETE CASCADE
    );

    -- Создаем таблицу активности по заказам
    CREATE TABLE activity_user_on_order (
        id_activity_user_on_order INT AUTO_INCREMENT PRIMARY KEY,-- PRIMARY KEY(id_order_on_activity)
        id_order INT NOT NULL,                              -- id_order(СВЯЗАН)
        id_user INT NOT NULL,                               -- id_user(СВЯЗАН)
        progress VARCHAR(100) NULL DEFAULT 'READY',         -- Прогресс выполнения заказа(Order_progress)

        FOREIGN KEY (id_user) REFERENCES users(id_user) ON DELETE CASCADE,
        FOREIGN KEY (id_order) REFERENCES orders(id_order) ON DELETE CASCADE
    );

    -- Создаем таблицу настроек
    CREATE TABLE settings (
        id_settings INT AUTO_INCREMENT PRIMARY KEY,         -- PRIMARY KEY(id_settings)
        id_user INT NOT NULL,                               -- id_user(СВЯЗАН)
        auto_delete BIT NULL DEFAULT 1,                     -- Авто удаление
        pining_messages BIT NULL DEFAULT 1,                 -- Закрепление сообщений
        priority VARCHAR(100) NULL DEFAULT 'PRIORITY_PART_TIME_JOB', -- Приоритет в работе(PRIORITY_PART_TIME_JOB/PRIORITY_HELP)
		
        FOREIGN KEY (id_user) REFERENCES users(id_user) ON DELETE CASCADE
    );


    -- Views
    CREATE VIEW view_list_block AS SELECT id_user FROM users WHERE user_status = 'BLOCKED';

END