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
import com.example.pg_azhar.databinding.FragmentSupervisorNoteForResearchersBinding;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class SupervisorNoteForResearchers extends Fragment {
   Retrofit retrofit;
   EndPoint endPoint;
    ArrayList<Note>listOfNotes;

    FragmentSupervisorNoteForResearchersBinding fragmentSupervisorNoteForResearchersBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentSupervisorNoteForResearchersBinding=DataBindingUtil.inflate(inflater, R.layout.fragment_supervisor_note_for_researchers, container, false);
        retrofit=Client.getRetrofit();
        endPoint=retrofit.create(EndPoint.class);
        getAllNote(AllSupervisorInOneDepartment.adminNumber);
        return  fragmentSupervisorNoteForResearchersBinding.getRoot();
    }
    public void getAllNote(int deptId){
        Call<ArrayList<Note>> call=endPoint.getAllNoteInOneDepattmentforSupervisorForResearchers(deptId);
        call.enqueue(new Callback<ArrayList<Note>>() {
            @Override
            public void onResponse(Call<ArrayList<Note>> call, Response<ArrayList<Note>> response) {
                if(!response.isSuccessful()){
                    return;
                }
                listOfNotes=response.body();
                setAdapter(listOfNotes);
            }

            @Override
            public void onFailure(Call<ArrayList<Note>> call, Throwable t) {

            }
        });
    }

    //pass the data to adapter and attach recyclerView and defind layout
    public void setAdapter(ArrayList<Note> data) {
        RecyclerViewForNote recyclerViewForNote = new RecyclerViewForNote(data,this);
        fragmentSupervisorNoteForResearchersBinding.SupervisorNoteForResearchersRecyclerview.setAdapter(recyclerViewForNote);
        fragmentSupervisorNoteForResearchersBinding.SupervisorNoteForResearchersRecyclerview.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        recyclerViewForNote.notifyDataSetChanged();
    }
}