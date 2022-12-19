package azhar.supervisors.pg_azhar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.pg_azhar.R;
import com.example.pg_azhar.databinding.FragmentReportInformationBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class ReportInformationFragment extends Fragment {
    Retrofit retrofit;
    EndPoint endPoint;
    FragmentReportInformationBinding fragmentReportInformationBinding;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentReportInformationBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_report_information, container, false);
        retrofit = Client.getRetrofit();
        endPoint = retrofit.create(EndPoint.class);
        getOneReport(recieveId());
        return fragmentReportInformationBinding.getRoot();
    }

    public int recieveId() {
        int idReport = 0;
        if (getArguments() != null) {
            idReport = getArguments().getInt("ReportId");
        }
        return idReport;
    }

    public void getOneReport(int id) {
        Call<ReportModel> call = endPoint.getOneReportById(id);
        call.enqueue(new Callback<ReportModel>() {
            @Override
            public void onResponse(Call<ReportModel> call, Response<ReportModel> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                ReportModel reportModel = response.body();
                setDataInReportInformation(reportModel);
            }

            @Override
            public void onFailure(Call<ReportModel> call, Throwable t) {

            }
        });
    }

    public void setDataInReportInformation(ReportModel reportModel) {
        NotesInformaitonFragment.setDepartmentNameText(reportModel.getDeptId(), fragmentReportInformationBinding.departmentName);
        setDataOnCheckBox(reportModel.isRegularAttendance(), fragmentReportInformationBinding.reportInformationFragmentChecboxFirstcheck);
        setDataOnCheckBox(reportModel.isStudyHard(), fragmentReportInformationBinding.reportInformationFragmentChecboxSecondcheck);
        setDataOnCheckBox(reportModel.isUpToFinish(), fragmentReportInformationBinding.reportInformationFragmentChecboxThirdcheck);
        setDataOnCheckBox(reportModel.isWarned(), fragmentReportInformationBinding.reportInformationFragmentChecboxFourthcheck);
        setDataOnCheckBox(reportModel.isResponsiple(), fragmentReportInformationBinding.reportInformationFragmentChecboxFifthcheck);
        fragmentReportInformationBinding.reportInformationFragmentEditTextNumberOfWriting.setText(reportModel.getNumber_chapters() + "");
        fragmentReportInformationBinding.noteInformationFragmentTxtVSupervisorName.setText(reportModel.getSuperviosrName());
        fragmentReportInformationBinding.noteInformationFragmentTxtVResaercherName.setText(reportModel.getResearcherName());
        fragmentReportInformationBinding.reportInformationFragmentChecboxFirstcheck.setEnabled(false);
        fragmentReportInformationBinding.reportInformationFragmentChecboxSecondcheck.setEnabled(false);
        fragmentReportInformationBinding.reportInformationFragmentChecboxFourthcheck.setEnabled(false);
        fragmentReportInformationBinding.reportInformationFragmentChecboxThirdcheck.setEnabled(false);
        fragmentReportInformationBinding.reportInformationFragmentChecboxFifthcheck.setEnabled(false);

    }

    public void setDataOnCheckBox(boolean state, CheckBox checkBox) {
        checkBox.setChecked(state);
    }
}