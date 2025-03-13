package lk.ijse.orm.ormproject.dao;

import java.util.List;
import java.util.Optional;

/**
 * @author manuthlakdiv
 * @email manuthlakdiv2006.com
 * @project ORM-PROJECT
 * @github https://github.com/ManuthLakdiw
 */
public interface CrudDao<T extends SuperDao , ID> extends SuperDao {
    boolean save(T entity) throws Exception;
    boolean update(T entity) throws Exception;
    boolean delete(ID id) throws Exception;
    List<T> getAll() throws Exception;
    Optional<T> findById(ID pk) throws Exception;
    Optional<String> lastPK() throws Exception;
}
