package resttask.dtos;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.regex.Pattern;

/*
    NOTICE: Solution with DTO is much more elegant to my taste, but it is generating some overhead due to generation of extra objects.
    Whole exercise could be implemented completely without DTO operations etc. - only with pure String operations - just by
    editing each single row to match json pattern.
 */

@Data
@Builder
public class Record {

    private static Pattern emailRegex = Pattern.compile("[a-zA-Z0-9]+\\.+[a-zA-Z0-9]+@domain.com");

    @SerializedName("FirstName")
    private String firstName;

    @SerializedName("LastName")
    private String lastName;

    @SerializedName("Email")
    private String email;

    private List<String> param1;
    private List<String> param2;
    private List<String> param3;
    private List<String> param4;
    private List<String> param5;
    private List<String> param6;
    private List<String> param7;
    private List<String> param8;
    private List<String> param0;

    public boolean isValidEmail() {
        if (email == null) return false;
        if (!emailRegex.matcher(email).matches()) return false;
        if (!email.contains(firstName) || !email.contains(lastName)) return false;
        return true;
    }
}