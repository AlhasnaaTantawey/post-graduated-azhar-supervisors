package azhar.supervisors.pg_azhar;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.pg_azhar.R;
import com.example.pg_azhar.databinding.FragmentResearcherInformationOneNoteBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class ResearcherInformationOneNoteDetails extends Fragment {
    FragmentResearcherInformationOneNoteBinding fragmentResearcherInformationOneNoteBinding;
    Retrofit retrofit;
    EndPoint endPoint;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentResearcherInformationOneNoteBinding= DataBindingUtil.inflate(inflater, R.layout.fragment_researcher_information_one_note, container, false);
        retrofit=Client.getRetrofit();
        endPoint=retrofit.create(EndPoint.class);
         getOneNote(recivedNoteId());
        return fragmentResearcherInformationOneNoteBinding.getRoot();

    }
    public void getOneNote (int noteId){
        Call<Note> call=endPoint.getOneNoteResearcherInformation(noteId);
        call.enqueue(new Callback<Note>() {
            @Override
            public void onResponse(Call<Note> call, Response<Note> response) {
                if(!response.isSuccessful()){
                    return;
                }
                Note note=response.body();
                handleData(note.getResearcherName(),fragmentResearcherInformationOneNoteBinding.ResearcherInformationOneNoteResearcherName);
                handleData(note.getRes_id()+"",fragmentResearcherInformationOneNoteBinding.ResearcherInformationOneNoteResearcherId);
                handleData(note.getFileNum()+"",fragmentResearcherInformationOneNoteBinding.ResearcherInformationOneNoteResearcherFileNum);
                handleData(note.getNote(),fragmentResearcherInformationOneNoteBinding.ResearcherInformationOneNoteResearcherNoteText);
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