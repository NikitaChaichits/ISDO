//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package edu.org.auth;

import by.i4t.helper.EduDocsAppLogSettings;
import by.i4t.helper.UserStatusEnum;
import by.i4t.log.EduDocsDBAppender;
import by.i4t.log.LogMessage;
import by.i4t.objects.User;
import edu.org.service.RepositoryService;
import java.io.IOException;
import java.security.cert.X509Certificate;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

@WebFilter(
        filterName = "AuthFilter",
        urlPatterns = {"*.xhtml"}
)
public class AuthFilter implements Filter {
    private static final boolean DEBUG = true;
    protected Logger log = LogManager.getLogger();
    private RepositoryService repositoryService;

    public AuthFilter() {
    }

    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("**** FILTER INIT ****");
        ServletContext servletContext = filterConfig.getServletContext();
        WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        AutowireCapableBeanFactory autowireCapableBeanFactory = webApplicationContext.getAutowireCapableBeanFactory();
        this.repositoryService = (RepositoryService)autowireCapableBeanFactory.getBean("repositoryService");
        LoggerContext ctx = (LoggerContext)LogManager.getContext(false);
        Configuration config = ctx.getConfiguration();
        EduDocsDBAppender appender = (EduDocsDBAppender)autowireCapableBeanFactory.getBean("dbAppender");
        appender.start();
        config.getLoggerConfig("edu.org.*").addAppender(appender, Level.ALL, (org.apache.logging.log4j.core.Filter)null);
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest)request;
        X509Certificate[] certs = (X509Certificate[])((X509Certificate[])req.getAttribute("javax.servlet.request.X509Certificate"));
        if (certs == null) {
            ;
        }

        String certificateID = null;
        certificateID = "40E505D4C0D01CFB00000134";
        if (certificateID != null && req.getSession() != null && req.getSession().getAttribute("auth_result") == null) {
            User user = this.repositoryService.getUserRepository().findByCertificatId(certificateID);
            if (user != null && !UserStatusEnum.BLOCKED.getCode().equals(user.getStatus())) {
                req.getSession().setAttribute("userInfo", user);
                req.getSession().setAttribute("auth_result", "OK");
                this.log.info(new LogMessage(user, EduDocsAppLogSettings.LOGIN_USER_ACTION_LOG, "Аутентификация пользователя " + user.getName() + " (" + user.getID() + ") успешна"));
            } else if (user == null) {
                req.getSession().setAttribute("auth_result", "ERROR");
                this.log.info(new LogMessage(user, EduDocsAppLogSettings.LOGIN_USER_ACTION_LOG, "Учётная запись не найдена"));
            } else if (UserStatusEnum.BLOCKED.getCode().equals(user.getStatus())) {
                req.getSession().setAttribute("auth_result", "ERROR");
                this.log.info(new LogMessage(user, EduDocsAppLogSettings.LOGIN_USER_ACTION_LOG, "Учётная запись заблокирована"));
            }
        }

        chain.doFilter(request, response);
    }

    private void findUser() {
    }

    public void destroy() {
        System.out.println("**** FILTER DESTROY ****");
    }
}
