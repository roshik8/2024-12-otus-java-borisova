package ru.otus.demo;

import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.shaded.org.apache.commons.lang3.time.StopWatch;
import ru.otus.cache.HwCache;
import ru.otus.cache.HwListener;
import ru.otus.cache.MyCache;
import ru.otus.core.repository.DataTemplateHibernate;
import ru.otus.core.repository.HibernateUtils;
import ru.otus.core.sessionmanager.TransactionManagerHibernate;
import ru.otus.crm.dbmigrations.MigrationsExecutorFlyway;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;
import ru.otus.crm.service.DBServiceClient;
import ru.otus.crm.service.DbServiceClientCache;
import ru.otus.crm.service.DbServiceClientImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DbServiceDemo {

    private static final Logger log = LoggerFactory.getLogger(DbServiceDemo.class);

    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    public static void main(String[] args) {

        var dbCacheServiceClient = createDBServiceClient(true);
        //намеренно вызываем метод получить всех, чтобы значения сложились в кэш
        var allClients = dbCacheServiceClient.findAll();
        var dbServiceClient = createDBServiceClient(false);

        var clientCount = allClients.size();
        System.out.println("Время выполнения с кэшем: " + culcExecuteTime(dbCacheServiceClient, clientCount));
        System.out.println("Время выполнения без кэша: " + culcExecuteTime(dbServiceClient, clientCount));
        //Время выполнения с кэшем 270, без кеша 18887
    }
    private static DBServiceClient createDBServiceClient(boolean needCache) {
        var configuration = new Configuration().configure(HIBERNATE_CFG_FILE);

        var dbUrl = configuration.getProperty("hibernate.connection.url");
        var dbUserName = configuration.getProperty("hibernate.connection.username");
        var dbPassword = configuration.getProperty("hibernate.connection.password");

        new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword).executeMigrations();
        var sessionFactory = HibernateUtils.buildSessionFactory(configuration, Client.class, Address.class, Phone.class);
        var transactionManager = new TransactionManagerHibernate(sessionFactory);
        var clientTemplate = new DataTemplateHibernate<>(Client.class);
        if (needCache) {
            HwCache<Long, Client> cache = new MyCache<>();
            HwListener<Long, Client> listener = (key, value, action) -> log.info("key: {}, value: {}, action: {}", key, value, action);
            cache.addListener(listener);
            return new DbServiceClientCache(transactionManager, clientTemplate, cache);
        } else {
            return new DbServiceClientImpl(transactionManager, clientTemplate);
        }
    }

    private static long culcExecuteTime(DBServiceClient serviceClient, int clientCount) {
        long startTime = System.currentTimeMillis();
        for( int i = 0; i < clientCount; i++ ) {
            serviceClient.getClient(clientCount);
        }
        return System.currentTimeMillis() - startTime;
    }
}
