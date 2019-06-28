package com.twoam.cartello.Utilities.Base;

import android.view.View;

public interface OnItemClick {

    void onRemoveClicked(int position);

    void onItemClicked(int position);

    void onItemClicked(int position, int type);

    void onItemLLongClicked(int position);

    void onPostLikeClicked(int position);

    void onPostRepostClicked(int position);

    void onPostCommentClicked(int position);

    void onOptionClicked(int position, View view);

    void onChoiceSelected(int position, long optionId);

    void onUnlockPollClicked(int position);


}
