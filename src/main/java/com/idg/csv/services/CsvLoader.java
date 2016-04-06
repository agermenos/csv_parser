package com.idg.csv.services;

import com.idg.csv.daos.FindingsDao;
import com.idg.csv.pojos.FinderEntity;
import com.idg.csv.utils.Formatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by agermenos on 4/6/16.
 */
@Component("csvLoader")
public class CsvLoader {
    private static String DATE_FORMAT="mm/dd/yy";
    private String csvFile;
    @Autowired
    private FindingsDao findingsDao;

    public void readCSVFile(String fileName, Charset cs) {
        Path path = Paths.get(fileName);
        BufferedReader br = null;
        Integer lineNumber=0;
        try {
            br = Files.newBufferedReader(path, cs);
            String line;
            while((line = br.readLine()) != null){
                if (lineNumber++>0) {
                    try {
                        FinderEntity finding = new FinderEntity();
                        finding.setId(lineNumber);
                        finding.setUrl(line.split(",")[0]);
                        finding.setResponseCode(Integer.parseInt(line.split(",")[1]));
                        finding.setDateDetected(Formatter.formatDate(line.split(",")[3], DATE_FORMAT));
                        finding.setCategory(line.split(",")[4]);
                        finding.setPlatform(line.split(",")[5]);
                        finding.setDateCrawled(Formatter.formatDate(line.split(",")[6], DATE_FORMAT));
                        findingsDao.add(finding);
                    }
                    catch (Exception e){
                        System.out.println("ERROR: " + line);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            br.close();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]){
        ApplicationContext context = new ClassPathXmlApplicationContext(
                "/spring/config/beanlocations.xml");
        CsvLoader csvLoader = (CsvLoader)context.getBean("csvLoader");

        csvLoader.readCSVFile("target/classes/javaworld_file.csv", StandardCharsets.UTF_8);

    }
}
