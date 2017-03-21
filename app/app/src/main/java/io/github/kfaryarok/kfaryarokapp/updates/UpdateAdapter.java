package io.github.kfaryarok.kfaryarokapp.updates;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
        tvText.setText(update.getText());

        // set class
        TextView tvClass = (TextView) itemView.findViewById(R.id.tv_updatecard_class);
        if (update.getAffected().isGlobal()) {
            // tells user it's a global update
            tvClass.setText(R.string.global_update);
        } else {
            // appends all classes to the class textview
            if (update.getAffected() instanceof ClassesAffected) {
                // get user's class
                String userClass = PreferenceUtil.getClassPreference(holder.itemView.getContext());

                // get the affected instance
                ClassesAffected affected = (ClassesAffected) update.getAffected();

                // set class textview to show affected classes, with user's class first
                tvClass.setText(UpdateHelper.formatClassString(affected.getClassesAffected(), userClass));
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
                    mClickHandler.onClickOptions(mUpdates[getAdapterPosition()], (Button) v.findViewById(R.id.btn_updatecard_options));
                    return true;
                }
            });

            // click handler for options button
            itemView.findViewById(R.id.btn_updatecard_options).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClickHandler.onClickOptions(mUpdates[getAdapterPosition()], (Button) v);
                }
            });
        }
    }

    public interface UpdateAdapterOnClickHandler {
        void onClickCard(Update update);
        void onClickOptions(Update update, Button buttonView); /* bypassing limitations */
    }

}
