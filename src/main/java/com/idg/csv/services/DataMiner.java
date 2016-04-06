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
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by agermenos on 4/6/16.
 */
@Service("dataMiner")
public class DataMiner {
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
        for (String type:types) {
            List<FinderEntity> findings = findingsDao.findByPattern(type);
            log.info("Pattern " + type + " \t---> " + findings.size() + " hits");
        }
    }

    public void groupByDatesAndTypes(String[] types){
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
            for (String type:types) {
                List<FinderEntity> findings = findingsDao.findByPatternAndDateRange(type, fromDate, toDate);
                log.info("Pattern " + type + " \t(" + varYear + ") ---> " + findings.size() + " hits");
            }
        }
    }

    public static void main(String args[]){
        String pattern_types[] = {"article","ad","javaforums","category","archives","blog","search","ifind"};
        ApplicationContext context = new ClassPathXmlApplicationContext(
                "/spring/config/beanlocations.xml");
        DataMiner dataMiner = (DataMiner) context.getBean("dataMiner");
        dataMiner.groupAllByDates();
        dataMiner.groupByTypes(pattern_types);
        dataMiner.groupByDatesAndTypes(pattern_types);
    }
}
