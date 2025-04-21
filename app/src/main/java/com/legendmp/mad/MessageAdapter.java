package com.legendmp.mad;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private List<Message> messageList;

    // Constructor to accept messages data
    public MessageAdapter(List<Message> messageList) {
        this.messageList = messageList;
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
        return new MessageViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
        Message message = messageList.get(position);

        // Set message text
        holder.messageText.setText(message.getText());

        // Change the alignment for the sender and receiver
        if (message.isSender()) {
            holder.messageText.setBackgroundResource(R.drawable.sender_message_bg);
        } else {
            holder.messageText.setBackgroundResource(R.drawable.receiver_message_bg);
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    // ViewHolder for each message
    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageText;

        public MessageViewHolder(View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.textMessage);
        }
    }
}
