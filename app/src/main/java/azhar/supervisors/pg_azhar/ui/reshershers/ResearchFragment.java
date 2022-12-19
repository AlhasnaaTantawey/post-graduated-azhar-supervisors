package azhar.supervisors.pg_azhar.ui.reshershers;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import azhar.supervisors.pg_azhar.Client;
import azhar.supervisors.pg_azhar.EndPoint;
import com.example.pg_azhar.R;
import com.example.pg_azhar.databinding.FragmentReasearchersBinding;

import azhar.supervisors.pg_azhar.RecyclerViewForResearcher;
import azhar.supervisors.pg_azhar.ResearcherModel;
import azhar.supervisors.pg_azhar.SupervisorActivity;
import azhar.supervisors.pg_azhar.ui.personalData.PersonalDataFragment;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ResearchFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    Retrofit retrofit;
    EndPoint endPoint;
    ArrayList<ResearcherModel> allResearcherForSupervisor;
    RecyclerViewForResearcher recyclerViewForResearcher;
    ArrayList<ResearcherModel> allResearchers;
    ArrayList<ResearcherModel> continueResearcher;
    ArrayList<ResearcherModel> grantedResearchers;
    ArrayList<ResearcherModel> disconnectedResearchers;
    FragmentReasearchersBinding binding;
    ArrayList<ResearcherModel>allData;
    ArrayList<ResearcherModel>notRecordeddResearchers;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_reasearchers, container, false);
        retrofit = Client.getRetrofit();
        endPoint = retrofit.create(EndPoint.class);
        allData=new ArrayList<>();
        allResearchers = new ArrayList<>();
        continueResearcher = new ArrayList<>();
        grantedResearchers = new ArrayList<>();
        disconnectedResearchers=new ArrayList<>();
        notRecordeddResearchers=new ArrayList<>();
        getAllResearchersForOneSupervisor(SupervisorActivity.supervisorId);
        setSpinner();
        goBackToPersonalInformation(this);
        return binding.getRoot();
    }


    public static void goBackToPersonalInformation(Fragment fragment) {
       fragment. requireActivity().getOnBackPressedDispatcher().addCallback(fragment.getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                fragment.getFragmentManager().
                        beginTransaction()
                        .replace(R.id.nav_host_fragment_activity_supervisor,new PersonalDataFragment())
                        .commit();
            }
        });
    }


    //get Researcher list form Api for superviosr
    public void getAllResearchersForOneSupervisor(int id) {
        Call<ArrayList<ResearcherModel>> call = endPoint.getAllStudnets_sup(id);
        call.enqueue(new Callback<ArrayList<ResearcherModel>>() {
            @Override
            public void onResponse(Call<ArrayList<ResearcherModel>> call, Response<ArrayList<ResearcherModel>> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                allResearcherForSupervisor = response.body();

                allResearchers.addAll(allResearcherForSupervisor);
                allData.addAll(getContinueResearchers());
                allData.addAll(getGrantedResearchers());
                allData.addAll(getDisconnectedResearchers());
                allData.addAll(getNotRecorded());
                setAdapter(allData);
            }

            @Override
            public void onFailure(Call<ArrayList<ResearcherModel>> call, Throwable t) {
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (i == 0) {
            setAdapter(allData);
        } else if (i == 1) {
            setAdapter(getContinueResearchers());
        } else if (i == 2) {
            setAdapter(getGrantedResearchers());
        }  else if(i==3) {
            setAdapter(getDisconnectedResearchers());
        }else if(i==4){
            setAdapter(getNotRecorded());
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    // filter all researchers to get continueResearchers
    public ArrayList<ResearcherModel> getContinueResearchers() {
        continueResearcher.clear();
        for (int i = 0; i < allResearchers.size(); i++) {
            if (allResearchers.get(i).getIsGranted()!=null&&allResearchers.get(i).getIsCancelled()!=null&&allResearchers.get(i).getIsGranted() == 0&& allResearchers.get(i).getIsCancelled()!=-1) {
                continueResearcher.add(allResearchers.get(i));
            }
        }
        return continueResearcher;
    }

    // filter all researchers to get grantedResearchers
    public ArrayList<ResearcherModel> getGrantedResearchers() {
        grantedResearchers.clear();
        for (int i = 0; i < allResearchers.size(); i++) {
            if (allResearchers.get(i).getIsGranted()!=null&&allResearchers.get(i).getIsGranted() == -1) {
                grantedResearchers.add(allResearchers.get(i));
            }

        }
        return grantedResearchers;
    }

    //pass the data to adapter and attach recyclerView and defind layout
    public void setAdapter(ArrayList<ResearcherModel> data) {
        recyclerViewForResearcher = new RecyclerViewForResearcher(data, ResearchFragment.this);
        binding.fragmentResearcherforsupervisorRecyclerview.setAdapter(recyclerViewForResearcher);
        binding.fragmentResearcherforsupervisorRecyclerview.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        recyclerViewForResearcher.notifyDataSetChanged();
    }

    //spinner to filter the Researchers
    public void setSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.spinner_filterResearchers, R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerFilterResearcher.setAdapter(adapter);
        binding.spinnerFilterResearcher.setOnItemSelectedListener(this);
    }

    // filter all researchers to get disconnected
    public ArrayList<ResearcherModel> getDisconnectedResearchers() {
        disconnectedResearchers.clear();
        for (int i = 0; i < allResearchers.size(); i++) {
            if (allResearchers.get(i).getIsCancelled()!=null&&allResearchers.get(i).getIsCancelled()==-1) {
                disconnectedResearchers.add(allResearchers.get(i));
            }

        }
        return disconnectedResearchers;
    }
    // filter all researchers to get disconnected
    public ArrayList<ResearcherModel> getNotRecorded() {
        notRecordeddResearchers.clear();
        for (int i = 0; i < allResearchers.size(); i++) {
            if (allResearchers.get(i).getIsGranted()==null) {
                notRecordeddResearchers.add(allResearchers.get(i));
            }

        }
        return notRecordeddResearchers;
    }
}