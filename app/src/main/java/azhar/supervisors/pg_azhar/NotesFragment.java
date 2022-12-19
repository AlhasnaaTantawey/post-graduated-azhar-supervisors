package azhar.supervisors.pg_azhar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.example.pg_azhar.R;
import com.example.pg_azhar.databinding.FragmentNotesBinding;

import azhar.supervisors.pg_azhar.ui.personalData.PersonalDataFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;



public class NotesFragment extends Fragment {

    FragmentNotesBinding binding;
    Retrofit retrofit;
    EndPoint endPoint;
    Note note;
    int resId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_notes, container, false);
        retrofit = Client.getRetrofit();
        endPoint = retrofit.create(EndPoint.class);
        binding.buttonNotesFragmentFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(binding.edittextNotesFragmentNotes.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(),"يجب ان تكتب الملاحظه اولا",Toast.LENGTH_LONG).show();}
                else {
                    setAlerDialog();}
            }

        });


            resId = receveId();

        return binding.getRoot();
    }

    public void sendNote() {
        note = new Note();
        note.setRes_id(resId);
        note.setSu_id(SupervisorActivity.supervisorId);
        note.setNote(binding.edittextNotesFragmentNotes.getText().toString());
        note.setDeptId(PersonalDataFragment.supervisor.getDeptNumber());
        note.setNoteType(1);
        Call<Note> call = endPoint.sendNotes(note);
        call.enqueue(new Callback<Note>() {
            @Override
            public void onResponse(Call<Note> call, Response<Note> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(getContext(),"فشل فى ارسال الملاحظه ",Toast.LENGTH_LONG).show();
                    return;
                }
                Note note=response.body();
                if(note!=null){
                    Toast.makeText(getContext(), "تم ارسال الملاحظه ",Toast.LENGTH_LONG).show();
                    getActivity().onBackPressed();
                }
                else {
                    Toast.makeText(getContext(),"فشل فى ارسال الملاحظه ",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Note> call, Throwable t) {
                Toast.makeText(getContext(),"فشل فى ارسال الملاحظه ", Toast.LENGTH_LONG).show();
            }
        });

    }

    // make alerDialog
    public void setAlerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                .setTitle("تسجيل الملاحظه")
                .setMessage("هل تريد تسجيل الملاحظه؟")
                .setCancelable(false)
                .setPositiveButton(" نعم ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sendNote();

                    }
                }).setNegativeButton("لا", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    public int  receveId(){
        if(getArguments()!=null) {
            return getArguments().getInt(PersonalInformationForOneResearcher.RESEARCHERID);
        }else{
            return  0;
        }
    }
}