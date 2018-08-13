package by.epam.web.service;

import by.epam.web.dao.DaoException;
import by.epam.web.dao.ReviewDao;
import by.epam.web.dao.impl.ReviewDaoImpl;
import by.epam.web.entity.Review;
import by.epam.web.validation.NumberValidator;
import by.epam.web.validation.ReviewValidator;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class ReviewService {
    private static final ReviewDao reviewDao = new ReviewDaoImpl();

    ReviewService() {
    }

    public boolean addReview(String userId, String activityId, String mark, String message)
            throws ServiceException {
        try {
            if (!NumberValidator.getInstance().validateId(userId) ||
                    !ReviewValidator.getInstance().validateMark(mark) ||
                    !ReviewValidator.getInstance().validateMessage(message) ||
                    !NumberValidator.getInstance().validateId(activityId)) {
                return false;
            }
            reviewDao.addReview(Integer.parseInt(userId), Integer.parseInt(activityId),
                    Integer.parseInt(mark), message);
            return true;
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public boolean deleteReview(String id) throws ServiceException {
        try {
            if (!NumberValidator.getInstance().validateId(id)) {
                return false;
            }
            reviewDao.deleteReviewById(Integer.parseInt(id));
            return true;
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public Optional<Review> findReviewById(String id) throws ServiceException {
        try {
            return (NumberValidator.getInstance().validateId(id)) ?
                    reviewDao.findReviewById(Integer.parseInt(id)) :
                    Optional.empty();
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public List<Review> findReviewByActivityId(String id) throws ServiceException {
        try {
            return (NumberValidator.getInstance().validateId(id)) ?
                    reviewDao.findReviewsByActivityId(Integer.parseInt(id)) :
                    Collections.emptyList();
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
