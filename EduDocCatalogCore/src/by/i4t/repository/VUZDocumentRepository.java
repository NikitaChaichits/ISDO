package by.i4t.repository;

import by.i4t.objects.EduDocType;
import by.i4t.objects.EduOrganization;
import by.i4t.objects.VUZDocument;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Ilya on 03.11.2016.
 */
public interface VUZDocumentRepository extends BaseUUIDRepository<VUZDocument> {
    Long countByStatus(Integer status);

    List<VUZDocument> findByStatusNotNull(Pageable pageable);

    List<VUZDocument> findByStatusNotNull();

    List<VUZDocument> findByDocNumberAndDocSeria(String docNumber, String docSeria);

    List<VUZDocument> findByDocNumberAndDocSeriaAndEduOrganization(String docNumber, String docSeria, EduOrganization eduOrganization);

    List<VUZDocument> findByDocRegNumberAndDocNumberAndDocTypeAndEduOrganization(String docRegNumber, String docNumber, EduDocType docType, EduOrganization eduOrganization);

    @Query("select gi.vuzDocument from GisunExportInfo as gi where gi.errorCode = ?1 and gi.vuzDocument.eduOrganization.code = ?2")
    List<VUZDocument> getByGisunErrorCodeAndEduOrgCode(String errorCode, Integer eduOrgCode);

    //TODO: refactor to entity
    @Query("select org, count(*) from VUZDocument as doc left join doc.eduOrganization as org where doc.status in ?1 and doc.docIssueDate between ?2 and ?3 group by org.id")
    List getCountListByStatusAndPeriodGroupByOrg(Integer[] status, Date lo, Date hi);

    //TODO: refactor to entity
    @Query("select org, count(*) from VUZDocument as doc left join doc.eduOrganization as org where date_part('year', doc.docIssueDate) = ?1 group by org.id")
    List getCountListByYearGroupByOrg(Integer year);

    //Запрос для админа. Вкладка Статистика. Подсчет кол-ва доков по периоду для определенного учреждения
    @Query("select docType.name, count(*) from VUZDocument as doc left join doc.docType as docType where (doc.eduOrganization.code = ?3) and (doc.docIssueDate between ?1 and ?2) group by docType.ID")
    List getCountListByPeriod(Date startDate, Date endDate, Integer eduOrgCode);

    //Запрос для админа. Вкладка Статистика. Подсчет кол-ва доков для всех учреждений по периоду
    @Query("select docType.name, count(*) from VUZDocument as doc left join doc.docType as docType where doc.docIssueDate between ?1 and ?2 group by docType.ID")
    List getCountListByPeriodForAll(Date startDate, Date endDate);

    //Запрос для админа. Вкладка Статистика. Подсчет общего кол-ва доков для по периоду
    @Query("select count(*) from VUZDocument as doc left join doc.docType as docType where (doc.eduOrganization.code = ?3) and (doc.docIssueDate between ?1 and ?2)")
    Integer getTotal(Date startDate, Date endDate, Integer eduOrgCode);

    //TODO: refactor to entity
    @Query("select extract(year from doc.docIssueDate) as year, count(*) as count from VUZDocument as doc where doc.status in ?1 and doc.eduOrganization.code = ?2 group by extract(year from doc.docIssueDate)")
    List<Map<String, Number>> getStatByStatusAndOrgGroupByYear(List<Integer> status, Integer eduOrgCode);

    //TODO: refactor to entity
    @Query("select extract(year from doc.docIssueDate) as year, count(*) as count from VUZDocument as doc where doc.eduOrganization.code = ?1 group by extract(year from doc.docIssueDate)")
    List<Map<String, Number>> getStatByOrgGroupByYear(Integer eduOrgCode);

    //TODO: refactor to entity
    @Query("select eduLevel, count(*) from VUZDocument as doc left join doc.docType.eduLevel as eduLevel where doc.docIssueDate between ?1 and ?2 group by eduLevel.id")
    List getStatByPeriodGroupByEduLevel(Date lo, Date hi);

    //TODO: refactor to entity
    @Query("select docType.name, count(*) from VUZDocument as doc left join doc.docType as docType where (docType.eduLevel = 4\n" +
            " or docType.eduLevel = 5)\n" +
            " and (date_part('year', doc.docIssueDate)) = ?1 group by docType.ID")
    List getStatByYearGroupByEduLevel(Integer year);

    //TODO: refactor to entity
    @Query("select docType.name, count(*) from VUZDocument as doc left join doc.docType as docType where (doc.eduOrganization.code = ?2) and (docType.eduLevel = 4\n" +
            " or docType.eduLevel = 5)\n" +
            " and (date_part('year', doc.docIssueDate)) = ?1 group by docType.ID")
    List getStatByYearGroupByEduLevelAndOrg(Integer year, Integer orgID);

    @Query ("select doc.docNumber from VUZDocument as doc group by doc.docNumber, doc.docType, doc.status having count(*) > 1 and doc.status= ?1 and doc.docType.ID=?2")
    List getVUZDocumentByDocNumber (Integer status, Integer docType);

    @Query("select doc from VUZDocument as doc where doc.status= ?1 and doc.docSeria='А'")
    List<VUZDocument> findByStatus(Integer status);

}
