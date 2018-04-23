package edu.org.service;

import by.i4t.exceptions.BusinessConditionException;
import by.i4t.objects.EduDocType;
import edu.org.models.lineitems.SimpleIntValueLineItem;
import edu.org.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service("docTypeCatalogService")
public class DocTypeCatalogService {
    @Autowired
    private RepositoryService repositoryService;

    public List<SimpleIntValueLineItem> loadEduDocTypes() {
        List<SimpleIntValueLineItem> list = new ArrayList<SimpleIntValueLineItem>();
        for (EduDocType docType : repositoryService.getEduDocTypeRepository().findAll())
            list.add(new SimpleIntValueLineItem(docType.getName(), docType.getID()));

        Collections.sort(list, new Comparator<SimpleIntValueLineItem>() {
            @Override
            public int compare(SimpleIntValueLineItem obj1, SimpleIntValueLineItem obj2) {
                return obj1.getName().compareTo(obj2.getName());
            }
        });

        return list;
    }

    public EduDocType getEduDocType(Integer ID) {
        if (ID == null)
            return null;
        return repositoryService.getEduDocTypeRepository().findOne(ID);
    }

    public EduDocType saveEduDocType(Integer ID, String name) throws BusinessConditionException {
        if (StringUtil.isNullOrEmpty(name))
            throw new BusinessConditionException("Нулевое значение наименования типа документов!");

        EduDocType eduDocType = null;
        if (ID == null)
            eduDocType = new EduDocType();
        else
            eduDocType = repositoryService.getEduDocTypeRepository().findOne(ID);

        eduDocType.setName(name);
        repositoryService.getEduDocTypeRepository().save(eduDocType);
        return eduDocType;
    }
}
