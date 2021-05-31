package Helpers;

import Models.Person;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PersonHelper {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("movieCrud");
    EntityManager em = emf.createEntityManager();
    Scanner scanner = new Scanner(System.in);
    Helpers helpers = new Helpers();

    public boolean checkedNameOrSurname(String text){
        Pattern pattern = Pattern.compile("^[\\p{L}\\. \'-]+$");
        Matcher matcher = pattern.matcher(text);
        if (!matcher.matches()) System.out.println("Zehmet olmasa ancaq hərif daxil edin!!");
        return matcher.matches();
    }

    public String enterInfo(String text) {
        switch (text.trim()){
            case "name":
                String name="";
                System.out.println("Zəhmət olmasa ad daxil edin: ");
                do{
                    name = scanner.nextLine();
                }
                while (name.trim().length()==0||!checkedNameOrSurname(name.trim()));
                return name;
            case "surname":
                String surname="";
                System.out.println("Zəhmət olmasa soyad daxil edin: ");
                do{
                    surname = scanner.nextLine();
                }
                while (surname.trim().length()==0||!checkedNameOrSurname(surname.trim()));
                return surname;
            case "birth":
                String birth="";
                boolean checkTime;
                LocalDate date=null;
                System.out.println("Zəhmət olmasa doğum gününü daxil edin(Nümunə: (1999-04-25))");
                do{
                    checkTime = true;
                    birth = scanner.nextLine();
                    try {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        date = LocalDate.parse(birth, formatter);
                    }
                    catch (Exception e){
                        checkTime=false;
                        System.out.println("Zəhmət olmasa düzgün tarix daxil edin!!");
                    }
                }
                while (birth.trim().length()==0||!checkTime);
                return date.toString();

            case "number":
                String phoneNumber="";
                System.out.println("Zəhmət olmasa nomre daxil edin: ");
                do{
                    phoneNumber = scanner.nextLine();
                }
                while (phoneNumber.trim().length()==0||!checkPhoneNumber(phoneNumber));
                return phoneNumber;

            case "about":
                String about="";
                System.out.println("Zəhmət olmasa əlavə məlumat daxil edin daxil edin: ");
                do{
                    about = scanner.nextLine();
                }
                while (about.trim().length()==0);
                return about;

            default:
                return "error";
        }
    }

    public Person askIdAndGetPerson() {
        String id="";
        Person person;
        EntityTransaction et;
        do{
            do{
                System.out.println("Zəhmət olmasa İD daxil edin!!");
                id = scanner.next();
            }
            while (id.trim().length()==0||!helpers.checkCorrectionForId(id.trim()));
            et = em.getTransaction();
            et.begin();
            person = em.find(Person.class,Long.parseLong(id));
            if (person==null||person.getDeletedDate()!=null){
                System.out.println("Bu ID də şəxsiyyət tapılmadı");
                et.commit();
            }
            else break;
        }
        while (true);
        et.commit();
        return person;
    }

    boolean checkPhoneNumber(String phoneNumber) {
        Pattern pattern = Pattern.compile("[+]{1}[9]{2}[4]{1}(([5]([0]|[1]|[5]))|([7]([0]|[7]))|([9]([9])))[1-9][0-9]{6}");
        Matcher matcher = pattern.matcher(phoneNumber);
        if (!matcher.matches()) {
            System.out.println("Zəhmıt olmasa düzgün nömrə daxil edin... (Ex: +994(50/51/55/70/77/99)6705569)!!!");
            return matcher.matches();
        }
        return matcher.matches();
    }
}

