package edu.org.validators;

import java.util.HashMap;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@ManagedBean
@RequestScoped
public class PersonalIDValidator implements Validator {

    private char[] ruCharArray = "АВЕЗКМНОРСТУХ".toCharArray();
    private char[] enCharArray = "ABE3KMHOPCTYX".toCharArray();
    private Map<Character, Character> changeMap = new HashMap<>();


    {
        for (int i = 0; i < ruCharArray.length - 1; i++)
            changeMap.put(ruCharArray[i], enCharArray[i]);
    }

    @Override
    public void validate(FacesContext context, UIComponent component,
                         Object value) throws ValidatorException {
        if (value == null || ((String) value).trim().isEmpty())
            throw new ValidatorException(new FacesMessage(
                    FacesMessage.SEVERITY_ERROR, "Не указан идентификационный номер", ""));
        String val = ((String) value).trim().toUpperCase();
        if (val.length() != 14)
            throw new ValidatorException(new FacesMessage(
                    FacesMessage.SEVERITY_ERROR, "Неверный формат идентификационного номера", ""));
        if (val.contains(" "))
            throw new ValidatorException(new FacesMessage(
                    FacesMessage.SEVERITY_ERROR, "Неверное значение идентификационного номера", ""));
        char[] newBytearray = new char[val.length()];
        char[] originalByteArray = val.toCharArray();

        for (int i = 0; i < 7; ++i)
            newBytearray[i] = checkDigital(originalByteArray[i], i + 1, component);
        newBytearray[7] = checkSymbol(originalByteArray[7], 8, component);
        newBytearray[8] = checkDigital(originalByteArray[8], 9, component);
        newBytearray[9] = checkDigital(originalByteArray[9], 10, component);
        newBytearray[10] = checkDigital(originalByteArray[10], 11, component);
        newBytearray[11] = checkSymbol(originalByteArray[11], 12, component);
        newBytearray[12] = checkSymbol(originalByteArray[12], 13, component);
        newBytearray[13] = checkDigital(originalByteArray[13], 14, component);
    }

    private char checkDigital(Character value, int i, UIComponent component) throws ValidatorException {
        if (Character.isDigit(value))
            return value;
        else {
            Character O_ru = 'О';
            Character O_en = 'O';
            if (value.equals(O_ru) || value.equals(O_en))
                return '0';
            else
                throw new ValidatorException(new FacesMessage(
                        FacesMessage.SEVERITY_ERROR, "Неверный символ №" + i + " (" + value + ") в значении идентификационного номера", ""));
        }
    }

    private char checkSymbol(char value, int i, UIComponent component) throws ValidatorException {
        if (value >= 'A' && value <= 'Z')
            return value;
        else {
            if (changeMap.containsKey(value))
                return changeMap.get(value);
            else
                throw new ValidatorException(new FacesMessage(
                        FacesMessage.SEVERITY_ERROR, "Неверный символ №" + i + " (" + value + ") в значении идентификационного номера", ""));
        }
    }
}
