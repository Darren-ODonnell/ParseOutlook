package Models;

import lombok.Builder;
import lombok.Data;

@Data @Builder
public class Infoview {

    // key fullname -> to be able to locate

    private String classRollNo; //0
    private String studentNo; //3
    private String address; //4
    private String email; //5
    private String surnameFirstname; //8
    private String surname; //9
    private String firstname; //10
    private String fullnameMin; //11
    private String fullnameMax; //12

}