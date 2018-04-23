package by.i4t.repository;

import by.i4t.objects.Citizen;

import java.util.List;
import java.util.UUID;

/**
 * Created by Ilya on 09.11.2016.
 */
public interface CitizenRepository extends BaseUUIDRepository<Citizen> {

    List<Citizen> findByIdNumber(String idNumber);

    List<Citizen> findByFirstNameAndSecondNameAndPatronymicAndMemberOfBel(String firstName, String secondName, String patronymic, String memberOfBel);
}
