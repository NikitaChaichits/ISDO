package by.i4t.repository;

import by.i4t.objects.Specialty;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by Ilya on 09.11.2016.
 */
public interface SpecialtyRepository extends BaseUUIDRepository<Specialty> {
    @Query("select max(code) from Specialty")
    Integer getMaxCode();
}
