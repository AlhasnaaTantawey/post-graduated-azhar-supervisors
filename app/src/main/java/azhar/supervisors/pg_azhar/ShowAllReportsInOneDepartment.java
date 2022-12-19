package azhar.supervisors.pg_azhar;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pg_azhar.R;
import com.example.pg_azhar.databinding.FragmentShowAllReportsInOneDepartmentBinding;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class ShowAllReportsInOneDepartment extends Fragment {
    FragmentShowAllReportsInOneDepartmentBinding fragmentShowAllReportsInOneDepartmentBinding;
    Retrofit retrofit;
    EndPoint endPoint;
    ArrayList<ReportModel>listOfReportsInOneDepartment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentShowAllReportsInOneDepartmentBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_show_all_reports_in_one_department, container, false);
        retrofit=Client.getRetrofit();
        endPoint=retrofit.create(EndPoint.class);
        getListOfReportOnOneDepartment(AllSupervisorInOneDepartment.adminNumber);
        clickBack();
        return fragmentShowAllReportsInOneDepartmentBinding.getRoot();
    }
    public void getListOfReportOnOneDepartment(int deptId){
        Call<ArrayList<ReportModel>> call=endPoint.getAllReportOnOneDepartment(deptId);
        call.enqueue(new Callback<ArrayList<ReportModel>>() {
            @Override
            public void onResponse(Call<ArrayList<ReportModel>> call, Response<ArrayList<ReportModel>> response) {
                if(!response.isSuccessful()){
                    return;
                }
                listOfReportsInOneDepartment=response.body();
                setAdapter(listOfReportsInOneDepartment);
            }

            @Override
            public void onFailure(Call<ArrayList<ReportModel>> call, Throwable t) {

            }
        });
    }
    //pass the data to adapter and attach recyclerView and defind layout
    public void setAdapter(ArrayList<ReportModel> data) {
        RecyclerViewForReport recyclerViewForReport = new RecyclerViewForReport(data,this);
        fragmentShowAllReportsInOneDepartmentBinding.showAllReportsInOneDepartmentRecyclerview.setAdapter(recyclerViewForReport);
        fragmentShowAllReportsInOneDepartmentBinding.showAllReportsInOneDepartmentRecyclerview.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        recyclerViewForReport.notifyDataSetChanged();
    }
    public void setFragment(Fragment fragment){
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.adminActivity_fragmetnArea,fragment)
                .commit();
    }
    public  void clickBack(){
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
              setFragment(new ShowAllNotesOnOneDepartment());
            }
        });
    }
}