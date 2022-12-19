package azhar.supervisors.pg_azhar;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.pg_azhar.R;
import com.example.pg_azhar.databinding.FragmentChangeTitleBinding;

import azhar.supervisors.pg_azhar.ui.personalData.PersonalDataFragment;

import java.time.LocalDate;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ChangeTitleFragment extends Fragment {

    FragmentChangeTitleBinding binding;
    Retrofit retrofit;
    EndPoint endPoint;
    int  researcherId;
    String messageTitle;
    int selectedItemInRatioGroup=0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_change_title, container, false);
        retrofit = Client.getRetrofit();
        endPoint = retrofit.create(EndPoint.class);
        recieveData();
        binding.textViewChangeTitleFragmentOriginaltitle.setText(messageTitle);
        buttonSendRequest();


        return  binding.getRoot();
    }

    //retrofit method
    private  void changeTitleRequest(){
        ChangeTitleRequestModel changeTitleRequestModel=new ChangeTitleRequestModel();
        changeTitleRequestModel.setRes_id(researcherId);
        changeTitleRequestModel.setDeptId(PersonalDataFragment.supervisor.getDeptNumber());
        changeTitleRequestModel.setSup_id(SupervisorActivity.supervisorId);
        changeTitleRequestModel.setSent_date(LocalDate.now().toString());
        changeTitleRequestModel.setNewArabicAddress(binding.textViewChangeTitleFragmentNewArabictitle.getText().toString());
        changeTitleRequestModel.setNewEnglishAddress(binding.textViewChangeTitleFragmentNewEnglishtitle.getText().toString());
        changeTitleRequestModel.setOldAddress(messageTitle);
        changeTitleRequestModel.setIsSubstantial(selectedItemInRatioGroup);


        Call<ChangeTitleRequestModel> call=endPoint.changeTitleRequest(changeTitleRequestModel);
        call.enqueue(new Callback<ChangeTitleRequestModel>() {
            @Override
            public void onResponse(Call<ChangeTitleRequestModel> call, Response<ChangeTitleRequestModel> response) {

                if (!response.isSuccessful()) {
                    Toast.makeText(getContext(), "فشل فى طلب تغير عنوان الرساله ", Toast.LENGTH_LONG).show();
                }
                ChangeTitleRequestModel changeTitleRequestModel1 = response.body();
                if (changeTitleRequestModel1 != null) {
                    Toast.makeText(getContext(), "تم ارسال طلب تغير عنوان الرساله ", Toast.LENGTH_LONG).show();
                    getActivity().onBackPressed();
                } else {
                    Toast.makeText(getContext(), "فشل فى طلب تغير عنوان الرساله ", Toast.LENGTH_LONG).show();
                }

            }
            @Override
            public void onFailure(Call<ChangeTitleRequestModel> call, Throwable t) {

            }
        });
    }

    private void buttonSendRequest(){
        binding.buttonChangeTitleFragmentSedRequest.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                boolean isArabicAddressEmpty=binding.textViewChangeTitleFragmentNewArabictitle.getText().toString().isEmpty();
                boolean isEnglishAddressEmpty=binding.textViewChangeTitleFragmentNewEnglishtitle.getText().toString().isEmpty();
                boolean isRadio1ButtonSelect=binding.radio1ChangeTitleFragment.isChecked();
                boolean isRadio2ButtonSelect=binding.radio2ChangeTitleFragment.isChecked();
                if (isRadio1ButtonSelect&&!isArabicAddressEmpty&&!isEnglishAddressEmpty) {
                    android.util.Log.i("islam","islam 1");
                    selectedItemInRatioGroup=1;
                    setAlerDialog();
                } else if (isRadio2ButtonSelect&& !isArabicAddressEmpty&& !isEnglishAddressEmpty) {
                    android.util.Log.i("islam","islam 2");
                    selectedItemInRatioGroup=0;
                    setAlerDialog();
                }else{
                    Toast.makeText(getContext(),"من فضلك اكمل البيانات",Toast.LENGTH_LONG).show();
                    android.util.Log.i("islam","islam 3");
                }

            }
        });
    }

    public int recieveData() {
        if (getArguments() != null) {
            researcherId = getArguments().getInt(PersonalInformationForOneResearcher.RESEARCHERID);
            messageTitle=getArguments().getString(PersonalInformationForOneResearcher.TITLE);
        }
        return researcherId;
    }
    // make alerDialog
    public void setAlerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                .setTitle("طلب تغير عنوان الرساله")
                .setMessage("هل تريد طلب تغير عنوان الرساله؟")
                .setCancelable(false)
                .setPositiveButton(" نعم ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        changeTitleRequest();
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