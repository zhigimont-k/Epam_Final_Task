package by.epam.web.service;

import by.epam.web.dao.DaoException;
import by.epam.web.dao.activity.ActivityDao;
import by.epam.web.dao.activity.impl.ActivityDaoImpl;
import by.epam.web.entity.Activity;

import java.math.BigDecimal;
import java.util.List;

public class ActivityService {
    private static final ActivityDao activityDao = new ActivityDaoImpl();
    ActivityService(){}

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

    public List<Activity> findAllActivities() throws ServiceException {
        try {
            return activityDao.findAllActivities();
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public Activity changeActivityStatus(String name, String status) throws ServiceException{
        try {
            Activity found = activityDao.findActivityByName(name);
            return activityDao.changeActivityStatus(found.getId(), status);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }
}
