package azhar.supervisors.pg_azhar;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResearcherModel {
    @SerializedName("degree")
    @Expose
    private String degree;

    @SerializedName("count_payments")
    @Expose
    private Integer count_payments;

    @SerializedName("count_reports")
    @Expose
    private Integer count_reports;


    @SerializedName("UniAccept")
    @Expose
    private Integer uniAccept;


    @SerializedName("InsertValue")
    @Expose
    private Integer insertValue;
    @SerializedName("ID")
    @Expose
    private Integer id;
    @SerializedName("Arname")
    @Expose
    private String arabicName;
    @SerializedName("Enname")
    @Expose
    private String Enname;
    @SerializedName("Gender")
    @Expose
    private String gender;
    @SerializedName("Azhar")
    @Expose
    private String azhar;
    @SerializedName("Forign")
    @Expose
    private String forign;
    @SerializedName("BirthDate")
    @Expose
    private String birthDate;
    @SerializedName("BirthBlace")
    @Expose
    private String birthPlace;
    @SerializedName("village")
    @Expose
    private String village;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("CurrentPlace")
    @Expose
    private String currentPlace;
    @SerializedName("Tel")
    @Expose
    private String telephone;
    @SerializedName("Mobaile")
    @Expose
    private String mobaile;
    @SerializedName("InJop")
    @Expose
    private Integer inJop;
    @SerializedName("Jop")
    @Expose
    private String jop;
    @SerializedName("JopPlace")
    @Expose
    private String jopPlace;

    @SerializedName("JopTel")
    @Expose
    private String jopTelphone;


    @SerializedName("DeptID")
    @Expose
    private Integer deptId;
    @SerializedName("specialization")
    @Expose
    private String specialization;
    @SerializedName("department")
    @Expose
    private String department;
    @SerializedName("NationalID")
    @Expose
    private String nationalID;
    @SerializedName("NationalPlace")
    @Expose
    private String nationalPlace;
    @SerializedName("NationalDate")
    @Expose
    private String nationalDate;
    @SerializedName("MsgArAddress")
    @Expose
    private String msgArAddress;

    @SerializedName("MsgEnAddress")
    @Expose
    private String msgEnAddress;
    @SerializedName("LastFac")
    @Expose
    private String lastFac;
    @SerializedName("Qualification")
    @Expose
    private String qualification;
    @SerializedName("QualSpecialization")
    @Expose
    private String qualSpecialization;
    @SerializedName("QualFac")
    @Expose
    private String qualFac;
    @SerializedName("QualDate")
    @Expose
    private String qualDate;
    @SerializedName("QualUnivesity")
    @Expose
    private String qualUnivesity;

    @SerializedName("QualGrade")
    @Expose
    private Integer qualGrade;

    @SerializedName("MsgAccept")
    @Expose
    private String msgAccept;

    @SerializedName("Dept_Council")
    @Expose
    private String dept_Council;
    @SerializedName("Fac_Council")
    @Expose
    private String fac_Council;


    @SerializedName("Fac_Council_No")
    @Expose
    private String fac_Council_No;

    @SerializedName("Uni_Council")
    @Expose
    private String uni_Council;

    @SerializedName("Regsitretion_Period")
    @Expose
    private String regsitretion_Period;

    @SerializedName("IsCancelled")
    @Expose
    private Integer isCancelled;

    @SerializedName("DegId")
    @Expose
    private Integer degId;

    @SerializedName("MilitrayId")
    @Expose
    private Integer militrayId;


    @SerializedName("BrId")
    @Expose
    private Integer brId;

    @SerializedName("SpId")
    @Expose
    private Integer spId;


    @SerializedName("IsGranted")
    @Expose
    private Integer isGranted;

    @SerializedName("DeptAccept")
    @Expose
    private Integer deptAccept;
    @SerializedName("FacAccept")
    @Expose
    private Integer facAccept;


    @SerializedName("Filenum")
    @Expose
    private Integer Filenum;

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public Integer getCount_payments() {
        return count_payments;
    }

    public void setCount_payments(Integer count_payments) {
        this.count_payments = count_payments;
    }

    public Integer getCount_reports() {
        return count_reports;
    }

    public void setCount_reports(Integer count_reports) {
        this.count_reports = count_reports;
    }

    public Integer getUniAccept() {
        return uniAccept;
    }

    public void setUniAccept(Integer uniAccept) {
        this.uniAccept = uniAccept;
    }

    public Integer getInsertValue() {
        return insertValue;
    }

    public void setInsertValue(Integer insertValue) {
        this.insertValue = insertValue;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getArabicName() {
        return arabicName;
    }

    public void setArabicName(String arabicName) {
        this.arabicName = arabicName;
    }

    public String getEnname() {
        return Enname;
    }

    public void setEnname(String enname) {
        Enname = enname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAzhar() {
        return azhar;
    }

    public void setAzhar(String azhar) {
        this.azhar = azhar;
    }

    public String getForign() {
        return forign;
    }

    public void setForign(String forign) {
        this.forign = forign;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCurrentPlace() {
        return currentPlace;
    }

    public void setCurrentPlace(String currentPlace) {
        this.currentPlace = currentPlace;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getMobaile() {
        return mobaile;
    }

    public void setMobaile(String mobaile) {
        this.mobaile = mobaile;
    }

    public Integer getInJop() {
        return inJop;
    }

    public void setInJop(Integer inJop) {
        this.inJop = inJop;
    }

    public String getJop() {
        return jop;
    }

    public void setJop(String jop) {
        this.jop = jop;
    }

    public String getJopPlace() {
        return jopPlace;
    }

    public void setJopPlace(String jopPlace) {
        this.jopPlace = jopPlace;
    }

    public String getJopTelphone() {
        return jopTelphone;
    }

    public void setJopTelphone(String jopTelphone) {
        this.jopTelphone = jopTelphone;
    }

    public Integer getDeptId() {
        return deptId;
    }

    public void setDeptId(Integer deptId) {
        this.deptId = deptId;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getNationalID() {
        return nationalID;
    }

    public void setNationalID(String nationalID) {
        this.nationalID = nationalID;
    }

    public String getNationalPlace() {
        return nationalPlace;
    }

    public void setNationalPlace(String nationalPlace) {
        this.nationalPlace = nationalPlace;
    }

    public String getNationalDate() {
        return nationalDate;
    }

    public void setNationalDate(String nationalDate) {
        this.nationalDate = nationalDate;
    }

    public String getMsgArAddress() {
        return msgArAddress;
    }

    public void setMsgArAddress(String msgArAddress) {
        this.msgArAddress = msgArAddress;
    }

    public String getMsgEnAddress() {
        return msgEnAddress;
    }

    public void setMsgEnAddress(String msgEnAddress) {
        this.msgEnAddress = msgEnAddress;
    }

    public String getLastFac() {
        return lastFac;
    }

    public void setLastFac(String lastFac) {
        this.lastFac = lastFac;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getQualSpecialization() {
        return qualSpecialization;
    }

    public void setQualSpecialization(String qualSpecialization) {
        this.qualSpecialization = qualSpecialization;
    }

    public String getQualFac() {
        return qualFac;
    }

    public void setQualFac(String qualFac) {
        this.qualFac = qualFac;
    }

    public String getQualDate() {
        return qualDate;
    }

    public void setQualDate(String qualDate) {
        this.qualDate = qualDate;
    }

    public String getQualUnivesity() {
        return qualUnivesity;
    }

    public void setQualUnivesity(String qualUnivesity) {
        this.qualUnivesity = qualUnivesity;
    }

    public Integer getQualGrade() {
        return qualGrade;
    }

    public void setQualGrade(Integer qualGrade) {
        this.qualGrade = qualGrade;
    }

    public String getMsgAccept() {
        return msgAccept;
    }

    public void setMsgAccept(String msgAccept) {
        this.msgAccept = msgAccept;
    }

    public String getDept_Council() {
        return dept_Council;
    }

    public void setDept_Council(String dept_Council) {
        this.dept_Council = dept_Council;
    }

    public String getFac_Council() {
        return fac_Council;
    }

    public void setFac_Council(String fac_Council) {
        this.fac_Council = fac_Council;
    }

    public String getFac_Council_No() {
        return fac_Council_No;
    }

    public void setFac_Council_No(String fac_Council_No) {
        this.fac_Council_No = fac_Council_No;
    }

    public String getUni_Council() {
        return uni_Council;
    }

    public void setUni_Council(String uni_Council) {
        this.uni_Council = uni_Council;
    }

    public String getRegsitretion_Period() {
        return regsitretion_Period;
    }

    public void setRegsitretion_Period(String regsitretion_Period) {
        this.regsitretion_Period = regsitretion_Period;
    }

    public Integer getIsCancelled() {
        return isCancelled;
    }

    public void setIsCancelled(Integer isCancelled) {
        this.isCancelled = isCancelled;
    }

    public Integer getSpId() {
        return spId;
    }

    public void setSpId(Integer spId) {
        this.spId = spId;
    }

    public Integer getIsGranted() {
        return isGranted;
    }

    public void setIsGranted(Integer isGranted) {
        this.isGranted = isGranted;
    }

    public Integer getDeptAccept() {
        return deptAccept;
    }

    public void setDeptAccept(Integer deptAccept) {
        this.deptAccept = deptAccept;
    }

    public Integer getFacAccept() {
        return facAccept;
    }

    public void setFacAccept(Integer facAccept) {
        this.facAccept = facAccept;
    }

    public Integer getFilenum() {
        return Filenum;
    }

    public void setFilenum(Integer filenum) {
        Filenum = filenum;
    }

    public Integer getDegId() {
        return degId;
    }

    public void setDegId(Integer degId) {
        this.degId = degId;
    }

    public Integer getMilitrayId() {
        return militrayId;
    }

    public void setMilitrayId(Integer militrayId) {
        this.militrayId = militrayId;
    }

    public Integer getBrId() {
        return brId;
    }

    public void setBrId(Integer brId) {
        this.brId = brId;
    }
}
