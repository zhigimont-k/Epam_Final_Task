package by.epam.web.constant;

public final class PageAddress {
    public static final String SERVLET_NAME = "app";
    public static final String HOME_PAGE = "/home";
    public static final String REGISTER_PAGE = "/register";
    public static final String LOGIN_PAGE = "/login";
    public static final String ERROR_PAGE = "/error";
    public static final String USERS_PAGE = "/users";
    public static final String ACTIVITIES_PAGE = "/services";
    public static final String ACCOUNT_PAGE = "/account";
    public static final String USER_ORDERS_PAGE = "/userOrders";
    public static final String ALL_ORDERS_PAGE = "/allOrders";
    public static final String EDIT_ACTIVITY_PAGE = "/editService";
    public static final String VIEW_ACTIVITY_PAGE = "/viewService";
    public static final String EDIT_REVIEW_PAGE = "/editReview";
    public static final String VIEW_ORDER_PAGE = "/viewOrder";
    public static final String ADD_ORDER_PAGE = "/addOrder";
    public static final String ADD_MONEY_PAGE = "/addMoney";
    public static final String NOT_FOUND_ERROR_PAGE = "/notFound";
    public static final String FORBIDDEN_ERROR_PAGE = "/forbidden";

    public static final String VIEW_ACTIVITIES = "app?command=viewActivities";
    public static final String VIEW_ALL_ORDERS = "app?command=viewAllOrders";
    public static final String VIEW_USERS = "app?command=viewUsers";
    public static final String VIEW_ACTIVITY = "app?command=viewActivity&activityId=";
    public static final String VIEW_USER_ORDERS = "app?command=viewUserOrders";
    public static final String VIEW_USER_INFO = "app?command=viewUserInfo";


    private PageAddress(){}
}
