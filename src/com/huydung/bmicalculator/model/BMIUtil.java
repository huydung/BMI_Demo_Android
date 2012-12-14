package com.huydung.bmicalculator.model;

import java.security.InvalidParameterException;

import android.content.Context;
import android.content.SharedPreferences;


public class BMIUtil {
	private Context context;
	private static final String CENTIMETERS = "centimeters";
	private static final String KGS = "kgs";
	private int centimeters = -1;
	private int kgs = -1;

	public BMIUtil(Context context) {
		super();
		this.context = context;
		loadValues();
	}

	public void saveValues(int centimeters, int kgs)
	{
		if( context != null )
		{
			SharedPreferences sf = context.getSharedPreferences("BMI", Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = sf.edit();
			editor.putInt(CENTIMETERS, centimeters);
			editor.putInt(KGS, kgs);
			editor.commit();
		}
	};
	
	public void loadValues()
	{
		if( context != null )
		{
			SharedPreferences sf = context.getSharedPreferences("BMI", Context.MODE_PRIVATE);
			centimeters = sf.getInt(CENTIMETERS, -1);
			kgs = sf.getInt(KGS, -1);
		}
	};
	
	public float calculate(int centimeters, int kgs)
	{
		if( centimeters > 0 && kgs > 0 && centimeters < 251 && kgs < 401)
		{
			saveValues(centimeters, kgs);
			float result = (kgs * 10000.0f) / ((float)centimeters * (float)centimeters);
			return result;
		}
		else
		{
			throw new InvalidParameterException("The centimeters must be within 0-250 and kg between 0-400");
		}
	};
	
	public float calculate()
	{
		if( centimeters < 0 || kgs < 0 )
		{
			loadValues();
		}
		if( centimeters < 0 || kgs < 0 )
		{
			throw new IllegalStateException("Default centimeters and kgs not set");
		}
		return calculate(centimeters, kgs);
	};
	
	public BMIStatus getStatus(float value)
	{
		int roundedValue = Math.round(value * 10.0f);
		if( roundedValue < 186 )
		{
			return BMIStatus.THIN;
		}
		else if( roundedValue < 250 )
		{
			return BMIStatus.NORMAL;
		}
		else if( roundedValue < 300 )
		{
			return BMIStatus.FAT;
		}
		else 
		{
			return BMIStatus.OBESITY;
		}
	}

	public int getCentimeters() {
		return centimeters;
	}

	public int getKgs() {
		return kgs;
	}
	
	
}
