package Helpers;

import Models.Genre;
import Models.Profession;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GenreHelper {

    public static Scanner scanner = new Scanner(System.in);
    EntityManager em ;
    Helpers helpers = new Helpers();
    public GenreHelper(EntityManager entityManager) {
        this.em=entityManager;
    }
    public boolean checkedName(String text){
        Pattern pattern = Pattern.compile("([0-9]+)?^[\\p{L}\\. \'-]+([0-9]+)?$");
        Matcher matcher = pattern.matcher(text);
        if (!matcher.matches()) System.out.println("Zehmet olmasa ancaq hərif və rəqəm daxile edin!!");
        return matcher.matches();
    }

    public Genre askIdAndGetGenre() {
        String id="";
        do{
            System.out.println("Zəhmət olmasa İD daxil edin!!");
            id = scanner.next();
        }
        while (id.trim().length()==0||!helpers.checkCorrectionForId(id.trim()));
        EntityTransaction et = em.getTransaction();
        Genre genre = em.find(Genre.class,Long.parseLong(id));
        return genre;
    }

    public String enterInfo() {
        String name="";
        System.out.println("Zəhmət olmasa ad daxil edin: ");
        do {
            name = scanner.nextLine();
        }
        while (name.trim().length()==0||!checkedName(name.trim()));
        return name;
    }
}
