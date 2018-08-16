package com.iportalen.timestack.service.sms.linkmobility;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonTypeName(value = "message")
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
public class LinkmobilityMessage implements Serializable {
	private static final long serialVersionUID = 7562194937150489753L;

	private String recipients;
	private String sender;
	private String message;
	
	public static class LinkmobilityMessageBuilder {
        public LinkmobilityMessageBuilder recipients(String[] recipients) {
            this.recipients = StringUtils.join(recipients, ",");
            return this;
        }
        
    }

}

