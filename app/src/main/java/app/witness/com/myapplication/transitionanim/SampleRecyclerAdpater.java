/*
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package app.witness.com.myapplication.transitionanim;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import app.witness.com.myapplication.databinding.ItemTransitionLayoutBinding;

public class SampleRecyclerAdpater extends RecyclerView.Adapter<SampleRecyclerAdpater.SamplesViewHolder> {
    private Activity activity;
    private List<Sample> samples;

    public SampleRecyclerAdpater(Activity activity, List<Sample> samples) {
        this.activity = activity;
        this.samples = samples;
    }

    @Override
    public SamplesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemTransitionLayoutBinding itemTransitonLayoutBinding = ItemTransitionLayoutBinding.inflate(LayoutInflater.from(activity), parent, false);
        return new SamplesViewHolder(itemTransitonLayoutBinding.getRoot());
    }

    @Override
    public void onBindViewHolder(SamplesViewHolder holder, final int position) {
        Sample sample = samples.get(position);
        holder.layoutBinding.setSample(sample);
        holder.layoutBinding.sampleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (position){
                    case 0:

                        break;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return samples.size();
    }

    public class SamplesViewHolder extends RecyclerView.ViewHolder {

        ItemTransitionLayoutBinding layoutBinding;

        public SamplesViewHolder(View view) {
            super(view);
            layoutBinding = DataBindingUtil.bind(view);
        }
    }
}
