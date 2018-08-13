package by.epam.web.service;

import by.epam.web.dao.DaoException;
import by.epam.web.dao.ActivityDao;
import by.epam.web.dao.impl.ActivityDaoImpl;
import by.epam.web.entity.Activity;
import by.epam.web.validation.ActivityValidator;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class ActivityService {
    private static final ActivityDao activityDao = new ActivityDaoImpl();

    ActivityService() {
    }

    public boolean addActivity(String name, String description, String price) throws ServiceException {
        try {
            if (!ActivityValidator.getInstance().validateActivity(name, description, price)) {
                return false;
            }
            Activity activity;
            activity = new Activity();
            activity.setName(name);
            activity.setDescription(description);
            activity.setPrice(new BigDecimal(price));
            activityDao.addActivity(activity);
            return true;
        } catch (DaoException e){
            throw new ServiceException(e);
        }
    }

    public boolean nameExists(String name) throws ServiceException {
        try {
            Optional<Activity> found = activityDao.findActivityByName(name);
            return found.isPresent();
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public boolean activityExists(int activityId, String activityName) throws ServiceException {
        try {
            List<Activity> allActivities = activityDao.findAllActivities();
            for (Activity activity : allActivities) {
                if (activity.getName().equalsIgnoreCase(activityName) &&
                        activity.getId() != activityId) {
                    return true;
                }
            }
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
        return false;
    }

    public List<Activity> findAllActivities() throws ServiceException {
        try {
            return activityDao.findAllActivities();
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public List<Activity> findAvailableActivities() throws ServiceException {
        try {
            return activityDao.findAvailableActivities();
        } catch (DaoException e) {
            throw new ServiceException(e);
        }

    }

    public boolean updateActivity(int id, String name, String description, String price,
                                  String status) throws ServiceException {
        if (!ActivityValidator.getInstance().validateActivity(name, description, price) ||
                !ActivityValidator.getInstance().validateStatus(status)) {
            return false;
        }
        try {
            activityDao.updateActivity(id, name,
                    description, new BigDecimal(price), status);
            return true;
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public Optional<Activity> findActivityById(int id) throws ServiceException {
        try {
            return activityDao.findActivityById(id);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }
}
