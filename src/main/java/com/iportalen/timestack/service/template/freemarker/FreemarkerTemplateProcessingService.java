package com.iportalen.timestack.service.template.freemarker;

import java.io.StringWriter;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iportalen.timestack.service.template.TemplateProcessingException;
import com.iportalen.timestack.service.template.TemplateProcessingService;

import freemarker.template.Configuration;
import freemarker.template.Template;

@Service
public class FreemarkerTemplateProcessingService implements TemplateProcessingService {
	
	@Autowired
	protected Configuration freemarkerConfiguration;

	public String process(com.iportalen.timestack.service.template.templates.Template template) {
		return process(template, Locale.ROOT);
	}
	
	public String process(com.iportalen.timestack.service.template.templates.Template template, Locale locale) {
		StringWriter writer = new StringWriter();
		try {
			Template freeTemp = freemarkerConfiguration.getTemplate(template.getPath(), locale);
			if(template.getDataModel().isPresent()) {
				freeTemp.process(template.getDataModel().get(), writer);
			} else {
				writer.write(freeTemp.toString());
			}
		} catch (Exception e) {
			throw new TemplateProcessingException("Could not create template!", e.getCause());
		}
		return writer.toString();
	}
	
}
