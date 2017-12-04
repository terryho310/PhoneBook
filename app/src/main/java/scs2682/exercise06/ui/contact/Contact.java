package scs2682.exercise06.ui.contact;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import scs2682.exercise06.AppActivity;
import scs2682.exercise06.R;
import scs2682.exercise06.data.ContactInfo;

public class Contact extends Fragment {
	public static final String NAME = Contact.class.getSimpleName();

	private static final String KEY_FIRST_NAME = "firstName";
	private static final String KEY_LAST_NAME = "lastName";
	private static final String KEY_PHONE = "phone";
	private static final String KEY_EMAIL = "email";

	/**
	 * Creates and adds lazily a new instance of this fragment.
	 * <p/>
	 * Only one instance of this fragment is allowed to live in memory.
	 */
	public static void create(@NonNull FragmentManager fragmentManager, int parentViewId, @Nullable String firstName,
		@Nullable String lastName, @Nullable String phone, @Nullable String email) {
		Bundle bundle = new Bundle();
		bundle.putString(KEY_FIRST_NAME, firstName);
		bundle.putString(KEY_LAST_NAME, lastName);
		bundle.putString(KEY_PHONE, phone);
		bundle.putString(KEY_EMAIL, email);

		Contact form = new Contact();
		form.setArguments(bundle);

		// only one instance is allowed in memory, so let's ensure we remove a previous one
		destroy(fragmentManager);

		// and then add the new instance
		fragmentManager.beginTransaction()
			.add(parentViewId, form, NAME)
			.commit();
	}

	public static void destroy(@NonNull FragmentManager fragmentManager) {
		Fragment instance = fragmentManager.findFragmentByTag(NAME);
		if (instance != null) {
			fragmentManager.beginTransaction()
				.remove(instance)
				.commit();
		}
	}

	private EditText firstName;
	private EditText lastName;
	private EditText phone;
	private EditText email;

	private Button cancel;
	private Button update;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.contact, container, false);
	}

	@Override
	public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
		String firstNameValue = "";
		String lastNameValue = "";
		String phoneValue = "";
		String emailValue = "";

		if (savedInstanceState != null) {
			// from instance state - saved in onSaveInstanceState()
			firstNameValue = savedInstanceState.getString(KEY_FIRST_NAME, "");
			lastNameValue = savedInstanceState.getString(KEY_LAST_NAME, "");
			phoneValue = savedInstanceState.getString(KEY_PHONE, "");
			emailValue = savedInstanceState.getString(KEY_EMAIL, "");
		}
		else if (getArguments() != null) {
			// created first time in AppActivity by calling Form.create()
			firstNameValue = getArguments().getString(KEY_FIRST_NAME, "");
			lastNameValue = getArguments().getString(KEY_LAST_NAME, "");
			phoneValue = getArguments().getString(KEY_PHONE, "");
			emailValue = getArguments().getString(KEY_EMAIL, "");
		}

		firstName = view.findViewById(R.id.firstName);
		firstName.setText(firstNameValue);

		lastName = view.findViewById(R.id.lastName);
		lastName.setText(lastNameValue);

		phone = view.findViewById(R.id.phone);
		phone.setText(phoneValue);

		email = view.findViewById(R.id.email);
		email.setText(emailValue);

		cancel = view.findViewById(R.id.cancel);
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(final View v) {
				// Using AppActivity.getSupportFragmentManager(), since AppActivity derives from AppCompatActivity
				AppActivity appActivity = (AppActivity) getActivity();
				Contact.destroy(appActivity.getSupportFragmentManager());
			}
		});

		update = view.findViewById(R.id.update);
		update.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(final View v) {
				String firstNameValue = firstName.getText().toString();
				String emailValue = email.getText().toString();

				if (TextUtils.isEmpty(firstNameValue)){
					Toast.makeText(getContext(), getString(R.string.first_name_cannot_be_empty), Toast.LENGTH_SHORT).show();
				}
				else if (TextUtils.isEmpty(emailValue)){
					Toast.makeText(getContext(), getString(R.string.email_cannot_be_empty), Toast.LENGTH_SHORT).show();
				}
				else {
					// everything is fine, pass the value
					AppActivity appActivity = (AppActivity) getActivity();
					ContactInfo contactInfo = new ContactInfo(firstNameValue, lastName.getText().toString(),
							phone.getText().toString(), emailValue);
					appActivity.onContactUpdated(contactInfo);
				}
			}
		});
	}

	@Override
	public void onSaveInstanceState(final Bundle outState) {
		super.onSaveInstanceState(outState);

		// store all needed value for reading them back after a configuration change happens
		outState.putString(KEY_FIRST_NAME, firstName.getText().toString());
		outState.putString(KEY_LAST_NAME, lastName.getText().toString());
		outState.putString(KEY_PHONE, phone.getText().toString());
		outState.putString(KEY_EMAIL, email.getText().toString());
	}
}
