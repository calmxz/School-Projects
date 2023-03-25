import java.util.Scanner;
import java.util.ArrayList;
import java.io.IOException;

public class DataStrucPrelimExam {
    public static Scanner input = new Scanner (System.in);
    public static ArrayList<String> infoName = new ArrayList<String> ();
    public static ArrayList<String> infoAddress = new ArrayList<String> ();
    public static ArrayList<String> infoCourse = new ArrayList<String> ();
    public static String name;
    public static String address;
    public static String course;

    //displays the menu options and allows the user to choose which option he/she wants to use/do
    static void menu() throws IOException, InterruptedException{
        System.out.println("==========MENU==========");
        System.out.println("(1) Add Information");
        System.out.println("(2) Update Information");
        System.out.println("(3) Read Information");
        System.out.println("(4) Remove Information");
        System.out.println("(5) Terminate Program");
        int menuNum = 0;

    //tries the line of code, and prevents the program from terminating from an error
        try {
            System.out.print("Enter a number: ");
            menuNum = input.nextInt();
        } catch (Exception e) {
            input.nextLine();
        }

        //if user input matches, it runs the corresponding method, else, it will return to the menu
        if (menuNum == 1){
            add();
        }else if(menuNum == 2){
            //clears the console
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            update();
        }else if(menuNum == 3){
            read();
        }else if(menuNum == 4){
            //clears the console
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            remove();
        }else if(menuNum == 5){
            terminate();
        }else{
            System.out.println("Invalid option. Try again.");
            System.out.println();
            menu();
        }
    }
    
    //add information menu
    static void add() throws IOException, InterruptedException{
        //clears the console
        new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        System.out.println("==========ADD INFORMATION==========");

        //lets the user insert a name
        System.out.print("Name: ");
        input.nextLine();
        String name = input.nextLine();
        infoName.add(name);
        
        //lets the user insert an address
        System.out.print("Address: ");
        String address = input.nextLine();
        infoAddress.add(address);

        //lets the user insert a course
        System.out.print("Course: ");
        String course = input.nextLine();
        infoCourse.add(course);
        
        System.out.println("\nProfile " + (infoName.size())+  " successfully added.");

        char addChoice;

        //lets the user choose if he/she wants to add another profile
        System.out.println();
        System.out.print("Would you like to add another profile? (Y/N): ");
        addChoice = input.next().charAt(0);

        //if yes, lets the user return to the add menu to add another profile
        if (addChoice == 'Y' || addChoice == 'y'){
            System.out.println();
            add();
        //if no, the program will display all information added, and will return the user to the menu
        }else if (addChoice == 'N' || addChoice == 'n'){
            System.out.println();
            for (int i = 0; i < infoName.size(); i++){
                System.out.println("Profile " + (i + 1) + ": ");
                System.out.println("Name: " + infoName.get(i));
                System.out.println("Address: " + infoAddress.get(i));
                System.out.println("Course: " + infoCourse.get(i));
                System.out.println();
            }
        //calls the menu method
            menu();
        //if the user inputs another option besides Y or N, the user will be returned to the menu
        }else{
            System.out.println();
            System.out.println("Invalid option. Returning to menu.");
            menu();
        }
    }
    //update information menu
    static void update() throws IOException, InterruptedException{
        System.out.println("==========UPDATE INFORMATION==========");
       
        //shows the user all profiles
        for (int i = 0; i < infoName.size(); i++){
            System.out.println("Profile " + (i + 1) + ": ");
            System.out.println("Name: " + infoName.get(i));
            System.out.println("Address: " + infoAddress.get(i));
            System.out.println("Course: " + infoCourse.get(i));
            System.out.println();
        }

        //if no profiles exists, the user will be returned to the menu
        if (infoName.size() == 0){
            System.out.println("No profiles to update. Returning to menu.");
            menu();
        }

        int updateNum = 0;

        //asks the user which profile he/she wants to updates
        try {
            System.out.print("Which profile would you like to update?: ");
            updateNum = input.nextInt();
            input.nextLine();
        } catch (Exception e) {
            System.out.println("Error. Please try again.");
            input.nextLine();
        }

        updateNum -= 1;

        //checks if updateNum is greater than 0 AND updateNum is less than the size of the arraylist
        //if the condition is met, it will let the user update the profile he/she chose
            if (updateNum >= 0 && updateNum < infoName.size()){
                System.out.print("Name: ");
                name = input.nextLine();
                infoName.set(updateNum, name);
    
                System.out.print("Address: ");
                address = input.nextLine();
                infoAddress.set(updateNum, address);
    
                System.out.print("Course: ");
                course = input.nextLine();
                infoCourse.set(updateNum, course);

                System.out.println("\nProfile " + (updateNum + 1)+  " successfully updated. Returning to menu.");
                System.out.println();
                //shows the user all profiles and the updated profile
                for (int i = 0; i < infoName.size(); i++){
                    System.out.println("Profile " + (i + 1) + ": ");
                    System.out.println("Name: " + infoName.get(i));
                    System.out.println("Address: " + infoAddress.get(i));
                    System.out.println("Course: " + infoCourse.get(i));
                    System.out.println();
                }
                //calls the menu method
                menu();
            //if the user inputs a profile that does not exist, the program lets the user retry again
            }else{
                System.out.println("Profile not found. Please try again.");
                update();
        }
    }

