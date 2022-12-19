package azhar.supervisors.pg_azhar;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Note {

    public Integer getSu_id() {
        return su_id;
    }

    public void setSu_id(Integer su_id) {
        this.su_id = su_id;
    }




    @SerializedName("noteType")
    @Expose
    private Integer noteType;

    public Integer getNoteType() {
        return noteType;
    }

    public void setNoteType(Integer noteType) {
        this.noteType = noteType;
    }

    @SerializedName("noteID")
   @Expose
   private int noteId;
    @SerializedName("note")
    @Expose
    private String note;

    @SerializedName("su_id")
    @Expose
    private Integer su_id;

    public Integer getRes_id() {
        return res_id;
    }

    public void setRes_id(Integer res_id) {
        this.res_id = res_id;
    }

    @SerializedName("res_id")
    @Expose
    private Integer res_id;

    @SerializedName("deptID")
    @Expose
    private  int deptId;
    @SerializedName("Supervisor_name")
    @Expose
    private  String supervisorName;

    public int getFileNum() {
        return fileNum;
    }

    public void setFileNum(int fileNum) {
        this.fileNum = fileNum;
    }

    @SerializedName("Researcher_name")
    @Expose
    private String ResearcherName;

    @SerializedName("Filenum")
    @Expose
    private int fileNum;

    public int getNoteId() {
        return noteId;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }


    public String getResearcherName() {
        return ResearcherName;
    }

    public void setResearcherName(String researcherName) {
        ResearcherName = researcherName;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }



    public int getDeptId() {
        return deptId;
    }

    public void setDeptId(int deptId) {
        this.deptId = deptId;
    }

    public String getSupervisorName() {
        return supervisorName;
    }

    public void setSupervisorName(String supervisorName) {
        this.supervisorName = supervisorName;
    }
}