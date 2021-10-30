package com.example.demo;

import com.example.demo.controller.DemoController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;


@SpringBootApplication
//@ComponentScan({"com.example.demo","DemoController"})
public class DemoApplication {

	public static void main(String[] args) {
		new File(DemoController.uploadDirectory).mkdir();
		SpringApplication.run(DemoApplication.class, args);
	}

}
