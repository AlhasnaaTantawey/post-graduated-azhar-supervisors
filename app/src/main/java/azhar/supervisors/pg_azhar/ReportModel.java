package azhar.supervisors.pg_azhar;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReportModel {



    @SerializedName("res_id")
    @Expose
    private  int res_id;
    @SerializedName("sup_id")
    @Expose
    private  int sup_id;
    @SerializedName("isRegularAttendance")
    @Expose
    private boolean isRegularAttendance;

    @SerializedName("isStudyHard")
    @Expose
    private  boolean isStudyHard;


    @SerializedName("isUpToFinish")
    @Expose
    private  boolean isUpToFinish;

    @SerializedName("isResponsiple")
    @Expose
    private  boolean isResponsiple;
    @SerializedName("isWarned")
    @Expose
    private  boolean isWarned;
    @SerializedName("number_chapters")
    @Expose
    private  int number_chapters;
    @SerializedName("sent_date")
    @Expose
    private String sent_date;

    public String getSuperviosrName() {
        return superviosrName;
    }

    public void setSuperviosrName(String superviosrName) {
        this.superviosrName = superviosrName;
    }

    public String getResearcherName() {
        return researcherName;
    }

    public void setResearcherName(String researcherName) {
        this.researcherName = researcherName;
    }

    public int getDeptId() {
        return deptId;
    }

    public void setDeptId(int deptId) {
        this.deptId = deptId;
    }

    @SerializedName("Supervisor_name")
    @Expose
    private String superviosrName;

    @SerializedName("Researcher_name")
    @Expose
    private String researcherName;

    @SerializedName("deptID")
    @Expose
    private int deptId;

    @SerializedName("rep_id")
    @Expose
    private int rep_id;

    public int getRep_id() {
        return rep_id;
    }

    public void setRep_id(int rep_id) {
        this.rep_id = rep_id;
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

    public boolean isRegularAttendance() {
        return isRegularAttendance;
    }

    public void setRegularAttendance(boolean regularAttendance) {
        isRegularAttendance = regularAttendance;
    }

    public boolean isStudyHard() {
        return isStudyHard;
    }

    public void setStudyHard(boolean studyHard) {
        isStudyHard = studyHard;
    }

    public boolean isUpToFinish() {
        return isUpToFinish;
    }

    public void setUpToFinish(boolean upToFinish) {
        isUpToFinish = upToFinish;
    }

    public boolean isResponsiple() {
        return isResponsiple;
    }

    public void setResponsiple(boolean responsiple) {
        isResponsiple = responsiple;
    }

    public boolean isWarned() {
        return isWarned;
    }

    public void setWarned(boolean warned) {
        isWarned = warned;
    }

    public int getNumber_chapters() {
        return number_chapters;
    }

    public void setNumber_chapters(int number_chapters) {
        this.number_chapters = number_chapters;
    }

    public String getSent_date() {
        return sent_date;
    }

    public void setSent_date(String sent_date) {
        this.sent_date = sent_date;
    }
}