    //read information option
    static void read() throws IOException, InterruptedException{
        //clears the console
        new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        System.out.println("==========READ INFORMATION==========");

        //if no profiles exists, the program returns the user to the menu
        if (infoName.size() == 0){
            System.out.println("No profiles to read. Returning to menu.");
            menu();
        }

        //reads all profile
        for (int i = 0; i < infoName.size(); i++){
            System.out.println("Profile " + (i + 1) + ": ");
            System.out.println("Name: " + infoName.get(i));
            System.out.println("Address: " + infoAddress.get(i));
            System.out.println("Course: " + infoCourse.get(i));
            System.out.println();
        }
        //after reading the profiles, user can return to the menu by pressing enter.
        try {
            System.out.print("Press enter to return to menu.");
            input.nextLine();
            input.nextLine();
            menu();
        } catch (Exception e) {
        }
    }

    //remove menu option
    static void remove() throws IOException, InterruptedException{
        System.out.println("==========REMOVE INFORMATION==========");

        //if no profiles exists, the user will be returned to the menu
        if (infoName.size() == 0){
            System.out.println("No profiles to remove. Returning to menu.");
            menu();
        }

        //displays the profiles
        for (int i = 0; i < infoName.size(); i++){
            System.out.println("Profile " + (i + 1) + ": ");
            System.out.println("Name: " + infoName.get(i));
            System.out.println("Address: " + infoAddress.get(i));
            System.out.println("Course: " + infoCourse.get(i));
            System.out.println();
        }

        int removeNum = 0;
        //asks the user which profile to remove
        try {
            System.out.print("Which profile would you like to remove?: ");
            removeNum = input.nextInt();
            System.out.println();
        } catch (Exception e) {
            System.out.println("Error. Please try again.");
        }
        
        removeNum -= 1;

        //if user enters correctly, all information in the profile is removed
        if (removeNum >= 0 && removeNum < infoName.size()){
            infoName.remove(removeNum);
            infoAddress.remove(removeNum);
            infoCourse.remove(removeNum);
            //displays the remaining profiles
            for (int i = 0; i < infoName.size(); i++){
                System.out.println("Profile " + (i + 1) + ": ");
                System.out.println("Name: " + infoName.get(i));
                System.out.println("Address: " + infoAddress.get(i));
                System.out.println("Course: " + infoCourse.get(i));
                System.out.println();
            }
            System.out.println("Profile " + (removeNum + 1)+  " successfully deleted. Returning to menu.");
            System.out.println();
        //if the user's input is invalid, the program lets the user try again
        }else{
            System.out.println("Profile not found. Please try again.");
            input.nextLine();
            remove();
        }
        //calls the menu method
        menu();
    } 

    //terminate program menu option
    static void terminate() throws InterruptedException, IOException{
        //clears the console
        new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        //exits from the program
        System.exit(0);
    }

    //clears the console screen
    public static void main(String[] args) throws IOException, InterruptedException {
        //clears the console
        new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        //calls the menu method
        menu();
        input.close();
    }
}
