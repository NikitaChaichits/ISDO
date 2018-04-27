package by.i4t.repository;

import by.i4t.objects.User;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by Ilya on 08.11.2016.
 */
public interface UserRepository extends BaseUUIDRepository<User> {
    User findByCertificatId(String certificatId);

    List<User> findByEduOrgTypeAndRole(Integer eduOrganizationType, String userRole);

    List<User> findByRole(String userRole);

    @Query("select user from User user where user.role = ?1 and user.eduOrganization.name = ?2")
    List<User> findByRoleAndEduOrganization(String userRole, String eduOrgName);
}
