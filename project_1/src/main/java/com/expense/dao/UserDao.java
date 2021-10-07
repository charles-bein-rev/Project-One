package com.expense.dao;

import com.expense.model.User;
import com.expense.utils.HibernateSessionFactory;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class UserDao {

	public User findByName(String username) {
		User user = null;
		Session session = null;
		Transaction tx = null;

		try {
			session = HibernateSessionFactory.getSession();
			tx = session.beginTransaction();
			user = session.createQuery("FROM User WHERE username = :username", User.class)
					.setParameter("username", username)
					.uniqueResult();
			tx.commit();
		} catch (HibernateException e) {
			tx.rollback();
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return user;
	}
}
