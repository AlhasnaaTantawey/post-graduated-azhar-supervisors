package azhar.supervisors.pg_azhar;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SupervisorModel {
    @SerializedName("Staffid")
    @Expose
    private int id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("nationalid")
    @Expose
    private String nationalid;

    @SerializedName("degree")
    @Expose
    private String degree;

    @SerializedName("dept")
    @Expose
    private int DeptNumber;

    @SerializedName("continued_researchers")
    @Expose
    private int continued_researchers;

    public int getContinued_researchers() {
        return continued_researchers;
    }

    public void setContinued_researchers(int continued_researchers) {
        this.continued_researchers = continued_researchers;
    }

    public int getDeptNumber() {
        return DeptNumber;
    }

    public void setDeptNumber(int deptNumber) {
        DeptNumber = deptNumber;
    }

    public String getDegname() {
        return degname;
    }

    public void setDegname(String degname) {
        this.degname = degname;
    }

    @SerializedName("degname")
    @Expose
    private String degname;


    public Integer getIsAdmin() {
        return isAdmin;
    }


    public void setIsAdmin(Integer isAdmin) {
        this.isAdmin = isAdmin;
    }

    @SerializedName("isAdmin")
    @Expose
    private Integer isAdmin;
    @SerializedName("department")
    @Expose
    private String department;

    @SerializedName("branch")
    @Expose
    private String BranchName;

    @SerializedName("organization")
    @Expose
    private String org;
    @SerializedName("specialization")
    @Expose
    private String specialization;

    @SerializedName("IsExternal")
    @Expose
    private Integer  IsExternal;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNationalid() {
        return nationalid;
    }

    public void setNationalid(String nationalid) {
        this.nationalid = nationalid;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getBranchName() {
        return BranchName;
    }

    public void setBranchName(String branchName) {
        BranchName = branchName;
    }

    public String getOrg() {
        return org;
    }

    public void setOrg(String org) {
        this.org = org;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public Integer getIsExternal() {
        return IsExternal;
    }

    public void setIsExternal(Integer isExternal) {
        IsExternal = isExternal;
    }


    @Override
    public String toString() {
        return "SupervisorModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", nationalid='" + nationalid + '\'' +
                ", degree='" + degree + '\'' +
                ", department='" + department + '\'' +
                ", BranchName='" + BranchName + '\'' +
                ", org='" + org + '\'' +
                ", specialization='" + specialization + '\'' +
                ", IsExternal=" + IsExternal +
                '}';
    }
}
