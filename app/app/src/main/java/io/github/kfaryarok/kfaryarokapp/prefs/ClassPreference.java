package io.github.kfaryarok.kfaryarokapp.prefs;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.preference.DialogPreference;
import android.util.AttributeSet;

import io.github.kfaryarok.kfaryarokapp.R;
import io.github.kfaryarok.kfaryarokapp.util.ClassUtil;

/**
 * Shows a dialog with two side-by-side checkbox lists
 *
 * @author tbsc on 29/03/2017
 */
public class ClassPreference extends DialogPreference {

    String mGrade;
    int mClassNum;

    public ClassPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        setDialogLayoutResource(R.layout.dialog_class);
        setDialogTitle(R.string.dialog_pref_class_title);
        setFragment(ClassPreferenceDialogFragmentCompat.class.getSimpleName());
        setDialogIcon(null);
    }

    public void setClass(String grade, int classNum) {
        this.mGrade = grade;
        this.mClassNum = classNum;
        persistString(grade + classNum);
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getString(index);
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        if (restorePersistedValue) {
            String persistedString = getPersistedString(mGrade + mClassNum);
            setClass(ClassUtil.parseHebrewGrade(persistedString), ClassUtil.parseHebrewClassNumber(persistedString));
        } else {
            String defaultString = (String) defaultValue;
            setClass(ClassUtil.parseHebrewGrade(defaultString), ClassUtil.parseHebrewClassNumber(defaultString));
        }
    }

}
