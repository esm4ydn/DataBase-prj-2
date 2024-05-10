import java.math.BigDecimal;

public class ContainerShip extends Ship {
    private int containerCapacity;  //konteynr gemisi için kapasite
    private BigDecimal maxWeightCapacity;  //konteynr gemisi için en fazla ağırlık mik.

    public int getContainerCapacity() {
        return containerCapacity;
    }

    public void setContainerCapacity(int containerCapacity) {
        this.containerCapacity = containerCapacity;
    }

    public BigDecimal getMaxWeightCapacity() {
        return maxWeightCapacity;
    }

    public void setMaxWeightCapacity(BigDecimal maxWeightCapacity) {
        this.maxWeightCapacity = maxWeightCapacity;
    }
}