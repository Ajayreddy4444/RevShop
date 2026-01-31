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

            System.out.println("\n===== REVSHOP =======");
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

                String name = null;
                String email = null;
                String password = null;
                String hint = null;

                String role = (choice == 1) ? "BUYER" : "SELLER";

                // ---------- NAME ----------
                while (true) {
                    try {
                        System.out.print("Enter Name: ");
                        name = sc.nextLine();

                        if (name == null || name.trim().isEmpty()) {
                            throw new ValidationException("Name cannot be empty");
                        }
                        break; // valid
                    } catch (ValidationException e) {
                        System.out.println("❌ " + e.getMessage());
                    }
                }

                // ---------- EMAIL ----------
                while (true) {
                    try {
                        System.out.print("Enter Email: ");
                        email = sc.nextLine();

                        // validate only email using service logic
                        authService.login(email, "dummy123"); 
                        // we EXPECT password error here, email validated already

                    } catch (ValidationException e) {
                        System.out.println("❌ " + e.getMessage());
                        continue;
                    } catch (InvalidCredentialsException e) {
                        // expected → email format passed
                        break;
                    }
                }

                // ---------- PASSWORD ----------
                while (true) {
                    try {
                        System.out.print("Enter Password: ");
                        password = sc.nextLine();

                        if (password == null || password.length() < 4) {
                            throw new ValidationException(
                                    "Password must be at least 4 characters"
                            );
                        }
                        break;
                    } catch (ValidationException e) {
                        System.out.println("❌ " + e.getMessage());
                    }
                }

                // ---------- PASSWORD HINT ----------
                while (true) {
                    try {
                        System.out.print("Enter Password Hint: ");
                        hint = sc.nextLine();

                        if (hint == null || hint.trim().isEmpty()) {
                            throw new ValidationException("Password hint is required");
                        }

                        if (hint.equals(password)) {
                            throw new ValidationException(
                                    "Password and hint cannot be same"
                            );
                        }
                        break;
                    } catch (ValidationException e) {
                        System.out.println("❌ " + e.getMessage());
                    }
                }

                // ---------- FINAL REGISTER ----------
                try {
                    User user = new User(name, email, password, role);
                    user.setPasswordHint(hint);

                    authService.register(user);
                    System.out.println("✅ Registered Successfully as " + role);

                } catch (AuthException e) {
                    System.out.println("❌ " + e.getMessage());
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
            case 4:
                try {
                    System.out.print("Enter Registered Email: ");
                    String emailid = sc.nextLine();

                    System.out.print("Enter Password Hint: ");
                    String yourhint = sc.nextLine();

                    System.out.print("Enter New Password: ");
                    String newPassword = sc.nextLine();

                    authService.forgotPassword(emailid, yourhint, newPassword);

                    System.out.println("✅ Password reset successful!");

                } catch (ValidationException v) {
                    System.out.println("❌ " + v.getMessage());
                }
                catch (InvalidCredentialsException i) {
                    System.out.println("❌ " + i.getMessage());
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
