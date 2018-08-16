package com.iportalen.timestack.config;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.iportalen.timestack.conditionals.FirebaseCondition;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
@FirebaseCondition
public class FirebaseConfig {

	@Bean
	public DatabaseReference firebaseDatabase() {
		return FirebaseDatabase.getInstance().getReference();
	}

	@Value("${firebase.database.url}")
	private String databaseUrl;

	@Value("classpath:${firebase.config.path}")
	private Resource config;
	
	@PostConstruct
	public void init() throws IOException {
		if (!FirebaseApp.getApps().isEmpty()) // 
	        return;
		
		/**
		 * https://firebase.google.com/docs/admin/setup
		 * 
		 * Create service account , download json
		 */
		
		FirebaseOptions options = new FirebaseOptions.Builder()
		    .setCredentials(GoogleCredentials.fromStream(config.getInputStream()))
		    .setDatabaseUrl(databaseUrl)
		    .build();

		FirebaseApp.initializeApp(options);
		
		log.info("Initialized Firebase");
		
	}
}