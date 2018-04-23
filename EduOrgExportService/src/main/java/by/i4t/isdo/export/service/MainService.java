package by.i4t.isdo.export.service;

import by.i4t.objects.ExportServiceLog;
import by.i4t.repository.ExportServiceLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by roman on 28.11.2016.
 */
@Service
@Transactional
public class MainService {
    @Autowired
    private ExportServiceLogRepository exportServiceLogRepository;
    private boolean isOn = false;
    private Thread worker;

    @Scheduled(fixedDelay = 5 * 1000)
    public void statusCheck() throws Exception {
        List<ExportServiceLog> exportServiceLogs = exportServiceLogRepository.findAll();
        if (exportServiceLogs.size() != 1)
            throw new Exception("Ошибка получения статуса сервиса экспорта");

        ExportServiceLog serviceStatus = exportServiceLogs.get(0);
        boolean mustRun = serviceStatus.isOn();
        if (mustRun){
            if (isOn){
                serviceStatus.setLog("Работает");
            }else{
                worker = new Thread();
                worker.start();
                isOn = true;
                serviceStatus.setLog("Сервис экспорта запущен");
            }
        }
        else{
            if (isOn){
                if (worker != null)
                    worker.interrupt();
                isOn = false;
                serviceStatus.setLog("В процессе остановки");
            } else{
                serviceStatus.setLog("Режим ожидания");

            }
        }
        serviceStatus.setDate(new Date());
        System.out.println(serviceStatus.getLog());
        exportServiceLogRepository.save(serviceStatus);
    }

}
