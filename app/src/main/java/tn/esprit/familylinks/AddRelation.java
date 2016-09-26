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
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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
import tn.esprit.familylinks.Utils.user;

/**
 * Created by L on 27-12-15.
 */
public class AddRelation extends AppCompatActivity {
    private Uri imageCaptureUri;
    private ImageView mImageView;
    private static final int PICK_FROM_CAMERA = 1;
    private static final int PICK_FROM_FILE = 2;
    ParseFile imgFile;
    Bitmap resizedBitmap;
    EditText lname ;
    EditText fname;
    EditText job;
    EditText password;
    Spinner gender;
    TextView dob;
    EditText email;
    Button signup;
    String usernametxt;
    String passwordtxt;
    String gendertxt;
    String dobtxt;
    String lnametxt;
    String fnametxt;
    String emailtxt;
    String jobtxt;
    Spinner spinner;
    Toolbar toolbar;
    ActionBar actionBar;
    String newObjetId;
    String typeRealtion;
    ParseObject updatingPerson;
    ParseObject selectedPerson;


    private DatePicker datePicker;
    private Calendar calendar;
    private TextView dateView;
    private int year, month, day;
    //ParseUser currentUser =ParseUser.getCurrentUser();
    ParseObject user1;
    //  ListView lstFamille;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_relation);

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
        } );

        final AlertDialog dialog = builder.create();

        mImageView = (ImageView) findViewById(R.id.img_show);

        ((Button) findViewById(R.id.btn_choose_image)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });



        //sharedInformation.sharedSelectedPerson=sharedInformation.sharedcurrentPerson;
        Log.w("updating person gender", sharedInformation.sharedcurrentPerson.gender);
        Log.d("updating personId",sharedInformation.sharedcurrentPerson.personId);
        //   Log.d("updatingd personId", sharedInformation.sharedcurrentPerson.personId);
        //   Log.w("updatingw personId", sharedInformation.sharedcurrentPerson.personId);

        //   updatingPerson=ParseObject.createWithoutData("Person", sharedInformation.sharedcurrentPerson.personId);
        selectedPerson=ParseObject.createWithoutData("Person", sharedInformation.sharedSelectedPerson.personId);

        Bundle extra=getIntent().getExtras();
        if(extra!=null)
            typeRealtion=extra.getString("relation");
        Log.e("rel",typeRealtion);

        //date picker
        dateView = (TextView) findViewById(R.id.dob);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month + 1, day);
//spinner gender
        spinner = (Spinner) findViewById(R.id.gender_spinner);

// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gender, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);

