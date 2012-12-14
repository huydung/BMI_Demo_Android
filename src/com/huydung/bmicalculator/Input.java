package com.huydung.bmicalculator;

import java.security.InvalidParameterException;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.huydung.bmicalculator.model.BMIStatus;
import com.huydung.bmicalculator.model.BMIUtil;

public class Input extends Activity implements SeekBar.OnSeekBarChangeListener {

	private static final int MIN_HEIGHT = 100;
	private static final int MIN_WEIGHT = 30;
	
	SeekBar sliderHeight;
	SeekBar sliderWeight;
	TextView txtHeight;
	TextView txtWeight;
	ImageView iconView;
	RelativeLayout groupResult;
	
	TextView txtResult;
	TextView txtYourBMI;
	
	BMIUtil bmiUtil;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);
        
        //get Controls
        sliderHeight = (SeekBar) findViewById(R.id.sliderHeight);
        sliderWeight = (SeekBar) findViewById(R.id.sliderWeight);
        txtHeight = (TextView) findViewById(R.id.txtCurrentHeight);
        txtWeight = (TextView) findViewById(R.id.txtCurrentWeight);
        iconView = (ImageView) findViewById(R.id.iconView);
        groupResult = (RelativeLayout) findViewById(R.id.groupResult);
        txtResult = (TextView) findViewById(R.id.txtResultStatus);
        txtYourBMI = (TextView) findViewById(R.id.txtYourBMI);
        txtHeight.setText(String.format("%.2fm", (float)MIN_HEIGHT / 100));
        txtWeight.setText(String.format("%dkg", MIN_WEIGHT));
        
        //Initialize the BMIUtil
        bmiUtil = new BMIUtil(this);
        
        //Try to check the values that saved before, if any.
        //Notice we're doing this BEFORE assign event listener to sliders, to avoid loop
        boolean hasValidValues = true;
        if( bmiUtil.getCentimeters() > -1 && bmiUtil.getCentimeters() < sliderHeight.getMax() + MIN_HEIGHT )
        {
        	sliderHeight.setProgress(bmiUtil.getCentimeters() - MIN_HEIGHT);
        }
        else
        {
        	hasValidValues = false;
        }
        if( bmiUtil.getKgs() > -1 && bmiUtil.getKgs() < sliderWeight.getMax() + MIN_WEIGHT)
        {
        	sliderWeight.setProgress(bmiUtil.getKgs() - MIN_WEIGHT);
        }
        else
        {
        	hasValidValues = false;
        }
        groupResult.setVisibility( hasValidValues ? View.VISIBLE : View.INVISIBLE ); 
        if( hasValidValues )
        {
			updateLayout(bmiUtil.calculate());
        }
     
        //Now we assign listener
        sliderHeight.setOnSeekBarChangeListener(this);
        sliderWeight.setOnSeekBarChangeListener(this);
    }


	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		if( seekBar == sliderHeight )
		{
			String text = String.format("%.2fm", (float)(MIN_HEIGHT+progress) / 100.0f);
			txtHeight.setText(text);
		}
		else if ( seekBar == sliderWeight )
		{
			txtWeight.setText((MIN_WEIGHT+progress) + "kg");
		}
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		//Do nothing
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		try
		{
			float bmi = bmiUtil.calculate(
					MIN_HEIGHT+sliderHeight.getProgress(), 
					MIN_WEIGHT+sliderWeight.getProgress());
			groupResult.setVisibility(View.VISIBLE);
			updateLayout(bmi);
		}
		catch(InvalidParameterException e)
		{
			Log.e("HDVD", "Exception Occurs: " + e.getMessage());
			groupResult.setVisibility(View.INVISIBLE);
		}
	}
	
	protected void updateLayout(float bmi)
	{
		BMIStatus status = bmiUtil.getStatus(bmi);
		Resources res = getResources();
		txtYourBMI.setText(String.format(
			res.getString(R.string.yourbmi), bmi
		));
		switch(status)
		{
		case THIN:
			txtResult.setText(res.getString(R.string.thin));
			iconView.setImageDrawable(res.getDrawable(R.drawable.thin));
			break;
		case NORMAL:
			txtResult.setText(res.getString(R.string.normal));
			iconView.setImageDrawable(res.getDrawable(R.drawable.normal));
			break;
		case FAT:
			txtResult.setText(res.getString(R.string.fat));
			iconView.setImageDrawable(res.getDrawable(R.drawable.fat));
			break;
		case OBESITY:
			txtResult.setText(res.getString(R.string.obesity));
			iconView.setImageDrawable(res.getDrawable(R.drawable.obesity));
			break;
		}
	}

}
