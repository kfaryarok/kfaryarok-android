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

    public Update[] mUpdates;
    public int mItemCount;
    private final UpdateAdapterOnClickHandler mClickHandler;

    public UpdateAdapter(Update[] updates, UpdateAdapterOnClickHandler clickHandler) {
        this.mItemCount = updates == null ? 0 : updates.length;
        this.mUpdates = updates;
        this.mClickHandler = clickHandler;
    }

    @Override
    public UpdateViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new UpdateViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.update_list_item_relative, parent, false));
    }

    @Override
    public void onBindViewHolder(UpdateViewHolder holder, int position) {
        final View itemView = holder.itemView;
        Update update = mUpdates[position];

        // set text
        final TextView tvText = (TextView) itemView.findViewById(R.id.tv_updatecard_text);
        tvText.setText(update.getText());

        // messy hack that android forces me to use
        // i can't get the line count without queuing a runnable, because it isn't known yet
        // and will only be known once the layout is drawn
        // queuing a runnable will make sure that it's called after drawing
        tvText.post(new Runnable() {

            @Override
            public void run() {
                // if line count is more than the max
                if (tvText.getLineCount() > 3) {
                    // set expand view to be visible
                    View viewExpand = itemView.findViewById(R.id.view_updatecard_expand);
                    viewExpand.setVisibility(View.VISIBLE);
                }
            }

        });

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
                    mClickHandler.onClickCard(v, mUpdates[getAdapterPosition()]);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mClickHandler.onClickOptions(v, mUpdates[getAdapterPosition()], (Button) v.findViewById(R.id.btn_updatecard_options));
                    return true;
                }
            });

            // click handler for options button
            itemView.findViewById(R.id.btn_updatecard_options).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClickHandler.onClickOptions(v, mUpdates[getAdapterPosition()], (Button) v);
                }
            });
        }
    }

    public interface UpdateAdapterOnClickHandler {
        void onClickCard(View v, Update update);
        void onClickOptions(View v, Update update, Button buttonView); /* bypassing limitations */
    }

}
