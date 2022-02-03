package com.alp.chatuygulamasi;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.core.Context;

import org.w3c.dom.Text;

import java.util.List;

public class MesageAdapter extends RecyclerView.Adapter<MesageAdapter.MessageHolder> {

  List<MessageModel> list;
  Context context;
  String userId;
  Boolean state = false;
  int viewSend =1,viewReceived =2;
    public MesageAdapter(List<MessageModel> list,   String userId) {
        this.list = list;

        this.userId = userId;
    }

    @NonNull
    @Override
    public MessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view;
       if (viewType == viewSend) {
          view =LayoutInflater.from(parent.getContext()).inflate(R.layout.send_layout, parent, false);

           return new MessageHolder(view);
       }else {
           view =LayoutInflater.from(parent.getContext()).inflate(R.layout.received_layout, parent, false);

           return new MessageHolder(view);
       }

    }



    @Override
    public void onBindViewHolder(@NonNull MessageHolder holder, int position) {
        final MessageModel messageModel = list.get(position);

        holder.textView.setText(messageModel.getMessage());
    }
    @Override
    public int getItemCount() {
        return list.size();
    }
    class MessageHolder extends RecyclerView.ViewHolder {
        TextView textView;
        public MessageHolder(@NonNull View itemView) {
            super(itemView);

            if (state){
                textView = itemView.findViewById(R.id.sendLayout_textView);
            }else {
                textView = itemView.findViewById(R.id.receivedLayout_textView);
            }



        }
    }

    @Override
    public int getItemViewType(int position) {
        if (list.get(position).getSender().equals(userId)){
            state = true;
            return viewSend;
        }
        else {
            state = false;
            return viewReceived;
        }
    }
}
