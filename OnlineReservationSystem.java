import java.sql.*;
import java.util.Scanner;
public class OnlineReservationSystem {
    public static Connection connect() {
        String url = "jdbc:mysql://localhost:3306/reservation_system";
        String username = "root";
        String password = "Mohdsaad@786";
        try {
            Connection con = DriverManager.getConnection(url, username, password);
            System.out.println(" Database connected successfully ");
            return con;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    //login system
    public static boolean login(Connection con) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter username : ");
        String inputusername = sc.nextLine();
        System.out.println("Enter your Password ");
        String inputpassword = sc.nextLine();
        try {
            String query = "SELECT * FROM users  WHERE username = ? AND password =?";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1, inputusername);
            pstmt.setString(2, inputpassword);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                System.out.println("Login Successful !");
                return true;
            } else {
                System.out.println(" Invalid .Try Again");
                return false;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    //Reservation System
    public static void ReservationTicket(Connection con) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter Train Number : ");
        String TrainNumber = sc.nextLine();
        System.out.println("Enter source station :");
        String source = sc.nextLine();
        System.out.println("Enter Destination Station: ");
        String Destination = sc.nextLine();
        System.out.print("Enter Travel Date (yyyy-MM-DD) : ");
        String date = sc.nextLine();
        try {
            String quere = "INSERT INTO reservation(train_number,Source,destination,date)VALUES(?,?,?,?)";
            PreparedStatement pstmt = con.prepareStatement(quere);
            pstmt.setString(1, TrainNumber);
            pstmt.setString(2, source);
            pstmt.setString(3, Destination);
            pstmt.setString(4, date);
            pstmt.executeUpdate();
            System.out.println("Reservation Successful ");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void showReservation(Connection con) {
        try {
            String query = "SELECT * FROM reservation";
            PreparedStatement pstmt = con.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();
            System.out.println("Current Reservations ");
            while (rs.next()) {
                System.out.println("ID :  " + rs.getInt("id") +"    Train Number :"+ rs.getString("train_number") + "     Source: " + rs.getString("Source")
                +"    Destination:  " + rs.getString("Destination")
                        + "   Date :  " + rs.getDate("date"));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    //Cancellation System
    public static void cancelTicket(Connection con) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter train number to cancel");
        String TrainNumber = sc.nextLine();
        try {
            String query = "SELECT * FROM reservation WHERE train_number =?";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1, TrainNumber);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                System.out.println("Reservation Found: ID: " + rs.getInt("id") +
                        " Train Number: " + rs.getString("train_number") +
                        " Source: " + rs.getString("Source") +
                        " Destination: " + rs.getString("Destination") +
                        " Date: " + rs.getDate("date"));

                System.out.println("DO YOU WANT TO CANCEL TICKET YES or NO");
                String CONFERMATION = sc.nextLine();
                if (CONFERMATION.equalsIgnoreCase("Yes")) {
                    String deletequery = "DELETE FROM reservation Where id=?";
                    PreparedStatement deletepstmt = con.prepareStatement(deletequery);
                    deletepstmt.setInt(1, rs.getInt("id"));
                    deletepstmt.executeUpdate();
                    System.out.println("Ticket Cancelled Successfully ");
                } else {
                    System.out.println("Cancellation Aborted.");
                }
            } else {
                System.out.println("Not reservation found Train number" + TrainNumber);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
        public static void main(String[] args) {
            Connection con = connect();
            if (con == null) {
                System.out.println("Exiting system due to database connection failure.");
                return;
            }
            System.out.println("Welcome to the Reservation System");
            if (!login(con)) {
                System.out.println("Exiting system.login Required");
                return;
            }
            Scanner sc = new Scanner(System.in);
            while(true){
            System.out.println("\nMenu");
            System.out.println("1.Reserve Ticket");
            System.out.println("2.Show Reservation");
            System.out.println("3.Cancel Ticket");
            System.out.println("4.Exit");
            int choice = sc.nextInt();
            switch (choice) {
                case 1:
                    ReservationTicket(con);
                    break;
                case 2:
                    showReservation(con);
                    break;
                case 3:
                    cancelTicket(con);
                    break;
                case 4:
                    System.out.println("Thank you for using the Reservation system");
                    return;
                default:
                    System.out.println("Invalid choice Try again");
            }
        }
    }

}