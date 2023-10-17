// Generated by view binder compiler. Do not edit!
package com.example.gathernow.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.gathernow.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityLogInBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final TextView alert;

  @NonNull
  public final AppCompatEditText email;

  @NonNull
  public final TextView emailText;

  @NonNull
  public final AppCompatButton login;

  @NonNull
  public final TextView loginText;

  @NonNull
  public final AppCompatEditText password;

  @NonNull
  public final TextView passwordText;

  private ActivityLogInBinding(@NonNull ConstraintLayout rootView, @NonNull TextView alert,
      @NonNull AppCompatEditText email, @NonNull TextView emailText, @NonNull AppCompatButton login,
      @NonNull TextView loginText, @NonNull AppCompatEditText password,
      @NonNull TextView passwordText) {
    this.rootView = rootView;
    this.alert = alert;
    this.email = email;
    this.emailText = emailText;
    this.login = login;
    this.loginText = loginText;
    this.password = password;
    this.passwordText = passwordText;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityLogInBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityLogInBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_log_in, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityLogInBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.alert;
      TextView alert = ViewBindings.findChildViewById(rootView, id);
      if (alert == null) {
        break missingId;
      }

      id = R.id.email;
      AppCompatEditText email = ViewBindings.findChildViewById(rootView, id);
      if (email == null) {
        break missingId;
      }

      id = R.id.emailText;
      TextView emailText = ViewBindings.findChildViewById(rootView, id);
      if (emailText == null) {
        break missingId;
      }

      id = R.id.login;
      AppCompatButton login = ViewBindings.findChildViewById(rootView, id);
      if (login == null) {
        break missingId;
      }

      id = R.id.loginText;
      TextView loginText = ViewBindings.findChildViewById(rootView, id);
      if (loginText == null) {
        break missingId;
      }

      id = R.id.password;
      AppCompatEditText password = ViewBindings.findChildViewById(rootView, id);
      if (password == null) {
        break missingId;
      }

      id = R.id.passwordText;
      TextView passwordText = ViewBindings.findChildViewById(rootView, id);
      if (passwordText == null) {
        break missingId;
      }

      return new ActivityLogInBinding((ConstraintLayout) rootView, alert, email, emailText, login,
          loginText, password, passwordText);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}