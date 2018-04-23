package edu.org.service;

import by.i4t.exceptions.BusinessConditionException;
import by.i4t.objects.EduLevel;
import by.i4t.objects.Specialty;
import by.i4t.objects.SpecialtyDirection;
import by.i4t.objects.SpecialtyGroup;
import edu.org.models.lineitems.SimpleStringValueLineItem;
import edu.org.utils.StringUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

@Service("specialtyCatalogService")
public class SpecialtyCatalogService {
    @Autowired
    RepositoryService repositoryService;

    public SpecialtyGroup getSpecialtyGroup(String ID) {
        if (ID == null || ID.isEmpty())
            return null;

        return repositoryService.getSpecialtyGroupRepository().findOne(UUID.fromString(ID));
    }

    public Specialty getSpecialty(String ID) {
        if (ID == null || ID.isEmpty())
            return null;

        return repositoryService.getSpecialtyRepository().findOne(UUID.fromString(ID));
    }

    public SpecialtyDirection saveSpecialtyDirection(String code, String name, String eduLevelID) throws BusinessConditionException {
        if (StringUtil.isNullOrEmpty(eduLevelID))
            throw new BusinessConditionException("Не выбран уровень образования!");
        if (StringUtil.isNullOrEmpty(code))
            throw new BusinessConditionException("Нулевое значение кода группы специальностей!");
        if (StringUtil.isNullOrEmpty(name))
            throw new BusinessConditionException("Нулевое значение наименования группы специальностей!");

        EduLevel eduLevel = repositoryService.getEduLevelRepository().findOne(Integer.parseInt(eduLevelID));
        if (eduLevel == null)
            throw new BusinessConditionException("Уровень образования не найден!");
        SpecialtyDirection specialtyDirection = new SpecialtyDirection();
        specialtyDirection.setCode(code);
        specialtyDirection.setName(name);
        specialtyDirection.setEduLevel(eduLevel);
        repositoryService.getSpecialtyDirectionRepository().save(specialtyDirection);
        return specialtyDirection;
    }

    public SpecialtyGroup saveSpecialtyGroup(String ID, String specialtyDirectionID, String code, String name) throws BusinessConditionException {
        if (StringUtil.isNullOrEmpty(specialtyDirectionID))
            throw new BusinessConditionException("Не выбрано направление специальностей!");
        if (StringUtil.isNullOrEmpty(code))
            throw new BusinessConditionException("Нулевое значение кода группы специальностей!");
        if (StringUtil.isNullOrEmpty(name))
            throw new BusinessConditionException("Нулевое значение наименования группы специальностей!");

        SpecialtyDirection specialtyDirection = repositoryService.getSpecialtyDirectionRepository().findOne(UUID.fromString(specialtyDirectionID));
        if (specialtyDirection == null)
            throw new BusinessConditionException("Направление специальностей не найдено!");

        SpecialtyGroup specialtyGroup = null;
        if (ID == null || ID.isEmpty())
            specialtyGroup = new SpecialtyGroup();
        else
            specialtyGroup = repositoryService.getSpecialtyGroupRepository().findOne(UUID.fromString(ID));

        specialtyGroup.setCode(code);
        specialtyGroup.setName(name);
        specialtyGroup.setDirection(specialtyDirection);
        repositoryService.getSpecialtyGroupRepository().save(specialtyGroup);

        return specialtyGroup;
    }

    public Specialty saveSpecialty(String ID, SimpleStringValueLineItem specialtyGroupLI, String code, String name)
            throws BusinessConditionException {
        if (specialtyGroupLI == null)
            throw new BusinessConditionException("Не выбрана группа специальностей!");
        if (StringUtil.isNullOrEmpty(code))
            throw new BusinessConditionException("Нулевое значение кода группы специальностей!");
        if (StringUtil.isNullOrEmpty(name))
            throw new BusinessConditionException("Нулевое значение наименования группы специальностей!");

        SpecialtyGroup specialtyGroup = repositoryService.getSpecialtyGroupRepository().findOne(UUID.fromString(specialtyGroupLI.getValue()));
        if (specialtyGroup == null)
            throw new BusinessConditionException("Группа специальностей не найдена!");

        Specialty specialty = null;
        if (ID == null || ID.isEmpty()) {
            specialty = new Specialty();
            Integer maxCode = repositoryService.getSpecialtyRepository().getMaxCode();
            specialty.setCode(maxCode + 1);
        } else
            specialty = repositoryService.getSpecialtyRepository().findOne(UUID.fromString(ID));
        specialty.setGroup(specialtyGroup);
        specialty.setOKRBCode(code);
        specialty.setName(name);

        repositoryService.getSpecialtyRepository().save(specialty);

        return specialty;
    }

    public ByteArrayInputStream buildUploadFile() {
        Map<Integer, String> dataMap = new HashMap<Integer, String>();
        for (Specialty specialty : repositoryService.getSpecialtyRepository().findAll())
            if (!dataMap.containsKey(specialty.getCode()))
                dataMap.put(specialty.getCode(), specialty.getName());

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet();

        int idx = 1;
        for (Entry<Integer, String> entry : dataMap.entrySet()) {
            Row row = sheet.createRow(idx);
            try {
                row.createCell(0).setCellValue(entry.getKey());
                row.createCell(1).setCellValue(entry.getValue());
                idx++;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            workbook.write(baos);
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ByteArrayInputStream(baos.toByteArray());
    }
}
