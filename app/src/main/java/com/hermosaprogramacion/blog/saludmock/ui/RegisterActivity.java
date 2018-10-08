package com.hermosaprogramacion.blog.saludmock.ui;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.hermosaprogramacion.blog.saludmock.R;
import com.hermosaprogramacion.blog.saludmock.data.api.SaludMockApi;
import com.hermosaprogramacion.blog.saludmock.data.api.model.Affiliate;
import com.hermosaprogramacion.blog.saludmock.data.api.model.ApiError;
import com.hermosaprogramacion.blog.saludmock.data.api.model.LoginBody;
import com.hermosaprogramacion.blog.saludmock.data.api.model.RegisterBody;
import com.hermosaprogramacion.blog.saludmock.data.prefs.SessionPrefs;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Screen de login para afiliados.
 */
public class RegisterActivity extends AppCompatActivity {

    private Retrofit mRestAdapter;
    private SaludMockApi mSaludMockApi;

    // UI references.
    private ImageView mLogoView;
    private EditText mUserIdView;
    private EditText mNameView;
    private EditText mAddressView;
    private EditText mGenderView;
    private EditText mPasswordView;
    private TextInputLayout mFloatLabelUserId;
    private TextInputLayout mFloatLabelName;
    private TextInputLayout mFloatLabelAddress;
    private TextInputLayout mFloatLabelGender;
    private TextInputLayout mFloatLabelPassword;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Crear adaptador Retrofit
        mRestAdapter = new Retrofit.Builder()
                .baseUrl(SaludMockApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Crear conexión a la API de SaludMock
        mSaludMockApi = mRestAdapter.create(SaludMockApi.class);

        mLogoView = (ImageView) findViewById(R.id.image_logo);
        mUserIdView = (EditText) findViewById(R.id.user_id);
        mNameView = (EditText) findViewById(R.id.user_name);
        mAddressView = (EditText) findViewById(R.id.user_address);
        mGenderView = (EditText) findViewById(R.id.user_gender);
        mPasswordView = (EditText) findViewById(R.id.password);
        mFloatLabelUserId = (TextInputLayout) findViewById(R.id.float_label_user_id);
        mFloatLabelName = (TextInputLayout) findViewById(R.id.float_label_user_name);
        mFloatLabelAddress = (TextInputLayout) findViewById(R.id.float_label_user_address);
        mFloatLabelGender = (TextInputLayout) findViewById(R.id.float_label_user_gender);
        mFloatLabelPassword = (TextInputLayout) findViewById(R.id.float_label_password);
        //Button mSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        Button mRegisterButton = (Button) findViewById(R.id.email_register_button);
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);



        mRegisterButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegister();
                //Toast.makeText(RegisterActivity.this, "Aca se la pego al web service register", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void attemptRegister() {

        // Reset errors.
        mFloatLabelUserId.setError(null);
        mFloatLabelName.setError(null);
        mFloatLabelAddress.setError(null);
        mFloatLabelGender.setError(null);
        mFloatLabelPassword.setError(null);

