package azhar.supervisors.pg_azhar;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.pg_azhar.R;
import com.example.pg_azhar.databinding.FragmentReportBinding;

import azhar.supervisors.pg_azhar.ui.personalData.PersonalDataFragment;

import java.time.LocalDate;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ReportFragment extends Fragment {
    FragmentReportBinding fragmentReportBinding;
    Retrofit retrofit;
    EndPoint endPoint;
    int id, numberOfChapter;
    boolean check1, check2, check3, check4, check5;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentReportBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_report, container, false);
        retrofit = Client.getRetrofit();
        endPoint = retrofit.create(EndPoint.class);
        sendReportClick();
        return fragmentReportBinding.getRoot();
    }

    public void sendReportClick() {
        fragmentReportBinding.researcherStatefragmentBtnSendState.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                try {
                    if (fragmentReportBinding.researcherFragmentEditTextNumberOfWriting.getText().toString().isEmpty()) {
                        Toast.makeText(getContext(), "ادخل عدد الابحاث", Toast.LENGTH_SHORT).show();
                    } else {
                        takeDataFromCheckBoxs();
                        setAlerDialog();
                    }
                } catch (Exception exception) {
                    Toast.makeText(getContext(), "ادخل رقم الابحاث بشكل صحيح", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public int receivedId() {
        if (getArguments() != null) {
            id = getArguments().getInt(PersonalInformationForOneResearcher.RESEARCHERID);
        }
        return id;
    }

    //recieve the values form checkboxs and EditText
    public void takeDataFromCheckBoxs() {
        check1 = fragmentReportBinding.researcherFragmentChecboxFirstcheck.isChecked();
        check2 = fragmentReportBinding.researcherFragmentChecboxSecondcheck.isChecked();
        check3 = fragmentReportBinding.researcherFragmentChecboxThirdcheck.isChecked();
        check4 = fragmentReportBinding.researcherFragmentChecboxFourthcheck.isChecked();
        check5 = fragmentReportBinding.researcherFragmentChecboxFifthcheck.isChecked();
        numberOfChapter = Integer.parseInt(fragmentReportBinding.researcherFragmentEditTextNumberOfWriting.getText().toString().trim());
    }

    // create new object form ReportModel and pass the value
    public ReportModel createNewReportObject() {
        ReportModel new_report = new ReportModel();
        new_report.setRes_id(receivedId());
        new_report.setSup_id(SupervisorActivity.supervisorId);
        new_report.setDeptId(PersonalDataFragment.supervisor.getDeptNumber());
        new_report.setSent_date(LocalDate.now().toString());
        new_report.setRegularAttendance(check1);
        new_report.setStudyHard(check2);
        new_report.setUpToFinish(check3);
        new_report.setWarned(check4);
        new_report.setResponsiple(check5);
        new_report.setNumber_chapters(numberOfChapter);
        return new_report;
    }

    //use Api to send new report
    public void sendNewReport() {

        Call<ReportModel> call = endPoint.createReport(createNewReportObject());
        call.enqueue(new Callback<ReportModel>() {
            @Override
            public void onResponse(Call<ReportModel> call, Response<ReportModel> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getContext(), "فشل فى ارسال التقرير", Toast.LENGTH_SHORT).show();
                    return;
                }
                ReportModel report = response.body();
                Toast.makeText(getContext(), "تم ارسال التقرير", Toast.LENGTH_SHORT).show();
                getActivity().onBackPressed();
            }

            @Override
            public void onFailure(Call<ReportModel> call, Throwable t) {
                Toast.makeText(getContext(), "فشل فى ارسال التقرير", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // make alerDialog
    public void setAlerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                .setTitle("تقرير علمى")
                .setMessage("هل تريد ارسال التقرير العلمى؟")
                .setCancelable(false)
                .setPositiveButton(" نعم ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sendNewReport();
                    }
                }).setNegativeButton("لا", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}