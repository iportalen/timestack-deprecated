package com.iportalen.timestack.service.template;

import java.util.Locale;

public interface TemplateProcessingService {
	String process(com.iportalen.timestack.service.template.templates.Template template);
	String process(com.iportalen.timestack.service.template.templates.Template template, Locale locale);
}
