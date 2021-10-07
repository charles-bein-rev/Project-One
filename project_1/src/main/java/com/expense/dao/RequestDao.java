package com.expense.dao;

import com.expense.model.Request;
import com.expense.utils.HibernateSessionFactory;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class RequestDao {

	public Request findById(int id) {
		Request request = null;
		Session session = null;
		Transaction tx = null;

		try {
			session = HibernateSessionFactory.getSession();
			tx = session.beginTransaction();
			request = session.get(Request.class, id);
			tx.commit();
		} catch(HibernateException e) {
			tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}

		return request;
	}

	public List<Request> findAll() {
		List<Request> requests = null;

		Session session = null;
		Transaction tx = null;

		try {
			session = HibernateSessionFactory.getSession();
			tx = session.beginTransaction();
			requests = session.createQuery("FROM Request", Request.class).getResultList();
			tx.commit();
		} catch(HibernateException e) {
			tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}

		return requests;
	}

	public void save(Request request) {
		Session session = null;
		Transaction tx = null;

		try {
			session = HibernateSessionFactory.getSession();
			tx = session.beginTransaction();
			session.save(request);
			tx.commit();
		} catch(HibernateException e) {
			tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	public void update(Request request) {
		Session session = null;
		Transaction tx = null;

		try {
			session = HibernateSessionFactory.getSession();
			tx = session.beginTransaction();
			session.update(request);
			tx.commit();
		} catch(HibernateException e) {
			tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	public void delete(Request request) {
		Session session = null;
		Transaction tx = null;

		try {
			session = HibernateSessionFactory.getSession();
			tx = session.beginTransaction();
			session.delete(request);
			tx.commit();
		} catch(HibernateException e) {
			tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
	}
}
