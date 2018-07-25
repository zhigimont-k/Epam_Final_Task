package by.epam.web.service;

import by.epam.web.dao.DaoException;
import by.epam.web.dao.review.ReviewDao;
import by.epam.web.dao.review.impl.ReviewDaoImpl;
import by.epam.web.entity.Review;

import java.util.List;
import java.util.Optional;

public class ReviewService {
    private static final ReviewDao reviewDao = new ReviewDaoImpl();

    ReviewService(){}

    public Review addReview(int userId, int activityId, int mark, String message) throws ServiceException {
        Review review;
        try {
            review = new Review();
            review.setUserId(userId);
            review.setActivityId(activityId);
            review.setMark(mark);
            review.setMessage(message);
            return reviewDao.addReview(review);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public void deleteReview(int id) throws ServiceException{
        try {
            reviewDao.deleteReviewById(id);
        } catch (DaoException e){
            throw new ServiceException(e);
        }
    }

    public Optional<Review> findReviewById(int id) throws ServiceException {
        try {
            return reviewDao.findReviewById(id);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public List<Review> findReviewByActivityId(int id) throws ServiceException {
        try {
            return reviewDao.findReviewsByActivityId(id);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public Optional<Review> updateReview(int id, int mark, String message) throws ServiceException {
        try {
            return reviewDao.updateReview(id, mark, message);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }
}
