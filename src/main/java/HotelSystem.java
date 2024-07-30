import java.io.*;
import java.util.*;

public class HotelSystem {
    private List<Guest> guests = new ArrayList<>();
    private List<Clerk> clerks = new ArrayList<>();
    private Admin admin = new Admin();
    private List<Room> rooms = new ArrayList<>();
    private List<Reservation> reservations = new ArrayList<>();
    private static final String GUESTS_FILE = "guests.txt";
    private static final String CLERKS_FILE = "clerks.txt";

    public static void main(String[] args) {
        HotelSystem hotelSystem = new HotelSystem();
        hotelSystem.loadAccounts();
        hotelSystem.run();
    }

    private void run() {
        // Initialize some rooms for the example
        rooms.add(new Room(101, "Executive", "King", false));
        rooms.add(new Room(102, "Business", "Queen", false));
        rooms.add(new Room(103, "Comfort", "Full", true));
        rooms.add(new Room(104, "Economy", "Twin", true));

        // Add some sample data
        if (clerks.isEmpty() && guests.isEmpty()) {
            admin.createClerkAccount("clerk1", "password1", clerks);
            admin.createGuestAccount("guest1", "password1", guests);
            saveAccounts();
        }

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("1. Create Guest Account");
            System.out.println("2. Guest Login");
            System.out.println("3. Clerk Login");
            System.out.println("4. Admin Login");
            System.out.println("5. Exit");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    createGuestAccount(scanner);
                    break;
                case 2:
                    guestLogin(scanner);
                    break;
                case 3:
                    clerkLogin(scanner);
                    break;
                case 4:
                    adminLogin(scanner);
                    break;
                case 5:
                    saveAccounts();
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private void createGuestAccount(Scanner scanner) {
        System.out.println("Enter new guest username:");
        String newUsername = scanner.nextLine();
        System.out.println("Enter new guest password:");
        String newPassword = scanner.nextLine();
        admin.createGuestAccount(newUsername, newPassword, guests);
        saveAccounts();
        System.out.println("Guest account created.");
    }

    private void guestLogin(Scanner scanner) {
        System.out.println("Enter username:");
        String username = scanner.nextLine();
        System.out.println("Enter password:");
        String password = scanner.nextLine();

        Guest guest = authenticateGuest(username, password);
        if (guest != null) {
            System.out.println("Welcome, " + username);
            guestMenu(scanner, guest);
        } else {
            System.out.println("Invalid credentials.");
        }
    }

    private void guestMenu(Scanner scanner, Guest guest) {
        while (true) {
            System.out.println("1. Modify Account");
            System.out.println("2. Search Rooms");
            System.out.println("3. Make a Reservation");
            System.out.println("4. Change a Reservation");
            System.out.println("5. Logout");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    guest.modifyAccount(scanner, guests);
                    saveAccounts();
                    break;
                case 2:
                    searchRooms();
                    break;
                case 3:
                    guest.makeReservation(scanner, rooms, reservations);
                    break;
                case 4:
                    guest.changeReservation(scanner, reservations);
                    break;
                case 5:
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private void clerkLogin(Scanner scanner) {
        System.out.println("Enter username:");
        String username = scanner.nextLine();
        System.out.println("Enter password:");
        String password = scanner.nextLine();

        Clerk clerk = authenticateClerk(username, password);
        if (clerk != null) {
            System.out.println("Welcome, " + username);
            clerkMenu(scanner, clerk);
        } else {
            System.out.println("Invalid credentials.");
        }
    }

    private void clerkMenu(Scanner scanner, Clerk clerk) {
        while (true) {
            System.out.println("1. Modify Profile");
            System.out.println("2. Enter/Modify Room Info");
            System.out.println("3. Logout");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    clerk.modifyProfile(scanner, clerks);
                    saveAccounts();
                    break;
                case 2:
                    clerk.modifyRoomInfo(scanner, rooms);
                    break;
                case 3:
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private void adminLogin(Scanner scanner) {
        System.out.println("Enter admin username:");
        String username = scanner.nextLine();
        System.out.println("Enter admin password:");
        String password = scanner.nextLine();

        if (admin.authenticate(username, password)) {
            System.out.println("Welcome, Admin");
            adminMenu(scanner);
        } else {
            System.out.println("Invalid credentials.");
        }
    }

    private void adminMenu(Scanner scanner) {
        while (true) {
            System.out.println("1. Create Clerk Account");
            System.out.println("2. Create Guest Account");
            System.out.println("3. Logout");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    admin.createClerkAccount(scanner, clerks);
                    saveAccounts();
                    break;
                case 2:
                    admin.createGuestAccount(scanner, guests);
                    saveAccounts();
                    break;
                case 3:
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private Guest authenticateGuest(String username, String password) {
        for (Guest guest : guests) {
            if (guest.getUsername().equals(username) && guest.getPassword().equals(password)) {
                return guest;
            }
        }
        return null;
    }

    private Clerk authenticateClerk(String username, String password) {
        for (Clerk clerk : clerks) {
            if (clerk.getUsername().equals(username) && clerk.getPassword().equals(password)) {
                return clerk;
            }
        }
        return null;
    }

    private void searchRooms() {
        System.out.println("Available rooms:");
        for (Room room : rooms) {
            System.out.println(room);
        }
    }

    private void loadAccounts() {
        loadGuests();
        loadClerks();
    }

    private void saveAccounts() {
        saveGuests();
        saveClerks();
    }

    private void loadGuests() {
        try (BufferedReader br = new BufferedReader(new FileReader(GUESTS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    guests.add(new Guest(parts[0], parts[1]));
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading guest accounts.");
        }
    }

    private void saveGuests() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(GUESTS_FILE))) {
            for (Guest guest : guests) {
                bw.write(guest.getUsername() + "," + guest.getPassword());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving guest accounts.");
        }
    }

    private void loadClerks() {
        try (BufferedReader br = new BufferedReader(new FileReader(CLERKS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    clerks.add(new Clerk(parts[0], parts[1]));
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading clerk accounts.");
        }
    }

    private void saveClerks() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CLERKS_FILE))) {
            for (Clerk clerk : clerks) {
                bw.write(clerk.getUsername() + "," + clerk.getPassword());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving clerk accounts.");
        }
    }

    // Inner classes for Guest, Clerk, Admin, Room, and Reservation

    static class Guest {
        private String username;
        private String password;
        private String name;
        private String address;
        private String creditCardNumber;
        private String creditCardExpiry;

        public Guest(String username, String password) {
            this.username = username;
            this.password = password;
        }

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }

        public void modifyAccount(Scanner scanner, List<Guest> guests) {
            System.out.println("Enter new username:");
            String newUsername = scanner.nextLine();
            System.out.println("Enter new password:");
            String newPassword = scanner.nextLine();
            this.username = newUsername;
            this.password = newPassword;
            System.out.println("Enter new name:");
            this.name = scanner.nextLine();
            System.out.println("Enter new address:");
            this.address = scanner.nextLine();
            System.out.println("Enter new credit card number:");
            this.creditCardNumber = scanner.nextLine();
            System.out.println("Enter new credit card expiry:");
            this.creditCardExpiry = scanner.nextLine();
            System.out.println("Account updated.");
        }

        public void makeReservation(Scanner scanner, List<Room> rooms, List<Reservation> reservations) {
            System.out.println("Enter room number:");
            int roomNumber = scanner.nextInt();
            scanner.nextLine(); // consume newline
            System.out.println("Enter start date (YYYY-MM-DD):");
            String startDate = scanner.nextLine();
            System.out.println("Enter end date (YYYY-MM-DD):");
            String endDate = scanner.nextLine();

            Room room = null;
            for (Room r : rooms) {
                if (r.getNumber() == roomNumber) {
                    room = r;
                    break;
                }
            }

            if (room != null) {
                Reservation reservation = new Reservation(this, room, startDate, endDate);
                reservations.add(reservation);
                System.out.println("Reservation made.");
            } else {
                System.out.println("Room not found.");
            }
        }

        public void changeReservation(Scanner scanner, List<Reservation> reservations) {
            System.out.println("Enter reservation ID:");
            int reservationId = scanner.nextInt();
            scanner.nextLine(); // consume newline

            for (Reservation reservation : reservations) {
                if (reservation.getId() == reservationId && reservation.getGuest().equals(this)) {
                    System.out.println("Enter new start date (YYYY-MM-DD):");
                    String newStartDate = scanner.nextLine();
                    System.out.println("Enter new end date (YYYY-MM-DD):");
                    String newEndDate = scanner.nextLine();
                    reservation.setDates(newStartDate, newEndDate);
                    System.out.println("Reservation updated.");
                    return;
                }
            }
            System.out.println("Reservation not found.");
        }
    }

    static class Clerk {
        private String username;
        private String password;
        private String profileInfo;

        public Clerk(String username, String password) {
            this.username = username;
            this.password = password;
        }

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }

        public void modifyProfile(Scanner scanner, List<Clerk> clerks) {
            System.out.println("Enter new username:");
            String newUsername = scanner.nextLine();
            System.out.println("Enter new password:");
            String newPassword = scanner.nextLine();
            this.username = newUsername;
            this.password = newPassword;
            System.out.println("Enter new profile information:");
            this.profileInfo = scanner.nextLine();
            System.out.println("Profile updated.");
        }

        public void modifyRoomInfo(Scanner scanner, List<Room> rooms) {
            System.out.println("Enter room number to modify:");
            int roomNumber = scanner.nextInt();
            scanner.nextLine(); // consume newline

            for (Room room : rooms) {
                if (room.getNumber() == roomNumber) {
                    System.out.println("Enter new room type:");
                    room.setType(scanner.nextLine());
                    System.out.println("Enter new bed type:");
                    room.setBedType(scanner.nextLine());
                    System.out.println("Enter smoking status (true/false):");
                    room.setSmoking(scanner.nextBoolean());
                    scanner.nextLine(); // consume newline
                    System.out.println("Room updated.");
                    return;
                }
            }
            System.out.println("Room not found.");
        }
    }

    static class Admin {
        private String username = "admin";
        private String password = "admin123";

        public boolean authenticate(String username, String password) {
            return this.username.equals(username) && this.password.equals(password);
        }

        public void createClerkAccount(Scanner scanner, List<Clerk> clerks) {
            System.out.println("Enter new clerk username:");
            String newUsername = scanner.nextLine();
            System.out.println("Enter new clerk password:");
            String newPassword = scanner.nextLine();
            Clerk newClerk = new Clerk(newUsername, newPassword);
            clerks.add(newClerk);
            System.out.println("Clerk account created.");
        }

        public void createGuestAccount(Scanner scanner, List<Guest> guests) {
            System.out.println("Enter new guest username:");
            String newUsername = scanner.nextLine();
            System.out.println("Enter new guest password:");
            String newPassword = scanner.nextLine();
            Guest newGuest = new Guest(newUsername, newPassword);
            guests.add(newGuest);
            System.out.println("Guest account created.");
        }

        public void createClerkAccount(String username, String password, List<Clerk> clerks) {
            Clerk newClerk = new Clerk(username, password);
            clerks.add(newClerk);
            System.out.println("Clerk account created.");
        }

        public void createGuestAccount(String username, String password, List<Guest> guests) {
            Guest newGuest = new Guest(username, password);
            guests.add(newGuest);
            System.out.println("Guest account created.");
        }
    }

    static class Room {
        private int number;
        private String type;
        private String bedType;
        private boolean smoking;

        public Room(int number, String type, String bedType, boolean smoking) {
            this.number = number;
            this.type = type;
            this.bedType = bedType;
            this.smoking = smoking;
        }

        public int getNumber() {
            return number;
        }

        public void setType(String type) {
            this.type = type;
        }

        public void setBedType(String bedType) {
            this.bedType = bedType;
        }

        public void setSmoking(boolean smoking) {
            this.smoking = smoking;
        }

        @Override
        public String toString() {
            return "Room{" +
                    "number=" + number +
                    ", type='" + type + '\'' +
                    ", bedType='" + bedType + '\'' +
                    ", smoking=" + smoking +
                    '}';
        }
    }

    static class Reservation {
        private static int counter = 1;
        private int id;
        private Guest guest;
        private Room room;
        private String startDate;
        private String endDate;

        public Reservation(Guest guest, Room room, String startDate, String endDate) {
            this.id = counter++;
            this.guest = guest;
            this.room = room;
            this.startDate = startDate;
            this.endDate = endDate;
        }

        public int getId() {
            return id;
        }

        public Guest getGuest() {
            return guest;
        }

        public void setDates(String startDate, String endDate) {
            this.startDate = startDate;
            this.endDate = endDate;
        }
    }
}
