package com.hit.hitchatapp.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hit.hitchatapp.databinding.ItemContainerReceiveMessageBinding;
import com.hit.hitchatapp.databinding.ItemContainerSentMessageBinding;
import com.hit.hitchatapp.models.ChatMessage;
import com.hit.hitchatapp.utilities.PreferenceManager;

import java.util.List;


public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<ChatMessage> chatMessages;
    private Bitmap receiverProfileImage;
    private final String senderId;
    private PreferenceManager preferenceManager;
    public static final int VIEW_TYPE_SENT = 1;
    public static final int VIEW_TYPE_RECEIVED = 2;

    public void setReceiverProfileImage(Bitmap bitmap) {
        receiverProfileImage = bitmap;
    }

    public ChatAdapter(List<ChatMessage> chatMessages, Bitmap receiverProfileImage, String senderId) {
        this.chatMessages = chatMessages;
        this.receiverProfileImage = receiverProfileImage;
        this.senderId = senderId;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_SENT) {
            return new SentMessageViewHolder(
                    ItemContainerSentMessageBinding.inflate(
                            LayoutInflater.from(parent.getContext()),
                            parent,
                            false
                    )
            );
        } else {
            return new ReceiveMessageViewHolder(
                    ItemContainerReceiveMessageBinding.inflate(
                            LayoutInflater.from(parent.getContext()),
                            parent,
                            false
                    )
            );
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (getItemViewType(position) == VIEW_TYPE_SENT) {
            ((SentMessageViewHolder) holder).setData(chatMessages.get(position));
        } else {
            ((ReceiveMessageViewHolder) holder).setData(chatMessages.get(position), receiverProfileImage);
        }
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (chatMessages.get(position).senderId.equals(senderId)) {
            return VIEW_TYPE_SENT;
        } else {
            return VIEW_TYPE_RECEIVED;
        }
    }

    static class SentMessageViewHolder extends RecyclerView.ViewHolder {

        private final ItemContainerSentMessageBinding binding;

        SentMessageViewHolder(ItemContainerSentMessageBinding itemContainerSentMessageBinding) {
            super(itemContainerSentMessageBinding.getRoot());
            binding = itemContainerSentMessageBinding;

            binding.textMessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    binding.textMessage.setText("short click");
                }

            });
            binding.textMessage.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    setLong();
                    return true;
                }
            });


        }

        private void setLong() {
            binding.textMessage.setText("Long Click");
        }

        void setData(ChatMessage chatMessage) {

            if (chatMessage.chatImage == null) {

                binding.textMessage.setText(chatMessage.message);
                binding.textDateTime.setText(chatMessage.dateTime);
                binding.chatImage.setVisibility(View.GONE);
                binding.textDateTime1.setVisibility(View.GONE);
            } else {
                binding.chatImage.setImageBitmap(getChatImage(chatMessage.chatImage));
                binding.textDateTime1.setText(chatMessage.dateTime);
                binding.textMessage.setVisibility(View.GONE);
                binding.textDateTime.setVisibility(View.GONE);

            }
        }
    }


    private static Bitmap getChatImage(String encodedChatImage) {
        byte[] bytes = Base64.decode(encodedChatImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

    }

    static class ReceiveMessageViewHolder extends RecyclerView.ViewHolder {
        private final ItemContainerReceiveMessageBinding binding;

        ReceiveMessageViewHolder(ItemContainerReceiveMessageBinding itemContainerReceiveMessageBinding) {
            super(itemContainerReceiveMessageBinding.getRoot());
            binding = itemContainerReceiveMessageBinding;

        }

        void setData(ChatMessage chatMessage, Bitmap receiverProfileImage) {

            {

                if (chatMessage.chatImage == null) {
                    binding.textMessage.setText(chatMessage.message);
                    binding.textDateTime.setText(chatMessage.dateTime);
                    binding.chatImage.setVisibility(View.GONE);
                    binding.textDateTime1.setVisibility(View.GONE);
                } else {
                    binding.chatImage.setImageBitmap(getChatImage(chatMessage.chatImage));
                    binding.textDateTime1.setText(chatMessage.dateTime);
                    binding.textMessage.setVisibility(View.GONE);
                    binding.textDateTime.setVisibility(View.GONE);
                }

                if (receiverProfileImage != null) {
                    binding.imageProfile.setImageBitmap(receiverProfileImage);
                }
            }

        }
    }
}
