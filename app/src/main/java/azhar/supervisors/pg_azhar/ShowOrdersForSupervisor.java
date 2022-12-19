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
import com.example.pg_azhar.databinding.FragmentShowOrdersForSupervisorBinding;

import azhar.supervisors.pg_azhar.ui.reshershers.ResearchFragment;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class ShowOrdersForSupervisor extends Fragment {

    FragmentShowOrdersForSupervisorBinding fragmentShowOrdersForSupervisorBinding;
    RecyclerViewForRequest recyclerViewForRequest;
    Retrofit retrofit;
    EndPoint endPoint;
    public static final String REQUESTID="requestId";
    ArrayList<RecievedRequestModel> data;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentShowOrdersForSupervisorBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_show_orders_for_supervisor, container, false);
        retrofit = Client.getRetrofit();
        endPoint = retrofit.create(EndPoint.class);
        getAllRequestForSupervisor(SupervisorActivity.supervisorId);
        ResearchFragment.goBackToPersonalInformation(this);
        return fragmentShowOrdersForSupervisorBinding.getRoot();
    }

    //pass the data to adapter and attach recyclerView and defind layout
    public void setAdapter(ArrayList<RecievedRequestModel> data) {
        recyclerViewForRequest = new RecyclerViewForRequest(data,this);
        fragmentShowOrdersForSupervisorBinding.fragmentResearcherforsupervisorRecyclerview.setAdapter(recyclerViewForRequest);
        fragmentShowOrdersForSupervisorBinding.fragmentResearcherforsupervisorRecyclerview.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        recyclerViewForRequest.notifyDataSetChanged();
    }

    public void getAllRequestForSupervisor(int id) {
        Call<ArrayList<RecievedRequestModel>> call = endPoint.getAllRequestsForOneSupervisor(id);
        call.enqueue(new Callback<ArrayList<RecievedRequestModel>>() {
            @Override
            public void onResponse(Call<ArrayList<RecievedRequestModel>> call, Response<ArrayList<RecievedRequestModel>> response) {
                if (!response.isSuccessful()) {
                    return;
                }

                data = response.body();
                setAdapter(data);

            }

            @Override
            public void onFailure(Call<ArrayList<RecievedRequestModel>> call, Throwable t) {
            }
        });
    }

}