package run.example.shilo.finalprogect;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.games.AchievementsClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.LeaderboardsClient;
import com.google.android.gms.games.Player;
import com.google.android.gms.games.leaderboard.Leaderboard;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import java.util.ArrayList;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener{
    private static final int RC_LEADERBOARD_UI = 9004;
    private AdView mAdView,mAdView1,mAdView2;
    AnimationDrawable man;
    ImageView man_image;
    Intent go;
    int RC_SIGN_IN=0;
    TextView Title,bestScore,ContactTitel,Tutorial;
    public boolean audio = true,vibration = true, music=true;
    private static final String TAG = "MainActivity";
    ConstraintLayout layout;
    ConstraintLayout layout1,layout2,layout3;
    String lang = "";
    private EditText editTextEmail;
    private EditText editTextSubject;
    private EditText editTextMessage;
    double widthScreen;
    LeaderboardsClient mLeaderboardsClient;
    GoogleSignInClient mGoogleSignInClient;
    GoogleSignInAccount mGoogleSignInAccount;
    GoogleApiClient mGoogleApiClient;
    ArrayList<String> a;
    SharedPreferences prefs;
    ImageButton soundPic,vibrationPic,musicPic;
    //Send button
    private Button buttonSend;
    GoogleSignInOptions gso;
    private AchievementsClient mAchievementsClient;
    SignInButton signInButton = findViewById(R.id.sign_in_button);
    @Override
    protected void onCreate(Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            try {
                this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            } catch (Exception e) {
            }
            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            widthScreen = size.x;
            MobileAds.initialize(this, "ca-app-pub-9142758453140162~8087689106");
            mAdView1 = findViewById(R.id.adView1);
            mAdView = findViewById(R.id.adView);
            mAdView2=findViewById(R.id.adView2);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
            mAdView1.loadAd(adRequest);
            mAdView2.loadAd(adRequest);
            go = new Intent(this, Game.class);
            Title = (TextView) findViewById(R.id.textView);
            Typeface myfont = Typeface.createFromAsset(getAssets(), "font1.otf");
            Title.setTypeface(myfont);
            bestScore = (TextView) findViewById(R.id.textView3);
            ContactTitel = (TextView) findViewById(R.id.textView8);
            Tutorial = (TextView) findViewById(R.id.textView20);
            Typeface myfont1 = Typeface.createFromAsset(getAssets(), "quitemegical.ttf");
            bestScore.setTypeface(myfont1);
            ContactTitel.setTypeface(myfont1);
            Tutorial.setTypeface(myfont1);
            ShowBestScore();
            man_image = (ImageView) findViewById(R.id.image1);
            man_image.setBackgroundResource(R.drawable.animation);
            man = (AnimationDrawable) man_image.getBackground();
            layout = findViewById(R.id.main_layout);
            layout1 = findViewById(R.id.main_layout1);
            layout2 = findViewById(R.id.main_layout2);
            layout3 = findViewById(R.id.main_layout3);
            //-------------- Email --------------
            //Initializing the views
            editTextEmail = (EditText) findViewById(R.id.editTextEmail);
            editTextSubject = (EditText) findViewById(R.id.editTextSubject);
            editTextMessage = (EditText) findViewById(R.id.editTextMessage);
            buttonSend = (Button) findViewById(R.id.buttonSend);
            soundPic =(ImageButton) findViewById(R.id.sound);
            musicPic =(ImageButton) findViewById(R.id.music);
            vibrationPic =(ImageButton) findViewById(R.id.vibration);
            //Adding click listener
            signInButton = findViewById(R.id.sign_in_button);
            buttonSend.setOnClickListener((View.OnClickListener) this);
            editTextEmail.setText("");
            editTextMessage.setText("");
            editTextSubject.setText("");
            layout.setVisibility(View.VISIBLE);
            layout1.setVisibility(View.GONE);
            layout2.setVisibility(View.GONE);
            layout3.setVisibility(View.GONE);
            prefs = this.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
            vibration=prefs.getBoolean("vibration", true);
            if(vibration)
                vibrationPic.setBackgroundResource(R.drawable.vibration1);
            else
                vibrationPic.setBackgroundResource(R.drawable.vibrationdark);

            audio=prefs.getBoolean("vibration", true);
            if(audio)
                soundPic.setBackgroundResource(R.drawable.sound);
            else
                soundPic.setBackgroundResource(R.drawable.sounddark);
            music = prefs.getBoolean("music",true);
            if(music)
                musicPic.setBackgroundResource(R.drawable.music);
            else
                musicPic.setBackgroundResource(R.drawable.musicdark);
            StopOpenGame();
            a = new ArrayList<>();

            /*GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();
            mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
            signIn();
            Log.d("ddd",""+mLeaderboardsClient);*/

        //startSignInIntent();
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        /*GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();*/
        //Scope s = new Scope("708959726924-mkep207e6fmvrmbajr6mn571r7amo40j.apps.googleusercontent.com");
        /*gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        // Set the dimensions of the sign-in button.

        signInButton.setSize(SignInButton.SIZE_STANDARD);*/
        //----------------------------------------------------------
        // Create the client used to sign in to Google services.
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
     //           .requestIdToken("708959726924-8gn4iafqkekg6c2aivfd7833a2n3qc5p.apps.googleusercontent.com")
                .requestScopes(Games.SCOPE_GAMES_LITE)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        //startSignInIntent();
        signIn();
        //----------------------------------------------------------
        //signIn();
        /*mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks((GoogleApiClient.ConnectionCallbacks) this)
                .addOnConnectionFailedListener((GoogleApiClient.OnConnectionFailedListener) this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .addApi(Games.API)
                .addScope(Games.SCOPE_GAMES)
                .setViewForPopups(findViewById(android.R.id.content)).useDefaultAccount()
                .build();
        mGoogleApiClient.connect();*/
        //connect_to_leader();
        //Log.e("e","signInResult:sussed code= "+mGoogleApiClient.isConnected());
    }

    private void showLeaderboard() {
        Games.getLeaderboardsClient(this, GoogleSignIn.getLastSignedInAccount(this))
                .getLeaderboardIntent("CgkIzNqritEUEAIQAQ")
                .addOnSuccessListener(new OnSuccessListener<Intent>() {
                    @Override
                    public void onSuccess(Intent intent) {
                        startActivityForResult(intent, RC_LEADERBOARD_UI);
                    }
                });
    }
    //- ------------------------
    public void onClick123(View v) {

        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
            // ...
        }
    }

    private void signIn() {
        Log.d(TAG,"aaaaaaaaaaaaaaaaaaaaa");
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    /*@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }*/
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            mGoogleSignInAccount = completedTask.getResult(ApiException.class);
            Log.w(TAG, "signInResult:sussed code=" + "12");
            bestScore.setText("signInResult: sussed");

            // Signed in successfully, show authenticated UI.
       //     updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e);
            bestScore.setText("signInResult: fail. " + e);
         //   updateUI(null);
        }
    }
    //-- -----------------------
    /*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // The signed in account is stored in the result.
                GoogleSignInAccount signedInAccount = result.getSignInAccount();
            } else {
                String message = result.getStatus().getStatusMessage();
                if (message == null || message.isEmpty()) {

                }
                new AlertDialog.Builder(this).setMessage(message)
                        .setNeutralButton(android.R.string.ok, null).show();
            }
        }
    }
*/
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG,"bbbbbbbbbbbbbbbb "+ requestCode);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    /*private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            mGoogleSignInAccount = completedTask.getResult(ApiException.class);
            // Signed in successfully, show authenticated UI.
            mLeaderboardsClient = Games.getLeaderboardsClient((Activity) this, mGoogleSignInAccount);
            SharedPreferences prefs = this.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
            Log.d("dddd",""+mLeaderboardsClient);
            Log.d("dddd","eeeee");
            Log.d("dddd",mGoogleSignInAccount+"");
            int score = prefs.getInt("easy", 0);
            Games.getLeaderboardsClient(this, GoogleSignIn.getLastSignedInAccount(this))
                    .submitScore("CgkI_Zjyy4kEEAIQAQ", score);

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.d(TAG, "signInResult:failed code=" + e.getStatusCode());

        }
    }*/

    public void StopOpenGame(){
        layout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        man.start();
    }

    private void sendEmail() {
            String email = editTextEmail.getText().toString().trim();
            String subject = " send from :" + email + " ," + editTextSubject.getText().toString().trim();
            String message = editTextMessage.getText().toString().trim();
            SendMail sm = new SendMail(this, email, subject, message, widthScreen);
            sm.execute();


    }

    @Override
    public void onClick(View v) {
        if (!isValid(editTextEmail.getText().toString())) {
            Toast t = Toast.makeText(this, "Invalid email address.", Toast.LENGTH_SHORT);
            t.setGravity(Gravity.CENTER, 0, 0);
            ViewGroup group = (ViewGroup) t.getView();
            TextView messageTextView = (TextView) group.getChildAt(0);
            messageTextView.setTextSize((int)widthScreen/36);
            t.show();
        }
        else if(editTextMessage.getText().toString().equals("")) {

            Toast t = Toast.makeText(this, "There must be something written in the message.", Toast.LENGTH_SHORT);
            t.setGravity(Gravity.CENTER, 0, 0);
            ViewGroup group = (ViewGroup) t.getView();
            TextView messageTextView = (TextView) group.getChildAt(0);
            messageTextView.setTextSize((int)widthScreen/36);
            t.show();
        }
        else
            sendEmail();
    }

    public static boolean isValid(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null || email.equals(""))
            return false;
        return pat.matcher(email).matches();
    }

    // can't go Back
    public void onBackPressed(){
        if(layout1.isShown()||layout2.isShown())
            BackToMain(findViewById(android.R.id.content));
        else if (layout.isShown()) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    // go to Game Activity easy game
    public void GoToGameEasy(View view) {
        /*try{
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .addApi(Games.API)
                    .addScope(Games.SCOPE_GAMES)
                    .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                            Log.e(TAG, "Could not connect to Play games services "+connectionResult);
                            finish();
                        }
                    }).build();
            mGoogleApiClient.connect();
            Log.e(TAG, "connect: " + mGoogleApiClient.isConnected());}
        catch (Exception e){Log.e(TAG, "error: " + e);}*/
        try{
        showLeaderboard();}
        catch (Exception e){bestScore.setText(e+"");}
        /*try {
            a.add("Easy");
            a.add("main");
            go.putExtra("kind",a);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("GamePlayed", prefs.getInt("GamePlayed", 0)+1);
            editor.commit();
            startActivity(go);

        }
        catch(Exception e){
            new AlertDialog.Builder(this).setMessage(""+e)
                    .setNeutralButton(android.R.string.ok, null).show();;
        }*/
    }

    // go to Game Activity normal game
    public void GoToGameNormal(View view) {
        try {
            a.add("Medium");
            a.add("main");
            go.putExtra("kind",a);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("GamePlayed", prefs.getInt("GamePlayed", 0)+1);
            editor.commit();
            startActivity(go);

        }
        catch(Exception e){
            new AlertDialog.Builder(this).setMessage(""+e)
                    .setNeutralButton(android.R.string.ok, null).show();;
        }
    }

    // go to Game Activity hard game
    public void GoToGameHard(View view) {
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("708959726924-8gn4iafqkekg6c2aivfd7833a2n3qc5p.apps.googleusercontent.com")
                .requestScopes(Games.SCOPE_GAMES_LITE)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        //startSignInIntent();
        signIn();
        /*try {
            a.add("Hard");
            a.add("main");
            go.putExtra("kind",a);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("GamePlayed", prefs.getInt("GamePlayed", 0)+1);
            editor.commit();
            startActivity(go);

        }
        catch(Exception e){
            new AlertDialog.Builder(this).setMessage(""+e)
                    .setNeutralButton(android.R.string.ok, null).show();
        }*/
    }

//-----------------------
    public void ShowBestScore(){
        SharedPreferences prefs = this.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        bestScore.setText("Best scores :\n" +prefs.getInt("easy", 0)+" | "+ prefs.getInt("medium", 0)+" | "+ prefs.getInt("hard", 0) +"\nGames played : " + prefs.getInt("GamePlayed", 0));
    }

    public void GoToSettings(View view) {
        layout3.setVisibility(View.VISIBLE);
        layout.setVisibility(View.GONE);
    }


    public void GoToEmail(View view) {
        //layout.setVisibility(View.GONE);
        layout.animate()
                .translationY(layout.getHeight())
                .setDuration(500)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        layout.setVisibility(View.GONE);
                        layout1.setVisibility(View.VISIBLE);
                    }
                });

    }

    public void BackToMain(View view) {
        editTextEmail.setText("");
        editTextMessage.setText("");
        editTextSubject.setText("");
        if (layout1.isShown())
            layout1.setVisibility(View.GONE);
        else
            layout2.setVisibility(View.GONE);

        layout.setVisibility(View.VISIBLE);
        layout.animate()
                .translationY(0)
                .setDuration(500)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                    }
                });
        final View activityRootView = findViewById(android.R.id.content);

                Rect r = new Rect();
                //r will be populated with the coordinates of your view that area still visible.
                activityRootView.getWindowVisibleDisplayFrame(r);
                int heightDiff = activityRootView.getRootView().getHeight() - (r.bottom - r.top);
                if (heightDiff > 100) { // if more than 100 pixels, its probably a keyboard...
                    Log.d("ddd","keyboard is on");
                    View v = activityRootView;
                    if (v != null) {
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }
                }

        }

    public void GoToTutorial(View view) {

        layout.animate()
                .translationY(layout.getHeight())
                .setDuration(500)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        layout.setVisibility(View.GONE);
                        layout2.setVisibility(View.VISIBLE);
                    }
                });
    }

    public void sound(View view) {
        SharedPreferences prefs = this.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        audio=prefs.getBoolean("audio", true);
        if (audio) {
            soundPic.setBackgroundResource(R.drawable.sounddark);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("audio", !audio);
            editor.commit();
        }
        else{
            soundPic.setBackgroundResource(R.drawable.sound);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("audio", !audio);
            editor.commit();
        }

}

    public void vibration(View view) {
        SharedPreferences prefs = this.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        vibration=prefs.getBoolean("vibration", true);
        if (vibration) {
            vibrationPic.setBackgroundResource(R.drawable.vibrationdark);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("vibration", !vibration);
            editor.commit();
        }
        else{
            vibrationPic.setBackgroundResource(R.drawable.vibration1);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("vibration", !vibration);
            editor.commit();
        }

    }

    public void music(View view) {
        SharedPreferences prefs = this.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        music=prefs.getBoolean("music", true);
        if (music) {
            musicPic.setBackgroundResource(R.drawable.musicdark);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("music", !music);
            editor.commit();
        }
        else{
            musicPic.setBackgroundResource(R.drawable.music);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("music", !music);
            editor.commit();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.w(TAG,"signInResult:failed code= amit connected ");
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.w(TAG, "signInResult:failed code= amit " + connectionResult);
    }


// ----------------------
private void signInSilently() {
    Log.d(TAG, "signInSilently()");
    mGoogleSignInClient.silentSignIn().addOnCompleteListener(this,
            new OnCompleteListener<GoogleSignInAccount>() {
                @Override
                public void onComplete(@NonNull Task<GoogleSignInAccount> task) {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "signInSilently(): success");
                        onConnected(task.getResult());
                    } else {
                        Log.d(TAG, "signInSilently(): failure", task.getException());
                        onDisconnected();
                    }
                }
            });
}
    private void startSignInIntent() {
        startActivityForResult(mGoogleSignInClient.getSignInIntent(), RC_SIGN_IN);
    }
    private void signOut() {
        Log.d(TAG, "signOut()");

        if (!isSignedIn()) {
            Log.w(TAG, "signOut() called, but was not signed in!");
            return;
        }

        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        boolean successful = task.isSuccessful();
                        Log.d(TAG, "signOut(): " + (successful ? "success" : "failed"));

                        onDisconnected();
                    }
                });
    }
    private boolean isSignedIn() {
        return GoogleSignIn.getLastSignedInAccount(this) != null;
    }
    /*@Override
    public void onShowLeaderboardsRequested() {
        mLeaderboardsClient.getAllLeaderboardsIntent()
                .addOnSuccessListener(new OnSuccessListener<Intent>() {
                    @Override
                    public void onSuccess(Intent intent) {
                        startActivityForResult(intent, RC_UNUSED);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        handleException(e, getString(R.string.leaderboards_exception));
                    }
                });
    }
*/
    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task =
                    GoogleSignIn.getSignedInAccountFromIntent(intent);

            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                onConnected(account);
            } catch (ApiException apiException) {
                String message = apiException.getMessage();
                if (message == null || message.isEmpty()) {
                    message = "Erroreeeeeeeee";//getString(R.string.signin_other_error);
                }

                onDisconnected();

                new AlertDialog.Builder(this)
                        .setMessage(message)
                        .setNeutralButton(android.R.string.ok, null)
                        .show();
            }
        }
    }*/
    private void onConnected(GoogleSignInAccount googleSignInAccount) {
        Log.d(TAG, "onConnected(): connected to Google APIs");

        mAchievementsClient = Games.getAchievementsClient(this, googleSignInAccount);
        mLeaderboardsClient = Games.getLeaderboardsClient(this, googleSignInAccount);
        /*mEventsClient = Games.getEventsClient(this, googleSignInAccount);
        mPlayersClient = Games.getPlayersClient(this, googleSignInAccount);

        // Show sign-out button on main menu
        mMainMenuFragment.setShowSignInButton(false);

        // Show "you are signed in" message on win screen, with no sign in button.
        mWinFragment.setShowSignInButton(false);

        // Set the greeting appropriately on main menu
        mPlayersClient.getCurrentPlayer()
                .addOnCompleteListener(new OnCompleteListener<Player>() {
                    @Override
                    public void onComplete(@NonNull Task<Player> task) {
                        String displayName;
                        if (task.isSuccessful()) {
                            displayName = task.getResult().getDisplayName();
                        } else {
                            Exception e = task.getException();
                            handleException(e, getString(R.string.players_exception));
                            displayName = "???";
                        }
                        mMainMenuFragment.setGreeting("Hello, " + displayName);
                    }
                });


        // if we have accomplishments to push, push them
        if (!mOutbox.isEmpty()) {
            pushAccomplishments();
            Toast.makeText(this, getString(R.string.your_progress_will_be_uploaded),
                    Toast.LENGTH_LONG).show();
        }

        loadAndPrintEvents();*/
    }

    private void onDisconnected() {
        Log.d(TAG, "onDisconnected()");

        mAchievementsClient = null;
        mLeaderboardsClient = null;
        /*mPlayersClient = null;

        // Show sign-in button on main menu
        mMainMenuFragment.setShowSignInButton(true);

        // Show sign-in button on win screen
        mWinFragment.setShowSignInButton(true);

        mMainMenuFragment.setGreeting(getString(R.string.signed_out_greeting));*/
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume()");

        // Since the state of the signed in user can change when the activity is not active
        // it is recommended to try and sign in silently from when the app resumes.
        //signInSilently();
    }
}