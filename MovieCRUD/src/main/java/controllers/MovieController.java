package controllers;

import Helpers.Helpers;
import Interfaces.IMovie;
import Models.Genre;
import Models.Movie;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import Helpers.*;
import Models.Person;

public class MovieController implements IMovie {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("movieCrud");
    Helpers helpers = new Helpers();
    MovieHelper movieHelper = new MovieHelper();
    GenreHelper genreHelper = new GenreHelper();
    Scanner scanner = new Scanner(System.in);
    EntityManager em = emf.createEntityManager();

    @Override
    public void createMovie() {
        String title = movieHelper.enterInfo("title");
        String publish = movieHelper.enterInfo("publish");
        LocalDate publishLocal = LocalDate.parse(publish);
        EntityTransaction et = em.getTransaction();
        et.begin();
        Movie newMovie = Movie.builder()
                .title(title)
                .publishedDate(publishLocal)
                .createdDate(LocalDate.now()).build();
        em.merge(newMovie);
        et.commit();
    }

    @Override
    public void updateMovie() {
        Movie findMovie =  null;
        EntityTransaction et = em.getTransaction();
        do{
            findMovie = movieHelper.askIdAndGetMovie();
            if (findMovie==null||findMovie.getDeletedDate()!=null){
                System.out.println("Bu İD də heç bir kino tapılmadı!!");
            }
        }
        while (findMovie==null||findMovie.getDeletedDate()!=null);
        boolean wrongSelect = true;
        et.begin();
        do{
            System.out.println("Ad deyişmək istəyirsiniz?");
            int yesOrNot = helpers.askQuestion();
            if (yesOrNot==1){
                findMovie.setTitle(movieHelper.enterInfo("title"));
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
            System.out.println("Yayım tarixini deyişmək istəyirsiniz?");
            int yesOrNot = helpers.askQuestion();
            if (yesOrNot==1){
                wrongSelect = true;
                findMovie.setPublishedDate(LocalDate.parse(movieHelper.enterInfo("publish")));
            }else if(yesOrNot==2){
                wrongSelect=true;
            }
            else {
                wrongSelect=false;
            }
        }
        while (!wrongSelect);
        em.merge(findMovie);
        et.commit();
    }

    @Override
    public void deleteMovie() {
        Movie movie=null;
        do{
            movie = movieHelper.askIdAndGetMovie();
            if (movie==null||movie.getDeletedDate()!=null){
                System.out.println("Bu İD də heç bir kino tapılmadı!!");
            }
        }
        while (movie==null||movie.getDeletedDate()!=null);
        EntityTransaction et = em.getTransaction();
        et.begin();
        movie.setDeletedDate(LocalDate.now());
        em.merge(movie);
        System.out.println("Kino müvəffəqiyyətlə silindi");
        et.commit();
    }


    @Override
    public List<Movie> getAll() {
        List<Movie> movies = em.createNativeQuery("select * from movies m where m.deleted_date is null order by m.title",Movie.class).getResultList();
        return movies;
    }

    @Override
    public void searchWithName() {
        String text = "";
        do{
            System.out.println("Zəhmət olmasa ad daxil edin!!");
            text = scanner.nextLine();
        }
        while (text.length()==0||!movieHelper.checkedName(text));
        List<Movie> movies = getAll();
        System.out.println("-------------Kinolar------------");
        String finalText = text;
        movies.stream().filter(w->w.getTitle().toLowerCase().startsWith(finalText.trim().toLowerCase())).forEach(w->{
            System.out.println("-------------------Kino-------------------------\n"+
                    "İD: " + w.getId() +" || "+
                    "AD: " + w.getTitle() +" || "+
                    "YAYIM TARİXİ: "+  w.getPublishedDate());
            System.out.println("-------------------JANRLAR---------------------------------");
            w.getGenres().forEach(p->{
                System.out.println("JANR "+p.getId()+": "+p.getName());
            });
            System.out.println("------------------------------------------------------------");
            System.out.println("------------------------------------------------------------");
        });
        if (movies.size()!=0)
            movieMenu2();
        else {
            System.out.println("Bu adda Kino mövcud deyil");
            return;
        }
    }

    public void movieMenu(){
        while (true){
            System.out.println("1. Kino əlavə edin");
            System.out.println("2. Ada görə axtarış");
            System.out.println("3. Kinodakı şəxsiyyətlərə görə əməliyyat");
            System.out.println("4. Kinoya janr əlavə etmək");
            System.out.println("0. Geriyə qayit");
            int select =  selectOptionInMovieMenu();
            if (select==0){
                return;
            }
        }
    }

    public void movieMenu2(){
        while (true){
            System.out.println("1. Kinonu redaktə et");
            System.out.println("2. Kino silmək");
            System.out.println("0. Geriyə qayit");
            int select =  selectOptionInMovieMenu2();
            return;
        }
    }

    private int selectOptionInMovieMenu() {
        System.out.println("Zəhmət olmasa seçim edin!!");
        int whichOperation =  helpers.enterAndCheckSelectedOption(5);
        switch (whichOperation){
            case 1:{
                createMovie();
                return 1;
            }
            case 2:{
                searchWithName();
                return 2;
            }
            case 3:{
                MovieAndPersonController movieAndPersonController = new MovieAndPersonController();
                movieAndPersonController.movieAndPersonMenu();
                return 3;
            }
            case 4:{
                addGenreToMovie();
                return 4;
            }
            case 0:
                return 0;
            default:
                return 5;
        }
    }

    @Override
    public void addGenreToMovie() {
        System.out.println("-------Bütün Kinolar---------");
        List<Movie> movies = getAll();
        if (movies.size()==0){
            System.out.println("Heç bir kino tapılmadı!!!!");
            return;
        }
        movies.forEach(w->{
            System.out.println("İD: " + w.getId() +" || AD: " + w.getTitle() +" || YAYIM TARİXİ: "+  w.getPublishedDate());
        });
        Movie movie=null;
        do{
            movie = movieHelper.askIdAndGetMovie();
            if (movie==null||movie.getDeletedDate()!=null){
                System.out.println("Bu İD də heç bir kino tapılmadı!!");
            }
        }
        while (movie==null||movie.getDeletedDate()!=null);
        System.out.println("-------Bütün Janrlar---------");
        List<Genre> genres = em.createNativeQuery("select * from genres g where g.deleted_date is null order by g.name",Genre.class).getResultList();
        genres.forEach(w->{
            System.out.println("İD: " + w.getId() +" || AD: " + w.getName());
        });
        Genre genre=null;
        do{
            genre = genreHelper.askIdAndGetGenre();
            if (genre==null||genre.getDeletedDate()!=null)
            {
                System.out.println("Bu İD də heç bir janr tapılmadı!!");
            }
        }
        while (genre==null||genre.getDeletedDate()!=null);
        EntityTransaction et = em.getTransaction();
        et.begin();
        genre.getMovies().add(movie);
        movie.getGenres().add(genre);
        em.merge(movie);
        em.merge(genre);
        System.out.println("Kinoya yeni janr elavə olundu");
        et.commit();
    }

    private int selectOptionInMovieMenu2() {
        System.out.println("Zəhmət olmasa seçim edin!!");
        int whichOperation =  helpers.enterAndCheckSelectedOption(3);
        switch (whichOperation){
            case 1:{
                updateMovie();
                return 1;
            }
            case 2:{
                deleteMovie();
                return 2;
            }
            case 0:
                return 0;
            default:
                return 5;
        }
    }
}
