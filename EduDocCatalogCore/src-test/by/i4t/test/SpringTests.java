package by.i4t.test;

import by.i4t.helper.UserRole;
import by.i4t.objects.Citizen;
import by.i4t.objects.User;
import by.i4t.objects.VUZDocument;
import by.i4t.repository.*;
import by.i4t.repository.search.*;
import com.google.gson.Gson;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Ilya on 04.11.2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestConfig.class})
public class SpringTests {
    @Autowired
    VUZDocumentRepository documentRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    GisunExportInfoRepository gisunExportInfoRepository;
    @Autowired
    ExceptionInfoRepository exceptionInfoRepository;
    @Autowired
    SpecialtyRepository specialtyRepository;


    @Test
    public void docSearch() throws Exception {
        Specification<VUZDocument> specification = new Specification<VUZDocument>() {
            @Override
            public Predicate toPredicate(Root<VUZDocument> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

                return criteriaBuilder.equal(root.get("citizen"), new Citizen());
            }
        };
        SearchPageRequest request = new SearchPageRequest(0, 20, Sort.Direction.DESC, "ID");
        List<SearchRestriction> elements = new ArrayList<>();
        elements.add(new SearchRestriction("eduOrganization.shortName", "equals", "БГУИР", null, null));
        SearchFilter criteria = new SearchFilter("OR", elements);
        PageAndFilter filter = new PageAndFilter(request, criteria);
        System.out.println(new Gson().toJson(filter));
        SearchSpecificationBuilder<VUZDocument> builder = new SearchSpecificationBuilder<>(criteria);
        Specification<VUZDocument> build = builder.build();
        System.out.println(documentRepository.findAll(build, request).getTotalElements());


    }

    @Test
    public void getDocs() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -2);
//        List<Map<String, Long>> result = documentRepository.getStatByStatusAndOrgGroupByYear(Arrays.asList(new Integer[]{2, 3}), 83);
        List result = documentRepository.getStatByPeriodGroupByEduLevel(calendar.getTime(), new Date());
        System.out.println(result.size());
//        result.forEach(entry -> {
//            Object[] objArray = (Object[]) entry;
//            System.out.println((((EduOrganization) objArray[0]).getCode() + "   ----  " +
//                    ((Long) objArray[1]).intValue()));
//        });
    }

    @Test
    public void getUser() {
        User user = userRepository.findByCertificatId("40E4722EB303114400000088");
        System.out.println(user);
        List<User> users = userRepository.findByRole(UserRole.ADMIN.getCode());
        users.forEach(System.out::println);
        users = userRepository.findByEduOrgTypeAndRole(5, UserRole.USER.getCode());
        users.forEach(user1 -> System.out.println(user1));
    }

    @Test
    public void getExceptions() {
        exceptionInfoRepository.findTop200ByOrderByExceptionDateDesc().forEach(info -> {
            System.out.println(info);
        });
    }

    @Test
    public void getSpecialty() {
        System.out.println(specialtyRepository.getMaxCode());
    }

    @Test
    public void getErrors() {
        gisunExportInfoRepository.getCountListByErrorCode("02").forEach(info -> {
            System.out.println(info);
        });
//        documentRepository.getByGisunErrorCodeAndEduOrgCode("02", 83).forEach(doc -> System.out.println(doc));
//        gisunExportInfoRepository.getByGisunErrorCodeAndEduOrgCode("02",83).forEach(doc -> System.out.println(doc));
    }

}
