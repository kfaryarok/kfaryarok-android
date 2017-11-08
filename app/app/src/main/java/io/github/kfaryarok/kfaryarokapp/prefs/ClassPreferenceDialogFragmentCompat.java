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

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.preference.DialogPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceDialogFragmentCompat;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.RadioGroup;

import io.github.kfaryarok.kfaryarokapp.R;
import io.github.kfaryarok.kfaryarokapp.util.ClassUtil;

/**
 * Fragment for the TimePreference to show when clicked, and to have control of it.
 *
 * @author tbsc on 10/03/2017
 */
public class ClassPreferenceDialogFragmentCompat extends PreferenceDialogFragmentCompat implements DialogPreference.TargetFragment {

    RadioGroup gradeRadioGroup;
    NumberPicker classNumPicker;

    @Override
    protected View onCreateDialogView(Context context) {
        return super.onCreateDialogView(context);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        if (dialog.getWindow() != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                dialog.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            }
        }
        return dialog;
    }

    @Override
    protected void onBindDialogView(View v) {
        super.onBindDialogView(v);

        gradeRadioGroup = v.findViewById(R.id.rg_dialog_grade);
        classNumPicker = v.findViewById(R.id.np_dialog_class_num);
        final ClassPreference pref = (ClassPreference) getPreference();

        // set options and set current selected entries
        gradeRadioGroup.check(convertGradeStringToRadioButtonRes(pref.grade));
        // used to change the number picker's max value based on the selected grade
        gradeRadioGroup.setOnCheckedChangeListener(
                (group, checkedId) ->
                        classNumPicker.setMaxValue(ClassUtil.getClassesInHebrewGrade(convertGradeRadioButtonResToString(getContext(), checkedId)))
        );
        classNumPicker.setMinValue(1);
        classNumPicker.setMaxValue(ClassUtil.getClassesInHebrewGrade(pref.grade));
        classNumPicker.setWrapSelectorWheel(false);
        classNumPicker.setValue(pref.classNum);
    }

    @Override
    public void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            @IdRes int gradeRes = gradeRadioGroup.getCheckedRadioButtonId();
            String grade = convertGradeRadioButtonResToString(getContext(), gradeRes);
            int classNum = classNumPicker.getValue();
            ClassPreference pref = (ClassPreference) getPreference();
            pref.setClass(grade, classNum);
            getPreference().setSummary(grade + classNum);
        }
    }

    @Override
    public Preference findPreference(CharSequence charSequence) {
        return getPreference();
    }

    /**
     * Takes the ID resource, checks if it matches any of the resources noted below, and returns
     * an equivalent as a string, based on matching string resources.
     * Valid resources:
     *   - {@link io.github.kfaryarok.kfaryarokapp.R.id#rb_dialog_grade_g}
     *   - {@link io.github.kfaryarok.kfaryarokapp.R.id#rb_dialog_grade_h}
     *   - {@link io.github.kfaryarok.kfaryarokapp.R.id#rb_dialog_grade_i}
     *   - {@link io.github.kfaryarok.kfaryarokapp.R.id#rb_dialog_grade_j}
     *   - {@link io.github.kfaryarok.kfaryarokapp.R.id#rb_dialog_grade_k}
     *   - {@link io.github.kfaryarok.kfaryarokapp.R.id#rb_dialog_grade_l}
     * Matching strings (in matching order to valid resources):
     *   - {@link io.github.kfaryarok.kfaryarokapp.R.string#grade_g}
     *   - {@link io.github.kfaryarok.kfaryarokapp.R.string#grade_h}
     *   - {@link io.github.kfaryarok.kfaryarokapp.R.string#grade_i}
     *   - {@link io.github.kfaryarok.kfaryarokapp.R.string#grade_j}
     *   - {@link io.github.kfaryarok.kfaryarokapp.R.string#grade_k}
     *   - {@link io.github.kfaryarok.kfaryarokapp.R.string#grade_l}
     * Example:
     * Input: {@link io.github.kfaryarok.kfaryarokapp.R.id#rb_dialog_grade_h}
     * Output: VALUE of {@link io.github.kfaryarok.kfaryarokapp.R.string#grade_h}
     *         which is ח
     *
     * @param ctx Used to get string grade strings
     * @param gradeRes ID resource to one of those noted above
     * @return Hebrew grade string, or null if not any of the resources noted above
     */
    @Nullable
    public static String convertGradeRadioButtonResToString(Context ctx, @IdRes int gradeRes) {
        switch (gradeRes) {
            case R.id.rb_dialog_grade_g:
                return ctx.getString(R.string.grade_g);
            case R.id.rb_dialog_grade_h:
                return ctx.getString(R.string.grade_h);
            case R.id.rb_dialog_grade_i:
                return ctx.getString(R.string.grade_i);
            case R.id.rb_dialog_grade_j:
                return ctx.getString(R.string.grade_j);
            case R.id.rb_dialog_grade_k:
                return ctx.getString(R.string.grade_k);
            case R.id.rb_dialog_grade_l:
                return ctx.getString(R.string.grade_l);
            default:
                return null;
        }
    }

    /**
     * Takes the grade string, checks to see if it's valid using {@link ClassUtil#isValidHebrewGrade(String)},
     * matches it with a radio button resource ID and returns it.
     * @param grade Hebrew grade
     * @return Radio button resource ID
     */
    @IdRes
    public static int convertGradeStringToRadioButtonRes(String grade) {
        if (grade == null || grade.length() == 0) {
            return 0;
        }

        if (!ClassUtil.isValidHebrewGrade(grade)) {
            return 0;
        }

        switch (grade) {
            case "ז":
                return R.id.rb_dialog_grade_g;
            case "ח":
                return R.id.rb_dialog_grade_h;
            case "ט":
                return R.id.rb_dialog_grade_i;
            case "י":
                return R.id.rb_dialog_grade_j;
            case "יא":
                return R.id.rb_dialog_grade_k;
            case "יב":
                return R.id.rb_dialog_grade_l;
            default:
                return 0;
        }
    }

}
