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
        mainMenu();
    }

    public static void mainMenu(){
        while (true){
        System.out.println("1. Şəxsiyyət üzərində əməliyyat");
        System.out.println("2. Vəzifə üzərində əməliyyat");
        System.out.println("3. Kino üzərində əməliyyat");
        System.out.println("4. Janr üzərində əməliyyat");
        System.out.println("0. Sistemdən çıxmaq");
        selectOptionInMainMenu();
        }
    }

    public static void selectOptionInMainMenu(){
        System.out.println("Zəhmət olmasa seçim edin!!");
        int whichOperation =  helpers.enterAndCheckSelectedOption(5);
        switch (whichOperation){
            case 1:{
                PersonController personController = new PersonController();
                personController.personMenu();
                return;
            }
            case 2:{
                ProfessionController professionController = new ProfessionController();
                professionController.professionMenu();
                return;
            }
            case 3:{
                MovieController movieController = new MovieController();
                movieController.movieMenu();
                return;
            }
            case 4:{
                GenreController genreController = new GenreController();
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
