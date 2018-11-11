package itpdm.timemanager;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import itpdm.timemanager.MainActivity.Login;

import static android.app.Activity.RESULT_OK;

public class Profile extends Fragment {
    private FirebaseAuth mAuth;
    private int RESULT_LOAD_IMAGE=1;
    View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_profile, container, false);
        mAuth = FirebaseAuth.getInstance();
        final DatabaseReference mDatabases;
        mDatabases = FirebaseDatabase.getInstance().getReference();
        ImageView image=rootView.findViewById(R.id.profileimage);
        final EditText email=rootView.findViewById(R.id.email);
        final EditText pass=rootView.findViewById(R.id.pass);
        final EditText confirm=rootView.findViewById(R.id.confirmpass);
        Button update=rootView.findViewById(R.id.updateprofilebtn);
        Button delete=rootView.findViewById(R.id.btndeleteaccount);
        update.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pass.getText().toString().equals(confirm.getText().toString())) {
                    mAuth.getCurrentUser().updateEmail(email.getText().toString());
                    mAuth.getCurrentUser().updatePassword(pass.getText().toString());
                    Snackbar.make(v, "Account Updated!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                else{
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getContext());
                    alertDialogBuilder.setMessage("Re-type confirm passwords");
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
            }
        });
        delete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                //Uncomment the below code to Set the message and title from the strings.xml file
                builder.setMessage("Everything will wipe!!!").setTitle("Warning");

                //Setting message manually and performing action on button click
                builder.setMessage("Do you want to proceed this application ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogs, int id) {
                                mDatabases.child("UserInfo").child(mAuth.getCurrentUser().getEmail().toString().split("@")[0]).removeValue();
                                mDatabases.child("TaskDetails").child(mAuth.getCurrentUser().getEmail().toString().split("@")[0]).removeValue();
                                mDatabases.child("notifications").child(mAuth.getCurrentUser().getEmail().toString().split("@")[0]).removeValue();
                                mAuth.getCurrentUser().delete();
                                AlertDialog.Builder mBuilders = new AlertDialog.Builder(getContext());
                                View mView = getLayoutInflater().inflate(R.layout.activity_login, null);
                                mBuilders.setView(mView);
                                final AlertDialog dialog = mBuilders.create();
                                Login.Login(mView, dialog);
                                dialog.show();
                                dialogs.cancel();
                                dialog.setCanceledOnTouchOutside(false);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogs, int id) {
                                dialogs.cancel();
                                Toast.makeText(getContext().getApplicationContext(), "you choose no action for alertbox",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.setTitle("Warning");
                alert.show();

            }
        }

        );
        image.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), RESULT_LOAD_IMAGE);

            }

        });

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContext().getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            ImageView imageView = (ImageView)rootView.findViewById(R.id.profileimage);
            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));

        }


    }
}
