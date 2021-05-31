package controllers;
import Helpers.Helpers;
import Interfaces.IGenre;
import Models.Genre;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
import Helpers.GenreHelper;


public class GenreController implements IGenre {
    Helpers helpers = new Helpers();
    GenreHelper genreHelper;
    Scanner scanner = new Scanner(System.in);
    EntityManager em ;
    public GenreController(EntityManager entityManager) {
        this.em=entityManager;
        this.genreHelper=new GenreHelper(entityManager);
    }
    @Override
    public void createGenre() {
        String name = genreHelper.enterInfo();
        EntityTransaction et = em.getTransaction();
        et.begin();
        Genre newGenre = Genre.builder()
                .name(name)
                .createdDate(LocalDate.now()).build();
        em.merge(newGenre);
        et.commit();
    }

    @Override
    public void updateGenre() {
        Genre findGenre =  null;
        EntityTransaction et = em.getTransaction();
        do{
            findGenre = genreHelper.askIdAndGetGenre();
            if (findGenre==null||findGenre.getDeletedDate()!=null){
                System.out.println("Bu İD də heç bir janr tapılmadı!!");
            }
        }
        while (findGenre==null||findGenre.getDeletedDate()!=null);
        boolean wrongSelect = true;
        et.begin();
        do{
            System.out.println("Ad deyişmək istəyirsiniz?");
            int yesOrNot = helpers.askQuestion();
            if (yesOrNot==1){
                findGenre.setName(genreHelper.enterInfo());
                wrongSelect = true;
            }else if(yesOrNot==2){
                wrongSelect=true;
            }
            else {
                wrongSelect=false;
            }
        }
        while (!wrongSelect);
        em.merge(findGenre);
        et.commit();
    }

    @Override
    public void deleteGenre() {
        Genre genre=null;
        do{
            genre = genreHelper.askIdAndGetGenre();
            if (genre==null||genre.getDeletedDate()!=null){
                System.out.println("Bu İD də heç bir janr tapılmadı tapılmadı!!");
            }
        }
        while (genre==null||genre.getDeletedDate()!=null);
        EntityTransaction et = em.getTransaction();
        et.begin();
        genre.setDeletedDate(LocalDate.now());
        em.merge(genre);
        System.out.println("Janr müvəffəqiyyətlə silindi");
        et.commit();

    }

    @Override
    public void searchWithName() {
        String text = "";
        do{
            System.out.println("Zəhmət olmasa ad daxil edin!!");
            text = scanner.nextLine();
        }
        while (text.length()==0||!genreHelper.checkedName(text));
        List<Genre> genres = em.createNativeQuery(
                "select * from genres g WHERE g.deleted_date is null order by g.name",Genre.class)
                .getResultList();
        System.out.println("--------------Janrlar-------------");
        String finalText = text;
        genres.stream().filter(w->w.getName().toLowerCase().startsWith(finalText.trim().toLowerCase())).forEach(p->{
            System.out.println(""+
                    "İD: " + p.getId() +" || "+
                    "AD: " + p.getName());
        });
        if (genres.size()!=0)
            genreMenu2();
        else {
            System.out.println("Bu adda Janr mövcud deyil");
            return;
        }
    }

    public void genreMenu(){
        while (true){
            System.out.println("1. Janr əlavə edin");
            System.out.println("2. Ada görə axtarış");
            System.out.println("0. Geriyə qayit");
            int select =  selectOptionInGenreMenu();
            if (select==0){
                return;
            }
        }
    }

    public void genreMenu2(){
        while (true){
            System.out.println("1. Janr redaktə et");
            System.out.println("2. Janr silmək");
            System.out.println("0. Geriyə qayit");
            int select =  selectOptionInGenreMenu2();
            return;
        }
    }

    private int selectOptionInGenreMenu() {
        System.out.println("Zəhmət olmasa seçim edin!!");
        int whichOperation =  helpers.enterAndCheckSelectedOption(3);
        switch (whichOperation){
            case 1:{
                createGenre();
                return 1;
            }
            case 2:{
                searchWithName();
                return 2;
            }
            case 0:
                return 0;
            default:
                return 5;
        }
    }

    private int selectOptionInGenreMenu2() {
        System.out.println("Zəhmət olmasa seçim edin!!");
        int whichOperation =  helpers.enterAndCheckSelectedOption(3);
        switch (whichOperation){
            case 1:{
                updateGenre();
                return 1;
            }
            case 2:{
                deleteGenre();
                return 2;
            }
            case 0:
                return 0;
            default:
                return 5;
        }
    }
}
