package by.epam.web.pool;

final class ConnectionData {
    public static final String BASE_NAME = "database";
    public static final String DATABASE_USER = BASE_NAME + ".user";
    public static final String DATABASE_PASSWORD = BASE_NAME + ".password";
    public static final String DATABASE_POOL_SIZE = BASE_NAME + ".poolSize";
    public static final String DATABASE_URL = BASE_NAME + ".url";
    public static final String DATABASE_DRIVER = BASE_NAME + ".driver";

    private ConnectionData() {
    }
}
