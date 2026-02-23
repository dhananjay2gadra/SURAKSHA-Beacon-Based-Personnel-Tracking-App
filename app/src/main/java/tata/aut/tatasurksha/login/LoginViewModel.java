package tata.aut.tatasurksha.login;

import android.os.AsyncTask;
import android.util.Patterns;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import tata.aut.tatasurksha.R;
import tata.aut.tatasurksha.login.data.LoginRepository;
import tata.aut.tatasurksha.login.data.Result;
import tata.aut.tatasurksha.login.data.model.LoggedInUser;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private LoginRepository loginRepository;

    LoginViewModel(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void login(String username, String password) {
        // can be launched in a separate asynchronous job
        new LoginTask().execute(username, password);

    }

    public class LoginTask extends AsyncTask<String, Void, Result<LoggedInUser>> {
       // RestTemplate restTemplate = new RestTemplate();
        @Override
        protected Result<LoggedInUser> doInBackground(String... strings) {
            String username= strings[0];
            String password=strings[1];
            //---network call here!
            Result<LoggedInUser> result = loginRepository.login(username, password);
            return result;
        }

        @Override
        protected void onPostExecute(Result<LoggedInUser> loggedInUserResult) {
            super.onPostExecute(loggedInUserResult);

            if (loggedInUserResult instanceof Result.Success) {
                LoggedInUser data = ((Result.Success<LoggedInUser>) loggedInUserResult).getData();
                loginResult.setValue(new LoginResult(new LoggedInUserView(data.getDisplayName())));
            } else if(loggedInUserResult instanceof  Result.Error){
                Exception err= ((Result.Error)loggedInUserResult).getError();
                loginResult.setValue(new LoginResult(R.string.login_failed, err.getMessage()));

            }else{
                loginResult.setValue(new LoginResult(R.string.login_failed));
            }
        }
    }

    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }
}
