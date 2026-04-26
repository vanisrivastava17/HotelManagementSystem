import java.util.*;

/**
 * ============================================================
 * Use Case 10: Booking Cancellation & Inventory Rollback
 * ============================================================
 *
 * Concepts Covered:
 * - Stack (LIFO rollback)
 * - Map (Reservation tracking)
 * - Controlled state reversal
 * - Inventory restoration
 *
 * This is a SINGLE FILE implementation for VS Code.
 */

public class UseCase10 {

    /* ============================================================
       RoomInventory Class
       Handles inventory storage and restoration
       ============================================================ */
    static class RoomInventory {

        private Map<String, Integer> roomInventory;

        public RoomInventory() {
            roomInventory = new HashMap<>();

            // Initial inventory
            roomInventory.put("Single", 5);
            roomInventory.put("Double", 3);
            roomInventory.put("Suite", 2);
        }

        public boolean bookRoom(String roomType) {

            if (roomInventory.containsKey(roomType)
                    && roomInventory.get(roomType) > 0) {

                roomInventory.put(
                        roomType,
                        roomInventory.get(roomType) - 1
                );

                return true;
            }

            return false;
        }

        public void restoreRoom(String roomType) {

            roomInventory.put(
                    roomType,
                    roomInventory.get(roomType) + 1
            );
        }

        public void showInventory() {

            System.out.println("\nUpdated Room Availability:");

            for (String type : roomInventory.keySet()) {

                System.out.println(
                        type + " Rooms Available: "
                                + roomInventory.get(type)
                );
            }
        }

        public int getAvailability(String roomType) {
            return roomInventory.get(roomType);
        }
    }


    /* ============================================================
       CancellationService Class
       Handles rollback logic using Stack
       ============================================================ */
    static class CancellationService {

        // Stack stores recently released reservations
        private Stack<String> releasedRoomIds;

        // Maps reservation -> room type
        private Map<String, String> reservationRoomTypeMap;

        public CancellationService() {

            releasedRoomIds = new Stack<>();
            reservationRoomTypeMap = new HashMap<>();
        }


        /**
         * Register booking
         */
        public void registerBooking(
                String reservationId,
                String roomType) {

            reservationRoomTypeMap.put(
                    reservationId,
                    roomType
            );
        }


        /**
         * Cancel booking
         */
        public void cancelBooking(
                String reservationId,
                RoomInventory inventory) {

            // Validate reservation
            if (!reservationRoomTypeMap
                    .containsKey(reservationId)) {

                System.out.println(
                        "Invalid reservation ID. Cancellation rejected."
                );
                return;
            }

            String roomType =
                    reservationRoomTypeMap.get(reservationId);

            // Restore inventory
            inventory.restoreRoom(roomType);

            // Push rollback history
            releasedRoomIds.push(reservationId);

            // Remove reservation
            reservationRoomTypeMap.remove(reservationId);

            System.out.println(
                    "Booking cancelled successfully. "
                            + "Inventory restored for room type: "
                            + roomType
            );
        }


        /**
         * Display rollback history
         */
        public void showRollbackHistory() {

            System.out.println(
                    "\nRollback History (Most Recent First):"
            );

            Stack<String> tempStack =
                    (Stack<String>) releasedRoomIds.clone();

            while (!tempStack.isEmpty()) {

                System.out.println(
                        "Released Reservation ID: "
                                + tempStack.pop()
                );
            }
        }
    }


    /* ============================================================
       MAIN METHOD
       ============================================================ */
    public static void main(String[] args) {

        System.out.println("Booking Cancellation");

        RoomInventory inventory =
                new RoomInventory();

        CancellationService cancelService =
                new CancellationService();

        String reservationId = "Single-1";
        String roomType = "Single";


        // Simulate booking
        if (inventory.bookRoom(roomType)) {

            cancelService.registerBooking(
                    reservationId,
                    roomType
            );

            System.out.println(
                    "Room booked successfully."
            );
        }


        // Cancel booking
        cancelService.cancelBooking(
                reservationId,
                inventory
        );


        // Show rollback history
        cancelService.showRollbackHistory();


        // Show updated inventory
        System.out.println(
                "\nUpdated Single Room Availability: "
                        + inventory.getAvailability("Single")
        );
    }
}