        // Store values at the time of the login attempt.
        String userId = mUserIdView.getText().toString();
        String name = mNameView.getText().toString();
        String address = mAddressView.getText().toString();
        String gender = mGenderView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mFloatLabelPassword.setError(getString(R.string.error_field_required));
            focusView = mFloatLabelPassword;
            cancel = true;
        } else if (!isPasswordValid(password)) {
            mFloatLabelPassword.setError(getString(R.string.error_invalid_password));
            focusView = mFloatLabelPassword;
            cancel = true;
        }

        // Verificar si el ID tiene contenido.
        if (TextUtils.isEmpty(userId)) {
            mFloatLabelUserId.setError(getString(R.string.error_field_required));
            focusView = mFloatLabelUserId;
            cancel = true;
        } else if (!isUserIdValid(userId)) {
            mFloatLabelUserId.setError(getString(R.string.error_invalid_user_id));
            focusView = mFloatLabelUserId;
            cancel = true;
        }

        // Verificar si el Nombre tiene contenido.
        if (TextUtils.isEmpty(name)) {
            mFloatLabelName.setError(getString(R.string.error_field_required));
            focusView = mFloatLabelName;
            cancel = true;
        } else if (!isNameValid(name)) {
            mFloatLabelName.setError(getString(R.string.error_invalid_name));
            focusView = mFloatLabelName;
            cancel = true;
        }

        // Verificar si la Dirección tiene contenido.
        if (TextUtils.isEmpty(address)) {
            mFloatLabelAddress.setError(getString(R.string.error_field_required));
            focusView = mFloatLabelAddress;
            cancel = true;
        } else if (!isAddressValid(address)) {
            mFloatLabelAddress.setError(getString(R.string.error_invalid_address));
            focusView = mFloatLabelAddress;
            cancel = true;
        }
        // Verificar si el Genero tiene contenido.
        if (TextUtils.isEmpty(gender)) {
            mFloatLabelGender.setError(getString(R.string.error_field_required));
            focusView = mFloatLabelGender;
            cancel = true;
        } else if (!isGenderValid(gender)) {
            mFloatLabelGender.setError(getString(R.string.error_invalid_gender));
            focusView = mFloatLabelGender;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Mostrar el indicador de carga y luego iniciar la petición asíncrona.
            showProgress(true);

            Call<Affiliate> registerCall = mSaludMockApi.register(new RegisterBody(userId, name, address, gender, password));
            registerCall.enqueue(new Callback<Affiliate>() {
                @Override
                public void onResponse(Call<Affiliate> call, Response<Affiliate> response) {
                    // Mostrar progreso
                    showProgress(false);

                    // Procesar errores
                    if (!response.isSuccessful()) {
                        String error = "Ha ocurrido un error. Contacte al administrador";
                        if (response.errorBody()
                                .contentType()
                                .subtype()
                                .equals("json")) {
                            ApiError apiError = ApiError.fromResponseBody(response.errorBody());

                            error = apiError.getMessage();
                            Log.d("RegisterActivity", apiError.getDeveloperMessage());
                        } else {
                            try {
                                // Reportar causas de error no relacionado con la API
                                Log.d("RegisterActivity", response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        showRegisterError(error);
                        return;
                    }
/*
                    // Guardar afiliado en preferencias
                    SessionPrefs.get(RegisterActivity.this).saveAffiliate(response.body());
*/
                    // Ir a la citas médicas
                    showAppointmentsScreen();
                }

                @Override
                public void onFailure(Call<Affiliate> call, Throwable t) {
                    showProgress(false);
                    showLoginError(t.getMessage());
                }
            });
        }
    }

    private boolean isUserIdValid(String userId) {
        return userId.length() == 10;
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4
                && password.length() < 10
                && password.split("\'").length == 1
                && password.split("\"").length == 1;
    }

    private boolean isNameValid(String name) {
        return name.length() > 4
                && name.length() < 10
                && name.split("\'").length == 1
                && name.split("\"").length == 1;
    }
    private boolean isAddressValid(String address) {
        return address.length() > 4
                && address.length() < 10
                && address.split("\'").length == 1
                && address.split("\"").length == 1;
    }
    private boolean isGenderValid(String gender) {
        return gender.length() == 1
                && (gender.equals("F") || gender.equals("M"));
    }

    private void showProgress(boolean show) {
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);

        int visibility = show ? View.GONE : View.VISIBLE;
        mLogoView.setVisibility(visibility);
        mLoginFormView.setVisibility(visibility);
    }

    private void showAppointmentsScreen() {
        startActivity(new Intent(this, AppointmentsActivity.class));
        finish();
    }

    private void showLoginError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
    }

    private void showRegisterError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
    }

    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }

    private void showLoginActivity() {
        startActivity(new Intent(this,LoginActivity.class));
        finish();
    }
    @Override
    public void onBackPressed() {
        showLoginActivity();
        return;
    }



}

