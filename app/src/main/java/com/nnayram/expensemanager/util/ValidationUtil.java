package com.nnayram.expensemanager.util;

import android.widget.EditText;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by Rufo on 1/18/2017.
 */
public class ValidationUtil {
    private static final String ERROR_EDIT_TEXT = "Field must not be empty.";

    public static boolean isEmpty(EditText etText) {
        boolean isEmpty = StringUtils.isEmpty(etText.toString());
        if (isEmpty) {
            etText.setError(ERROR_EDIT_TEXT);
        } else {
            etText.setError(null);
        }

        return isEmpty;
    }
}
