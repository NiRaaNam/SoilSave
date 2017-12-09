package com.gistda.niraanam.soilsave;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;

public class ShowSingleRecordActivity extends AppCompatActivity {

    HttpParse httpParse = new HttpParse();
    ProgressDialog pDialog;

    // Http Url For Filter Student Data from Id Sent from previous activity.
    String HttpURL = "http://150.107.31.104/soil_collecting/FilterSoilData.php";

    // Http URL for delete Already Open Student Record.
    //String HttpUrlDeleteRecord = "https://androidjsonblog.000webhostapp.com/Student/DeleteStudent.php";

    String finalResult ;
    HashMap<String,String> hashMap = new HashMap<>();
    String ParseResult ;
    HashMap<String,String> ResultHash = new HashMap<>();
    String FinalJSonObject ;

    EditText Numpoint,LatVal,LonVal,Value1,Value2,Value3,Value4,Value5,Value6,Value7,Value8;

    String Value1Holder, Value2Holder, Value3Holder, Value4Holder, Value5Holder, Value6Holder, Value7Holder, Value8Holder, Value9Holder,LatValueHolder,LonValueHolder;

    String TempItem;
    ProgressDialog progressDialog2;
    Button theClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_single_record);

        Numpoint = (EditText)findViewById(R.id.edtNumpoint);
        LatVal = (EditText)findViewById(R.id.edtLatSing);
        LonVal = (EditText)findViewById(R.id.edtLonSing);
        Value1 = (EditText)findViewById(R.id.edtVal1);
        Value2 = (EditText)findViewById(R.id.edtVal2);
        Value3 = (EditText)findViewById(R.id.edtVal3);
        Value4 = (EditText)findViewById(R.id.edtVal4);
        Value5 = (EditText)findViewById(R.id.edtVal5);
        Value6 = (EditText)findViewById(R.id.edtVal6);
        Value7 = (EditText)findViewById(R.id.edtVal7);
        Value8 = (EditText)findViewById(R.id.edtVal8);

        theClose = (Button)findViewById(R.id.btnClose);


        //Receiving the ListView Clicked item value send by previous activity.
        TempItem = getIntent().getStringExtra("ListViewValue");

        //Calling method to filter Student Record and open selected record.
        HttpWebCall(TempItem);


        // Add Click listener on Delete button.
        theClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Calling Student delete method to delete current record using Student ID.
                finish();

            }
        });




    }



    //Method to show current record Current Selected Record
    public void HttpWebCall(final String PreviousListViewClickedItem){

        class HttpWebCallFunction extends AsyncTask<String,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                pDialog = ProgressDialog.show(ShowSingleRecordActivity.this,"Loading Data",null,true,true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                pDialog.dismiss();

                //Storing Complete JSon Object into String Variable.
                FinalJSonObject = httpResponseMsg ;

                //Parsing the Stored JSOn String to GetHttpResponse Method.
                new GetHttpResponse(ShowSingleRecordActivity.this).execute();

            }

            @Override
            protected String doInBackground(String... params) {

                ResultHash.put("theID",params[0]);

                ParseResult = httpParse.postRequest(ResultHash, HttpURL);

                return ParseResult;
            }
        }

        HttpWebCallFunction httpWebCallFunction = new HttpWebCallFunction();

        httpWebCallFunction.execute(PreviousListViewClickedItem);
    }


    // Parsing Complete JSON Object.
    private class GetHttpResponse extends AsyncTask<Void, Void, Void>
    {
        public Context context;

        public GetHttpResponse(Context context)
        {
            this.context = context;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0)
        {
            try
            {
                if(FinalJSonObject != null)
                {
                    JSONArray jsonArray = null;

                    try {
                        jsonArray = new JSONArray(FinalJSonObject);

                        JSONObject jsonObject;

                        for(int i=0; i<jsonArray.length(); i++)
                        {
                            jsonObject = jsonArray.getJSONObject(i);

                            // Storing Student Name, Phone Number, Class into Variables.
                            Value1Holder = jsonObject.getString("num_mark").toString() ;
                            Value2Holder = jsonObject.getString("value1").toString() ;
                            Value3Holder = jsonObject.getString("value2").toString() ;
                            Value4Holder = jsonObject.getString("value3").toString() ;
                            Value5Holder = jsonObject.getString("value4").toString() ;
                            Value6Holder = jsonObject.getString("value5").toString() ;
                            Value7Holder = jsonObject.getString("value6").toString() ;
                            Value8Holder = jsonObject.getString("value7").toString() ;
                            Value9Holder = jsonObject.getString("value8").toString() ;
                            LatValueHolder = jsonObject.getString("lat").toString() ;
                            LonValueHolder = jsonObject.getString("lon").toString() ;

                        }
                    }
                    catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
            catch (Exception e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {

            // Setting Student Name, Phone Number, Class into TextView after done all process .
            Numpoint.setText(Value1Holder);
            Value1.setText(Value2Holder);
            Value2.setText(Value3Holder);
            Value3.setText(Value4Holder);
            Value4.setText(Value5Holder);
            Value5.setText(Value6Holder);
            Value6.setText(Value7Holder);
            Value7.setText(Value8Holder);
            Value8.setText(Value9Holder);
            LatVal.setText(LatValueHolder);
            LonVal.setText(LonValueHolder);


        }
    }

}
