import java.math.BigDecimal;

public class PetrolTankerShip extends Ship {
    private BigDecimal fuelCapacityLiters; //Petrol gemisi için yakıt litresi

    public BigDecimal getFuelCapacityLiters() {
        return fuelCapacityLiters;
    }

    public void setFuelCapacityLiters(BigDecimal fuelCapacityLiters) {
        this.fuelCapacityLiters = fuelCapacityLiters;
    }


}
