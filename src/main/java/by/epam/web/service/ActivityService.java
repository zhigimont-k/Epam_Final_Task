package by.epam.web.service;

import by.epam.web.dao.DaoException;
import by.epam.web.dao.activity.ActivityDao;
import by.epam.web.dao.activity.impl.ActivityDaoImpl;
import by.epam.web.entity.Activity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class ActivityService {
    private static final ActivityDao activityDao = new ActivityDaoImpl();

    ActivityService() {
    }

    public Activity addActivity(String name, String description, BigDecimal price) throws ServiceException {
        Activity activity;
        try {
            activity = new Activity();
            activity.setName(name);
            activity.setDescription(description);
            activity.setPrice(price);
            return activityDao.addActivity(activity);
        } catch (DaoException e) {
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

    public List<Activity> findAllActivities() throws ServiceException {
        try {
            return activityDao.findAllActivities();
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public Optional<Activity> findActivityByName(String name) throws ServiceException{
        try {
            return activityDao.findActivityByName(name);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public Optional<Activity> changeActivityStatus(int id, String status) throws ServiceException {
        try {
            return activityDao.changeActivityStatus(id, status);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public Optional<Activity> changeActivityStatus(String name, String status) throws ServiceException {
        try {
            Optional<Activity> found = activityDao.findActivityByName(name);
            if (found.isPresent()) {
                int id = found.get().getId();
                return activityDao.changeActivityStatus(id, status);
            }
            return found;
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public Optional<Activity> updateActivity(Activity activity) throws ServiceException {
        try {
            return activityDao.updateActivity(activity.getId(), activity.getName(),
                    activity.getDescription(), activity.getPrice());
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }
}
