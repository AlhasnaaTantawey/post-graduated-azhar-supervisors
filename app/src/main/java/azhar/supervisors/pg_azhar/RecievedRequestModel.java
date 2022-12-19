package azhar.supervisors.pg_azhar;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RecievedRequestModel {

    @SerializedName("deptID")
    @Expose
    private int  deptID;

    @SerializedName("Filenum")
    @Expose
    private String  Filenum;

    @SerializedName("type_name")
    @Expose
    private String  type_name;

    @SerializedName("Supervisor_name")
    @Expose
    private String supervisorName;



    @SerializedName("Order_id")
    @Expose
    private int Order_id;

    @SerializedName("res_id")
    @Expose
    private int res_id;

    @SerializedName("sup_id")
    @Expose
    private int sup_id;

    @SerializedName("department_approvement")
    @Expose
    private String department_approvement;

    @SerializedName("faculity_approvement")
    @Expose
    private String faculity_approvement;

    @SerializedName("university_approvement")
    @Expose
    private String university_approvement;


    @SerializedName("sent_date")
    @Expose
    private String sent_date;


    @SerializedName("Type")
    @Expose
    private int type;

    @SerializedName("Researcher_name")
    @Expose
    private String Researcher_name;



    public int getOrder_id() {
        return Order_id;
    }

    public void setOrder_id(int order_id) {
        Order_id = order_id;
    }

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

    public String getDepartment_approvement() {
        return department_approvement;
    }

    public void setDepartment_approvement(String department_approvement) {
        this.department_approvement = department_approvement;
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

    public String getSent_date() {
        return sent_date;
    }

    public void setSent_date(String sent_date) {
        this.sent_date = sent_date;
    }

    public int getType() {
        return type;
    }

    public String getResearcher_name() {
        return Researcher_name;
    }

    public void setResearcher_name(String researcher_name) {
        Researcher_name = researcher_name;
    }

    public void setType(int type) {
        this.type = type;
    }



    public String getFilenum() {
        return Filenum;
    }

    public void setFilenum(String filenum) {
        Filenum = filenum;
    }

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }

    public String getSupervisorName() {
        return supervisorName;
    }

    public void setSupervisorName(String supervisorName) {
        this.supervisorName = supervisorName;
    }

    public int getDeptID() {
        return deptID;
    }

    public void setDeptID(int deptID) {
        this.deptID = deptID;
    }
}
