package azhar.supervisors.pg_azhar;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pg_azhar.R;
import com.example.pg_azhar.databinding.FragmentAllChangeTitleRequestBinding;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class AllChangeTitleRequest extends Fragment {
FragmentAllChangeTitleRequestBinding fragmentAllChangeTitleRequestBinding;
Retrofit retrofit;
EndPoint endPoint;
ArrayList<RecievedRequestModel> listOfRequestsOnOneDepartmentForSupervisor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentAllChangeTitleRequestBinding= DataBindingUtil.inflate(inflater, R.layout.fragment_all_change_title_request, container, false);
        retrofit =Client.getRetrofit();
        endPoint=retrofit.create(EndPoint.class);
        getListOfRequest(AllSupervisorInOneDepartment.adminNumber);
        return fragmentAllChangeTitleRequestBinding.getRoot();
    }
    public void getListOfRequest(int id){
        Call<ArrayList<RecievedRequestModel>> call=endPoint.getAllRequestOnOneDepartmentForSupervisors(id,3);
        call.enqueue(new Callback<ArrayList<RecievedRequestModel>>() {
            @Override
            public void onResponse(Call<ArrayList<RecievedRequestModel>> call, Response<ArrayList<RecievedRequestModel>> response) {
                if(!response.isSuccessful()){
                    return;
                }
                listOfRequestsOnOneDepartmentForSupervisor=response.body();
                setAdapter(listOfRequestsOnOneDepartmentForSupervisor);

            }

            @Override
            public void onFailure(Call<ArrayList<RecievedRequestModel>> call, Throwable t) {

            }
        });
    }
    //pass the data to adapter and attach recyclerView and defind layout
    public void setAdapter(ArrayList<RecievedRequestModel> data) {
        RecyclerViewForRequest recyclerViewForRequest = new RecyclerViewForRequest(data,this);
        fragmentAllChangeTitleRequestBinding.allChangeTitleRequestsRecyclerview.setAdapter(recyclerViewForRequest);
        fragmentAllChangeTitleRequestBinding.allChangeTitleRequestsRecyclerview.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        recyclerViewForRequest.notifyDataSetChanged();
    }
}