package byu.cs240.android.familymap.Fragments;

import android.os.Bundle;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import RequestResult.EAPIResult;
import RequestResult.LoginRequest;
import RequestResult.LoginResult;
import RequestResult.PAPIResult;
import RequestResult.RegisterRequest;
import RequestResult.RegisterResult;
import byu.cs240.android.familymap.R;
import byu.cs240.android.familymap.ServerProxy;

public class LoginFragment extends Fragment
{
    private LoginRequest loginRequest;
    private RegisterRequest registerRequest;
    private EditText serverHostField;
    private EditText serverPortField;
    private EditText usernameField;
    private EditText firstNameField;
    private EditText passwordField;
    private EditText lastNameField;
    private EditText emailField;
    private RadioButton maleButton;
    private RadioButton femaleButton;
    private Button signInButton;
    private Button registerButton;
    private Listener listener;
    private static final String BOOL_KEY_1 = "boolean.for.key.unique.a.is.this";
    private static final String BOOL_KEY_2 = "boolean.for.key.unique.another.is.this";
    private static final String STRING_KEY_1 = "string.for.key.unique.a.is.this";
    private static final String TAG = "LoginFragment";

    public interface Listener { void notifySwitch(); }

    public void registerListener(Listener listener) { this.listener = listener; }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_login, container,
                false);
        serverHostField = view.findViewById(R.id.server_host);
        serverPortField = view.findViewById(R.id.server_port);
        usernameField = view.findViewById(R.id.username);
        passwordField = view.findViewById(R.id.password);
        firstNameField = view.findViewById(R.id.first_name);
        lastNameField = view.findViewById(R.id.last_name);
        emailField = view.findViewById(R.id.email);

        //code for male radio button configuration
        maleButton = view.findViewById(R.id.male);
        maleButton.setOnClickListener(v ->
        {
            if(canRegister())
            {
                ServerProxy.setServerHost(serverHostField.getText().toString());
                ServerProxy.setServerPort(serverPortField.getText().toString());

                loginRequest = new LoginRequest(
                        usernameField.getText().toString(),
                        passwordField.getText().toString());

                registerRequest = new RegisterRequest(
                        usernameField.getText().toString(),
                        passwordField.getText().toString(),
                        emailField.getText().toString(),
                        firstNameField.getText().toString(),
                        lastNameField.getText().toString(),"m");

                registerButton.setEnabled(true);
            }
            else { registerButton.setEnabled(false); }
        });

        //code for female radio button configuration
        femaleButton = view.findViewById(R.id.female);
        femaleButton.setOnClickListener(v ->
        {
            if(canRegister())
            {
                ServerProxy.setServerHost(serverHostField.getText().toString());
                ServerProxy.setServerPort(serverPortField.getText().toString());

                loginRequest = new LoginRequest(
                        usernameField.getText().toString(),
                        passwordField.getText().toString());

                registerRequest = new RegisterRequest(
                        usernameField.getText().toString(),
                        passwordField.getText().toString(),
                        emailField.getText().toString(),
                        firstNameField.getText().toString(),
                        lastNameField.getText().toString(),"f");

                registerButton.setEnabled(true);
            }
            else { registerButton.setEnabled(false); }
        });

        //code for sign in button configuration
        signInButton = view.findViewById(R.id.sign_in);
        signInButton.setEnabled(false);
        signInButton.setOnClickListener(v ->
        {
            Handler uiThreadHandler = new Handler(Looper.getMainLooper())
            {
                @Override
                public void handleMessage(Message message)
                {
                    Bundle bundle = message.getData();
                    boolean success = bundle.getBoolean(BOOL_KEY_1, false);
                    if (success)
                    {
                        Handler uiThreadHandler2 = new Handler(Looper.getMainLooper())
                        {
                            @Override
                            public void handleMessage(Message message)
                            {
                                Bundle bundle = message.getData();
                                String loginMessage = bundle.getString(STRING_KEY_1, "FAIL");
                                boolean gotPeople = bundle.getBoolean(BOOL_KEY_1, false);
                                boolean gotEvents = bundle.getBoolean(BOOL_KEY_2, false);
                                if (gotPeople && gotEvents)
                                {
                                    Toast.makeText(
                                            getActivity(),
                                            loginMessage,
                                            Toast.LENGTH_SHORT)
                                            .show();
                                    if (listener != null)
                                    {
                                        listener.notifySwitch();
                                    }
                                }
                                else
                                {
                                    Toast.makeText(
                                            getActivity(),
                                            R.string.login_failed_toast,
                                            Toast.LENGTH_SHORT)
                                            .show();
                                }
                            }
                        };

                        GetDataTask task = new GetDataTask(uiThreadHandler2);
                        ExecutorService es = Executors.newSingleThreadExecutor();
                        es.submit(task);
                    }
                    else
                    {
                        Toast.makeText(
                                getActivity(),
                                R.string.login_failed_toast,
                                Toast.LENGTH_SHORT)
                                .show();
                    }
                }
            };

            LoginTask task = new LoginTask(uiThreadHandler, loginRequest);
            ExecutorService es = Executors.newSingleThreadExecutor();
            es.submit(task);
        });

        //code for register button configuration
        registerButton = view.findViewById(R.id.register);
        registerButton.setEnabled(false);
        registerButton.setOnClickListener(v ->
        {
            Handler uiThreadHandler = new Handler(Looper.getMainLooper())
            {
                @Override
                public void handleMessage(Message message)
                {
                    Log.d(TAG, "Handling Message from Register Thread");
                    Bundle bundle = message.getData();
                    boolean success = bundle.getBoolean(BOOL_KEY_1, false);
                    if (success)
                    {
                        Handler uiThreadHandler2 = new Handler(Looper.getMainLooper())
                        {
                            @Override
                            public void handleMessage(Message message)
                            {
                                Log.d(TAG, "Handling Message from GetData Thread");
                                Bundle bundle = message.getData();
                                String registerMessage = bundle.getString(STRING_KEY_1, "FAIL");
                                boolean gotPeople = bundle.getBoolean(BOOL_KEY_1, false);
                                boolean gotEvents = bundle.getBoolean(BOOL_KEY_2, false);
                                if (gotPeople && gotEvents)
                                {
                                    Log.d(TAG, "Ready to show Success Toast");
                                    Toast.makeText(
                                            getActivity(),
                                            registerMessage,
                                            Toast.LENGTH_SHORT)
                                            .show();
                                    if (listener != null)
                                    {
                                        listener.notifySwitch();
                                    }
                                }
                                else
                                {
                                    Log.d(TAG, "Ready to show Fail Toast from GetDataThread");
                                    Toast.makeText(
                                            getActivity(),
                                            R.string.register_failed_toast,
                                            Toast.LENGTH_SHORT)
                                            .show();
                                }
                            }
                        };

                        GetDataTask task = new GetDataTask(uiThreadHandler2);
                        ExecutorService es = Executors.newSingleThreadExecutor();
                        Log.d(TAG, "About to start GetData Task");
                        es.submit(task);
                        Log.d(TAG, "Started GetData Task");
                    }
                    else
                    {
                        Log.d(TAG, "Ready to show Fail Toast from Register Thread");
                        Toast.makeText(
                                        getActivity(),
                                        R.string.register_failed_toast,
                                        Toast.LENGTH_SHORT)
                                .show();
                    }
                }
            };

            RegisterTask task = new RegisterTask(uiThreadHandler, registerRequest);
            ExecutorService es = Executors.newSingleThreadExecutor();
            Log.d(TAG, "About to start Register Task");
            es.submit(task);
            Log.d(TAG, "Started Register Task");
        });

        return view;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        TextWatcher signInWatcher = new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if (canSignIn())
                {
                    ServerProxy.setServerHost(serverHostField.getText().toString());
                    ServerProxy.setServerPort(serverPortField.getText().toString());
                    loginRequest = new LoginRequest(
                            usernameField.getText().toString(),
                            passwordField.getText().toString());
                    signInButton.setEnabled(true);
                    if (canRegister())
                    {
                        registerRequest = new RegisterRequest(
                                usernameField.getText().toString(),
                                passwordField.getText().toString(),
                                emailField.getText().toString(),
                                firstNameField.getText().toString(),
                                lastNameField.getText().toString(),"f");
                        if (maleButton.isChecked()) { registerRequest.setGender("m"); }
                        registerButton.setEnabled(true);
                    }
                    else { registerButton.setEnabled(false); }
                }
                else
                {
                    signInButton.setEnabled(false);
                    registerButton.setEnabled(false);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        };
        serverHostField.addTextChangedListener(signInWatcher);
        serverPortField.addTextChangedListener(signInWatcher);
        usernameField.addTextChangedListener(signInWatcher);
        passwordField.addTextChangedListener(signInWatcher);

        TextWatcher registerWatcher = new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if (canRegister())
                {
                    ServerProxy.setServerHost(serverHostField.getText().toString());
                    ServerProxy.setServerPort(serverPortField.getText().toString());
                    registerRequest = new RegisterRequest(
                            usernameField.getText().toString(),
                            passwordField.getText().toString(),
                            emailField.getText().toString(),
                            firstNameField.getText().toString(),
                            lastNameField.getText().toString(),"f");
                    if (maleButton.isChecked()) { registerRequest.setGender("m"); }
                    registerButton.setEnabled(true);
                }
                else { registerButton.setEnabled(false); }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        };
        firstNameField.addTextChangedListener(registerWatcher);
        lastNameField.addTextChangedListener(registerWatcher);
        emailField.addTextChangedListener(registerWatcher);
    }

    public boolean canSignIn()
    {
        return ((!serverHostField.getText().toString().isEmpty())
                && (!serverPortField.getText().toString().isEmpty())
                && (!usernameField.getText().toString().isEmpty())
                && (!passwordField.getText().toString().isEmpty()));
    }

    public boolean canRegister()
    {
        return (canSignIn()
                && (!firstNameField.getText().toString().isEmpty())
                && (!lastNameField.getText().toString().isEmpty())
                && (!emailField.getText().toString().isEmpty())
                && (maleButton.isChecked() || femaleButton.isChecked()));
    }

    private static class LoginTask implements Runnable
    {
        private final Handler messageHandler;
        private final LoginRequest request;

        public LoginTask(Handler messageHandler, LoginRequest request)
        {
            this.messageHandler = messageHandler;
            this.request = request;
        }

        @Override
        public void run()
        {
            ServerProxy client = new ServerProxy();
            LoginResult result = client.login(request);
            sendMessage(result.isSuccess());
        }

        private void sendMessage(boolean success)
        {
            Message message = Message.obtain();
            Bundle messageBundle = new Bundle();
            messageBundle.putBoolean(BOOL_KEY_1, success);
            message.setData(messageBundle);
            messageHandler.sendMessage(message);
        }
    }

    private static class RegisterTask implements Runnable
    {
        private final Handler messageHandler;
        private final RegisterRequest request;

        public RegisterTask(Handler messageHandler, RegisterRequest request)
        {
            this.messageHandler = messageHandler;
            this.request = request;
        }

        @Override
        public void run()
        {
            Log.d(TAG, "Running Register Task");
            ServerProxy client = new ServerProxy();
            RegisterResult result = client.register(request);
            Log.d(TAG, "Registered");
            sendMessage(result.isSuccess());
        }

        private void sendMessage(boolean success)
        {
            Log.d(TAG, "Sending Message");
            Message message = Message.obtain();
            Bundle messageBundle = new Bundle();
            messageBundle.putBoolean(BOOL_KEY_1, success);
            message.setData(messageBundle);
            messageHandler.sendMessage(message);
        }
    }

    private static class GetDataTask implements Runnable
    {
        private final Handler messageHandler;

        public GetDataTask(Handler messageHandler)
        {
            this.messageHandler = messageHandler;
        }
        @Override
        public void run()
        {
            ServerProxy client = new ServerProxy();
            PAPIResult result = client.getPeople();
            EAPIResult result2 = client.getEvents();
            sendMessage(result.getMessage(), result.isSuccess(), result2.isSuccess());
        }

        private void sendMessage(String fullName, boolean gotPeople, boolean gotEvents)
        {
            Message message = Message.obtain();
            Bundle messageBundle = new Bundle();
            messageBundle.putString(STRING_KEY_1, fullName);
            messageBundle.putBoolean(BOOL_KEY_1, gotPeople);
            messageBundle.putBoolean(BOOL_KEY_2, gotEvents);
            message.setData(messageBundle);
            messageHandler.sendMessage(message);
        }
    }
}
