package azhar.supervisors.pg_azhar;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.pg_azhar.R;
import com.example.pg_azhar.databinding.FragmentRequestDetailsBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RequestDetails extends Fragment {
    Retrofit retrofit;
    EndPoint endPoint;
    FragmentRequestDetailsBinding fragmentRequestDetailsBinding;
    RecievedRequestModel request;
    int requestId;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentRequestDetailsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_request_details, container, false);
        retrofit = Client.getRetrofit();
        endPoint = retrofit.create(EndPoint.class);
        recieveId();
        getRequestDetails(requestId);
        return fragmentRequestDetailsBinding.getRoot();
    }

    public void getRequestDetails(int id) {
        Call<RecievedRequestModel> call = endPoint.getRequestDetails(id);
        call.enqueue(new Callback<RecievedRequestModel>() {
            @Override
            public void onResponse(Call<RecievedRequestModel> call, Response<RecievedRequestModel> response) {
                if (!response.isSuccessful()) {
                    return;
                }

                request = response.body();
                handleType(request.getType(),fragmentRequestDetailsBinding.requestGroupviewTextViewRequestType);
                handleData(request.getResearcher_name(), fragmentRequestDetailsBinding.requestGroupviewTextViewResearcherName);
                handleData(request.getSent_date(), fragmentRequestDetailsBinding.requestGroupviewTextViewRequestDate);
                handleData(request.getDepartment_approvement(), fragmentRequestDetailsBinding.requestGroupviewTextViewDepartmentApprovement);
                handleData(request.getFaculity_approvement(), fragmentRequestDetailsBinding.requestGroupviewTextViewFaculityApprovement);
                handleData(request.getUniversity_approvement(), fragmentRequestDetailsBinding.requestGroupviewTextViewUniversityApprovement);

            }

            @Override
            public void onFailure(Call<RecievedRequestModel> call, Throwable t) {
            }
        });
    }

    public void recieveId() {
        if (getArguments() != null) {
            requestId = getArguments().getInt("RequestId");
        }
    }

    public void handleData(String text, TextView textView) {
        if (text == null) {
            textView.setText("_______");
        } else {
            textView.setText(text.toString());
        }
    }

    public void handleType(int number, TextView textView) {
        if (number == 1) {
            textView.setText("طلب مد");
        } else if (number == 2) {
            textView.setText("طلب تغير مشرفين");
        }else if(number==3){
            textView.setText("طلب تغير غير العنوان");

        }

    }
}