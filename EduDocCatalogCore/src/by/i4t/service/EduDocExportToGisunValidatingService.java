package by.i4t.service;

import by.i4t.objects.VUZDocument;
import by.i4t.repository.VUZDocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EduDocExportToGisunValidatingService {
    @Autowired
    private VUZDocumentRepository vuzDocumentRepository;

    public void execute() {
        byte[] ruByteArray = "АВЕЗКМНОРСТУХ".getBytes();
        byte[] enByteArray = "ABE3KMHOPCTYX".getBytes();
        Map<Byte, Byte> changeMap = new HashMap<>();
        for (int i = 0; i < ruByteArray.length; i++)
            changeMap.put(ruByteArray[i], enByteArray[i]);

        List<VUZDocument> docList = vuzDocumentRepository.findByStatusNotNull(new PageRequest(0, 300));
        while (!docList.isEmpty()) {
            for (VUZDocument doc : docList) {
                // -1 validated error
                // 0 new
                // 1 validated
                // 2 ready to gisun export
                // 3 exported to gisun

                if (doc.getCitizen().getIdNumber() != null && !doc.getCitizen().getIdNumber().contains(" "))
                    doc.setStatus(2);
                else
                    doc.setStatus(0);

                String idNumber = doc.getCitizen().getIdNumber();
                if (idNumber != null) {
                    byte[] newBytearray = new byte[idNumber.length()];
                    int i = 0;
                    for (byte bute : idNumber.getBytes()) {
                        if (changeMap.containsKey(bute))
                            newBytearray[i] = changeMap.get(bute);
                        else
                            newBytearray[i] = bute;

                        i++;
                    }
                    doc.getCitizen().setIdNumber(new String(newBytearray));
                }
            }
            vuzDocumentRepository.save(docList);
            docList = vuzDocumentRepository.findByStatusNotNull(new PageRequest(0, 300));
        }
    }
}
