package tn.esprit.familylinks;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Calendar;

import tn.esprit.familylinks.Utils.sharedInformation;

public class RegisterActivity3 extends AppCompatActivity {
    private Uri imageCaptureUri;
    private ImageView mImageView;
    private static final int PICK_FROM_CAMERA = 1;
    private static final int PICK_FROM_FILE = 2;
    EditText country;
    EditText job;
    EditText lname;
    EditText fname;
    EditText username;
    EditText password;
    TextView dob;
    EditText email;
    Button signup;
    String gendertxt;
    String dobtxt;
    String jobtxt;
    String lnametxt;
    String fnametxt;
    Toolbar toolbar;
    ActionBar actionBar;
    ParseFile imgFile;

    private Calendar calendar;
    private TextView dateView;
    private int year, month, day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register3);
        final String[] items = new String[]{ "From SD Card"};
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, items);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Select Image");
        builder.setAdapter(adapter2, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
             /*   if (item == 0) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File file = new File(Environment.getExternalStorageDirectory(),
                            "tmp_avatar_" + String.valueOf(System.currentTimeMillis()) + ".jpg");
                    imageCaptureUri = Uri.fromFile(file);

                    try {
                        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageCaptureUri);
                        intent.putExtra("return-data", true);

                        startActivityForResult(intent, PICK_FROM_CAMERA);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    dialog.cancel();
                } else {*/
                Intent intent = new Intent();

                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(intent, "Complete action using"), PICK_FROM_FILE);
                //   }

            }
        });

        final AlertDialog dialog = builder.create();

        mImageView = (ImageView) findViewById(R.id.img_showm);

        ((Button) findViewById(R.id.btn_choose_imagem)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });
        /*toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/
        //date picker
        dateView = (TextView) findViewById(R.id.dobm);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month + 1, day);


        // Locate EditTexts in main.xml
        fname = (EditText) findViewById(R.id.FirstNameMother);
        lname = (EditText) findViewById(R.id.LastNameMother);
        job = (EditText) findViewById(R.id.JobMother);
        signup = (Button) findViewById(R.id.addMother);

        /*toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();

        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        actionBar.setDisplayHomeAsUpEnabled(true);*/

        signup.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                // Retrieve the text entered from the EditText
                lnametxt = lname.getText().toString();
                fnametxt = fname.getText().toString();
                dobtxt = dateView.getText().toString();
                jobtxt = job.getText().toString();
                // Force user to fill up the form
                if (lnametxt.equals("") && fnametxt.equals("")) {
                    Toast.makeText(getApplicationContext(),
                            "Please complete her name",
                            Toast.LENGTH_LONG).show();

                } else if (jobtxt.equals("")) {
                    Toast.makeText(getApplicationContext(),
                            "Please complete her job",
                            Toast.LENGTH_LONG).show();
                } else {
                    if (imgFile != null) {
                    // Save new user data into Parse.com Data Storage
                    final ParseObject mother = new ParseObject("Person");
                    mother.put("Gender", "Female");
                    mother.put("DOB", dobtxt);
                    mother.put("LastName", lnametxt);
                    mother.put("FirstName", fnametxt);
                    mother.put("Job", job.getText().toString());
                    mother.put("image", imgFile);
                    mother.put("Email", "unk@unk.com");
                    //Log.w("father id register3", sharedInformation.sharedParseFatherId);
                    mother.put("SpouseP", ParseObject.createWithoutData("Person", sharedInformation.sharedParseFatherId));
                    mother.put("FatherP", ParseObject.createWithoutData("Person", "RyQlY5U6UM"));
                    mother.put("MotherP", ParseObject.createWithoutData("Person", "RyQlY5U6UM"));
                    mother.saveInBackground();
                    //Person.saveInBackground();

                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Person");
                    query.getInBackground(sharedInformation.sharedParseFatherId, new GetCallback<ParseObject>() {
                        public void done(ParseObject father, ParseException e) {
                            if (e == null) {
                                // Now let's update it with some new data. In this case, only cheatMode and score
                                // will get sent to the Parse Cloud. playerName hasn't changed.
                                father.put("SpouseP", mother);
                                father.saveInBackground();
                            }
                        }
                    });
                    ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Person");
                    query2.getInBackground(sharedInformation.sharedParsePersonId, new GetCallback<ParseObject>() {
                        public void done(ParseObject pers, ParseException e) {
                            if (e == null) {
                                // Now let's update it with some new data. In this case, only cheatMode and score
                                // will get sent to the Parse Cloud. playerName hasn't changed.

                                pers.put("FatherP", mother);
                                pers.put("MotherP", ParseObject.createWithoutData("Person", sharedInformation.sharedParseFatherId));
                                pers.saveInBackground();
                            }
                        }
                    });

                    // Show a simple Toast message upon successful registration
                        Toast.makeText(getApplicationContext(),
                                "Successfully Signed up, please log in.",
                        Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(RegisterActivity3.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                   /* ParseQuery<ParseObject> queryTest = ParseQuery.getQuery("Person");
                    queryTest.whereEqualTo("FirstName", fnametxt);
                    queryTest.whereEqualTo("LastName", lnametxt);
                    queryTest.include("Spouse");
                    queryTest.findInBackground(new FindCallback<ParseObject>() {
                        public void done(List<ParseObject> similarMothers, ParseException e) {
                            if (e == null) {
                                if (similarMothers.size() == 0) {
                                    Intent intent = new Intent(RegisterActivity3.this, LoginActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    ParseObject sharedfather = sharedInformation.sharedParseFather;

                                    for (ParseObject j : similarMothers) {
                                        ParseObject father = j.getParseObject("Spouse");
                                        if (father.getString("FirstName").equals(sharedfather.getString("FirstName"))) {
                                            Log.e("same", "father");
                                        } else {
                                            Log.e("No", "No");
                                        }
                                    }
                                }
                            } else {
                                Log.d("score", "Error: " + e.getMessage());
                            }
                        }
                    });*/
                }else Toast.makeText(RegisterActivity3.this, "Please add an image", Toast.LENGTH_SHORT).show();}
            }
        });
    }

    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this, myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
            // arg1 = year
            // arg2 = month
            // arg3 = day
            showDate(arg1, arg2 + 1, arg3);
        }
    };

    private void showDate(int year, int month, int day) {
        dateView.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) return;

        Bitmap bitmap = null;
        String path = "";

        if (requestCode == PICK_FROM_FILE) {
            imageCaptureUri = data.getData();
            path = getRealPathFromURI(imageCaptureUri); //from Gallery

            if (path == null)
                path = imageCaptureUri.getPath(); //from File Manager

            if (path != null)
                bitmap = BitmapFactory.decodeFile(path);
        } else {
            path = imageCaptureUri.getPath();
            bitmap = BitmapFactory.decodeFile(path);
        }

        mImageView.setImageBitmap(bitmap);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        //  bitmap.compress(Bitmap.CompressFormat.JPEG,20 , stream);
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(
                bitmap, 300, 300, false);
        resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
        // get byte array here
        byte[] bytearray = stream.toByteArray();
        imgFile = new ParseFile("MotherAndroid.jpg", bytearray);

    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);

        if (cursor == null) return null;

        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        cursor.moveToFirst();

        return cursor.getString(column_index);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(RegisterActivity3.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}

