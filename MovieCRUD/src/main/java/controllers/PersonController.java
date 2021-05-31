package controllers;
import Helpers.Helpers;
import Interfaces.IPerson;
import Models.Person;
import javax.persistence.*;
import Helpers.PersonHelper;
import Models.PersonDetail;
import Models.Profession;

import java.time.LocalDate;
import java.util.*;

public class PersonController implements IPerson {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("movieCrud");
    Helpers helpers = new Helpers();
    PersonHelper personHelper = new PersonHelper();
    Scanner scanner = new Scanner(System.in);
    EntityManager em = emf.createEntityManager();
    @Override
    public void createPerson() {
        String name = personHelper.enterInfo("name");
        String surname = personHelper.enterInfo("surname");
        String birth = personHelper.enterInfo("birth");
        String about = personHelper.enterInfo("about");
        String phoneNumber = personHelper.enterInfo("number");
        LocalDate birthLocal = LocalDate.parse(birth);
        EntityTransaction et = em.getTransaction();
        et.begin();
        PersonDetail personDetail = PersonDetail.builder().
                phoneNumber(phoneNumber).
                about(about).build();
        Person newPerson = Person.builder()
                .name(name)
                .surname(surname)
                .birthDay(birthLocal)
                .personDetail(personDetail)
                .createdDate(LocalDate.now()).build();
        em.merge(newPerson);
        et.commit();
    }

    @Override
    public void updatePerson() {
        Person findPerson =  null;
        EntityTransaction et = em.getTransaction();
        do{
            findPerson = personHelper.askIdAndGetPerson();
            if (findPerson==null||findPerson.getDeletedDate()!=null){
                System.out.println("Bu İD də heç bir şəxs tapılmadı!!");
            }
        }
        while (findPerson==null||findPerson.getDeletedDate()!=null);
        boolean wrongSelect = true;
        et.begin();
        do{
            System.out.println("Ad deyişmək istəyirsiniz?");
            int yesOrNot = helpers.askQuestion();
            if (yesOrNot==1){
                findPerson.setName(personHelper.enterInfo("name"));
                wrongSelect = true;
            }else if(yesOrNot==2){
                wrongSelect=true;
            }
            else {
                wrongSelect=false;
            }
        }
        while (!wrongSelect);
        wrongSelect = true;
        do{
            System.out.println("Soyad deyişmək istəyirsiniz?");
            int yesOrNot = helpers.askQuestion();
            if (yesOrNot==1){
                wrongSelect = true;
                findPerson.setSurname(personHelper.enterInfo("surname"));
            }else if(yesOrNot==2){
                wrongSelect=true;
            }
            else {
                wrongSelect=false;
            }
        }
        while (!wrongSelect);
        wrongSelect = true;
        do{
            System.out.println("Doğum tarixini deyişmək istəyirsiniz?");
            int yesOrNot = helpers.askQuestion();
            if (yesOrNot==1){
                wrongSelect = true;
                findPerson.setBirthDay(LocalDate.parse(personHelper.enterInfo("birth")));
            }else if(yesOrNot==2){
                wrongSelect=true;
            }
            else {
                wrongSelect=false;
            }
        }
        while (!wrongSelect);
        em.merge(findPerson);
        et.commit();
    }

    @Override
    public void deletePerson() {
        Person person=null;
        do{
            person = personHelper.askIdAndGetPerson();
            if (person==null||person.getDeletedDate()!=null){
                System.out.println("Bu İD də heç bir şəxs tapılmadı!!");
            }
        }
        while (person==null||person.getDeletedDate()!=null);
        EntityTransaction et = em.getTransaction();
        et.begin();
        person.setDeletedDate(LocalDate.now());
        em.merge(person);
        System.out.println("Şəxsiyyət müvəffəqiyyətlə silindi");
        et.commit();
    }

    @Override
    public void getAll() {
        List<Person> people = em.createNativeQuery(
                "SELECT * FROM people" +
                        " where people.deleted_date" +
                        " is null order by people.name",Person.class)
                .getResultList();
        people.forEach(p->{
                    System.out.println("" +
                            "İD: " + p.getId() +
                            "||  AD: " + p.getName() +
                            "||  SOYAD: " + p.getSurname()  +
                            "||  DOĞUM GÜNÜ: " + p.getBirthDay()+
                            "||  HAQQINDA: " + p.getPersonDetail().getAbout()+
                            "||  NÖMRƏ: "+p.getPersonDetail().getPhoneNumber());
                });
    }

    @Override
    public void searchWithName() {
        String text = "";
        do{
            System.out.println("Zəhmət olmasa ad daxil edin!!");
            text = scanner.nextLine();
        }
        while (text.length()==0||!personHelper.checkedNameOrSurname(text));
        List<Person> people = em.createNativeQuery("select * from people",Person.class).getResultList();
        String finalText = text;
        System.out.println("-------------------Şəxsiyyətlər--------------------");
        people.stream().filter(w->w.getDeletedDate()==null&&w.getName().toLowerCase().startsWith(finalText.trim().toLowerCase()))
                .forEach(p->{
                    System.out.println("" +
                            "İD: " + p.getId() +
                            "||  AD: " + p.getName() +
                            "||  SOYAD: " + p.getSurname()  +
                            "||  DOĞUM GÜNÜ: " + p.getBirthDay()+
                            "||  HAQQINDA: " + p.getPersonDetail().getAbout()+
                            "||  NÖMRƏ: "+p.getPersonDetail().getPhoneNumber());
                });

        return;
    }

    public void personMenu(){
        while (true){
        System.out.println("1. Şəxsiyyət əlavə etmək");
        System.out.println("2. Şəxsiyyət redaktə etmək");
        System.out.println("3. Şəxsiyyəti silmək");
        System.out.println("4. Bütün şəxsiyyətlər");
        System.out.println("5. Ada gÖrə axtarış");
        System.out.println("0. Geriye Qayiıtmaq");
        int x = selectOptionInPersonMenu();
        if (x==0){
            return;
        }
        }
    }

    private int selectOptionInPersonMenu() {
        System.out.println("Zəhmət olmasa seçim edin!!");
        int whichOperation =  helpers.enterAndCheckSelectedOption(6);
        switch (whichOperation){
            case 1:{
                createPerson();
                return 1;
            }
            case 2:{
                updatePerson();
                return 2;
            }
            case 3:{
                deletePerson();
                return 3;
            }
            case 4:{
                getAll();
                return 5;
            }
            case 5:{
                searchWithName();
                return 6;
            }
            case 0:
                return 0;
            default:
                return 8;
        }
    }


}
