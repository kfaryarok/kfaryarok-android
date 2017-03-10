package io.github.kfaryarok.kfaryarokapp.updates;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import io.github.kfaryarok.kfaryarokapp.R;
import io.github.kfaryarok.kfaryarokapp.util.PreferenceUtil;

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
            if (update.getAffected() instanceof ClassesAffected) {
                // get user's class
                String userClazz = PreferenceUtil.getClassPreference(holder.itemView.getContext());

                // get the affected instance
                ClassesAffected affected = (ClassesAffected) update.getAffected();

                // we know the update MUST affect the user, so put his class first
                tvClass.setText(userClazz);

                // get all affected classes
                String[] classesAffected = affected.getClassesAffected();

                String tag = getClass().getSimpleName();

                // if there're more classes than the users, put a comma
                if (classesAffected.length > 1) {
                    tvClass.append(", ");
                }

                for (int i = 0; i < classesAffected.length; i++) {
                    String clazz = classesAffected[i];

                    // if update's class is not the user's class (which is already appended)
                    if (!clazz.equalsIgnoreCase(userClazz)) {
                        // put it too
                        tvClass.append(clazz);

                        // if the last class isn't the user's class and we haven't reached the last class, put a comma
                        if (!classesAffected[classesAffected.length - 1].equalsIgnoreCase(userClazz) && i != classesAffected.length - 1) {
                            tvClass.append(", ");
                        }
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
