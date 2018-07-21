package by.epam.web.dao.review;

import by.epam.web.dao.AbstractDao;
import by.epam.web.dao.DaoException;
import by.epam.web.entity.Review;

import java.util.Optional;

public interface ReviewDao extends AbstractDao<Review> {
    Review addReview(Review review) throws DaoException;
    Optional<Review> updateReview(int reviewId, int mark, String message) throws DaoException;
    Optional<Review> findReviewById(int id) throws DaoException;
    Optional<Review> findReviewByActivityId(int activityId) throws DaoException;
    Optional<Review> findReviewByUserId(int userId) throws DaoException;
}
