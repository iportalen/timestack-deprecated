package com.iportalen.timestack.service.sms.linkmobility;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.iportalen.timestack.service.sms.SMS;
import com.iportalen.timestack.service.sms.SmsService;

import freemarker.template.Configuration;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LinkmobilitySmsService implements SmsService {
	private String sender;
	private String apiKey;
	private String sendUri;

	private BlockingQueue<LinkmobilityMessage> messageQueue;
	private ExecutorService messageSendExecutor;
	
	public LinkmobilitySmsService(String apiKey, String sendUri, String sender) {
		this.sender = sender;
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
		try {
			ResponseEntity<String> response = restTemplate.postForEntity(uriBuilder.toUriString(), message, String.class);
			if (!response.getStatusCode().equals(HttpStatus.CREATED))
				throw new Exception();

			log.info("Sent SMS to " + String.join(",", message.getRecipients()));
		} catch (Exception e) {
			log.info("Failed to send SMS to " + String.join(",", message.getRecipients()), e.getMessage());
		}
	}
	
	public String getSender() {
		return this.sender;
	}

}
