package Ui;

import Model.User;
import Service.AuthService;

import java.util.Scanner;

public class MainMenu {

    public static void main(String[] args) {

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
                    newUser.setPasswordHint(hint);   // ✅ IMPORTANT

                    if (authService.register(newUser)) {
                        System.out.println("✅ Registered Successfully as " + role);
                    } else {
                        System.out.println("❌ Registration Failed!");
                    }
                    break;

                // ================= LOGIN =================
                case 3:
                    System.out.print("Enter Email: ");
                    String loginEmail = sc.nextLine();

                    System.out.print("Enter Password: ");
                    String loginPassword = sc.nextLine();

                    User user = authService.login(loginEmail, loginPassword);

                    if (user != null) {
                        System.out.println("✅ Login Successful! Welcome " + user.getName());
                        System.out.println("Your Role: " + user.getRole());

                        if (user.getRole().equalsIgnoreCase("BUYER")) {
                            BuyerMenu.show(user);
                        } else {
                            SellerMenu.show(user);
                        }

                    } else {
                        System.out.println("❌ Invalid Email or Password!");
                    }
                    break;

                // ================= FORGOT PASSWORD =================
                case 4:
                    System.out.println("\n===== FORGOT PASSWORD =====");

                    System.out.print("Enter registered email: ");
                    String fpEmail = sc.nextLine();

                    System.out.print("Enter password hint: ");
                    String fpHint = sc.nextLine();

                    System.out.print("Enter new password: ");
                    String newPassword = sc.nextLine();

                    boolean reset = authService.forgotPassword(fpEmail, fpHint, newPassword);

                    if (reset) {
                        System.out.println("✅ Password reset successful! Please login.");
                    } else {
                        System.out.println("❌ Password reset failed! Check email or hint.");
                    }
                    break;

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
