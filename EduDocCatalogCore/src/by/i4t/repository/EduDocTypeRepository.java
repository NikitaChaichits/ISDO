package by.i4t.repository;

import by.i4t.objects.EduDocType;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * Created by Ilya on 09.11.2016.
 */
public interface EduDocTypeRepository extends PagingAndSortingRepository<EduDocType, Integer>, JpaSpecificationExecutor<EduDocType> {
    List<EduDocType> findAll();

    EduDocType findFirstByName(String name);
}
