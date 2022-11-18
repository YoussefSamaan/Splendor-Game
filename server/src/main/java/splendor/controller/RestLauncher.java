package splendor.controller;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * This class powers up Spring and ensures the annotated controllers are detected.
 */
@SpringBootApplication
public class RestLauncher {
  public static void main(String[] args) {
    SpringApplication.run(RestLauncher.class, args);
  }
}
