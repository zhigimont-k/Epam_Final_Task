package by.epam.web.dao.activity;

import by.epam.web.dao.AbstractDao;
import by.epam.web.dao.DaoException;
import by.epam.web.entity.Activity;

import java.math.BigDecimal;
import java.util.List;

public interface ActivityDao extends AbstractDao<Activity> {
    Activity addActivity(Activity activity) throws DaoException;
    Activity updateActivity(int id, String name, String description, BigDecimal price) throws DaoException;
    Activity findActivityById(int id) throws DaoException;
    Activity findActivityByName(String name) throws DaoException;
    List<Activity> findAllActivities() throws DaoException;
    List<Activity> findAvailableActivities() throws DaoException;
    Activity changeActivityStatus(int id, String status) throws DaoException;
}
