package azhar.supervisors.pg_azhar.ui.personalData;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import azhar.supervisors.pg_azhar.Client;
import azhar.supervisors.pg_azhar.EndPoint;
import azhar.supervisors.pg_azhar.NoteForSupervisorsInformation;
import com.example.pg_azhar.R;
import com.example.pg_azhar.databinding.FragmentPersonalDataBinding;

import azhar.supervisors.pg_azhar.SupervisorActivity;
import azhar.supervisors.pg_azhar.SupervisorModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PersonalDataFragment extends Fragment {
    EndPoint endPoint;
    Retrofit retrofit;
    public static SupervisorModel supervisor;

    private FragmentPersonalDataBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_personal_data, container, false);
        retrofit = Client.getRetrofit();
        endPoint = retrofit.create(EndPoint.class);
        getPersonalInformation(SupervisorActivity.supervisorId);
        clickBack();
        floatingNoteButton();
        return binding.getRoot();
    }

    // get personal information for superviosr from API
    public void getPersonalInformation(int id) {
        Call<SupervisorModel> call = endPoint.getOneSupervisor(id);
        call.enqueue(new Callback<SupervisorModel>() {
            @Override
            public void onResponse(Call<SupervisorModel> call, Response<SupervisorModel> response) {
                if (!response.isSuccessful()) {
                    return;
                }

                 supervisor = response.body();
                handleData(supervisor.getName(),binding.textViewNameValue);
                handleData(supervisor.getDegree(),binding.textViewDegreeValue);
                handleData(supervisor.getDepartment(),binding.textViewDepartmentValue);
                binding.textViewIsExternalValue.setText("_______");
                binding.textViewIdValue.setText(supervisor.getId()+"");
                handleData(supervisor.getSpecialization(),binding.textViewSpecialityValue);
                handleData(supervisor.getOrg(),binding.textViewOrganizationValue);
                handleData(supervisor.getNationalid(),binding.textViewSsnValue);
            }

            @Override
            public void onFailure(Call<SupervisorModel> call, Throwable t) {

            }

        });

    }

    public  void handleData(String text, TextView textView) {
        if (text == null) {
            textView.setText("_______");
        } else {
            textView.setText(text.toString());
        }
    }
public void floatingNoteButton(){
        binding.personalDataInformationFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             setFragment(new NoteForSupervisorsInformation());
            }
        });
}
public void setFragment(Fragment fragment){
              getFragmentManager()
                .beginTransaction()
                .replace(R.id.nav_host_fragment_activity_supervisor,fragment)
                      .addToBackStack(null)
                .commit();
}
public  void clickBack(){
    requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
        @Override
        public void handleOnBackPressed() {
            requireActivity().finish();
        }
    });
}
}