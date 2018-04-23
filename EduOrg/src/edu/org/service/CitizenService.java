package edu.org.service;

import by.i4t.exceptions.BusinessConditionException;
import by.i4t.objects.Citizen;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CitizenService {
    @Autowired
    RepositoryService repositoryService;

    public Citizen createNew(String firstName, String secondName, String patronymic, String personalID, String memberOfBel) throws BusinessConditionException {
        List<Citizen> citizenList;
        if (personalID != null && !personalID.isEmpty())
            citizenList = repositoryService.getCitizenRepository().findByIdNumber(personalID);
        else {
            citizenList = repositoryService.getCitizenRepository().findByFirstNameAndSecondNameAndPatronymicAndMemberOfBel(firstName, secondName, patronymic, memberOfBel);
        }

        if (citizenList.size() > 9)
            throw new BusinessConditionException("Гражданин с индификационным номером: " + personalID + " уже существует");


        Citizen citizen = new Citizen();
        if (firstName != null)
            citizen.setFirstName(firstName.trim());
        if (secondName != null)
            citizen.setSecondName(secondName.trim());
        if (patronymic != null)
            citizen.setPatronymic(patronymic.trim());
        if (memberOfBel != null)
            citizen.setMemberOfBel(memberOfBel.trim());
        citizen.setIdNumber(personalID);
        repositoryService.getCitizenRepository().save(citizen);
        return citizen;
    }

    public Citizen createNew(String firstName, String secondName, String patronymic, String memberOfBel) throws BusinessConditionException {
        Citizen citizen = new Citizen();
        if (firstName != null)
            citizen.setFirstName(firstName.trim());
        if (secondName != null)
            citizen.setSecondName(secondName.trim());
        if (patronymic != null)
            citizen.setPatronymic(patronymic.trim());
        if (memberOfBel != null)
            citizen.setMemberOfBel(memberOfBel.trim());
        repositoryService.getCitizenRepository().save(citizen);
        return citizen;
    }

    public Citizen update (UUID ID, String firstName, String secondName, String patronymic, String personalID, String memberOfBel) throws BusinessConditionException {
        List<Citizen> citizenList = repositoryService.getCitizenRepository().findByIdNumber(personalID);
        Citizen citizen = new Citizen();

        citizen.setID(ID);
        citizen.setIdNumber(personalID);
        if (firstName != null)
            citizen.setFirstName(firstName.trim());
        if (secondName != null)
            citizen.setSecondName(secondName.trim());
        if (patronymic != null)
            citizen.setPatronymic(patronymic.trim());
        if (memberOfBel != null)
            citizen.setMemberOfBel(memberOfBel.trim());
        repositoryService.getCitizenRepository().save(citizen);
        citizenList.set(0, citizen);
        return citizenList.get(0);
    }

    public Citizen update (UUID ID, String firstName, String secondName, String patronymic, String memberOfBel) throws BusinessConditionException {
        List<Citizen> citizenList = repositoryService.getCitizenRepository().findByFirstNameAndSecondNameAndPatronymicAndMemberOfBel(firstName, secondName, patronymic, memberOfBel);
        Citizen citizen = new Citizen();

        citizen.setID(ID);
        if (firstName != null)
            citizen.setFirstName(firstName.trim());
        if (secondName != null)
            citizen.setSecondName(secondName.trim());
        if (patronymic != null)
            citizen.setPatronymic(patronymic.trim());
        if (memberOfBel != null)
            citizen.setMemberOfBel(memberOfBel.trim());
        repositoryService.getCitizenRepository().save(citizen);
        citizenList.set(0, citizen);
        return citizenList.get(0);
    }


}
