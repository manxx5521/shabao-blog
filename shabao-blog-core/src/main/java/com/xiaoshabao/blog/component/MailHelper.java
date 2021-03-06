package com.xiaoshabao.blog.component;

import java.util.Map;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.xiaoshabao.blog.lang.MtonsException;

import freemarker.template.Template;

/**
 */
@Component
public class MailHelper {
    private final Logger log = LoggerFactory.getLogger(this.getClass());  
    
    @Autowired
    private Environment env;
    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;
    @Autowired
    private JavaMailSender javaMailSender;

    public void sendEmail(String template, String to, String title, Map<String, Object> content) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {

            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setFrom(env.getProperty("spring.mail.username"));
            helper.setTo(to);

            helper.setSubject(title);
            helper.setText(render(template, content), true);
            javaMailSender.send(mimeMessage);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new  MtonsException("邮件发送失败", e);
        }
    }

    public String render(String templateName, Map<String, Object> model) {
        try {
            Template t = freeMarkerConfigurer.getConfiguration().getTemplate(templateName, "UTF-8");
            t.setOutputEncoding("UTF-8");
            return FreeMarkerTemplateUtils.processTemplateIntoString(t, model);
        } catch (Exception e) {
            throw new MtonsException(e.getMessage(), e);
        }
    }
}
