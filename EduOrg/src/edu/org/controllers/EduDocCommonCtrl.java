package edu.org.controllers;

import edu.org.auth.SecurityManager;
import edu.org.service.ApplicationCache;
import edu.org.service.RepositoryService;
import edu.org.spring.jsfbridge.ISDOServiceFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import java.lang.reflect.ParameterizedType;

public class EduDocCommonCtrl<ViewModel> {
    protected Logger log = LogManager.getLogger();
    @ManagedProperty(value = "#{serviceFactory}")
    private ISDOServiceFactory serviceFactory;
    private ViewModel viewModel;
    //	private DAO dao;
    private ApplicationCache appCache;
    private RepositoryService repositoryService;


    @PostConstruct
    public void init() {
//		setDao((DAO) getServiceFactory().getService("dao"));
        setAppCache((ApplicationCache) getServiceFactory().getService("appCache"));
        repositoryService = (RepositoryService) getServiceFactory().getService("repositoryService");
        try {
            setViewModel(((Class<ViewModel>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0]).newInstance());
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    //	public DAO getDao() {
//		return dao;
//	}
//	public void setDao(DAO dao) {
//		this.dao = dao;
//	}
    public ViewModel getViewModel() {
        return viewModel;
    }

    public void setViewModel(ViewModel viewModel) {
        this.viewModel = viewModel;
    }

    public ISDOServiceFactory getServiceFactory() {
        return serviceFactory;
    }

    public void setServiceFactory(ISDOServiceFactory serviceFactory) {
        this.serviceFactory = serviceFactory;
    }

    public ApplicationCache getAppCache() {
        return appCache;
    }

    public void setAppCache(ApplicationCache appCache) {
        this.appCache = appCache;
    }

    public RepositoryService getRepositoryService() {
        return repositoryService;
    }

    protected void showErrorMsg(Exception ex) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Системная ошибка.", ex.getLocalizedMessage()));
        ex.printStackTrace();
    }

    public Boolean isAdmin() {
        return SecurityManager.isAdmin();
    }
}
