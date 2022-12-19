package azhar.supervisors.pg_azhar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.pg_azhar.R;
import com.example.pg_azhar.databinding.FragmentChangeSupervisorsBinding;

import azhar.supervisors.pg_azhar.ui.personalData.PersonalDataFragment;

import java.time.LocalDate;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ChangeSupervisorsFragment extends Fragment {

    FragmentChangeSupervisorsBinding changeSupervisorsBinding;
    Retrofit retrofit;
    EndPoint endPoint;
    ArrayList<SupervisorModel> allSupervisorForResearcher;
    ArrayList<SupervisorModel> allSuperviosrOnOneDepartment;
    ArrayList<String> listOfSupervisorName;
    int idResearcher;
    int deptResearcher;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        changeSupervisorsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_change_supervisors, container, false);
        allSuperviosrOnOneDepartment = new ArrayList<>();
        listOfSupervisorName = new ArrayList();
        retrofit = Client.getRetrofit();
        endPoint = retrofit.create(EndPoint.class);
        recevedData();
        showListOfSupervisorInEditText();
        getSupervisors_res(idResearcher);

        clickButtonRequest();

        return changeSupervisorsBinding.getRoot();
    }

    public void sendChangeSupervisorRequest(ChangeSupervisorsRequestModel changeSupervisorsRequestModel) {
        Call<ChangeSupervisorsRequestModel> call = endPoint.sendChangeSupervisorRequest(changeSupervisorsRequestModel);
        call.enqueue(new Callback<ChangeSupervisorsRequestModel>() {
            @Override
            public void onResponse(Call<ChangeSupervisorsRequestModel> call, Response<ChangeSupervisorsRequestModel> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getContext(), "فشل فى ارسال الطلب ", Toast.LENGTH_LONG).show();
                }
                ChangeSupervisorsRequestModel Request = response.body();
                if (Request != null) {
                    Toast.makeText(getContext(), "تم ارسال الطلب ", Toast.LENGTH_LONG).show();
                    getActivity().onBackPressed();
                } else {
                    Toast.makeText(getContext(), "فشل فى ارسال الطلب ", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ChangeSupervisorsRequestModel> call, Throwable t) {
                Toast.makeText(getContext(), "فشل فى ارسال الطلب ", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void createRequestObject() {
        ChangeSupervisorsRequestModel changeSupervisorsRequestModel = new ChangeSupervisorsRequestModel();
        changeSupervisorsRequestModel.setRes_id(idResearcher);
        changeSupervisorsRequestModel.setSup_id(SupervisorActivity.supervisorId);
        changeSupervisorsRequestModel.setNewSup1(changeSupervisorsBinding.editTextChangeSupervisorFragmentFirstSuper.getText().toString());
        changeSupervisorsRequestModel.setNewSup2(changeSupervisorsBinding.editTextChangeSupervisorFragmentSecondSuper.getText().toString());
        changeSupervisorsRequestModel.setSent_date(LocalDate.now().toString());
        changeSupervisorsRequestModel.setDeptId(PersonalDataFragment.supervisor.getDeptNumber());
        changeSupervisorsRequestModel.setOldSup1(allSupervisorForResearcher.get(0).getName());
        changeSupervisorsRequestModel.setOldSup2(allSupervisorForResearcher.get(1).getName());
        changeSupervisorsRequestModel.setReasons(changeSupervisorsBinding.editTextChangeSupervisorFragmentResons.getText().toString());
        int firstSuperviosrId=getIdSupervisor(changeSupervisorsBinding.editTextChangeSupervisorFragmentFirstSuper.getText().toString(),getlistOfSuperviosr(PersonalDataFragment.supervisor.getDeptNumber()));
        int secondSupervisorId=getIdSupervisor(changeSupervisorsBinding.editTextChangeSupervisorFragmentSecondSuper.getText().toString(),getlistOfSuperviosr(PersonalDataFragment.supervisor.getDeptNumber()));
        changeSupervisorsRequestModel.setFirstNewSupervisorId(firstSuperviosrId);
        changeSupervisorsRequestModel.setSecondNewSupervisorId(secondSupervisorId);

        if(firstSuperviosrId!=0&&secondSupervisorId!=0) {
            sendChangeSupervisorRequest(changeSupervisorsRequestModel);
        }else{
            Toast.makeText(getContext(), "فشل فى ارسال الطلب اختار من قائمه اسماء المشرفين", Toast.LENGTH_SHORT).show();
        }
    }

    public void recevedData() {
        if (getArguments() != null) {
            idResearcher = getArguments().getInt(PersonalInformationForOneResearcher.RESEARCHERID);
            deptResearcher = getArguments().getInt(PersonalInformationForOneResearcher.DEPT_ID);
            android.util.Log.i("islam", idResearcher + "     " + deptResearcher);
        }
    }

    public void getSupervisors_res(int id) {

        Call<ArrayList<SupervisorModel>> call = endPoint.getAllSupervisor_res(id);
        call.enqueue(new Callback<ArrayList<SupervisorModel>>() {
            @Override
            public void onResponse(Call<ArrayList<SupervisorModel>> call, Response<ArrayList<SupervisorModel>> response) {
                if (!response.isSuccessful()) {
                    return;
                }

                allSupervisorForResearcher = response.body();

                changeSupervisorsBinding.textViewChangeSupervisorFragmentCurrentSupervisors.setText(putTextViewSupervisor(allSupervisorForResearcher));

            }

            @Override
            public void onFailure(Call<ArrayList<SupervisorModel>> call, Throwable t) {
            }
        });

    }


    public void clickButtonRequest() {
        changeSupervisorsBinding.buttonChangeSupervisorFragmentSedRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean condition1 = !changeSupervisorsBinding.editTextChangeSupervisorFragmentFirstSuper.getText().toString().isEmpty();
                boolean condition2 = !changeSupervisorsBinding.editTextChangeSupervisorFragmentSecondSuper.getText().toString().isEmpty();
                boolean condition3 =!changeSupervisorsBinding.editTextChangeSupervisorFragmentResons.getText().toString().isEmpty();
                if (condition1 && condition2&&condition3) {
                    setAlerDialog();
                } else {
                    Toast.makeText(getContext(), "من فضلك اكمل ادخال البيانات", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public StringBuffer putTextViewSupervisor(ArrayList<SupervisorModel> data) {
        StringBuffer  supervisors=new StringBuffer("");
        for(int i=0;i<data.size();i++){
            supervisors.append(data.get(i).getName() +"\n");
        }
        return supervisors;
    }

    // make alerDialog
    public void setAlerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                .setTitle("طلب مد")
                .setMessage("هل موافق على طلب تغير المشرفين ؟")
                .setCancelable(false)
                .setPositiveButton(" نعم ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        createRequestObject();
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

    public ArrayList<String> getlistOfSuperviosrName(int adminNumber) {
        Call<ArrayList<SupervisorModel>> call = endPoint.getAllSuperviosrOnOneDepartment(adminNumber);
        call.enqueue(new Callback<ArrayList<SupervisorModel>>() {
            @Override
            public void onResponse(Call<ArrayList<SupervisorModel>> call, Response<ArrayList<SupervisorModel>> response) {
                if (!response.isSuccessful()) {
                    return;
                }

                allSuperviosrOnOneDepartment = response.body();
                for (int i = 0; i < allSuperviosrOnOneDepartment.size(); i++) {
                    listOfSupervisorName.add(allSuperviosrOnOneDepartment.get(i).getName());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<SupervisorModel>> call, Throwable t) {
            }
        });
        return listOfSupervisorName;

    }

    // shwo list of supervisor to select when researcher make change to andother supervisor
    public void showListOfSupervisorInEditText() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (getContext(), android.R.layout.simple_list_item_1, getlistOfSuperviosrName(deptResearcher));
        changeSupervisorsBinding.editTextChangeSupervisorFragmentFirstSuper.setAdapter(adapter);
        changeSupervisorsBinding.editTextChangeSupervisorFragmentSecondSuper.setAdapter(adapter);
    }
    public int getIdSupervisor(String name,ArrayList<SupervisorModel> list){
        int idSuperviosr=0;
        for(int i=0;i<list.size();i++)
            if(name.equals(list.get(i).getName())){
                idSuperviosr=list.get(i).getId();
                break;
            }
        return idSuperviosr;
    }

    public ArrayList<SupervisorModel> getlistOfSuperviosr(int adminNumber) {
        Call<ArrayList<SupervisorModel>> call = endPoint.getAllSuperviosrOnOneDepartment(adminNumber);
        call.enqueue(new Callback<ArrayList<SupervisorModel>>() {
            @Override
            public void onResponse(Call<ArrayList<SupervisorModel>> call, Response<ArrayList<SupervisorModel>> response) {
                if (!response.isSuccessful()) {
                    return;
                }

                allSuperviosrOnOneDepartment = response.body();


            }

            @Override
            public void onFailure(Call<ArrayList<SupervisorModel>> call, Throwable t) {
            }
        });
        return allSuperviosrOnOneDepartment;

    }

}