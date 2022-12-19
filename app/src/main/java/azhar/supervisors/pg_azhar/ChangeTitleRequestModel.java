package azhar.supervisors.pg_azhar;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChangeTitleRequestModel {

    @SerializedName("Researcher_name")
    @Expose
    private String researcherName;

    @SerializedName("type_name")
    @Expose
    private String typeName;

    @SerializedName("Qualification")
    @Expose
    private String qualification;

    @SerializedName("LastFac")
    @Expose
    private String LastFac;


    @SerializedName("deptID")
    @Expose
    private int deptId;

    @SerializedName("isSubstantial")
    @Expose
    private int isSubstantial;

    @SerializedName("Supervisor_name")
    @Expose
    private String supervisorName;

    @SerializedName("Type")
    @Expose
    private int type;

    @SerializedName("sup_id")
    @Expose
    private int sup_id;

    @SerializedName("res_id")
    @Expose
    private int res_id;

    @SerializedName("Order_id")
    @Expose
    private int Order_id;

    @SerializedName("faculity_approvement")
    @Expose
    private String faculity_approvement;

    @SerializedName("university_approvement")
    @Expose
    private String university_approvement;

    @SerializedName("department_approvement")
    @Expose
    private String department_approvement;

    @SerializedName("sent_date")
    @Expose
    private String sent_date;

    @SerializedName("oldAddress")
    @Expose
    private String oldAddress;

    @SerializedName("newArabicAddress")
    @Expose
    private String newArabicAddress;

    @SerializedName("newEnglishAddress")
    @Expose
    private String newEnglishAddress;

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getResearcherName() {
        return researcherName;
    }

    public void setResearcherName(String researcherName) {
        this.researcherName = researcherName;
    }

    public String getSupervisorName() {
        return supervisorName;
    }

    public void setSupervisorName(String supervisorName) {
        this.supervisorName = supervisorName;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getDeptId() {
        return deptId;
    }

    public void setDeptId(int deptId) {
        this.deptId = deptId;
    }

    public int getSup_id() {
        return sup_id;
    }

    public void setSup_id(int sup_id) {
        this.sup_id = sup_id;
    }

    public int getRes_id() {
        return res_id;
    }

    public void setRes_id(int res_id) {
        this.res_id = res_id;
    }

    public int getOrder_id() {
        return Order_id;
    }

    public void setOrder_id(int order_id) {
        Order_id = order_id;
    }

    public String getFaculity_approvement() {
        return faculity_approvement;
    }

    public void setFaculity_approvement(String faculity_approvement) {
        this.faculity_approvement = faculity_approvement;
    }

    public String getUniversity_approvement() {
        return university_approvement;
    }

    public void setUniversity_approvement(String university_approvement) {
        this.university_approvement = university_approvement;
    }

    public String getDepartment_approvement() {
        return department_approvement;
    }

    public void setDepartment_approvement(String department_approvement) {
        this.department_approvement = department_approvement;
    }

    public String getSent_date() {
        return sent_date;
    }

    public void setSent_date(String sent_date) {
        this.sent_date = sent_date;
    }


    public int getIsSubstantial() {
        return isSubstantial;
    }

    public void setIsSubstantial(int isSubstantial) {
        this.isSubstantial = isSubstantial;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }


    public String getLastFac() {
        return LastFac;
    }

    public void setLastFac(String lastFac) {
        LastFac = lastFac;
    }


}