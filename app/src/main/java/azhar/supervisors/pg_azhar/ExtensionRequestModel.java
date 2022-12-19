package azhar.supervisors.pg_azhar;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExtensionRequestModel {
    @SerializedName("Type")
    @Expose
    private int type;


    @SerializedName("Researcher_name")
    @Expose
    private String researcherName;

    @SerializedName("Supervisor_name")
    @Expose
    private String supervisorName;


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


    @SerializedName("extend_period")
    @Expose
    private int extend_period;

    @SerializedName("deptID")
    @Expose
    private int deptId;

    @SerializedName("sup_id")
    @Expose
    private int sup_id;

    @SerializedName("res_id")
    @Expose
    private int res_id;

    @SerializedName("sent_date")
    @Expose
    private String sent_date;

    @SerializedName("Order_id")
    @Expose
    private int Order_id;
    @SerializedName("type_name")
    @Expose
    private String typeName;

    @SerializedName("faculity_approvement")
    @Expose
    private String faculity_approvement;

    @SerializedName("uviversity_approvement")
    @Expose
    private String uviversity_approvement;

    @SerializedName("department_approvement")
    @Expose
    private String department_approvement;

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



    public int getExtend_period() {
        return extend_period;
    }

    public void setExtend_period(int extend_period) {
        this.extend_period = extend_period;
    }

    public String getSent_date() {
        return sent_date;
    }

    public void setSent_date(String sent_date) {
        this.sent_date = sent_date;
    }
}
