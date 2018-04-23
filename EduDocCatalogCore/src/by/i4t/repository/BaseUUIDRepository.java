package by.i4t.repository;

import by.i4t.dao.interfaces.DBEntity;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.UUID;

/**
 * Created by Ilya on 04.11.2016.
 */
@NoRepositoryBean
public interface BaseUUIDRepository<E extends DBEntity> extends BaseRepository<E, UUID> {
}
