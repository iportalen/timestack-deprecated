package com.iportalen.timestack.service.sms.linkmobility;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.iportalen.timestack.service.sms.SMS;
import com.iportalen.timestack.service.sms.SmsService;

import freemarker.template.Configuration;
import lombok.extern.slf4j.Slf4j;

@Service
@Profile("prod")
@Slf4j
public class LinkmobilitySmsService implements SmsService {

	private final String sender = "ProjectX - Verification code"; 
	private final String apiKey;
	private final String sendUri;
	
	private BlockingQueue<LinkmobilityMessage> messageQueue;
	private ExecutorService messageSendExecutor;
	
	@Autowired
	protected Configuration freemarkerConfiguration;

	public LinkmobilitySmsService(@Value("${sms.gateway.linkmobility.apikey}") String apiKey, @Value("${sms.gateway.linkmobility.uri.send}") String sendUri) {
		this.apiKey = apiKey;
		this.sendUri = sendUri;
		this.messageQueue = new ArrayBlockingQueue<LinkmobilityMessage>(1000, true);
		start();
	}
	
	@Override
	public void sendMessage(SMS message) {
		LinkmobilityMessage linkmobilityMessage = createLinkmobilityMessage(message);
		this.messageQueue.add(linkmobilityMessage);
	}
	
	private LinkmobilityMessage createLinkmobilityMessage(SMS message) {
		return LinkmobilityMessage.builder()
				.sender(this.sender)
				.recipients(message.getRecipients())
				.message(message.getText())
				.build();
	}
	
	@Override
	public void stop() {
		this.messageSendExecutor.shutdown();
		log.info("Stopped LinkmobilitySmsService");
	}
	
	@Override
	public void start() {
		this.messageSendExecutor = Executors.newSingleThreadExecutor();
		addMessageSenderTask();
		log.info("Started LinkmobilitySmsService");
	}
	
	private void addMessageSenderTask() {
		Runnable takeStringFromQueueAndPrint = () -> {
			while (true) {
				try {
					LinkmobilityMessage takenFromQueue = messageQueue.take(); // Blocks thread until a message is available
					send(takenFromQueue);
				} catch (InterruptedException e) {
					log.error("Could not take message from queue", e);
				}
			}
		};
		messageSendExecutor.submit(takeStringFromQueueAndPrint);
	}
	
	private void send(LinkmobilityMessage message) {
		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(this.sendUri).queryParam("apikey", this.apiKey);
		
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response = restTemplate.postForEntity(uriBuilder.toUriString(), message, String.class);
		if(response.getStatusCode().equals(HttpStatus.OK)) {
			log.info("Sent SMS to " + StringUtils.join(message.getRecipients(), ","));
		} else {
			log.info("Failed to send SMS to " + StringUtils.join(message.getRecipients(), ","));
		}
	}

}
