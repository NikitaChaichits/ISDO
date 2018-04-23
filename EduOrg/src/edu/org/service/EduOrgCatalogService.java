package edu.org.service;

import by.i4t.objects.EduOrganization;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service("eduOrgCatalogService")
public class EduOrgCatalogService {
    public ByteArrayInputStream buildUploadFile(List<EduOrganization> eduOrgList) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet();

        for (EduOrganization eduOrg : eduOrgList) {
            Row row = sheet.createRow(eduOrgList.indexOf(eduOrg));
            try {
                row.createCell(0).setCellValue(eduOrg.getCode() + "-" + eduOrg.getNameVersion());
                row.createCell(1).setCellValue(eduOrg.getName());
                row.createCell(2).setCellValue(eduOrg.getNameInBel());
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
