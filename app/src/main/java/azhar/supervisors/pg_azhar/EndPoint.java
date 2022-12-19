package azhar.supervisors.pg_azhar;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface EndPoint {

    @GET("/is_supervisor_exist/{username}/{password}")
    Call<SupervisorModel> getSupervisor(@Path("username")String  user_name, @Path("password")String password);

    @GET("/One_Supervisor/{id}")
    Call<SupervisorModel> getOneSupervisor(@Path("id")int id);

    @GET("All_Researchers_sup/{id}")
    Call<ArrayList<ResearcherModel>> getAllStudnets_sup(@Path("id")int id);

    @GET("/One_Researcher/{id}")
    Call<ResearcherModel> getOneResearcher(@Path("id")int id);


    @GET("dept_supervisors/{adminNumber}")
    Call<ArrayList<SupervisorModel>> getAllSuperviosrOnOneDepartment(@Path("adminNumber")int adminNumber);

    @POST("create_a_note/")
    Call<Note> sendNotes(@Body Note note);

    @POST("create_changingSupervisor/")
    Call<ChangeSupervisorsRequestModel> sendChangeSupervisorRequest(@Body ChangeSupervisorsRequestModel changeSupervisorsRequestModel);


    @GET("supervisors_res/{id}")
    Call<ArrayList<SupervisorModel>> getAllSupervisor_res(@Path("id")int id);

    @POST("/create_extension/")
    Call<ExtensionRequestModel> getExtension(@Body ExtensionRequestModel extensionModel);

    @POST("change_Address/")
    Call<ChangeTitleRequestModel> changeTitleRequest(@Body ChangeTitleRequestModel changeTitleRequestModel);

    @GET("supervisors_Request/{id}")
    Call<ArrayList<RecievedRequestModel>> getAllRequestsForOneSupervisor(@Path("id")int id);

    @GET("Request_Details/{id}")
    Call<RecievedRequestModel> getRequestDetails(@Path("id")int id);

    @POST("create_report/")
    Call<ReportModel> createReport(@Body ReportModel reportModel);


    @GET("get_a_note/{id}")
    Call<Note>getNote(@Path("id")int id);

    @GET("get_a_report/{id}")
    Call<ReportModel>getOneReportById(@Path("id")int id);

    @GET("department_supervisorInformation_notes/{id}")
    Call<ArrayList<Note>> getAllNoteInOneDepattmentforSupervisorInformation(@Path("id")int id);

    @GET("department_supervisorForResearcher_notes/{id}")
    Call<ArrayList<Note>> getAllNoteInOneDepattmentforSupervisorForResearchers(@Path("id")int id);

    @GET("department_researcherForSupervisor_notes/{id}")
    Call<ArrayList<Note>> getAllNoteInOneDepattmentforResearcherForSupervisors(@Path("id")int id);

    @GET("department_researcher_information_notes/{id}")
    Call<ArrayList<Note>> getAllNoteInOneDepattmentforResearcherInformation(@Path("id")int id);

    @GET("get_a_note_researcher_information/{id}")
    Call<Note>getOneNoteResearcherInformation(@Path("id")int id);

    @GET("get_a_note_supervisorInformation/{id}")
    Call<Note>getOneNoteSupervisorInformation(@Path("id")int id);

    @GET("get_a_note/{id}")
    Call<Note>getSuperviosrForResarcherAndResearcherForSuperviosrsOneNoteDetails(@Path("id")int id);


    @GET ("department_reportes/{id}")
    Call<ArrayList<ReportModel>>getAllReportOnOneDepartment(@Path("id")int id);

    @GET("department_orders/{id}/{num}")
    Call<ArrayList<RecievedRequestModel>>getAllRequestOnOneDepartmentForSupervisors(@Path("id")int id,@Path("num")int num);

    //http://35.173.80.178/ReportForExtend/21
    @GET("ReportForExtend/{id}")
    Call<ExtensionRequestModel2> getOneExtendRequest(@Path("id")int id);

    @GET("ReportForModifySuprevisors/{id}")
    Call<ModifySupervisorsModel> getOneChangeSupervisorRequest(@Path("id")int id);


    @GET("ReportForModifyAddress/{id}")
    Call<InfoToChangeMessageTitleModel> getOneChangeTitleRequest(@Path("id")int id);

    @PATCH("update_username_pass_Supervisor/{id}/{username}/{passw}")
    Call<SupervisorModel> changeUsernameAndPassword(@Path("id") int id, @Path("username") String username, @Path("passw") String password);


}
