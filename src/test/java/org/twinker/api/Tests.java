package org.twinker.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.javafaker.Faker;
import io.qameta.allure.Link;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.twinker.api.model.Company;
import org.twinker.api.model.Employee;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;

@DisplayName("API: Company and Employee management")
@Link("https://x-clients-be.onrender.com/docs/#/")
public class Tests {
    public static final String URL = "https://x-clients-be.onrender.com";
    public static final String SWAGGER = "https://x-clients-be.onrender.com/docs/#/";
    public static final String LOGIN = "/auth/login";
    public static final String EMPLOYEE = "/employee";
    public static final String X_CLIENT_TOKEN = "x-client-token";
    public static final String COMPANY = "/company";
    public static final String DELETE = "/delete";
    public static String token;
    static String firstName;
    static String lastName;
    static String email;
    static String phone;
    static String companyName;
    static String companyDescription;
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
        companyName = faker.company().name();
        companyDescription = faker.company().industry();
    }

    @Test
    @DisplayName("Create a new company")
    public void createNewCompanyTest() {

        int companyId = createNewCompany()
                .then()
                .statusCode(201)
                .body("id", greaterThan(0))
                .extract().path("id");

        deleteCompanyById(companyId);
    }

    @Test
    @DisplayName("Delete an existing company")
    public void deleteCompanyTest() {
        int companyId = createNewCompany()
                .then()
                .extract().path("id");

        deleteCompanyById(companyId)
                .then().statusCode(200)
                .body("id", equalTo(companyId));
    }

    @Test
    @DisplayName("Add an employee to a company")
    public void addEmployeeToCompanyTest() {
        int companyId = createNewCompany().then().extract().path("id");

        addEmployeeToCompany(companyId).then()
                .statusCode(201)
                .body("id", Matchers.greaterThan(0));

        deleteCompanyById(companyId);
    }

    @Test
    @DisplayName("Employee is listed under the company")
    public void isEmployeeDisplayedInTheCompanyList() {
        int companyId = createNewCompany().then().extract().path("id");

        addEmployeeToCompany(companyId);
        List<Employee> allCompanyEmployees = getAllEmployeesByCompanyId(companyId);
        Assertions.assertTrue(allCompanyEmployees
                .stream()
                .anyMatch(x -> firstName.equals(x.getFirstName())));

        deleteCompanyById(companyId);
    }

    @Test
    @DisplayName("Deactivate an employee (set isActive to false)")
    public void changeTheEmployeeActivityToFalse() throws JsonProcessingException {
        int companyId = createNewCompany().then().extract().path("id");
        int employeeId = addEmployeeToCompany(companyId)
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

        deleteCompanyById(companyId);
    }

    @Test
    @DisplayName("Reactivate an employee (set isActive to true)")
    public void changeTheEmployeeActivityToTrue() throws JsonProcessingException {
        int companyId = createNewCompany().then().extract().path("id");
        int employeeId = addEmployeeToCompany(companyId)
                .then()
                .extract()
                .path("id");

        changeEmployeeInformation(Employee.builder().id(employeeId).isActive(false).build()).then().statusCode(200)
                .body("isActive", equalTo(false));

        changeEmployeeInformation(Employee.builder().id(employeeId).isActive(true).build())
                .then().statusCode(200)
                .body("isActive", equalTo(true));

        deleteCompanyById(companyId);
    }

    @Test
    @DisplayName("Update an employee's email")
    public void changeTheEmployeeEmail() {
        int companyId = createNewCompany().then().extract().path("id");
        int employeeId = addEmployeeToCompany(companyId)
                .then()
                .extract()
                .path("id");

        String newEmail = faker.internet().emailAddress();
        changeEmployeeInformation(Employee.builder().id(employeeId).email(newEmail).build());
        getEmployeeByIdResponse(employeeId).then().body("email", equalTo(newEmail));

        deleteCompanyById(companyId);
    }

    @Test
    @DisplayName("Cannot create employee for non-existent company")
    public void cantCreateNewEmployeeForNonExistentCompany() {
        addEmployeeToCompany(0).then().statusCode(500);
    }

    public Response changeEmployeeInformation(Employee employee) {
        Map<String, Object> map = new HashMap<>();
        if (employee.getFirstName() != null) map.put("firstName", employee.getId());
        if (employee.getLastName() != null) map.put("lastName", employee.getLastName());
        if (employee.getEmail() != null) map.put("email", employee.getEmail());
        if (employee.getPhone() != null) map.put("phone", employee.getPhone());
        if (employee.getIsActive() != null) map.put("isActive", employee.getIsActive());

//        it is not necessary to explicitly convert map->json. Jackson will convert it to JSON by itself.
//        ObjectMapper mapper = new ObjectMapper();
//        String json = mapper.writeValueAsString(map);

        return given()
                .header(X_CLIENT_TOKEN, token)
                .body(map).contentType(ContentType.JSON)
                .patch(EMPLOYEE + "/" + employee.getId());
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

    public Company getCompanyInfo(int companyId) {
        return given().get(COMPANY + "/" + companyId)
                .then().extract().as(Company.class);
    }

    public Response createNewCompany() {
        String json = String.format("""
                {
                  "name": "%s",
                  "description": "ave 1613 %s"
                }
                """, companyName, companyDescription);

        return given()
                .header(X_CLIENT_TOKEN, getToken())
                .body(json).contentType(ContentType.JSON)
                .post(COMPANY);
    }

    public Response deleteCompanyById(int companyId) {
        return given().header(X_CLIENT_TOKEN, getToken())
                .get(COMPANY + DELETE + "/" + companyId);
    }
}
