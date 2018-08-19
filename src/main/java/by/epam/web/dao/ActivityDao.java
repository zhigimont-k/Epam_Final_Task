package by.epam.web.dao;

import by.epam.web.entity.Activity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ActivityDao extends AbstractDao<Activity> {
    /**
     * Adds an activity to database
     *
     * @param activity activity to be added to database
     *
     * @throws DaoException if SQLException occurs
     */
    void addActivity(Activity activity) throws DaoException;

    /**
     * Updates an existing activity
     *
     * @param id          id of updated activity
     * @param name        new name of activity
     * @param description new description of activity
     * @param price       new price of activity
     * @param status      new status of activity
     *
     * @throws DaoException if SQLException occurs
     */
    void updateActivity(int id, String name, String description,
                        BigDecimal price, String status) throws DaoException;

    /**
     * Looks for activity with given ID
     *
     * @param id id of activity to look for
     *
     * @return Found activity
     *
     * @throws DaoException if SQLException occurs
     */
    Optional<Activity> findActivityById(int id) throws DaoException;

    /**
     * Looks for activity with given name
     *
     * @param name name of activity to look for
     *
     * @return Found activity
     *
     * @throws DaoException if SQLException occurs
     */
    Optional<Activity> findActivityByName(String name) throws DaoException;

    /**
     * Looks for all activities in the database
     *
     * @return List of found activities
     *
     * @throws DaoException if SQLException occurs
     */
    List<Activity> findAllActivities() throws DaoException;

    /**
     * Looks for activities with 'available' status
     *
     * @return List of found activities
     *
     * @throws DaoException if SQLException occurs
     */
    List<Activity> findAvailableActivities() throws DaoException;
}
