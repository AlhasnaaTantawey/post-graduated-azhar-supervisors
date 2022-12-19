package azhar.supervisors.pg_azhar;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.pg_azhar.R;
import com.example.pg_azhar.databinding.FragmentNotesInformaitonBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class NotesInformaitonFragment extends Fragment {
    FragmentNotesInformaitonBinding fragmentNotesInformaitonBinding;
    Retrofit retrofit;
    EndPoint endPoint;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentNotesInformaitonBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_notes_informaiton, container, false);
        retrofit = Client.getRetrofit();
        endPoint = retrofit.create(EndPoint.class);
        getNote(recieveNoteId());
        return fragmentNotesInformaitonBinding.getRoot();
    }

    public void getNote(int notId) {
        Call<Note> call = endPoint.getNote(notId);
        call.enqueue(new Callback<Note>() {
            @Override
            public void onResponse(Call<Note> call, Response<Note> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                Note note = response.body();
                setDepartmentNameText(note.getDeptId(),fragmentNotesInformaitonBinding.departmentName);
                handleData(note.getSupervisorName(), fragmentNotesInformaitonBinding.noteInformationFragmentTxtVSupervisorName);
                handleData(note.getResearcherName(), fragmentNotesInformaitonBinding.noteInformationFragmentTxtVResaercherName);
                handleData(note.getNote(), fragmentNotesInformaitonBinding.noteInformationFragmentTxtVNoteText);
                fragmentNotesInformaitonBinding.noteInformationFragmentTxtVSupervisorId.setText(note.getSu_id() + "");
                fragmentNotesInformaitonBinding.noteInformationFragmentTxtVResearcherId.setText(note.getRes_id()+"");
                fragmentNotesInformaitonBinding.noteInformationFragmentTxtVResearcherFileNum.setText(note.getFileNum()+"");
            }

            @Override
            public void onFailure(Call<Note> call, Throwable t) {

            }
        });
    }

    public void handleData(String text, TextView textView) {
        if (text == null) {
            textView.setText("_______");
        } else {
            textView.setText(text.toString());
        }
    }
    public int recieveNoteId(){
        int noteId=0;
        if(getArguments()!=null){
            noteId=  getArguments().getInt("NoteId");
        }
        return noteId;
    }
    // set name of department to note details
    public static void setDepartmentNameText(int d,TextView textView) {
        if(d==2){
            textView.setText(" قسم هندسه تخطيط");
        }else if(d==3){
            textView.setText(" قسم الهندسه المدنيه");
        }else if(d==4){
            textView.setText(" قسم الهندسه الميكانيكيه");
        }else if(d==5){
            textView.setText(" قسم الهندسه الكهربيه");
        }else if(d==6){
            textView.setText(" قسم هندسه بترول وتعدين");
        }else if(d==7){
            textView.setText(" قسم الهندسه المعماريه");
        }else if(d==8){
            textView.setText(" قسم هندسه نظم وحاسبات");
        }
    }
}