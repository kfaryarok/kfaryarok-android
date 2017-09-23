/*
 * This file is part of kfaryarok-android.
 *
 * kfaryarok-android is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * kfaryarok-android is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with kfaryarok-android.  If not, see <http://www.gnu.org/licenses/>.
 */

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

    String grade;
    int classNum;

    public ClassPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        setDialogLayoutResource(R.layout.dialog_class);
        setDialogTitle(R.string.dialog_pref_class_title);
        setFragment(ClassPreferenceDialogFragmentCompat.class.getSimpleName());
        setDialogIcon(null);
    }

    public void setClass(String grade, int classNum) {
        this.grade = grade;
        this.classNum = classNum;
        persistString(grade + classNum);
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getString(index);
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        if (restorePersistedValue) {
            String persistedString = getPersistedString(grade + classNum);
            setClass(ClassUtil.parseHebrewGrade(persistedString), ClassUtil.parseHebrewClassNumber(persistedString));
        } else {
            String defaultString = (String) defaultValue;
            setClass(ClassUtil.parseHebrewGrade(defaultString), ClassUtil.parseHebrewClassNumber(defaultString));
        }
    }

}