// Locate EditTexts in main.xml
        fname = (EditText) findViewById(R.id.FirstName);
        lname = (EditText) findViewById(R.id.LastName);
        job = (EditText) findViewById(R.id.Job);
        //email= (EditText) findViewById(R.id.Email);
        signup = (Button)findViewById(R.id.add_relation_button);

        if((!typeRealtion.equals("Spouse")) && (!typeRealtion.equals("Mother"))){ lname.setText(sharedInformation.sharedSelectedPerson.lastName); }

        /*actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_action_back);
        actionBar.setDisplayHomeAsUpEnabled(true);
*/
        signup.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                // Retrieve the text entered from the EditText
                usernametxt = lname.getText().toString()+fname.getText().toString();
                //emailtxt=email.getText().toString();
                lnametxt = lname.getText().toString();
                fnametxt = fname.getText().toString();
                jobtxt = job.getText().toString();
                gendertxt =spinner.getSelectedItem().toString();
                dobtxt =dateView.getText().toString();
                // Force user to fill up the form
                if (fname.equals("") ) {
                    Toast.makeText(getApplicationContext(),
                            "Please complete the sign up form",
                            Toast.LENGTH_LONG).show();
                } else {
                    if (imgFile != null) {
                        // Save new user data into Parse.com Data Storage
                        user1 = new ParseObject("Person");
                        user1.put("LastName", lnametxt);
                        user1.put("FirstName", fnametxt);
                        user1.put("Gender", gendertxt);
                        user1.put("DOB", dobtxt);
                        user1.put("Job", jobtxt);
                        user1.put("image", imgFile);
                        user1.put("FatherP", ParseObject.createWithoutData("Person", "RyQlY5U6UM"));
                        user1.put("MotherP", ParseObject.createWithoutData("Person", "RyQlY5U6UM"));
                        user1.put("SpouseP", ParseObject.createWithoutData("Person", "RyQlY5U6UM"));
                        user1.saveInBackground();

                        ParseQuery<ParseObject> query = ParseQuery.getQuery("Person");
                        query.include("MotherP");
                        // Retrieve the object by id
                        query.getInBackground(sharedInformation.sharedSelectedPerson.personId, new GetCallback<ParseObject>() {
                            public void done(ParseObject per, ParseException e) {

                                if (e == null) {
                                    Log.w("PersonName", user1.getString("LastName"));
                                    if (typeRealtion.equals("Children")) {
                                        Log.w("updating person gender", sharedInformation.sharedSelectedPerson.gender);
                                        if (sharedInformation.sharedSelectedPerson.gender.equals("Male")) {
                                            user1.put("FatherP", selectedPerson);
                                            //Log.w("aaaaaa", per.getParseObject("MotherP").getObjectId());
                                            //user.put("MotherP", per.getParseObject("MotherP"));*/
                                            user1.put("MotherP", ParseObject.createWithoutData("Person", sharedInformation.sharedSelectedPerson.spouseId));
                                        } else {
                                            user1.put("MotherP", selectedPerson);
                                            user1.put("FatherP", ParseObject.createWithoutData("Person", sharedInformation.sharedSelectedPerson.spouseId));
                                        }
                                        user1.saveInBackground();
                                    } else if (typeRealtion.equals("Spouse")) {
                                        per.put("SpouseP", user1);
                                        per.saveInBackground();
                                        // user1.put("SpouseP", selectedPerson);
                                        user1.saveInBackground();
                                        //Log.w("SpouseOK", user.getString("LastName"));
                                    } else if (typeRealtion.equals("Parent")) {
                                        if (gendertxt.equals("Male")) {
                                            per.put("FatherP", user1);
                                            per.put("MotherP", ParseObject.createWithoutData("Person", sharedInformation.sharedSelectedPerson.motherId));
                                        } else {
                                            per.put("MotherP", user1);
                                            per.put("FatherP", ParseObject.createWithoutData("Person", sharedInformation.sharedSelectedPerson.fatherId));
                                        }
                                        per.saveInBackground();
                                    } else if (typeRealtion.equals("Father")) {
                                        per.put("FatherP", user1);
                                        per.put("MotherP", ParseObject.createWithoutData("Person", sharedInformation.sharedSelectedPerson.motherId));
                                        per.saveInBackground();
                                    } else if (typeRealtion.equals("Mother")) {
                                        per.put("MotherP", user1);
                                        per.put("FatherP", ParseObject.createWithoutData("Person", sharedInformation.sharedSelectedPerson.fatherId));
                                        ;
                                        per.saveInBackground();
                                    }

                                    Log.w("idPerson", sharedInformation.sharedSelectedPerson.personId);
                                    user u = new user(user1.getObjectId(), fnametxt, lnametxt, gendertxt, dobtxt, jobtxt, resizedBitmap, user1.getParseObject("FatherP").getObjectId(), user1.getParseObject("MotherP").getObjectId(), user1.getParseObject("SpouseP").getObjectId());
                                    sharedInformation.sharedUsers.add(u);
                                    Intent intent = new Intent(getApplication(), MainActivity.class);
                                    intent.putExtra("searched", sharedInformation.sharedSelectedPerson.personId);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(getApplicationContext(),
                                            e.toString(),
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                    }
                    else Toast.makeText(AddRelation.this, "add image svp ", Toast.LENGTH_SHORT).show();
                }
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
            showDate(arg1, arg2+1, arg3);
        }
    };

    private void showDate(int year, int month, int day) {
        dateView.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) return;

        Bitmap bitmap 	= null;
        String path		= "";

        if (requestCode == PICK_FROM_FILE) {
            imageCaptureUri = data.getData();
            path = getRealPathFromURI(imageCaptureUri); //from Gallery

            if (path == null)
                path = imageCaptureUri.getPath(); //from File Manager

            if (path != null)
                bitmap 	= BitmapFactory.decodeFile(path);
        } else {
            path	= imageCaptureUri.getPath();
            bitmap  = BitmapFactory.decodeFile(path);
        }

        mImageView.setImageBitmap(bitmap);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        //  bitmap.compress(Bitmap.CompressFormat.JPEG,20 , stream);
        resizedBitmap = Bitmap.createScaledBitmap(
                bitmap, 300, 300, false);
        resizedBitmap.compress(Bitmap.CompressFormat.JPEG,50 , stream);
        // get byte array here
        byte[] byteArray = stream.toByteArray();
        imgFile = new ParseFile("braveImg.jpg", byteArray);

    }

    public String getRealPathFromURI(Uri contentUri) {
        String [] proj 		= {MediaStore.Images.Media.DATA};
        Cursor cursor 		= managedQuery( contentUri, proj, null, null,null);

        if (cursor == null) return null;

        int column_index 	= cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        cursor.moveToFirst();

        return cursor.getString(column_index);
    }


    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplication(), MainActivity.class);
        intent.putExtra("searched", sharedInformation.sharedSelectedPerson.personId);
        startActivity(intent);
    }

}