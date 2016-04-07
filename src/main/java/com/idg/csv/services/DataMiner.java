package com.idg.csv.services;

import com.idg.csv.daos.FindingsDao;
import com.idg.csv.pojos.FinderEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by agermenos on 4/6/16.
 */
@Service("dataMiner")
public class DataMiner {
    final static String PATTERN_TYPES[] = {"article","ad","javaforums","category","archives","blog","search","ifind","javaworjw"};
    final static Integer ERROR_TYPES[] = {403, 400, 500, 404, 301, 503};
    final static Integer TOTAL_PAGES = 1440;
    final static String VALID_PATTERNS[] = {"article","category","blog", "author"};

    @Autowired
    private FindingsDao findingsDao;
    Logger log = LoggerFactory.getLogger(this.getClass());
    public void groupAllByDates(){
        SimpleDateFormat sdf = new SimpleDateFormat("mm/dd/yy");
        Calendar calendar = new GregorianCalendar(2013,1,28,13,24,56);
        for (Integer varYear=2010; varYear<2016; varYear++) {
            calendar.set(Calendar.YEAR, varYear);
            calendar.set(Calendar.MONTH, 0);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            Date fromDate = calendar.getTime();
            calendar.set(Calendar.MONTH, 11);
            calendar.set(Calendar.DAY_OF_MONTH, 31);
            Date toDate = calendar.getTime();
            List<FinderEntity> findings = findingsDao.findByDateRange(fromDate, toDate);
            log.info("Total of " + findings.size() + " items " + " for Year " + varYear);
        }
    }

    public void groupByTypes(String [] types){
        log.info ("GROUPED BY TYPES");
        log.info("||Type||Hits||Percentage||");
        for (String type:types) {
            List<FinderEntity> findings = findingsDao.findByPattern(type);
            log.info("|" + type + "|" + findings.size() + "|" + percent(findings.size())+"|");
        }
    }

    public void groupByDatesAndTypes(String[] types){
        log.info ("GROUPED BY DATES AND TYPES");
        log.info("||Type||Year||Hits||Percentage||");

        SimpleDateFormat sdf = new SimpleDateFormat("mm/dd/yy");
        Calendar calendar = new GregorianCalendar(2013,1,28,13,24,56);
        for (Integer varYear=2010; varYear<2017; varYear++) {
            calendar.set(Calendar.YEAR, varYear);
            calendar.set(Calendar.MONTH, 0);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            Date fromDate = calendar.getTime();
            calendar.set(Calendar.MONTH, 11);
            calendar.set(Calendar.DAY_OF_MONTH, 31);
            Date toDate = calendar.getTime();
            for (String type:types) {
                List<FinderEntity> findings = findingsDao.findByPatternAndDateRange(type, fromDate, toDate);
                log.info("|" + type + "|" + varYear + "|" + findings.size() + "|" + percent(findings.size())+"|");
            }
        }
    }

    public void groupByTypesAndErrors(String[] types, Integer[] errorTypes) {

        log.info ("GROUPED BY TYPES AND ERRORS");
        log.info("||Type||Error Type||Hits||Percentage||");
        for (String type:types) {
            for (Integer errorType : errorTypes) {
                List<FinderEntity> findings = findingsDao.findByTypeAndError(type, errorType);
                log.info("|" + type + "|" + errorType + "|" + findings.size() + "|" + percent(findings.size())+"|");
            }
        }
    }

    public void groupByAcceptedURLs(String[] acceptedUrls, Integer[] errorTypes) {
        log.info ("ACCEPTED URLs AND ERRORS");
        log.info("||Url Pattern||Error Type||Hits||Percentage||");
        Map<Integer, Integer> unnacceptedUrls=new HashMap<>();
        for (Integer errorType : errorTypes) {
            Integer total=0;
            for (String accpetedUrl:acceptedUrls) {
                List<FinderEntity> findings = findingsDao.findByTypeAndError(accpetedUrl,errorType);
                log.info("|" + accpetedUrl + "|" + errorType + "|" + findings.size() + "|" + percent(findings.size())+"|");
                total+=findings.size();
            }
            unnacceptedUrls.put(errorType, findingsDao.findByError(errorType).size() - total);
        }

        log.info ("UNACCEPTED URLs AND ERRORS");
        log.info("||Error Type||Hits||Percentage||");
        for (Integer errorType: errorTypes){
            log.info("|"+errorType+"|"+unnacceptedUrls.get(errorType)+"|"+percent(unnacceptedUrls.get(errorType))+"|");
        }
    }


    public static void main(String args[]){
        ApplicationContext context = new ClassPathXmlApplicationContext(
                "/spring/config/beanlocations.xml");
        DataMiner dataMiner = (DataMiner) context.getBean("dataMiner");
        //dataMiner.groupAllByDates();
        dataMiner.groupByTypes(PATTERN_TYPES);
        //dataMiner.groupByDatesAndTypes(PATTERN_TYPES);
        dataMiner.groupByTypesAndErrors(PATTERN_TYPES, ERROR_TYPES);
        dataMiner.groupByAcceptedURLs(VALID_PATTERNS, ERROR_TYPES);
    }

    private String percent(int hits){
        float result=hits*100/TOTAL_PAGES;
        return String.format("%.1f", result)+"%";
    }
}
