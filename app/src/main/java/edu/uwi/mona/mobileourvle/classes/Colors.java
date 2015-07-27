package edu.uwi.mona.mobileourvle.classes;

import android.content.Context;

import java.util.HashMap;

import edu.uwi.mona.mobileourvle.R;

/**
 * Created by javon_000 on 11/06/2015.
 */
public class Colors {

    public HashMap<Character,Integer> colorHashMap = new HashMap<>();
    private HashMap<Character, Integer> idMap = new HashMap<>();
    private static Context mContext;

    public void populate()
    {
        colorHashMap.put('A',mContext.getResources().getColor(R.color.A));
        colorHashMap.put('B',mContext.getResources().getColor(R.color.B));
        colorHashMap.put('C',mContext.getResources().getColor(R.color.C));
        colorHashMap.put('D',mContext.getResources().getColor(R.color.D));
        colorHashMap.put('E',mContext.getResources().getColor(R.color.E));
        colorHashMap.put('F',mContext.getResources().getColor(R.color.F));
        colorHashMap.put('G',mContext.getResources().getColor(R.color.G));
        colorHashMap.put('H',mContext.getResources().getColor(R.color.H));
        colorHashMap.put('I',mContext.getResources().getColor(R.color.I));
        colorHashMap.put('J',mContext.getResources().getColor(R.color.J));
        colorHashMap.put('K',mContext.getResources().getColor(R.color.K));
        colorHashMap.put('L',mContext.getResources().getColor(R.color.L));
        colorHashMap.put('M',mContext.getResources().getColor(R.color.M));
        colorHashMap.put('N',mContext.getResources().getColor(R.color.N));
        colorHashMap.put('O',mContext.getResources().getColor(R.color.O));
        colorHashMap.put('P',mContext.getResources().getColor(R.color.P));
        colorHashMap.put('Q',mContext.getResources().getColor(R.color.Q));
        colorHashMap.put('R',mContext.getResources().getColor(R.color.R));
        colorHashMap.put('S',mContext.getResources().getColor(R.color.S));
        colorHashMap.put('T',mContext.getResources().getColor(R.color.T));
        colorHashMap.put('U',mContext.getResources().getColor(R.color.U));
        colorHashMap.put('V',mContext.getResources().getColor(R.color.V));
        colorHashMap.put('W',mContext.getResources().getColor(R.color.W));
        colorHashMap.put('X',mContext.getResources().getColor(R.color.X));
        colorHashMap.put('Y',mContext.getResources().getColor(R.color.Y));
        colorHashMap.put('Z',mContext.getResources().getColor(R.color.Z));
    }

    public Colors (Context context)
    {
        mContext = context;
        populate();

    }

    public int getColor(Character c)
    {
        return colorHashMap.keySet().contains(c) ? colorHashMap.get(c):R.color.standard;
    }
}
