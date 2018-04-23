package by.i4t.repository;

import by.i4t.objects.EduOrganizationType;

/**
 * Created by Ilya on 09.11.2016.
 */
public interface EduOrganizationTypeRepository extends BaseUUIDRepository<EduOrganizationType> {
    EduOrganizationType findFirstByCode(Integer code);
}
