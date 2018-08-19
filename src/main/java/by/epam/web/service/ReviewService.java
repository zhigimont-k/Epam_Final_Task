package by.epam.web.service;

import by.epam.web.dao.DaoException;
import by.epam.web.dao.ReviewDao;
import by.epam.web.dao.impl.ReviewDaoImpl;
import by.epam.web.entity.Review;
import by.epam.web.validation.ReviewValidator;

import java.util.List;
import java.util.Optional;

public class ReviewService {
    private static final ReviewDao reviewDao = new ReviewDaoImpl();

    ReviewService() {
    }

    public boolean addReview(int userId, int activityId, String mark, String message)
            throws ServiceException {
        try {
            if (!ReviewValidator.getInstance().validateMark(mark) ||
                    !ReviewValidator.getInstance().validateMessage(message)) {
                return false;
            }
            reviewDao.addReview(userId, activityId, Integer.parseInt(mark), message);
            return true;
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public void deleteReview(int id) throws ServiceException {
        try {
            reviewDao.deleteReviewById(id);
        } catch (DaoException e) {
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

    public boolean updateReview(int id, int mark, String message) throws ServiceException {
        try {
            if (!ReviewValidator.getInstance().validateMark(String.valueOf(mark)) ||
                    !ReviewValidator.getInstance().validateMessage(message)) {
                return false;
            }
            reviewDao.updateReview(id, mark, message);
            return true;
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }
}
