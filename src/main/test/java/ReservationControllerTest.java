import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class ReservationControllerTest {
    private ReservationController controller;
    private ReservationService service;

    @Before
    public void setUp() {
        service = new ReservationService();
        controller = new ReservationController(service);
    }

    @Test
    public void testCreateReservation() {
        Reservation reservation = controller.createReservation(1, "2024-08-01", "2024-08-05", "John Doe");
        assertNotNull(reservation);
        assertEquals(1, reservation.getId());
        assertEquals("2024-08-01", reservation.getStartDate());
        assertEquals("2024-08-05", reservation.getEndDate());
        assertEquals("John Doe", reservation.getGuestName());
    }

    @Test
    public void testListReservations() {
        controller.createReservation(1, "2024-08-01", "2024-08-05", "John Doe");
        controller.createReservation(2, "2024-08-06", "2024-08-10", "Jane Smith");

        List<Reservation> reservations = controller.listReservations();
        assertEquals(2, reservations.size());
    }
}
