package tn.esprit.familylinks.Utils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import tn.esprit.familylinks.LoginActivity;
import tn.esprit.familylinks.R;

/**
 * Created by L on 29-12-15.
 */
public class SplashScreen extends Activity {
    List<ParseObject> allUser;
    public List<user> users;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        users=new ArrayList<>();
        setContentView(R.layout.splash_screen);

        Thread timerThread = new Thread(){
            public void run(){
                try{
                    ParseQuery<ParseObject> queryAll = ParseQuery.getQuery("Person");
                    allUser= queryAll.find();
                    for (ParseObject j : allUser) {
                        if(j.getObjectId().equals("RyQlY5U6UM"))sharedInformation.sharedParseUnknown=j;
                        else{
                            ParseFile file = j.getParseFile("image");
                            String username = j.getString("Username");
                            String name = j.getString("FirstName");
                            String mail = j.getString("Email");
                            String lastName = j.getString("LastName");
                            String job = j.getString("Job");
                            String age = j.getString("DOB");
                            String gender = j.getString("Gender");
                            String personId = j.getObjectId();
                            Bitmap bitmap = null;
                            try {
                                InputStream is = new FileInputStream(file.getFile());
                                bitmap = BitmapFactory.decodeStream(is);
                            } catch (FileNotFoundException ex) {
                                ex.printStackTrace();
                            } catch (ParseException ex) {
                                ex.printStackTrace();
                            }
                            user u = new user(personId, username, mail, name, lastName, gender, age, job, bitmap,
                                    j.getParseObject("FatherP").getObjectId(),
                                    j.getParseObject("MotherP").getObjectId(),
                                    j.getParseObject("SpouseP").getObjectId());
                            users.add(u);
                            //Log.d("getUsername in ", U.getString("LastName"));
                        }
                        sharedInformation.sharedUsers=users;
                    }
                    Log.e("splash list size",Integer.toString(sharedInformation.sharedUsers.size()));
                } catch (ParseException e) {
                    Log.e("Error", e.getMessage());
                    e.printStackTrace();
                }finally{

                    Intent intent = new Intent(SplashScreen.this,LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                    //System.exit(0);
            }
            }
        };
        timerThread.start();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        finish();
    }

}