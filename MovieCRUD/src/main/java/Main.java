import Helpers.Helpers;
import Models.Person;
import Models.Profession;
import controllers.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static Helpers helpers = new Helpers();
    public static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("movieCrud");
        EntityManager em = emf.createEntityManager();
        mainMenu(em);
    }

    public static void mainMenu(EntityManager entityManager){
        while (true){
        System.out.println("1. Şəxsiyyət üzərində əməliyyat");
        System.out.println("2. Vəzifə üzərində əməliyyat");
        System.out.println("3. Kino üzərində əməliyyat");
        System.out.println("4. Janr üzərində əməliyyat");
        System.out.println("0. Sistemdən çıxmaq");
        selectOptionInMainMenu(entityManager);
        }
    }

    public static void selectOptionInMainMenu(EntityManager entityManager){
        System.out.println("Zəhmət olmasa seçim edin!!");
        int whichOperation =  helpers.enterAndCheckSelectedOption(5);
        switch (whichOperation){
            case 1:{
                PersonController personController = new PersonController(entityManager);
                personController.personMenu();
                return;
            }
            case 2:{
                ProfessionController professionController = new ProfessionController(entityManager);
                professionController.professionMenu();
                return;
            }
            case 3:{
                MovieController movieController = new MovieController(entityManager);
                movieController.movieMenu();
                return;
            }
            case 4:{
                GenreController genreController = new GenreController(entityManager);
                genreController.genreMenu();
                return;
            }
            case 0:
                System.exit(0);
            default:
                return;
        }

    }
}
