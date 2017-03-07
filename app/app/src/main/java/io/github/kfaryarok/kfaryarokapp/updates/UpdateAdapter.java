package io.github.kfaryarok.kfaryarokapp.updates;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import io.github.kfaryarok.kfaryarokapp.R;
import io.github.kfaryarok.kfaryarokapp.util.ClassUtil;
import io.github.kfaryarok.kfaryarokapp.util.ScreenUtil;

/**
 * Update adapter for creating cards to display in the recycler view.
 *
 * @author tbsc on 03/03/2017
 */
public class UpdateAdapter extends RecyclerView.Adapter<UpdateAdapter.UpdateViewHolder> {

    private Update[] mUpdates;
    private int mItemCount;
    private final UpdateAdapterOnClickHandler mClickHandler;

    public UpdateAdapter(Update[] updates, UpdateAdapterOnClickHandler clickHandler) {
        this.mItemCount = updates.length;
        this.mUpdates = updates;
        this.mClickHandler = clickHandler;
    }

    @Override
    public UpdateViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new UpdateViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.update_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(UpdateViewHolder holder, int position) {
        View itemView = holder.itemView;
        Update update = mUpdates[position];

        // set text
        TextView tvText = (TextView) itemView.findViewById(R.id.tv_updatecard_text);
        String text = " " + update.getText();
        tvText.setText(text); // so android studio won't complain about concatenation

        // set class
        TextView tvClass = (TextView) itemView.findViewById(R.id.tv_updatecard_class);
        if (update.getAffected().isGlobal()) {
            // tells user it's a global update
            tvClass.setText(R.string.global_update);
        } else {
            // appends all classes to the class textview
            // TODO make so that your class is appended first
            if (update.getAffected() instanceof ClassesAffected) {
                ClassesAffected affected = (ClassesAffected) update.getAffected();
                tvClass.setText("");
                String[] classesAffected = affected.getClassesAffected();
                for (int i = 0; i < classesAffected.length; i++) {
                    // if class is in English, convert to Hebrew
                    String clazz = ClassUtil.checkValidEnglishClassName(classesAffected[i]) ?
                            ClassUtil.convertEnglishClassToHebrew(classesAffected[i]) : classesAffected[i];

                    tvClass.append(clazz);

                    // once text length reaches certain size, stop appending and add 3 dots
                    Rect bounds = new Rect();
                    tvClass.getPaint().getTextBounds(tvClass.getText().toString(), 0, tvClass.getText().length(), bounds);
                    if (ScreenUtil.pxToDp(bounds.width()) >= 120) {
                        tvClass.append("...");
                        break;
                    }

                    if (i != classesAffected.length - 1) {
                        tvClass.append(", ");
                    }
                }

            }
        }
    }

    @Override
    public int getItemCount() {
        return mItemCount;
    }

    public class UpdateViewHolder extends RecyclerView.ViewHolder {

        public UpdateViewHolder(final View itemView) {
            super(itemView);

            // hacky way to handle clicks, but it works i guess
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClickHandler.onClickCard(mUpdates[getAdapterPosition()]);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mClickHandler.onClickOptions(mUpdates[getAdapterPosition()]);
                    return true;
                }
            });

            // click handler for options button
            itemView.findViewById(R.id.btn_updatecard_options).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClickHandler.onClickOptions(mUpdates[getAdapterPosition()]);
                }
            });
        }
    }

    public interface UpdateAdapterOnClickHandler {
        void onClickCard(Update update);
        void onClickOptions(Update update);
    }

}
