package testes;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import entities.TableDBF;

public class TesteAcessoBD {

	public static void notmain(String[] args){
		try {
			EntityManagerFactory factory = Persistence.createEntityManagerFactory("medic_base");
			
			EntityManager em = factory.createEntityManager();
			
			TableDBF t;
		
			t = new TableDBF();
			
			t.setCreatedAt(new Date());
			Date now = new Date();
			t.setDbfFileType("Tipo B negativo");
			t.setLastUpdate(now);
			t.setModifiedAt(now);
			t.setName("HJK9989");
			t.setNumberRecords(666);
			t.setTableFlag("Flag Rado");
			
			em.getTransaction().begin();
			em.persist(t);
			em.getTransaction().commit();
		
		
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage() + " " + e.getLocalizedMessage());
		}
		System.out.println("Fim!");

	}
	
}
