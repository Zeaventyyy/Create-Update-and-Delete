package com.example.zeave.crud;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CrudOperationActivity extends AppCompatActivity {

    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;

    EditText etEmployeeId, etName, etUsername, etPassword, etAge;

    Button btnCreateUpdate, btnRefresh;

    ListView listView;

    List<Employee> employeeList;

    boolean isUpdating = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crud_operation);

        etEmployeeId = findViewById(R.id.etEmployeeId);
        etName = findViewById(R.id.etName);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etAge = findViewById(R.id.etAge);
        listView = findViewById(R.id.listViewData);
        btnCreateUpdate = findViewById(R.id.btnCreateUpdate);
        btnRefresh = findViewById(R.id.btnRefresh);


        employeeList = new ArrayList<>();

        btnCreateUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isUpdating) {
                    updateEmployee();
                } else {
                    createEmployee();
                }
            }
        });

        readEmployees();

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readEmployees();
            }
        });
    }

    private void createEmployee() {
        String name = etName.getText().toString().trim();
        String age = etAge.getText().toString().trim();
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            etName.setError("Please Enter Name");
            etName.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(age)) {
            etAge.setError("Please Enter Age");
            etAge.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(username)) {
            etUsername.setError("Please Enter Username");
            etUsername.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Please Enter Password");
            etPassword.requestFocus();
            return;
        }

        HashMap<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("age", age);
        params.put("username", username);
        params.put("password", password);

        //Calling the create employee API
        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_CREATE_EMPLOYEE, params, CODE_POST_REQUEST);
        request.execute();
    }

    private void readEmployees() {
        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_READ_EMPLOYEE, null, CODE_GET_REQUEST);
        request.execute();
    }

    private void updateEmployee() {
        String id = etEmployeeId.getText().toString();
        String name = etName.getText().toString().trim();
        String username = etUsername.getText().toString().trim();
        String age = etAge.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(id)) {
            etName.setError("Please ID");
            etName.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(name)) {
            etName.setError("Please enter name");
            etName.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(age)) {
            etAge.setError("Please Enter Age");
            etAge.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(username)) {
            etUsername.setError("Please Enter Username");
            etUsername.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Please Enter Password");
            etPassword.requestFocus();
            return;
        }

        HashMap<String, String> params = new HashMap<>();
        params.put("id", id);
        params.put("name", name);
        params.put("age", age);
        params.put("username", username);
        params.put("password", password);


        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_UPDATE_EMPLOYEE, params, CODE_POST_REQUEST);
        request.execute();

        btnCreateUpdate.setText("Add");

        etName.setText("");
        etUsername.setText("");
        etAge.setText("");
        etPassword.setText("");

        isUpdating = false;
    }

    private void refreshEmployeeList(JSONArray employees) throws JSONException {
        //clearing previous employees
        employeeList.clear();

        //the json we got from the response
        for (int i = 0; i < employees.length(); i++) {
            //getting each employee object
            JSONObject obj = employees.getJSONObject(i);

            //adding the employee to the list
            employeeList.add(new Employee(
                    obj.getInt("id"),
                    obj.getString("name"),
                    obj.getInt("age"),
                    obj.getString("username"),
                    obj.getString("password")
            ));
        }

        //creating the adapter and setting it to the listview
        EmployeeAdapter adapter = new EmployeeAdapter(employeeList);
        listView.setAdapter(adapter);
    }

    private void deleteEmployee(int id) {
        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_DELETE_EMPLOYEE + id,null, CODE_GET_REQUEST);
        request.execute();
        //readEmployees();
    }

    public void clear() {
        etUsername.setText("");
        etName.setText("");
        etAge.setText("");
        etPassword.setText("");
    }

    private class PerformNetworkRequest extends AsyncTask<Void, Void, String> {
        //the url where we need to send the request
        String url;

        HashMap<String, String> params;

        //the request code to define whether it is a GET or POST
        int requestCode;

        PerformNetworkRequest(String url, HashMap<String, String> params, int requestCode) {
            this.url = url;
            this.params = params;
            this.requestCode = requestCode;
        }

        //this method will give the response from the request
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                JSONObject object = new JSONObject(s);

                if (!object.getBoolean("error")) {
                    Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                    refreshEmployeeList(object.getJSONArray("employees"));
                    clear();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //the network operation will be performed in background
        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();

            if (requestCode == CODE_POST_REQUEST)
                return requestHandler.sendPostRequest(url, params);

            if (requestCode == CODE_GET_REQUEST)
                return requestHandler.sendGetRequest(url);

            return null;
        }
    }

    class EmployeeAdapter extends ArrayAdapter<Employee> {
        List<Employee> employeeList;

        public EmployeeAdapter(List<Employee> employeeList) {
            super(CrudOperationActivity.this, R.layout.employee, employeeList);
            this.employeeList = employeeList;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View listViewItem = inflater.inflate(R.layout.employee, null, true);

            //getting the textview for displaying name
            TextView textViewName = listViewItem.findViewById(R.id.textViewName);

            //the update and delete textview
            TextView textViewUpdate = listViewItem.findViewById(R.id.textViewUpdate);
            TextView textViewDelete = listViewItem.findViewById(R.id.textViewDelete);
            RelativeLayout relativeLayout = listViewItem.findViewById(R.id.relativeRow);
            if ( position % 2 == 0){
                relativeLayout.setBackgroundResource(R.color.alternative);
            }

            final Employee employee = employeeList.get(position);

            textViewName.setText(employee.getName());

            //attaching click listener to update
            textViewUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //so when it is updating we will
                    //make the isUpdating as true
                    isUpdating = true;

                    //we will set the selected employees to the UI elements
                    etEmployeeId.setText(String.valueOf(employee.getId()));
                    etName.setText(employee.getName());
                    etAge.setText(employee.getAge() + "");
                    etUsername.setText(employee.getUsername());
                    etPassword.setText(employee.getPassword());

                    //we will also make the button text to Update
                    btnCreateUpdate.setText("Update");
                }
            });

            //when the user selected delete
            textViewDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // we will display a confirmation dialog before deleting
                    AlertDialog.Builder builder = new AlertDialog.Builder(CrudOperationActivity.this);

                    builder.setTitle("Delete " + employee.getName())
                            .setMessage("Are you sure you want to delete it?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    //if the choice is yes we will delete the employee
                                    deleteEmployee(employee.getId());
                                    readEmployees();

                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();


                }
            });

            return listViewItem;
        }
    }
}