package controllers;

import Helpers.*;
import Interfaces.IPersonAndProfession;
import Models.Movie;
import Models.Person;
import Models.Profession;

import javax.persistence.*;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class PersonAndProfession implements IPersonAndProfession {
    Helpers helpers = new Helpers();
    ProfessionHelper professionHelper ;
    PersonHelper personHelper ;
    EntityManager em ;

    public PersonAndProfession(EntityManager em) {
        this.professionHelper = new ProfessionHelper(em);
        this.personHelper = new PersonHelper(em);
        this.em = em;
    }

    public void PersonAndProfessionMenu(){
        while (true){
            System.out.println("1. Şəxsiyyətə vəzifə əlavə et");
            System.out.println("2. Şəxsiyyəti vəzifədən sil");
            System.out.println("0. Geriyə qayıt");
            int select = helpers.enterAndCheckSelectedOption(3);
            switch (select){
                case 1:
                    addProfessionToPerson();
                    return;
                case 2:
                    deletePersonFromProfession();
                    return;
                case 0:
                    return;
                default:
                    return;
            }
        }
    }

    @Override
    public void addProfessionToPerson() {
        System.out.println("-------------Şəxsiyyət seçin-----------");
        boolean people = getPeopleNotHaveProfession();
        if (!people) return;
        System.out.println("Zəhmət olmasa şəxsiyyət seçin.....");
        Person getPerson;
        do{
            getPerson = personHelper.askIdAndGetPerson();
            if (getPerson==null||getPerson.getProfession()!=null){
                System.out.println("Zəhmət olmasa, düzgün şəxsiyyət  seçin!!");
            }
            else  break;
        }
        while (true);
        System.out.println("---------Verəcəyiniz vəzifəni seçin---------");
        boolean professions =  getAllProfessions();
        if (!professions) return;
        Profession profession;
        do{
            profession = professionHelper.askIdAndGetProfession();
            if (profession==null){
                System.out.println("Zəhmət olmasa, düzgün vəzifə seçin!!");
            }
            else  break;
        }
        while (true);
        EntityTransaction et = em.getTransaction();
        et.begin();
        getPerson.setProfession(profession);
        em.merge(getPerson);
        et.commit();
        return;
    }

    @Override
    public void deletePersonFromProfession(){
        System.out.println("---------------Vəzifə siyahısı---------------");
        boolean professions=getAllProfessions();
        if (!professions) return;
        System.out.println("---------------Vəzifə siyahısı---------------");
        System.out.println("Zəhmət olmasa, şəxsiyyəti silmək isdədiyiniz vəzifəni seçin.....");
        Profession getProfession;
        do{
            getProfession = professionHelper.askIdAndGetProfession();
            if (getProfession==null){
                System.out.println("Zəhmət olmasa, düzgün vəzifə seçin seçin!!");
            }
            else  break;
        }
        while (true);
        System.out.println("----------------Şəxsiyyətlərin siyahısı----------------");
        boolean people =  getPeopleWhoHaveProfession(getProfession.getId());
        if (!people) return;
        System.out.println("----------------Şəxsiyyətlərin siyahısı----------------");
        System.out.println("Zəhmət olmasa, silmək isdədiyiniz şəxsiyyəti seçin.....");
        Person getPerson;
        do{
            getPerson = personHelper.askIdAndGetPerson();
            Person finalGetPerson = getPerson;
            Optional<Person> isInProfession= getProfession.getPeople().stream().filter(w->w.getId() == finalGetPerson.getId()).findFirst();
            if (isInProfession.isEmpty()){
                System.out.println("Bu şəxsiyyət bu vəzifədə işləmir, zəhmət olmasa düzgün şəxsiyyət seçin!!");
            }
            else  break;
        }
        while (true);
        EntityTransaction et = em.getTransaction();
        et.begin();
        getPerson.setProfession(null);
        em.merge(getPerson);
        et.commit();
    }

    @Override
    public boolean getAllProfessions() {
        List<Profession> professions = em.createNativeQuery(
                "select * from professions p where p.deleted_date is null",Profession.class)
                .getResultList();
        if (professions.size()==0){
            System.out.println("Şəxsiyyətə vəzifə əlavə etdikdən qabaq, zəhmət olmasa vəzifə yaradın!!");
            return false;
        }
        for(Profession profession : professions) {
            System.out.println(" " +
                    "İD: " +profession.getId() +" || "+
                    "AD: " + profession.getName());
        }
        return true;
    }

    public  boolean  getPeopleNotHaveProfession(){
        List<Person> people = em.createNativeQuery(
                "select * from people p JOIN people_details pd on p.person_detail_id = pd.id where p.deleted_date is null and p.profession_id is NULL",Person.class)
                .getResultList();
        if (people.size()==0){
            System.out.println("Şəxsiyyətə vəzifə əlavə etdikdən qabaq, zəhmət olmasa şəxsiyyət yaradın!!");
            return false;
        }
        for(Person p : people) {
            System.out.println(" " +
                    "İD: " + p.getId() +" || "+
                    "AD: " + p.getName() +" || "+
                    "SOYAD: " + p.getSurname()  +","+
                    "KINO:" + p.getName() +" || "+
                    "DOĞUM GÜNÜ: " + p.getBirthDay()+" || "+
                    "HAQQINDA: " + p.getPersonDetail().getAbout()+" || "+
                    "NÖMRƏ: "+p.getPersonDetail().getPhoneNumber());
        }
        return true;
    }

    public  boolean  getPeopleWhoHaveProfession(long movieId){
        List<Person> people = em.createNativeQuery(
                "select * from people p " +
                        "JOIN professions prf on p.profession_id = prf.id and p.profession_id="+movieId+"" +
                        "WHERE p.deleted_date is null",Person.class)
                .getResultList();
        if (people.size()==0){
            System.out.println("Bu vəzifədə hər hansı bir şəxsiyyət yoxdur...!!");
            return false;
        }
        for(Person p : people) {
            System.out.println(" " +
                    "İD: " + p.getId() +" || "+
                    "AD: " + p.getName() +" || "+
                    "SOYAD: " + p.getSurname()  +" || "+
                    "KINO:" + p.getName() +" || "+
                    "DOĞUM GÜNÜ: " + p.getBirthDay()+" || "+
                    "HAQQINDA: " + p.getPersonDetail().getAbout()+" || "+
                    "NÖMRƏ: "+p.getPersonDetail().getPhoneNumber());
        }
        return true;
    }
}
