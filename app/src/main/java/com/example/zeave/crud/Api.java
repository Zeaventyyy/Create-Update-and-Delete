package com.example.zeave.crud;

public class Api {
    private static final String ROOT_URL = "http://192.168.0.162/EmployeeApi/v1/API.php?apicall=";

    public static final String URL_CREATE_EMPLOYEE = ROOT_URL + "createemployee";
    public static final String URL_READ_EMPLOYEE = ROOT_URL + "getEmployees";
    public static final String URL_UPDATE_EMPLOYEE = ROOT_URL + "updateEmployee";
    public static final String URL_DELETE_EMPLOYEE = ROOT_URL + "deleteemployee&id=";
}
