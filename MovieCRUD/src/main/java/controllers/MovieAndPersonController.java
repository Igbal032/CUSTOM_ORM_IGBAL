package controllers;
import Helpers.*;
import Interfaces.IMovieAndPerson;
import Models.Movie;
import Models.Person;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.security.PublicKey;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class MovieAndPersonController implements IMovieAndPerson {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("movieCrud");
    Helpers helpers = new Helpers();
    MovieHelper movieHelper = new MovieHelper();
    PersonHelper personHelper = new PersonHelper();
    Scanner scanner = new Scanner(System.in);
    EntityManager em = emf.createEntityManager();

    public void movieAndPersonMenu(){
        while (true){
            System.out.println("1. Kinoya şəxsiyyət əlavə et");
            System.out.println("2. Kinodan şəxsiyyət sil");
            System.out.println("3. Kinodaki bütün şəxsiyyətləri göstər vəzifələri ilə birlikdə");
            System.out.println("0. Geriyə qayıt");
            int select = helpers.enterAndCheckSelectedOption(4);
            switch (select){
                case 1:
                    addPersonToMovie();
                    return;
                case 2:
                    deletePersonFromMovie();
                    return;
                case 3:
                    showAllPeopleInMovie();
                    return;
                case 0:
                    return;
                default:
                    return;
            }
        }
    }
    @Override
    public void showAllPeopleInMovie() {
        System.out.println("--------------Kinoların siyahısı--------------");
        boolean movies=getAllMovies();
        if (!movies) return;
        System.out.println("---------------Kinoların siyahısı---------------");
        System.out.println("Zəhmət olmasa, şəxsiyyəti silmək isdədiyiniz kinonu seçin.....");
        Movie getMovie;
        do{
            getMovie = movieHelper.askIdAndGetMovie();
            if (getMovie==null){
                System.out.println("Zəhmət olmasa, düzgün kino seçin!!");
            }
            else  break;
        }
        while (true);
        getMovie.getPeople().forEach(person->{
            System.out.println("ID: "+person.getId()+",AD: "+person.getName()+
                    ",SOYAD: "+person.getSurname()+",DOĞUM TARİXİ: "+person.getBirthDay()+
                    ",NÖMRƏ: "+person.getPersonDetail().getPhoneNumber()+", HAQQINDA: "+person.getPersonDetail().getAbout());

        });
        return;
    }

    @Override
    public void addPersonToMovie() {
        System.out.println("----------------Kinoların siyahısı----------------");
        boolean movies=getAllMovies();
        if (!movies) return;
        System.out.println("----------------Kinoların siyahısı----------------");
        System.out.println("Zəhmət olmasa, şəxsiyyət əlavə etmək isdədiyiniz kinonu seçin.....");
        Movie getMovie;
        do{
            getMovie = movieHelper.askIdAndGetMovie();
            if (getMovie==null){
                System.out.println("Zəhmət olmasa, düzgün kino seçin!!");
            }
            else  break;
        }
        while (true);
        System.out.println("----------------Şəxsiyyətlərin siyahısı----------------");
        boolean people =  getPeopleNotParticipate();
        if (!people) return;
        System.out.println("----------------Şəxsiyyətlərin siyahısı----------------");
        System.out.println("Zəhmət olmasa, şəxsiyyət  seçin.....");
        Person getPerson;
        do{
            getPerson = personHelper.askIdAndGetPerson();
            if (getPerson==null||getPerson.getMovie()!=null){
                System.out.println("Zəhmət olmasa, düzgün şəxsiyyət  seçin!!");
            }
            else  break;
        }
        while (true);
        EntityTransaction et = em.getTransaction();
        et.begin();
        getPerson.setMovie(getMovie);
        em.merge(getPerson);
        et.commit();
        return;
    }

    @Override
    public void deletePersonFromMovie(){
        System.out.println("---------------Kinoların siyahısı---------------");
        boolean movies=getAllMovies();
        if (!movies) return;
        System.out.println("---------------Kinoların siyahısı---------------");
        System.out.println("Zəhmət olmasa, şəxsiyyəti silmək isdədiyiniz kinonu seçin.....");
        Movie getMovie;
        do{
            getMovie = movieHelper.askIdAndGetMovie();
            if (getMovie==null){
                System.out.println("Zəhmət olmasa, düzgün kino seçin!!");
            }
            else  break;
        }
        while (true);
        System.out.println("----------------Şəxsiyyətlərin siyahısı----------------");
        boolean people =  getPeopleParticipate(getMovie.getId());
        if (!people) return;
        System.out.println("----------------Şəxsiyyətlərin siyahısı----------------");

        System.out.println("Zəhmət olmasa, silmƏk isdədiyiniz şəxsiyyəti seçin.....");
        Person getPerson;
        do{
            getPerson = personHelper.askIdAndGetPerson();
            Person finalGetPerson = getPerson;
            Optional<Person> isInMovie= getMovie.getPeople().stream().filter(w->w.getId() == finalGetPerson.getId()).findFirst();
            if (isInMovie.isEmpty()){
                System.out.println("Bu şəxsiyyət bu kinoda iştirak etmir, zəhmət olmasa düzgün şəxsiyyət seçin!!");
            }
            else  break;
        }
        while (true);
        EntityTransaction et = em.getTransaction();
        et.begin();
        getPerson.setMovie(null);
        em.merge(getPerson);
        et.commit();
    }

    @Override
    public  boolean  getAllMovies(){
        List<Movie> movies = em.createNativeQuery(
                "select * from movies m WHERE m.deleted_date is null order by m.title",Movie.class)
                .getResultList();
        if (movies.size()==0){
            System.out.println("Kinoya şəxsiyyət əlavə etməkdən qabaq, zəhmət olmasa kino yaradın!!");
            return false;
        }
        System.out.println("----------------Kinolar-------------");
        movies.forEach(m->{
            System.out.println(""+
                    "İD: " + m.getId() +" || "+
                    "AD: " + m.getTitle()+" || "+
                    "YAYIM TARİXİ: "+ m.getPublishedDate());
        });
        return true;
    }

    public  boolean  getPeopleNotParticipate(){
        List<Person> people = em.createNativeQuery(
                "select * from people p JOIN people_details pd on p.person_detail_id = pd.id WHERE p.movie_id is NULL and p.deleted_date is null",Person.class)
                .getResultList();
        if (people.size()==0){
            System.out.println("Kinoya şəxsiyyət əlavə etməkdən qabaq, zəhmət olmasa şəxsiyyət yaradın!!");
            return false;
        }
        people.stream().forEach(p->{
                    System.out.println("" +
                            "İD: " + p.getId() +
                            " || AD: " + p.getName() +
                            " || SOYAD: " + p.getSurname()  +
                            " || DOĞUM GÜNÜ: " + p.getBirthDay()+
                            " || HAQQINDA: " + p.getPersonDetail().getAbout()+
                            " || NÖMRƏ: "+p.getPersonDetail().getPhoneNumber());
                });
        return true;
    }

    public boolean getPeopleParticipate(long movieId){
        List<Person> people = em.createNativeQuery("select * from people p JOIN people_details pd on p.person_detail_id=pd.id " +
                "JOIN movies m on p.movie_id=m.id and p.movie_id="+movieId+"" +
                "where p.deleted_date is null",Person.class)
                .getResultList();
        if (people.size()==0){
            System.out.println("Kinoya şəxsiyyət əlavə etməkdən qabaq, zəhmət olmasa şəxsiyyət yaradın!!");
            return false;
        }
        people.stream().forEach(p->{
            System.out.println("" +
                    "İD: " + p.getId() +
                    " || AD: " + p.getName() +
                    " || SOYAD: " + p.getSurname()  +
                    " || DOĞUM GÜNÜ: " + p.getBirthDay()+
                    " || HAQQINDA: " + p.getPersonDetail().getAbout()+
                    " || NÖMRƏ: "+p.getPersonDetail().getPhoneNumber());
        });
        return true;
    }
}

