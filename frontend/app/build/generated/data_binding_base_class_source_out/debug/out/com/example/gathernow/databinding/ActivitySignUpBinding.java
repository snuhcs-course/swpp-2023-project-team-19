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

public final class ActivitySignUpBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final TextView alert;

  @NonNull
  public final AppCompatEditText email;

  @NonNull
  public final TextView emailText;

  @NonNull
  public final AppCompatEditText name;

  @NonNull
  public final TextView nameText;

  @NonNull
  public final AppCompatEditText password;

  @NonNull
  public final AppCompatEditText passwordConfirm;

  @NonNull
  public final TextView passwordText;

  @NonNull
  public final TextView pwConfirmationText;

  @NonNull
  public final AppCompatButton signup;

  @NonNull
  public final TextView signupText;

  private ActivitySignUpBinding(@NonNull ConstraintLayout rootView, @NonNull TextView alert,
      @NonNull AppCompatEditText email, @NonNull TextView emailText,
      @NonNull AppCompatEditText name, @NonNull TextView nameText,
      @NonNull AppCompatEditText password, @NonNull AppCompatEditText passwordConfirm,
      @NonNull TextView passwordText, @NonNull TextView pwConfirmationText,
      @NonNull AppCompatButton signup, @NonNull TextView signupText) {
    this.rootView = rootView;
    this.alert = alert;
    this.email = email;
    this.emailText = emailText;
    this.name = name;
    this.nameText = nameText;
    this.password = password;
    this.passwordConfirm = passwordConfirm;
    this.passwordText = passwordText;
    this.pwConfirmationText = pwConfirmationText;
    this.signup = signup;
    this.signupText = signupText;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivitySignUpBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivitySignUpBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_sign_up, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivitySignUpBinding bind(@NonNull View rootView) {
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

      id = R.id.name;
      AppCompatEditText name = ViewBindings.findChildViewById(rootView, id);
      if (name == null) {
        break missingId;
      }

      id = R.id.nameText;
      TextView nameText = ViewBindings.findChildViewById(rootView, id);
      if (nameText == null) {
        break missingId;
      }

      id = R.id.password;
      AppCompatEditText password = ViewBindings.findChildViewById(rootView, id);
      if (password == null) {
        break missingId;
      }

      id = R.id.password_confirm;
      AppCompatEditText passwordConfirm = ViewBindings.findChildViewById(rootView, id);
      if (passwordConfirm == null) {
        break missingId;
      }

      id = R.id.passwordText;
      TextView passwordText = ViewBindings.findChildViewById(rootView, id);
      if (passwordText == null) {
        break missingId;
      }

      id = R.id.pwConfirmationText;
      TextView pwConfirmationText = ViewBindings.findChildViewById(rootView, id);
      if (pwConfirmationText == null) {
        break missingId;
      }

      id = R.id.signup;
      AppCompatButton signup = ViewBindings.findChildViewById(rootView, id);
      if (signup == null) {
        break missingId;
      }

      id = R.id.signupText;
      TextView signupText = ViewBindings.findChildViewById(rootView, id);
      if (signupText == null) {
        break missingId;
      }

      return new ActivitySignUpBinding((ConstraintLayout) rootView, alert, email, emailText, name,
          nameText, password, passwordConfirm, passwordText, pwConfirmationText, signup,
          signupText);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}