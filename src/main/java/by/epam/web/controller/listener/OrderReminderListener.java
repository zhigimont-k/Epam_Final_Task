package by.epam.web.controller.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@WebListener
public class OrderReminderListener implements ServletContextListener {
    private ScheduledExecutorService executorService;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleWithFixedDelay(new OrderReminderThread(), 0,12,
                TimeUnit.HOURS);
    }

    public void contextDestroyed(ServletContextEvent sce) {
        executorService.shutdownNow();
    }
}
