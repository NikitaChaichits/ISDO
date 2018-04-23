package edu.org.service;

import by.i4t.objects.VUZDocument;
import by.i4t.repository.VUZDocumentRepository;
import by.i4t.repository.search.SearchPageRequest;
import by.i4t.repository.search.SearchSpecificationBuilder;
import edu.org.controllers.EduDocsDataCtrl;
import edu.org.models.lineitems.EduDocLineItem;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EduDocDataService extends LazyDataModel<EduDocLineItem> {

    private static final long serialVersionUID = -3081472502812660757L;
    private EduDocsDataCtrl ctrl;

    private VUZDocumentRepository documentRepository;

    public EduDocDataService(EduDocsDataCtrl ctrl) {
        this.ctrl = ctrl;
        documentRepository = ctrl.getRepositoryService().getVuzDocumentRepository();
    }

    @Override
    public List<EduDocLineItem> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        List<EduDocLineItem> list = new ArrayList<>();
        Page<VUZDocument> result = documentRepository.findAll(
                new SearchSpecificationBuilder<VUZDocument>(ctrl.getSearchFilter()).build(),
                prepareRequest(first / pageSize, pageSize, sortField, sortOrder)
        );
        list.addAll(result.getContent().stream().map(EduDocLineItem::new).collect(Collectors.toList()));
        this.setRowCount((int) result.getTotalElements());
        return list;
    }

    @SuppressWarnings("unchecked")
    @Override
    public EduDocLineItem getRowData(String rowKey) {
        List<EduDocLineItem> rows = (ArrayList<EduDocLineItem>) getWrappedData();
        for (EduDocLineItem row : rows) {
            if (row.getDocumentID().equals(rowKey))
                return row;
        }
        return null;
    }

    @Override
    public Object getRowKey(EduDocLineItem row) {
        return row.getDocumentID();
    }

    public SearchPageRequest prepareRequest(int first, int pageSize, String sortField, SortOrder sortOrder) {
        String translated = null;
        if (sortField != null)
            switch (sortField) {
                case "name":
                    translated = "citizen.secondName";
                    break;
                case "idNumber":
                    translated = "citizen.idNumber";
                    break;
                case "eduOrgName":
                    translated = "eduOrganization.name";
                    break;
                case "eduStartDate":
                case "eduStopDate":
                case "docType":
                case "docRegNumber":
                case "docIssueDate":
                    translated = sortField;
                    break;
                case "docSeriaNumber":
                    translated = "docNumber";
                    break;
                case "specialty":
                    translated = "specialty.name";
                    break;
                case "specialization":
                    translated = "specializationTXT";
                    break;
                default:
                    return null;
            }
        else
            translated = "citizen.idNumber";
        Sort.Direction direction = null;
        switch (sortOrder) {
            case ASCENDING:
                direction = Sort.Direction.ASC;
                break;
            case DESCENDING:
                direction = Sort.Direction.DESC;
                break;
        }
        ctrl.setSortField(translated);
        ctrl.setSortOrder(direction);
        return new SearchPageRequest(first, pageSize, direction, translated);
    }

}
