package azhar.supervisors.pg_azhar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.cardview.widget.CardView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.pg_azhar.R;
import com.example.pg_azhar.databinding.FragmentShowAllRequestInOneDepartmentBinding;

import java.util.ArrayList;

import retrofit2.Retrofit;


public class ShowAllRequestInOneDepartment extends Fragment {
    FragmentShowAllRequestInOneDepartmentBinding fragmentShowAllRequestInOneDepartmentBinding;
    Retrofit retrofit;
    EndPoint endPoint;
    ArrayList<RecievedRequestModel>listOfRequestsOnOneDepartmentForSupervisor;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentShowAllRequestInOneDepartmentBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_show_all_request_in_one_department, container, false);
        retrofit=Client.getRetrofit();
        endPoint=retrofit.create(EndPoint.class);
        clickNoteButton(fragmentShowAllRequestInOneDepartmentBinding.allRequestInOneDepartmentExtendedRequest,new AllExtendsRequest());
        clickNoteButton(fragmentShowAllRequestInOneDepartmentBinding.allRequestInOneDepartmentChangeSupervisorsRequest,new AllChangeSupervisorsRequest());
        clickNoteButton(fragmentShowAllRequestInOneDepartmentBinding.allRequestInOneDepartmentChangeTitleRequest,new AllChangeTitleRequest());

        clickBack();
        return fragmentShowAllRequestInOneDepartmentBinding.getRoot();
    }

    public  void clickBack(){
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                setFragment(new ShowAllNotesOnOneDepartment());
            }
        });
    }
    public void setFragment(Fragment fragment){
       getFragmentManager()
                .beginTransaction()
                .replace(R.id.adminActivity_fragmetnArea,fragment)
                .commit();
    }



    public void clickNoteButton(CardView cardView, Fragment fragment) {
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager()
                        .beginTransaction()
                        .replace(R.id.adminActivity_fragmetnArea, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }
}