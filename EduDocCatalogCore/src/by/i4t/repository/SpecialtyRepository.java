package by.i4t.repository;

import by.i4t.objects.Specialty;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by Ilya on 09.11.2016.
 */
public interface SpecialtyRepository extends BaseUUIDRepository<Specialty> {
    @Query("select max(code) from Specialty")
    Integer getMaxCode();

    @Query("select s from Specialty as s where s.OKRBCode = ?1 and s.name = ?2")
    List<Specialty> findByOKRBCodeByName(String code, String name);
}
