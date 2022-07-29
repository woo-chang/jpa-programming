package fetch;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class Main {

  public static void main(String[] args) {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
    EntityManager em = emf.createEntityManager();
    EntityTransaction tx = em.getTransaction();

    tx.begin();

    try {
      Library libraryA = new Library();
      libraryA.setName("libraryA");
      em.persist(libraryA);

      Book bookA = new Book();
      bookA.setName("bookA");
      bookA.setLibrary(libraryA);
      em.persist(bookA);

      Book bookB = new Book();
      bookB.setName("bookB");
      bookB.setLibrary(libraryA);
      em.persist(bookB);

      String query = "select distinct l from Library l join fetch l.books where l.name = 'libraryA'";

      em.flush();
      em.clear();

      List<Library> libraries = em.createQuery(query, Library.class).getResultList();

      for (Library library : libraries) {
        System.out.println("library.name = " + library.getName() + ", book count = " + library.getBooks().size());
      }

      tx.commit();
    } catch (Exception e) {
      tx.rollback();
    } finally {
      em.close();
    }

    emf.close();
  }

}
