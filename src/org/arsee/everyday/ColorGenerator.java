package org.arsee.everyday;

import android.graphics.Color;

public class ColorGenerator {
	private static final int[] COLORS={Color.BLUE, Color.RED};
	
	private int prevColor=COLORS[0];
	
	int getNewColor()
	{
		for( int i=0; i<COLORS.length; i++){
			if( COLORS[i] != prevColor){
				prevColor = COLORS[i];
				return COLORS[i];
			}				
		}
		
		return 0;
	}
	
}
