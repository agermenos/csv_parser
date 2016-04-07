package com.idg.csv.daos;

import com.idg.csv.pojos.FinderEntity;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by agermenos on 4/6/16.
 */

@Repository("findingsDao")
public class FindingsDao {
    @Autowired
    private SessionFactory sessionFactory;

    @Transactional
    public void add(FinderEntity entinty){
        sessionFactory.getCurrentSession().save(entinty);
    }

    @Transactional
    public void update(FinderEntity entity){
        sessionFactory.getCurrentSession().update(entity);
    }

    @Transactional
    public void delete(FinderEntity entity){
        sessionFactory.getCurrentSession().delete(entity);
    }

    @Transactional
    public FinderEntity findById(int entityId){
        return (FinderEntity) sessionFactory.getCurrentSession().get(FinderEntity.class, entityId);
    }
    @Transactional
    public List<FinderEntity> findByDateRange(Date fromDate, Date toDate) {
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(FinderEntity.class);
        Conjunction and = Restrictions.conjunction();
        and.add( Restrictions.ge("dateDetected", fromDate) );
        and.add( Restrictions.lt("dateDetected", toDate) );
        criteria.add(and);
        return criteria.list();
    }
    @Transactional
    public List<FinderEntity> findByPattern(String type) {
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(FinderEntity.class);
        criteria.add(Restrictions.like("url", "%" + type + "%"));
        return criteria.list();
    }

    @Transactional
    public List<FinderEntity> findByPatternAndDateRange(String type, Date fromDate, Date toDate){
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(FinderEntity.class);
        Conjunction and = Restrictions.conjunction();
        and.add( Restrictions.ge("dateDetected", fromDate) );
        and.add( Restrictions.lt("dateDetected", toDate) );
        criteria.add(and);
        criteria.add(Restrictions.like("url", "%" + type + "%"));
        return criteria.list();
    }

    @Transactional
    public List<FinderEntity> findByTypeAndError(String type, Integer errorType) {
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(FinderEntity.class);
        criteria.add(Restrictions.like("url", "%" + type + "%"));
        criteria.add(Restrictions.eq("responseCode", errorType));
        return criteria.list();
    }

    @Transactional
    public List<FinderEntity> findByError(Integer errorType) {
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(FinderEntity.class);
        criteria.add(Restrictions.eq("responseCode", errorType));
        return criteria.list();
    }
}
