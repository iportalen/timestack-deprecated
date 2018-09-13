package com.iportalen.timestack.service.template.templates;

import java.util.Map;
import java.util.Optional;

public class Template {
	
	private String path;
	protected Map<String, String> dataModel;
	
	
	public Template(String path) {
		this.path = path;
	}

	public Template(String path, Map<String, String> dataModel) {
		this(path);
		this.dataModel = dataModel;
	}
	
	public String getPath() {
		return this.path;
	}
	
	public Optional<Map<String, String>> getDataModel() {
		return Optional.ofNullable(this.dataModel);
	}

}
