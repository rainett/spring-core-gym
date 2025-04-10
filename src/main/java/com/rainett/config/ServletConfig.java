package com.rainett.config;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class ServletConfig extends AbstractAnnotationConfigDispatcherServletInitializer {
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[] {AppConfig.class};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[0];
    }

    @Override
    protected String[] getServletMappings() {
        return new String[] {"/"};
    }

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        super.onStartup(servletContext);
        setUpFilters(servletContext);
    }

    private void setUpFilters(ServletContext servletContext) {
        String[] filterNames = {"transactionLoggingFilter", "authenticationFilter"};
        for (String filterName : filterNames) {
            addFilter(servletContext, filterName);
        }
    }

    private static void addFilter(ServletContext servletContext, String filterName) {
        DelegatingFilterProxy proxy = new DelegatingFilterProxy(filterName);
        servletContext.addFilter(filterName, proxy)
                .addMappingForUrlPatterns(null, false, "/*");
    }
}
