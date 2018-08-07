package by.epam.web.constant;

public final class PageAddress {
    public static final String SERVLET_NAME = "app";

    public static final String HOME_PAGE = "/home";
    public static final String REGISTER_PAGE = "/register";
    public static final String LOGIN_PAGE = "/login";

    public static final String ACCOUNT_PAGE = "/WEB-INF/jsp/user/account.jsp";
    public static final String ADD_MONEY_PAGE = "/addMoney";

    public static final String ERROR_PAGE = "/WEB-INF/jsp/error/error.jsp";
    public static final String NOT_FOUND_ERROR_PAGE = "/WEB-INF/jsp/error/notFound.jsp";
    public static final String FORBIDDEN_ERROR_PAGE = "/WEB-INF/jsp/error/forbidden.jsp";
    public static final String BAD_REQUEST_ERROR_PAGE = "/WEB-INF/jsp/error/badRequest.jsp";

    public static final String USERS_PAGE = "/WEB-INF/jsp/admin/users.jsp";

    public static final String ACTIVITIES_PAGE = "/WEB-INF/jsp/common/services.jsp";
    public static final String EDIT_ACTIVITY_PAGE = "/WEB-INF/jsp/admin/editService.jsp";
    public static final String VIEW_ACTIVITY_PAGE = "/WEB-INF/jsp/common/service.jsp";


    public static final String ADD_ORDER_PAGE = "/WEB-INF/jsp/user/addOrder.jsp";
    public static final String VIEW_ORDER_PAGE = "/viewOrder";
    public static final String USER_ORDERS_PAGE = "/WEB-INF/jsp/user/userOrders.jsp";
    public static final String ALL_ORDERS_PAGE = "/WEB-INF/jsp/admin/allOrders.jsp";

    public static final String EDIT_REVIEW_PAGE = "/WEB-INF/jsp/user/editReview.jsp";


    public static final String VIEW_ACTIVITIES = "app?command=viewActivities";
    public static final String VIEW_ALL_ORDERS = "app?command=viewAllOrders&pageNumber=1";
    public static final String VIEW_USERS = "app?command=viewUsers";
    public static final String VIEW_ACTIVITY = "app?command=viewActivity&activityId=";
    public static final String VIEW_USER_ORDERS = "app?command=viewUserOrders&pageNumber=1";
    public static final String VIEW_USER_INFO = "app?command=viewUserInfo";
    public static final String ADD_ORDER = "app?command=createOrder";


    private PageAddress(){}
}
