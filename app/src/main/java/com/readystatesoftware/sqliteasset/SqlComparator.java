package com.readystatesoftware.sqliteasset;

import android.util.Log;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper.SQLiteAssetException;

import java.util.Comparator;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class SqlComparator implements Comparator<String> {
    private static final String TAG = SQLiteAssetHelper.class.getSimpleName();
    private Pattern b = Pattern.compile(".*_upgrade_([0-9]+)-([0-9]+).*");

    SqlComparator() {
    }

    public int compare(String str, String str2) {
        Matcher matcher = this.b.matcher(str);
        Matcher matcher2 = this.b.matcher(str2);
        if (!matcher.matches()) {
            throw new SQLiteAssetException("Invalid upgrade script file");
        } else if (matcher2.matches()) {
            int i = 1;
            int intValue = Integer.parseInt(Objects.requireNonNull(matcher.group(1)));
            int intValue2 = Integer.parseInt(Objects.requireNonNull(matcher2.group(1)));
            int intValue3 = Integer.parseInt(Objects.requireNonNull(matcher.group(2)));
            int intValue4 = Integer.parseInt(Objects.requireNonNull(matcher2.group(2)));
            if (intValue != intValue2) {
                if (intValue < intValue2) {
                    i = -1;
                }
                return i;
            } else if (intValue3 == intValue4) {
                return 0;
            } else {
                if (intValue3 < intValue4) {
                    i = -1;
                }
                return i;
            }
        } else {
            throw new SQLiteAssetException("Invalid upgrade script file");
        }
    }
}
