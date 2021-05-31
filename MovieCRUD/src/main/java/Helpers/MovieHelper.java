package Helpers;

import Models.Movie;
import Models.Person;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MovieHelper {
    EntityManager em;
    Scanner scanner = new Scanner(System.in);
    Helpers helpers = new Helpers();
    public MovieHelper(EntityManager entityManager) {
        this.em=entityManager;
    }
    public boolean checkedName(String text){
        Pattern pattern = Pattern.compile("([0-9]+)?^[\\p{L}\\. \'-]+([0-9]+)?$");
        Matcher matcher = pattern.matcher(text);
        if (!matcher.matches()) System.out.println("Zehmet olmasa ancaq hərif və rəqəm daxile edin!!");
        return matcher.matches();
    }

    public String enterInfo(String text) {
        switch (text.trim()){
            case "title":
                String title="";
                System.out.println("Zəhmət olmasa ad daxil edin: ");
                do{
                    title = scanner.nextLine();
                }
                while (title.trim().length()==0||!checkedName(title.trim()));
                return title;
            case "publish":
                String publish="";
                boolean checkTime;
                LocalDate date=null;
                System.out.println("Zəhmət olmasa yayımlamaq tarixini daxil edin (Nümunə: (2020-04-25))");
                do{
                    checkTime = true;
                    publish = scanner.nextLine();
                    try {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        date = LocalDate.parse(publish, formatter);
                    }
                    catch (Exception e){
                        checkTime=false;
                        System.out.println("Zəhmət olmasa düzgün tarix daxil edin!!");
                    }
                }
                while (publish.trim().length()==0||!checkTime);
                return date.toString();
            default:
                return "error";
        }
    }

    public Movie askIdAndGetMovie() {
        String id="";
        EntityTransaction et;
        Movie movie;
        do{
            do{
                System.out.println("Zəhmət olmasa İD daxil edin!!");
                id = scanner.next();
            }
            while (id.trim().length()==0||!helpers.checkCorrectionForId(id.trim()));
            movie = em.find(Movie.class,Long.parseLong(id));
            if (movie==null||movie.getDeletedDate()!=null){
                System.out.println("Bu ID də kino tapılmadı!!");
            }
            else break;
        }
        while (true);
        return movie;
    }

}
