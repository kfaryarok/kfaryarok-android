package io.github.kfaryarok.kfaryarokapp.updates;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import io.github.kfaryarok.kfaryarokapp.R;

/**
 * Update adapter for creating cards to display in the recycler view.
 *
 * Created by tbsc on 03/03/2017.
 */
public class UpdateAdapter extends RecyclerView.Adapter<UpdateAdapter.UpdateViewHolder> {

    private Update[] mUpdates;

    private int mItemCount;

    public UpdateAdapter(int itemCount, Update[] updates) {
        this.mItemCount = itemCount;
        this.mUpdates = updates;
    }

    @Override
    public UpdateViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new UpdateViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.update_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(UpdateViewHolder holder, int position) {
        View itemView = holder.itemView;
        Update update = mUpdates[position];

        // set short text
        TextView tvShort = (TextView) itemView.findViewById(R.id.tv_updatecard_text);
        String text = " " + update.getText();
        tvShort.setText(text); // so android studio won't complain about concatenation

        // allow viewing long text if there is one
        if (update.hasLongText()) {
            // shows more button
            Button btnMore = (Button) itemView.findViewById(R.id.btn_updatecard_more);
            btnMore.setVisibility(View.VISIBLE);
        }

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
                    String clazz = classesAffected[i];

                    tvClass.append(clazz);

                    if (i != classesAffected.length - 1)
                        tvClass.append(", ");
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return mItemCount;
    }

    public class UpdateViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public UpdateViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void onClick(View v) {
            // TODO if has long text, show activity with it
        }

    }

}
