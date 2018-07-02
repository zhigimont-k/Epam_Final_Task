package by.epam.web.service;

public class ServiceFactory {

    private static ServiceFactory instance = new ServiceFactory();
    private OrderService orderService = new OrderService();
    private UserService userService = new UserService();
    private ServiceService serviceService = new ServiceService();

    public static ServiceFactory getInstance() {
        return instance;
    }

    public OrderService getOrderService() {
        return orderService;
    }

    public UserService getUserService() {
        return userService;
    }

    public ServiceService getServiceService() {
        return serviceService;
    }

    private ServiceFactory(){}
}
