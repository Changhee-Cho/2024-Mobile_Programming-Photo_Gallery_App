package com.example.homework2.ui.slideshow;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.homework2.R;

import java.util.ArrayList;

public class SlideshowFragment extends Fragment {
    ViewPager pager;
    ImageView[] images = new ImageView[30];
    EditText editText;

    public SlideshowFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_slideshow, container, false);
        editText = rootView.findViewById(R.id.editTextText);

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener(){
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                int number = Integer.parseInt(editText.getText().toString());
                if (number < 1 || number > 30) {
                    Toast.makeText(getContext(), "Please enter a digit number between 1 ~ 30", Toast.LENGTH_SHORT).show();
                    editText.setText("");
                }
                else {
                    pager.setCurrentItem(number - 1);  // number - 1로 설정해야 페이지 번호가 맞음
                }
                return true;
            }
        });

        pager = rootView.findViewById(R.id.pager);
        pager.setOffscreenPageLimit(30);

        MyPagerAdapter adapter = new MyPagerAdapter();

        // 30개의 이미지를 배열에 추가
        for (int i = 0; i < 30; i++) {
            int drawable_id = getResources().getIdentifier("image" + (i + 1), "drawable", getContext().getPackageName());
            ImageView imageView = new ImageView(getContext());
            imageView.setImageResource(drawable_id);
            images[i] = imageView;
            adapter.addItem(imageView);
        }

        pager.setAdapter(adapter);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                editText.setText(String.valueOf(position + 1));  // 페이지 번호 업데이트
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        pager.setCurrentItem(0);  // 첫 번째 페이지로 설정

        editText.setText("1");  // 처음에 1로 설정

        return rootView;
    }

    // 뷰페이저에 프래그먼트 30개를 만들어서 적용했더니 다른 메뉴 이동 후 다시 돌아올 때 메모리 누수로 프로그램이 튕기는 현상이 있었음. 따라서 이미지 뷰로 대체
    class MyPagerAdapter extends PagerAdapter {
        ArrayList<ImageView> items = new ArrayList<>();

        public void addItem(ImageView item) {
            items.add(item);
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = items.get(position);
            container.addView(imageView);
            return imageView;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }
}

// 뷰페이저에 이미지뷰를 적용하는 것이 아닌, 30개의 프래그먼트 객체 생성 후 ViewPager에 적용하는 경우
// 이 경우 ImageView를 xml에 정의한 ImageFragment 이름의 프래그먼트가 정의되어 있어야 함.
// 단 이 경우, 초기 실행은 잘 되나, 메모리 누수로 인해 재진입 시 프로그램 강제 종료 현상 발생
/*
public class SlideshowFragment extends Fragment {
    ViewPager pager;
    ImageFragment[] imageFragment = new ImageFragment[30];
    EditText editText;

    public SlideshowFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_slideshow, container, false);
        editText = rootView.findViewById(R.id.editTextText);

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener(){
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                int number = Integer.parseInt(editText.getText().toString());
                if (number < 1 || number > 30) {
                    Toast.makeText(getContext(), "Please enter a digit number between 1 ~ 30", Toast.LENGTH_SHORT).show();
                }
                else {
                    pager.setCurrentItem(number - 1);  // number - 1로 설정해야 페이지 번호가 맞음
                }
                return true;
            }
        });

        pager = rootView.findViewById(R.id.pager);
        pager.setOffscreenPageLimit(30);

        MyPagerAdapter adapter = new MyPagerAdapter(getChildFragmentManager());

        // 30개의 이미지를 배열에 추가
        for (int i = 0; i < 30; i++) {
            int drawable_id = getResources().getIdentifier("image" + (i + 1), "drawable", getContext().getPackageName());
            imageFragment[i] = new ImageFragment(drawable_id); // 이미지를 직접 전달
            adapter.addItem(imageFragment[i]);  // 어댑터에 추가
        }

        pager.setAdapter(adapter);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                editText.setText(String.valueOf(position + 1));  // 페이지 번호 업데이트
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        pager.setCurrentItem(0);  // 첫 번째 페이지로 설정

        editText.setText("1");  // 처음에 1로 설정

        return rootView;
    }

    class MyPagerAdapter extends FragmentStatePagerAdapter {
        ArrayList<Fragment> items = new ArrayList<Fragment>();

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return items.get(position);
        }

        public void addItem(Fragment item) {
            items.add(item);
        }

        @Override
        public int getCount() {
            return items.size();
        }
    }
}

 */