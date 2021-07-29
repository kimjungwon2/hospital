package site.hospital;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import java.util.Optional;
import java.util.UUID;

@EnableJpaAuditing
@SpringBootApplication
public class HospitalApplication {


	public static void main(String[] args) {
		SpringApplication.run(HospitalApplication.class, args);
	}

	//작성자 확인
	@Bean
	public Optional<String> auditorProvider() {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		//인증되지 않으면 null을 반환.
		if (authentication  == null || !authentication.isAuthenticated()) {
			return null;
		}

		User user = (User) authentication.getPrincipal();
		return Optional.of(user.getUsername());
	}

}
