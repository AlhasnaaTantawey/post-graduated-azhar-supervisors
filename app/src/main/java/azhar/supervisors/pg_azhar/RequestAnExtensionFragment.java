package azhar.supervisors.pg_azhar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pg_azhar.R;
import com.example.pg_azhar.databinding.FragmentRequestAnExtensionBinding;

import azhar.supervisors.pg_azhar.ui.personalData.PersonalDataFragment;

import java.time.LocalDate;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RequestAnExtensionFragment extends Fragment {
    FragmentRequestAnExtensionBinding binding;
    String registerationDate;
    int researcherId;
    Retrofit retrofit;
    EndPoint endPoint;
    int itemSelected=0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_request_an_extension, container, false);
        retrofit=Client.getRetrofit();
        endPoint=retrofit.create(EndPoint.class);
        receivedData();
        putDateIntextView(registerationDate,binding.requestAnExtensionFragmentTextViewSixDate,6);
        putDateIntextView(registerationDate,binding.requestAnExtensionFragmentTextViewSevenDate,7);
        putDateIntextView(registerationDate,binding.requestAnExtensionFragmentTextViewEightDate,8);
        addListenerOnButtonClick();
        return binding.getRoot();
    }

    public void receivedData() {
        if (getArguments() != null) {
             researcherId = getArguments().getInt(PersonalInformationForOneResearcher.RESEARCHERID);
             registerationDate = getArguments().getString(PersonalInformationForOneResearcher.RESTERATION_DATE);
        }
    }
    public void putDateIntextView(String date, TextView textView,int number){
        LocalDate localDate =LocalDate.parse(date);
        textView.setText(localDate.plusYears(number).toString());
    }

    private  void sendExtension(){
        ExtensionRequestModel extensionModel=new ExtensionRequestModel();
        extensionModel.setSup_id(SupervisorActivity.supervisorId);
        extensionModel.setExtend_period(itemSelected);
        extensionModel.setSent_date(LocalDate.now().toString());
        extensionModel.setRes_id(researcherId);
        extensionModel.setDeptId(PersonalDataFragment.supervisor.getDeptNumber());
        Call<ExtensionRequestModel> call = endPoint.getExtension(extensionModel);
        call.enqueue(new Callback<ExtensionRequestModel>() {
            @Override
            public void onResponse(Call<ExtensionRequestModel> call, Response<ExtensionRequestModel> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(getContext(),"فشل فى طلب المد ",Toast.LENGTH_LONG).show();
                }
                ExtensionRequestModel extensionRequestModel=response.body();
                if(extensionRequestModel!=null){
                    Toast.makeText(getContext(), "تم ارسال طلب المد ",Toast.LENGTH_LONG).show();
                    getActivity().onBackPressed();
                }
                else {
                    Toast.makeText(getContext(),"فشل فى طلب المد ",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ExtensionRequestModel> call, Throwable t) {

            }
        });

    }
    public void addListenerOnButtonClick(){

        //Applying the Listener on the Button click
        binding.buttonRequestAnExtensionFragmentSendRequest.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                if(binding.checkBox2RequestAnExtensionFragmentEight.isChecked()){
                    itemSelected=3;

                }
                else if(binding.checkBox3RequestAnExtensionFragmentSeven.isChecked()){
                    itemSelected=2;

                }
                else if(binding.checkBoxRequestAnExtensionFragmentSix.isChecked()){
                    itemSelected=1;
                }

                //Displaying the message on the toast
                if(itemSelected!=0){
                setAlerDialog();}
                else {
                    Toast.makeText(getContext(),"من فضلك اختار فتره المد",Toast.LENGTH_LONG).show();
                }
            }

        });
    }
    // make alerDialog
    public void setAlerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                .setTitle("طلب مد")
                .setMessage("هل تريد ارسال طلب مد؟")
                .setCancelable(false)
                .setPositiveButton(" نعم ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sendExtension();
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