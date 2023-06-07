package org.swmaestro.kauth.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Spring Bean Container에서 Kauth에 필요한 Bean을 찾아 제공
 * @author ChangEn Yea
 */
@Component
public class KauthBeansProvider implements ApplicationContextAware {

    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    public static JwtUtil getJwtUtil() throws BeansException{
        return context.getBean(JwtUtil.class);
    }

    public static UserDetailsService getUserDetailsService() {
        return context.getBean(UserDetailsService.class);
    }

    public static ObjectMapper getObjectMapper() {
        return context.getBean(ObjectMapper.class);
    }

    public static PasswordEncoder getPasswordEncoder() {
        return context.getBean(PasswordEncoder.class);
    }
}