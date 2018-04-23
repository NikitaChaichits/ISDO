package edu.org.validators;

import by.i4t.exceptions.DataValidationException;
import by.i4t.objects.EduDocType;
import by.i4t.objects.EduOrganization;
import by.i4t.objects.Specialty;
import edu.org.service.ApplicationCache;
import edu.org.service.RepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class VUZEduDocValidator {
    @Autowired
    private ApplicationCache appCache;
    @Autowired
    private RepositoryService repositoryService;

    private char[] ruCharArray = "АВЕЗКМНОРСТУХ".toCharArray();
    private char[] enCharArray = "ABE3KMHOPCTYX".toCharArray();
    private Map<Character, Character> changeMap = new HashMap<>();
    // private EduDocTypeDAO eduDocTypeDAO = new EduDocTypeDAO();
    private Map<String, EduDocType> eduDocTypeMap = new HashMap<String, EduDocType>();
    private Map<String, EduOrganization> eduOrgMap = new HashMap<String, EduOrganization>();
    private Map<String, Specialty> specialtyMap = new HashMap<String, Specialty>();

    public VUZEduDocValidator() {
        // !!!!!!!!!!!!this is a temporary initialization beacuse dao is not
        // actually autowired
        // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

        for (int i = 0; i < ruCharArray.length - 1; i++)
            changeMap.put(ruCharArray[i], enCharArray[i]);
    }

    public void checkPersonalName(String firstName, String secondName, String patronymic, String memberOfBel) throws DataValidationException {
        checkName("имя", firstName);
        checkName("фамилия", secondName);
        //checkName("отчество", patronymic);
    }

    public void checkPersonalNameForeignStudent(String firstName, String secondName, String patronymic, String memberOfBel) throws DataValidationException {
        checkName("имя", firstName);
        checkName("фамилия", secondName);
        //checkName("отчество", patronymic);
    }

    public String checkMemberOfBel (String memberOfBel) throws DataValidationException {
        if ((memberOfBel.equalsIgnoreCase("да")) || (memberOfBel.equalsIgnoreCase("нет")) )
            return memberOfBel;
        else
            throw new DataValidationException("Ошибка проверки данных: в поле Гражданство либо да, либо нет.");
    }

    public String checkPersonalID(String value) throws DataValidationException {
        if (value == null || value.trim().isEmpty())
            throw new DataValidationException("Ошибка проверки данных: отсутствует идентификационный номер физического лица.");

        if (value.contains(" "))
            throw new DataValidationException("Ошибка проверки данных: неверное значение идентификационного номера физического лица (" + value + ").");

        value = value.trim().toUpperCase();
        if (value.length() != 14)
            throw new DataValidationException("Ошибка проверки данных: некоректный формат идентификационного номера физического лица.");

        char[] newBytearray = new char[value.length()];
        char[] originalByteArray = value.toCharArray();

        for (int i = 0; i < 7; ++i)
        newBytearray[i] = checkDigital(originalByteArray[i]);
        newBytearray[7] = checkSymbol(originalByteArray[7]);
        newBytearray[8] = checkDigital(originalByteArray[8]);
        newBytearray[9] = checkDigital(originalByteArray[9]);
        newBytearray[10] = checkDigital(originalByteArray[10]);
        newBytearray[11] = checkSymbol(originalByteArray[11]);
        newBytearray[12] = checkSymbol(originalByteArray[12]);
        newBytearray[13] = checkDigital(originalByteArray[13]);

        return new String(newBytearray);
    }

    public Boolean checkEducationPeriod(Date start, Date stop) throws DataValidationException {
        if (start == null)
            throw new DataValidationException("Ошибка проверки данных: дата начала обучения не указана.");

        if (stop == null)
            throw new DataValidationException("Ошибка проверки данных: дата окончания обучения не указана.");

        if (start.after(stop))
            throw new DataValidationException("Ошибка проверки данных: окончание обучения не должно предшествовать началу.");
        return true;
    }

    public String checkEduDocSeria(String docSeria) throws DataValidationException {
        if (docSeria == null || docSeria.isEmpty())
            throw new DataValidationException("Ошибка проверки данных: не указана серия документа.");
        return docSeria.trim();
    }

    public String checkEduDocNumber(String docNumber) throws DataValidationException {
        if (docNumber == null || docNumber.isEmpty())
            throw new DataValidationException("Ошибка проверки данных: не указан номер документа.");
        if (!docNumber.matches("[0-9]{7}"))
            throw new DataValidationException("Ошибка проверки данных: номер документа должен содержать 7 цифр.");
        return docNumber;
    }

    public String checkEduDocRegNumber(String docRegNumber) throws DataValidationException {
        if (docRegNumber == null || docRegNumber.isEmpty())
            throw new DataValidationException("Ошибка проверки данных: не указан регистрационный номер записи в журнале выдачи документа.");
        return docRegNumber;
    }

    public Date checkEduDocIssueDate(String docIssueDate) throws DataValidationException {
        Date date = null;
        try {
            date = new SimpleDateFormat("dd.MM.yyyy").parse(docIssueDate);
        } catch (ParseException ex) {
            throw new DataValidationException("Ошибка проверки данных: неверно указана дата выдачи документа.");
        }
        return date;
    }

    public EduDocType checkEduDocType(String value) throws DataValidationException {
        if (value == null || value.trim().isEmpty())
            throw new DataValidationException("Ошибка проверки данных: не указан тип документа об образовании.");

        String tmpValue = value.toLowerCase().trim();
        if (eduDocTypeMap.containsKey(tmpValue))
            return eduDocTypeMap.get(tmpValue);

        // EduDocType eduDocType = eduDocTypeDAO.findByName(tmpValue);
        EduDocType eduDocType = repositoryService.getEduDocTypeRepository().findFirstByName(tmpValue);
        if (eduDocType != null) {
            eduDocTypeMap.put(tmpValue, eduDocType);
            return eduDocType;
        } else
            throw new DataValidationException("Ошибка проверки данных: некорректное значение типа документа об образовании.");
    }

    public EduOrganization checkEduOrg(String value) throws DataValidationException {
        if (value == null || value.trim().isEmpty())
            throw new DataValidationException("Ошибка проверки данных: не указано учреждение образования.");

        if (eduOrgMap.containsKey(value.trim()))
            return eduOrgMap.get(value.trim());

        List<EduOrganization> eduOrganizationList = repositoryService.getEduOrganizationRepository().findByName(value.trim());
        if (!eduOrganizationList.isEmpty()) {
            eduOrgMap.put(value, eduOrganizationList.get(0));
            return eduOrganizationList.get(0);
        } else
            throw new DataValidationException("Ошибка проверки данных: некорректное название учреждения образования.");
    }

    public Specialty checkSpecialty(String specialty, String specialization, String kwalification) throws DataValidationException {
        if (specialty == null || specialty.trim().isEmpty())
            throw new DataValidationException("Ошибка проверки данных: не указана специальность.");
        // if (specialization == null || specialization.trim().isEmpty())
        // throw new
        // DataValidationException("Ошибка проверки данных: не указана специализация.");

        Specialty specialtyObj = appCache.getSpecialtiesByName(specialty.trim());

//	String specialtyStr = specialty.trim().toLowerCase();

//	if (specialtyMap.containsKey(specialtyStr))
//	    return specialtyMap.get(specialtyStr);

//	List<Specialty> specialtyList = dao.getByFieldIgnoreCase(Specialty.class, "name", specialtyStr);
//	if (specialtyList.isEmpty())
        if (specialtyObj == null)
            throw new DataValidationException("Ошибка проверки данных: некорректное название специальности.");
        // TODO: add specialization and kwalification
//	specialtyMap.put(specialtyStr, specialtyList.get(0));
        return specialtyObj;
    }

    private char checkDigital(Character value) throws DataValidationException {
        if (Character.isDigit(value))
            return value;
        else {
            Character O_ru = 'О';
            Character O_en = 'O';
            if (value.equals(O_ru) || value.equals(O_en))
                return '0';
            else
                throw new DataValidationException("Ошибка проверки данных: неверный символ  (" + value + ") в значении идентификационного номера физического лица");
        }
    }

    private char checkSymbol(char value) throws DataValidationException {
        if (value >= 'A' && value <= 'Z')
            return value;
        else {
            if (changeMap.containsKey(value))
                return changeMap.get(value);
            else
                throw new DataValidationException("Ошибка проверки данных: неверный символ  (" + value + ") в значении идентификационного номера физического лица");
        }
    }

    private void checkName(String fieldName, String value) throws DataValidationException {
        if (value == null || value.trim().isEmpty())
            throw new DataValidationException("Ошибка проверки данных: не задано значение поля " + fieldName);
    }
}
