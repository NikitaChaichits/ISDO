package by.i4t.parser;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.poi.POIXMLDocument;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import by.i4t.exceptions.ImportDataException;
import by.i4t.objects.bo.VUZEduDocLineItem;

public class VuzDocImportFileParser {
    public List<VUZEduDocLineItem> execute(byte[] content) throws ImportDataException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(content);
        if (isExcelFile(inputStream))
            return new VuzDocExcelParser().execute(inputStream);
        else if (isXMLFile(new ByteArrayInputStream(content)))
            return new VuzDocXMLParser().execute(new ByteArrayInputStream(content));
        else
            throw new ImportDataException("Import data from file error: imported file has not valid format (only XML, XLS or XLSX format is supported)!");
    }

    private Boolean isExcelFile(InputStream inputStream) throws ImportDataException {
        Boolean result = true;
        try {
            result = POIXMLDocument.hasOOXMLHeader(inputStream) || POIFSFileSystem.hasPOIFSHeader(inputStream);
        } catch (IOException e) {
            throw new ImportDataException("Import data about high education from file failed!", e);
        }
        return result;
    }

    private Boolean isXMLFile(InputStream inputStream) throws ImportDataException {
        Boolean result = true;
        try {
            // TODO: replace by XSD schema check
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            String str = br.readLine();
            if (str == null || !str.startsWith("<?xml version"))
                result = false;
        } catch (IOException e) {
            throw new ImportDataException("Import data about high education from file failed!", e);
        }
        return result;
    }
}
