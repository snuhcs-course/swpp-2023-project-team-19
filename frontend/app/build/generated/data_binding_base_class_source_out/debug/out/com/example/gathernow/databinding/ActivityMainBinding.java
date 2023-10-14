// Generated by view binder compiler. Do not edit!
package com.example.gathernow.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.gathernow.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityMainBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final AppCompatButton loginButton;

  @NonNull
  public final TextView onboardingText;

  @NonNull
  public final AppCompatButton signupButton;

  private ActivityMainBinding(@NonNull ConstraintLayout rootView,
      @NonNull AppCompatButton loginButton, @NonNull TextView onboardingText,
      @NonNull AppCompatButton signupButton) {
    this.rootView = rootView;
    this.loginButton = loginButton;
    this.onboardingText = onboardingText;
    this.signupButton = signupButton;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityMainBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityMainBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_main, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityMainBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.loginButton;
      AppCompatButton loginButton = ViewBindings.findChildViewById(rootView, id);
      if (loginButton == null) {
        break missingId;
      }

      id = R.id.onboardingText;
      TextView onboardingText = ViewBindings.findChildViewById(rootView, id);
      if (onboardingText == null) {
        break missingId;
      }

      id = R.id.signupButton;
      AppCompatButton signupButton = ViewBindings.findChildViewById(rootView, id);
      if (signupButton == null) {
        break missingId;
      }

      return new ActivityMainBinding((ConstraintLayout) rootView, loginButton, onboardingText,
          signupButton);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
