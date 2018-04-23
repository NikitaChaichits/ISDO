package by.i4t.repository;

import by.i4t.objects.GisunExportInfo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by Ilya on 08.11.2016.
 */
public interface GisunExportInfoRepository extends BaseUUIDRepository<GisunExportInfo> {
    @Query("select distinct errorCode from GisunExportInfo where status = 'ERROR'")
    List<String> getUniqueExportErrors();

    Long countByErrorCode(String errorCode);

    List<GisunExportInfo> findByStatus(String status, Pageable pageable);

    @Query("select vuzDocument.eduOrganization.code, count(vuzDocument.eduOrganization.code) from GisunExportInfo where errorCode = ?1 group by vuzDocument.eduOrganization.code")
    List getCountListByErrorCode(String errorCode);
}
