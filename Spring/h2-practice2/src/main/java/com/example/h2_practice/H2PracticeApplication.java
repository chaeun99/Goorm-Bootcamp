package com.example.h2_practice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class H2PracticeApplication {

	public static void main(String[] args) {
		SpringApplication.run(H2PracticeApplication.class, args);

        System.out.println("\n" +
                "===================================================\n" +
                "🚀 H2 Database Practice Application 시작 완료!\n" +
                "===================================================\n" +
                "📊 H2 Console: http://localhost:8080/h2-console\n" +
                "📋 Swagger UI: http://localhost:8080/swagger-ui.html\n" +
                "📖 API Docs: http://localhost:8080/v3/api-docs\n" +
                "===================================================\n");
	}

}
