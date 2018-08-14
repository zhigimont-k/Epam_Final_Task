package by.epam.web.dao;

import by.epam.web.dao.AbstractDao;
import by.epam.web.dao.DaoException;
import by.epam.web.entity.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewDao extends AbstractDao<Review> {
    /**
     * Adds review to database
     *
     * @param userId
     * author of the revies
     * @param activityId
     * ID of the activity the revies is for
     * @param mark
     * mark for the activity
     * @param message
     * message in the review
     * @throws DaoException if exception occurs
     */
    void addReview(int userId, int activityId, int mark, String message) throws DaoException;

    /**
     * Updates existing review
     *
     * @param reviewId
     * ID of the review to update
     * @param mark
     * new mark
     * @param message
     * new message
     * @throws DaoException if exception occurs
     */
    void updateReview(int reviewId, int mark, String message) throws DaoException;

    /**
     * Looks for a review with given ID
     *
     * @param id
     * ID of the review to look for
     * @return
     * Found review
     * @throws DaoException if exception occurs
     */
    Optional<Review> findReviewById(int id) throws DaoException;

    /**
     * Looks for review for a given activity
     *
     * @param activityId
     * ID of the activity to find reviews for
     * @return
     * List of found reviews
     * @throws DaoException if exception occurs
     */
    List<Review> findReviewsByActivityId(int activityId) throws DaoException;

    /**
     * Deletes review from the database
     *
     * @param id
     * ID of the review to delete
     * @throws DaoException if exception occurs
     */
    void deleteReviewById(int id) throws DaoException;
}
