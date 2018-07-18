package by.epam.web.dao.activity;

import by.epam.web.dao.AbstractDao;
import by.epam.web.dao.DaoException;
import by.epam.web.entity.Activity;

import java.util.List;

public interface ActivityDao extends AbstractDao<Activity> {
    Activity insertActivity(Activity activity) throws DaoException;
    Activity findActivityById(int id) throws DaoException;
    Activity findActivityByName(String name) throws DaoException;
    List<Activity> findAllActivities() throws DaoException;
    Activity changeActivityStatus(int id, String status) throws DaoException;
    boolean nameExists(String name) throws DaoException;
}
