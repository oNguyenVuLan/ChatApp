package vulan.com.chatapp.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.RuntimeExecutionException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import vulan.com.chatapp.R;
import vulan.com.chatapp.util.Constants;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int MINIMUM_LENGTH = 6;
    private TextView mTextView;
    private Button mButtonSignUp;
    private Button mButtonSignIn;
    private EditText mTextPassword;
    private EditText mTextID;
    private static final int BLANK_STATE = 1, TRUE_STATE = 2, MINIMUM_LENGTH_STATE = 3;
    private FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    public static String sId, sPassword;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        findView();
        init();
    }

    private void findView() {
        mTextView = (TextView) findViewById(R.id.text_logo);
        mButtonSignUp = (Button) findViewById(R.id.sign_up_button);
        mButtonSignIn = (Button) findViewById(R.id.sign_in_button);
        mTextID = (EditText) findViewById(R.id.edt_email);
        mTextPassword = (EditText) findViewById(R.id.edt_password);
        mButtonSignUp.setOnClickListener(this);
        mButtonSignIn.setOnClickListener(this);
    }

    private void init() {
        mProgressDialog=new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage("Authenticating...");
        Typeface typeface = Typeface.createFromAsset(getAssets(), Constants.FONT);
        mTextView.setTypeface(typeface);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mProgressDialog!=null){
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onClick(View v) {
        String id = mTextID.getText().toString();
        String password = mTextPassword.getText().toString();
        switch (checkData(id, password)) {
            case BLANK_STATE:
                Toast.makeText(SignUpActivity.this, "Blank text", Toast.LENGTH_SHORT).show();
                break;
            case TRUE_STATE:
                switch (v.getId()) {
                    case R.id.sign_up_button:
                        signUp(id, password);
                        break;
                    case R.id.sign_in_button:
                        signIn(id, password);
                        Toast.makeText(SignUpActivity.this, "success ", Toast.LENGTH_SHORT).show();
                        break;
                }
                break;
            case MINIMUM_LENGTH_STATE:
                Toast.makeText(SignUpActivity.this, "Password or ID should be at least 6 characters  ", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void signUp(final String id, final String password) {
        mProgressDialog.show();
        mFirebaseAuth = FirebaseAuth.getInstance();

            mFirebaseAuth.createUserWithEmailAndPassword(id, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    mProgressDialog.dismiss();
                    if (task.isSuccessful()) {
                       // Toast.makeText(SignUpActivity.this, "success ", Toast.LENGTH_SHORT).show();
                        // signIn(id, password);
                        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        try{

                        }catch (Exception e){
                        Toast.makeText(SignUpActivity.this, "The email address is already in use by another account ", Toast.LENGTH_SHORT).show();
                    }
                    }
                }
            });


    }

    private int checkData(String id, String password) {
        if (id.equals("") || password.equals("")) {
            return BLANK_STATE;
        }
        if (id.length() < MINIMUM_LENGTH || password.length() < MINIMUM_LENGTH) {
            return MINIMUM_LENGTH_STATE;
        }
        return TRUE_STATE;
    }

    private void signIn(final String id, final String password) {

            mProgressDialog.show();
            mFirebaseAuth.signInWithEmailAndPassword(id, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    mProgressDialog.dismiss();
                    try{
                        if (!task.isSuccessful()) {
                            Toast.makeText(SignUpActivity.this, "" + task.getResult(), Toast.LENGTH_SHORT).show();
                        } else {
                            sId = id;
                            sPassword = password;
                            Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                    }catch (Exception e){
                        Toast.makeText(SignUpActivity.this, "Error:  "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }
}
