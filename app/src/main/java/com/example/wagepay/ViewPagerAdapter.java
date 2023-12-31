package com.example.wagepay;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class ViewPagerAdapter extends PagerAdapter {

    Context context;

    int image[]={

            R.drawable.worker,
            R.drawable.calculation,
            R.drawable.summary,
            R.drawable.phone

    };

    int headings[]={
            R.string.heading1,
            R.string.heading2,
            R.string.heading3,
            R.string.heading4
    };

    int description[]={
            R.string.des1,
            R.string.des2,
            R.string.des3,
            R.string.des4
    };

    public ViewPagerAdapter(Context context){

        this.context = context;
    }
    @Override
    public int getCount() {
        return headings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (LinearLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View  view = layoutInflater.inflate(R.layout.slider_layout,container,false);

        ImageView slideTitleImage = (ImageView) view.findViewById(R.id.titleImage);
        TextView slideHeading = (TextView) view.findViewById(R.id.texttitle);
        TextView slideDescription = (TextView) view.findViewById(R.id.textdescription);

        slideTitleImage.setImageResource(image[position]);
        slideHeading.setText(headings[position]);
        slideDescription.setText(description[position]);

        container.addView(view);

        return view;

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
      container.removeView((LinearLayout)object);
    }
}
