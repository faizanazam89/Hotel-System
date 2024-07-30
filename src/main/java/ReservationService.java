import java.util.ArrayList;
import java.util.List;

public class ReservationService {
    private List<Reservation> reservations = new ArrayList<>();

    public Reservation makeReservation(int id, String startDate, String endDate, String guestName) {
        Reservation reservation = new Reservation(id, startDate, endDate, guestName);
        reservations.add(reservation);
        return reservation;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }
}
