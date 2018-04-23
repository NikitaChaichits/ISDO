package edu.org.spring.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfig {
//    @Autowired
//    private DAOProvider daoProvider;
//    @Autowired
//    private RepositoryService repositoryService;

//    @Bean(name = "daoProvider")
//    public DAOProvider daoProvider() {
//        return new DAOProvider();
//    }
//
//    @Bean(name = "dao")
//    @Scope("session")
//    public DAO dao() {
//        return daoProvider.getDAO();
//    }

//    @Bean(name = "appCache")
//    public ApplicationCache appCache() {
//        return new ApplicationCache(daoProvider.getDAO());
//    }

//    @Bean(name = "vuzDocTransformer")
//    @Scope("session")
//    public VUZDocumentTransformer vuzDocTransformer() {
//        return new VUZDocumentTransformer();
//    }

//    @Bean(name = "vuzEduDocValidator")
//    @Scope("session")
//    public VUZEduDocValidator vuzEduDocValidator() {
//        return new VUZEduDocValidator();
//    }

//    @Bean(name = "citizenService")
//    @Scope("session")
//    public CitizenService citizenService() {
//        return new CitizenService();
//    }

//    @Bean(name = "repositoryService")
//    public RepositoryService repositoryService() {
//        return new RepositoryService();
//    }

//    @Bean(name = "dbAppender")
//    public EduDocsDBAppender dbAppender() {
//        return EduDocsDBAppender.createAppender(
//                "EduDocsDBAppender", null, null, null,
//                repositoryService.getEduDocsAppSettingsRepository(),
//                repositoryService.getLogsRepository());
//    }
}
