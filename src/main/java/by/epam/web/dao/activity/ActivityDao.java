package by.epam.web.dao.activity;

import by.epam.web.dao.AbstractDao;
import by.epam.web.dao.DaoException;
import by.epam.web.entity.Activity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ActivityDao extends AbstractDao<Activity> {
    Activity addActivity(Activity activity) throws DaoException;
    Optional<Activity> updateActivity(int id, String name, String description, BigDecimal price) throws DaoException;
    Optional<Activity> findActivityById(int id) throws DaoException;
    Optional<Activity> findActivityByName(String name) throws DaoException;
    List<Activity> findAllActivities() throws DaoException;
    List<Activity> findAvailableActivities() throws DaoException;
    Optional<Activity> changeActivityStatus(int id, String status) throws DaoException;
}
