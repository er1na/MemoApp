package com.example.memoapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private Button resetButton;
    private Button viewButton;
    private Button saveButton;

    private EditText inpTitle;
    private EditText inpPlace;
    private EditText inpMain;

    private Intent intent;

    String saveTitle = inpTitle.getText().toString();
    String savePlace = inpPlace.getText().toString();
    String saveMain = inpMain.getText().toString();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        connectView();
        reset();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveMemo();
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reset();
            }
        });

        viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(intent != null){
                    startActivity(intent);
                }
            }
        });

    }

    private void connectView(){

        resetButton = (Button) findViewById(R.id.resetButton);
        viewButton = (Button)findViewById(R.id.viewButton);
        saveButton = (Button) findViewById(R.id.saveButton);

        inpTitle = (EditText) findViewById(R.id.inpTitle);
        inpPlace = (EditText) findViewById(R.id.inpPlace);
        inpMain = (EditText) findViewById(R.id.inpMain);

    }

    private void reset(){

        inpTitle.setText("");
        inpPlace.setText("");
        inpMain.setText("");

        inpTitle.requestFocus();

    }

    private void saveMemo(){

        if(saveTitle == "" || savePlace == "" || saveMain == "") {
            DialogFragment confirmDialog = new ConfirmDialogFragment();
            confirmDialog.show(getSupportFragmentManager(), "confirm");
        }else{
            DBAdapter dbAdapter = new DBAdapter(this);
            dbAdapter.openDB();
            dbAdapter.saveDB(saveTitle, savePlace, saveMain);
            dbAdapter.closeDB();
        }

    }

    public class ConfirmDialogFragment extends DialogFragment {

        public Dialog OnCreateDialog(Bundle savedInstanceState){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.confirm_text)
                    .setPositiveButton(R.string.confirm_ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int id) {
                            DBAdapter dbAdapter = new DBAdapter(getContext());
                            dbAdapter.openDB();
                            dbAdapter.saveDB(saveTitle, savePlace, saveMain);
                            dbAdapter.closeDB();
                            reset();
                        }
                    })
                    .setNegativeButton(R.string.confirm_cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int id) {

                        }
                    });

            return builder.create();
        }

    }

}