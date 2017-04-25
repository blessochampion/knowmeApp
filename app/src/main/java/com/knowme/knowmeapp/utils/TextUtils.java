package com.knowme.knowmeapp.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by blessochampion on 4/25/17.
 */

public class TextUtils
{
    public static boolean isValidEmail(String email)
    {
        String expression = "^[\\w\\.]+@([\\w]+\\.)+[A-Z]{2,7}$";
        CharSequence inputString = email;
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputString);
        if (matcher.matches())
        {
            return true;
        }
        else{
            return false;
        }
    }
}
