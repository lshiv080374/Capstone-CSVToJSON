package com.example.demo.controller;

import com.example.demo.mainlogic.CSVToJson_Logic;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class DemoController {
    public static String uploadDirectory = System.getProperty("user.dir") + "/uploads";

    @RequestMapping("/")
    public String UploadPage(Model model) {
        return "MainPage1";
    }

    @RequestMapping("/upload")
    public String upload(Model model, @RequestParam("files") MultipartFile file) throws IOException {
        StringBuilder jsonStringBuilder = new StringBuilder();
        StringBuilder resultStringBuilder = new StringBuilder();
        String csvContent=null;
        String jsonContent=null;

        StringBuilder fileNames = new StringBuilder();
        //for (MultipartFile file : files) {
        Path fileNameAndPath = Paths.get(uploadDirectory, file.getOriginalFilename());
        fileNames.append(file.getOriginalFilename());
       // System.out.println("The file name is: " + fileNames.toString());

        File currentFile = new File(uploadDirectory+"/"+fileNames.toString());

        try {
            FileWriter f = new FileWriter(currentFile);
            resultStringBuilder = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
                String line;
                while ((line = br.readLine()) != null) {
                    resultStringBuilder.append(line).append("\n");
                }
            }
            f.write(resultStringBuilder.toString());
            f.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            // Files.write(fileNameAndPath, file.getBytes());
            Files.write(fileNameAndPath, file.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        CSVToJson_Logic parse = new CSVToJson_Logic();
        File jsonFile=parse.convert(currentFile);


        BufferedReader reader = new BufferedReader(new FileReader(jsonFile));
        String line = null;
        String ls = System.getProperty("line.separator");
        while ((line = reader.readLine()) != null) {
            jsonStringBuilder.append(line);
            jsonStringBuilder.append(ls);
        }
        // delete the last new line separator
        jsonStringBuilder.deleteCharAt(jsonStringBuilder.length() - 1);
        reader.close();
        jsonContent = jsonStringBuilder.toString();
        csvContent= resultStringBuilder.toString();

        //map a new method to call csv to json converter class
        //push the result to the respective IDs (text area) - model.attributes("CSV", filenames)
        //data structure, for each to display line by line
        //in foreach loap: use this code to display the whole: model.addAttribute("name","Lavanya"); - to pull data from backend to front end
        //check thymeleaf tab- to display the files as tabs

        model.addAttribute("jsonFileContent",jsonContent);
        model.addAttribute("csvFileContent",csvContent);
        model.addAttribute("msg", "Successfully uploaded file " + fileNames.toString());
        return "MainPage1";


    }
    //@PostMapping()
    /*public static void convertfile(File file) {
        CSVToJson_Logic parse = new CSVToJson_Logic();
        parse.convert(file);
    } */


}
