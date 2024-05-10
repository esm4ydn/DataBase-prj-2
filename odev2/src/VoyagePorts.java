import java.time.LocalDate;

public class VoyagePorts {
    private int portId;
    private LocalDate arrivalDate;
    private LocalDate departureDate;

    public VoyagePorts(int portId, LocalDate arrivalDate, LocalDate departureDate) {
        this.portId = portId;
        this.arrivalDate = arrivalDate;
        this.departureDate = departureDate;
    }

    // Getters and setters
    public int getPortId() {
        return portId;
    }

    public void setPortId(int portId) {
        this.portId = portId;
    }

    public LocalDate getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(LocalDate arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public LocalDate getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(LocalDate departureDate) {
        this.departureDate = departureDate;
    }
}

