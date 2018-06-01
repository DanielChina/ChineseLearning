package test.zx.learnwords;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ViewPagerFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ViewPagerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewPagerFragment extends Fragment {
    protected View[] fragmentView=new View[3];
    private   MainActivity activity;
    private MyGridView myGridView;
    private DBInfo dbInfo;
    private DateConfirm dateConfirm;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ViewPagerFragment() {
        dateConfirm=new DateConfirm();
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ViewPagerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ViewPagerFragment newInstance(String param1, String param2) {
        ViewPagerFragment fragment = new ViewPagerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null){
            String FRAGMENTS_TAG = "android:support:fragments";
            savedInstanceState.remove(FRAGMENTS_TAG);
        }
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view;
        view=inflater.inflate(R.layout.fragment_view_pager, container, false);
        super.onViewCreated(view, savedInstanceState);
        ViewPager mViewPager=iniViewPager(inflater,view);
        setListener(mViewPager);
        myGridView=new MyGridView(inflater,fragmentView,this.getActivity(),activity);
        myGridView.iniGridView();
        myGridView.setGridViewListener();
        NewWordsItem newWordsItem=new NewWordsItem(myGridView,fragmentView[Constant.NEW_WORDS],activity);
        newWordsItem.iniNewWordsComponent();
        
       //this.getActivity()获得fragment对应的activity
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if(mListener != null){
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {

        super.onAttach(context);
        activity=(MainActivity)context;
        if(context instanceof OnFragmentInteractionListener){
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    public class MyPagerAdapter extends PagerAdapter{
        private List<View> views;
        private List<String> titleList;

        public MyPagerAdapter(List<View> views) {
            this.views = views;
        }

        public int getCount() {
            return views.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            boolean newDate=false;
            View view = views.get(position);
            container.addView(view);
            if(position==Constant.NEW_WORDS && activity.getDbInfo().getDataBase()
                                               .getNewWords().getWordsNum()<Constant.MIN_WORDS_EACH_DAY){
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(activity, "每天至少学习5个生字", Toast.LENGTH_SHORT).show();
                    }
                });

            }
            dbInfo=activity.getDbInfo();
            newDate=dateConfirm.checkIfNewDate(dbInfo.getDataBase().getNewWords().getTime());
            if(newDate){
                dbInfo.clearAllNewWordsAndSetNewTime();
                myGridView.emptyNewWordsGrid();
                myGridView.increaseNewWords(dbInfo);
            }
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(views.get(position));
        }

        @Override
        public CharSequence getPageTitle(int position) {
            titleList = new ArrayList<String>();
            titleList.add("每日任务");
            titleList.add("已学");
            titleList.add("测试记录");
            return titleList.get(position);
        }
    }
    public ViewPager iniViewPager(LayoutInflater inflater, View view) {
        List<View> views = new ArrayList<>();
        fragmentView[Constant.HISTORIC_TEST] = inflater.inflate(R.layout.fragment_historic_test, null);
        fragmentView[Constant.HISTORIC_WORDS] = inflater.inflate(R.layout.fragment_historic_words, null);
        fragmentView[Constant.NEW_WORDS] = inflater.inflate(R.layout.fragment_new_words, null);
        for(int i=0;i<3;i++)
            views.add(fragmentView[i]);
        ViewPager mViewPager = (ViewPager) view.findViewById(R.id.view_pager);
        PagerAdapter PagerAdapter = new MyPagerAdapter(views);
        mViewPager.setAdapter(PagerAdapter);
        TabLayout mTabLayout=(TabLayout) view.findViewById(R.id.viewpager_title);
        mTabLayout.setSelectedTabIndicatorColor(getResources().getColor(android.R.color.white));
        return mViewPager;
    }
    void setListener(ViewPager mViewPager){
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    public MyGridView getMyGridView() {
        return myGridView;
    }

    public void setMyGridView(MyGridView myGridView) {
        this.myGridView = myGridView;
    }
}
