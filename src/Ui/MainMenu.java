package Ui;

import Exception.AuthException;
import Exception.InvalidCredentialsException;
import Exception.ValidationException;
import Model.User;
import Service.AuthService;

import java.sql.SQLException;
import java.util.Scanner;

public class MainMenu {

    public static void main(String[] args)  {

        Scanner sc = new Scanner(System.in);
        AuthService authService = new AuthService();

        while (true) {

            System.out.println("\n========== REVSHOP ==========");
            System.out.println("1. Register as Buyer");
            System.out.println("2. Register as Seller");
            System.out.println("3. Login");
            System.out.println("4. Forgot Password");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

            int choice = sc.nextInt();
            sc.nextLine(); // consume newline

            switch (choice) {

                // ================= REGISTER =================
            case 1:
            case 2:
                try {
                    System.out.print("Enter Name: ");
                    String name = sc.nextLine();

                    System.out.print("Enter Email: ");
                    String email = sc.nextLine();

                    System.out.print("Enter Password: ");
                    String password = sc.nextLine();

                    System.out.print("Enter Password Hint (used for recovery): ");
                    String hint = sc.nextLine();

                    String role = (choice == 1) ? "BUYER" : "SELLER";

                    User newUser = new User(name, email, password, role);
                    newUser.setPasswordHint(hint);

                    authService.register(newUser);   // ✅ no if-condition

                    System.out.println("✅ Registered Successfully as " + role);

                } catch (ValidationException ve) {
                    System.out.println("❌ " + ve.getMessage());
                } catch (AuthException ae) {
                    System.out.println("❌ " + ae.getMessage());
                }
                break;


                // ================= LOGIN =================
            case 3:
                try {
                    System.out.print("Enter Email: ");
                    String loginEmail = sc.nextLine();

                    System.out.print("Enter Password: ");
                    String loginPassword = sc.nextLine();

                    User user = authService.login(loginEmail, loginPassword);

                    System.out.println("✅ Login Successful! Welcome " + user.getName());

                    if (user.getRole().equalsIgnoreCase("BUYER")) {
                        BuyerMenu.show(user);
                    } else {
                        SellerMenu.show(user);
                    }

                } catch (ValidationException ve) {
                    System.out.println("❌ " + ve.getMessage());
                } catch (InvalidCredentialsException ice) {
                    System.out.println("❌ " + ice.getMessage());
                } catch (AuthException ae) {
                    System.out.println("❌ " + ae.getMessage());
                }
                break;


                // ================= FORGOT PASSWORD =================
            

                // ================= EXIT =================
                case 5:
                    System.out.println("Thank you for using RevShop!");
                    sc.close();
                    return;

                default:
                    System.out.println("❌ Invalid choice! Try again.");
            }
        }
    }
}
