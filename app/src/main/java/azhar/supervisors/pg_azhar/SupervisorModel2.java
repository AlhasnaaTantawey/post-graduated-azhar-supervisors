package azhar.supervisors.pg_azhar;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SupervisorModel2 {

    @SerializedName("Name")
    @Expose
    private String Name;

    @SerializedName("degree")
    @Expose
    private String degree;

    @SerializedName("workplace")
    @Expose
    private String workplace;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getWorkplace() {
        return workplace;
    }

    public void setWorkplace(String workplace) {
        this.workplace = workplace;
    }
}
