package by.i4t.repository;

import by.i4t.objects.EduDocType;
import by.i4t.objects.EduOrganization;
import by.i4t.objects.VUZDocument;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Ilya on 03.11.2016.
 */
public interface VUZDocumentRepository extends BaseUUIDRepository<VUZDocument> {
    List<VUZDocument> findByStatusNotNull(Pageable pageable);

    List<VUZDocument> findByStatusNotNull();

    @Query("select gi.vuzDocument from GisunExportInfo as gi where gi.errorCode = ?1 and gi.vuzDocument.eduOrganization.code = ?2")
    List<VUZDocument> getByGisunErrorCodeAndEduOrgCode(String errorCode, Integer eduOrgCode);

    Long countByStatus(Integer status);

    List<VUZDocument> findByDocNumberAndDocSeria(String docNumber, String docSeria);

    List<VUZDocument> findByDocNumberAndDocSeriaAndEduOrganization(String docNumber, String docSeria, EduOrganization eduOrganization);

    List<VUZDocument> findByDocNumberAndDocTypeAndEduOrganization(String docNumber, EduDocType docType, EduOrganization eduOrganization);

    //TODO: refactor to entity
    @Query("select org, count(*) from VUZDocument as doc left join doc.eduOrganization as org where doc.status in ?1 and doc.docIssueDate between ?2 and ?3 group by org.id")
    List getCountListByStatusAndPeriodGroupByOrg(Integer[] status, Date lo, Date hi);

    //TODO: refactor to entity
    @Query("select org, count(*) from VUZDocument as doc left join doc.eduOrganization as org where date_part('year', doc.docIssueDate) = ?1 group by org.id")
    List getCountListByYearGroupByOrg(Integer year);

    //TODO: refactor to entity
    @Query("select extract(year from doc.docIssueDate) as year, count(*) as count from VUZDocument as doc where doc.status in ?1 and doc.eduOrganization.code = ?2 group by extract(year from doc.docIssueDate)")
    List<Map<String, Number>> getStatByStatusAndOrgGroupByYear(List<Integer> status, Integer eduOrgCode);

    //TODO: refactor to entity
    @Query("select extract(year from doc.docIssueDate) as year, count(*) as count from VUZDocument as doc where doc.eduOrganization.code = ?1 group by extract(year from doc.docIssueDate)")
    List<Map<String, Number>> getStatByOrgGroupByYear(Integer eduOrgCode);

    //TODO: refactor to entity
    @Query("select eduLevel, count(*) from VUZDocument as doc left join doc.docType.eduLevel as eduLevel where doc.docIssueDate between ?1 and ?2 group by eduLevel.id")
    List getStatByPeriodGroupByEduLevel(Date lo, Date hi);

   /* //TODO: refactor to entity
    @Query("select eduLevel, count(*) from VUZDocument as doc left join doc.docType.eduLevel as eduLevel where date_part('year', doc.docIssueDate) = ?1 group by eduLevel.id")
    List getStatByYearGroupByEduLevel(Integer year);*/

    //TODO: refactor to entity
    @Query("select docType.name, count(*) from VUZDocument as doc left join doc.docType as docType where (docType.eduLevel = 4\n" +
            " or docType.eduLevel = 5) and (docType.ID = 1 or docType.ID = 5 or docType.ID = 9)\n" +
            " and (date_part('year', doc.docIssueDate)) = ?1 group by docType.ID")
    List getStatByYearGroupByEduLevel(Integer year);

    //TODO: refactor to entity
    @Query("select docType.name, count(*) from VUZDocument as doc left join doc.docType as docType where (doc.eduOrganization.code = ?2) and (docType.eduLevel = 4\n" +
            " or docType.eduLevel = 5) and (docType.ID = 1 or docType.ID = 5 or docType.ID = 9)\n" +
            " and (date_part('year', doc.docIssueDate)) = ?1 group by docType.ID")
    List getStatByYearGroupByEduLevelAndOrg(Integer year, Integer orgID);
}
