package by.i4t.parser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import by.i4t.exceptions.ImportDataException;
import by.i4t.objects.bo.VUZEduDocLineItem;

public class VuzDocExcelParser {
    public List<VUZEduDocLineItem> execute(ByteArrayInputStream inputStream) throws ImportDataException {
        List<VUZEduDocLineItem> list = new ArrayList<VUZEduDocLineItem>();

        try {
            Workbook wb = WorkbookFactory.create(inputStream);

            Sheet sheet = wb.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();
            Integer stringCount = 0;
            while (rowIterator.hasNext()) {
                stringCount++;

                VUZEduDocLineItem item = new VUZEduDocLineItem();
                Row row = rowIterator.next();

                if (row.getCell(0) != null) {
                    if (Cell.CELL_TYPE_STRING == row.getCell(0).getCellType() || Cell.CELL_TYPE_BLANK == row.getCell(0).getCellType())
                        item.setSecondName(row.getCell(0).getStringCellValue());
                    else
                        throw new ImportDataException("Import data about high education from file failed! String:" + stringCount + " , column 0 - invalid data format.");
                }

                if (row.getCell(1) != null) {
                    if (Cell.CELL_TYPE_STRING == row.getCell(1).getCellType() || Cell.CELL_TYPE_BLANK == row.getCell(1).getCellType())
                        item.setFirstName(row.getCell(1).getStringCellValue());
                    else
                        throw new ImportDataException("Import data about high education from file failed! String:" + stringCount + " , column 1 - invalid data format.");
                }

                if (row.getCell(2) != null) {
                    if (Cell.CELL_TYPE_STRING == row.getCell(2).getCellType() || Cell.CELL_TYPE_BLANK == row.getCell(2).getCellType())
                        item.setPatronymic(row.getCell(2).getStringCellValue());
                    else
                        throw new ImportDataException("Import data about high education from file failed! String:" + stringCount + " , column 2 - invalid data format.");
                }

                if (row.getCell(3) != null) {
                    if (Cell.CELL_TYPE_STRING == row.getCell(3).getCellType() || Cell.CELL_TYPE_BLANK == row.getCell(3).getCellType())
                        item.setPersonalID(row.getCell(3).getStringCellValue());
                    else
                        throw new ImportDataException("Import data about high education from file failed! String:" + stringCount + " , column 3 - invalid data format.");
                }

                if (row.getCell(4) != null) {
                    if (Cell.CELL_TYPE_STRING == row.getCell(4).getCellType() || Cell.CELL_TYPE_BLANK == row.getCell(4).getCellType())
                        item.setEducationLevel(row.getCell(4).getStringCellValue());
                    else
                        throw new ImportDataException("Import data about high education from file failed! String:" + stringCount + " , column 4 - invalid data format.");
                }

                if (row.getCell(5) != null) {
                    if (Cell.CELL_TYPE_STRING == row.getCell(5).getCellType() || Cell.CELL_TYPE_BLANK == row.getCell(5).getCellType())
                        item.setEduOrg(row.getCell(5).getStringCellValue());
                    else
                        throw new ImportDataException("Import data about high education from file failed! String:" + stringCount + " , column 5 - invalid data format.");
                }

                if (row.getCell(6) != null && Cell.CELL_TYPE_BLANK != row.getCell(6).getCellType()) {
                    if (Cell.CELL_TYPE_STRING == row.getCell(6).getCellType())
                        item.setEduStartDate(new SimpleDateFormat("dd.MM.yyyy").parse(row.getCell(6).getStringCellValue()));
                    else
                        item.setEduStartDate(row.getCell(6).getDateCellValue());
                }

                if (row.getCell(7) != null && Cell.CELL_TYPE_BLANK != row.getCell(7).getCellType()) {
                    if (Cell.CELL_TYPE_STRING == row.getCell(7).getCellType())
                        item.setEduStopDate(new SimpleDateFormat("dd.MM.yyyy").parse(row.getCell(7).getStringCellValue()));
                    else
                        item.setEduStopDate(row.getCell(7).getDateCellValue());
                }

                if (row.getCell(8) != null) {
                    if (Cell.CELL_TYPE_STRING == row.getCell(8).getCellType() || Cell.CELL_TYPE_BLANK == row.getCell(8).getCellType())
                        item.setDocType(row.getCell(8).getStringCellValue());
                    else
                        throw new ImportDataException("Import data about high education from file failed! String:" + stringCount + " , column 8 - invalid data format.");
                }

                if (row.getCell(9) != null) {
                    if (Cell.CELL_TYPE_STRING == row.getCell(9).getCellType() || Cell.CELL_TYPE_BLANK == row.getCell(9).getCellType())
                        item.setEduDocSeria(row.getCell(9).getStringCellValue());
                    else
                        throw new ImportDataException("Import data about high education from file failed! String:" + stringCount + " , column 9 - invalid data format.");
                }

                if (row.getCell(10) != null) {
                    if (row.getCell(10).getCellType() == Cell.CELL_TYPE_NUMERIC) {
                        item.setEduDocNumber(String.valueOf(row.getCell(10).getNumericCellValue()).replaceAll("\\.0", ""));
                        // item.setEduDocNumberBad(String.valueOf(row.getCell(10).getNumericCellValue()).replaceAll(".0",
                        // ""));
                    } else
                        item.setEduDocNumber(row.getCell(10).getStringCellValue());
                }

                if (row.getCell(11) != null) {
                    if (row.getCell(11).getCellType() == Cell.CELL_TYPE_NUMERIC) {
                        item.setEduDocRegNumber(String.valueOf(row.getCell(11).getNumericCellValue()).replaceAll("\\.0", ""));
                        // item.setEduDocRegNumberBad(String.valueOf(row.getCell(11).getNumericCellValue()).replaceAll(".0",
                        // ""));
                    } else
                        item.setEduDocRegNumber(row.getCell(11).getStringCellValue());
                }

                if (row.getCell(12) != null && Cell.CELL_TYPE_BLANK != row.getCell(12).getCellType()) {
                    if (Cell.CELL_TYPE_STRING == row.getCell(12).getCellType())
                        item.setEduDocIssueDate(row.getCell(12).getStringCellValue());
                    else
                        item.setEduDocIssueDate(new SimpleDateFormat("dd.MM.yyyy").format(row.getCell(12).getDateCellValue()));
                }

                if (row.getCell(13) != null) {
                    if (Cell.CELL_TYPE_STRING == row.getCell(13).getCellType() || Cell.CELL_TYPE_BLANK == row.getCell(13).getCellType())
                        item.setSpecialty(row.getCell(13).getStringCellValue());
                    else
                        throw new ImportDataException("Import data about high education from file failed! String:" + stringCount + " , column 13 - invalid data format.");
                }

                if (row.getCell(14) != null) {
                    if (Cell.CELL_TYPE_STRING == row.getCell(14).getCellType() || Cell.CELL_TYPE_BLANK == row.getCell(14).getCellType())
                        item.setSpecialization(row.getCell(14).getStringCellValue());
                    else
                        throw new ImportDataException("Import data about high education from file failed! String:" + stringCount + " , column 14 - invalid data format.");
                }

                if (row.getCell(15) != null) {
                    if (Cell.CELL_TYPE_STRING == row.getCell(15).getCellType() || Cell.CELL_TYPE_BLANK == row.getCell(15).getCellType())
                        item.setQualification(row.getCell(15).getStringCellValue());
                    else
                        throw new ImportDataException("Import data about high education from file failed! String:" + stringCount + " , column 15 - invalid data format.");
                }

                if (row.getCell(16) != null) {
                    if (Cell.CELL_TYPE_STRING == row.getCell(16).getCellType() || Cell.CELL_TYPE_BLANK == row.getCell(16).getCellType())
                        item.setMemberOfBel(row.getCell(16).getStringCellValue());
                    else
                        throw new ImportDataException("Import data about high education from file failed! String:" + stringCount + " , column 16 - invalid data format.");
                }

                list.add(item);
            }
        } catch (InvalidFormatException e) {
            e.printStackTrace();
            throw new ImportDataException("Import data about high education from file failed!", e);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ImportDataException("Import data about high education from file failed!", e);
        } catch (ParseException e) {
            e.printStackTrace();
            throw new ImportDataException("Import data about high education from file failed: date format is bad.", e);
        }

        return list;
    }
}
