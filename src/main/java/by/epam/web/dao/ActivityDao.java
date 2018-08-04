package by.epam.web.dao;

import by.epam.web.dao.AbstractDao;
import by.epam.web.dao.DaoException;
import by.epam.web.entity.Activity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ActivityDao extends AbstractDao<Activity> {
    void addActivity(Activity activity) throws DaoException;
    void updateActivity(int id, String name, String description,
                                      BigDecimal price, String status) throws DaoException;
    Optional<Activity> findActivityById(int id) throws DaoException;
    Optional<Activity> findActivityByName(String name) throws DaoException;
    List<Activity> findAllActivities() throws DaoException;
    List<Activity> findAvailableActivities() throws DaoException;
    void changeActivityStatus(int id, String status) throws DaoException;
}
