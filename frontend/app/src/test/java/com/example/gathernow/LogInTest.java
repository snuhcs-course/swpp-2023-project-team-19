import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.mockito.Mockito.when;

import com.example.gathernow.CodeMessageResponse;
import com.example.gathernow.LogIn;
import com.example.gathernow.RetrofitClient;
import com.example.gathernow.ServiceApi;
import com.example.gathernow.UserData;

@RunWith(MockitoJUnitRunner.class)
public class LogInTest {

    @Mock
    private ServiceApi service;

    @Mock
    private Call<CodeMessageResponse> call;

    @Mock
    private LogIn logInActivity;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testLoginSuccessful() {
        // Create a UserData object with valid email and password
        UserData userData = new UserData("user3@gmail.com", "123");

        // Create a successful response
        CodeMessageResponse successResponse = new CodeMessageResponse();
        successResponse.setMessage("Login successful.");

        // Call the loginButtonOnClick method
        logInActivity.loginButtonOnClick(null);
    }

    @Test
    public void testEmptyFields() {
        // Create a UserData object with empty email and password
        UserData userData = new UserData("", "");

        // Call the loginButtonOnClick method
        logInActivity.loginButtonOnClick(null);

        // Add assertions to check if the appropriate action is taken for empty fields
        // For example, you can use Mockito to verify that the "Please fill in all required fields!" alert is set.
    }

    // Write unittest for the invalidEmail
    @Test
    public void testInvalidEmail(){
        // Create a UserData object with invalid email and password
        UserData userData = new UserData("test", "password");

        // Call the loginButtonOnClick method
        logInActivity.loginButtonOnClick(null);
    }

    // Write unittest for the incorrectPassword
    @Test
    public void testIncorrectPassword(){
        // Create a UserData object with valid email and incorrect password
        UserData userData = new UserData("user3@gmail.com", "test");
    }
    
}