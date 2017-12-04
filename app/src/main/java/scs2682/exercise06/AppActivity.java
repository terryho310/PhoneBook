package scs2682.exercise06;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;


import java.util.ArrayList;
import java.util.List;

import scs2682.exercise06.data.ContactInfo;
import scs2682.exercise06.ui.app.SpinnerAdapter;
import scs2682.exercise06.ui.contact.Contact;

public class AppActivity extends AppCompatActivity {
	public static final String NAME = AppActivity.class.getSimpleName();

	private Button add;
	private Spinner spinner;
	private SpinnerAdapter spinnerAdapter;

	private List<ContactInfo> contacts = new ArrayList<>();

	private AdapterView.OnItemSelectedListener onItemSelectedListener = new AdapterView.OnItemSelectedListener() {
		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
			ContactInfo contactInfo = spinnerAdapter.getItem(position);
			spinnerAdapter.setSelectItemPosition(position);

			showContact(contactInfo);
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// where there was a selection but got lost while tap was still on

		}
	};

	/**
	 * Called from 'Contact' fragment
	 *
	 * @param contactInfo Null or an instance. If null, disregard
	 */

	public void onContactUpdated(ContactInfo contactInfo){
		if (contactInfo != null && !contactInfo.getEmail().isEmpty()){
			//There is data, we can add it or update the spinner
			int position = contacts.indexOf(contactInfo);

			if (position > -1){
				//we have a contact already, let's update it
				contacts.set(position, contactInfo);
			}
			else{
				// -1 means brand new contact, let's add it at the end
				contacts.add(contactInfo);
				position = contacts.size() - 1;
			}

			// force update the adapter
			spinnerAdapter.notifyDataSetChanged();

			// skip on-selected-item even to be fired
			//if you omit this line, it will force this event,
			//which will add Contact fragment instance
			//every time you add or update a contact
			spinner.setOnItemSelectedListener(null);  //disable the bug
			spinner.setSelection(position, false);
			spinnerAdapter.setSelectItemPosition(position);

			spinner.setOnItemSelectedListener(onItemSelectedListener);
		}

		// hide spinner if contacts is empty
		int visibility = contacts.isEmpty() ? View.INVISIBLE : View.VISIBLE;
		spinner.setVisibility(visibility);

		// at the end, we need to remove Contact
		Contact.destroy(getSupportFragmentManager());
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.appactivity);

		//read and set values from saved instance state
		ArrayList<Bundle> contactBundles = null;

		if (savedInstanceState!= null && savedInstanceState.containsKey(NAME)){
			// from a configuration change
			contactBundles = savedInstanceState.getParcelableArrayList(NAME);

			if(contactBundles != null){
				//we have stored values from a saved instance state, let's read and
				//convert them back into ContactInfo object
				for (Bundle contactBundle : contactBundles){
					ContactInfo contactInfo = new ContactInfo(contactBundle);
					contacts.add(contactInfo);
				}
			}
		}

		add = findViewById(R.id.add);
		add.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showContact(null);
			}
		});

		spinnerAdapter = new SpinnerAdapter(this, contacts);

		spinner = findViewById(R.id.spinner);
		spinner.setAdapter(spinnerAdapter);
		spinner.setOnItemSelectedListener(onItemSelectedListener);

		if (spinnerAdapter.getCount() > 0){
			//there are contact to be shown in spinner
			spinner.setVisibility(View.VISIBLE);
		}
		else {
			// no contacts, then hide spinner
			spinner.setVisibility(View.INVISIBLE);
		}

	}

	/**
	 *
	 * @param contact If null means create a brand new contact
	 */

	private void showContact(ContactInfo contact){
		// first ensure we remove any previous instances of contact
		Contact.destroy(getSupportFragmentManager());

		// then assigned default values
		String firstNameValue = "";
		String lastNameValue = "";
		String phoneValue = "";
		String emailValue = "";

		if (contact != null){
			// we have contact details, just add them
			firstNameValue = contact.getFirstName();
			lastNameValue = contact.getLastName();
			phoneValue = contact.getPhone();
			emailValue = contact.getEmail();
		}

		// and finally create a new Contact instance
		Contact.create(getSupportFragmentManager(), R.id.appActivity, firstNameValue, lastNameValue,
				phoneValue, emailValue);

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		ArrayList<Bundle> contactBundles = new ArrayList<>(contacts.size());
		for (ContactInfo contactInfo : contacts){
			contactBundles.add(contactInfo.getBundle());
		}

		outState.putParcelableArrayList(NAME, contactBundles);
	}
}
