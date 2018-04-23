package by.i4t.parser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import by.i4t.exceptions.ImportDataException;
import by.i4t.objects.bo.VUZEduDocLineItem;
import by.i4t.objects.bo.VUZEduDocsList;

public class VuzDocXMLParser {
    public List<VUZEduDocLineItem> execute(InputStream inputStream) throws ImportDataException {
        List<VUZEduDocLineItem> list = new ArrayList<VUZEduDocLineItem>();
        try {
            VUZEduDocsList vuzEduDocsList = (VUZEduDocsList) JAXBContext.newInstance(VUZEduDocsList.class).createUnmarshaller().unmarshal(inputStream);
            if (vuzEduDocsList != null)
                list.addAll(vuzEduDocsList.getVUZEduDocItems());
        } catch (JAXBException e) {
            throw new ImportDataException("Import data about high education from XML file failed!", e);
        }

        return list;
    }
}
