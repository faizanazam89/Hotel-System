import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ReservationTest {
    private Reservation reservation;

    @Before
    public void setUp() {
        reservation = new Reservation(1, "2024-08-01", "2024-08-05", "John Doe");
    }

    @Test
    public void testReservationCreation() {
        assertEquals(1, reservation.getId());
        assertEquals("2024-08-01", reservation.getStartDate());
        assertEquals("2024-08-05", reservation.getEndDate());
        assertEquals("John Doe", reservation.getGuestName());
    }

    @Test
    public void testSetters() {
        reservation.setId(2);
        reservation.setStartDate("2024-09-01");
        reservation.setEndDate("2024-09-05");
        reservation.setGuestName("Jane Smith");

        assertEquals(2, reservation.getId());
        assertEquals("2024-09-01", reservation.getStartDate());
        assertEquals("2024-09-05", reservation.getEndDate());
        assertEquals("Jane Smith", reservation.getGuestName());
    }
}
