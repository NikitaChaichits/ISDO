package by.i4t.repository;

import by.i4t.objects.EduOrganization;

import java.util.List;

/**
 * Created by Ilya on 09.11.2016.
 */
public interface EduOrganizationRepository extends BaseUUIDRepository<EduOrganization> {
    List<EduOrganization> findByCode(Integer code);

    List<EduOrganization> findByName(String name);
}
