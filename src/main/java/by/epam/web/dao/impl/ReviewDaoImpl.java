package by.epam.web.dao.impl;

import by.epam.web.dao.DaoException;
import by.epam.web.dao.ReviewDao;
import by.epam.web.entity.Review;
import by.epam.web.pool.ConnectionPool;
import by.epam.web.pool.PoolException;
import by.epam.web.pool.ProxyConnection;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ReviewDaoImpl implements ReviewDao {
    private static Logger logger = LogManager.getLogger();

    private static ConnectionPool pool = ConnectionPool.getInstance();

    private static final String DB_REVIEW_ID_FIELD = "review_id";
    private static final String DB_REVIEW_USER_ID_FIELD = "user_id";
    private static final String DB_REVIEW_ACTIVITY_ID_FIELD = "service_id";
    private static final String DB_REVIEW_CREATION_DATE_FIELD = "creation_date";
    private static final String DB_REVIEW_MARK_FIELD = "mark";
    private static final String DB_REVIEW_MESSAGE_FIELD = "message";

    private static final String INSERT_REVIEW = "INSERT INTO review " +
            "(user_id, service_id, mark, message) " +
            "VALUES (?, ?, ?, ?)";
    private static final String UPDATE_REVIEW = "UPDATE review " +
            "SET mark = ?, message = ?, creation_date = creation_date " +
            "WHERE review_id = ?";
    private static final String FIND_REVIEW_BY_ID = "SELECT review.review_id, " +
            "review.user_id, review.service_id, review.creation_date, review.mark, review.message " +
            "FROM review " +
            "WHERE review.review_id = ?";
    private static final String FIND_REVIEW_BY_USER_ID = "SELECT review.review_id, " +
            "review.user_id, review.service_id, review.creation_date, review.mark, review.message " +
            "FROM review " +
            "WHERE review.user_id = ?";
    private static final String FIND_REVIEW_BY_ACTIVITY_ID = "SELECT review.review_id, " +
            "review.user_id, review.service_id, review.creation_date, review.mark, review.message " +
            "FROM review " +
            "WHERE review.service_id = ?";
    private static final String DELETE_REVIEW_BY_ID = "DELETE  " +
            "FROM review " +
            "WHERE review.review_id = ?";


    @Override
    public void addReview(int userId, int activityId, int mark, String message) throws DaoException {
        ProxyConnection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = pool.takeConnection();

            preparedStatement = connection.prepareStatement(INSERT_REVIEW,
                    Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, activityId);
            preparedStatement.setInt(3, mark);
            preparedStatement.setString(4, message);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new DaoException("Failed to add review" + e.getMessage(), e);
        } finally {
            try {
                closeStatement(preparedStatement);
                pool.releaseConnection(connection);
            } catch (PoolException e) {
                logger.log(Level.ERROR, e.getMessage(), e);
            }
        }
    }

    @Override
    public void updateReview(int id, int mark, String message) throws DaoException {
        ProxyConnection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = pool.takeConnection();

            preparedStatement = connection.prepareStatement(UPDATE_REVIEW);
            preparedStatement.setInt(1, mark);
            preparedStatement.setString(2, message);
            preparedStatement.setInt(3, id);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new DaoException("Failed to update review" + e.getMessage(), e);
        } finally {
            try {
                closeStatement(preparedStatement);
                pool.releaseConnection(connection);
            } catch (PoolException e) {
                logger.log(Level.ERROR, e.getMessage(), e);
            }
        }
    }

    @Override
    public Optional<Review> findReviewById(int id) throws DaoException {
        ProxyConnection connection = null;
        ResultSet resultSet;
        PreparedStatement preparedStatement = null;
        Optional<Review> result = Optional.empty();
        try {
            connection = pool.takeConnection();

            preparedStatement = connection.prepareStatement(FIND_REVIEW_BY_ID);
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Review review = new Review();

                review.setId(id);
                review.setUserId(resultSet.getInt(DB_REVIEW_USER_ID_FIELD));
                review.setActivityId(resultSet.getInt(DB_REVIEW_ACTIVITY_ID_FIELD));
                review.setCreationDate(resultSet.getTimestamp(DB_REVIEW_CREATION_DATE_FIELD));
                review.setMark(resultSet.getInt(DB_REVIEW_MARK_FIELD));
                review.setMessage(resultSet.getString(DB_REVIEW_MESSAGE_FIELD));
                result = Optional.of(review);
            }

            return result;
        } catch (SQLException e) {
            throw new DaoException("Failed to find review by id" + e.getMessage(), e);
        } finally {
            try {
                closeStatement(preparedStatement);
                pool.releaseConnection(connection);
            } catch (PoolException e) {
                logger.log(Level.ERROR, e.getMessage(), e);
            }
        }
    }

    @Override
    public List<Review> findReviewsByActivityId(int activityId) throws DaoException {
        ProxyConnection connection = null;
        ResultSet resultSet;
        PreparedStatement preparedStatement = null;

        List<Review> reviewList = new ArrayList<>();
        try {
            connection = pool.takeConnection();

            preparedStatement = connection.prepareStatement(FIND_REVIEW_BY_ACTIVITY_ID);
            preparedStatement.setInt(1, activityId);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Review review = new Review();

                review.setId(resultSet.getInt(DB_REVIEW_ID_FIELD));
                review.setUserId(resultSet.getInt(DB_REVIEW_USER_ID_FIELD));
                review.setActivityId(activityId);
                review.setCreationDate(resultSet.getTimestamp(DB_REVIEW_CREATION_DATE_FIELD));
                review.setMark(resultSet.getInt(DB_REVIEW_MARK_FIELD));
                review.setMessage(resultSet.getString(DB_REVIEW_MESSAGE_FIELD));
                reviewList.add(review);
            }

            return reviewList;
        } catch (SQLException e) {
            throw new DaoException("Failed to find review by activity id" + e.getMessage(), e);
        } finally {
            try {
                closeStatement(preparedStatement);
                pool.releaseConnection(connection);
            } catch (PoolException e) {
                logger.log(Level.ERROR, e.getMessage(), e);
            }
        }
    }

    @Override
    public List<Review> findReviewsByUserId(int userId) throws DaoException {
        ProxyConnection connection = null;
        ResultSet resultSet;
        PreparedStatement preparedStatement = null;
        List<Review> reviewList = new ArrayList<>();
        try {
            connection = pool.takeConnection();

            preparedStatement = connection.prepareStatement(FIND_REVIEW_BY_USER_ID);
            preparedStatement.setInt(1, userId);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Review review = new Review();

                review.setId(resultSet.getInt(DB_REVIEW_ID_FIELD));
                review.setUserId(userId);
                review.setActivityId(resultSet.getInt(DB_REVIEW_ACTIVITY_ID_FIELD));
                review.setCreationDate(resultSet.getTimestamp(DB_REVIEW_CREATION_DATE_FIELD));
                review.setMark(resultSet.getInt(DB_REVIEW_MARK_FIELD));
                review.setMessage(resultSet.getString(DB_REVIEW_MESSAGE_FIELD));
                reviewList.add(review);
            }

            return reviewList;
        } catch (SQLException e) {
            throw new DaoException("Failed to find review by user id" + e.getMessage(), e);
        } finally {
            try {
                closeStatement(preparedStatement);
                pool.releaseConnection(connection);
            } catch (PoolException e) {
                logger.log(Level.ERROR, e.getMessage(), e);
            }
        }
    }

    @Override
    public void deleteReviewById(int id) throws DaoException {
        ProxyConnection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = pool.takeConnection();

            preparedStatement = connection.prepareStatement(DELETE_REVIEW_BY_ID);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new DaoException("Failed to find review by user id" + e.getMessage(), e);
        } finally {
            try {
                closeStatement(preparedStatement);
                pool.releaseConnection(connection);
            } catch (PoolException e) {
                logger.log(Level.ERROR, e.getMessage(), e);
            }
        }
    }
}