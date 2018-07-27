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

import java.util.*;

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

    public boolean checkEduStartDate (Date start, String personalID) throws DataValidationException{
        String bd="";

        if (personalID == null || personalID.equals(""))
            return true;

        char[] originalByteArray = personalID.toCharArray();

        for (int i = 1; i < 5; ++i) {
            bd += checkDigital(originalByteArray[i]);
            if (i%2==0)
                bd += ".";
        }

        String b_year = "" + originalByteArray[5] + originalByteArray[6];
        Calendar calendar = Calendar.getInstance();
        if (Integer.valueOf(b_year) + 2000 > calendar.get(Calendar.YEAR))
            b_year="19" + originalByteArray[5] + originalByteArray[6];
        else
            b_year="20" + originalByteArray[5] + originalByteArray[6];

        String birthday = bd + b_year;

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        try {
            Date birthDate = sdf.parse(birthday);
            Calendar birthdayDate = Calendar.getInstance();
            Calendar startDate = Calendar.getInstance();

            birthdayDate.setTime(birthDate);
            birthdayDate.add(Calendar.DAY_OF_MONTH, -1);
            startDate.setTime(start);

            int age = startDate.get(Calendar.YEAR) - birthdayDate.get(Calendar.YEAR);
            if (startDate.get(Calendar.DAY_OF_YEAR) <= birthdayDate.get(Calendar.DAY_OF_YEAR)) {
                age--;
            }
            if (age < 12) {
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return true;
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

    public String checkEduDocSeria(String docSeria, Integer docTypeID) throws DataValidationException {

        int [] seriaA = {1, 5, 9, 21, 25, 29, 45, 46, 51, 52};
        int [] seriaDI = {2,3, 4, 6, 7, 8, 10, 11, 12, 22, 23, 24, 26, 27, 28, 30, 31, 32, 41, 42, 43, 44, 47, 48, 49, 50, 53, 54 };
        int [] seriaNull = {17, 18, 19, 20, 37, 38, 39, 40};
        int [] seriaDD = {13, 14, 15, 16, 33, 34, 35, 36};
        boolean status = false;

        if ("А".equalsIgnoreCase(docSeria))
            for (int i : seriaA){
                if (docTypeID==i){
                    status=true;
                    break;
                }
            }

        if ("ДИ".equalsIgnoreCase(docSeria) || "ДИБ".equalsIgnoreCase(docSeria))
            for (int i : seriaDI){
                if (docTypeID==i){
                    status=true;
                    break;
                }
            }

        if ("ДД".equalsIgnoreCase(docSeria))
            for (int i : seriaDD){
                if (docTypeID==i){
                    status=true;
                    break;
                }
            }

        if (docSeria == null || docSeria.isEmpty() || "-".equalsIgnoreCase(docSeria))
            for (int i : seriaNull){
                if (docTypeID==i){
                    status=true;
                    docSeria="-";
                    break;
                }
            }

        if (status)
            return docSeria;
        else
            throw new DataValidationException("Ошибка проверки данных: проверьте серию или тип документа.");
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

    public Date checkEduDocIssueDate(Date docIssueDate, Date stopDate) throws DataValidationException {

        Calendar calendar = new GregorianCalendar();
        calendar.setTime(docIssueDate);
        int Month1 = calendar.get(Calendar.MONTH);
        int Year1 = calendar.get(Calendar.YEAR);
        calendar.setTime(stopDate);
        int Month2 = calendar.get(Calendar.MONTH);
        int Year2 = calendar.get(Calendar.YEAR);

        if (Month1<Month2)
            if (Year1<=Year2)
                if ((Month1+1)!=Month2)
                throw new DataValidationException("Ошибка проверки данных: дата выдачи документа меньше даты окончания обучения");
        if (Year1<Year2)
            throw new DataValidationException("Ошибка проверки данных: дата выдачи документа меньше даты окончания обучения");
        return docIssueDate;
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

    public Specialty checkSpecialty(String specialty, String code) throws DataValidationException {
        if (specialty == null || specialty.trim().isEmpty())
            throw new DataValidationException("Ошибка проверки данных: не указана специальность.");

        Specialty specialtyObj = appCache.getSpecialtiesByName(specialty.trim());

        if (specialtyObj == null)
            throw new DataValidationException("Ошибка проверки данных: некорректное название специальности.");

        if (code.length()>10){
            throw new DataValidationException("Ошибка проверки данных: проверьте код специальности. Количество знаков должно быть не более 10");
        }

        List<Specialty> specialtyList;
        specialtyList = repositoryService.getSpecialtyRepository().findByOKRBCodeByName(code, specialty);
        if (specialtyList.size() ==0)
            throw new DataValidationException("Ошибка проверки данных: проверьте код и название специальности согласно Справочнику ОКРБ 011-2009");



        return specialtyList.get(0);
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
