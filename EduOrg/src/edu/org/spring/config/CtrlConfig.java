package edu.org.spring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import edu.org.controllers.EduDocsAdmCtrl;
import edu.org.controllers.EduDocsDataCtrl;
import edu.org.controllers.EduDocsMainCtrl;
import edu.org.controllers.EduDocsStatCtrl;
import edu.org.controllers.GisunExportCtrl;
import edu.org.controllers.HelpSectionCtrl;
import edu.org.models.EduDocsAdmViewModel;
import edu.org.models.EduDocsDataViewModel;
import edu.org.models.EduDocsMainViewModel;
import edu.org.models.EduDocsStatViewModel;
import edu.org.models.GisunExportViewModel;
import edu.org.models.HelpSectionViewModel;

@Configuration
public class CtrlConfig {
//	@Bean(name="eduDocsMainCtrl")
//	@Scope("session")
//	public EduDocsMainCtrl eduDocsMainCtrl() {
//		EduDocsMainCtrl ctrl = new EduDocsMainCtrl();
//		ctrl.setViewModel(new EduDocsMainViewModel());
//		return ctrl;
//	}

//	@Bean(name="eduDocsDataCtrl")
//	@Scope("session")
//	public EduDocsDataCtrl eduDocsDataCtrl() {
//		EduDocsDataCtrl ctrl = new EduDocsDataCtrl();
//		ctrl.setViewModel(new EduDocsDataViewModel());
//		return ctrl;
//	}

//	@Bean(name="eduDocsAdmCtrl")
//	@Scope("session")
//	public EduDocsAdmCtrl eduDocsAdmCtrl() {
//		EduDocsAdmCtrl ctrl = new EduDocsAdmCtrl();
//		ctrl.setViewModel(new EduDocsAdmViewModel());
//		return ctrl;
//	}
//	
//	@Bean(name="eduDocsStatCtrl")
//	@Scope("session")
//	public EduDocsStatCtrl eduDocsStatCtrl() {
//		EduDocsStatCtrl ctrl = new EduDocsStatCtrl();
//		ctrl.setViewModel(new EduDocsStatViewModel());
//		return ctrl;
//	}
//	
//	@Bean(name="gisunExportCtrl")
//	@Scope("session")
//	public GisunExportCtrl gisunExportCtrl() {
//		GisunExportCtrl ctrl = new GisunExportCtrl();
//		ctrl.setViewModel(new GisunExportViewModel());
//		return ctrl;
//	}
//	
//	@Bean(name="helpSectionCtrl")
//	@Scope("session")
//	public HelpSectionCtrl helpSectionCtrl() {
//		HelpSectionCtrl ctrl = new HelpSectionCtrl();
//		ctrl.setViewModel(new HelpSectionViewModel());
//		return ctrl;
//	}
}
