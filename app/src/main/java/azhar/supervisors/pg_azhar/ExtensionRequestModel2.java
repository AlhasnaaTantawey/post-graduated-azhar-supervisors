package azhar.supervisors.pg_azhar;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ExtensionRequestModel2 {

    @SerializedName("Arname")
    @Expose
    private String arname;


    @SerializedName("DeptName")
    @Expose
    private String deptName;

    @SerializedName("Forign")
    @Expose
    private String forign;

    @SerializedName("Specialization")
    @Expose
    private String specialization;

    @SerializedName("MsgArAddress")
    @Expose
    private String msgArAddress;

    @SerializedName("QualUnivesity")
    @Expose
    private String qualUnivesity;

    //موافقة الكلية على التسجيل
    @SerializedName("Fac_Council")
    @Expose
    private String fac_Council;

    //"   جامعة  "    "
    @SerializedName("Uni_Council")
    @Expose
    private String uni_Council;

    @SerializedName("extend_period")
    @Expose
    private int extend_period;

    @SerializedName("supervisors")
    @Expose
    private List<SupervisorModel2> supervisors;

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

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getMsgArAddress() {
        return msgArAddress;
    }

    public void setMsgArAddress(String msgArAddress) {
        this.msgArAddress = msgArAddress;
    }

    public String getQualUnivesity() {
        return qualUnivesity;
    }

    public void setQualUnivesity(String qualUnivesity) {
        this.qualUnivesity = qualUnivesity;
    }

    public String getFac_Council() {
        return fac_Council;
    }

    public void setFac_Council(String fac_Council) {
        this.fac_Council = fac_Council;
    }

    public String getUni_Council() {
        return uni_Council;
    }

    public void setUni_Council(String uni_Council) {
        this.uni_Council = uni_Council;
    }

    public int getExtend_period() {
        return extend_period;
    }

    public void setExtend_period(int extend_period) {
        this.extend_period = extend_period;
    }

    public List<SupervisorModel2> getSupervisors() {
        return supervisors;
    }

    public void setSupervisors(List<SupervisorModel2> supervisors) {
        this.supervisors = supervisors;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }
}


