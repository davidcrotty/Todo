package com.david.todo.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.david.todo.R;
import com.david.todo.model.TaskItemModel;

import java.util.ArrayList;

/**
 * Created by David Crotty on 18/10/2015.
 */
public class TodoItemListAdapter extends RecyclerView.Adapter<TodoItemListAdapter.ViewHolder> {
    private ArrayList<TaskItemModel> _taskItemModelList;

    /*Google recommend making this static as to avoid leaking, an alternative is to pass an instance
    of view holder into the constructor. */
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView _title, _description;
        public ViewHolder(View itemView) {
            super(itemView);
            _title = (TextView) itemView.findViewById(R.id.title_text);
            _description = (TextView) itemView.findViewById(R.id.description_text);
        }
    }

    public TodoItemListAdapter(ArrayList<TaskItemModel> taskItemModelList) {
        _taskItemModelList = taskItemModelList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TaskItemModel taskItemModel = _taskItemModelList.get(position);
        holder._description.setText(taskItemModel._title);
        holder._description.setText(taskItemModel._description);
    }

    @Override
    public int getItemCount() {
        return _taskItemModelList.size();
    }

}
