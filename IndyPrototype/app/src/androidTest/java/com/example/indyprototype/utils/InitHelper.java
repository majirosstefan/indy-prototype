package com.example.indyprototype.utils;


public class InitHelper {


	public void init() {

		// necessary to load shared libraries first
		System.loadLibrary("gnustl_shared");
		System.loadLibrary("indy");

	}
}

