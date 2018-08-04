package by.epam.web.service;

import by.epam.web.dao.DaoException;
import by.epam.web.dao.ReviewDao;
import by.epam.web.dao.impl.ReviewDaoImpl;
import by.epam.web.entity.Review;

import java.util.List;
import java.util.Optional;

public class ReviewService {
    private static final ReviewDao reviewDao = new ReviewDaoImpl();

    ReviewService(){}

    public void addReview(int userId, int activityId, int mark, String message) throws ServiceException {
        try {
            reviewDao.addReview(userId, activityId, mark, message);
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

    public void updateReview(int id, int mark, String message) throws ServiceException {
        try {
            reviewDao.updateReview(id, mark, message);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }
}
