package com.example.ltrpmlogistics;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class collection extends AppCompatActivity {

    EditText filecoll;
    Button uploadfile;

    StorageReference storageReference;
    DatabaseReference databaseReference;
    private Object StorageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);

        filecoll=findViewById(R.id.filecoll);
        uploadfile=findViewById(R.id.uploadfile);

        storageReference=FirebaseStorage.getInstance().getReference();
        StorageReference=FirebaseStorage.getInstance().getReference("putPDF");

        uploadfile.setEnabled(false);

        filecoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectPDF();
            }
        });

    }

    private void selectPDF() {

        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "PDF FILE SELECT"), 12);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==12 && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            uploadfile.setEnabled(true);
            filecoll.setText(data.getDataString().substring(data.getDataString().lastIndexOf("/") + 1));

           uploadfile.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {

                   uploadPDFFileFirebase(data.getData());
               }
           });
        }
    }

    private void uploadPDFFileFirebase(Uri data) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("file is loading...");
        progressDialog.show();

        StorageReference reference=storageReference.child("upload"+System.currentTimeMillis() +".pdf");

        reference.putFile(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Task<Uri> uriTask= taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isComplete());
                        Uri uri= uriTask.getResult();

                        putPDF putPDF=new putPDF(filecoll.getText().toString(),uri.toString());
                        databaseReference.child(databaseReference.push().getKey()).setValue(putPDF);
                        Toast.makeText(collection.this, "file upload", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();

                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

                double progress=(100.0* snapshot.getBytesTransferred())/snapshot.getTotalByteCount();
                progressDialog.setMessage("file uploaded..." +(int) progress+"%");

            }
        });

    }
}