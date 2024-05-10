
public class PORT {
    private String name;
    private String country;
    private int population;
    private double anchorageFee;
    private boolean passport;
    private boolean firstTime;
    private int id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public boolean isPassport() {
        return passport;
    }

    public void setPassport(boolean passport) {
        this.passport = passport;
    }

    public double getAnchorageFee() {
        return anchorageFee;
    }

    public void setAnchorageFee(double anchorageFee) {
        this.anchorageFee = anchorageFee;
    }

    public boolean isFirstTime() {
        return firstTime;
    }
    public void setFirstTime(boolean firstTime) {
        this.firstTime = firstTime;
    }

    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;

    }
    // toString metodu
    @Override
    public String toString() {
        return "Liman{" +
                "Liman Adı='" + name + '\'' +
                ", Ülke='" + country + '\'' +
                ", Nüfus=" + population +
                ", Pasaport Gerekli Mi =" + passport +
                ", Demirleme Ücreti=" + anchorageFee +
                '}';
    }
}


