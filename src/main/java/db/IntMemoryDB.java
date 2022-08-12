package db;

import javafx.scene.control.Alert;
import redis.clients.jedis.Jedis;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Set;

public class IntMemoryDB {
    private static ArrayList<Book> bookDataBase = new ArrayList<>();
    private static ArrayList<User> userDataBase = new ArrayList<>();
    private static ArrayList<IssueBook> issueBookDataBase = new ArrayList<>();

    public void initialize(){

    }

    public static boolean addNewBook(Book newBook){
        if (findBook(newBook.getIsbnNumber())!=null) return false;
        bookDataBase.add(newBook);
        return true;
    }
    public static boolean addNewUser(User newUser){
        if (findUser(newUser.getNic())!=null) return false;
        userDataBase.add(newUser);
        return true;
    }
    public static void addNewIssueBook(IssueBook newIssueBook){
        issueBookDataBase.add(newIssueBook);
    }

    public static Book findBook(String isbnNumber){
        for (Book book : bookDataBase) {
            if (book.getIsbnNumber().equals(isbnNumber)) return book;
            //else return null;
        }
        return null;
    }
    public static User findUser(String nic){
        for (User user : userDataBase) {
            if (user.getNic().equalsIgnoreCase(nic)) return user;
        }
        return null;
    }
    public static IssueBook findIssueBook(String isbnNumber){
        for (IssueBook issueBook : issueBookDataBase) {
            if (issueBook.getISBN().equalsIgnoreCase(isbnNumber)) return issueBook;
        }
        return null;
    }

    public static void removeBook(String isbnNumber){
        Book book = findBook(isbnNumber);
        if (book!=null) bookDataBase.remove(book);
    }
    public static void removeUser(String nic){
        User user = findUser(nic);
        if (user!=null) userDataBase.remove(user);
    }
    public static void removeIssueBook(IssueBook issueBook){
        if (issueBook!=null) issueBookDataBase.remove(issueBook);
    }


    public static ArrayList<Book> getBookDataBase(){
        return bookDataBase;
    }
    public static ArrayList<User> getUserDataBase(){
        return userDataBase;
    }
    public static ArrayList<IssueBook> getIssueBookDataBase(){
        return issueBookDataBase;
    }

    public static boolean updateBook(int index,Book book){
        bookDataBase.set(index,book);
        return true;
    }
    public static boolean updateUser(int index,User user){
        userDataBase.set(index,user);
        return true;
    }

    public static ArrayList<Book> findBooks(String query){
        ArrayList<Book> searchedBooks = new ArrayList<>();
        for (Book book : bookDataBase) {
            if (book.getIsbnNumber().toLowerCase().contains(query.toLowerCase()) || book.getAuthor().toLowerCase().contains(query.toLowerCase()) || book.getName().toLowerCase().contains(query.toLowerCase())){
                searchedBooks.add(book);
            }
        }
        return searchedBooks;
    }
    public static ArrayList<User> findUsers(String query){
        ArrayList<User> searchedUsers = new ArrayList<>();
        for (User user : userDataBase) {
            if (user.getRegistrationNumber().toLowerCase().contains(query.toLowerCase()) || user.getFullName().toLowerCase().contains(query.toLowerCase()) || user.getNic().toLowerCase().contains(query.toLowerCase()) || user.getAddress().toLowerCase().contains(query.toLowerCase())){
                searchedUsers.add(user);
            }
        }
        return searchedUsers;
    }
    public static ArrayList<IssueBook> findIssueBooks(String query){
        ArrayList<IssueBook> issueBookList = new ArrayList<>();
        for (IssueBook issueBook : issueBookDataBase) {
            if (issueBook.getNic().toLowerCase().contains(query.toLowerCase())) {
                issueBookList.add(issueBook);
            }
        }
        return issueBookList;
    }

    public static boolean isMobileNumber(String input){
        if (input.length()!=11 ) return false;
        else if (!(input.charAt(3)=='-')) {
            return false;
        }
        else if (!input.substring(0,3).matches("\\d+") && !input.substring(4,11).matches("\\d+")) return false;
        return true;
    }

    public static boolean isNumber(String input){
        char[] chars = input.toCharArray();
        for (char aChar : chars) {
            if (!Character.isDigit(aChar)) return false;
        }
        return true;
    }
    public static boolean isName(String input){
        char[] chars = input.toCharArray();
        for (char aChar : chars) {
            if (!Character.isLetter(aChar) && aChar!=' ') return false;
        }
        return true;
    }
    public static boolean isEmpty(String input){
        if (input.isEmpty()){
            new Alert(Alert.AlertType.ERROR,"Text field can't be empty.").showAndWait();
            return true;
        }
        return false;
    }

    public static boolean isValidNIC(String input){
        if (input.length()!=10) return false;
        if (!(input.endsWith("v") || input.endsWith("V"))) return false;
        if (!input.substring(0,9).matches("\\d+")) return false;
        return true;
    }
}
