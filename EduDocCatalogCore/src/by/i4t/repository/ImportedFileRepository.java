package by.i4t.repository;

import by.i4t.objects.EduOrganization;
import by.i4t.objects.ImportedFile;

import java.util.List;

/**
 * Created by Ilya on 09.11.2016.
 */
public interface ImportedFileRepository extends BaseUUIDRepository<ImportedFile> {
    List<ImportedFile> findByEduOrg(EduOrganization eduOrganization);
    List<ImportedFile> findByStatus(Integer status);
}
