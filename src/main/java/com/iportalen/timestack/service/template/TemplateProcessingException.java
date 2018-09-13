package com.iportalen.timestack.service.template;

public class TemplateProcessingException extends RuntimeException {
	private static final long serialVersionUID = 8948885477254459959L;

	public TemplateProcessingException(String message) {
		super(message);
	}
	
	public TemplateProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
