public class ReservationController {
    private ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    public Reservation createReservation(int id, String startDate, String endDate, String guestName) {
        return reservationService.makeReservation(id, startDate, endDate, guestName);
    }

    public List<Reservation> listReservations() {
        return reservationService.getReservations();
    }
}
