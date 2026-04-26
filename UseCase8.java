import java.util.*;

/**
 * ============================================================
 * SINGLE FILE - UseCase8
 * ============================================================
 *
 * Use Case 8: Booking History & Reporting
 *
 * @version 8.0
 */

// -------------------- Reservation --------------------
class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}

// -------------------- Booking History --------------------
class BookingHistory {

    // Stores confirmed reservations in order
    private List<Reservation> confirmedReservations;

    public BookingHistory() {
        confirmedReservations = new ArrayList<>();
    }

    // Add reservation to history
    public void addReservation(Reservation reservation) {
        confirmedReservations.add(reservation);
    }

    // Get all reservations
    public List<Reservation> getConfirmedReservations() {
        return confirmedReservations;
    }
}

// -------------------- Report Service --------------------
class BookingReportService {

    // Generate and print report
    public void generateReport(List<Reservation> reservations) {

        System.out.println("\nBooking History Report");

        for (Reservation r : reservations) {
            System.out.println(
                "Guest: " + r.getGuestName() +
                ", Room Type: " + r.getRoomType()
            );
        }
    }
}

// -------------------- MAIN CLASS --------------------
public class UseCase8 {

    public static void main(String[] args) {

        System.out.println("Booking History and Reporting");

        // Create history
        BookingHistory history = new BookingHistory();

        // Add confirmed bookings (same as previous UC output)
        history.addReservation(new Reservation("Abhi", "Single"));
        history.addReservation(new Reservation("Subha", "Double"));
        history.addReservation(new Reservation("Vanmathi", "Suite"));

        // Generate report
        BookingReportService reportService = new BookingReportService();
        reportService.generateReport(history.getConfirmedReservations());
    }
}