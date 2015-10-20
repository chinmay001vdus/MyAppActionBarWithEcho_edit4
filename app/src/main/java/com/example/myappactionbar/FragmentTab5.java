package com.example.myappactionbar;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class FragmentTab5 extends Fragment {
	communicate cm;
	private Context globalContext = null;
	Button buttonFx1,buttonFx2,buttonFx3,buttonFx4,buttonFx5,buttonFx6,buttonFx7,buttonFx8,buttonFx9;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View audioFxAuto = inflater.inflate(R.layout.tab_frag5, container, false);


		return audioFxAuto;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		globalContext = getActivity();

	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		//buttonFx1 = (Button)view.findViewById(R.id.button1);
	}

	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		cm = (communicate) getActivity();
	}






}
