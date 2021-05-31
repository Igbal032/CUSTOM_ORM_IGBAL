package Helpers;

import Models.Person;
import Models.Profession;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProfessionHelper {
    Scanner scanner = new Scanner(System.in);
    Helpers helpers =  new Helpers();
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("movieCrud");
    EntityManager em = emf.createEntityManager();

    public String enterInfo(){
        String name="";
        System.out.println("Zəhmət olmasa ad daxil edin: ");
        do{
            name = scanner.nextLine();
        }
        while (name.trim().length()==0||!checkedName(name.trim()));
        return name;
    }

    public Profession askIdAndGetProfession() {
        String id="";
        do{
            System.out.println("Zəhmət olmasa İD daxil edin!!");
            id = scanner.next();
        }
        while (id.trim().length()==0||!helpers.checkCorrectionForId(id.trim()));
        EntityTransaction et = em.getTransaction();
        et.begin();
        Profession profession = em.find(Profession.class,Long.parseLong(id));
        et.commit();
        return profession;
    }

    public boolean checkedName(String text){
        Pattern pattern = Pattern.compile("^[\\p{L}\\. \'-]+$");
        Matcher matcher = pattern.matcher(text);
        if (!matcher.matches()) System.out.println("Zehmet olmasa ancaq hərif daxil edin!!");
        return matcher.matches();
    }

}
