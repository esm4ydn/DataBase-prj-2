import java.util.Date;

public class Crew {
    int crewId;
    String crewName;
    String crewLastName;
    String crewAddress;
    int crewTckn;
    Date crewBirthDate; //YIL-AY-GÜN
    Date crewStartDate; //YIL-AY-GÜN
    String crewTask;


    public int getCrewId() {
        return crewId;
    }

    public void setCrewId(int crewId) {
        this.crewId = crewId;
    }

    public String getCrewName() {
        return crewName;
    }

    public void setCrewName(String crewName) {
        this.crewName = crewName;
    }

    public String getCrewLastName() {
        return crewLastName;
    }

    public void setCrewLastName(String crewLastName) {
        this.crewLastName = crewLastName;
    }

    public String getCrewAddress() {
        return crewAddress;
    }

    public void setCrewAddress(String crewAddress) {
        this.crewAddress = crewAddress;
    }

    public int getCrewTckn() {
        return crewTckn;
    }

    public void setCrewTckn(int crewTckn) {
        this.crewTckn = crewTckn;
    }

    public Date getCrewBirthDate() {
        return crewBirthDate;
    }

    public void setCrewBirthDate(Date crewBirthDate) {
        this.crewBirthDate = crewBirthDate;
    }

    public Date getCrewStartDate() {
        return crewStartDate;
    }

    public void setCrewStartDate(Date crewStartDate) {
        this.crewStartDate = crewStartDate;
    }

    public String getCrewTask() {
        return crewTask;
    }

    public void setCrewTask(String crewTask) {
        this.crewTask = crewTask;
    }
}
