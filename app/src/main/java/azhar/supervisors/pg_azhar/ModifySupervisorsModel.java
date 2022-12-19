package azhar.supervisors.pg_azhar;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ModifySupervisorsModel {
  //       LastFac    Qualification


    @SerializedName("oldSupervisors")
    @Expose
    private List<SupervisorModel2> oldSupervisors;

    @SerializedName("reasons")
    @Expose
    private String reasons;

    @SerializedName("newSupervisors")
    @Expose
    private List<SupervisorModel2> newSupervisors;


    @SerializedName("degname")
    @Expose
    private String  degname ;

    @SerializedName("MsgArAddress")
    @Expose
    private String  MsgArAddress ;

    @SerializedName("Fac_Council")
    @Expose
    private String Fac_Council;


    @SerializedName("Uni_Council")
    @Expose
    private String Uni_Council;


    @SerializedName("Arname")
    @Expose
    private String  arname ;


    @SerializedName("Forign")
    @Expose
    private String   forign ;

    @SerializedName("DeptName")
    @Expose
    private String   deptName ;

    @SerializedName("Specialization")
    @Expose
    private String   specialization ;


    @SerializedName("QualFac")
    @Expose
    private String   qualFac ;



    @SerializedName("QualUnivesity")
    @Expose
    private String   qualUnivesity ;


    public List<SupervisorModel2> getOldSupervisors() {
        return oldSupervisors;
    }

    public void setOldSupervisors(List<SupervisorModel2> oldSupervisors) {
        this.oldSupervisors = oldSupervisors;
    }

    public List<SupervisorModel2> getNewSupervisors() {
        return newSupervisors;
    }

    public void setNewSupervisors(List<SupervisorModel2> newSupervisors) {
        this.newSupervisors = newSupervisors;
    }

    public String getDegname() {
        return degname;
    }

    public void setDegname(String degname) {
        this.degname = degname;
    }

    public String getMsgArAddress() {
        return MsgArAddress;
    }

    public void setMsgArAddress(String msgArAddress) {
        MsgArAddress = msgArAddress;
    }

    public String getFac_Council() {
        return Fac_Council;
    }

    public void setFac_Council(String fac_Council) {
        Fac_Council = fac_Council;
    }

    public String getUni_Council() {
        return Uni_Council;
    }

    public void setUni_Council(String uni_Council) {
        Uni_Council = uni_Council;
    }

    public String getArname() {
        return arname;
    }

    public void setArname(String arname) {
        this.arname = arname;
    }

    public String getForign() {
        return forign;
    }

    public void setForign(String forign) {
        this.forign = forign;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getQualFac() {
        return qualFac;
    }

    public void setQualFac(String qualFac) {
        this.qualFac = qualFac;
    }

    public String getQualUnivesity() {
        return qualUnivesity;
    }

    public void setQualUnivesity(String qualUnivesity) {
        this.qualUnivesity = qualUnivesity;
    }

    public String getReasons() {
        return reasons;
    }

    public void setReasons(String reasons) {
        this.reasons = reasons;
    }
}


