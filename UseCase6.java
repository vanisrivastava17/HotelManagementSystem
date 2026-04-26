import java.util.*;

/**
 * ============================================================
 * SINGLE FILE - UseCase6
 * ============================================================
 *
 * Use Case 6: Reservation Confirmation & Room Allocation
 *
 * @version 6.0
 */

// -------------------- Reservation --------------------
class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() { return guestName; }
    public String getRoomType() { return roomType; }
}

// -------------------- Booking Queue --------------------
class BookingRequestQueue {
    private Queue<Reservation> queue;

    public BookingRequestQueue() {
        queue = new LinkedList<>();
    }

    public void addRequest(Reservation r) {
        queue.offer(r);
    }

    public Reservation getNextRequest() {
        return queue.poll();
    }

    public boolean hasPendingRequests() {
        return !queue.isEmpty();
    }
}

// -------------------- Inventory --------------------
class RoomInventory {
    private Map<String, Integer> availability;

    public RoomInventory() {
        availability = new HashMap<>();
        availability.put("Single", 2);
        availability.put("Double", 1);
        availability.put("Suite", 1);
    }

    public boolean isAvailable(String type) {
        return availability.getOrDefault(type, 0) > 0;
    }

    public void reduce(String type) {
        availability.put(type, availability.get(type) - 1);
    }
}

// -------------------- Allocation Service --------------------
class RoomAllocationService {

    private Set<String> allocatedRoomIds;
    private Map<String, Set<String>> assignedRoomsByType;

    public RoomAllocationService() {
        allocatedRoomIds = new HashSet<>();
        assignedRoomsByType = new HashMap<>();
    }

    public String allocateRoom(String roomType) {

        int count = 1;
        String roomId;

        do {
            roomId = roomType + "-" + count++;
        } while (allocatedRoomIds.contains(roomId));

        allocatedRoomIds.add(roomId);

        assignedRoomsByType
                .computeIfAbsent(roomType, k -> new HashSet<>())
                .add(roomId);

        return roomId;
    }
}

// -------------------- MAIN CLASS --------------------
public class UseCase6 {

    public static void main(String[] args) {

        System.out.println("Room Allocation Processing");

        BookingRequestQueue queue = new BookingRequestQueue();
        RoomInventory inventory = new RoomInventory();
        RoomAllocationService allocator = new RoomAllocationService();

        // Requests (FIFO order)
        queue.addRequest(new Reservation("Abhi", "Single"));
        queue.addRequest(new Reservation("Subha", "Single"));
        queue.addRequest(new Reservation("Vanmathi", "Suite"));

        // Process queue
        while (queue.hasPendingRequests()) {

            Reservation r = queue.getNextRequest();
            String type = r.getRoomType();

            if (inventory.isAvailable(type)) {

                String roomId = allocator.allocateRoom(type);
                inventory.reduce(type);

                System.out.println(
                        "Booking confirmed for Guest: " +
                        r.getGuestName() +
                        ", Room ID: " +
                        roomId
                );

            } else {
                System.out.println(
                        "Booking failed for Guest: " +
                        r.getGuestName() +
                        " (No rooms available)"
                );
            }
        }
    }
}