package edu.org.service;

import by.i4t.exceptions.ImportDataException;
import by.i4t.objects.ExceptionInfo;
import by.i4t.objects.ImportedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by SM on 07.01.2017.
 */
@Service("docsParsingService")
public class DocsParsingService {
    @Autowired
    RepositoryService repositoryService;
    @Autowired
    private VUZDocParsingService vuzDocParsingService;
    private AtomicBoolean running = new AtomicBoolean(false);

    public DocsParsingService() {
    }

    @Scheduled(fixedRate = 1000 * 10,  initialDelay = 1000)//run every 1 minutes
    public void start() {
        if (running.get()) {
//            System.out.println("Предыдущая операция импорта не завершена");
            return;
        }
        running.set(true);
//        System.out.println("Старт импорта");
        importFilesParsing();
//        System.out.println("Импорт завершен");
        running.set(false);
    }

    private void importFilesParsing() {
        for (ImportedFile importedFile : repositoryService.getImportedFileRepository().findByStatus(0)) {
            try {
                vuzDocParsingService.execute(importedFile);
            } catch (Exception e) {
                e.printStackTrace();
                importedFile.setStatus(2);
                importedFile.setExceptionInfo(new ExceptionInfo(e));
                repositoryService.getImportedFileRepository().save(importedFile);
            }
        }
    }
}
