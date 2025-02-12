import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

class Train {
    final int trainNumber;
    String trainName;
    String sourceStation;
    String destinationStation;
    int totalSeats;
    int availableSeats;

    public Train(int trainNumber, String trainName, String sourceStation, String destinationStation, int totalSeats, int availableSeats) {
        this.trainNumber = trainNumber;
        this.trainName = trainName;
        this.sourceStation = sourceStation;
        this.destinationStation = destinationStation;
        this.totalSeats = totalSeats;
        this.availableSeats = availableSeats;
    }

    public void displayTrainInfo() {
        System.out.println("\nTrain Number: " + trainNumber);
        System.out.println("Train Name: " + trainName);
        System.out.println("Source Station: " + sourceStation);
        System.out.println("Destination Station: " + destinationStation);
        System.out.println("Total Seats: " + totalSeats);
        System.out.println("Available Seats: " + availableSeats);
    }

    public boolean checkAvailability() {
        return availableSeats > 0;
    }
}

// Station Class
class Station {
    private String stationName;
    private String stationCode;
    private ArrayList<Train> trains;

    public Station(String stationName, String stationCode) {
        this.stationName = stationName;
        this.stationCode = stationCode;
        trains = new ArrayList<>();
    }

    public void addTrain(Train tr) {
        trains.add(tr);
    }

    public void displayStationInfo() {
        System.out.println("\nStation Name: " + stationName);
        System.out.println("Station Code: " + stationCode);
        System.out.println("Trains Available: ");
        for (Train tr : trains) {
            tr.displayTrainInfo();
        }
    }

    public void searchTrains(String sourceStation, String destinationStation) {
        boolean found = false;
        System.out.println("\nQuerying trains from " + sourceStation + " to " + destinationStation);
        for (Train tr : trains) {
            if (tr.sourceStation.equalsIgnoreCase(sourceStation) && tr.destinationStation.equalsIgnoreCase(destinationStation)) {
                tr.displayTrainInfo();
                found = true;
            }
        }
        if (!found) {
            System.out.println("No Trains Found");
        }
    }
}

// Booking Class
class Booking {
    private int bookingID;
    private String passengerName;
    private Train tr;
    private int seatNo;

    public Booking(String passengerName, Train tr, int seatNo) {
        try {
            if (tr == null) {
                throw new IllegalArgumentException("Invalid train selected!");
            }
            if (!tr.checkAvailability()) {
                throw new Exception("Booking Failed! No seats available.");
            }
            if (seatNo <= 0 || seatNo > tr.totalSeats) {
                throw new IllegalArgumentException("Invalid seat number!");
            }

            this.bookingID = generateBookingID();
            this.passengerName = passengerName;
            this.tr = tr;
            this.seatNo = seatNo;
            tr.availableSeats--;

            System.out.println("Booking Successful! Booking ID: " + bookingID);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
            this.bookingID = -1;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            this.bookingID = -1;
        }
    }

    public int getBookingID() {
        return bookingID;
    }

    private int generateBookingID() {
        Random r = new Random();
        return 10000 + r.nextInt(90000);
    }

    public void displayBookingInfo() {
        if (bookingID != -1) {
            System.out.println("\nBooking ID: " + bookingID);
            System.out.println("Passenger Name: " + passengerName);
            System.out.println("Train Number: " + tr.trainNumber);
            System.out.println("Train Name: " + tr.trainName);
            System.out.println("Seat Number: " + seatNo);
        } else {
            System.out.println("Booking Failed! Seats Not Available");
        }
    }

    public void cancelBooking() {
        if (bookingID != -1) {
            tr.availableSeats++;
            System.out.println("Booking Cancelled Successfully");
            bookingID = -1;
        } else {
            System.out.println("No Booking found under the Booking ID: " + bookingID);
        }
    }
}

// TrainSearch Class (Fixed Naming Issue)
class TrainSearch {
    private List<Train> trains;

    public TrainSearch(List<Train> trains) {
        this.trains = trains;
    }

    public List<Train> searchTrains(String sourceStation, String destinationStation) {
        List<Train> result = new ArrayList<>();
        for (Train tr : trains) {
            if (tr.sourceStation.equalsIgnoreCase(sourceStation) && tr.destinationStation.equalsIgnoreCase(destinationStation)) {
                result.add(tr);
            }
        }
        return result;
    }
}

// BookingHistory Class (Fixed Naming Issue)
class BookingHistory {
    private List<Booking> book;

    public BookingHistory() {
        this.book = new ArrayList<>();
    }

    public List<Booking> getBookings() {
        return book;
    }

    public void addBooking(Booking b) {
        if (b != null && b.getBookingID() != -1) {
            book.add(b);
            System.out.println("Booking Added Successfully");
        } else {
            System.out.println("Booking Failed! Seats Not Available");
        }
    }

    public void displayBookingHistory() {
        if (book.isEmpty()) {
            System.out.println("No Bookings Found");
            return;
        }
        for (Booking b : book) {
            b.displayBookingInfo();
            System.out.println("-------------------------------");
        }
    }

    public void removeBooking(Booking b) {
        if (book.isEmpty()) {
            System.out.println("No Bookings Found");
        } else if (book.contains(b)) {
            book.remove(b);
            System.out.println("Booking Removed Successfully");
        } else {
            System.out.println("No Booking Found");
        }
    }
}

// Main Class
public class TrainFlow {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        Station chennai = new Station("Chennai Central", "MAS");
        Station bangalore = new Station("Bangalore City", "SBC");

        Train t1 = new Train(101, "Shatabdi Express", "Chennai Central", "Bangalore City", 100, 10);
        Train t2 = new Train(102, "Brindavan Express", "Chennai Central", "Bangalore City", 120, 5);

        chennai.addTrain(t1);
        chennai.addTrain(t2);
        bangalore.addTrain(t1);
        bangalore.addTrain(t2);

        BookingHistory bookingHistory = new BookingHistory();

        int choice;
        do {
            System.out.println("\n==== RAILWAY MANAGEMENT SYSTEM ====");
            System.out.println("1. Display Available Trains");
            System.out.println("2. Search for Trains");
            System.out.println("3. Book a Ticket");
            System.out.println("4. View Booking History");
            System.out.println("5. Cancel a Booking");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");
            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 -> chennai.displayStationInfo();
                case 2 -> {
                    System.out.print("\nEnter Source Station: ");
                    String src = sc.nextLine();
                    System.out.print("Enter Destination Station: ");
                    String dest = sc.nextLine();
                    chennai.searchTrains(src, dest);
                }
                case 3 -> {
                    System.out.print("\nEnter Your Name: ");
                    String name = sc.nextLine();
                    System.out.print("Enter Train Number: ");
                    int trainNo = sc.nextInt();
                    System.out.print("Enter Seat Number: ");
                    int seatNo = sc.nextInt();
                    sc.nextLine();
                    Booking booking = new Booking(name, t1, seatNo);
                    bookingHistory.addBooking(booking);
                }
                case 4 -> bookingHistory.displayBookingHistory();
                case 5 -> System.out.println("Feature Under Development!");
                case 6 -> System.out.println("Exiting...");
                default -> System.out.println("Invalid Choice! Try again.");
            }
        } while (choice != 6);

        sc.close();
    }
}
