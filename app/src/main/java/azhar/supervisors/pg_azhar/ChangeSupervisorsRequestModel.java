package azhar.supervisors.pg_azhar;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChangeSupervisorsRequestModel {

    @SerializedName("Researcher_name")
    @Expose
    private String researcherName;

    @SerializedName("type_name")
    @Expose
    private String typeName;
    @SerializedName("reasons")
    @Expose
    private String reasons;

    public String getReasons() {
        return reasons;
    }

    public void setReasons(String reasons) {
        this.reasons = reasons;
    }

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

    @SerializedName("Supervisor_name")
    @Expose
    private String supervisorName;
    @SerializedName("firstNewSupID")
    @Expose
    private int firstNewSupervisorId;
    @SerializedName("secondNewSupID")
    @Expose
    private int secondNewSupervisorId;

    public int getFirstNewSupervisorId() {
        return firstNewSupervisorId;
    }

    public void setFirstNewSupervisorId(int firstNewSupervisorId) {
        this.firstNewSupervisorId = firstNewSupervisorId;
    }

    public int getSecondNewSupervisorId() {
        return secondNewSupervisorId;
    }

    public void setSecondNewSupervisorId(int secondNewSupervisorId) {
        this.secondNewSupervisorId = secondNewSupervisorId;
    }

    @SerializedName("Type")
    @Expose
    private int type;
    @SerializedName("Order_id")
    @Expose
    private int Order_id;

    @SerializedName("res_id")
    @Expose
    private int res_id;

    @SerializedName("sup_id")
    @Expose
    private int sup_id;

    @SerializedName("faculity_approvement")
    @Expose
    private String faculity_approvement;

    @SerializedName("uviversity_approvement")
    @Expose
    private String uviversity_approvement;



    @SerializedName("department_approvement")
    @Expose
    private String department_approvement;

    @SerializedName("sent_date")
    @Expose
    private String sent_date;

    public int getRes_id() {
        return res_id;
    }

    public void setRes_id(int res_id) {
        this.res_id = res_id;
    }

    public int getSup_id() {
        return sup_id;
    }

    public void setSup_id(int sup_id) {
        this.sup_id = sup_id;
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

    public String getSent_date() {
        return sent_date;
    }

    public void setSent_date(String sent_date) {
        this.sent_date = sent_date;
    }

    public String getOldSup1() {
        return oldSup1;
    }

    public void setOldSup1(String oldSup1) {
        this.oldSup1 = oldSup1;
    }

    public String getOldSup2() {
        return oldSup2;
    }

    public void setOldSup2(String oldSup2) {
        this.oldSup2 = oldSup2;
    }

    public String getNewSup1() {
        return newSup1;
    }

    public void setNewSup1(String newSup1) {
        this.newSup1 = newSup1;
    }

    public String getNewSup2() {
        return newSup2;
    }

    public void setNewSup2(String newSup2) {
        this.newSup2 = newSup2;
    }

    @SerializedName("oldSup1")
    @Expose
    private String oldSup1;

    @SerializedName("oldSup2")
    @Expose
    private String oldSup2;

    @SerializedName("newSup1")
    @Expose
    private String newSup1;

    @SerializedName("newSup2")
    @Expose
    private String newSup2;

    public int getDeptId() {
        return deptId;
    }

    public void setDeptId(int deptId) {
        this.deptId = deptId;
    }

    @SerializedName("deptID")
    @Expose
    private int deptId;

    @Override
    public String toString() {
        return "ChangeSupervisorsRequestModel{" +
                "res_id=" + res_id +
                ", sup_id=" + sup_id +
                ", faculity_approvement='" + faculity_approvement + '\'' +
                ", uviversity_approvement='" + uviversity_approvement + '\'' +
                ", department_approvement='" + department_approvement + '\'' +
                ", sent_date='" + sent_date + '\'' +
                ", oldSup1='" + oldSup1 + '\'' +
                ", oldSup2='" + oldSup2 + '\'' +
                ", newSup1='" + newSup1 + '\'' +
                ", newSup2='" + newSup2 + '\'' +
                '}';
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getOrder_id() {
        return Order_id;
    }

    public void setOrder_id(int order_id) {
        Order_id = order_id;
    }
}