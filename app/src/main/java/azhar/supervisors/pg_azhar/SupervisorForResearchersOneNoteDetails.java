package azhar.supervisors.pg_azhar;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.pg_azhar.R;
import com.example.pg_azhar.databinding.FragmentSupervisorForResearchersOneNoteDetailsBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class SupervisorForResearchersOneNoteDetails extends Fragment {
FragmentSupervisorForResearchersOneNoteDetailsBinding fragmentSupervisorForResearchersOneNoteDetailsBinding;
Retrofit retrofit;
EndPoint endPoint;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentSupervisorForResearchersOneNoteDetailsBinding= DataBindingUtil.inflate(inflater, R.layout.fragment_supervisor_for_researchers_one_note_details, container, false);
       retrofit=Client.getRetrofit();
       endPoint=retrofit.create(EndPoint.class);
       getOneNote(recivedNoteId());
        return  fragmentSupervisorForResearchersOneNoteDetailsBinding.getRoot();
    }
    public void getOneNote (int noteId){
        Call<Note> call=endPoint.getSuperviosrForResarcherAndResearcherForSuperviosrsOneNoteDetails(noteId);
        call.enqueue(new Callback<Note>() {
            @Override
            public void onResponse(Call<Note> call, Response<Note> response) {
                if(!response.isSuccessful()){
                    return;
                }
                Note note=response.body();
                handleData(note.getResearcherName(),fragmentSupervisorForResearchersOneNoteDetailsBinding.researcherForSuperviosrsOneDetailsResearcherName);
                handleData(note.getRes_id()+"",fragmentSupervisorForResearchersOneNoteDetailsBinding.researcherForSuperviosrsOneDetailsResearcherNum);
                handleData(note.getFileNum()+"",fragmentSupervisorForResearchersOneNoteDetailsBinding.researcherForSuperviosrsOneDetailsResearcherFileNum);
                handleData(note.getNote(),fragmentSupervisorForResearchersOneNoteDetailsBinding.researcherForSuperviosrsOneDetailsTextNote);
                handleData(note.getSupervisorName(),fragmentSupervisorForResearchersOneNoteDetailsBinding.researcherForSuperviosrsOneDetailsSupervisorName);
                handleData(note.getSu_id()+"",fragmentSupervisorForResearchersOneNoteDetailsBinding.researcherForSuperviosrsOneDetailsSupervisorNumber);
            }

            @Override
            public void onFailure(Call<Note> call, Throwable t) {

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
    public int recivedNoteId(){
        int id=0;
        if(getArguments()!=null){
            id=getArguments().getInt("NoteId");
        }
        return id;
    }

}