package org.twinker.tests.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.twinker.api.model.Employee;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class Tests {
    public static final String URL = "https://x-clients-be.onrender.com";
    public static final String LOGIN = "/auth/login";
    public static final String EMPLOYEE = "/employee";
    public static final String X_CLIENT_TOKEN = "x-client-token";
    public static final String COMPANY = "/company";
    public static String token;
    static String firstName;
    static String lastName;
    static String email;
    static String phone;
    static Faker faker;

    @BeforeAll
    public static void setUp() {
        RestAssured.baseURI = URL;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        token = getToken();
        faker = new Faker(new Locale("pl"));
        firstName = faker.name().firstName();
        lastName = faker.name().lastName();
        email = faker.internet().emailAddress();
        phone = faker.phoneNumber().phoneNumber();
//        Map<Integer, String> twinkerCompanies = getAllCompaniesIdsAndNames()
//                .entrySet()
//                .stream()
//                .filter(entry -> entry.getValue().contains("Twinker"))
//                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Test
    public void addEmployeeToCompany() {
        addEmployeeToCompany(910).then()
                .log().body()
                .statusCode(201)
                .body("id", Matchers.greaterThan(1))
                .extract().path("id");
    }

    @Test
    public void isEmployeeDisplayedInTheCompanyList() {
        addEmployeeToCompany(910);
        List<Employee> allCompanyEmployees = getAllEmployeesByCompanyId(910);
        Assertions.assertTrue(allCompanyEmployees
                .stream()
                .anyMatch(x -> firstName.equals(x.firstName())));
    }

    @Test
    public void changeTheEmployeeActivityToFalse() throws JsonProcessingException {
        int employeeId = addEmployeeToCompany(910)
                .then()
                .extract()
                .path("id");

        getEmployeeByIdResponse(employeeId)
                .then()
                .statusCode(200)
                .body("isActive", equalTo(true));

        changeEmployeeInformation(Employee.builder().id(employeeId).isActive(false).build())
                .then().statusCode(200)
                .body("isActive", equalTo(false));
    }

    @Test
    public void changeTheEmployeeActivityToTrue() throws JsonProcessingException {
        int employeeId = addEmployeeToCompany(910)
                .then()
                .extract()
                .path("id");

        changeEmployeeInformation(Employee.builder().id(employeeId).isActive(false).build()).then().statusCode(200)
                .body("isActive", equalTo(false));

        changeEmployeeInformation(Employee.builder().id(employeeId).isActive(true).build())
                .then().statusCode(200)
                .body("isActive", equalTo(true));

    }

    @Test
    public void changeTheEmployeeEmail() throws JsonProcessingException {
        int employeeId = addEmployeeToCompany(910)
                .then()
                .extract()
                .path("id");

        String newEmail = faker.internet().emailAddress();
        changeEmployeeInformation(Employee.builder().id(employeeId).email(newEmail).build());
        getEmployeeByIdResponse(employeeId).then().body("email", equalTo(newEmail));
    }

    @Test
    public void cantCreateNewEmployeeForNonExistentCompany() {
        addEmployeeToCompany(0).then().statusCode(200);
    }

    public Response changeEmployeeInformation(Employee employee) throws JsonProcessingException {
        Map<String, Object> map = new HashMap<>();
        if (employee.firstName() != null) map.put("firstName", employee.id());
        if (employee.lastName() != null) map.put("lastName", employee.lastName());
        if (employee.email() != null) map.put("email", employee.email());
        if (employee.phone() != null) map.put("phone", employee.phone());
        if (employee.isActive() != null) map.put("isActive", employee.isActive());

//        it is not necessary to explicitly convert map->json. Jackson will convert it to JSON by itself.
//        ObjectMapper mapper = new ObjectMapper();
//        String json = mapper.writeValueAsString(map);

        return given()
                .header(X_CLIENT_TOKEN, token)
                .body(map).contentType(ContentType.JSON)
                .patch(EMPLOYEE + "/" + employee.id());
    }

    //TODO return Response
    public static Response addEmployeeToCompany(int companyId) {
        String json = String.format("""
                {
                  "firstName": "%s",
                  "lastName": "%s",
                  "middleName": "Party",
                  "companyId": %s,
                  "email": "%s",
                  "url": "string",
                  "phone": "%s",
                  "birthdate": "2025-04-17T09:16:53.514Z",
                  "isActive": true
                }
                """, firstName, lastName, companyId, email, phone);

        return given().body(json).contentType(ContentType.JSON).header(X_CLIENT_TOKEN, token).post(EMPLOYEE);
    }

    //TODO return object of Employee.class
    public static Employee getEmployeeById(int id) {
        return given().get(EMPLOYEE + "/" + id).then().extract().as(Employee.class);
    }

    //TODO return Response
    public static Response getEmployeeByIdResponse(int id) {
        return given().get(EMPLOYEE + "/" + id);
    }

    //TODO return List<Employee>
    public static List<Employee> getAllEmployeesByCompanyId(int id) {
        return given().get(EMPLOYEE + "?company=" + id).then().extract().as(new TypeRef<>() {
        });
    }

    //TODO return String value
    public static String getToken() {
        String json = """
                {
                  "username": "leonardo",
                  "password": "leads"
                }
                """;

        return given().body(json).contentType(ContentType.JSON).post(LOGIN).path("userToken");
    }

    //TODO return HashMap
    public static Map<Integer, String> getAllCompaniesIdsAndNames() {

        //option 1
        Response response = given().get(COMPANY).then().extract().response();
        List<Integer> ids = response.path("id");
        List<String> names = response.path("name");

        Map<Integer, String> map = new HashMap<>();
        for (int i = 0; i < ids.size(); i++) {
            map.put(ids.get(i), names.get(i));
        }

        return map;
    }
}
