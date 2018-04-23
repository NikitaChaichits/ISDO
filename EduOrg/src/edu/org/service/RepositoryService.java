package edu.org.service;

import by.i4t.objects.EduDocsAppSettings;
import by.i4t.repository.*;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Ilya on 03.11.2016.
 */
@Getter
@Service
public class RepositoryService {
    @Autowired
    private VUZDocumentRepository vuzDocumentRepository;
    @Autowired
    private LogsRepository logsRepository;
    @Autowired
    private EduDocsAppSettingsRepository eduDocsAppSettingsRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GisunExportInfoRepository gisunExportInfoRepository;
    @Autowired
    private ExceptionInfoRepository exceptionInfoRepository;
    @Autowired
    private FileContentRepository fileContentRepository;
    @Autowired
    private ImportedFileRepository importedFileRepository;
    @Autowired
    private EduDocTypeRepository eduDocTypeRepository;
    @Autowired
    private EduOrganizationTypeRepository eduOrganizationTypeRepository;
    @Autowired
    private EduOrganizationRepository eduOrganizationRepository;
    @Autowired
    private SpecialtyGroupRepository specialtyGroupRepository;
    @Autowired
    private SpecialtyRepository specialtyRepository;
    @Autowired
    private SpecialtyDirectionRepository specialtyDirectionRepository;
    @Autowired
    private EduOrgOwnershipTypeRepository eduOrgOwnershipTypeRepository;
    @Autowired
    private CitizenRepository citizenRepository;
    @Autowired
    private EduLevelRepository eduLevelRepository;

    @Autowired
    private ExportServiceLogRepository exportServiceLogRepository;

    @Autowired
    private NotificationRepository notificationRepository;
}
