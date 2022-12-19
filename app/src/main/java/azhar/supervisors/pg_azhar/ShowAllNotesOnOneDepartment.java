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
import com.example.pg_azhar.databinding.FragmentShowAllNotesOnOneDepartmentBinding;

import java.util.ArrayList;

import retrofit2.Retrofit;


public class ShowAllNotesOnOneDepartment extends Fragment {

    FragmentShowAllNotesOnOneDepartmentBinding fragmentShowAllNotesOnOneDepartmentBinding;
    Retrofit retrofit;
    EndPoint endPoint;
    public ArrayList<Note> listOfNotes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentShowAllNotesOnOneDepartmentBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_show_all_notes_on_one_department, container, false);
        retrofit = Client.getRetrofit();
        endPoint = retrofit.create(EndPoint.class);
        clickNoteButton(fragmentShowAllNotesOnOneDepartmentBinding.allNoteInOneDepartmentTxtVNoteSupervisorInformation, new SupervisorInformationNote());
        clickNoteButton(fragmentShowAllNotesOnOneDepartmentBinding.allNoteInOneDepartmentTxtVNoteResearcherInformation,new ResearcherInformationNote());
        clickNoteButton(fragmentShowAllNotesOnOneDepartmentBinding.allNoteInOneDepartmentTxtVSuperviosrNoteForResearchers,new SupervisorNoteForResearchers());
        clickNoteButton(fragmentShowAllNotesOnOneDepartmentBinding.allNoteInOneDepartmentTxtVResearcherNoteForSuperviosrs,new ResearcherNoteForSupervisors());
        clickBack();

        return fragmentShowAllNotesOnOneDepartmentBinding.getRoot();
    }

    public void clickBack() {
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                requireActivity().finish();
            }
        });
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