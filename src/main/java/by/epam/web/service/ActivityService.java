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

    public Optional<Activity> changeActivityStatus(int id, String status) throws ServiceException{
        try {
            return activityDao.changeActivityStatus(id, status);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }
}
