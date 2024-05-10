import java.sql.Date;
import java.util.List;

public class Voyage {
    private int voyageId;
    private int shipId; //Ship tablo anahtarı
    Date departureDate; //YIL-AY-GÜN
    Date returnDate; //YIL-AY-GÜN
    private int departurePort; //ÇIKIŞ LİMANI
    private List<VoyageCrew> crewIds;
    private List<VoyageCaptains> captainIds;
    private List<VoyagePorts> voyagePorts;

    // Getter ve setter metotları
    public int getVoyageId() {
        return voyageId;
    }

    public void setVoyageId(int voyageId) {
        this.voyageId = voyageId;
    }

    public int getShipId() {
        return shipId;
    }

    public void setShipId(int shipId) {
        this.shipId = shipId;
    }

    public Date getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(Date departureDate) {
        this.departureDate = departureDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public int getDeparturePort() {
        return departurePort;
    }

    public void setDeparturePort(int departurePort) {
        this.departurePort = departurePort;
    }

    public List<VoyageCrew> getCrewIds() {
        return crewIds;
    }
    public void setCrewIds(List<VoyageCrew> crewIds) {
        this.crewIds = crewIds;
    }
    public List<VoyageCaptains> getCaptainIds() {
        return captainIds;
    }
    public void setCaptainIds(List<VoyageCaptains> captainIds) {
        this.captainIds = captainIds;
    }
    public List<VoyagePorts> getVoyagePorts() {
        return voyagePorts;
    }
    public void setVoyagePorts(List<VoyagePorts> voyagePorts) {
        this.voyagePorts = voyagePorts;
    }
}
