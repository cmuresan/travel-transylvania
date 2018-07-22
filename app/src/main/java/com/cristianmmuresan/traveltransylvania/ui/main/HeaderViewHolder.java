package com.cristianmmuresan.traveltransylvania.ui.main;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cristianmmuresan.traveltransylvania.databinding.ItemHeaderBinding;

class HeaderViewHolder extends RecyclerView.ViewHolder {
    private ItemHeaderBinding binding;

    HeaderViewHolder(View viewHeader) {
        super(viewHeader);
        binding = DataBindingUtil.bind(viewHeader);
    }

    void bindData(String header) {
        binding.header.setText(header);
    }
}
