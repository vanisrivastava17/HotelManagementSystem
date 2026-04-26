import java.util.*;

// ===============================
// Custom Exception
// ===============================
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

// ===============================
// Room Inventory Class
// ===============================
class RoomInventory {
    private Map<String, Integer> rooms;

    public RoomInventory() {
        rooms = new HashMap<>();
        rooms.put("Single", 2);
        rooms.put("Double", 2);
        rooms.put("Suite", 1);
    }

    public boolean isRoomAvailable(String type) {
        return rooms.containsKey(type) && rooms.get(type) > 0;
    }

    public void bookRoom(String type) {
        rooms.put(type, rooms.get(type) - 1);
    }

    public Set<String> getAvailableRoomTypes() {
        return rooms.keySet();
    }
}

// ===============================
// Validator Class
// ===============================
class ReservationValidator {

    public void validate(String name, String roomType, RoomInventory inventory)
            throws InvalidBookingException {

        // Name validation
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidBookingException("Guest name cannot be empty.");
        }

        // Room type validation
        if (!inventory.getAvailableRoomTypes().contains(roomType)) {
            throw new InvalidBookingException("Invalid room type selected.");
        }

        // Availability validation
        if (!inventory.isRoomAvailable(roomType)) {
            throw new InvalidBookingException("Selected room is not available.");
        }
    }
}

// ===============================
// Booking Queue (for concept)
// ===============================
class BookingRequestQueue {
    private Queue<String> queue = new LinkedList<>();

    public void addRequest(String request) {
        queue.add(request);
    }

    public String processRequest() {
        return queue.poll();
    }
}

// ===============================
// MAIN CLASS (ENTRY POINT)
// ===============================
public class UseCase9 {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        RoomInventory inventory = new RoomInventory();
        ReservationValidator validator = new ReservationValidator();
        BookingRequestQueue queue = new BookingRequestQueue();

        try {
            System.out.println("=== Booking Validation ===");

            System.out.print("Enter guest name: ");
            String name = scanner.nextLine();

            System.out.print("Enter room type (Single/Double/Suite): ");
            String roomType = scanner.nextLine();

            // Validate input
            validator.validate(name, roomType, inventory);

            // Add to queue
            queue.addRequest(name + " -> " + roomType);

            // Process booking
            String request = queue.processRequest();
            inventory.bookRoom(roomType);

            System.out.println("Booking successful for: " + request);

        } catch (InvalidBookingException e) {
            System.out.println("Booking failed: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
}