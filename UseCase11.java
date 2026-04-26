import java.util.*;

// ===============================
// Main Class
// ===============================
public class UseCase11 {

    public static void main(String[] args) {

        // Shared resources
        BookingRequestQueue queue = new BookingRequestQueue();
        RoomInventory inventory = new RoomInventory();
        RoomAllocationService service = new RoomAllocationService(inventory);

        // Add booking requests
        queue.addRequest(new BookingRequest("Abhi", "Single"));
        queue.addRequest(new BookingRequest("Vanmathi", "Double"));
        queue.addRequest(new BookingRequest("Kural", "Suite"));
        queue.addRequest(new BookingRequest("Subha", "Single"));

        // Create threads
        Thread t1 = new Thread(new ConcurrentBookingProcessor(queue, service));
        Thread t2 = new Thread(new ConcurrentBookingProcessor(queue, service));

        // Start threads
        t1.start();
        t2.start();

        // Wait for completion
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            System.out.println("Thread interrupted");
        }

        // Print remaining inventory
        inventory.printInventory();
    }
}

// ===============================
// Booking Request
// ===============================
class BookingRequest {
    String guestName;
    String roomType;

    public BookingRequest(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }
}

// ===============================
// Thread-Safe Queue
// ===============================
class BookingRequestQueue {

    private Queue<BookingRequest> queue = new LinkedList<>();

    public synchronized void addRequest(BookingRequest request) {
        queue.add(request);
    }

    public synchronized BookingRequest getRequest() {
        return queue.poll();
    }
}

// ===============================
// Inventory (THREAD SAFE)
// ===============================
class RoomInventory {

    private Map<String, Integer> rooms = new HashMap<>();

    public RoomInventory() {
        rooms.put("Single", 4);
        rooms.put("Double", 2);
        rooms.put("Suite", 1);
    }

    public synchronized boolean bookRoom(String type) {
        int count = rooms.getOrDefault(type, 0);
        if (count > 0) {
            rooms.put(type, count - 1);
            return true;
        }
        return false;
    }

    public void printInventory() {
        System.out.println("\nRemaining Inventory:");
        for (String type : rooms.keySet()) {
            System.out.println(type + ": " + rooms.get(type));
        }
    }
}

// ===============================
// Allocation Service
// ===============================
class RoomAllocationService {

    private RoomInventory inventory;

    public RoomAllocationService(RoomInventory inventory) {
        this.inventory = inventory;
    }

    public void processBooking(BookingRequest request) {

        synchronized (inventory) { // Critical section

            boolean success = inventory.bookRoom(request.roomType);

            if (success) {
                System.out.println("Booking confirmed for Guest: "
                        + request.guestName + ", Room: " + request.roomType);
            } else {
                System.out.println("Booking FAILED for Guest: "
                        + request.guestName + " (No rooms)");
            }
        }
    }
}

// ===============================
// THREAD CLASS
// ===============================
class ConcurrentBookingProcessor implements Runnable {

    private BookingRequestQueue queue;
    private RoomAllocationService service;

    public ConcurrentBookingProcessor(BookingRequestQueue queue,
                                      RoomAllocationService service) {
        this.queue = queue;
        this.service = service;
    }

    @Override
    public void run() {

        while (true) {

            BookingRequest request;

            synchronized (queue) {
                request = queue.getRequest();
            }

            if (request == null) break;

            service.processBooking(request);

            try {
                Thread.sleep(100); // simulate delay
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}       