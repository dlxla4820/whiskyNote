package develop.whiskyNote;

import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@SpringBootTest
@ActiveProfiles("local")
@TestPropertySource(properties = "jasypt.encryptor.password=pistachio")
class WiskyNoteApplicationTests {

//	@Test
//	void contextLoads() {
//	}
//	private PooledPBEStringEncryptor encryptor;
//	@Value("${jasypt.encryptor.password}")
//	public String password;
//	@Test
//	@DisplayName("encrypt")
//	void encrypt(){
//		String targetText = "https://13.125.39.61:3306/";
//
//
//		String encryptedText = encryptor.encrypt(targetText);
//		System.out.println(encryptedText);
//
//		String decryptedText = encryptor.decrypt(encryptedText);
//		System.out.println(decryptedText);
//
//		Assertions.assertEquals(targetText, decryptedText);
//
//
//	}
}
