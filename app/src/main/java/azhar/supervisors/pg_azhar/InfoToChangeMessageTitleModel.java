package azhar.supervisors.pg_azhar;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class InfoToChangeMessageTitleModel {
    @SerializedName("QualFac")
    @Expose
    private String qualFac;

    @SerializedName("Fac_Council")
    @Expose
    private String Fac_Council;


    @SerializedName("Uni_Council")
    @Expose
    private String Uni_Council;


    @SerializedName("Arname")
    @Expose
    private String Arname;

    @SerializedName("ID")
    @Expose
    private  int id;

    @SerializedName("Forign")
    @Expose
    private String Forign;

    @SerializedName("oldAddress")
    @Expose
    private String oldAddress;

    @SerializedName("newArabicAddress")
    @Expose
    private String newArabicAddress;

    @SerializedName("newEnglishAddress")
    @Expose
    private String newEnglishAddress;

    @SerializedName("MsgArAddress")
    @Expose
    private String MsgArAddress;


    @SerializedName("MsgEnAddress")
    @Expose
    private String MsgEnAddress;

    @SerializedName("LastFac")
    @Expose
    private String LastFac;

    @SerializedName("type_name")
    @Expose
    private String typeName;

    @SerializedName("Qualification")
    @Expose
    private String Qualification;

    @SerializedName("QualSpecializationLastFac")
    @Expose
    private String QualSpecializationLastFac;

    @SerializedName("sent_date")
    @Expose
    private String sent_date;


    @SerializedName("degname")
    @Expose
    private String degname;


    @SerializedName("QualUnivesity")
    @Expose
    private String QualUnivesity;


    @SerializedName("supervisors")
    @Expose
    private List<SupervisorModel2> supervisors;

    @SerializedName("isSubstantial")
    @Expose
    private int isSubstantial;

    @SerializedName("DeptName")
    @Expose
    private String DeptName;

    @SerializedName("faculity_approvement")
    @Expose
    private String faculity_approvement;

    @SerializedName("university_approvement")
    @Expose
    private String uviversity_approvement;


    @SerializedName("department_approvement")
    @Expose
    private String department_approvement;

    public String getArname() {
        return Arname;
    }

    public void setArname(String arname) {
        Arname = arname;
    }

    public String getForign() {
        return Forign;
    }

    public void setForign(String forign) {
        Forign = forign;
    }

    public String getMsgArAddress() {
        return MsgArAddress;
    }

    public void setMsgArAddress(String msgArAddress) {
        MsgArAddress = msgArAddress;
    }

    public String getMsgEnAddress() {
        return MsgEnAddress;
    }

    public void setMsgEnAddress(String msgEnAddress) {
        MsgEnAddress = msgEnAddress;
    }

    public String getLastFac() {
        return LastFac;
    }

    public void setLastFac(String lastFac) {
        LastFac = lastFac;
    }

    public String getQualification() {
        return Qualification;
    }

    public void setQualification(String qualification) {
        Qualification = qualification;
    }

    public String getQualSpecializationLastFac() {
        return QualSpecializationLastFac;
    }

    public void setQualSpecializationLastFac(String qualSpecializationLastFac) {
        QualSpecializationLastFac = qualSpecializationLastFac;
    }

    public String getQualUnivesity() {
        return QualUnivesity;
    }

    public void setQualUnivesity(String qualUnivesity) {
        QualUnivesity = qualUnivesity;
    }

    public List<SupervisorModel2> getSupervisors() {
        return supervisors;
    }

    public void setSupervisors(List<SupervisorModel2> supervisors) {
        this.supervisors = supervisors;
    }

    public String getDeptName() {
        return DeptName;
    }

    public void setDeptName(String deptName) {
        DeptName = deptName;
    }

    public int getIsSubstantial() {
        return isSubstantial;
    }

    public void setIsSubstantial(int isSubstantial) {
        this.isSubstantial = isSubstantial;
    }

    public String getFaculity_approvement() {
        return faculity_approvement;
    }

    public void setFaculity_approvement(String faculity_approvement) {
        this.faculity_approvement = faculity_approvement;
    }

    public String getUviversity_approvement() {
        return uviversity_approvement;
    }

    public void setUviversity_approvement(String uviversity_approvement) {
        this.uviversity_approvement = uviversity_approvement;
    }

    public String getDepartment_approvement() {
        return department_approvement;
    }

    public void setDepartment_approvement(String department_approvement) {
        this.department_approvement = department_approvement;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getSent_date() {
        return sent_date;
    }

    public void setSent_date(String sent_date) {
        this.sent_date = sent_date;
    }

    public String getOldAddress() {
        return oldAddress;
    }

    public void setOldAddress(String oldAddress) {
        this.oldAddress = oldAddress;
    }

    public String getNewArabicAddress() {
        return newArabicAddress;
    }

    public void setNewArabicAddress(String newArabicAddress) {
        this.newArabicAddress = newArabicAddress;
    }

    public String getNewEnglishAddress() {
        return newEnglishAddress;
    }

    public void setNewEnglishAddress(String newEnglishAddress) {
        this.newEnglishAddress = newEnglishAddress;
    }

    public String getQualFac() {
        return qualFac;
    }

    public void setQualFac(String qualFac) {
        this.qualFac = qualFac;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDegname() {
        return degname;
    }

    public void setDegname(String degname) {
        this.degname = degname;
    }
}
