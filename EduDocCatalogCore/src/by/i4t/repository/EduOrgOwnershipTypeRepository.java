package by.i4t.repository;

import by.i4t.objects.EduOrgOwnershipType;

/**
 * Created by Ilya on 09.11.2016.
 */
public interface EduOrgOwnershipTypeRepository extends BaseUUIDRepository<EduOrgOwnershipType> {
    EduOrgOwnershipType findFirstByCode(Integer code);
}
