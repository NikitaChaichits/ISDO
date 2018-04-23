package by.i4t.repository;

import by.i4t.dao.interfaces.DBEntity;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Ilya on 19.12.2016.
 */
@NoRepositoryBean
public interface BaseRepository<E extends DBEntity, ID extends Serializable> extends PagingAndSortingRepository<E, ID>, JpaSpecificationExecutor<E> {
    List<E> findAll();
}
